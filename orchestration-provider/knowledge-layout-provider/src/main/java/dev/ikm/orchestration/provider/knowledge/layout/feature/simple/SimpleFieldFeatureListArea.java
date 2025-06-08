package dev.ikm.orchestration.provider.knowledge.layout.feature.simple;

import dev.ikm.komet.framework.observable.ObservableField;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.feature.KlListOfFieldArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint.GridPaneFeatureListAreaBlueprint;
import javafx.scene.layout.BorderPane;

public final class SimpleFieldFeatureListArea extends GridPaneFeatureListAreaBlueprint<ObservableField<?>>
        implements KlListOfFieldArea<BorderPane> {

    public SimpleFieldFeatureListArea(KometPreferences preferences) {
        super(preferences);
    }

    public SimpleFieldFeatureListArea(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory);
    }



    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }

    public static Factory factory() {
        return new Factory();
    }

    public static SimpleFieldFeatureListArea restore(KometPreferences preferences) {
        return factory().restore(preferences);
    }

    public static SimpleFieldFeatureListArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
        return factory().create(preferencesFactory, areaGridSettings);
    }

    public static SimpleFieldFeatureListArea create(KlPreferencesFactory preferencesFactory) {
        return factory().create(preferencesFactory);
    }

    public static class Factory implements KlListOfFieldArea.Factory<BorderPane> {

        @Override
        public SimpleFieldFeatureListArea restore(KometPreferences preferences) {
            return new SimpleFieldFeatureListArea(preferences);
        }

        @Override
        public SimpleFieldFeatureListArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            return new SimpleFieldFeatureListArea(preferencesFactory, this);
        }

        @Override
        public SimpleFieldFeatureListArea create(KlPreferencesFactory preferencesFactory) {
            SimpleFieldFeatureListArea area = new SimpleFieldFeatureListArea(preferencesFactory, this);
            area.setGridLayout(defaultAreaGridSettings());
            return area;
        }
    }
}
