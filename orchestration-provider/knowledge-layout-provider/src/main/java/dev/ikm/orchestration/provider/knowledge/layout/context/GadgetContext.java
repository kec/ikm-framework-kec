package dev.ikm.orchestration.provider.knowledge.layout.context;

import dev.ikm.komet.framework.view.ObservableView;
import dev.ikm.komet.framework.view.ObservableViewNoOverride;
import dev.ikm.komet.layout.*;
import dev.ikm.komet.layout.context.KlContext;
import dev.ikm.komet.layout.context.KlContextProvider;
import dev.ikm.komet.layout.preferences.PreferencePropertyObject;
import dev.ikm.komet.layout.preferences.PreferencePropertyString;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.GadgetBlueprint;
import dev.ikm.tinkar.common.id.PublicIdStringKey;
import dev.ikm.tinkar.common.id.PublicIds;
import dev.ikm.tinkar.coordinate.view.ViewCoordinateRecord;
import javafx.scene.Node;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.Optional;
import java.util.UUID;

public final class GadgetContext implements KlContext, KlStateCommands {

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
    private final PreferencePropertyObject<ViewCoordinateRecord> viewCoordinateProperty;

    /**
     * Represents a property containing the context name as a string in the {@code Context} class.
     * It is a constant preference property used to manage and persist the context name.
     * <p>
     * This property may be used for associating a specific name or identifier
     * with a given context instance, enabling better traceability and management
     * of multiple contexts in an application.
     * <p>
     * The property is declared as final to ensure immutability and safeguard its association
     * with the context instance and preferences it represents.
     */
    private final PreferencePropertyString contextNameProperty;

    /**
     * Represents a preference property for storing the UUID of the context as a string.
     * This property is immutable and serves as an identifier for the context.
     * It is primarily used for serialization, storage, and referencing purposes
     * where the context's unique UUID is required in string format.
     */
    private final PreferencePropertyString contextUuidStringProperty;

    final KometPreferences preferences;
    final ObservableViewNoOverride observableView;
    final PublicIdStringKey publicIdStringKey;
    final KlObject klObject;

    // create
    protected GadgetContext(KlContextProvider contextProvider, ViewCoordinateRecord viewCoordinateRecord, PublicIdStringKey publicIdStringKey) {
        this.preferences = contextProvider.klObject().preferences();
        this.klObject = contextProvider.klObject();
        this.viewCoordinateProperty =
                PreferencePropertyObject.objectProp(this.klObject, KlContext.PreferenceKeys.VIEW_COORDINATE);
        this.contextUuidStringProperty =
                PreferencePropertyObject.stringProp(this.klObject, KlContext.PreferenceKeys.CONTEXT_UUID);
        this.contextNameProperty =
                PreferencePropertyObject.stringProp(this.klObject, KlContext.PreferenceKeys.CONTEXT_NAME);

        this.observableView = new ObservableViewNoOverride(viewCoordinateRecord, publicIdStringKey.getString());
        this.publicIdStringKey = publicIdStringKey;

        this.klObject.properties().put(KlObject.PropertyKeys.KL_CONTEXT, this);
    }

    // restore
    protected GadgetContext(KometPreferences preferences, KlContextProvider contextProvider) {
        this.preferences = preferences;
        this.klObject = contextProvider.klObject();
        this.viewCoordinateProperty =
                PreferencePropertyObject.objectProp(this.klObject, KlContext.PreferenceKeys.VIEW_COORDINATE);
        this.contextNameProperty = PreferencePropertyObject.stringProp(this.klObject, KlContext.PreferenceKeys.CONTEXT_NAME);
        this.contextUuidStringProperty = PreferencePropertyObject.stringProp(this.klObject, KlContext.PreferenceKeys.CONTEXT_UUID);

        String contextName = preferences.get(PreferenceKeys.CONTEXT_NAME, (String) PreferenceKeys.CONTEXT_NAME.defaultValue());
        String contextUuidStr = preferences.get(PreferenceKeys.CONTEXT_UUID, (String) PreferenceKeys.CONTEXT_UUID.defaultValue());
        this.publicIdStringKey = new PublicIdStringKey(PublicIds.of(contextUuidStr), contextName);
        ViewCoordinateRecord viewCoordinateRecord = preferences.getObject(PreferenceKeys.VIEW_COORDINATE, (ViewCoordinateRecord) PreferenceKeys.VIEW_COORDINATE.defaultValue());
        this.observableView = new ObservableViewNoOverride(viewCoordinateRecord, contextName);

        this.klObject.properties().put(KlObject.PropertyKeys.KL_CONTEXT, this);

    }

    @Override
    public KlObject klPeer() {
        return klObject;
    }

    public PreferencePropertyObject<ViewCoordinateRecord> viewCoordinatePropertyProperty() {
        return viewCoordinateProperty;
    }

