package dev.ikm.orchestration.provider.knowledge.layout.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public abstract class WindowBlueprint extends GadgetBlueprint implements KlWindow {

    Stage root = new Stage();

    public WindowBlueprint(KometPreferences preferences) {
        super(preferences);
    }

    public WindowBlueprint(KometPreferences preferences, KlFactory factory) {
        super(preferences, factory);
    }

    @Override
    public Parent root() {
        return root;
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }
}
