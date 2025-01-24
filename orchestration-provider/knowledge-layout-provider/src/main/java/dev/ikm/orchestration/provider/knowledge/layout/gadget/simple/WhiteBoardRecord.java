package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.window.KlWhiteBoard;
import dev.ikm.komet.layout.window.KlWhiteBoardFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

/**
 * Represents a record-based implementation of the {@link KlWhiteBoard} interface.
 * This class combines the structure of a Java record with the behavior of a whiteboard
 * system, to be used within the knowledge layout framework.
 *
 * The WhiteBoardRecord is responsible for initializing necessary components using a
 * {@link KlWhiteBoardFactory}, managing preferences through {@link KometPreferences},
 * and providing access to the root node of its associated scene hierarchy.
 *
 * The record encapsulates the following properties:
 * - Root node of the whiteboard's scene hierarchy.
 * - Preferences for configuring the whiteboard.
 * - The factory used to create this instance of a whiteboard.
 */
public record WhiteBoardRecord(BorderPane whiteboardRoot, KometPreferences preferences, KlWhiteBoardFactory whiteBoardFactory) implements KlWhiteBoard {
    public WhiteBoardRecord {
        //universalInitialize(preferences, whiteBoardFactory);
    }

    /**
     * Retrieves the root node of the associated Scene.
     * The root node represents the top-most parent within the scene's hierarchy
     * of visual elements.
     *
     * @return The {@link Parent} object serving as the root node of the Scene.
     */
    @Override
    public BorderPane root() {
        return whiteboardRoot;
    }

    /**
     * Retrieves this instance as a KlWhiteBoard gadget.
     * This method provides a self-referential return, allowing the instance
     * to represent itself within the Knowledge Layout system when a KlWhiteBoard gadget is required.
     *
     * @return This instance as a {@link KlWhiteBoard} object.
     */
    @Override
    public KlWhiteBoard klGadget() {
        return this;
    }
}
