package dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;

import dev.ikm.komet.layout.*;
import static dev.ikm.komet.layout.KlGadget.*;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.preferences.PreferenceProperty;
import dev.ikm.komet.layout.preferences.PreferencePropertyBoolean;
import dev.ikm.komet.layout.preferences.PreferencePropertyString;
import dev.ikm.komet.layout.window.KlFxWindow;
import dev.ikm.komet.layout.window.KlWindowPane;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.tinkar.common.util.time.DateTimeUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Window;
import javafx.util.Subscription;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.prefs.BackingStoreException;

/**
 * Abstract base class representing a gadget blueprint.
 * <p>
 * This class defines the foundational behavior and state for gadgets, including
 * preference management, initialization, and subscription handling. It is designed
 * to enable both the restoration of gadgets from previously stored preferences and
 * the creation of new gadgets from a factory.
 *
 * @param <T> the type of objects managed or represented by the implementing gadget blueprint
 */
public sealed abstract class GadgetBlueprint<T> implements KlStateCommands, KlContextSensitiveComponent
    permits GadgetWithContextBlueprint, WindowPaneBlueprint {

    protected static final Logger LOG = LoggerFactory.getLogger(GadgetBlueprint.class);

    /**
     * The preferences associated with this {@code GadgetBlueprint} instance.
     * <p>
     * This field is used to store and manage configuration data required
     * by the {@code GadgetBlueprint}. The preferences enable synchronization
     * of state and behavior, and they are utilized for state restoration,
     * initialization, and handling preference changes.
     * <p>
     * {@code preferences} is immutable and is typically provided during object
     * construction. It forms the backbone for both state restoration and updates
     * triggered by preference changes.
     */
    private final KometPreferences preferences;
    /**
     * Holds an atomic reference to the current subscription for managing
     * changes or updates related to preferences associated with this gadget blueprint.
     * <p>
     * The `subscriptionReference` is initialized with an empty subscription and can be
     * updated as new subscriptions are added. This ensures thread-safe handling of
     * preference-related notifications and updates.
     */
    protected final AtomicReference<Subscription> subscriptionReference = new AtomicReference<>(Subscription.EMPTY);

    /**
     * Indicates whether the gadget blueprint has been modified.
     * <p>
     * This property is primarily used to track the state of the blueprint
     * in terms of changes. It is set to {@code true} when modifications
     * to preferences or internal state occur. This allows for monitoring
     * and handling updates related to the blueprint's configuration or behavior.
     */
    private final SimpleBooleanProperty changed = new SimpleBooleanProperty(false);

    /**
     * Represents a boolean preference property indicating whether the
     * gadget blueprint has been initialized.
     * <p>
     * This property is backed by the {@code INITIALIZED} preference key
     * within the {@link PreferenceKeys} enumeration. It is used to track
     * and determine the initialization state of the {@code GadgetBlueprint}.
     * <p>
     * The property maintains synchronization with the preference storage,
     * ensuring that changes made to the preference value are reflected
     * in the property, and vice versa.
     * <p>
     * This field is defined as {@code protected} and {@code final},
     * signaling that it is accessible within the class hierarchy and
     * cannot be reassigned after initialization.
     */
    protected final PreferencePropertyBoolean initialized = PreferenceProperty.booleanProp(klGadget(), PreferenceKeys.INITIALIZED);
    /**
     * Represents a preference-backed property that defines the class name of the factory
     * associated with this {@code GadgetBlueprint}.
     * <p>
     * This property is initialized with a default value or restored from the user's
     * preferences. Changes to the value of this property reflect the factory's class
     * name that influences the gadget's behavior or functionality. It is used
     * internally by the {@code GadgetBlueprint} to maintain and manage the state
     * related to the factory.
     */
    protected final PreferencePropertyString factoryClassName = PreferenceProperty.stringProp(klGadget(), PreferenceKeys.FACTORY_CLASS);
    /**
     * Represents the name used to aid the user in selecting to restore the state of a gadget from preferences.
     * This property is a {@code PreferencePropertyString} bound to a key in the preference
     * storage, allowing the restoration of a gadget's state using a unique identifier.
     * <p>
     * This property is primarily utilized in cases where a previously saved or persisted
     * gadget needs to be reconstructed and reinitialized using its corresponding stored name.
     * <p>
     * The associated preference key is {@code PreferenceKeys.NAME_FOR_RESTORE}, and if no value
     * is present in the preference storage, the property's default value will be used.
     * <p>
     * This attribute is immutable after initialization and serves as a critical component
     * in restoring state consistency in the {@code GadgetBlueprint}.
     */
    protected final PreferencePropertyString  nameForRestore = PreferenceProperty.stringProp(klGadget(), PreferenceKeys.NAME_FOR_RESTORE);

    protected final T fxGadget;
    /**
     * Constructs a new instance of {@code GadgetBlueprint} with the specified preferences
     * and gadget object.
     *
     * This constructor initializes the blueprint with the provided preferences
     * and the given gadget object, while setting up synchronization and proper configuration.
     *
     * @param preferences the {@code KometPreferences} instance associated with this gadget blueprint
     * @param fxGadget the specific gadget object of type {@code T} to be encapsulated within the blueprint. T is checked
     *                 to be either a {@code Window} nor a {@code Node}.
     * @throws IllegalStateException if the provided gadget object is neither a {@code Window} nor a {@code Node}
     */
    public GadgetBlueprint(KometPreferences preferences, T fxGadget) {
        this.preferences = preferences;
        this.fxGadget = fxGadget;
        switch (fxGadget) {
            case Window window -> window.getProperties().put(KlGadget.PropertyKeys.KL_PEER, this);
            case Node node -> node.getProperties().put(KlGadget.PropertyKeys.KL_PEER, this);
            default -> throw new IllegalStateException("Unexpected value: " + fxGadget);
        }
        subscribeToChanges();
        restoreFromPreferencesOrDefaults();
    }

    /**
     * Constructs a new instance of {@code GadgetBlueprint} with the provided preferences gadgetFactory,
     * gadgetFactory instance, and gadget object. This constructor initializes the blueprint with
     * its associated preferences and metadata, ensuring synchronization and proper setup.
     *
     * @param preferencesFactory the gadgetFactory responsible for creating and managing preferences
     * @param gadgetFactory the instance of {@code KlFactory} associated with this gadget blueprint
     * @param fxGadget the specific gadget object to be encapsulated within the blueprint
     */
    public GadgetBlueprint(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory, T fxGadget) {
        this(preferencesFactory.get(), fxGadget);
        initialized.setValue(true);
        factoryClassName.setValue(gadgetFactory.getClass().getName());
        nameForRestore.setValue(gadgetFactory.klGadgetName() + " from " + DateTimeUtil.timeNowSimple());
        try {
            preferences.sync();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public final KlGadget klGadget() {
        return (KlGadget) this;
    }

    public KlObject klObject() {
        return klGadget();
    }

    /**
     * Signals that this instance of {@code KlGadget} needs to unsubscribe from all  {@code KlContext} properties,
     * prior to {@code KlGadget} deletion or reorganization. The specific behavior and implementation of this method
     * are left to the discretion of the implementing class. Calls to {@code KlGadget.unsubscribeFromContext()}
     * must occur in a depth-first manner, staring with the top {@code KlGadget} that will encapsulate all the
     * intended changes. It is not the responsibility of this method to provide the depth-first logic. That responsibility
     * is placed on the {@code KlContext} object which will notify subordinate {@code KlGadget} of an impending change.
     */
    public abstract void unsubscribeFromContext();

    /**
     * Signals this {@code KlGadget} instance to subscribe to any necessary {@code KlContext} properties
     * or events. This method ensures the gadget is actively synchronized with any relevant contextual
     * updates within the knowledge layout system. The specific subscription logic and its scope
     * are left to the implementing class.
     * <p>
     * It is the responsibility of the implementing class to define how and which properties or
     * events of the {@code KlContext} are subscribed to. This provides flexibility for the gadget
     * to interact with its contextual environment according to its requirements.
     */
    public abstract void subscribeToContext();


    /**
     * Retrieves the fxGadget associated with this {@code GadgetBlueprint} instance.
     *
     * @return the {@code fxGadget}, representing the encapsulated gadget or component
     *         associated with this blueprint.
     */
    public final T fxGadget() {
        return fxGadget;
    }

    /**
     * Restores the state of the gadget blueprint from stored preferences or
     * applies default values where preferences are not defined.
     * <p>
     * This method iterates through all preference keys specified in
     * {@code KlGadget.PreferenceKeys} and attempts to load the values
     * from the preference storage. If a preference for a given key
     * is not available, the key's default value is used instead.
     * <p>
     * The specific behavior depending on the key type is as follows:
     * - For the key {@code INITIALIZED}, the preference value is retrieved
     *   as a {@code boolean}, with the default value from the key used if absent.
     * - For the keys {@code FACTORY_CLASS} and {@code NAME_FOR_RESTORE}, the
     *   preference value is retrieved as a {@code String}, with the default
     *   value from the key used if absent.
     */
    private void restoreFromPreferencesOrDefaults() {
        for (PreferenceKeys key : PreferenceKeys.values()) {
            switch (key) {
                case INITIALIZED ->
                        this.initialized.setValue(preferences.getBoolean(key, (Boolean) key.defaultValue()));
                case FACTORY_CLASS ->
                        this.factoryClassName.setValue(preferences.get(key, (String) key.defaultValue()));
                case NAME_FOR_RESTORE ->
                        this.nameForRestore.setValue(preferences.get(key, (String) key.defaultValue()));
            }
        }
    }

    /**
     * Subscribes to changes for all preference keys associated with the gadget blueprint.
     * <p>
     * This method iterates through all preference keys defined in {@code KlGadget.PreferenceKeys}
     * and creates subscriptions for preferences corresponding to each key. The subscriptions
     * trigger the {@code preferencesChanged} method to handle updates when a preference value changes.
     */
    private void subscribeToChanges() {
        for (PreferenceKeys key : PreferenceKeys.values()) {
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
    public final void addPreferenceSubscription(Subscription preferenceSubscription) {
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
    protected BooleanProperty changedProperty() {
        return changed;
    }

    /**
     * Retrieves the property indicating whether this gadget blueprint has been initialized.
     *
     * @return the {@code PreferencePropertyBoolean} representing the initialization state of the gadget blueprint
     */
    public PreferencePropertyBoolean initializedProperty() {
        return initialized;
    }

    /**
     * Retrieves the property representing the factory class name associated with the gadget blueprint.
     *
     * @return the {@code PreferencePropertyString} representing the factory class name
     */
    public PreferencePropertyString factoryClassNameProperty() {
        return factoryClassName;
    }

    /**
     * Retrieves the property representing the name for restoring a gadget's state.
     *
     * @return the {@code PreferencePropertyString} associated with the name for restoring the gadget's state
     */
    public PreferencePropertyString nameForRestoreProperty() {
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

    /**
     * Deletes the preference node associated with this instance and ensures
     * that any changes are properly persisted to the backing store.
     *
     * This method performs the following operations:
     * 1. Removes the preference node tied to the current instance.
     * 2. Flushes the preferences to ensure changes are saved.
     *
     * If the operation fails due to a {@link BackingStoreException}, a runtime
     * exception is thrown to signal an error.
     *
     * @throws RuntimeException if the preferences cannot be removed or flushed
     *                          due to a backing store error
     */
    @Override
    public final void delete() {
        try {
            preferences().removeNode();
            preferences().flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the current state of the window and its preferences to persistent storage.
     *
     * This method iterates over all defined preference keys and updates the associated
     * preferences with the current state of the window properties such as opacity,
     * visibility, location, and size. It ensures that the updated preferences are
     * persisted by flushing the preferences to the backing store.
     *
     * If there are any additional stage-specific preferences to be saved, it delegates
     * that responsibility to the subStageSave method.
     *
     * Upon successful completion of the saving process, the changed property is reset
     * to indicate that there are no unsaved changes.
     *
     * Throws a RuntimeException if an error occurs while flushing preferences to the
     * backing store.
     */
    @Override
    public final void save() {
        try {
            for (PreferenceKeys key : PreferenceKeys.values()) {
                switch (key) {
                    case INITIALIZED -> preferences().putBoolean(key, initialized.getValue());
                    case NAME_FOR_RESTORE -> preferences().put(key, nameForRestore.getValue());
                    case FACTORY_CLASS -> preferences().put(key, factoryClassName.getValue());
                }
            }
            subGadgetSave();
            preferences().flush();
            changedProperty().setValue(false);
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Defines the custom save behavior for subclasses of {@code GadgetBlueprint}.
     *
     * This method is intended to be implemented by subclasses to handle the saving
     * of additional or subclass-specific state properties to preferences or
     * persistent storage. It is invoked during the {@code save} process to ensure
     * that all state associated with the gadget blueprint, including subclass-specific
     * properties, is saved correctly.
     *
     * Subclasses must override this method to provide the concrete logic for saving
     * any additional state that is not handled by the base class. This may include
     * saving data for custom components, properties, or configurations specific to
     * the subclass implementation.
     *
     * The implementation of this method should ensure that:
     * - Relevant state properties are captured and stored persistently.
     * - State consistency is maintained across the application's lifecycle.
     * - Data integrity and error handling are properly managed.
     *
     * This method is abstract to enforce that every subclass provides its own
     * implementation for persisting custom state.
     */
    protected abstract void subGadgetSave();

    /**
     * Reverts the current stage blueprint to its last saved state or default configuration.
     *
     * This method performs the following operations:
     * 1. Restores properties of the stage to their values from user preferences or defaults,
     *    ensuring that the stage location, size, visibility, and other attributes are
     *    reset accordingly.
     * 2. Triggers the `subStageRevert` method, which allows subclasses to implement specific
     *    revert logic for sub-stage components or additional properties.
     *
     * This method is typically invoked to undo changes made to the stage or to restore
     * its state to a consistent baseline, either due to user action or system requirements.
     *
     * The `revert` mechanism ensures that the stage and its subcomponents are aligned with
     * the user's preferences or default configuration.
     *
     * Note: Subclasses must implement {@link #subGadgetRevert()} to define custom
     * revert behavior for specific sub-stage properties or states.
     */
    @Override
    public final void revert() {
        restoreFromPreferencesOrDefaults();
        subGadgetRevert();
    }
    /**
     * Defines the behavior for reverting specific state or properties
     * of a gadget blueprint subclass to its last saved state or default configuration.
     *
     * This method is intended to be implemented by subclasses to handle
     * the restoration of custom or additional state properties specific
     * to the gadget blueprint. It is called during the overall revert
     * process to ensure that all subclass-specific attributes and components
     * are appropriately restored.
     *
     * Subclasses must override this method to provide concrete logic
     * for reverting their custom state changes or additional properties,
     * ensuring alignment with the user's preferences or default values.
     *
     * This is part of the broader revert mechanism, which includes restoring:
     * - General gadget blueprint properties, such as size, location, visibility, etc.
     * - Subclass-specific properties defined by overriding this method.
     *
     * The method is abstract to enforce that subclasses provide a concrete
     * implementation for handling their unique revert requirements.
     */
    protected abstract void subGadgetRevert();

}
