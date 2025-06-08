package dev.ikm.orchestration.provider.knowledge.layout.menu;

import dev.ikm.komet.layout.preferences.KlProfiles;
import dev.ikm.komet.layout.window.KlFxWindow;
import dev.ikm.orchestration.interfaces.window.WindowCreateProvider;
import dev.ikm.orchestration.provider.knowledge.layout.window.ComponentVersionsGridEmbeddedDetailsGridWindowFactory;
import dev.ikm.orchestration.provider.knowledge.layout.window.ComponentVersionsListDetailsAreaWindowFactory;
import dev.ikm.orchestration.provider.knowledge.layout.window.KnowledgeLayoutWindowFactory;
import dev.ikm.orchestration.provider.knowledge.layout.window.SimpleComponentWindowFactory;
import dev.ikm.tinkar.common.util.time.DateTimeUtil;
import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

public class NewWindowMenuProvider implements WindowCreateProvider {

    @Override
    public ImmutableList<Action> createWindowActions() {
        Action simpleComponentAreaTest = new Action("SimpleComponentArea test",
                event -> {
                    SimpleComponentWindowFactory simpleWindowFactory = new SimpleComponentWindowFactory();
                    KlFxWindow klWindow = simpleWindowFactory.create(KlProfiles.sharedWindowPreferenceFactory(SimpleComponentWindowFactory.class));
                    klWindow.context().subscribeDependentContexts();
                    klWindow.show();
                });
        Action componentVersionsListDetailsAreaTest = new Action("Component Versions Embedded Grid test",
                event -> {
                    ComponentVersionsGridEmbeddedDetailsGridWindowFactory windowFactory = new ComponentVersionsGridEmbeddedDetailsGridWindowFactory();
                    KlFxWindow klWindow = windowFactory.create(KlProfiles.sharedWindowPreferenceFactory(ComponentVersionsGridEmbeddedDetailsGridWindowFactory.class));
                    klWindow.setTitle("Embedded Grid for Versions: " + DateTimeUtil.timeNowSimple());
                    klWindow.context().subscribeDependentContexts();
                    klWindow.show();
                });
        Action componentVersionsGridEmbeddedDetailsGridTest = new Action("Component Versions List test",
                event -> {
                    ComponentVersionsListDetailsAreaWindowFactory windowFactory = new ComponentVersionsListDetailsAreaWindowFactory();
                    KlFxWindow klWindow = windowFactory.create(KlProfiles.sharedWindowPreferenceFactory(ComponentVersionsListDetailsAreaWindowFactory.class));
                    klWindow.setTitle("List for Versions: " + DateTimeUtil.timeNowSimple());
                    klWindow.context().subscribeDependentContexts();
                    klWindow.show();
                });
        Action dynamicLayoutTest = new Action("Knowledge Layout test",
                event -> {
                    KnowledgeLayoutWindowFactory windowFactory = new KnowledgeLayoutWindowFactory();
                    KlFxWindow klWindow = windowFactory.create(KlProfiles.sharedWindowPreferenceFactory(KnowledgeLayoutWindowFactory.class));
                    klWindow.setTitle("Knowledge layout: " + DateTimeUtil.timeNowSimple());
                    klWindow.context().subscribeDependentContexts();
                    klWindow.show();
                });
        return Lists.immutable.of(simpleComponentAreaTest, componentVersionsListDetailsAreaTest,
                componentVersionsGridEmbeddedDetailsGridTest, dynamicLayoutTest);
    }
}
