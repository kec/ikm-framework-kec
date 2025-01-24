package dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;

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

/**
 * Abstract base class representing a gadget blueprint.
 *
 * This class defines the foundational behavior and state for gadgets, including
 * preference management, initialization, and subscription handling. It is designed
 * to enable both the restoration of gadgets from previously stored preferences and
 * the creation of new gadgets from a factory.
 *
 * @param <T> the type of objects managed or represented by the implementing gadget blueprint
 */
public abstract class GadgetBlueprint<T> implements KlGadget<T> {


    /**
     * The preferences associated with this {@code GadgetBlueprint} instance.
     *
     * This field is used to store and manage configuration data required
     * by the {@code GadgetBlueprint}. The preferences enable synchronization
     * of state and behavior, and they are utilized for state restoration,
     * initialization, and handling preference changes.
     *
     * {@code preferences} is immutable and is typically provided during object
     * construction. It forms the backbone for both state restoration and updates
     * triggered by preference changes.
     */
    private final KometPreferences preferences;
    /**
     * Holds an atomic reference to the current subscription for managing
     * changes or updates related to preferences associated with this gadget blueprint.
     *
     * The `subscriptionReference` is initialized with an empty subscription and can be
     * updated as new subscriptions are added. This ensures thread-safe handling of
     * preference-related notifications and updates.
     */
    protected final AtomicReference<Subscription> subscriptionReference = new AtomicReference<>(Subscription.EMPTY);

    /**
     * Indicates whether the gadget blueprint has been modified.
     *
     * This property is primarily used to track the state of the blueprint
     * in terms of changes. It is set to {@code true} when modifications
     * to preferences or internal state occur. This allows for monitoring
     * and handling updates related to the blueprint's configuration or behavior.
     */
    private final SimpleBooleanProperty changed = new SimpleBooleanProperty(false);

    /**
     * Represents a boolean preference property indicating whether the
     * gadget blueprint has been initialized.
     *
     * This property is backed by the {@code INITIALIZED} preference key
     * within the {@link PreferenceKeys} enumeration. It is used to track
     * and determine the initialization state of the {@code GadgetBlueprint}.
     *
     * The property maintains synchronization with the preference storage,
     * ensuring that changes made to the preference value are reflected
     * in the property, and vice versa.
     *
     * This field is defined as {@code protected} and {@code final},
     * signaling that it is accessible within the class hierarchy and
     * cannot be reassigned after initialization.
     */
    protected final PreferencePropertyBoolean initialized = PreferenceProperty.booleanProp(this, PreferenceKeys.INITIALIZED);
    /**
     * Represents a preference-backed property that defines the class name of the factory
     * associated with this {@code GadgetBlueprint}.
     *
     * This property is initialized with a default value or restored from the user's
     * preferences. Changes to the value of this property reflect the factory's class
     * name that influences the gadget's behavior or functionality. It is used
     * internally by the {@code GadgetBlueprint} to maintain and manage the state
     * related to the factory.
     */
    protected final PreferencePropertyString factoryClassName = PreferenceProperty.stringProp(this, PreferenceKeys.FACTORY_CLASS);
    /**
     * Represents the name used to aid the user in selecting to restore the state of a gadget from preferences.
     * This property is a {@code PreferencePropertyString} bound to a key in the preference
     * storage, allowing the restoration of a gadget's state using a unique identifier.
     *
     * This property is primarily utilized in cases where a previously saved or persisted
     * gadget needs to be reconstructed and reinitialized using its corresponding stored name.
     *
     * The associated preference key is {@code PreferenceKeys.NAME_FOR_RESTORE}, and if no value
     * is present in the preference storage, the property's default value will be used.
     *
     * This attribute is immutable after initialization and serves as a critical component
     * in restoring state consistency in the {@code GadgetBlueprint}.
     */
    protected final PreferencePropertyString  nameForRestore = PreferenceProperty.stringProp(this, PreferenceKeys.NAME_FOR_RESTORE);

    /**
     * Constructs a new GadgetBlueprint with the specified preferences. This
     * constructor is intended to restore a previously created gadget, not for
     * the initial creation of a brand-new gadget.
     *
     * This constructor initializes the GadgetBlueprint by restoring its
     * state from the given preferences or applying default values when necessary.
     * It also subscribes to changes in preferences to maintain synchronization
     * with the application's state.
     *
     * @param preferences the preferences associated with this GadgetBlueprint instance
     */
    public GadgetBlueprint(KometPreferences preferences) {
        this.preferences = preferences;
        restoreFromPreferencesOrDefaults();
        subscribeToChanges();
    }

