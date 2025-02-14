package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.context.KlContextFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.window.KlWindowPane;
import dev.ikm.komet.layout.window.KlWindowPaneFactory;
import dev.ikm.komet.preferences.KometPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWindowPaneFactory implements KlWindowPaneFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleWindowPaneFactory.class);

    @Override
    public SimpleWindowPane create(KlPreferencesFactory preferencesFactory) {
        return new SimpleWindowPane(preferencesFactory, this);
    }

    @Override
    public SimpleWindowPane restore(KometPreferences whiteBoardPreferences) {
        return new SimpleWindowPane(whiteBoardPreferences);
    }

    @Override
    public SimpleWindowPane createWithContext(KlPreferencesFactory preferencesFactory, KlContextFactory contextFactory) {
        // context not used
        return new SimpleWindowPane(preferencesFactory, this);
    }

    @Override
    public Class<KlWindowPane> klInterfaceClass() {
        return KlWindowPane.class;
    }

    @Override
    public Class<? extends KlWindowPane> klImplementationClass() {
        return SimpleWindowPane.class;
    }
}
