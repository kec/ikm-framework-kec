package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.KlWidget;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWidget extends Label implements KlWidget<Label> {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWidget.class);

    final KometPreferences preferences;

    public SimpleWidget(KometPreferences preferences, KlFactory factory) {
        super(preferences.name());
        this.preferences = preferences;

    }

    public static SimpleWidget create(KometPreferences preferences, KlFactory factory) {
        return new SimpleWidget(preferences, factory);
    }
}
