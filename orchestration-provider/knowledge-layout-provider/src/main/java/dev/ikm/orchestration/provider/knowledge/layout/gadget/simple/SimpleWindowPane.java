package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.WindowPaneBlueprint;

public class SimpleWindowPane extends WindowPaneBlueprint {

    protected SimpleWindowPane(KometPreferences preferences) {
        super(preferences);
        simpleSetup();
    }

    protected SimpleWindowPane(KlPreferencesFactory preferencesFactory, KlFactory factory) {
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
