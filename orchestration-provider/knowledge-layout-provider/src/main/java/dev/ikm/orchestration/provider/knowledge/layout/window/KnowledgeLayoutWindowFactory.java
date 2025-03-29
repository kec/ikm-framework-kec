package dev.ikm.orchestration.provider.knowledge.layout.window;

import dev.ikm.orchestration.provider.knowledge.layout.component.DynamicComponentArea;
import dev.ikm.komet.layout.context.KlContext;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.window.KlFrameFactory;
import dev.ikm.komet.layout.window.KlFxWindow;
import dev.ikm.komet.layout.window.KlFxWindowFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.context.ContextFactory;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.SimpleFrameFactory;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.SimpleViewFactory;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.SimpleWindow;
import dev.ikm.tinkar.coordinate.view.ViewCoordinateRecord;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

public class KnowledgeLayoutWindowFactory implements KlFxWindowFactory {
    @Override
    public KlFxWindow create(KlPreferencesFactory preferencesFactory) {
        SimpleWindow simpleWindow = new SimpleWindow(preferencesFactory, this, new SimpleViewFactory(),
                new SimpleFrameFactory(), ContextFactory.getWithViewCoordinate((ViewCoordinateRecord) KlContext.PreferenceKeys.VIEW_COORDINATE.defaultValue()));
        DynamicComponentArea dynamicComponentArea = new DynamicComponentArea(preferencesFactory.get());
        simpleWindow.klFrame().fxObject().setCenter(dynamicComponentArea.fxObject());
        ToolBar windowToolBar = new ToolBar();
        windowToolBar.setOrientation(Orientation.VERTICAL);
        Label label = new Label("Simple Frame containing DynamicComponentArea");
        label.setStyle("-fx-rotate: -90;");
        windowToolBar.getItems().add(new Group(label));
        simpleWindow.klFrame().fxObject().setLeft(windowToolBar);

        return simpleWindow;
    }

    @Override
    public KlFxWindow restore(KometPreferences preferences) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public ImmutableList<Action> createNewWindowActions(KlPreferencesFactory preferencesFactory, KlFrameFactory... frameFactories) {
        //TODO: implement
        return Lists.immutable.empty();
    }

    @Override
    public ImmutableList<Action> createRestoreWindowActions() {
        //TODO: implement
        return Lists.immutable.empty();
    }


    @Override
    public Class klImplementationClass() {
        return SimpleWindow.class;
    }

}
