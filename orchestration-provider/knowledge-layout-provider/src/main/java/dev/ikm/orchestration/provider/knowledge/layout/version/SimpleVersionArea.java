package dev.ikm.orchestration.provider.knowledge.layout.version;

import dev.ikm.komet.framework.observable.*;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.version.KlGenericVersionArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.area.SupplementalArea;
import dev.ikm.orchestration.provider.knowledge.layout.feature.simple.GenericFieldArea;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.AreaBlueprint;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.AreaLayoutPropertySheet;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.RowIncrementLayoutComputer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.block.procedure.SumOfByteProcedure;

import java.util.Optional;

import static javafx.stage.StageStyle.UTILITY;

public class SimpleVersionArea extends AreaBlueprint<BorderPane>
        implements KlGenericVersionArea<BorderPane> {

    private final SimpleObjectProperty<ObservableVersion> versionProperty = new SimpleObjectProperty<>();
    {
        versionProperty.subscribe(this::versionChanged);
    }

    MutableList<GenericFieldArea> simpleGenericFieldPanes = Lists.mutable.empty();

    private final GridPane gridPane = new GridPane();

    public SimpleVersionArea(KometPreferences preferences) {
        super(preferences, new BorderPane());
        setup();
    }

    public SimpleVersionArea(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory, new BorderPane());
        setup();
    }

    private void setup() {
        fxObject().setCenter(gridPane);
    }

    @Override
    public GridPane gridPaneForChildren() {
        return gridPane;
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
        if (version != null) {
            ImmutableList<Feature> versionFeatures = version.getFeatures(calculatorForContext());
            RowIncrementLayoutComputer rowIncrementLayoutComputer = RowIncrementLayoutComputer.create(getMasterLayout());
            rowIncrementLayoutComputer.layout(versionFeatures, getLayoutKeyForArea().makeAreaKeyProvider())
                    .forEach(layoutElement -> {
                        var klArea = layoutElement.areaGridSettings().makeAndAddToParent(this);
                    });
        }
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
        for (GenericFieldArea simpleGenericFieldPane : simpleGenericFieldPanes) {
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
    private MenuItem makeVersionLayoutMenu(GenericFieldArea simpleGenericFieldPane) {
         MenuItem editGridLayout = new MenuItem("Edit grid layout for: " +
                 context().viewCoordinate().calculator().getPreferredDescriptionStringOrNid(simpleGenericFieldPane.getField().meaningNid()) + "");
        editGridLayout.setOnAction(event -> {
            AreaLayoutPropertySheet areaLayoutPropertySheet = new AreaLayoutPropertySheet(simpleGenericFieldPane);
            Stage stage = new Stage(UTILITY);
            stage.setTitle("Edit grid layout for: " + context().viewCoordinate().getDescriptionTextOrNid(simpleGenericFieldPane.getField().meaningNid()));
            Scene scene = new Scene(new VBox(areaLayoutPropertySheet.getPropertySheet()));
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


    public static SimpleVersionArea.Factory factory() {
        return new SimpleVersionArea.Factory();
    }

    public static SimpleVersionArea restore(KometPreferences preferences) {
        return factory().restore(preferences);
    }

    public static class Factory implements KlGenericVersionArea.Factory<BorderPane, SimpleVersionArea> {

        @Override
        public SimpleVersionArea restore(KometPreferences preferences) {
            return new SimpleVersionArea(preferences);
        }

        @Override
        public SimpleVersionArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            SimpleVersionArea simpleVersionArea = new SimpleVersionArea(preferencesFactory, this);
            simpleVersionArea.setGridLayout(areaGridSettings);
            return simpleVersionArea;
        }
    }
    
}
