package dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.KlWidget;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Abstract class representing a blueprint for creating configurable Widgets. This class provides
 * methods for managing widget preferences, dynamic configuration updates, and layout restoration.
 * It extends the {@code GadgetBlueprint} class and implements the {@code KlWidget} interface to
 * adhere to specific widget behaviors.
 *
 * @param <T> the type parameter extending from {@code Parent}, representing the root node of
 *            the widget's layout.
 */
public non-sealed abstract class WidgetBlueprint<T extends Parent> extends GadgetBlueprint<T> implements KlWidget<T> {

    /**
     * Restores a {@code WidgetBlueprint} object with the specified preferences.
     * <p>
     * This constructor initializes the {@code WidgetBlueprint} by invoking its superclass
     * constructor and performing necessary setup for synchronizing the widget's state
     * with the provided preferences and gadget.
     *
     * @param preferences the {@code KometPreferences} instance associated with the widget blueprint,
     *                    used for managing and restoring settings.
     * @param fxGadget    the gadget instance of type {@code T} to be configured and encapsulated
     *                    within the widget blueprint.
     * @throws IllegalStateException if the gadget object provided does not comply with
     *                               the expected specifications (e.g., back-end validation by superclass).
     */
    public WidgetBlueprint(KometPreferences preferences, T fxGadget) {
        super(preferences, fxGadget);
        setup();
    }

    /**
     * Constructs a new instance of {@code WidgetBlueprint} with the specified preferences factory,
     * gadget factory, and gadget object. This constructor initializes the widget blueprint by
     * delegating initialization to the superclass and invoking the {@code setup} method to
     * configure and synchronize the widget's state.
     *
     * @param preferencesFactory the factory responsible for creating and managing preferences,
     *                           which will be used for handling the widget's persistent settings
     *                           and configuration.
     * @param gadgetFactory      the instance of {@code KlFactory} representing the gadget-related
     *                           configuration and metadata for initializing the widget blueprint.
     * @param fxGadget           the specific gadget instance of type {@code T} to be encapsulated,
     *                           synchronized, and configured within the widget blueprint.
     */
    public WidgetBlueprint(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory, T fxGadget) {
        super(preferencesFactory, gadgetFactory, fxGadget);
        setup();
    }

    /**
     * Initializes and configures the widget blueprint by performing the following actions:
     * <p>
     * 1. Subscribes to changes in relevant preference keys to dynamically handle updates.
     * 2. Restores the widget's layout and configuration settings based on stored preferences
     *    or default values.
     * <p>
     * This method is essential for ensuring that the widget blueprint is fully synchronized
     * with the current preferences and ready to adapt to any future modifications.
     */
    private void setup() {
        subscribeToChanges();
        restoreFromPreferencesOrDefaults();
    }
    /**
     * Restores the layout and configuration settings of a widget from either the stored preferences
     * or the associated default values. This method iterates through all defined preference keys and
     * applies the corresponding settings to the widget.
     * <p>
     * The method dynamically adjusts various properties of the widget, such as growth priorities,
     * alignment, grid positioning, and margins. For each preference key, it fetches the value from
     * persistent preferences storage or uses the default value defined in the {@link KlWidget.PreferenceKeys}
     * enum. The fetched values are then applied to the widget using the appropriate configuration method.
     * <p>
     * The following settings are restored:
     * <p>- Horizontal and vertical growth priorities (H_GROW, V_GROW)
     * <p>- Horizontal and vertical alignment (H_ALIGNMENT, V_ALIGNMENT)
     * <p>- Grid positioning indices (COLUMN_INDEX, ROW_INDEX)
     * <p>- Grid span for rows and columns (COLUMN_SPAN, ROW_SPAN)
     * <p>- Margin values around the widget (MARGIN)
     * <p>
     * This method utilizes helper methods such as {@code fromDoubleArray} and {@code toDoubleArray}
     * for converting between custom data structures (e.g., insets and double arrays).
     * <p>
     * Note: Preferences are accessed through the {@link KometPreferences} instance retrieved via
     * the {@code preferences()} method. Default values are specified in the {@link KlWidget.PreferenceKeys} enum.
     */
    private void restoreFromPreferencesOrDefaults() {
        for (KlWidget.PreferenceKeys key : KlWidget.PreferenceKeys.values()) {
            switch (key) {
                case H_GROW ->
                        GridPane.setHgrow(klWidget(), Priority.valueOf(preferences().get(key, (String) key.defaultValue())));
                case V_GROW ->
                        GridPane.setVgrow(klWidget(), Priority.valueOf(preferences().get(key, (String) key.defaultValue())));
                case H_ALIGNMENT ->
                        GridPane.setHalignment(klWidget(), javafx.geometry.HPos.valueOf(preferences().get(key, (String) key.defaultValue())));
                case V_ALIGNMENT ->
                        GridPane.setValignment(klWidget(), javafx.geometry.VPos.valueOf(preferences().get(key, (String) key.defaultValue())));
                case COLUMN_INDEX ->
                        GridPane.setColumnIndex(klWidget(), preferences().getInt(key, (Integer) key.defaultValue()));
                case ROW_INDEX ->
                        GridPane.setRowIndex(klWidget(), preferences().getInt(key, (Integer) key.defaultValue()));
                case COLUMN_SPAN ->
                        GridPane.setColumnSpan(klWidget(), preferences().getInt(key, (Integer) key.defaultValue()));
                case ROW_SPAN ->
                        GridPane.setRowSpan(klWidget(), preferences().getInt(key, (Integer) key.defaultValue()));
                case MARGIN ->
                        GridPane.setMargin(klWidget(), fromDoubleArray(preferences().getDoubleArray(key, toDoubleArray(Insets.EMPTY))));
            }
        }
    }

    /**
     * Converts an array of doubles into an Insets object. If the array contains exactly four elements,
     * they are used as the values for top, right, bottom, and left insets. If the array does not contain
     * four elements, a default Insets object with all values set to 0 is returned.
     *
     * @param array the array of doubles to be converted into an Insets object.
     *              The array must have exactly four elements representing top, right, bottom, and left insets.
     * @return an Insets object created from the given array. If the array does not have exactly four elements,
     *         a default Insets object with all values set to 0 is returned.
     */
    private static Insets fromDoubleArray(double[] array) {
        if (array.length == 4) {
            return new Insets(array[0], array[1], array[2], array[3]);
        } else {
            return new Insets(0);
        }
    }

    /**
     * Converts an Insets object into an array of doubles, where the elements
     * in the array represent the top, right, bottom, and left values of the Insets,
     * in that order.
     *
     * @param insets the Insets object to be converted into a double array.
     *               The Insets object must not be null.
     * @return an array of doubles containing four elements representing the
     *         top, right, bottom, and left insets, respectively.
     */
    private static double[] toDoubleArray(Insets insets) {
        return new double[]{insets.getTop(), insets.getRight(), insets.getBottom(), insets.getLeft()};
    }

    /**
     * Subscribes to changes in specific preference keys and attaches appropriate listeners
     * for handling preference updates. This method is unique from some other blueprint subscriptions
     * since the values are stored in an observable map. Preferences not in the observable map
     * should be put in an independent enumeration and handled separately.
     */
    private void subscribeToChanges() {
        addPreferenceSubscription(this.properties().subscribe(this::preferencesChanged));
    }

    @Override
    protected void subGadgetRevert() {
        restoreFromPreferencesOrDefaults();
        subWidgetRevert();
    }

    @Override
    protected void subGadgetSave() {
        for (KlWidget.PreferenceKeys key : KlWidget.PreferenceKeys.values()) {
            switch (key) {
                case H_GROW -> preferences().put(key, this.getHgrow().name());
                case V_GROW -> preferences().put(key, this.getVgrow().name());
                case H_ALIGNMENT -> preferences().put(key, this.getHalignment().name());
                case V_ALIGNMENT -> preferences().put(key, this.getValignment().name());
                case COLUMN_INDEX -> preferences().putInt(key, this.getColumnIndex());
                case ROW_INDEX -> preferences().putInt(key, this.getRowIndex());
                case COLUMN_SPAN -> preferences().putInt(key, this.getColspan());
                case ROW_SPAN -> preferences().putInt(key, this.getRowspan());
                case MARGIN -> preferences().putDoubleArray(key, toDoubleArray(this.getMargins()));
            }
        }

        subWidgetSave();
    }

    protected abstract void subWidgetRevert();
    protected abstract void subWidgetSave();
}
