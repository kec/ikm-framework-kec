package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.komet.framework.observable.ComponentFields;
import dev.ikm.komet.framework.observable.SemanticFields;
import dev.ikm.komet.framework.observable.StampFields;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;

public record LayoutForSemanticVersion(ImmutableMap<ComponentFields, FieldLayoutRecord> componentFieldsLayout,
                                       ImmutableMap<SemanticFields, FieldLayoutRecord> semanticFieldsLayout,
                                       ImmutableList<FieldLayoutRecord> fieldElementsLayout,
                                       ImmutableMap<StampFields, FieldLayoutRecord>  stampFieldsLayout) {
}
