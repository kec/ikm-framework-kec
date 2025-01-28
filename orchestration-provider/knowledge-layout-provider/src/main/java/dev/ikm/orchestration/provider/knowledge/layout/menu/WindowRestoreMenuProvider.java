package dev.ikm.orchestration.provider.knowledge.layout.menu;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlProfiles;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.interfaces.window.WindowRestoreProvider;
import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.prefs.BackingStoreException;

import static dev.ikm.komet.layout.KlGadget.PreferenceKeys.FACTORY_CLASS;
import static dev.ikm.komet.layout.KlGadget.PreferenceKeys.NAME_FOR_RESTORE;

public class WindowRestoreMenuProvider implements WindowRestoreProvider {
    private static final Logger LOG = LoggerFactory.getLogger(WindowRestoreMenuProvider.class);

    @Override
    public ImmutableList<Action> restoreWindowActions() {
        MutableList<Action> windowActions =  Lists.mutable.empty();
        try {
            for (KometPreferences child: KlProfiles.sharedWindowPreferences().children()) {
                child.get(NAME_FOR_RESTORE).ifPresent(nameForRestore -> {
                    child.get(FACTORY_CLASS).ifPresent(factoryClassName -> {
                        Action newLayoutWindowAction = new Action(nameForRestore,
                                event -> {
                                    try {
                                        Class<KlFactory> factoryClass = (Class<KlFactory>) Class.forName(factoryClassName);
                                        KlFactory klFactory = factoryClass.getDeclaredConstructor().newInstance();
                                        KlWindow window = (KlWindow) klFactory.restore(child);
                                        window.show();
                                    } catch (ClassNotFoundException | InvocationTargetException |
                                             InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                        windowActions.add(newLayoutWindowAction);
                    });
                });
            }
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
        return windowActions.toImmutable();
    }
}
