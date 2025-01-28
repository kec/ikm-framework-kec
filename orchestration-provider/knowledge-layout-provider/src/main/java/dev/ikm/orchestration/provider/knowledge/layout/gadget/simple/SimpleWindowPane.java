package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.preferences.PropertyWithDefault;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.WindowPaneBlueprint;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ikm.komet.layout.preferences.PreferenceProperty.INITIAL_STRING_VALUE;

public class SimpleWindowPane extends WindowPaneBlueprint {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWindowPane.class);
    enum PreferenceKeys implements PropertyWithDefault {
        LABEL_TEXT(INITIAL_STRING_VALUE);

        Object defaultValue;
        PreferenceKeys(String initialStringValue) {
            this.defaultValue = initialStringValue;
        }

        @Override
        public Object defaultValue() {
            return this.defaultValue;
        }
    }
    protected SimpleWindowPane(KometPreferences preferences) {
        super(preferences);
        simpleSetup();
    }

    protected SimpleWindowPane(KlPreferencesFactory preferencesFactory, KlFactory factory) {
        super(preferencesFactory, factory);
        simpleSetup();
    }


    private void simpleSetup() {
        fxGadget().setCenter(new Label(PreferenceKeys.LABEL_TEXT.defaultValue().toString()));
    }

    @Override
    protected void subPaneSave() {
        // Nothing to do
    }

    @Override
    protected void subPaneRevert() {
        // Nothing to do
    }
}
