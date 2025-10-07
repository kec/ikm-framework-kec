package dev.ikm.orchestration.provider.knowledge.layout.area;

import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.application.Platform;
import javafx.scene.layout.Priority;

public final class SupplementalArea extends SupplementalAreaBlueprint {

    KlArea center;

    private SupplementalArea(KometPreferences preferences) {
        super(preferences);
    }

    private SupplementalArea(KlPreferencesFactory preferencesFactory, KlArea.Factory gadgetFactory) {
        super(preferencesFactory, gadgetFactory);
    }

    @Override
    protected void subAreaRevert() {

    }

    @Override
    protected void subAreaSave() {

    }

    @Override
    protected void subAreaRestoreFromPreferencesOrDefault() {
        // Nothing to restore.
    }

    @Override
    public void knowledgeLayoutUnbind() {
        // Nothing to unbind.
    }

    @Override
    public void knowledgeLayoutBind() {
        Platform.runLater(() -> this.lifecycleState.set(LifecycleState.BOUND));
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
            SupplementalArea supplementalArea = new SupplementalArea(preferences);
            return supplementalArea;
        }

        @Override
        public SupplementalArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            SupplementalArea area = new SupplementalArea(preferencesFactory, areaGridSettings.makeAreaFactory());
            area.setAreaLayout(areaGridSettings);
            return area;
        }
    }
}
