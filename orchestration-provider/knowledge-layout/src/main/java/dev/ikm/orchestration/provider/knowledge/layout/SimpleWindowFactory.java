package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.KlWidget;
import dev.ikm.komet.layout.KlWidgetFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.window.KlScene;
import dev.ikm.komet.layout.window.KlSceneFactory;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.layout.window.KlWindowFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class SimpleWindowFactory implements KlWindowFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWindowFactory.class);

    private final SimpleSceneFactory sceneFactory = new SimpleSceneFactory();


    @Override
    public ImmutableList<Action> createNewWindowActions(KlSceneFactory sceneFactory) {
        return Lists.immutable.empty();
    }

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
    public ImmutableList<Action> createNewWindowActions() {
        return null;
    }

    @Override
    public KlWindow create(KlWidgetFactory widgetFactory) {
        KometPreferences windowPreferences = KlPreferencesFactory.createWindowPreferences(KlWindowRecord.class);
        KlWidget widget = widgetFactory.create(KlPreferencesFactory.createFactory(windowPreferences,
                widgetFactory.klImplementationClass()));

        KlScene scene = sceneFactory.create(widget,
                KlPreferencesFactory.createScenePreferencesSupplier(windowPreferences));
        Stage window = new Stage();
        window.setScene(scene.scene());
        KlWindowRecord windowRecord = new KlWindowRecord(window, scene, windowPreferences);
        return windowRecord;
    }

    @Override
    public Class klInterfaceClass() {
        return KlWindow.class;
    }

    @Override
    public Class klImplementationClass() {
        return KlWindowRecord.class;
    }
}
