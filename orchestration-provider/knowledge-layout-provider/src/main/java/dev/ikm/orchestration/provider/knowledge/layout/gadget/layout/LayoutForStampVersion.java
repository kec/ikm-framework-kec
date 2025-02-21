package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.komet.framework.observable.ComponentFields;
import dev.ikm.komet.framework.observable.SemanticFields;
import dev.ikm.komet.framework.observable.StampFields;
import org.eclipse.collections.api.map.ImmutableMap;

public record LayoutForStampVersion(ImmutableMap<ComponentFields, FieldLayoutRecord> componentFieldsLayout,
                                    ImmutableMap<SemanticFields, FieldLayoutRecord> semanticFieldsLayout,
                                    ImmutableMap<StampFields, FieldLayoutRecord>  stampFieldsLayout) {

}
