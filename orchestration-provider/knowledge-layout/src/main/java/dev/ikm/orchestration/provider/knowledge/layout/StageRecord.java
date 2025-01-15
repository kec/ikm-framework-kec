package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.window.KlWhiteBoard;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.Window;

public record StageRecord(Stage stage, KlWhiteBoard klWhiteBoard, KometPreferences preferences) implements KlWindow {
    public StageRecord {
        universalInitialize();
    }

    @Override
    public Window klGadget() {
        return stage;
    }

    @Override
    public void classInitialize() {

    }

    @Override
    public Parent getRoot() {
        return klWhiteBoard.getRoot();
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
