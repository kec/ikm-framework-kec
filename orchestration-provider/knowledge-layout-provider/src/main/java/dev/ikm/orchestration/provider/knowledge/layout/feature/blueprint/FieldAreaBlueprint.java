package dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint;

import dev.ikm.komet.framework.observable.ObservableField;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.Optional;


public abstract class FieldAreaBlueprint<FX extends Region, DT> extends FeatureAreaBlueprint<FX, ObservableField<DT>> {

    public FieldAreaBlueprint(KometPreferences preferences, FX fxObject) {
        super(preferences, fxObject);
    }

    public FieldAreaBlueprint(KlPreferencesFactory preferencesFactory, KlArea.Factory gadgetFactory, FX fxObject) {
        super(preferencesFactory, gadgetFactory, fxObject);
    }

    public void setField(ObservableField<DT> field) {
        featurePropertyWrapper.getValue().setValue(field);
    }

    public ObservableField<DT> getField() {
        return featurePropertyWrapper.getValue().getValue();
    }
}