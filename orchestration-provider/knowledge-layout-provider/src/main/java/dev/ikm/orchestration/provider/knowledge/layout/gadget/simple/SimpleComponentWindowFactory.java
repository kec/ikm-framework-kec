package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.context.KlContext;
import dev.ikm.komet.layout.context.KlContextFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.window.KlFxWindow;
import dev.ikm.komet.layout.window.KlFxWindowFactory;
import dev.ikm.komet.layout.window.KlWindowPaneFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.context.ContextFactory;
import dev.ikm.tinkar.coordinate.view.ViewCoordinateRecord;
import javafx.scene.control.ListView;
import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleComponentWindowFactory implements KlFxWindowFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWindowFactory.class);

    @Override
    public KlFxWindow restore(KometPreferences preferences) {
        return new SimpleWindow(preferences);
    }

    @Override
    public ImmutableList<Action> createRestoreWindowActions() {
        return Lists.immutable.empty();
    }

    @Override
    public KlFxWindow create(KlPreferencesFactory preferencesFactory) {
        SimpleWindow simpleWindow = new SimpleWindow(preferencesFactory, this, new SimpleViewFactory(),
                new SimpleWindowPaneFactory(), ContextFactory.getWithViewCoordinate((ViewCoordinateRecord) KlContext.PreferenceKeys.VIEW_COORDINATE.defaultValue()));
        SimpleComponentPane simpleComponentPane = new SimpleComponentPane(preferencesFactory.get());
        simpleWindow.windowPane().fxGadget().setTop(simpleComponentPane.fxGadget());
        return simpleWindow;
    }

    @Override
    public KlFxWindow createWithContext(KlPreferencesFactory preferencesFactory, KlContextFactory contextFactory) {
        return new SimpleWindow(preferencesFactory, this, new SimpleViewFactory(),
                new SimpleWindowPaneFactory(), contextFactory);
    }

    @Override
    public ImmutableList<Action> createNewWindowActions(KlPreferencesFactory preferencesFactory, KlWindowPaneFactory... windowPaneFactories) {
        MutableList<Action> actions = Lists.mutable.empty();
        for (KlWindowPaneFactory windowPaneFactory : windowPaneFactories) {
            actions.add(new Action("New " + windowPaneFactory.name(), event -> {
                KlFxWindow window = create(preferencesFactory);
                window.show();
            }));
        }
        return actions.toImmutable();
    }

    @Override
    public Class klInterfaceClass() {
        return KlFxWindow.class;
    }

    @Override
    public Class klImplementationClass() {
        return SimpleWindow.class;
    }
}
