package dev.ikm.orchestration.provider.knowledge.layout.component;

import dev.ikm.komet.framework.observable.Feature;
import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlView;
import dev.ikm.komet.layout.LayoutComputer;
import dev.ikm.komet.layout.LayoutKey;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.component.KlGenericChronologyArea;
import dev.ikm.komet.layout.feature.KlListOfVersionArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.RowIncrementLayoutComputer;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Subscription;
import org.eclipse.collections.api.list.ImmutableList;

public class ChronologyVersionsListDetailsArea extends ChronologyAreaBlueprint {

    private ChronologyVersionsListDetailsArea(KometPreferences preferences) {
        super(preferences);
    }

    private ChronologyVersionsListDetailsArea(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory);
    }

    @Override
    protected void componentChanged(ObservableEntity<ObservableVersion<EntityVersion>> oldValue,
                                    ObservableEntity<ObservableVersion<EntityVersion>> newValue) {
        selectedItemsSubscriptionReference.get().unsubscribe();
        selectedItemsSubscriptionReference.set(Subscription.EMPTY);
        if (newValue != null) {
            ImmutableList<Feature> features = newValue.getFeatures(calculatorForContext());

            AreaGridSettings thisArea = this.getAreaLayout();

            gridPaneForChildren().getChildren().clear();

            gridPaneForChildren().add(new Label("Component selected: " + newValue), 0, 0);


            RowIncrementLayoutComputer rowIncrementLayoutComputer =
                    RowIncrementLayoutComputer.create(this.getMasterLayout());
            // Can add additional content here...
            // Add a description for a component as an example and then feed it into the layout computer.

            LayoutKey.ForArea layoutKeyForArea = thisArea.layoutKeyForArea();
            ImmutableList<LayoutComputer.LayoutElement> layout = rowIncrementLayoutComputer.layout(features,
                    layoutKeyForArea.makeAreaKeyProvider());

            layout.forEach(layoutElement -> {

                KlView klView = layoutElement.areaGridSettings().makeAndAddToParent(this);

                switch (klView) {
                    case KlListOfVersionArea listOfVersionArea -> {
                        selectedItemsSubscriptionReference.get().and(listOfVersionArea.selectedItems().subscribe(() -> {
                            selectedItems.setAll(listOfVersionArea.selectedItems());
                            selectedItemsChanged();
                        }));
                    }
                    default -> {
                        // let others just pass through.
                    }
                }
            });
        } else {
            gridPaneForChildren().add(new Label("No component selected"), 0, 0);
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

    public static Factory factory() {
        return new Factory();
    }

    public static ChronologyVersionsListDetailsArea restore(KometPreferences preferences) {
        return factory().restore(preferences);
    }

    public static ChronologyVersionsListDetailsArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
        return factory().create(preferencesFactory, areaGridSettings);
    }
    public static class Factory implements KlGenericChronologyArea.Factory<BorderPane, ChronologyVersionsListDetailsArea> {
        @Override
        public ChronologyVersionsListDetailsArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            ChronologyVersionsListDetailsArea chronologyVersionsListDetailsArea = new ChronologyVersionsListDetailsArea(preferencesFactory, this);
            chronologyVersionsListDetailsArea.setGridLayout(areaGridSettings);
            return chronologyVersionsListDetailsArea;
        }

        @Override
        public ChronologyVersionsListDetailsArea restore(KometPreferences preferences) {
            return new ChronologyVersionsListDetailsArea(preferences);
        }
    }
}
