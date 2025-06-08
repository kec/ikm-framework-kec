package dev.ikm.orchestration.provider.knowledge.layout.component;


import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.scene.control.Label;

public class ChronologyVersionsGridEmbeddedDetailsGrid
        extends ChronologyAreaBlueprint {

    public ChronologyVersionsGridEmbeddedDetailsGrid(KometPreferences preferences) {
        super(preferences);
    }

    public ChronologyVersionsGridEmbeddedDetailsGrid(KlPreferencesFactory preferencesFactory, KlArea.Factory gadgetFactory) {
        super(preferencesFactory, gadgetFactory);
    }

    @Override
    protected void componentChanged(ObservableEntity<ObservableVersion<EntityVersion>> oldValue,
                                    ObservableEntity<ObservableVersion<EntityVersion>> newValue) {
        if (newValue != null) {
            fxObject().setCenter(new Label("Field layout work in progress"));
        } else {
            fxObject().setCenter(new Label("No component selected"));
        }
    }

    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }
}
