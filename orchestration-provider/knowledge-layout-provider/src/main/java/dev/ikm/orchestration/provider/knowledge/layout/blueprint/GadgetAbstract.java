package dev.ikm.orchestration.provider.knowledge.layout.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.KlGadget;
import dev.ikm.komet.layout.preferences.PreferenceProperty;
import dev.ikm.komet.layout.preferences.PreferencePropertyBoolean;
import dev.ikm.komet.layout.preferences.PreferencePropertyString;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.tinkar.common.util.time.DateTimeUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Subscription;

import java.util.concurrent.atomic.AtomicReference;
import java.util.prefs.BackingStoreException;

public abstract class GadgetAbstract<T> implements KlGadget<T> {


    private final KometPreferences preferences;
    protected final AtomicReference<Subscription> subscriptionReference = new AtomicReference<>(Subscription.EMPTY);

    private final SimpleBooleanProperty changed = new SimpleBooleanProperty(false);

    protected final PreferencePropertyBoolean initialized = PreferenceProperty.booleanProp(this, PreferenceKeys.INITIALIZED);
    protected final PreferencePropertyString factoryClassName = PreferenceProperty.stringProp(this, PreferenceKeys.FACTORY_CLASS);
    protected final PreferencePropertyString  nameForRestore = PreferenceProperty.stringProp(this, PreferenceKeys.NAME_FOR_RESTORE);

    public GadgetAbstract(KometPreferences preferences) {
        this.preferences = preferences;
        restoreFromPreferencesOrDefaults();
        subscribeToChanges();
        classInitialize();
    }

    public GadgetAbstract(KometPreferences preferences, KlFactory factory) {
        this(preferences);
        initialized.setValue(true);
        factoryClassName.setValue(factory.getClass().getName());
        nameForRestore.setValue(factory.klGadgetName() + " from " + DateTimeUtil.timeNowSimple());
        try {
            preferences.sync();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }
    protected void restoreFromPreferencesOrDefaults() {
        for (KlGadget.PreferenceKeys key : KlGadget.PreferenceKeys.values()) {
            switch (key) {
                case INITIALIZED ->
                        this.preferences.putBoolean(key, preferences.getBoolean(key, (Boolean) key.defaultValue()));
                case FACTORY_CLASS, NAME_FOR_RESTORE ->
                        preferences.put(key, preferences.get(key, (String) key.defaultValue()));
            };
        }
    }

    protected void subscribeToChanges() {
        for (KlGadget.PreferenceKeys key : KlGadget.PreferenceKeys.values()) {
            Subscription preferenceSubscription = switch (key) {
                case INITIALIZED -> this.initialized.subscribe(this::preferencesChanged);
                case FACTORY_CLASS -> this.factoryClassName.subscribe(this::preferencesChanged);
                case NAME_FOR_RESTORE -> this.nameForRestore.subscribe(this::preferencesChanged);
            };
            subscriptionReference.set(subscriptionReference.get().and(preferenceSubscription));
        }
    }

    public void preferencesChanged() {
        changed.set(true);
    }

    BooleanProperty changedProperty() {
        return changed;
    }
    PreferencePropertyBoolean initializedPropertyGetter() {
        return initialized;
    }
    PreferencePropertyString factoryClassNamePropertyGetter() {
        return factoryClassName;
    }
    PreferencePropertyString nameForRestorePropertyGetter() {
        return nameForRestore;
    }

    public KometPreferences preferences() {
        return preferences;
    }

    Class<? extends KlFactory> getFactoryClass() {
        try {
            return (Class<? extends KlFactory>) Class.forName(this.factoryClassName.getValue());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    abstract void classInitialize();

}
