package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.KlWidget;
import dev.ikm.komet.layout.window.KlSceneFactory;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.layout.window.KlWindowFactory;
import dev.ikm.komet.preferences.KometPreferences;
import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWindowFactory implements KlWindowFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWindowFactory.class);

    @Override
    public KlWindow create(KometPreferences preferences) {
        return null;
    }

    @Override
    public KlWindow create() {
        return null;
    }

    @Override
    public ImmutableList<Action> createNewWindowActions(KlSceneFactory sceneFactory) {
        return null;
    }

    @Override
    public KlWindow restore(KometPreferences preferences) {
        return null;
    }

    @Override
    public ImmutableList<Action> createRestoreWindowActions() {
        return null;
    }

    @Override
    public WindowType factoryWindowType() {
        return WindowType.JAVAFX;
    }

    @Override
    public ImmutableList<Action> createNewWindowActions() {
        return null;
    }

    @Override
    public Class klInterfaceClass() {
        return ;
    }

    @Override
    public Class klImplementationClass() {
        return null;
    }
}
