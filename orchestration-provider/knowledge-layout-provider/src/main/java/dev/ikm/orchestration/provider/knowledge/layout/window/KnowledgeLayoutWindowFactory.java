package dev.ikm.orchestration.provider.knowledge.layout.window;

import dev.ikm.komet.layout.window.KlRenderView;
import dev.ikm.orchestration.provider.knowledge.layout.area.SupplementalArea;
import dev.ikm.orchestration.provider.knowledge.layout.component.DynamicChronologyArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.window.KlFrameFactory;
import dev.ikm.komet.layout.window.KlFxWindow;
import dev.ikm.komet.layout.window.KlFxWindowFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.context.ViewContextMenuButtonArea;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.FxWindow;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.RenderView;
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

        FxWindow simpleWindow = FxWindow.factory().create(preferencesFactory);
        KlRenderView renderView = RenderView.factory().create(simpleWindow.childPreferencesFactory(RenderView.class));
        simpleWindow.setKlRenderView(renderView);
        SupplementalArea supplementalArea =
                SupplementalArea.factory().create(renderView.childPreferencesFactory(SupplementalArea.class));
        ToolBar windowToolBar = new ToolBar();
        windowToolBar.setOrientation(Orientation.VERTICAL);
        Label label = new Label("FxWindow containing DynamicComponentArea");
        label.setStyle("-fx-rotate: -90;");
        windowToolBar.getItems().add(new Group(label));
        supplementalArea.fxObject().setLeft(windowToolBar);
        renderView.setKlRootArea(supplementalArea);

        ViewContextMenuButtonArea viewContextMenuButtonArea = ViewContextMenuButtonArea.factory().create(simpleWindow.childPreferencesFactory(ViewContextMenuButtonArea.class));
        supplementalArea.setCenter(viewContextMenuButtonArea);

        DynamicChronologyArea dynamicComponentArea = new DynamicChronologyArea(preferencesFactory.get());
        viewContextMenuButtonArea.setCenter(dynamicComponentArea);

        return simpleWindow;
    }

    @Override
    public KlFxWindow restore(KometPreferences preferences) {
        return FxWindow.restore(preferences);
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

}
