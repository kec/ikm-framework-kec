package dev.ikm.orchestration.provider.knowledge.layout.field.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.attribute.KlListOfVersionsAttributeArea;
import dev.ikm.komet.layout.attribute.factory.KlListOfVersionsFieldAreaFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.LayoutContextMenu;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.StampVersionListCellFactory;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Subscription;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AttributeListAreaBlueprint<LE extends EntityVersion>
        extends AttributeAreaBlueprint<ListView, ObservableList<LE>>
        implements KlListOfVersionsAttributeArea<LE, ListView>  {

    AtomicReference<Subscription> subscriptionReference = new AtomicReference<>(Subscription.EMPTY);
    ObservableList<LE> selectedItemsList = FXCollections.observableArrayList();

    protected AttributeListAreaBlueprint(KometPreferences preferences, ListView fxGadget) {
        super(preferences, fxGadget);
        setup();
    }

    protected AttributeListAreaBlueprint(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory, ListView fxGadget) {
        super(preferencesFactory, gadgetFactory, fxGadget);
        setup();
    }

    private void setup() {
        fxObject.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fxObject.setCellFactory(new StampVersionListCellFactory(this));
        fxObject.setContextMenu(LayoutContextMenu.makeContextMenu(this, KlListOfVersionsFieldAreaFactory.class));
    }

    @Override
    public ObservableList<LE> selectedItems() {
        return selectedItemsList;
    }

    @Override
    protected void updateField() {
        // The field is an Observaable list. If updated, it is a new list, not a changed list.
        subscriptionReference.getAndSet(Subscription.EMPTY).unsubscribe();
        this.fxObject.getItems().clear();
        this.fxObject.getItems().addAll(this.attributeValue());
        this.updateSelectedItems();
        this.subscriptionReference.get().and(
                this.fxObject.getSelectionModel().getSelectedItems().subscribe(this::updateSelectedItems));
    }

    private void updateSelectedItems() {
        this.selectedItemsList.setAll(this.fxObject.getSelectionModel().getSelectedItems());
    }

}
