package dev.ikm.orchestration.provider.knowledge.layout.menu;

import dev.ikm.komet.layout.preferences.KlProfiles;
import dev.ikm.komet.layout.window.KlFxWindow;
import dev.ikm.orchestration.interfaces.window.WindowCreateProvider;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.SimpleWindowFactory;
import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

public class NewWindowMenuProvider implements WindowCreateProvider {

    @Override
    public ImmutableList<Action> createWindowActions() {
        Action newLayoutWindowAction = new Action("New Window Test",
        event -> {
                    SimpleWindowFactory simpleWindowFactory = new SimpleWindowFactory();
                    KlFxWindow klWindow = simpleWindowFactory.create(KlProfiles.sharedWindowPreferenceFactory(simpleWindowFactory.klImplementationClass()));
                    klWindow.context().subscribeDependentContexts();
                    klWindow.show();
                });
        return Lists.immutable.of(newLayoutWindowAction);
    }
}
