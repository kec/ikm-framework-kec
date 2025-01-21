package dev.ikm.orchestration.provider.knowledge.layout.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.KlView;
import dev.ikm.komet.layout.preferences.PreferencesPropertyObject;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.tinkar.coordinate.view.ViewCoordinateRecord;
import dev.ikm.tinkar.coordinate.view.calculator.ViewCalculator;
import dev.ikm.tinkar.coordinate.view.calculator.ViewCalculatorWithCache;
import javafx.scene.layout.BorderPane;

import static dev.ikm.komet.layout.KlView.PreferenceKeys.VIEW_COORDINATE;


public class ViewBlueprint extends GadgetBlueprint<BorderPane> implements KlView {

    private final PreferencesPropertyObject<ViewCoordinateRecord> viewCoordinate = PreferencesPropertyObject.objectProp(this, VIEW_COORDINATE);

    private final BorderPane viewBorderPane = new BorderPane();

    public ViewBlueprint(KometPreferences preferences) {
        super(preferences);
        setup();
    }

    public ViewBlueprint(KometPreferences preferences, KlFactory factory) {
        super(preferences, factory);
        setup();
    }

    /**
     * Configures the `ViewBlueprint` instance by restoring initial values
     * using stored preferences or default settings, and establishing necessary
     * subscriptions to monitor and react to changes in preferences.
     *
     * This method initializes and binds internal properties to ensure that
     * the `ViewBlueprint` remains synchronized with both the user preferences
     * and the system defaults. It sets up event-driven behavior to handle updates
     * dynamically.
     *
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
     *
     * This method iterates through all preference keys defined in the `KlView.PreferenceKeys`
     * enum and assigns appropriate values to the properties of the `ViewBlueprint` instance.
     * Specific operations include:
     *
     * - The `VIEW_COORDINATE` key: Fetches the stored user-defined value (if available)
     *   from the preferences, or otherwise uses its default value. The resulting value
     *   is then assigned to the `viewCoordinate` property.
     * - After updating the `viewCoordinate` property, the `updateViewCalculator` method
     *   is invoked to refresh any dependent components dynamically.
     *
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
     *
     * This method retrieves an instance of `ViewCalculator` using the cached factory
     * `ViewCalculatorWithCache` based on the current `viewCoordinate` property.
     * It ensures that the `viewBorderPane` properties are updated with the
     * new `viewCalculator`, keyed by `PropertyKeys.VIEW_CALCULATOR`.
     *
     * This method is typically invoked when the `viewCoordinate` property is modified
     * or when other related changes require recalculating the view state.
     */
    private void updateViewCalculator() {
        ViewCalculator viewCalculator = ViewCalculatorWithCache.getCalculator(viewCoordinate.getValue());
        this.viewBorderPane.getProperties().put(PropertyKeys.VIEW_CALCULATOR, viewCalculator);
    }

    /**
     * Establishes subscriptions to handle changes in preferences for the `ViewBlueprint` instance.
     *
     * This method iterates through all keys defined in the `KlView.PreferenceKeys` enumeration,
     * creating a subscription specific to each key and registering them using the
     * `addPreferenceSubscription` method.
     *
     * The subscription logic includes the following:
     * - For the `VIEW_COORDINATE` key, it sets up two reactive behaviors:
     *   1. Invoking `updateViewCalculator` to refresh components dynamically based on
     *      changes to the `viewCoordinate` property.
     *   2. Triggering the `preferencesChanged` method to notify that modifications
     *      in preferences have occurred, setting the `changed` property to `true`.
     *
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
    public BorderPane klGadget() {
        return this.viewBorderPane;
    }
}
