package dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint;

import dev.ikm.komet.framework.observable.Feature;
import dev.ikm.komet.framework.observable.FeatureList;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.version.FeatureListCellFactory;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public abstract class FeatureListViewAreaBlueprint<F extends Feature<?>>
        extends FeatureAreaBlueprint<ListView<F>, FeatureList<F>> {

    protected FeatureListViewAreaBlueprint(KometPreferences preferences) {
        super(preferences, new ListView());
        setup();
    }

    protected FeatureListViewAreaBlueprint(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory, new ListView());
        setup();
    }

    private void setup() {
        fxObject().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fxObject().setCellFactory(new FeatureListCellFactory(this));
    }

    public ObservableList<F> selectedItems() {
        return this.fxObject().getSelectionModel().getSelectedItems();
    }

    public FeatureList<F> getList() {
        return this.featurePropertyWrapper.get().getValue();
        //return this.fxObject.getItems();
    }

    public void setList(FeatureList<F> list) {
        this.featurePropertyWrapper.get().setValue(list);
    }

    @Override
    protected void propertyWrapperUpdated(Property<FeatureList<F>> oldValue, Property<FeatureList<F>> newValue) {
        this.fxObject().getSelectionModel().clearSelection();
        fxObject().setItems(newValue.getValue());
    }
}
