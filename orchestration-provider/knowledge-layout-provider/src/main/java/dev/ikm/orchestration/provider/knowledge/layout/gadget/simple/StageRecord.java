package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.window.KlWhiteBoard;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.Window;

public record StageRecord(Stage stage, KlWhiteBoard klWhiteBoard, KometPreferences preferences) implements KlWindow {


    @Override
    public Window klGadget() {
        return stage;
    }

    @Override
    public Parent root() {
        return klWhiteBoard.root();
    }

    @Override
    public void show() {
        stage.show();
    }

    @Override
    public void hide() {
        stage.hide();
    }
}
