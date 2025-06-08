package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.komet.layout.KnowledgeLayout;
import dev.ikm.komet.layout.LayoutKey;
import dev.ikm.komet.layout.LayoutOverrides;

import java.util.UUID;

public class DefaultRenderLayout implements KnowledgeLayout {
     final LayoutKey.ForArea layoutKey;

    public DefaultRenderLayout(UUID layoutId) {
        this.layoutKey = LayoutKey.makeTopArea(layoutId);
    }

    @Override
    public LayoutOverrides layoutOverrides() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LayoutKey.ForArea rootLayoutKey() {
        return layoutKey;
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException();
    }
}
