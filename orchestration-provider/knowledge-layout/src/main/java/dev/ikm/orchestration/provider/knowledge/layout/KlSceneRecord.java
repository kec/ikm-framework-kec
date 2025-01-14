package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.KlWidget;
import dev.ikm.komet.layout.window.KlScene;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.Parent;
import javafx.scene.Scene;

public record KlSceneRecord(Scene scene, KlWidget widget, KometPreferences preferences) implements KlScene {
    @Override
    public Parent getRoot() {
        return widget.klWidget();
    }

    @Override
    public KlScene klGadget() {
        return this;
    }

    @Override
    public void classInitialize() {

    }
}
