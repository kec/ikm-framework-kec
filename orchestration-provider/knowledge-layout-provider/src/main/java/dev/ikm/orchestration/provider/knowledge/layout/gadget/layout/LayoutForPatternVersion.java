package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.komet.framework.observable.ComponentFields;
import dev.ikm.komet.framework.observable.PatternFields;
import dev.ikm.komet.framework.observable.StampFields;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;

public record LayoutForPatternVersion(ImmutableMap<ComponentFields, FieldLayoutRecord> componentFieldsLayout,
                                      ImmutableMap<PatternFields, FieldLayoutRecord> patternFieldsLayout,
                                      ImmutableList<FieldLayoutRecord> fieldDefinitionElementsLayout,
                                      ImmutableMap<StampFields, FieldLayoutRecord>  stampFieldsLayout) {
}
