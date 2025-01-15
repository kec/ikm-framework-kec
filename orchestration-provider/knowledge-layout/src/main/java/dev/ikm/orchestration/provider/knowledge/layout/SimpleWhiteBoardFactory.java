package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.window.KlWhiteBoard;
import dev.ikm.komet.layout.window.KlWhiteBoardFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class SimpleWhiteBoardFactory implements KlWhiteBoardFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleWhiteBoardFactory.class);

    @Override
    public KlWhiteBoard create(Supplier<KometPreferences> preferenceSupplier) {
        BorderPane whiteboardRoot = new BorderPane();
        whiteboardRoot.setCenter(new Label("Simple Whiteboard"));
        KometPreferences whiteBoardPreferences = preferenceSupplier.get();
        WhiteBoardRecord whiteBoardRecord = new WhiteBoardRecord(whiteboardRoot, whiteBoardPreferences, this);
        return whiteBoardRecord;
    }

    @Override
    public KlWhiteBoard restore(KometPreferences whiteBoardPreferences) {
        BorderPane whiteboardRoot = new BorderPane();
        whiteboardRoot.setCenter(new Label("Simple Whiteboard"));
        WhiteBoardRecord whiteBoardRecord = new WhiteBoardRecord(whiteboardRoot, whiteBoardPreferences, this);
        return whiteBoardRecord;
    }

    @Override
    public Class<KlWhiteBoard> klInterfaceClass() {
        return KlWhiteBoard.class;
    }

    @Override
    public Class<? extends KlWhiteBoard> klImplementationClass() {
        return WhiteBoardRecord.class;
    }
}
