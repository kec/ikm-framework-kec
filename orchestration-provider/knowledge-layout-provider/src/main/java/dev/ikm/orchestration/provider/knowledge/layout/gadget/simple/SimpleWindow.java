package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.KlStateCommands;
import dev.ikm.komet.layout.KlViewFactory;
import dev.ikm.komet.layout.context.KlContext;
import dev.ikm.komet.layout.context.KlContextFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.preferences.KlProfiles;
import dev.ikm.komet.layout.window.KlWindowPaneFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.StageBlueprint;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.WindowEvent;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.prefs.BackingStoreException;

public class SimpleWindow extends StageBlueprint {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWindow.class);

    /**
     * Constructs a SimpleWindow instance restoring its state from the specified preferences.
     *
     * @param preferences the user preferences object used to store and retrieve
     *                    properties related to the window, such as layout settings,
     *                    visibility, and other configurations for this window instance.
     */
    public SimpleWindow(KometPreferences preferences) {
        super(preferences);
    }

    /**
     * Constructs a SimpleWindow instance with the specified factories for preferences, view,
     * and window pane components. This constructor initializes the window by setting up the
     * required components using the provided factories.
     *
     * @param windowPreferencesFactory the factory used to create preferences for the window.
     * @param thisFactory the factory responsible for creating components specific to this window.
     * @param viewFactory the factory used to generate the view to be displayed in the window.
     * @param windowPaneFactory the factory used to create the window pane embedded within the window.
     */
    public SimpleWindow(KlPreferencesFactory windowPreferencesFactory, KlFactory thisFactory,
                        KlViewFactory viewFactory, SimpleWindowPaneFactory windowPaneFactory, KlContextFactory contextFactory) {
        super(windowPreferencesFactory, thisFactory, viewFactory, windowPaneFactory, contextFactory);
    }


    /**
     * Handles the close request of the window by presenting the user with a choice dialog
     * to determine how to proceed based on the current state of the window. If there are
     * unsaved changes, users are presented with multiple options such as canceling the
     * close action, saving the current state, deleting the window, or reverting changes.
     *
     * The method ensures that user preferences and the state of the window are updated
     * or reverted based on the selected action. If an unexpected error occurs during
     * preference updates, an exception is thrown.
     *
     * @param windowEvent the {@link WindowEvent} associated with the window close request.
     *                    This event may be consumed to cancel the close action under certain conditions.
     */
    protected void onCloseRequest(WindowEvent windowEvent) {
        try {
            if (changedProperty().getValue()) {
                ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Delete window", "Cancel",
                        "Delete window", "Save window and close", "Save window and keep open", "Save as layout", "Revert window");
                choiceDialog.showAndWait();
                switch (choiceDialog.getResult()) {
                    case null -> windowEvent.consume();
                    case "Cancel" -> windowEvent.consume();
                    case "Delete window" -> delete();
                    case "Save window and close" -> findKlStateCommandPeers().forEach(KlStateCommands::save);
                    case "Save window and keep open" -> {
                        findKlStateCommandPeers().forEach(KlStateCommands::save);
                        windowEvent.consume();
                    }
                    case "Save as layout" -> {
                        preferences().sync();
                        preferences().copyThisSubtreeTo(KlProfiles.sharedLayoutPreferences(), true);
                        preferences().removeNode();
                        preferences().flush();
                        windowEvent.consume();
                    }
                    case "Revert window" -> findKlStateCommandPeers().forEach(KlStateCommands::revert);
                    default -> LOG.error("Unexpected choice dialog result: {}", choiceDialog.getResult());
                }
            }
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subStageSave() {
        // Nothing to do.

    }

    @Override
    protected void subStageRevert() {
        // Call revert for each KlStateCommands peer object in the Fx hierarchy.

    }


    /**
     * Finds and collects instances of {@link KlStateCommands} within the JavaFX hierarchy associated with the
     * current context. The method identifies eligible peers based on specific criteria and returns them as
     * an immutable list.
     *
     * @return an immutable list of {@link KlStateCommands} objects that meet the specified criteria.
     */
    ImmutableList<KlStateCommands> findKlStateCommandPeers() {
        return findPeers(object -> {
            if (object != null && object instanceof KlStateCommands commandsPeer) {
                return Optional.of(commandsPeer);
            }
            return Optional.empty();
        });
    }

    @Override
    public void unsubscribeFromContext() {
        LOG.info("Implement unsubscribeFromContext");
    }

    @Override
    public void subscribeToContext() {
        LOG.info("Implement subscribeToContext");
    }

}