    /**
     * Constructs a new GadgetBlueprint with the specified preferences and factory.
     * This constructor is intended for initial creation of a brand-new gadget, not for
     * restoring a previously constructed one.
     * Initializes the GadgetBlueprint and sets initial properties, including the
     * class name and restoration name for the provided factory. Synchronizes the
     * preferences to ensure they're up to date.
     *
     * @param preferences the preferences associated with this GadgetBlueprint instance
     * @param factory the factory used to initialize and configure the GadgetBlueprint
     * @throws RuntimeException if synchronizing preferences fails
     */
    public GadgetBlueprint(KometPreferences preferences, KlFactory factory) {
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

    /**
     * Restores the state of the gadget blueprint from stored preferences or
     * applies default values where preferences are not defined.
     *
     * This method iterates through all preference keys specified in
     * {@code KlGadget.PreferenceKeys} and attempts to load the values
     * from the preference storage. If a preference for a given key
     * is not available, the key's default value is used instead.
     *
     * The specific behavior depending on the key type is as follows:
     * - For the key {@code INITIALIZED}, the preference value is retrieved
     *   as a {@code boolean}, with the default value from the key used if absent.
     * - For the keys {@code FACTORY_CLASS} and {@code NAME_FOR_RESTORE}, the
     *   preference value is retrieved as a {@code String}, with the default
     *   value from the key used if absent.
     */
    private void restoreFromPreferencesOrDefaults() {
        for (KlGadget.PreferenceKeys key : KlGadget.PreferenceKeys.values()) {
            switch (key) {
                case INITIALIZED ->
                        this.preferences.putBoolean(key, preferences.getBoolean(key, (Boolean) key.defaultValue()));
                case FACTORY_CLASS, NAME_FOR_RESTORE ->
                        preferences.put(key, preferences.get(key, (String) key.defaultValue()));
            }
        }
    }

    /**
     * Subscribes to changes for all preference keys associated with the gadget blueprint.
     *
     * This method iterates through all preference keys defined in {@code KlGadget.PreferenceKeys}
     * and creates subscriptions for preferences corresponding to each key. The subscriptions
     * trigger the {@code preferencesChanged} method to handle updates when a preference value changes.
     */
    private void subscribeToChanges() {
        for (KlGadget.PreferenceKeys key : KlGadget.PreferenceKeys.values()) {
            addPreferenceSubscription(switch (key) {
                case INITIALIZED -> this.initialized.subscribe(this::preferencesChanged);
                case FACTORY_CLASS -> this.factoryClassName.subscribe(this::preferencesChanged);
                case NAME_FOR_RESTORE -> this.nameForRestore.subscribe(this::preferencesChanged);
            });

        }
    }

    /**
     * Adds a subscription to preference-related updates for this gadget blueprint.
     *
     * @param preferenceSubscription the subscription to be added that represents
     *                                a listener or handler for preference-related changes
     */
    protected final void addPreferenceSubscription(Subscription preferenceSubscription) {
        this.subscriptionReference.set(this.subscriptionReference.get().and(preferenceSubscription));
    }

    /**
     * Notifies that the preferences associated with this gadget blueprint have been changed.
     * This method updates the internal state to reflect that the blueprint has been modified
     * by setting the {@code changed} property to {@code true}.
     */
    public void preferencesChanged() {
        changed.set(true);
    }

    /**
     * Retrieves the property indicating whether the gadget blueprint has undergone changes.
     *
     * @return the {@code BooleanProperty} representing the change state of the gadget blueprint.
     */
    BooleanProperty changedProperty() {
        return changed;
    }

    /**
     * Retrieves the property indicating whether this gadget blueprint has been initialized.
     *
     * @return the {@code PreferencePropertyBoolean} representing the initialization state of the gadget blueprint
     */
    PreferencePropertyBoolean initializedPropertyGetter() {
        return initialized;
    }

    /**
     * Retrieves the property representing the factory class name associated with the gadget blueprint.
     *
     * @return the {@code PreferencePropertyString} representing the factory class name
     */
    PreferencePropertyString factoryClassNamePropertyGetter() {
        return factoryClassName;
    }

    /**
     * Retrieves the property representing the name for restoring a gadget's state.
     *
     * @return the {@code PreferencePropertyString} associated with the name for restoring the gadget's state
     */
    PreferencePropertyString nameForRestorePropertyGetter() {
        return nameForRestore;
    }

    /**
     * Retrieves the preferences associated with this GadgetBlueprint instance.
     *
     * @return the preferences configured for this gadget blueprint
     */
    public KometPreferences preferences() {
        return preferences;
    }

    @Override
    public Class<? extends KlFactory> factoryClass() {
        try {
            return (Class<? extends KlFactory>) Class.forName(this.factoryClassName.getValue());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
