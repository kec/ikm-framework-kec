package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.window.KlWhiteBoard;
import dev.ikm.komet.layout.window.KlWhiteBoardFactory;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.layout.window.KlWindowFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWindowFactory implements KlWindowFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWindowFactory.class);


    @Override
    public KlWindow restore(KometPreferences preferences) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public ImmutableList<Action> createRestoreWindowActions() {
        return Lists.immutable.empty();
    }

    @Override
    public WindowType factoryWindowType() {
        return WindowType.JAVAFX;
    }

    @Override
    public KlWindow create(KlWhiteBoardFactory whiteBoardFactory) {
        KometPreferences windowPreferences = KlPreferencesFactory.createWindowPreferences(StageRecord.class);
        KlWhiteBoard whiteBoard = whiteBoardFactory.create(KlPreferencesFactory.createFactory(windowPreferences,
                whiteBoardFactory.klImplementationClass()));
        Scene scene = new Scene(whiteBoard.getRoot());
        Stage window = new Stage();
        window.setScene(scene);
        StageRecord stageRecord = new StageRecord(window, whiteBoard, windowPreferences);
        return stageRecord;
    }

    @Override
    public ImmutableList<Action> createNewWindowActions(KlWhiteBoardFactory... whiteBoardFactories) {
        MutableList<Action> actions = Lists.mutable.empty();
        for (KlWhiteBoardFactory whiteBoardFactory : whiteBoardFactories) {
            actions.add(new Action("New " + whiteBoardFactory.name(), event -> {
                KlWindow window = create(whiteBoardFactory);
                window.show();
            }));
        }
        return actions.toImmutable();
    }

    @Override
    public Class klInterfaceClass() {
        return KlWindow.class;
    }

    @Override
    public Class klImplementationClass() {
        return StageRecord.class;
    }
}
