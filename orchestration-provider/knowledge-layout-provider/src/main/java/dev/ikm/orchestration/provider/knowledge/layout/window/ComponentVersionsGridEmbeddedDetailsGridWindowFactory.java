package dev.ikm.orchestration.provider.knowledge.layout.window;

import dev.ikm.komet.layout.context.KlContext;
import dev.ikm.komet.layout.context.KlContextFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.window.KlFrameFactory;
import dev.ikm.komet.layout.window.KlFxWindow;
import dev.ikm.komet.layout.window.KlFxWindowFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.component.ComponentVersionsGridEmbeddedDetailsGrid;
import dev.ikm.orchestration.provider.knowledge.layout.component.SimpleComponentArea;
import dev.ikm.orchestration.provider.knowledge.layout.context.ContextFactory;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.SimpleFrameFactory;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.SimpleViewFactory;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.SimpleWindow;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.SimpleWindowFactory;
import dev.ikm.tinkar.coordinate.view.ViewCoordinateRecord;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentVersionsGridEmbeddedDetailsGridWindowFactory implements KlFxWindowFactory {
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
                new SimpleFrameFactory(), ContextFactory.getWithViewCoordinate((ViewCoordinateRecord) KlContext.PreferenceKeys.VIEW_COORDINATE.defaultValue()));
        ComponentVersionsGridEmbeddedDetailsGrid componentArea = new ComponentVersionsGridEmbeddedDetailsGrid(preferencesFactory.get());
        simpleWindow.klFrame().fxObject().setCenter(componentArea.fxObject());
        ToolBar windowToolBar = new ToolBar();
        windowToolBar.setOrientation(Orientation.VERTICAL);
        Label label = new Label("Simple Frame containing Component Versions Grid");
        label.setStyle("-fx-rotate: -90;");
        windowToolBar.getItems().add(new Group(label));
        simpleWindow.klFrame().fxObject().setLeft(windowToolBar);

        return simpleWindow;
    }

    @Override
    public KlFxWindow createWithContext(KlPreferencesFactory preferencesFactory, KlContextFactory contextFactory) {
        return new SimpleWindow(preferencesFactory, this, new SimpleViewFactory(),
                new SimpleFrameFactory(), contextFactory);
    }

    @Override
    public ImmutableList<Action> createNewWindowActions(KlPreferencesFactory preferencesFactory, KlFrameFactory... frameFactories) {
        MutableList<Action> actions = Lists.mutable.empty();
        for (KlFrameFactory windowPaneFactory : frameFactories) {
            actions.add(new Action("New " + windowPaneFactory.name(), event -> {
                KlFxWindow window = create(preferencesFactory);
                window.show();
            }));
        }
        return actions.toImmutable();
    }

    @Override
    public Class klImplementationClass() {
        return SimpleWindow.class;
    }
}
