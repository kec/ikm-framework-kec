package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.context.KlContextFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.window.KlFrame;
import dev.ikm.komet.layout.window.KlFrameFactory;
import dev.ikm.komet.preferences.KometPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFrameFactory implements KlFrameFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleFrameFactory.class);

    @Override
    public SimpleFrame create(KlPreferencesFactory preferencesFactory) {
        return new SimpleFrame(preferencesFactory, this);
    }

    @Override
    public SimpleFrame restore(KometPreferences whiteBoardPreferences) {
        return new SimpleFrame(whiteBoardPreferences);
    }

    @Override
    public SimpleFrame createWithContext(KlPreferencesFactory preferencesFactory, KlContextFactory contextFactory) {
        // context not used
        return new SimpleFrame(preferencesFactory, this);
    }

    @Override
    public Class<? extends KlFrame> klImplementationClass() {
        return SimpleFrame.class;
    }
}
