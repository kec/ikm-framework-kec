package dev.ikm.orchestration.provider.knowledge.layout.component;

import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.SimpleKnowledgeLayout;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.collections.ObservableList;

public class DynamicComponentArea extends ComponentAreaBlueprint {

    final SimpleKnowledgeLayout layout = new SimpleKnowledgeLayout(this);

    public DynamicComponentArea(KometPreferences preferences) {
        super(preferences);
    }

    public DynamicComponentArea(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory);
    }

    @Override
    protected void componentChanged(ObservableEntity<ObservableVersion<EntityVersion>> oldValue, ObservableEntity<ObservableVersion<EntityVersion>> newValue) {
        //TODO: implement
    }

    @Override
    public ObservableList<ObservableVersion<EntityVersion>> selectedVersions() {
        throw new UnsupportedOperationException("Not supported yet.");
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


