package dev.ikm.orchestration.provider.knowledge.layout.component;

import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.SimpleKnowledgeLayout;
import dev.ikm.tinkar.entity.EntityVersion;

public class DynamicChronologyArea extends ChronologyAreaBlueprint {

    final SimpleKnowledgeLayout layout = new SimpleKnowledgeLayout(this);

    public DynamicChronologyArea(KometPreferences preferences) {
        super(preferences);
    }

    public DynamicChronologyArea(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory);
    }

    @Override
    protected void componentChanged(ObservableEntity<ObservableVersion<EntityVersion>> oldValue, ObservableEntity<ObservableVersion<EntityVersion>> newValue) {
        //TODO: implement
        LOG.info("Component changed description: " + context().viewCoordinate().calculator().getDescriptionTextOrNid(newValue));
        LOG.info("Component changed: " + newValue);
    }

    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }
}


