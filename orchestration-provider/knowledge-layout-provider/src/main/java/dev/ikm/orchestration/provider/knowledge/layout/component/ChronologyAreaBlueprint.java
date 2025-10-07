package dev.ikm.orchestration.provider.knowledge.layout.component;

import dev.ikm.komet.framework.observable.Feature;
import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.KlParent;
import dev.ikm.komet.layout.area.KlAreaForEntity;
import dev.ikm.komet.layout.component.KlChronologyArea;
import dev.ikm.komet.layout.component.KlGenericChronologyArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint.FeatureAreaBlueprint;
import dev.ikm.tinkar.entity.StampRecord;
import dev.ikm.tinkar.terms.ConceptFacade;
import dev.ikm.tinkar.terms.EntityProxy;
import dev.ikm.tinkar.terms.TinkarTerm;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Subscription;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ChronologyAreaBlueprint
        extends FeatureAreaBlueprint<ObservableEntity<?>, Feature<ObservableEntity<?>>, BorderPane>
        implements KlGenericChronologyArea<BorderPane>, KlParent<BorderPane>, KlAreaForEntity<BorderPane> {

    final AtomicReference<Subscription> selectedItemsSubscriptionReference = new AtomicReference<>(Subscription.EMPTY);
    final SimpleObjectProperty<ObservableEntity<ObservableVersion<?>>> componentProperty = new SimpleObjectProperty<>();
    {
        addPreferenceSubscription(componentProperty.subscribe(this::componentChanged));
    }

    final ObservableList<ObservableVersion<?>> selectedItems = FXCollections.observableArrayList();

    final MenuButton menuButton = new MenuButton();
    final ToolBar toolBar = new ToolBar(menuButton);
    private final GridPane gridPaneForChildren = new GridPane();
    {
        gridPaneForChildren.setAccessibleRoleDescription("Simple Chronology Area Child GridPane");
        toolBar.setMinHeight(25);
        fxObject().setTop(toolBar);
        fxObject().setCenter(gridPaneForChildren);
        MenuItem procedure = new MenuItem("Procedure");
        procedure.setOnAction(event -> componentProperty.set(ObservableEntity.get(TinkarTerm.PROCEDURE.nid())));

        MenuItem tofMenuItem = new MenuItem("Tetralogy of Fallot");
        ConceptFacade tofFacade = EntityProxy.Concept.make("Tetralogy of Fallot", UUID.fromString("4ebf1040-5f4c-5f56-96a7-8ee8de0a5bb2"));
        tofMenuItem.setOnAction(event -> componentProperty.set(ObservableEntity.get(tofFacade.nid())));

        MenuItem descriptionPattern = new MenuItem("Description Pattern");
        descriptionPattern.setOnAction(event -> componentProperty.set(ObservableEntity.get(TinkarTerm.DESCRIPTION_PATTERN.nid())));

        MenuItem description = new MenuItem("English Description");
        description.setOnAction(event -> componentProperty.set(ObservableEntity.get(TinkarTerm.ENGLISH_LANGUAGE.nid())));

        MenuItem stamp = new MenuItem("Non-existent Stamp");
        stamp.setOnAction(event -> componentProperty.set(ObservableEntity.get(StampRecord.nonExistentStamp())));

        menuButton.getItems().addAll(procedure, tofMenuItem, descriptionPattern, description, stamp);
    }

    public ChronologyAreaBlueprint(KometPreferences preferences) {
        super(preferences, new BorderPane());
    }

    public ChronologyAreaBlueprint(KlPreferencesFactory preferencesFactory,
                                   KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory, new BorderPane());
    }

    @Override
    public final GridPane gridPaneForChildren() {
        return gridPaneForChildren;
    }

    @Override
    public final ObjectProperty<ObservableEntity<ObservableVersion<?>>> chronologyProperty() {
        return componentProperty;
    }

    @Override
    public final ObservableList<ObservableVersion<?>> selectedVersions() {
        return selectedItems;
    }

    abstract protected void componentChanged(ObservableEntity<ObservableVersion<?>> oldValue,
                                             ObservableEntity<ObservableVersion<?>> newValue);

    @Override
    protected final void subAreaRevert() {
        preferences().getEntity(KlChronologyArea.PreferenceKeys.CURRENT_ENTITY).ifPresentOrElse(
                entityProxy ->
                        componentProperty.set(ObservableEntity.get(entityProxy.nid())),
                        () -> componentProperty.set(null));
        subChronologyAreaRevert();
    }

    protected abstract void subChronologyAreaRevert();

    @Override
    protected final void subAreaSave() {
        if (componentProperty.get() != null) {
            EntityProxy entityFacade = EntityProxy.make(componentProperty.get().nid());
            preferences().putEntity(KlChronologyArea.PreferenceKeys.CURRENT_ENTITY, entityFacade);
        } else {
            preferences().remove(KlChronologyArea.PreferenceKeys.CURRENT_ENTITY);
        }
        preferencesChanged();
        subChronologyAreaSave();
    }

    protected abstract void subChronologyAreaSave();

    public interface Factory<KL extends ChronologyAreaBlueprint>
            extends KlAreaForEntity.Factory<BorderPane, KL> {

    }

}
