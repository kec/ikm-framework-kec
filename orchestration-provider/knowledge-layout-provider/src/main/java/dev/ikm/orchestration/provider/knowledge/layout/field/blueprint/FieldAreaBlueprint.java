package dev.ikm.orchestration.provider.knowledge.layout.field.blueprint;

import dev.ikm.komet.framework.observable.ObservableField;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.WidgetBlueprint;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;

/**
 * The {@code FieldPaneBlueprint} is an abstract class designed to manage and construct
 * a UI component (of type {@code T}) that represents a pane for displaying and handling
 * fields. It provides methods for associating a field (via {@code ObservableField}) with
 * the UI and ensures that changes in the field's properties are reflected in the component.
 *
 * @param <FX> the type of the primary UI component, which must extend {@code Parent}.
 * @param <DT> the data type of the object managed by the {@code ObservableField}.
 */
public abstract class FieldAreaBlueprint<FX extends Parent, DT extends Object> extends WidgetBlueprint<FX> {

    /**
     * Represents a JavaFX {@code ObjectProperty} that holds an {@code ObservableField} of type {@code DT}.
     * The {@code fieldProperty} is used to manage and observe dynamic updates to an {@code ObservableField},
     * which provides value and property change notifications. This property plays a critical role
     * in the data-binding framework for the {@code FieldPaneBlueprint}, ensuring the UI reacts to changes
     * in the underlying field values.
     */
    ObjectProperty<ObservableField<DT>> fieldProperty = new SimpleObjectProperty<>();

    /**
     * Constructs a new {@code FieldPaneBlueprint} object by initializing it with the
     * specified preferences and UI gadget component.
     *
     * @param preferences the {@code KometPreferences} instance used to manage and
     *                    restore settings for this field pane blueprint.
     * @param fxGadget    the gadget instance of type {@code T} to be used as the primary
     *                    UI component for constructing and managing the field pane.
     */
    protected FieldAreaBlueprint(KometPreferences preferences, FX fxGadget) {
        super(preferences, fxGadget);
        setup();
    }

    /**
     * Constructs a new {@code FieldPaneBlueprint} instance with the specified preferences factory,
     * gadget factory, and UI gadget component. This constructor initializes the field pane blueprint
     * by delegating configuration to its superclass.
     *
     * @param preferencesFactory the factory managing and creating preference-related configurations
     *                           for the field pane blueprint.
     * @param gadgetFactory      the factory responsible for providing metadata and configurations
     *                           related to the UI gadget.
     * @param fxGadget           the UI gadget of type {@code FX} used as the primary component for
     *                           constructing and managing the field pane blueprint.
     */
    protected FieldAreaBlueprint(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory, FX fxGadget) {
        super(preferencesFactory, gadgetFactory, fxGadget);
        setup();
    }

    /**
     * Initializes the field pane blueprint by setting up the necessary
     * subscriptions for property updates. This method subscribes to the
     * {@code fieldProperty} changes and delegates the handling of updates
     * to the abstract {@code updateField} method. Ensures that the field pane
     * dynamically responds to changes in its observable field.
     */
    private void setup() {
        preferenceSubscriptionReference.get().and(fieldProperty.subscribe(this::updateField));
    }

    protected abstract void updateField();

    /**
     * Sets the {@code ObservableField} for this field pane.
     *
     * @param field the {@code ObservableField} instance to be associated with this field pane.
     *              It provides the value and observable properties for this field.
     */
    public final void setField(ObservableField<DT> field) {
        fieldProperty.set(field);
    }

    /**
     * Retrieves the {@code ObservableField} instance associated with this field pane.
     * The {@code ObservableField} provides access to the field's value and supports
     * observation of property changes.
     *
     * @return the {@code ObservableField} instance managing the field's data and properties
     */
    public final ObservableField<DT> getField() {
        return fieldProperty.get();
    }

}
