package dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.PreferencePropertyBoolean;
import dev.ikm.komet.layout.preferences.PreferencePropertyDouble;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.ikm.komet.layout.window.KlWindow.PreferenceKeys.*;

/**
 * The StageBlueprint class is a concrete implementation derived from GadgetBlueprint<Stage>
 * and KlWindow. It serves as a blueprint for defining, configuring, and synchronizing
 * stage-specific properties such as location, size, visibility, opacity, and other
 * related settings with user preferences. This class ensures consistency between the
 * application's state and the user's configured preferences for the stage window.
 *
 * Key functionalities of this class include:
 * - Initializing properties based on user preferences or default values.
 * - Restoring the state of the window from saved preferences.
 * - Subscribing to changes and establishing bidirectional synchronization
 *   between preferences and window properties.
 * - Allowing further subclass-specific initialization with the classInitialize method.
 *
 * Fields:
 * - LOG: Logger instance for handling debugging, error reporting, or informational messages.
 * - locationX: The X-coordinate of the stage window's position on the screen.
 * - locationY: The Y-coordinate of the stage window's position on the screen.
 * - width: The width of the stage window in pixels.
 * - height: The height of the stage window in pixels.
 * - opacity: The transparency level of the stage window.
 * - visible: A boolean indicating whether the stage window is visible or hidden.
 * - windowStage: The actual Stage object associated with this blueprint.
 *
 * Interfaces/Inheritance:
 * - Extends GadgetBlueprint<Stage>: Provides fundamental blueprinting behavior
 *   and manages preference-related configurations.
 * - Extends KlWindow: Adds window-specific functionality to manage stage-related
 *   properties such as location, size, and visibility.
 *
 * This class forms the foundational implementation for creating and managing
 * customizable and synchronized stage layouts using user-defined settings.
 */
public abstract class StageBlueprint extends GadgetBlueprint<Stage> implements KlWindow<Stage> {
    private static final Logger LOG = LoggerFactory.getLogger(StageBlueprint.class);


    /**
     * Represents the X-coordinate of the stage window's location. This property is
     * synchronized with user preferences and maintains the horizontal position of
     * the window on the screen.
     *
     * The `locationX` property retrieves its initial value from the user preferences
     * store during the setup process of the {@code StageBlueprint} class. If no
     * value is stored, a default value is applied. Any changes to this property
     * are automatically synchronized back to the preferences to maintain consistency.
     *
     * This property is primarily used to ensure that the window reopens at the
     * same horizontal position it was at when last used.
     */
    private final PreferencePropertyDouble locationX = PreferencePropertyDouble.doubleProp(this, WINDOW_X_LOCATION);
    /**
     * Represents the Y-coordinate of the window's position in the user interface.
     * This property is tied to user preferences, allowing for persistent storage
     * and retrieval of the window's vertical position.
     *
     * The value is stored as a double and synchronized with the user's preferences
     * through the {@link PreferencePropertyDouble} mechanism. This ensures that
     * changes to the Y-coordinate are reflected in both the application state and
     * the stored user preferences, providing consistency across application sessions.
     *
     * The property is initialized with the preference key {@code WINDOW_Y_LOCATION},
     * which identifies the stored value in the preference infrastructure.
     *
     * This field is immutable and is finalized to prevent reassignment.
     */
    private final PreferencePropertyDouble locationY =  PreferencePropertyDouble.doubleProp(this, WINDOW_Y_LOCATION);
    /**
     * Represents the width of the stage window, managed as a preference-backed property.
     * This property is synchronized with the user's stored preferences, allowing for
     * restoration of the stage's width when the application is initialized.
     *
     * The value is stored as a double precision floating-point number and reflects the
     * current width of the window in pixels. Changes to this property are automatically
     * persisted to the preferences system, ensuring that the preferred width is retained
     * across application sessions.
     *
     * This property also allows bidirectional synchronization with the stage's actual width,
     * so updates to either the stage or the preferences will keep them in sync.
     *
     * The field is declared as final to enforce immutability, ensuring the property itself
     * cannot be reassigned after initialization.
     */
    private final PreferencePropertyDouble width = PreferencePropertyDouble.doubleProp(this, WINDOW_WIDTH);
    /**
     * Represents the height property of the stage window in the `StageBlueprint` class.
     * This property is synchronized with the user preferences to persist the height value of the window.
     *
     * The value is stored as a double-precision floating-point number through
     * the `PreferencePropertyDouble` abstraction, which provides functionality
     * for binding and bidirectional synchronization.
     *
     * Key characteristics:
     * - The property's default or restored value is derived from the user preferences.
     * - Changes to this property are reflected in user preferences, and vice versa.
     * - This ensures that the height of the stage window respects user settings
     *   and persists across application sessions.
     */
    private final PreferencePropertyDouble height = PreferencePropertyDouble.doubleProp(this, WINDOW_HEIGHT);
    /**
     * Represents the opacity level of the stage window as a user preference.
     * This property allows the storage, retrieval, and synchronization of the
     * stage's transparency setting with user preferences.
     *
     * The opacity value is managed using the `PreferencePropertyDouble` class,
     * which provides mechanisms for interacting with the stored preference and
     * synchronizing changes between the application state and the preferences
     * system. The variable is initialized with the associated key `OPACITY`
     * to uniquely identify this property in the preferences store.
     *
     * This property is used in the context of a StageBlueprint to restore,
     * persist, and synchronize the opacity level of the stage window, ensuring
     * consistency between the user's preferences and the window's appearance.
     */
    private final PreferencePropertyDouble opacity = PreferencePropertyDouble.doubleProp(this, WINDOW_OPACITY);
    /**
     * Represents a boolean property for controlling the visibility state of the stage window.
     * This property is used to synchronize and store the visibility preference of the stage
     * between the application's runtime state and the user's preference storage.
     *
     * The visibility state is managed as a preference property, ensuring bidirectional
     * synchronization such that changes in the application's window visibility setting are
     * reflected in stored preferences, and vice versa. This integration allows the application
     * to restore the user's preferred visibility state across sessions.
     *
     * The property is declared as final, emphasizing that it is a constant member of the
     * containing class and its reference cannot be reassigned.
     *
     * Usage Context:
     * - Automatically updated when the user toggles the visibility of the stage.
     * - Used during the initialization to restore the visibility state from user preferences
     *   or to set its default value if no preference is available.
     */
    private final PreferencePropertyBoolean visible = PreferencePropertyBoolean.booleanProp(this,WINDOW_VISIBLE);

