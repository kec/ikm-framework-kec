package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.KlView;
import dev.ikm.komet.layout.KlViewFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;

public class SimpleViewFactory implements KlViewFactory {
    @Override
    public KlView create(KlPreferencesFactory preferencesFactory) {
        return new SimpleView(preferencesFactory, this);
    }

    @Override
    public KlView restore(KometPreferences preferences) {
        return new SimpleView(preferences);
    }

    @Override
    public Class<KlView> klInterfaceClass() {
        return KlView.class;
    }

    @Override
    public Class<? extends KlView> klImplementationClass() {
        return SimpleView.class;
    }
}
