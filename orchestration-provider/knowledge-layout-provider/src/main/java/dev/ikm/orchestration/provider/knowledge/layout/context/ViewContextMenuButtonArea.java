package dev.ikm.orchestration.provider.knowledge.layout.context;

import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.context.KlContext;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.area.SupplementalAreaBlueprint;
import dev.ikm.orchestration.provider.knowledge.layout.context.view.ViewMenuFactory;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToolBar;
import org.eclipse.collections.api.list.ImmutableList;

/**
 * A simple provider of ViewContext functionality, that provides a single view menu button.
 */
public final class ViewContextMenuButtonArea extends ViewContextBlueprint {

    MenuButton viewPropertiesMenuButton = new MenuButton();
    private final ToolBar toolBar = new ToolBar(viewPropertiesMenuButton, new Label("Simple View"));

    public ViewContextMenuButtonArea(KometPreferences preferences) {
        super(preferences);
    }

    public ViewContextMenuButtonArea(KlPreferencesFactory preferencesFactory, KlArea.Factory viewFactory, ViewContext viewContext) {
        super(preferencesFactory, viewFactory, viewContext);
        fxObject().setTop(toolBar);

        viewPropertiesMenuButton.setOnShowing(this::onShowing);
        viewPropertiesMenuButton.setOnHidden(this::onHidden);
    }

    public ViewContextMenuButtonArea(KlPreferencesFactory preferencesFactory, KlArea.Factory viewFactory) {
        super(preferencesFactory, viewFactory);
        fxObject().setTop(toolBar);

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
    protected void subViewContextSave() {
        // Nothing to do
    }

    @Override
    protected void subViewContextRevert() {
        // Nothing to do
    }

    public static Factory factory() {
        return new Factory();
    }

    public static ViewContextMenuButtonArea restore(KometPreferences preferences) {
        return factory().restore(preferences);
    }

    public static ViewContextMenuButtonArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
        return factory().create(preferencesFactory, areaGridSettings);
    }

    public static ViewContextMenuButtonArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings, ViewContext viewContext) {
        return factory().create(preferencesFactory, areaGridSettings, viewContext);
    }

    public static ViewContextMenuButtonArea create(KlPreferencesFactory preferencesFactory, ViewContext viewContext) {
        return factory().create(preferencesFactory, viewContext);
    }

    public static class Factory
            implements SupplementalAreaBlueprint.Factory<ViewContextMenuButtonArea> {

        @Override
        public ViewContextMenuButtonArea restore(KometPreferences preferences) {
            return new ViewContextMenuButtonArea(preferences);
        }

        @Override
        public ViewContextMenuButtonArea create(KlPreferencesFactory preferencesFactory) {
            ViewContextMenuButtonArea viewContextMenuButtonArea =
                    new ViewContextMenuButtonArea(preferencesFactory, this);
            viewContextMenuButtonArea.setGridLayout(AreaGridSettings.DEFAULT.with(this.getClass()));
            return viewContextMenuButtonArea;
        }


        @Override
        public ViewContextMenuButtonArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            ViewContextMenuButtonArea viewContextMenuButtonArea =
                    new ViewContextMenuButtonArea(preferencesFactory, this);
            viewContextMenuButtonArea.setGridLayout(areaGridSettings.with(this.getClass()));
            return viewContextMenuButtonArea;
        }


        public ViewContextMenuButtonArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings, ViewContext viewContext) {
            ViewContextMenuButtonArea viewContextMenuButtonArea =
                    new ViewContextMenuButtonArea(preferencesFactory, this, viewContext);
            viewContextMenuButtonArea.setGridLayout(areaGridSettings.with(this.getClass()));
            return viewContextMenuButtonArea;
        }

        public ViewContextMenuButtonArea create(KlPreferencesFactory preferencesFactory, ViewContext viewContext) {
            ViewContextMenuButtonArea viewContextMenuButtonArea =
                    new ViewContextMenuButtonArea(preferencesFactory, this, viewContext);
            viewContextMenuButtonArea.setGridLayout(defaultAreaGridSettings().with(this.getClass()));
            return viewContextMenuButtonArea;
        }
    }

}
