package dev.ikm.orchestration.provider.knowledge.layout.version;

import dev.ikm.komet.framework.observable.*;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.version.KlGenericVersionArea;
import dev.ikm.komet.layout.preferences.KlPreferenceFactoryProvider.PreferenceFactoryWithParentPreferences;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.GenericAttribute;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.GenericAttribute.Factory;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.WidgetBlueprint;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.GadgetLayoutPropertySheet;
import dev.ikm.tinkar.coordinate.stamp.calculator.Latest;
import dev.ikm.tinkar.entity.PatternEntityVersion;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import java.util.concurrent.atomic.AtomicInteger;

import static javafx.stage.StageStyle.UTILITY;

public class SimpleVersionArea extends WidgetBlueprint<BorderPane> implements KlGenericVersionArea<BorderPane> {

    private final SimpleObjectProperty<ObservableVersion> versionProperty = new SimpleObjectProperty<>();
    {
        versionProperty.subscribe(this::versionChanged);
    }

    MutableList<GenericAttribute> simpleGenericFieldPanes = Lists.mutable.empty();

    private final GridPane gridPane = new GridPane();

    public SimpleVersionArea(KometPreferences preferences) {
        super(preferences, new BorderPane());
        setup();
    }

    public SimpleVersionArea(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new BorderPane());
        setup();
    }

    private void setup() {
        fxObject.setCenter(gridPane);
    }


    /**
     * Handles changes to the version property by reconfiguring the grid and its contents
     * based on the newly selected version. Clears the existing grid, determines the type
     * of version, and adds the appropriate fields and components to the grid.
     * <p>
     * When the version is an instance of {@code ObservableSemanticVersion}, its fields
     * are populated based on the latest {@code PatternEntityVersion}. Fields are added
     * using the provided factory and preference classes, and additional stamp field
     * information is also added to the grid.
     * <p>
     * When the version is an instance of {@code ObservableVersion}, only the
     * stamp fields are added to the grid.
     * <p>
     * If the version is null, the method does nothing.
     * <p>
     * Finally, a bottom filler is added to the grid to occupy any remaining space
     * and maintain layout consistency.
     */
    private void versionChanged() {
        gridPane.getChildren().clear();
        simpleGenericFieldPanes.clear();
        ObservableVersion version = versionProperty.get();
        switch (version) {
            case ObservableSemanticVersion semanticVersion -> {
                Latest<PatternEntityVersion> latestPatternEntityVersion = context().viewCoordinate().calculator().latestPatternEntityVersion(semanticVersion.patternNid());
                if (latestPatternEntityVersion.isPresent()) {
                    ImmutableList<ObservableField> fields = semanticVersion.fields(latestPatternEntityVersion.get());
                    for (ObservableField semanticField : fields) {
                        addFieldToGrid(new Factory(), new PreferenceFactoryWithParentPreferences(preferences(), Factory.class), 0, 0, semanticField);
                    }
                    addStampFields(semanticVersion.stampNid(), new Factory(), new PreferenceFactoryWithParentPreferences(preferences(), Factory.class), new AtomicInteger(0), new AtomicInteger(1));
                }
            }
            case ObservableConceptVersion conceptVersion -> {

            }
            case ObservablePatternVersion patternVersion -> {

            }
            case ObservableStampVersion stampVersion -> {

            }
        }
        PreferenceFactoryWithParentPreferences preferenceFactoryWithParentPreferences =
                new PreferenceFactoryWithParentPreferences(preferences(), Factory.class);
        Factory simpleGenericFieldPaneFactory = new Factory();
        AtomicInteger col = new AtomicInteger(0);
        AtomicInteger row = new AtomicInteger(0);
        switch (version) {
            case ObservableSemanticVersion semanticVersion -> {
                Latest<PatternEntityVersion> latestPatternEntityVersion = context().viewCoordinate().calculator().latestPatternEntityVersion(semanticVersion.patternNid());
                if (latestPatternEntityVersion.isPresent()) {
                    ImmutableList<ObservableField> fields = semanticVersion.fields(latestPatternEntityVersion.get());
                    for (ObservableField semanticField : fields) {
                        addFieldToGrid(simpleGenericFieldPaneFactory, preferenceFactoryWithParentPreferences, col.get(), row.getAndIncrement(), semanticField);
                    }
                    addStampFields(semanticVersion.stampNid(), simpleGenericFieldPaneFactory, preferenceFactoryWithParentPreferences, row, col);
                }
            }
            case ObservableVersion observableVersion -> {
                addStampFields(observableVersion.stampNid(), simpleGenericFieldPaneFactory, preferenceFactoryWithParentPreferences, row, col);
            }
            case null -> {}
        }
        addBottomFillerToGrid(col.get(), row.getAndIncrement());
    }

    /**
     * Adds stamp fields to the grid for display and interaction. Stamp fields are created from the
     * provided stamp identifier and placed sequentially in the grid. The fields are added using
     * the given factories and row/column counters, which are updated as fields are added.
     *
     * @param stampNid the identifier of the stamp used to retrieve the fields to be added.
     * @param simpleGenericFieldPaneFactory the factory used to create {@code SimpleGenericFieldPane} instances for each field.
     * @param preferenceFactoryWithParentPreferences the preference factory for handling preferences with parent-level support.
     * @param row an atomic integer representing the current row index in the grid. It is incremented as fields are added.
     * @param col an atomic integer representing the current column index in the grid.
     */
    private void addStampFields(int stampNid, Factory simpleGenericFieldPaneFactory,
                                PreferenceFactoryWithParentPreferences preferenceFactoryWithParentPreferences, AtomicInteger row, AtomicInteger col) {
        ObservableStamp observableStamp = ObservableStamp.get(stampNid);
        ObservableStampVersion latestStamp = observableStamp.lastVersion();
        for (ObservableField stampField : latestStamp.fields()) {
            addFieldToGrid(simpleGenericFieldPaneFactory, preferenceFactoryWithParentPreferences, col.get(), row.getAndIncrement(), stampField);
        }
    }

    /**
     * Adds a {@code SimpleGenericFieldPane} to the specified location in the grid. The field pane
     * is created using the provided factory and is associated with the given semantic field.
     * Once added, the field pane is configured and subscribed to its context.
     *
     * @param simpleGenericFieldPaneFactory the factory used to create a {@code SimpleGenericFieldPane}.
     * @param preferenceFactoryWithParentPreferences the preference factory for setting preferences
     *                                               with parent support.
     * @param col the column index in the grid where the field pane will be placed.
     * @param row the row index in the grid where the field pane will be placed.
     * @param semanticField the {@code ObservableField} to be set in the created field pane.
     */
    private void addFieldToGrid(Factory simpleGenericFieldPaneFactory, PreferenceFactoryWithParentPreferences preferenceFactoryWithParentPreferences,
                                int col, int row, ObservableField semanticField) {
        GenericAttribute genericFieldPane = simpleGenericFieldPaneFactory.create(preferenceFactoryWithParentPreferences);
        simpleGenericFieldPanes.add(genericFieldPane);
        genericFieldPane.setHgrow(Priority.ALWAYS);
        genericFieldPane.setVgrow(Priority.NEVER);
        gridPane.add(genericFieldPane.fxObject(), col, row);
        genericFieldPane.setField(semanticField);
        genericFieldPane.subscribeToContext();
    }

    /**
     * Adds a filler label to the specified position in the grid to occupy remaining space.
     * The filler spans the maximum available width and height, ensuring the grid layout
     * remains balanced and visually appealing. The label created for the filler also includes
     * a context menu for user interactions.
     *
     * @param col the column index in the grid where the filler label will be added
     * @param row the row index in the grid where the filler label will be added
     */
    private void addBottomFillerToGrid(int col, int row) {
        Label fillerLabel = new Label("");
        fillerLabel.setMaxWidth(Double.MAX_VALUE);
        fillerLabel.setMaxHeight(Double.MAX_VALUE);
        GridPane.setHgrow(fillerLabel, Priority.ALWAYS);
        GridPane.setVgrow(fillerLabel, Priority.ALWAYS);
        gridPane.add(fillerLabel, col, row);
        fillerLabel.setContextMenu(makeContextMenu());
    }

    /**
     * Creates and returns a context menu populated with menu items for each {@code SimpleGenericFieldPane}
     * in the {@code simpleGenericFieldPanes} collection. Each menu item is created using the
     * {@code makeVersionLayoutMenu} method, which provides functionality for editing the grid layout
     * associated with the respective field pane.
     *
     * @return a {@code ContextMenu} containing menu items for editing the grid layout of
     *         all {@code SimpleGenericFieldPane} instances in the collection
     */
    ContextMenu makeContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        for (GenericAttribute simpleGenericFieldPane : simpleGenericFieldPanes) {
            contextMenu.getItems().add(makeVersionLayoutMenu(simpleGenericFieldPane));
        }
        return contextMenu;
    }

    /**
     * Creates and returns a menu item for editing the grid layout of the provided {@code SimpleGenericFieldPane}.
     * The menu item allows the user to open a new window for configuring the grid layout associated with the field pane.
     *
     * @param simpleGenericFieldPane the {@code SimpleGenericFieldPane} for which the grid layout edit menu item is created
     * @return a {@code MenuItem} configured to launch the grid layout editor for the specified {@code SimpleGenericFieldPane}
     */
    private MenuItem makeVersionLayoutMenu(GenericAttribute simpleGenericFieldPane) {
         MenuItem editGridLayout = new MenuItem("Edit grid layout for: " +
                 context().viewCoordinate().calculator().getPreferredDescriptionStringOrNid(simpleGenericFieldPane.getField().meaningNid()) + "");
        editGridLayout.setOnAction(event -> {
            GadgetLayoutPropertySheet gadgetLayoutPropertySheet = new GadgetLayoutPropertySheet(simpleGenericFieldPane);
            Stage stage = new Stage(UTILITY);
            stage.setTitle("Edit grid layout for: " + context().viewCoordinate().getDescriptionTextOrNid(simpleGenericFieldPane.getField().meaningNid()));
            Scene scene = new Scene(new VBox(gadgetLayoutPropertySheet.getPropertySheet()));
            stage.setScene(scene);
            stage.show();
        });
        return editGridLayout;
    }

    @Override
    public ObjectProperty<ObservableVersion> versionProperty() {
        return versionProperty;
    }

    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }

    @Override
    public void subscribeToContext() {

    }
}
