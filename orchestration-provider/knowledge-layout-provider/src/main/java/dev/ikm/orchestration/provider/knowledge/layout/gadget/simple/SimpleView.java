package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.framework.graphics.Icon;
import dev.ikm.komet.framework.view.ObservableView;
import dev.ikm.komet.framework.view.ObservableViewNoOverride;
import dev.ikm.komet.framework.view.ViewMenuModel;
import dev.ikm.komet.framework.view.ViewProperties;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.KlGadget;
import dev.ikm.komet.layout.context.KlContext;
import dev.ikm.komet.layout.context.KlContextFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.context.view.ViewMenuFactory;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.ViewBlueprint;
import javafx.beans.Observable;
import javafx.event.Event;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToolBar;
import org.eclipse.collections.api.list.ImmutableList;

public class SimpleView extends ViewBlueprint {

    MenuButton viewPropertiesMenuButton = new MenuButton();
    private final ToolBar toolBar = new ToolBar(viewPropertiesMenuButton);


    public SimpleView(KometPreferences preferences) {
        super(preferences);
    }

    public SimpleView(KlPreferencesFactory preferencesFactory, KlFactory viewFactory, KlContextFactory contextFactory) {
        super(preferencesFactory, viewFactory, contextFactory);
        fxGadget.setTop(toolBar);

        viewPropertiesMenuButton.setOnShowing(this::onShowing);
        viewPropertiesMenuButton.setOnHidden(this::onHidden);
    }

    private void onHidden(Event event) {
        viewPropertiesMenuButton.getItems().clear();
    }

    private void onShowing(Event event) {
        viewPropertiesMenuButton.getItems().clear();
        ImmutableList<KlContext> contexts = contexts();
        for (KlContext context : contexts) {
            Menu viewPropertiesMenu = ViewMenuFactory.create(context.viewCoordinate(),
                    context.viewCoordinate().calculator());
            viewPropertiesMenuButton.getItems().add(viewPropertiesMenu);
        }
    }

    @Override
    protected void subViewSave() {
        // Nothing to do
    }

    @Override
    protected void subViewRevert() {
        // Nothing to do
    }


    @Override
    public void unsubscribeFromContext() {
        LOG.info("Implement unsubscribeFromContext");
    }

    @Override
    public void subscribeToContext() {
        LOG.info("Implement subscribeToContext");
    }

}
