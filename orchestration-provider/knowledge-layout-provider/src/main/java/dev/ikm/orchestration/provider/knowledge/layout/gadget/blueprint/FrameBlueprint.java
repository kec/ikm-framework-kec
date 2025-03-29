package dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.preferences.PreferenceProperty;
import dev.ikm.komet.layout.preferences.PreferencePropertyDouble;
import dev.ikm.komet.layout.window.KlFrame;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ikm.komet.layout.window.KlFrame.PreferenceKeys.*;

public abstract non-sealed class FrameBlueprint extends GadgetBlueprint<BorderPane> implements KlFrame {
    private static final Logger LOG = LoggerFactory.getLogger(FrameBlueprint.class);

    //TODO: can we use the widget properties directly instead of creating our own here?
    private final PreferencePropertyDouble translateX = PreferenceProperty.doubleProp(this, TRANSLATE_X);
    private final PreferencePropertyDouble translateY = PreferenceProperty.doubleProp(this, TRANSLATE_Y);
    private final PreferencePropertyDouble translateZ = PreferenceProperty.doubleProp(this, TRANSLATE_Z);
    private final PreferencePropertyDouble scaleX = PreferenceProperty.doubleProp(this, SCALE_X);
    private final PreferencePropertyDouble scaleY = PreferenceProperty.doubleProp(this, SCALE_Y);
    private final PreferencePropertyDouble scaleZ = PreferenceProperty.doubleProp(this, SCALE_Z);
    private final PreferencePropertyDouble rotate = PreferenceProperty.doubleProp(this, ROTATE);

    protected FrameBlueprint(KometPreferences preferences) {
        super(preferences, new BorderPane());
        setup();
    }

    protected FrameBlueprint(KlPreferencesFactory preferencesFactory, KlFactory factory) {
        super(preferencesFactory, factory, new BorderPane());
        setup();
    }

    private void setup() {
        subscribeToChanges();
        restoreFromPreferencesOrDefaults();
    }
    private void restoreFromPreferencesOrDefaults() {
        for (KlFrame.PreferenceKeys key : KlFrame.PreferenceKeys.values()) {
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
        for (KlFrame.PreferenceKeys key : KlFrame.PreferenceKeys.values()) {
            addPreferenceSubscription(switch (key)  {
                case TRANSLATE_X -> translateX.subscribe(num -> fxObject().translateXProperty().set(num.doubleValue()))
                        .and(fxObject().translateXProperty().subscribe(num -> translateX.setValue(num.doubleValue())));
                case TRANSLATE_Y -> translateY.subscribe(num -> fxObject().translateYProperty().set(num.doubleValue()))
                        .and(fxObject().translateYProperty().subscribe(num -> translateY.setValue(num.doubleValue())));
                case TRANSLATE_Z -> translateZ.subscribe(num -> fxObject().translateZProperty().set(num.doubleValue()))
                        .and(fxObject().translateZProperty().subscribe(num -> translateZ.setValue(num.doubleValue())));
                case SCALE_X -> scaleX.subscribe(num -> fxObject().scaleXProperty().set(num.doubleValue()))
                        .and(fxObject().scaleXProperty().subscribe(num -> scaleX.setValue(num.doubleValue())));
                case SCALE_Y -> scaleY.subscribe(num -> fxObject().scaleYProperty().set(num.doubleValue()))
                        .and(fxObject().scaleYProperty().subscribe(num -> scaleY.setValue(num.doubleValue())));
                case SCALE_Z -> scaleZ.subscribe(num -> fxObject().scaleZProperty().set(num.doubleValue()))
                        .and(fxObject().scaleZProperty().subscribe(num -> scaleZ.setValue(num.doubleValue())));
                case ROTATE -> rotate.subscribe(num -> fxObject().rotateProperty().set(num.doubleValue()))
                        .and(fxObject().rotateProperty().subscribe(num -> rotate.setValue(num.doubleValue())));
            });
        }
    }

    @Override
    public BorderPane root() {
        return fxObject;
    }

    @Override
    public final void subGadgetSave() {
        for (KlFrame.PreferenceKeys key : KlFrame.PreferenceKeys.values()) {
            switch (key) {
                case TRANSLATE_X -> preferences().putDouble(key, translateX.doubleValue());
                case TRANSLATE_Y -> preferences().putDouble(key, translateY.doubleValue());
                case TRANSLATE_Z -> preferences().putDouble(key, translateZ.doubleValue());
                case SCALE_X -> preferences().putDouble(key, scaleX.doubleValue());
                case SCALE_Y -> preferences().putDouble(key, scaleY.doubleValue());
                case SCALE_Z -> preferences().putDouble(key, scaleZ.doubleValue());
                case ROTATE -> preferences().putDouble(key, rotate.doubleValue());
            }
        }
        subPaneSave();
    }

    protected abstract void subPaneSave();

    @Override
    protected void subGadgetRevert() {
        restoreFromPreferencesOrDefaults();
        subPaneRevert();
    }
    protected abstract void subPaneRevert();

}