    /**
     * Represents the primary {@link Stage} instance used as the window stage
     * within the {@code StageBlueprint} class. This stage defines properties
     * such as its size, location, visibility, transparency, and behavior,
     * which are synchronized with user preferences for consistency and state
     * restoration.
     *
     * The {@code windowStage} is initialized upon {@code StageBlueprint}
     * instantiation and serves as the core UI window that reflects changes
     * made to its properties from user interactions or preference updates.
     * Modifications to the properties of this stage, such as position or
     * visibility, are persisted and restored across sessions based on the
     * synchronized user preferences.
     *
     * Responsibilities and interactions:
     * - It is managed and configured by methods like {@code restoreFromPreferencesOrDefaults()}
     *   to apply initial property settings.
     * - Changes to the stage properties are tracked and synchronized using
     *   {@code subscribeToChanges()}, ensuring that updates in user preferences
     *   and application state remain consistent.
     * - It is foundational for configuring the visual and functional aspects
     *   of the user interface provided by the blueprint.
     *
     * This stage remains final to ensure that its core responsibilities and
     * behavior are not altered, maintaining a consistent interface and ensuring
     * stability across applications using this blueprint.
     */
    private final Stage windowStage = new Stage();

    /**
     * Constructs a StageBlueprint instance and initializes the window
     * stage properties based on user preferences. Additionally, it sets
     * up functionality for maintaining synchronization between state
     * changes and user preferences.
     *
     * @param preferences the user preferences object used to store and retrieve
     *                    properties related to the stage window, such as location,
     *                    size, visibility, opacity, and other relevant settings.
     */
    public StageBlueprint(KometPreferences preferences) {
        super(preferences);
        setup();
    }

    /**
     * Constructs a StageBlueprint instance, initializes the stage blueprint, and sets up
     * functionality for managing stage window properties based on user preferences.
     *
     * @param preferences the user preferences object used for storing and retrieving
     *                    window stage settings like opacity, visibility, location, size,
     *                    and other related properties.
     * @param factory     the factory responsible for creating and managing objects specific
     *                    to the stage blueprint; enables further customization or functionality.
     */
    public StageBlueprint(KometPreferences preferences, KlFactory factory) {
        super(preferences, factory);
        setup();
    }

