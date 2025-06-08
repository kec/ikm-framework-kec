package dev.ikm.orchestration.provider.knowledge.layout.window;

import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.window.KlFrameFactory;
import dev.ikm.komet.layout.window.KlFxWindow;
import dev.ikm.komet.layout.window.KlFxWindowFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.area.SupplementalArea;
import dev.ikm.orchestration.provider.knowledge.layout.component.ChronologyVersionsListDetailsArea;
import dev.ikm.orchestration.provider.knowledge.layout.context.ViewContextMenuButtonArea;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.FxWindow;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.SimpleKnowledgeLayout;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.RenderView;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

public class ComponentVersionsListDetailsAreaWindowFactory implements KlFxWindowFactory {

    @Override
    public KlFxWindow restore(KometPreferences preferences) {
        return FxWindow.restore(preferences);
    }

    @Override
    public ImmutableList<Action> createRestoreWindowActions() {
        return Lists.immutable.empty();
    }

    @Override
    public KlFxWindow create(KlPreferencesFactory preferencesFactory) {
        FxWindow simpleWindow = FxWindow.factory().create(preferencesFactory);
        RenderView renderView = new RenderView.Factory().create(simpleWindow.childPreferencesFactory(RenderView.class));
        simpleWindow.setKlRenderView(renderView);

        SupplementalArea supplementalArea = SupplementalArea.factory().create(renderView.childPreferencesFactory(SupplementalArea.class));
        renderView.setKlRootArea(supplementalArea);

        ToolBar windowToolBar = new ToolBar();
        windowToolBar.setOrientation(Orientation.VERTICAL);
        Label label = new Label("FxWindow containing Component Versions List");
        label.setStyle("-fx-rotate: -90;");
        windowToolBar.getItems().add(new Group(label));
        supplementalArea.fxObject().setLeft(windowToolBar);

        ViewContextMenuButtonArea viewContextMenuButtonArea = ViewContextMenuButtonArea.factory().create(supplementalArea.childPreferencesFactory(ViewContextMenuButtonArea.class));
        supplementalArea.setCenter(viewContextMenuButtonArea);

        AreaGridSettings componentVersionsSettings = AreaGridSettings.DEFAULT.withLayoutKeyForArea(renderView.getMasterLayout().rootLayoutKey());
        ChronologyVersionsListDetailsArea componentVersionsArea =
                ChronologyVersionsListDetailsArea.factory().create(viewContextMenuButtonArea.childPreferencesFactory(
                        ChronologyVersionsListDetailsArea.class), componentVersionsSettings);

        viewContextMenuButtonArea.setCenter(componentVersionsArea);
        supplementalArea.setMasterLayout(new SimpleKnowledgeLayout(componentVersionsArea));

        return simpleWindow;
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
}