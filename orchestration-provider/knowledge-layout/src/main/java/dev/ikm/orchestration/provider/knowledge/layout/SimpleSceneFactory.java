package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.KlWidget;
import dev.ikm.komet.layout.window.KlSceneFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSceneFactory implements KlSceneFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleSceneFactory.class);

    @Override
    public Scene create(KometPreferences preferences) {
        return null;
    }

    @Override
    public Class<? extends KlWidget> klWidgetInterfaceClass() {
        return null;
    }

    @Override
    public Class<?> klWidgetImplementationClass() {
        return null;
    }
}
