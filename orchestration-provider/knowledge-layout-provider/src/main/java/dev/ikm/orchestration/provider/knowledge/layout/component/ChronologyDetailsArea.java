package dev.ikm.orchestration.provider.knowledge.layout.component;

import dev.ikm.komet.framework.observable.Feature;
import dev.ikm.komet.framework.observable.FeatureKey;
import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.KlPeerToRegion;
import dev.ikm.komet.layout.LayoutComputer;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.area.KlAreaForEntity;
import dev.ikm.komet.layout.area.KlAreaForListOfVersions;
import dev.ikm.komet.layout.component.KlGenericChronologyArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.RowIncrementLayoutComputer;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Subscription;
import org.eclipse.collections.api.list.ImmutableList;

public class ChronologyDetailsArea extends ChronologyAreaBlueprint
        implements KlAreaForEntity<BorderPane> {

    private ChronologyDetailsArea(KometPreferences preferences) {
        super(preferences);
        this.fxObject().setId("ChronologyDetailsArea");
        this.gridPaneForChildren().setId("ChronologyDetailsArea GridPane for Children");
    }

    private ChronologyDetailsArea(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory);
        this.fxObject().setId("ChronologyDetailsArea");
        this.gridPaneForChildren().setId("ChronologyDetailsArea GridPane for Children");
    }

    @Override
    protected void subFeatureAreaBlueprintRestoreFromPreferencesOrDefault() {
        // Nothing to restore that is not handled by FeatureAreaBlueprint.
    }

    @Override
    protected void featureChanged(Feature<ObservableEntity<?>> oldFeature, Feature<ObservableEntity<?>> newFeature) {

    }

    @Override
    protected void componentChanged(ObservableEntity<ObservableVersion<?>> oldValue,
                                    ObservableEntity<ObservableVersion<?>> newValue) {
        unbindKnowledgeLayoutDescendents();
        selectedItemsSubscriptionReference.get().unsubscribe();
        selectedItemsSubscriptionReference.set(Subscription.EMPTY);
        if (newValue != null) {
            ImmutableList<Feature> features = newValue.getFeatures();

            // Can add or remove features here for layout...
            features = features.select(feature -> FeatureKey.Entity.PublicId().match(feature.featureKey())
                    || FeatureKey.Entity.VersionSet().match(feature.featureKey()));

            gridPaneForChildren().getChildren().clear();

            RowIncrementLayoutComputer rowIncrementLayoutComputer =
                    RowIncrementLayoutComputer.create(this.getMasterLayout());
            // Can add additional content here...
            // Add a description for a component as an example and then feed it into the layout computer.
            KlPeerToRegion.LOG.info("Laying out: " + this.getClass().getSimpleName());
            ImmutableList<LayoutComputer.LayoutElement> layout = rowIncrementLayoutComputer.layout(features,
                    this.getLayoutKeyForArea().makeAreaKeyProvider());

            layout.forEach(layoutElement -> {

                KlArea klArea = layoutElement.areaGridSettings().makeAndAddToParent(this);
                klArea.setId(layoutElement);

                switch (klArea) {
                    case KlAreaForListOfVersions listOfVersionArea ->
                            selectedItemsSubscriptionReference.get().and(listOfVersionArea.selectedItems().subscribe(() -> {
                                selectedItems.setAll(listOfVersionArea.selectedItems());
                                selectedItemsChanged();
                            }));
                    default -> {
                        // let others just pass through.
                    }
                }
            });
        } else {
            gridPaneForChildren().add(new Label("No component selected"), 0, 0);
        }
        bindKnowledgeLayoutDescendents();
    }

    private void selectedItemsChanged() {
        KlPeerToRegion.LOG.info("Selected items: " + selectedItems.size() + " " + selectedItems);
    }

    @Override
    protected void subChronologyAreaRevert() {
    }

    @Override
    protected void subChronologyAreaSave() {
    }

    public static Factory factory() {
        return new Factory();
    }

    public static ChronologyDetailsArea restore(KometPreferences preferences) {
        return factory().restore(preferences);
    }

    public static ChronologyDetailsArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
        return factory().create(preferencesFactory, areaGridSettings);
    }

    public static class Factory implements KlGenericChronologyArea.Factory<BorderPane, ChronologyDetailsArea> {
        @Override
        public ChronologyDetailsArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            ChronologyDetailsArea chronologyDetailsArea = new ChronologyDetailsArea(preferencesFactory, this);
            chronologyDetailsArea.setAreaLayout(areaGridSettings);
            return chronologyDetailsArea;
        }

        @Override
        public ChronologyDetailsArea restore(KometPreferences preferences) {
            ChronologyDetailsArea chronologyDetailsArea = new ChronologyDetailsArea(preferences);
            return chronologyDetailsArea;
        }
    }
}
