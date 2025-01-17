package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.KlGadget;
import dev.ikm.komet.layout.preferences.PreferenceProp;
import dev.ikm.komet.layout.preferences.PreferenceProperty;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.tinkar.common.util.time.DateTimeUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Subscription;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class GadgetAbstract<T> implements KlGadget<T> {


    private final KometPreferences preferences;
    protected final AtomicReference<Subscription> subscriptionReference = new AtomicReference<>(Subscription.EMPTY);

    private final SimpleBooleanProperty changed = new SimpleBooleanProperty(false);

    @PreferenceProp
    private final SimpleBooleanProperty initialized = new SimpleBooleanProperty(false);
    private final SimpleStringProperty factoryClassName = new SimpleStringProperty("uninitialized");
    private final SimpleStringProperty nameForRestore = new SimpleStringProperty("uninitialized");

    public GadgetAbstract(KometPreferences preferences, KlFactory factory) {
        this.preferences = preferences;
        initializeGadgetPreferences(factory);
    }

    public GadgetAbstract(KometPreferences preferences) {
        this.preferences = preferences;
        throw new UnsupportedOperationException("Need to initialize subscription properly... ");
        //restoreGadgetPreferences();
    }

    protected final void initializeGadgetPreferences(KlFactory factory) {
        for (GadgetKeys key : GadgetKeys.values()) {
            Subscription preferenceSubscription = switch (key) {
                case INITIALIZED -> {
                    this.initialized.set(true);
                    this.preferences.putBoolean(key, this.initialized.get());
                    yield this.initialized.subscribe(this::preferencesChanged);
                }
                case FACTORY_CLASS -> {
                    this.factoryClassName.set(factory.getClass().getName());
                    preferences.put(key, this.factoryClassName.getValue());
                    yield this.factoryClassName.subscribe(this::preferencesChanged);
                }
                case NAME_FOR_RESTORE -> {
                    this.nameForRestore.set(factory.klName() + " from " + DateTimeUtil.timeNowSimple());
                    preferences.put(key, this.nameForRestore.get());
                    yield this.nameForRestore.subscribe(this::preferencesChanged);
                }
            };
            subscriptionReference.set(subscriptionReference.get().and(preferenceSubscription));
        }
        classInitialize();
    }

    public void preferencesChanged() {
        changed.set(true);
    }

    BooleanProperty changedProperty() {
        return changed;
    }
    BooleanProperty initializedPropertyGetter() {
        return initialized;
    }
    StringProperty factoryClassNamePropertyGetter() {
        return factoryClassName;
    }
    StringProperty nameForRestorePropertyGetter() {
        return nameForRestore;
    }

    public KometPreferences preferences() {
        return preferences;
    }

    Class<? extends KlFactory> getFactoryClass() {
        Optional<String> factoryClassString =  preferences().get(GadgetKeys.FACTORY_CLASS);
        if (factoryClassString.isPresent()) {
            try {
                return (Class<? extends KlFactory>) Class.forName(factoryClassString.get());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalStateException("Preferences not initialized with Keys.FACTORY_CLASS");
    }



    abstract void classInitialize();

}
