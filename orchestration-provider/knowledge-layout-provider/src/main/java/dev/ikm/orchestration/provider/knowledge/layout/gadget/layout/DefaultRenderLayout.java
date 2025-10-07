package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.komet.layout.KnowledgeLayout;
import dev.ikm.komet.layout.LayoutKey;
import dev.ikm.komet.layout.LayoutOverrides;
import dev.ikm.komet.preferences.KometPreferences;

import java.util.UUID;

public class DefaultRenderLayout implements KnowledgeLayout {
     final LayoutKey.ForArea layoutKey;
     final LayoutOverrides overrides;

    public DefaultRenderLayout(UUID layoutId, LayoutOverrides overrides) {
        this.layoutKey = LayoutKey.makeTopArea(layoutId);
        this.overrides = overrides;
    }

    @Override
    public LayoutOverrides layoutOverrides() {
        return overrides;
    }

    @Override
    public LayoutKey.ForArea rootLayoutKey() {
        return layoutKey;
    }

    @Override
    public void save() {
        overrides.save();
    }
}
