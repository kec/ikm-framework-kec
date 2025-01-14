package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.Scene;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFxWindow extends KlWindow {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleFxWindow.class);

    final Window window;
    final KometPreferences preferences;

    @Override
    public Scene scene() {
        return window.getScene();
    }

    @Override
    public Window klGadget() {
        return window;
    }

    @Override
    public KometPreferences preferences() {
        return preferences;
    }

    @Override
    public void classInitialize() {

    }
}