    /**
     * Initializes the stage blueprint configuration by restoring window properties such as
     * location, size, visibility, and opacity from user preferences or default values, then
     * sets up mechanisms for synchronizing changes between application state and preferences.
     *
     * This method performs the following tasks:
     * - Invokes {@link #restoreFromPreferencesOrDefaults()} to load saved or default settings
     *   for the stage properties (e.g., location, size, and visibility).
     * - Calls {@link #subscribeToChanges()} to establish bidirectional synchronization
     *   between the user preferences and the stage properties.
     *
     * It ensures that the window stage maintains a consistent state with user-defined
     * or default preferences and dynamically reflects any updates in the application.
     */
    private void setup() {
        restoreFromPreferencesOrDefaults();
        subscribeToChanges();
    }

    /**
     * Restores the state of window properties (such as opacity, visibility, location, width, and height)
     * from user preferences if available, or default values if preferences are not set.
     *
     * This method iterates through each key defined in {@link KlWindow.PreferenceKeys}
     * and assigns the corresponding value from the preferences store or the default value
     * associated with the key to the stage-related properties.
     *
     * Preference keys and their purpose:
     * - OPACITY: Represents the transparency level of the window.
     * - VISIBLE: Determines whether the window is visible or hidden.
     * - WINDOW_X_LOCATION: Refers to the X-coordinate of the window's position.
     * - WINDOW_Y_LOCATION: Refers to the Y-coordinate of the window's position.
     * - WINDOW_WIDTH: The width of the window in pixels.
     * - WINDOW_HEIGHT: The height of the window in pixels.
     *
     * For each key, the method retrieves its stored value using the preferences API.
     * If no stored value exists for the key, the key's default value is used instead.
     *
     * This method is typically invoked during initialization to restore the user's
     * previously saved preferences or initialize with default settings.
     */
    private void restoreFromPreferencesOrDefaults() {
        for (KlWindow.PreferenceKeys key : KlWindow.PreferenceKeys.values()) {
            switch (key) {
                case WINDOW_OPACITY -> opacity.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
                case WINDOW_VISIBLE -> visible.setValue(preferences().getBoolean(key, (Boolean) key.defaultValue()));
                case WINDOW_X_LOCATION -> locationX.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
                case WINDOW_Y_LOCATION -> locationY.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
                case WINDOW_WIDTH -> width.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
                case WINDOW_HEIGHT -> height.setValue(preferences().getDouble(key, (Double) key.defaultValue()));
            }
        }
    }

