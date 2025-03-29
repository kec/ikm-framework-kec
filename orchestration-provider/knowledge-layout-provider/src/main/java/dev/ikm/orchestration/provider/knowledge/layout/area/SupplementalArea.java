package dev.ikm.orchestration.provider.knowledge.layout.area;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.layout.BorderPane;

public class SupplementalArea extends SupplementalAreaBlueprint<BorderPane> {
    public SupplementalArea(KometPreferences preferences) {
        super(preferences, new BorderPane());
    }

    public SupplementalArea(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new BorderPane());
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
