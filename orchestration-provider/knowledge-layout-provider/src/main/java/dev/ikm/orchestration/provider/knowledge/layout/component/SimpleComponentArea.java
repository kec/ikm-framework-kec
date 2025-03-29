package dev.ikm.orchestration.provider.knowledge.layout.component;

import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.component.KlGenericComponentArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.StampVersionListCellFactory;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.WidgetBlueprint;
import dev.ikm.orchestration.provider.knowledge.layout.version.SimpleVersionArea;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.terms.TinkarTerm;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class SimpleComponentArea
        extends WidgetBlueprint<BorderPane>
        implements KlGenericComponentArea<BorderPane> {

    final SimpleObjectProperty<ObservableEntity<ObservableVersion<EntityVersion>>> componentProperty = new SimpleObjectProperty<>();
    private final ListView<ObservableVersion<EntityVersion>> componentVersionsList = new ListView<>();
    private final GridPane gridPane = new GridPane();
    Label label = new Label("Simple Component Area");
    private final ToolBar toolBar = new ToolBar();
    {
        label.setStyle("-fx-rotate: -90;");
        toolBar.getItems().add(new Group(label));
        toolBar.setOrientation(Orientation.VERTICAL);
    }


    SimpleVersionArea simpleVersionArea;


    public SimpleComponentArea(KometPreferences preferences) {
        super(preferences, new BorderPane());
        setup();
    }

    public SimpleComponentArea(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new BorderPane());
        setup();
    }

    @Override
    public ObservableList<ObservableVersion<EntityVersion>> selectedVersions() {
        return componentVersionsList.getSelectionModel().getSelectedItems();
    }

    private void setup() {
        fxObject.setLeft(toolBar);
        componentVersionsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        componentVersionsList.setCellFactory(new StampVersionListCellFactory(this));
        componentVersionsList.setPrefHeight(150);
        fxObject.setCenter(gridPane);
        GridPane.setConstraints(componentVersionsList, 0, 0, 1, 1,
                HPos.LEFT, VPos.TOP, Priority.NEVER, Priority.NEVER, new Insets(5));
        gridPane.getChildren().add(componentVersionsList);
        simpleVersionArea = new SimpleVersionArea(preferences());
        simpleVersionArea.setRowIndex(1);
        simpleVersionArea.setHgrow(Priority.ALWAYS);
        simpleVersionArea.setVgrow(Priority.ALWAYS);
        simpleVersionArea.fxObject().setMaxHeight(Double.MAX_VALUE);
        gridPane.getChildren().add(simpleVersionArea.fxObject());

        simpleVersionArea.versionProperty().set(componentVersionsList.getSelectionModel().getSelectedItem());
        componentProperty.subscribe(observableEntity -> {
            if (observableEntity != null) {
                componentVersionsList.setItems(observableEntity.versionProperty());
            }
        });
        componentVersionsList.getSelectionModel().selectedItemProperty().subscribe(this::versionSelected);
        componentProperty.set(ObservableEntity.get(TinkarTerm.PROCEDURE.nid()));
    }

    private void versionSelected() {
        simpleVersionArea.versionProperty().set(selectedItemProperty().get());
    }

    @Override
    public ObjectProperty<ObservableEntity<ObservableVersion<EntityVersion>>> componentProperty() {
        return componentProperty;
    }

    private ReadOnlyObjectProperty<ObservableVersion<EntityVersion>> selectedItemProperty() {
        return componentVersionsList.getSelectionModel().selectedItemProperty();
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
