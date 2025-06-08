package dev.ikm.orchestration.provider.knowledge.layout.area;

import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.layout.Priority;

public final class SupplementalArea extends SupplementalAreaBlueprint {

    KlArea center;

    public SupplementalArea(KometPreferences preferences) {
        super(preferences);
    }

    public SupplementalArea(KlPreferencesFactory preferencesFactory, KlArea.Factory gadgetFactory) {
        super(preferencesFactory, gadgetFactory);
    }

    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }

    public void setCenter(KlArea center) {
        this.center = center;
        AreaGridSettings gridSettings = center.getAreaLayout()
                .withHGrow(Priority.ALWAYS).withVGrow(Priority.ALWAYS)
                .withFillHeight(true).withFillWidth(true);
        center.setGridLayout(gridSettings);
        this.gridPaneForChildren().getChildren().clear();
        this.gridPaneForChildren().getChildren().add(center.fxObject());
    }

    public static Factory factory() {
        return new Factory();
    }

    public static SupplementalArea restore(KometPreferences preferences) {
        return factory().restore(preferences);
    }

    public static SupplementalArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
        return factory().create(preferencesFactory, areaGridSettings);
    }

    public static SupplementalArea create(KlPreferencesFactory preferencesFactory) {
        return factory().create(preferencesFactory);
    }

    public static class Factory
            implements SupplementalAreaBlueprint.Factory<SupplementalArea> {

        @Override
        public SupplementalArea restore(KometPreferences preferences) {
            return new SupplementalArea(preferences);
        }

        @Override
        public SupplementalArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            return new SupplementalArea(preferencesFactory, areaGridSettings.makeAreaFactory());
        }
    }
}
