package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.StageBlueprint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWindow extends StageBlueprint {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWindow.class);

    public SimpleWindow(KometPreferences preferences) {
        super(preferences);
    }

    public SimpleWindow(KometPreferences preferences, KlFactory factory) {
        super(preferences, factory);
    }
}
