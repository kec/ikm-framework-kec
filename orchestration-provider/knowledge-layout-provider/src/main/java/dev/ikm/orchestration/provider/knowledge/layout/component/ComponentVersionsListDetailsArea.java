package dev.ikm.orchestration.provider.knowledge.layout.component;

import dev.ikm.komet.framework.observable.*;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.area.factory.KlAttributeAreaFactory;
import dev.ikm.komet.layout.area.factory.KlDynamicAreaFactory;
import dev.ikm.komet.layout.attribute.KlAttributeArea;
import dev.ikm.komet.layout.attribute.KlListAttributeArea;
import dev.ikm.komet.layout.attribute.factory.KlFieldAreaFactory;
import dev.ikm.komet.layout.preferences.KlPreferenceFactoryProvider;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.DefaultLayoutFactory;
import dev.ikm.tinkar.common.service.PluggableService;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.util.Subscription;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;

import java.lang.reflect.InvocationTargetException;

public class ComponentVersionsListDetailsArea extends ComponentAreaBlueprint {

    final ObservableList<ObservableVersion<EntityVersion>> selectedItems = FXCollections.observableArrayList();

    public ComponentVersionsListDetailsArea(KometPreferences preferences) {
        super(preferences);
    }

    public ComponentVersionsListDetailsArea(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory);
    }

    @Override
    public ObservableList<ObservableVersion<EntityVersion>> selectedVersions() {
        return selectedItems;
    }

    @Override
    protected void componentChanged(ObservableEntity<ObservableVersion<EntityVersion>> oldValue,
                                    ObservableEntity<ObservableVersion<EntityVersion>> newValue) {
        selectedItemsSubscriptionReference.get().unsubscribe();
        selectedItemsSubscriptionReference.set(Subscription.EMPTY);
        if (newValue != null) {
            ImmutableMap<AttributeLocator, ObservableField> fieldMap = newValue.getObservableAttributes();
            ImmutableList<AttributeLocator> fields = fieldMap.keysView().toImmutableSortedList();

            DefaultLayoutFactory defaultLayoutFactory = new DefaultLayoutFactory();
            GridPane gridPane = new GridPane();
            ImmutableList<KlDynamicAreaFactory> layout = defaultLayoutFactory.create(fields);
            layout.forEach(layoutForComponent -> {
                try {
                    Class<?> factoryClass = PluggableService.forName(layoutForComponent.areaFactoryName());
                    KlFieldAreaFactory<Object, Region, KlAttributeArea<Object, Region>> fieldAreaFactory = (KlFieldAreaFactory) factoryClass.getDeclaredConstructor().newInstance();
                    KlPreferencesFactory preferencesFactory = new KlPreferenceFactoryProvider.PreferenceFactoryWithParentPreferences(preferences(), fieldAreaFactory.klImplementationClass());
                    KlAttributeArea<Object, Region> fieldArea = fieldAreaFactory.create(preferencesFactory);

                    if (layoutForComponent instanceof KlAttributeAreaFactory fieldAreaSpecifierForField) {
                        ObservableField field = fieldMap.get(fieldAreaSpecifierForField.attributeLocator());
                        fieldArea.setAttribute(field);
                    }

                    fieldArea.setGridLayout(layoutForComponent.gridLayout());
                    gridPane.getChildren().add(fieldArea.fxObject());
                    if (fieldArea instanceof KlListAttributeArea listOfVersionsArea) {
                        selectedItemsSubscriptionReference.get().and(listOfVersionsArea.selectedItems().subscribe(() -> {
                            selectedItems.setAll(listOfVersionsArea.selectedItems());
                            selectedItemsChanged();
                        }));
                    }

                } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                         IllegalAccessException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
            fxObject.setCenter(gridPane);
        } else {
            fxObject.setCenter(new Label("No component selected"));
        }
    }

    private void selectedItemsChanged() {
        LOG.info("Selected items: " + selectedItems.size() + " " + selectedItems);
    }


    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }

    @Override
    public void subscribeToContext() {

    }
}
