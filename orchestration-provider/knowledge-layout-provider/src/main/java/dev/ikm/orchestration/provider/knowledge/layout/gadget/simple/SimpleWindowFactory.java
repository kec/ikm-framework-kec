package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.layout.window.KlWindowFactory;
import dev.ikm.komet.layout.window.KlWindowPaneFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWindowFactory implements KlWindowFactory<Stage> {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWindowFactory.class);

    @Override
    public KlWindow restore(KometPreferences preferences) {
        return new SimpleWindow(preferences);
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
    public KlWindow create(KlPreferencesFactory preferencesFactory) {
        return new SimpleWindow(preferencesFactory, this, new SimpleViewFactory(), new SimpleWindowPaneFactory());
    }

    @Override
    public ImmutableList<Action> createNewWindowActions(KlPreferencesFactory preferencesFactory, KlWindowPaneFactory... windowPaneFactories) {
        MutableList<Action> actions = Lists.mutable.empty();
        for (KlWindowPaneFactory windowPaneFactory : windowPaneFactories) {
            actions.add(new Action("New " + windowPaneFactory.name(), event -> {
                KlWindow window = create(preferencesFactory);
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
        return SimpleWindow.class;
    }
}
