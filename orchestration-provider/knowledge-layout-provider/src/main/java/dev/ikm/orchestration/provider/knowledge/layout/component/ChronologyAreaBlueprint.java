package dev.ikm.orchestration.provider.knowledge.layout.component;

import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.component.KlGenericChronologyArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.AreaBlueprint;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.StampRecord;
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

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ChronologyAreaBlueprint
        extends AreaBlueprint<BorderPane>
        implements KlGenericChronologyArea<BorderPane> {

    final AtomicReference<Subscription> selectedItemsSubscriptionReference = new AtomicReference<>(Subscription.EMPTY);
    final SimpleObjectProperty<ObservableEntity<ObservableVersion<EntityVersion>>> componentProperty = new SimpleObjectProperty<>();
    {
        addPreferenceSubscription(componentProperty.subscribe(this::componentChanged));
    }

    final ObservableList<ObservableVersion<EntityVersion>> selectedItems = FXCollections.observableArrayList();


    final MenuButton menuButton = new MenuButton();
    final ToolBar toolBar = new ToolBar(menuButton);
    private final GridPane gridPane = new GridPane();
    {
        toolBar.setMinHeight(25);
        fxObject().setTop(toolBar);
        fxObject().setCenter(gridPane);
        gridPane.add(new Label("Simple Chronology Area"), 0, 0);
        MenuItem procedure = new MenuItem("Procedure");
        procedure.setOnAction(event -> componentProperty.set(ObservableEntity.get(TinkarTerm.PROCEDURE.nid())));

        MenuItem descriptionPattern = new MenuItem("Description Pattern");
        descriptionPattern.setOnAction(event -> componentProperty.set(ObservableEntity.get(TinkarTerm.DESCRIPTION_PATTERN.nid())));

        MenuItem description = new MenuItem("English Description");
        description.setOnAction(event -> componentProperty.set(ObservableEntity.get(TinkarTerm.ENGLISH_LANGUAGE.nid())));

        MenuItem stamp = new MenuItem("Non-existent Stamp");
        stamp.setOnAction(event -> componentProperty.set(ObservableEntity.get(StampRecord.nonExistentStamp())));

        menuButton.getItems().addAll(procedure, descriptionPattern, description, stamp);
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
        return gridPane;
    }

    @Override
    public final ObjectProperty<ObservableEntity<ObservableVersion<EntityVersion>>> chronologyProperty() {
        return componentProperty;
    }

    @Override
    public final ObservableList<ObservableVersion<EntityVersion>> selectedVersions() {
        return selectedItems;
    }

    abstract protected void componentChanged(ObservableEntity<ObservableVersion<EntityVersion>> oldValue,
                                             ObservableEntity<ObservableVersion<EntityVersion>> newValue);

}
