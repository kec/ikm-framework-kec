package dev.ikm.orchestration.provider.knowledge.layout.field.simple;

import dev.ikm.komet.framework.observable.ComponentFieldLocator;
import dev.ikm.komet.framework.observable.FieldCategory;
import dev.ikm.komet.framework.observable.FieldLocator;
import dev.ikm.komet.layout.GridLayoutForComponent;
import dev.ikm.komet.layout.GridLayoutForComponentFactory;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.component.field.KlFieldArea;
import dev.ikm.komet.layout.component.field.KlListOfVersionsFieldArea;
import dev.ikm.komet.layout.component.field.KlListOfVersionsFieldAreaFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.field.blueprint.FieldAreaBlueprint;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.scene.control.ListView;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.map.ImmutableMap;

import java.util.List;


public class SimpleVersionList extends FieldAreaBlueprint<ListView, List<EntityVersion>>
    implements KlListOfVersionsFieldArea<List<EntityVersion>, EntityVersion, ListView> {

    public static class SimpleVersionListFactory implements KlListOfVersionsFieldAreaFactory<EntityVersion, List<EntityVersion>, ListView> {

        @Override
        public KlFieldArea<List<EntityVersion>, ListView> create(KlPreferencesFactory preferencesFactory) {
            return new SimpleVersionList(preferencesFactory, this);
        }

        @Override
        public KlFieldArea<List<EntityVersion>, ListView> create(KlPreferencesFactory preferencesFactory,
                                                                 GridLayoutForComponentFactory gridLayoutForComponentFactory) {
            SimpleVersionList simpleVersionList = new SimpleVersionList(preferencesFactory, this);
            ComponentFieldLocator versionsLocator = new ComponentFieldLocator(FieldCategory.COMPONENT_VERSIONS_LIST);
            ImmutableMap<FieldLocator, GridLayoutForComponent> layoutMap = gridLayoutForComponentFactory.create(
                    Lists.immutable.of(versionsLocator));

            simpleVersionList.setGridLayout(layoutMap.get(versionsLocator).gridLayout());
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public KlFieldArea<List<EntityVersion>, ListView> restore(KometPreferences preferences) {
            return new SimpleVersionList(preferences);
        }

        @Override
        public Class<? extends KlFieldArea<List<EntityVersion>, ListView>> klImplementationClass() {
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
    protected void updateField() {

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
