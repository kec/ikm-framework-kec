package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.preferences.KlProfiles;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.orchestration.interfaces.menu.MenuService;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.SimpleWindowFactory;
import javafx.application.Platform;
import javafx.scene.control.MenuItem;
import javafx.stage.Window;
import org.eclipse.collections.api.multimap.ImmutableMultimap;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.impl.factory.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewWindowMenuProvider implements MenuService {
    private static final Logger LOG = LoggerFactory.getLogger(NewWindowMenuProvider.class);

    @Override
    public ImmutableMultimap<String, MenuItem> getMenuItems(Window window) {
        MutableMultimap<String, MenuItem> menuItems = Multimaps.mutable.list.empty();

        MenuItem newLayoutWindowMenu = new MenuItem("New Window Test");
        newLayoutWindowMenu.setOnAction(event ->
            Platform.runLater(() -> {
                SimpleWindowFactory simpleWindowFactory = new SimpleWindowFactory();
                KlWindow klWindow = simpleWindowFactory.create(KlProfiles.sharedWindowPreferenceFactory(simpleWindowFactory.klImplementationClass()));
                klWindow.show();
            })
        );
        menuItems.put("Layout", newLayoutWindowMenu);

        return menuItems.toImmutable();
    }
}