    /**
     * Subscribes to changes in specific window preference properties and sets up
     * bidirectional synchronization between the application state and window stage
     * properties such as opacity, visibility, location, width, and height.
     *
     * This method iterates through the defined {@link KlWindow.PreferenceKeys} enums
     * and establishes subscriptions for each key. These subscriptions ensure that
     * any changes to the preferences are reflected in the corresponding window stage
     * properties and vice versa. It additionally links each subscription to a
     * handler for recording preference changes.
     *
     * The following preference keys are supported:
     * - OPACITY: Synchronizes stage opacity.
     * - VISIBLE: Synchronizes stage visibility (show or hide).
     * - WINDOW_X_LOCATION: Synchronizes X coordinate of the stage.
     * - WINDOW_Y_LOCATION: Synchronizes Y coordinate of the stage.
     * - WINDOW_WIDTH: Synchronizes the width of the stage.
     * - WINDOW_HEIGHT: Synchronizes the height of the stage.
     *
     * For each key, a set of subscriptions are created:
     * - One to listen for changes in preferences and apply them to the stage properties.
     * - One to listen for changes in the stage properties and update the preference values.
     * - One to monitor changes and trigger the `preferencesChanged` handler.
     *
     * Note: This method currently raises questions regarding the use of bidirectional
     * bindings versus separate subscriptions, particularly for the OPACITY property.
     * Further discussion and decisions on this matter may be required for consistency.
     */
    private void subscribeToChanges() {
        for (KlWindow.PreferenceKeys key : KlWindow.PreferenceKeys.values()) {
            addPreferenceSubscription(switch (key) {
                case WINDOW_OPACITY -> {
                    // TODO: Discuss with team... Subscription vs bidirectional binding when available?
                    // bindBidirectional changes the ceremony to unsubscribe, and perhaps introduces inconsistency?
                    // windowStage.opacityProperty().bindBidirectional(opacity);
                    addPreferenceSubscription(opacity.subscribe(v -> windowStage.setOpacity(v.doubleValue())));
                    addPreferenceSubscription(windowStage.opacityProperty().subscribe(v -> opacity.setValue(v.doubleValue())));
                    yield opacity.subscribe(this::preferencesChanged);
                }
                case WINDOW_VISIBLE -> {
                    addPreferenceSubscription(windowStage.showingProperty().subscribe(visible::setValue));
                    addPreferenceSubscription(visible.subscribe(() -> {
                        if (visible.getValue()) {
                            windowStage.show();
                        } else {
                            windowStage.hide();
                        }
                    }));
                    yield visible.subscribe(this::preferencesChanged);
                }
                case WINDOW_X_LOCATION -> {
                    addPreferenceSubscription(locationX.subscribe(x -> windowStage.setX(x.doubleValue())));
                    addPreferenceSubscription(windowStage.xProperty().subscribe(x -> locationX.setValue(x.doubleValue())));
                    yield locationX.subscribe(this::preferencesChanged);
                }
                case WINDOW_Y_LOCATION -> {
                    addPreferenceSubscription(locationY.subscribe(y -> windowStage.setY(y.doubleValue())));
                    addPreferenceSubscription(windowStage.yProperty().subscribe(y -> locationY.setValue(y.doubleValue())));
                    yield locationY.subscribe(this::preferencesChanged);
                }
                case WINDOW_WIDTH -> {
                    addPreferenceSubscription(width.subscribe(w -> windowStage.setWidth(w.doubleValue())));
                    addPreferenceSubscription(windowStage.widthProperty().subscribe(w -> width.setValue(w.doubleValue())));
                    yield width.subscribe(this::preferencesChanged);
                }
                case WINDOW_HEIGHT -> {
                    addPreferenceSubscription(height.subscribe(h -> windowStage.setHeight(h.doubleValue())));
                    addPreferenceSubscription(windowStage.heightProperty().subscribe(h -> height.setValue(h.doubleValue())));
                    yield height.subscribe(this::preferencesChanged);
                }
            });
        }
    }
    /**
     * Provides access to the X-coordinate location property of the stage.
     * This property represents the horizontal position of the stage and
     * is synchronized with user preferences.
     *
     * @return the X-coordinate location preference property associated
     *         with the stage
     */
    public PreferencePropertyDouble locationXProperty() {
        return locationX;
    }
    /**
     * Provides access to the Y-coordinate location property of the stage.
     * This property represents the vertical position of the stage and
     * is synchronized with user preferences.
     *
     * @return the Y-coordinate location preference property associated
     *         with the stage
     */
    public PreferencePropertyDouble locationYProperty() {
        return locationY;
    }
    /**
     * Provides access to the width property of the stage. This property represents
     * the width of the stage in pixels and is synchronized with user preferences.
     *
     * @return the width preference property associated with the stage
     */
    public PreferencePropertyDouble widthProperty() {
        return width;
    }
    /**
     * Provides access to the height property of the stage. This property represents
     * the height of the stage in pixels and is synchronized with user preferences.
     *
     * @return the height preference property associated with the stage
     */
    public PreferencePropertyDouble heightProperty() {
        return height;
    }
    /**
     * Provides access to the opacity property of the stage. This property represents
     * the transparency level of the stage and is synchronized with user preferences.
     *
     * @return the opacity preference property associated with the stage
     */
    public PreferencePropertyDouble opacityProperty() {
        return opacity;
    }
    /**
     * Provides access to the visibility property of the stage. This property
     * represents whether the stage should be visible or hidden and is
     * synchronized with user preferences.
     *
     * @return the visibility preference property associated with the stage
     */
    public PreferencePropertyBoolean visibleProperty() {
        return visible;
    }

    @Override
    public Stage klGadget() {
        return windowStage;
    }


    @Override
    public Parent root() {
        return this.windowStage.getScene().getRoot();
    }

    @Override
    public void show() {
        this.windowStage.show();
    }

    @Override
    public void hide() {
        this.windowStage.hide();
    }

}
