package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.ViewBlueprint;

public class SimpleView extends ViewBlueprint {

    public SimpleView(KometPreferences preferences) {
        super(preferences);
    }

    public SimpleView(KlPreferencesFactory preferencesFactory, KlFactory factory) {
        super(preferencesFactory, factory);
    }
}