    public PreferencePropertyString contextNameProperty() {
        return contextNameProperty;
    }

    public PreferencePropertyString contextUuidStringProperty() {
        return contextUuidStringProperty;
    }

    @Override
    public PublicIdStringKey<KlContext> contextId() {
        return publicIdStringKey;
    }

    @Override
    public Optional<Node> graphic() {
        return Optional.empty();
    }

    @Override
    public ObservableView viewCoordinate() {
        return observableView;
    }

    public static GadgetContext create(KlContextProvider contextProvider, ViewCoordinateRecord viewCoordinateRecord, UUID contextId, String contextName) {
        PublicIdStringKey<KlContext> publicIdStringKey = new PublicIdStringKey<>(PublicIds.of(contextId), contextName);
        return new GadgetContext(contextProvider, viewCoordinateRecord, publicIdStringKey);
    }
    public static GadgetContext create(KlContextProvider contextProvider, ViewCoordinateRecord viewCoordinateRecord, String contextName) {
        PublicIdStringKey<KlContext> publicIdStringKey = new PublicIdStringKey<>(PublicIds.newRandom(), contextName);
        return new GadgetContext(contextProvider, viewCoordinateRecord, publicIdStringKey);
    }
    public static GadgetContext restore(KometPreferences preferences , KlContextProvider contextProvider) {
        return new GadgetContext(preferences, contextProvider);
    }

    @Override
    public void unsubscribeDependentContexts() {
        switch (this.klObject) {
            case KlGadget<?> klGadget -> klGadget.dfsProcessKlGadgets(KlContextSensitiveComponent::unsubscribeFromContext);
            case KlKnowledgeBaseContext klKnowledgeBaseContext -> {
                // No action specified at this time for non-KlGadget klObjects.
                // TODO: Decide if this should propagate to windows and other KL Objects.
            }
        }
    }

    @Override
    public void subscribeDependentContexts() {
        switch (this.klObject) {
            case KlGadget<?> klGadget -> klGadget.dfsProcessKlGadgets(KlContextSensitiveComponent::subscribeToContext);
            case KlKnowledgeBaseContext klKnowledgeBaseContext -> {
                // No action specified at this time for non-KlGadget klObjects.
                // TODO: Decide if this should propagate to windows and other KL Objects.
            }
        }
    }

    @Override
    public void save() {
            for (KlContext.PreferenceKeys key : KlContext.PreferenceKeys.values()) {
                switch (key) {
                    case CONTEXT_NAME -> preferences.put(key, this.contextNameProperty.getValue());
                    case CONTEXT_UUID -> preferences.put(key, this.contextUuidStringProperty.getValue());
                    case VIEW_COORDINATE -> preferences.putObject(key, viewCoordinateProperty.getValue());
                }
            }
    }

    @Override
    public void revert() {
        for (KlContext.PreferenceKeys key : KlContext.PreferenceKeys.values()) {
            switch (key) {
                case CONTEXT_NAME -> this.contextNameProperty.setValue(preferences.get(key, (String) key.defaultValue()));
                case CONTEXT_UUID -> this.contextUuidStringProperty.setValue(preferences.get(key, (String) key.defaultValue()));
                case VIEW_COORDINATE -> this.viewCoordinateProperty.setValue(preferences.getObject(key, (ViewCoordinateRecord) key.defaultValue()));
            }
        }
    }

    @Override
    public void delete() {
        // This class is encapsulated and uses the preferences of the encapsulator. The encapsulator is
        // responsible for deletion of the preference node.
        for (KlContext.PreferenceKeys key : KlContext.PreferenceKeys.values()) {
            preferences.remove(key);
        }
    }

    public void subscribeToChanges() {
        if (this.klObject instanceof GadgetBlueprint gadgetBlueprint) {
            for (KlContext.PreferenceKeys key : KlContext.PreferenceKeys.values()) {
                gadgetBlueprint.addPreferenceSubscription(switch (key) {
                    case CONTEXT_NAME -> contextNameProperty.subscribe(() -> { /* TODO: placeholder for something to do? */ }).and(
                            contextNameProperty.subscribe(gadgetBlueprint::preferencesChanged)
                    );
                    case CONTEXT_UUID -> contextUuidStringProperty.subscribe(() -> { /* TODO: placeholder for something to do? */ }).and(
                            contextUuidStringProperty.subscribe(gadgetBlueprint::preferencesChanged)
                    );
                    case VIEW_COORDINATE ->
                            viewCoordinateProperty.subscribe(() -> observableView.setExceptOverrides(viewCoordinateProperty.getValue())).and(
                                    viewCoordinateProperty.subscribe(gadgetBlueprint::preferencesChanged));
                });
            }
        } else {
        }
    }


}
