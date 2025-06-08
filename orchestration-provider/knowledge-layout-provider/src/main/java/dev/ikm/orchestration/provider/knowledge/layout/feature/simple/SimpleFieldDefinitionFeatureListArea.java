package dev.ikm.orchestration.provider.knowledge.layout.feature.simple;

import dev.ikm.komet.framework.observable.ObservableFieldDefinition;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.feature.KlListOfFieldDefinitionArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint.GridPaneFeatureListAreaBlueprint;
import javafx.scene.layout.BorderPane;

public final class SimpleFieldDefinitionFeatureListArea
        extends GridPaneFeatureListAreaBlueprint<ObservableFieldDefinition>
        implements KlListOfFieldDefinitionArea<BorderPane> {

    public SimpleFieldDefinitionFeatureListArea(KometPreferences preferences) {
        super(preferences);
    }

    public SimpleFieldDefinitionFeatureListArea(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
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

    public static SimpleFieldDefinitionFeatureListArea restore(KometPreferences preferences) {
        return factory().restore(preferences);
    }

    public static SimpleFieldDefinitionFeatureListArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
        return factory().create(preferencesFactory, areaGridSettings);
    }
    public static SimpleFieldDefinitionFeatureListArea create(KlPreferencesFactory preferencesFactory) {
        return factory().create(preferencesFactory);
    }

    public static class Factory implements KlListOfFieldDefinitionArea.Factory<BorderPane> {

        @Override
        public SimpleFieldDefinitionFeatureListArea restore(KometPreferences preferences) {
            return new SimpleFieldDefinitionFeatureListArea(preferences);
        }

        @Override
        public SimpleFieldDefinitionFeatureListArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            return new SimpleFieldDefinitionFeatureListArea(preferencesFactory, this) ;
        }
        @Override
        public SimpleFieldDefinitionFeatureListArea create(KlPreferencesFactory preferencesFactory) {
            SimpleFieldDefinitionFeatureListArea area = new SimpleFieldDefinitionFeatureListArea(preferencesFactory, this);
            area.setGridLayout(defaultAreaGridSettings());
            return area;
        }
    }
}
