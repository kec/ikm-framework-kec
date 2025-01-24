package dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.preferences.PreferenceProperty;
import dev.ikm.komet.layout.preferences.PreferencePropertyDouble;
import dev.ikm.komet.layout.window.KlWindowPane;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ikm.komet.layout.window.KlWindowPane.PreferenceKeys.*;

public class WindowPaneBlueprint<P extends BorderPane> extends GadgetBlueprint<P> implements KlWindowPane<P> {
    private static final Logger LOG = LoggerFactory.getLogger(WindowPaneBlueprint.class);

    private BorderPane root = new BorderPane();

    private final PreferencePropertyDouble translateX = PreferenceProperty.doubleProp(this, TRANSLATE_X);
    private final PreferencePropertyDouble translateY = PreferenceProperty.doubleProp(this, TRANSLATE_Y);
    private final PreferencePropertyDouble translateZ = PreferenceProperty.doubleProp(this, TRANSLATE_Z);
    private final PreferencePropertyDouble scaleX = PreferenceProperty.doubleProp(this, SCALE_X);
    private final PreferencePropertyDouble scaleY = PreferenceProperty.doubleProp(this, SCALE_Y);
    private final PreferencePropertyDouble scaleZ = PreferenceProperty.doubleProp(this, SCALE_Z);
    private final PreferencePropertyDouble rotate = PreferenceProperty.doubleProp(this, ROTATE);

    protected WindowPaneBlueprint(KometPreferences preferences) {
        super(preferences);
        setup();
    }

    protected WindowPaneBlueprint(KlPreferencesFactory preferencesFactory, KlFactory factory) {
        super(preferencesFactory, factory);
        setup();
    }

    private void setup() {
        restoreFromPreferencesOrDefaults();
        subscribeToChanges();
    }
    private void restoreFromPreferencesOrDefaults() {
        for (KlWindowPane.PreferenceKeys key : KlWindowPane.PreferenceKeys.values()) {
            switch (key) {
                case TRANSLATE_X -> translateX.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
                case TRANSLATE_Y -> translateY.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
                case TRANSLATE_Z -> translateZ.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
                case SCALE_X -> scaleX.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
                case SCALE_Y -> scaleY.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
                case SCALE_Z -> scaleZ.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
                case ROTATE -> rotate.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
            }
        }
    }
    private void subscribeToChanges() {
        for (KlWindowPane.PreferenceKeys key : KlWindowPane.PreferenceKeys.values()) {
            addPreferenceSubscription(switch (key)  {
                case TRANSLATE_X -> translateX.subscribe(num -> root.translateXProperty().set(num.doubleValue()))
                        .and(root.translateXProperty().subscribe(num -> translateX.setValue(num.doubleValue())));
                case TRANSLATE_Y -> translateY.subscribe(num -> root.translateYProperty().set(num.doubleValue()))
                        .and(root.translateYProperty().subscribe(num -> translateY.setValue(num.doubleValue())));
                case TRANSLATE_Z -> translateZ.subscribe(num -> root.translateZProperty().set(num.doubleValue()))
                        .and(root.translateZProperty().subscribe(num -> translateZ.setValue(num.doubleValue())));
                case SCALE_X -> scaleX.subscribe(num -> root.scaleXProperty().set(num.doubleValue()))
                        .and(root.scaleXProperty().subscribe(num -> scaleX.setValue(num.doubleValue())));
                case SCALE_Y -> scaleY.subscribe(num -> root.scaleYProperty().set(num.doubleValue()))
                        .and(root.scaleYProperty().subscribe(num -> scaleY.setValue(num.doubleValue())));
                case SCALE_Z -> scaleZ.subscribe(num -> root.scaleZProperty().set(num.doubleValue()))
                        .and(root.scaleZProperty().subscribe(num -> scaleZ.setValue(num.doubleValue())));
                case ROTATE -> rotate.subscribe(num -> root.rotateProperty().set(num.doubleValue()))
                        .and(root.rotateProperty().subscribe(num -> rotate.setValue(num.doubleValue())));
            });
        }
    }

    @Override
    public P root() {
        return (P) root;
    }

    @Override
    public P klGadget() {
        return (P) root;
    }
}
