package dev.ikm.orchestration.provider.knowledge.layout.component;

import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.component.KlGenericComponentPane;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.WidgetBlueprint;
import dev.ikm.orchestration.provider.knowledge.layout.version.SimpleVersionPane;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.terms.TinkarTerm;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class SimpleComponentPane extends WidgetBlueprint<BorderPane> implements KlGenericComponentPane<BorderPane> {

    SimpleObjectProperty<ObservableEntity> componentProperty = new SimpleObjectProperty<>();
    SimpleVersionPane simpleVersionPane;

    private final ListView<ObservableVersion<EntityVersion>> componentVersionsList = new ListView<>();

    public SimpleComponentPane(KometPreferences preferences) {
        super(preferences, new BorderPane());
        setup();
    }

    public SimpleComponentPane(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new BorderPane());
        setup();
    }

    private void setup() {
        componentVersionsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        componentVersionsList.setCellFactory(new StampListFactory());
        fxGadget.setTop(componentVersionsList);
        this.simpleVersionPane = new SimpleVersionPane(preferences());
        simpleVersionPane.versionProperty().set(componentVersionsList.getSelectionModel().getSelectedItem());
        fxGadget.setCenter(simpleVersionPane.fxGadget());
        componentProperty.subscribe(observableEntity -> {
            if (observableEntity != null) {
                componentVersionsList.setItems(observableEntity.versionProperty());
            }
        });
        componentVersionsList.getSelectionModel().selectedItemProperty().subscribe(this::versionSelected);
        componentProperty.set(ObservableEntity.get(TinkarTerm.PROCEDURE.nid()));
    }

    private void versionSelected() {
        simpleVersionPane.versionProperty().set(selectedItemProperty().get());
    }

    @Override
    public ObjectProperty<ObservableEntity> componentProperty() {
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
    public void unsubscribeFromContext() {

    }

    @Override
    public void subscribeToContext() {

    }

    class StampListFactory implements Callback<ListView<ObservableVersion<EntityVersion>>, ListCell<ObservableVersion<EntityVersion>>> {
        @Override
        public ListCell<ObservableVersion<EntityVersion>> call(ListView<ObservableVersion<EntityVersion>> param) {
            return new ListCell<>(){
                @Override
                public void updateItem(ObservableVersion<EntityVersion> entityVersion, boolean empty) {
                    super.updateItem(entityVersion, empty);
                    setGraphic(null);
                    if (empty) {
                        setText(null);
                    } else if (entityVersion != null) {
                        String stampText = context().viewCoordinate().getPreferredTextForStamp(entityVersion.stampNid());
                        setText(stampText);
                    } else {
                        setText("Null value in list cell");
                    }
                }
            };
        }
    }
}
