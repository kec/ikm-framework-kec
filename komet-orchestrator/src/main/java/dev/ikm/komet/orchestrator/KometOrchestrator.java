package dev.ikm.komet.orchestrator;

import dev.ikm.komet.framework.KometNode;
import dev.ikm.komet.framework.events.EvtBus;
import dev.ikm.komet.framework.events.EvtBusFactory;
import dev.ikm.komet.framework.events.Subscriber;
import dev.ikm.komet.framework.preferences.PrefX;
import dev.ikm.komet.kview.events.CreateJournalEvent;
import dev.ikm.komet.kview.mvvm.view.journal.JournalController;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.komet.preferences.Preferences;
import dev.ikm.komet.preferences.PreferencesService;
import dev.ikm.komet.progress.CompletionNodeFactory;
import dev.ikm.komet.progress.ProgressNodeFactory;
import dev.ikm.orchestration.interfaces.Lifecycle;
import dev.ikm.orchestration.interfaces.OrchestrationService;
import dev.ikm.orchestration.interfaces.StatusReportService;
import dev.ikm.orchestration.interfaces.changeset.ChangeSetWriterService;
import dev.ikm.orchestration.interfaces.data.SelectDataService;
import dev.ikm.orchestration.interfaces.data.StartDataService;
import dev.ikm.orchestration.interfaces.journal.NewJournalService;
import dev.ikm.orchestration.interfaces.menu.MenuService;
import dev.ikm.orchestration.interfaces.menu.WindowMenuService;
import dev.ikm.plugin.layer.IkmServiceManager;
import dev.ikm.tinkar.common.service.PluggableService;
import dev.ikm.tinkar.common.service.PrimitiveData;
import dev.ikm.tinkar.common.service.TinkExecutor;
import dev.ikm.tinkar.common.util.time.DateTimeUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.eclipse.collections.api.multimap.ImmutableMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutionException;

import static dev.ikm.komet.kview.events.EventTopics.JOURNAL_TOPIC;
import static dev.ikm.komet.preferences.JournalWindowSettings.JOURNAL_TITLE;
import static dev.ikm.orchestration.interfaces.Lifecycle.*;

/**
 * The KometOrchestrator class is responsible for orchestrating the Komet application.
 * It extends the Application class and implements the OrchestrationService interface.
 */
public class KometOrchestrator extends Application implements OrchestrationService {
    private static final Logger LOG = LoggerFactory.getLogger(KometOrchestrator.class);
    public static final SimpleObjectProperty<Lifecycle> state = new SimpleObjectProperty<>(STARTING);
    private static Optional<String> optionalLastRun;
    private Stage primaryStage;
    protected static KometOrchestrator kometOrchestrator;
    private TextField statusTextField = new TextField("Status");

    private EvtBus kViewEventBus;
    // TODO: Do we need this journal controllers list, and how does it relate to save state and window manager.
    private List<JournalController> journalControllersList = new ArrayList<>();


    /**
     * Returns a {@link StatusReportService} provider. The provider creates an instance of a {@link StatusReportService}
     * that reports the status by calling the {@link KometOrchestrator#reportStatus(String)} method from the
     * {@link KometOrchestrator} class.
     *
     * @return a provider for {@link StatusReportService}
     */
    public static StatusReportService provider() {
        return (String status) -> KometOrchestrator.kometOrchestrator.reportStatus(status);
    }

    /**
     * The KometOrchestrator class represents an orchestrator that manages the lifecycle and execution flow
     * of a Komet application. It implements the OrchestrationService interface, which defines the necessary methods
     * for an orchestrator. The KometOrchestrator class is responsible for handling the primary stage, status reporting,
     * and managing the application state.
     *
     * This class contains a constructor that initializes the KometOrchestrator instance. It sets the static field
     * kometOrchestrator to refer to the current instance.
     */
    public KometOrchestrator() {
        KometOrchestrator.kometOrchestrator = this;
    }

    /**
     * Reports the status by logging the message and updating the statusTextField on the UI thread.
     *
     * @param message the status message to report
     */
    private void reportStatus(String message) {
        LOG.info(message);
        Platform.runLater(() -> statusTextField.setText(message));
    }

    /**
     * Returns the primary stage of the application.
     *
     * @return the primary stage
     */
    @Override
    public Stage primaryStage() {
        return primaryStage;
    }

    /**
     * Returns the lifecycle property of the KometOrchestrator.
     *
     * @return the lifecycle property
     */
    @Override
    public SimpleObjectProperty<Lifecycle> lifecycleProperty() {
        return state;
    }

