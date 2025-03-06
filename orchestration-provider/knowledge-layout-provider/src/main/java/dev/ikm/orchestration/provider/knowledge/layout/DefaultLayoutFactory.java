package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.framework.observable.FieldLocator;
import dev.ikm.komet.layout.GridLayout;
import dev.ikm.komet.layout.GridLayoutForComponent;
import dev.ikm.komet.layout.GridLayoutForComponentFactory;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.GenericField;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultLayoutFactory implements GridLayoutForComponentFactory {
    @Override
    public ImmutableMap<FieldLocator, GridLayoutForComponent> create(ImmutableList<FieldLocator> componentFieldSpecifications) {
        MutableMap<FieldLocator, GridLayoutForComponent> layoutMap = Maps.mutable.empty();
        AtomicInteger row = new AtomicInteger(0);
        componentFieldSpecifications.forEach(componentFieldSpecification -> {
            switch (componentFieldSpecification.category()) {
                case STATUS_FIELD, TIME_FIELD, AUTHOR_FIELD, MODULE_FIELD, PATH_FIELD,
                     PATTERN_MEANING_FIELD, PATTERN_PURPOSE_FIELD,
                     SEMANTIC_PATTERN_FIELD, SEMANTIC_REFERENCED_COMPONENT_FIELD,
                     PATTERN_FIELD_DEFINITION, SEMANTIC_FIELD ->
                    layoutMap.put(componentFieldSpecification,
                            new GridLayoutForComponent(GenericField.GenericFactory.class.getName(),
                                    componentFieldSpecification,
                                    GridLayout.DEFAULT.withRowIndex(row.getAndIncrement())));

                case PUBLIC_ID_FIELD -> {
                    /* Identicon factory */
                }

                case COMPONENT_VERSIONS_LIST -> {
                    /* Version list factory */
                    // SimpleComponentPane ?
                }

                case PATTERN_FIELD_DEFINITION_LIST, SEMANTIC_FIELD_LIST -> {
                    /* Field list factory */
                }
            }
        });

        return layoutMap.toImmutable();
    }
}
