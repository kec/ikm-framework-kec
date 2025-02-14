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
 * An abstract blueprint for creating field panes that encapsulate an observable field.
 * This class extends the {@code WidgetBlueprint} and provides support for managing
 * an {@code ObservableField} instance, enabling dynamic behavior and reactivity.
 *
 * @param <T> the type of {@code Parent} that this blueprint works with, used as the base component
 *            for constructing the field pane UI.
 */
public abstract class FieldPaneBlueprint<T extends Parent> extends WidgetBlueprint<T> {

    ObjectProperty<ObservableField<T>> fieldProperty = new SimpleObjectProperty<>();

    /**
     * Constructs a new {@code FieldPaneBlueprint} object by initializing it with the
     * specified preferences and UI gadget component.
     *
     * @param preferences the {@code KometPreferences} instance used to manage and
     *                    restore settings for this field pane blueprint.
     * @param fxGadget    the gadget instance of type {@code T} to be used as the primary
     *                    UI component for constructing and managing the field pane.
     */
    public FieldPaneBlueprint(KometPreferences preferences, T fxGadget) {
        super(preferences, fxGadget);
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
     * @param fxGadget           the UI gadget of type {@code T} used as the primary component for
     *                           constructing and managing the field pane blueprint.
     */
    public FieldPaneBlueprint(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory, T fxGadget) {
        super(preferencesFactory, gadgetFactory, fxGadget);
    }

    /**
     * Sets the {@code ObservableField} for this field pane.
     *
     * @param field the {@code ObservableField} instance to be associated with this field pane.
     *              It provides the value and observable properties for this field.
     */
    public void setField(ObservableField<T> field) {
        fieldProperty.set(field);
    }

    /**
     * Retrieves the {@code ObservableField} instance associated with this field pane.
     * The {@code ObservableField} provides access to the field's value and supports
     * observation of property changes.
     *
     * @return the {@code ObservableField} instance managing the field's data and properties
     */
    public ObservableField<T> getField() {
        return fieldProperty.get();
    }

}
