package dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.KlView;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.preferences.PreferencePropertyObject;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.tinkar.coordinate.view.ViewCoordinateRecord;
import dev.ikm.tinkar.coordinate.view.calculator.ViewCalculator;
import dev.ikm.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;
import javafx.scene.layout.BorderPane;

import static dev.ikm.komet.layout.KlView.PreferenceKeys.VIEW_COORDINATE;


/**
 * The `ViewBlueprint` class serves as a blueprint for managing a view coordinate,
 * and installing a view coordinate calculator into the {@code Node} hierarchy
 * using a `BorderPane` UI component. It extends the `GadgetBlueprint` class with
 * a specific focus on `BorderPane` and implements the `KlView` interface to define
 * specific behaviors and interactions for the view. This class provides methods to
 * initialize and synchronize properties with user preferences, as well as dynamically
 * update the view based on changes in preferences.
 */
public abstract class ViewBlueprint extends GadgetBlueprint<BorderPane> implements KlView {

    /**
     * Represents a preference property object bound to the `VIEW_COORDINATE` key
     * within the `ViewBlueprint` class. This object stores and manages the view
     * coordinate configuration for this instance, linking it to the associated
     * preferences to ensure synchronization between user-defined settings and
     * default values.
     * <p>
     * The `viewCoordinate` property serves as a reactive binding to handle changes
     * in user preferences or system configurations dynamically. Designed as a
     * final immutable object, it provides encapsulated access to the underlying
     * `ViewCoordinateRecord`, ensuring both data consistency and thread safety
     * during interactions.
     * <p>
     * This property is initialized during instance creation of the `ViewBlueprint`
     * class, either with user preferences or default values, and is updated
     * as needed through reactive subscriptions implemented in the class methods.
     * Changes to this property may trigger dependent operations such as view
     * recalculations or layout updates.
     */
    private final PreferencePropertyObject<ViewCoordinateRecord> viewCoordinate = PreferencePropertyObject.objectProp(this, VIEW_COORDINATE);


    /**
     * Constructs a new instance of the ViewBlueprint class, initializing it with the specified preferences.
     * This constructor also performs setup operations to configure the instance according to the provided
     * preferences or default settings.
     *
     * @param preferences the preferences object associated with this ViewBlueprint instance
     */
    public ViewBlueprint(KometPreferences preferences) {
        super(preferences, new BorderPane());
        setup();
    }

    /**
     * Constructs a new instance of the ViewBlueprint class, initializing it with the specified
     * preferences factory and factory. This constructor also performs setup operations to
     * configure the instance according to the provided preferences or default settings.
     *
     * @param preferencesFactory the factory used to retrieve preferences for this ViewBlueprint instance
     * @param factory the factory providing information about the gadget being created,
     *                such as its class name and default settings
     */
    public ViewBlueprint(KlPreferencesFactory preferencesFactory, KlFactory factory) {
        super(preferencesFactory, factory, new BorderPane());
        setup();
    }
    /**
     * The `viewBorderPane` is a UI component of type `BorderPane` responsible for providing
     * a flexible layout for arranging its children in top, left, right, bottom, and center regions.
     * <p>
     * This variable is a key element of the `ViewBlueprint` class, used to manage and display
     * the visual structure of the view. It serves as a container for dynamic content,
     * which is updated based on the `viewCoordinate` property and other preference-driven
     * configurations.
     * <p>
     * The `viewBorderPane` is initialized as a final instance, ensuring it is immutable
     * and consistently structured throughout the lifecycle of the class. Updates to its properties
     * and content are typically handled through internal methods, such as
     * `updateViewCalculator`, which refreshes the view state when necessary.
     */
    public final BorderPane viewBorderPane() {
        return fxGadget();
    }

    /**
     * Configures the `ViewBlueprint` instance by restoring initial values
     * using stored preferences or default settings, and establishing necessary
     * subscriptions to monitor and react to changes in preferences.
     * <p>
     * This method initializes and binds internal properties to ensure that
     * the `ViewBlueprint` remains synchronized with both the user preferences
     * and the system defaults. It sets up event-driven behavior to handle updates
     * dynamically.
     * <p>
     * Internally, this method performs the following actions:
     * 1. Invokes `restoreFromPreferencesOrDefaults` to load initial property values from preferences.
     * 2. Calls `subscribeToChanges` to establish reactive behavior for any changes in configured preferences.
     */
    private void setup() {
        restoreFromPreferencesOrDefaults();
        subscribeToChanges();
    }

