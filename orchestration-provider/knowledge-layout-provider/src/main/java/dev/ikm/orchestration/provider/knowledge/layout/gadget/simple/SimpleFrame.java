package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.FrameBlueprint;

public class SimpleFrame extends FrameBlueprint {

    protected SimpleFrame(KometPreferences preferences) {
        super(preferences);
        simpleSetup();
    }

    protected SimpleFrame(KlPreferencesFactory preferencesFactory, KlFactory factory) {
        super(preferencesFactory, factory);
        simpleSetup();
    }

    private void simpleSetup() {
        changedProperty().addListener(obs -> simpleUpdate());
        simpleUpdate();
    }

    protected void simpleUpdate() {
        // Any context-sensitive updates can be added here.
    }

    @Override
    protected void subPaneSave() {
        // Nothing to do
    }

    @Override
    protected void subPaneRevert() {
        // Nothing to do
    }

    @Override
    public void subscribeToContext() {
        contextSubscriptionReference.get().and(context().viewCoordinate().subscribe(this::simpleUpdate));
    }
}
