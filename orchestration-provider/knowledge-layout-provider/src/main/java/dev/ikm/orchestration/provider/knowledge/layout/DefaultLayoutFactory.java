package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.framework.observable.AttributeLocator;
import dev.ikm.komet.layout.LayoutComputer;
import dev.ikm.komet.layout.area.GridLayout;
import dev.ikm.komet.layout.area.factory.KlAttributeAreaFactory;
import dev.ikm.komet.layout.area.factory.KlDynamicAreaFactory;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.GenericAttribute;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.PublicIdAttribute;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.SimpleVersionList;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultLayoutFactory implements LayoutComputer {
    @Override
    public ImmutableList<KlDynamicAreaFactory> create(ImmutableList<AttributeLocator> attributeLocators) {
        MutableList<KlDynamicAreaFactory> layoutList = Lists.mutable.empty();
        AtomicInteger row = new AtomicInteger(0);
        attributeLocators.forEach(attributeLocator -> {
            switch (attributeLocator.category()) {
                case STATUS_FIELD, TIME_FIELD, AUTHOR_FIELD, MODULE_FIELD, PATH_FIELD,
                     PATTERN_MEANING_FIELD, PATTERN_PURPOSE_FIELD,
                     SEMANTIC_PATTERN_FIELD, SEMANTIC_REFERENCED_COMPONENT_FIELD,
                     PATTERN_FIELD_DEFINITION, SEMANTIC_FIELD ->
                    layoutList.add(
                            new KlAttributeAreaFactory(GenericAttribute.Factory.class.getName(),
                                    attributeLocator,
                                    GridLayout.DEFAULT.withRowIndex(row.getAndIncrement()),
                                    Lists.immutable.empty()));

                case PUBLIC_ID_FIELD -> layoutList.add(
                        new KlAttributeAreaFactory(PublicIdAttribute.Factory.class.getName(),
                                attributeLocator,
                                GridLayout.DEFAULT.withRowIndex(row.getAndIncrement()),
                                Lists.immutable.empty()));

                case COMPONENT_VERSIONS_LIST -> layoutList.add(
                        new KlAttributeAreaFactory(SimpleVersionList.SimpleVersionListFactory.class.getName(),
                                attributeLocator,
                                GridLayout.DEFAULT.withRowIndex(row.getAndIncrement()),
                                Lists.immutable.empty()));

                case PATTERN_FIELD_DEFINITION_LIST, SEMANTIC_FIELD_LIST -> {
                    /* Field list factory */
                }
            }
        });

        return layoutList.toImmutable();
    }
}