    /**
     * This method is the start method of the KometOrchestrator class. It is overridden from the Application class.
     * The start method initializes the primaryStage, sets the lifecycle property, and adds a listener to the state property.
     * Inside the listener, it executes a selectDataServiceTask using a thread from the TinkExecutor thread pool.
     * The selectDataServiceTask is obtained from the ServiceLoader of SelectDataService class, and if present, it is executed.
     * Otherwise, an IllegalStateException is thrown.
     *
     * @param primaryStage the primary stage of the application
     * @throws IOException if an I/O error occurs during execution of the selectDataServiceTask
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        lifecycleProperty().set(SELECT_DATA_SOURCE);
        state.addListener(this::appStateChangeListener);

        TinkExecutor.threadPool().execute(() -> {
            ServiceLoader<SelectDataService> dataServiceControllers = PluggableService.load(SelectDataService.class);

            dataServiceControllers.findFirst().ifPresentOrElse((SelectDataService selectDataService) -> {
                try {
                    selectDataService.selectDataServiceTask(this).get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }, () -> { throw new IllegalStateException("No SelectDataService found..."); });
        });
        //TODO: should the event bus replace the lifecycle property?
        // get the instance of the event bus
        kViewEventBus = EvtBusFactory.getInstance(EvtBus.class);
        Subscriber<CreateJournalEvent> detailsSubscriber = evt -> {

            String journalName = evt.getWindowSettingsObjectMap().getValue(JOURNAL_TITLE);
            // Inspects the existing journal windows to see if it is already open
            // So that we do not open duplicate journal windows
            journalControllersList.stream()
                    .filter(journalController -> journalController.getTitle().equals(journalName))
                    .findFirst()
                    .ifPresentOrElse(
                            journalController -> journalController.windowToFront(), /* Window already launched now make window to the front (so user sees window) */
                            () -> PluggableService.first(NewJournalService.class).make(PrefX.create(), journalControllersList));
        };
        // subscribe to the topic
        kViewEventBus.subscribe(JOURNAL_TOPIC, CreateJournalEvent.class, detailsSubscriber);

    }

    /**
     * The main method of the KometOrchestrator class. It initializes the Komet application and launches it.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            if (e.getMessage().equals("Cannot invoke \"javafx.scene.Node.getStyleClass()\" because \"this.label\" is null")) {
                LOG.info(e.getMessage());
            } else {
                LOG.error("On thread: " + t, e);
            }
        });

        System.setProperty("apple.laf.useScreenMenuBar", "false");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "KometOrchestrator");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Starting shutdown hook");
            PrimitiveData.stop();
            LOG.info("Finished shutdown hook");
        }));
        // setup plugin layers
        LOG.info("Starting KometOrchestrator");

        // setup plugin directory.
        LOG.info("Application working directory: {}", System.getProperty("user.dir"));

        Path pluginPath = resolvePluginPath();
        pluginPath.toFile().mkdirs();
        IkmServiceManager.setPluginDirectory(pluginPath);

        // Access Preferences
        KometPreferences userPreferences = PreferencesService.get().getUserPreferences();
        KometOrchestrator.optionalLastRun = userPreferences.get(OrchestratorPreferenceKeys.LAST_RUN);
        if (KometOrchestrator.optionalLastRun.isPresent()) {
            LOG.info("Last run: " + userPreferences.get(OrchestratorPreferenceKeys.LAST_RUN));
        } else {
            LOG.info("Last run not set");
        }
        userPreferences.put(OrchestratorPreferenceKeys.LAST_RUN, DateTimeUtil.nowWithZone());
        LOG.info("Update Last run: " + userPreferences.get(OrchestratorPreferenceKeys.LAST_RUN));
        // launch application
        launch();
    }

    /**
     * Provides the path to the plugins directory whether running a local build or as an installed application.
     *
     * @return Path to the plugins directory
     */
    private static Path resolvePluginPath() {
        // Initialize the pluginPath to the installed application plugin directory
        Path pluginPath = Path.of("/").resolve("Applications").resolve("Orchestrator.app")
                .resolve("Contents").resolve("plugins");

        // For local maven builds, use the latest plugin directory expected to exist at the localPluginPath.
        Path localPluginPath = Path.of(System.getProperty("user.dir")).resolve("target").resolve("plugins");
        if (localPluginPath.toFile().exists()) {
            pluginPath = localPluginPath;
        }
        LOG.info("Plugin directory: {}", pluginPath.toAbsolutePath());
        return pluginPath;
    }


    /**
     * This method is responsible for handling changes in the application state. It takes three parameters: `observable`, `oldValue`, and `newValue` which represent an observable value
     *  of type `Lifecycle`, the old value of the lifecycle, and the new value of the lifecycle respectively.
     *
     * The method uses a switch statement to perform different actions based on the new value of the lifecycle.
     *
     * If the new value is `SELECTED_DATA_SOURCE`, the method performs the following actions:
     * - Sets the title of the primary stage to "Komet OrchestrationService"
     * - Creates a new `MenuBar` and adds "File", "Edit", and "Window" menus to it. It then calls the `addMenuItems` method to add additional menu items to the menu bar.
     * - Creates a `TabPane` and a `BorderPane`. The `TabPane` is set as the center of the `BorderPane`, the `MenuBar` is set as the top, and the `statusTextField` is set as the bottom
     *  of the `BorderPane`.
     * - Creates a `ProgressNode` using `ProgressNodeFactory` and adds a new `Tab` containing the `ProgressNode` to the `TabPane`.
     * - Creates a `CompletionNode` using `CompletionNodeFactory` and adds a new `Tab` containing the `CompletionNode` to the `TabPane`.
     * - Selects the first tab in the `TabPane`.
     * - Sets the root of the primary stage's scene to the `BorderPane`.
     * - Executes a task from the `StartDataService` provider obtained from the `ServiceLoader`. The task is executed in a separate thread from the `TinkExecutor` thread pool.
     * - Sets the state to `LOADING_DATA_SOURCE` on the UI thread.
     * - If there is a previous run available, it reports the status as "Last opened: " + the last run.
     *
     * If the new value is `RUNNING`, the method performs the following actions:
     * - Obtains the `ChangeSetWriterService` provider from the `ServiceLoader` and gets the change set folder.
     * - Calls the `quit` method.
     *
     * If the new value is `SHUTDOWN`, the method simply calls the `quit` method.
     *
     * If an exception occurs during the execution of the method, it is printed to the standard error output and the application is terminated.
     *
     * @param observable the observable value of the lifecycle
     * @param oldValue the old value of the lifecycle
     * @param newValue the new value of the lifecycle
     */
    private void appStateChangeListener(ObservableValue<? extends Lifecycle> observable, Lifecycle oldValue, Lifecycle newValue) {
        try {
            switch (newValue) {
                case SELECTED_DATA_SOURCE -> {
                    // Convert the primary stage to a progress node...
                    this.primaryStage.setTitle("Komet OrchestrationService");
                    MenuBar menuBar = new MenuBar();
                    menuBar.getMenus().add(new Menu("File"));
                    menuBar.getMenus().add(new Menu("Edit"));
                    addMenuItems(this.primaryStage, menuBar);

                    TabPane progressTabPane = new TabPane();
                    BorderPane rootBorderPane = new BorderPane();
                    rootBorderPane.setCenter(progressTabPane);
                    rootBorderPane.setTop(menuBar);
                    rootBorderPane.setBottom(statusTextField);
                    //
                    ProgressNodeFactory progressNodeFactory = new ProgressNodeFactory();
                    KometNode progressNode = progressNodeFactory.create();
                    Tab progressTab = new Tab(progressNode.getTitle().getValue(), progressNode.getNode());
                    progressTab.setGraphic(progressNode.getTitleNode());
                    progressTabPane.getTabs().add(progressTab);
                    //
                    CompletionNodeFactory completionNodeFactory = new CompletionNodeFactory();
                    KometNode completionNode = completionNodeFactory.create();
                    Tab completionTab = new Tab(completionNode.getTitle().getValue(), completionNode.getNode());
                    completionTab.setGraphic(completionNode.getTitleNode());
                    progressTabPane.getTabs().add(completionTab);
                    progressTabPane.getSelectionModel().select(0);


                    primaryStage.getScene().setRoot(rootBorderPane);

                    TinkExecutor.threadPool().execute(() -> {
                        ServiceLoader<StartDataService> startDataServiceControllers = PluggableService.load(StartDataService.class);
                        try {
                            startDataServiceControllers.findFirst().get().startDataServiceTask(this).get();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    Platform.runLater(() -> state.set(LOADING_DATA_SOURCE));
                    if (optionalLastRun.isPresent()) {
                        Platform.runLater(() -> reportStatus("Last opened: " + optionalLastRun.get()));
                    }
                }

                case RUNNING -> {
                    ServiceLoader<ChangeSetWriterService> startDataServiceControllers = PluggableService.load(ChangeSetWriterService.class);
                    startDataServiceControllers.findFirst().get().getChangeSetFolder();
                    //primaryStage.hide();
                    //launchLandingPage();
                }
                case SHUTDOWN -> quit();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    /**
     * Adds menu items to the specified menu bar based on the provided MenuService
     * implementation, and then appends a Window menu from the WindowMenuService.
     *
     * @param menuBar the menu bar to add the menu items to
     */
    public void addMenuItems(Stage stage, MenuBar menuBar) {
        // Add menu items...
        ServiceLoader<MenuService> menuProviders = PluggableService.load(MenuService.class);
        menuProviders.forEach(menuProvider -> {
            ImmutableMultimap<String, MenuItem> menuMap = menuProvider.getMenuItems(stage);
            menuMap.forEachKeyValue((menuName, menuItem) -> {
                boolean found = false;
                for (Menu menu: menuBar.getMenus()) {
                    if (menu.getText().equals(menuName)) {
                        menu.getItems().add(menuItem);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Menu menu = new Menu(menuName);
                    menu.getItems().add(menuItem);
                    menuBar.getMenus().add(menu);
                }
            });
        });
        PluggableService.first(WindowMenuService.class).addWindowMenu(stage, menuBar);
    }

    /**
     * Quits the application by stopping necessary services and exiting the platform.
     * TODO: This call will likely be moved into the landing page functionality.
     */
    private void quit() {
        //TODO: that this call will likely be moved into the landing page functionality
        //saveJournalWindowsToPreferences();
        PrimitiveData.stop();
        Preferences.stop();
        Platform.exit();
    }

}