    /**
     * Restores values for internal properties based on user preferences or,
     * if not configured, from default settings associated with the application.
     * <p>
     * This method iterates through all preference keys defined in the `KlView.PreferenceKeys`
     * enum and assigns appropriate values to the properties of the `ViewBlueprint` instance.
     * Specific operations include:
     * <p>
     * - The `VIEW_COORDINATE` key: Fetches the stored user-defined value (if available)
     *   from the preferences, or otherwise uses its default value. The resulting value
     *   is then assigned to the `viewCoordinate` property.
     * - After updating the `viewCoordinate` property, the `updateViewCalculator` method
     *   is invoked to refresh any dependent components dynamically.
     * <p>
     * This ensures that any properties influencing the state or behavior of `ViewBlueprint`
     * are initialized during setup with either persisted preferences or fallback defaults.
     */
    private void restoreFromPreferencesOrDefaults() {
        for (KlView.PreferenceKeys key : KlView.PreferenceKeys.values()) {
            switch (key) {
                case VIEW_COORDINATE -> {
                    viewCoordinate.setValue(preferences().getObject(key, (ViewCoordinateRecord) key.defaultValue()));
                    updateViewCalculator();
                }
            }
        }
    }

    /**
     * Updates the view calculator associated with the current `viewCoordinate` value
     * and stores it in the properties of `viewBorderPane`.
     * <p>
     * This method retrieves an instance of `ViewCalculator` using the cached factory
     * `ViewCalculatorWithCache` based on the current `viewCoordinate` property.
     * It ensures that the `viewBorderPane` properties are updated with the
     * new `viewCalculator`, keyed by `PropertyKeys.VIEW_CALCULATOR`.
     * <p>
     * This method is typically invoked when the `viewCoordinate` property is modified
     * or when other related changes require recalculating the view state.
     */
    private void updateViewCalculator() {
        ViewCalculator viewCalculator = ViewCalculatorWithCache.getCalculator(viewCoordinate.getValue());
        this.viewBorderPane().getProperties().put(PropertyKeys.VIEW_CALCULATOR, viewCalculator);
    }

    /**
     * Establishes subscriptions to handle changes in preferences for the `ViewBlueprint` instance.
     * <p>
     * This method iterates through all keys defined in the `KlView.PreferenceKeys` enumeration,
     * creating a subscription specific to each key and registering them using the
     * `addPreferenceSubscription` method.
     * <p>
     * The subscription logic includes the following:
     * - For the `VIEW_COORDINATE` key, it sets up two reactive behaviors:
     *   1. Invoking `updateViewCalculator` to refresh components dynamically based on
     *      changes to the `viewCoordinate` property.
     *   2. Triggering the `preferencesChanged` method to notify that modifications
     *      in preferences have occurred, setting the `changed` property to `true`.
     * <p>
     * By establishing these subscriptions, the method ensures that any updates to
     * application preferences are appropriately handled, keeping the `ViewBlueprint`
     * instance synchronized with user-defined or default values.
     */
    private void subscribeToChanges() {
        for (KlView.PreferenceKeys key : KlView.PreferenceKeys.values()) {
            addPreferenceSubscription(switch (key) {
                case VIEW_COORDINATE ->
                        viewCoordinate.subscribe(() -> updateViewCalculator()).and(
                           viewCoordinate.subscribe(this::preferencesChanged));
            });
        }
    }

    @Override
    public final void subGadgetSave() {
        for (KlView.PreferenceKeys key : KlView.PreferenceKeys.values()) {
            switch (key)    {
                case VIEW_COORDINATE -> preferences().putObject(key, viewCoordinate.getValue());
            }
        }
        subViewSave();
    }
    /**
     * An abstract method intended to be implemented by subclasses of the `ViewBlueprint` class.
     *
     * This method is responsible for saving the current state or data of the underlying view
     * represented by the `ViewBlueprint` instance. The specific implementation details
     * depend on the subclass and how it handles the persistence of view-related data.
     *
     * Typically, this method is invoked as part of save operations where the view's
     * configuration, layout, or other pertinent information needs to be stored persistently.
     * Subclasses are expected to override this method to define the precise save logic
     * relevant to their specific requirements.
     */
    protected abstract void subViewSave();

    @Override
    protected void subGadgetRevert() {
        restoreFromPreferencesOrDefaults();
        subViewRevert();
    }
    /**
     * An abstract method intended to be implemented by subclasses of the `ViewBlueprint` class.
     *
     * This method is responsible for reverting the state or configuration of the view
     * to its prior or default state. The implementation details depend
     * on the specific requirements of the subclass and the context in which it is used.
     *
     * Typically, `subViewRevert` is invoked in workflows requiring an undo or rollback
     * operation. Subclasses should override this method to define the specific logic
     * for reversing any changes or adjustments made to the view's state or properties.
     */
    protected abstract void subViewRevert();

}
