package dev.ikm.orchestration.provider.knowledge.layout.component;

import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.component.KlGenericComponentArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.WidgetBlueprint;
import dev.ikm.tinkar.common.service.PrimitiveData;
import dev.ikm.tinkar.entity.EntityVersion;
import dev.ikm.tinkar.entity.StampRecord;
import dev.ikm.tinkar.terms.TinkarTerm;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.util.Subscription;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ComponentAreaBlueprint
        extends WidgetBlueprint<BorderPane>
        implements KlGenericComponentArea<BorderPane> {

    final AtomicReference<Subscription> selectedItemsSubscriptionReference = new AtomicReference<>(Subscription.EMPTY);
    final SimpleObjectProperty<ObservableEntity<ObservableVersion<EntityVersion>>> componentProperty = new SimpleObjectProperty<>();
    {
        addPreferenceSubscription(componentProperty.subscribe(this::componentChanged));
    }

    MenuButton menuButton = new MenuButton();
    ToolBar toolBar = new ToolBar(menuButton);
    {
        toolBar.setMinHeight(25);
        fxObject.setTop(toolBar);
        MenuItem procedure = new MenuItem("Procedure");
        procedure.setOnAction(event -> {
            componentProperty.set(ObservableEntity.get(TinkarTerm.PROCEDURE.nid()));
        });

        MenuItem descriptionPattern = new MenuItem("Description Pattern");
        descriptionPattern.setOnAction(event -> {
            componentProperty.set(ObservableEntity.get(TinkarTerm.DESCRIPTION_PATTERN.nid()));
        });

        MenuItem description = new MenuItem("English Description");
        description.setOnAction(event -> {
            componentProperty.set(ObservableEntity.get(PrimitiveData.nid(UUID.fromString("f248d1b8-a903-4005-a267-214280c9faf0"))));
        });

        MenuItem stamp = new MenuItem("Non-existent Stamp");
        stamp.setOnAction(event -> {
            componentProperty.set(ObservableEntity.get(StampRecord.nonExistentStamp()));
        });

        menuButton.getItems().addAll(procedure, descriptionPattern, description, stamp);
    }


    public ComponentAreaBlueprint(KometPreferences preferences) {
        super(preferences, new BorderPane());
    }

    public ComponentAreaBlueprint(KlPreferencesFactory preferencesFactory,
                                  KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new BorderPane());
    }

    @Override
    public final ObjectProperty<ObservableEntity<ObservableVersion<EntityVersion>>> componentProperty() {
        return componentProperty;
    }

    abstract protected void componentChanged(ObservableEntity<ObservableVersion<EntityVersion>> oldValue,
                                             ObservableEntity<ObservableVersion<EntityVersion>> newValue);

}
