package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.window.KlScene;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.stage.Window;

public record KlWindowRecord(Window window, KlScene klScene, KometPreferences preferences) implements KlWindow {
    @Override
    public KlScene scene() {
        return klScene;
    }

    @Override
    public Window klGadget() {
        return window;
    }

    @Override
    public void classInitialize() {

    }
}
