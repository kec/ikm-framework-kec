package dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint;

import dev.ikm.komet.framework.observable.LocatableFeature;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.feature.KlFeatureArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.AreaBlueprint;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;

public abstract class FeatureAreaBlueprint<FX extends Region, LF extends LocatableFeature>
        extends AreaBlueprint<FX>
        implements KlFeatureArea<LF, FX> {

    final protected ObjectProperty<Property<LF>> featurePropertyWrapper = new SimpleObjectProperty<>();

    /**
     * Constructs a new {@code FieldPaneBlueprint} object by initializing it with the
     * specified preferences and UI gadget component.
     *
     * @param preferences the {@code KometPreferences} instance used to manage and
     *                    restore settings for this field pane blueprint.
     * @param fxGadget    the gadget instance of type {@code T} to be used as the primary
     *                    UI component for constructing and managing the field pane.
     */
    protected FeatureAreaBlueprint(KometPreferences preferences, FX fxGadget) {
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
    protected FeatureAreaBlueprint(KlPreferencesFactory preferencesFactory, KlArea.Factory gadgetFactory, FX fxGadget) {
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
        preferenceSubscriptionReference.get().and(featurePropertyWrapper.subscribe(this::updatePropertyWrapper));
    }

    protected final void updatePropertyWrapper(Property<LF> oldValue, Property<LF> newValue) {
        propertyWrapperUpdated(oldValue, newValue);
    }

    protected void propertyWrapperUpdated(Property<LF> oldValue, Property<LF> newValue) {
        // Override if specific behavior wanted.
    }

    public void setProperty(Property<LF> property) {
        this.featurePropertyWrapper.setValue(property);
    }

    public Property<LF> getProperty() {
        return this.featurePropertyWrapper.get() ;
    }

}
