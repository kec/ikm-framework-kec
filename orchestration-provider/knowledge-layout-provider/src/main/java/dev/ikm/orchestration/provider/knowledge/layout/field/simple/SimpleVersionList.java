package dev.ikm.orchestration.provider.knowledge.layout.field.simple;

import dev.ikm.komet.framework.observable.AttributeCategory;
import dev.ikm.komet.framework.observable.AttributeLocator;
import dev.ikm.komet.framework.observable.DirectSingularAttributeLocator;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.LayoutComputer;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.area.factory.KlDynamicAreaFactory;
import dev.ikm.komet.layout.attribute.factory.KlListOfVersionsFieldAreaFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.field.blueprint.AttributeListAreaBlueprint;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.scene.control.ListView;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;


public class SimpleVersionList extends AttributeListAreaBlueprint<ObservableVersion<EntityVersion>> {

    public static class SimpleVersionListFactory implements KlListOfVersionsFieldAreaFactory<ObservableVersion<EntityVersion>, ListView> {

        @Override
        public SimpleVersionList create(KlPreferencesFactory preferencesFactory) {
            return new SimpleVersionList(preferencesFactory, this);
        }

        @Override
        public SimpleVersionList create(KlPreferencesFactory preferencesFactory,
                                        LayoutComputer layoutComputer) {
            SimpleVersionList simpleVersionList = new SimpleVersionList(preferencesFactory, this);
            DirectSingularAttributeLocator versionsLocator = AttributeLocator.direct.singular(AttributeCategory.COMPONENT_VERSIONS_LIST);
            ImmutableList<KlDynamicAreaFactory> layoutList = layoutComputer.create(
                    Lists.immutable.of(versionsLocator));

            simpleVersionList.setGridLayout(layoutList.getOnly().gridLayout());
            return simpleVersionList;
        }

        @Override
        public SimpleVersionList restore(KometPreferences preferences) {
            return new SimpleVersionList(preferences);
        }

        @Override
        public Class<? extends SimpleVersionList> klImplementationClass() {
            return SimpleVersionList.class;
        }
    }

    public SimpleVersionList(KometPreferences preferences) {
        super(preferences, new ListView<>());
    }

    public SimpleVersionList(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new ListView<>());
    }

    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }

    @Override
    public void subscribeToContext() {

    }
}
