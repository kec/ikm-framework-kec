package dev.ikm.orchestration.provider.knowledge.layout.version;

import dev.ikm.komet.framework.observable.Feature;
import dev.ikm.komet.framework.observable.ObservableField;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlParent;
import dev.ikm.komet.layout.KlView;
import dev.ikm.komet.layout.LayoutComputer;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.feature.KlFeatureArea;
import dev.ikm.komet.layout.feature.KlFieldArea;
import dev.ikm.komet.layout.feature.KlListOfVersionArea.VersionsAndSelection;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.version.KlMultiVersionArea;
import dev.ikm.komet.layout.version.KlVersionArea;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.area.SupplementalArea;
import dev.ikm.orchestration.provider.knowledge.layout.area.SupplementalAreaBlueprint;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.ColumnIncrementLayoutComputer;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.RowIncrementLayoutComputer;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.util.Subscription;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import java.util.concurrent.atomic.AtomicReference;

public final class MultiVersionArea
        extends SupplementalAreaBlueprint
        implements KlMultiVersionArea<ObservableVersion, BorderPane> {


    final AtomicReference<Subscription> transientSubscriptions = new AtomicReference<>(Subscription.EMPTY);
    final ObservableList<KlVersionArea<ObservableVersion, BorderPane>> versionAreas = FXCollections.observableArrayList();

    public MultiVersionArea(KometPreferences preferences) {
        super(preferences);
    }

    public MultiVersionArea(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory);
    }

    private void selectionChanged(VersionsAndSelection versionsAndSelection) {
        // Defensive copy, list was changing in the background.
        MutableList<Feature> selectedVersions = Lists.mutable.empty();
        selectedVersions.addAll(versionsAndSelection.selectedVersions().castToList());

        Platform.runLater(() -> {
            // The order of the layout may change, need to feed them all to the layout manager.
            gridPaneForChildren.getChildren().clear();
            versionAreas.forEach(KlVersionArea::unsubscribeFromContext);
            versionAreas.clear();


//            MutableIntObjectMap<ObservableVersion> indexVersionMap = IntObjectMaps.mutable.empty();
//
//            for (ObservableVersion observableVersion : versionsAndSelection.selectedVersions()) {
//                int index = versionsAndSelection.versions().indexOf(observableVersion);
//                indexVersionMap.put(index, observableVersion);
//                selectedVersions.add(observableVersion.locator());
//            }

            // When we get the layout, we lose the connection to the ObservableFeature. How do we retain that?
            // 1. Return an immutable multi-map of feature to layout.
            // 2. Return an immutable list of Layout feature records.
            // 3. Does the association need to persist? (Perhaps not, re-running the layout should generate the same results.)

            ColumnIncrementLayoutComputer columnIncrementLayoutComputer = ColumnIncrementLayoutComputer.create(this.getMasterLayout());
            ImmutableList<LayoutComputer.LayoutElement> columnLayout =
                    columnIncrementLayoutComputer.layout(selectedVersions.toImmutable(), this.getLayoutKeyForArea().makeAreaKeyProvider());

            columnLayout.forEach(layoutElement -> {
                KlView elementArea = layoutElement.areaGridSettings().makeAndAddToParent(this);
                if (layoutElement.features().getOnly() instanceof Feature featureOfObservableVersion) {
                    // Need to lay out individual versions and set properties.
                    if (elementArea instanceof KlVersionArea versionArea) {
                        versionArea.versionProperty().setValue(featureOfObservableVersion);
                        ImmutableList<Feature> versionFeatures = featureOfObservableVersion.containingComponent().getFeatures(calculatorForContext());
                        RowIncrementLayoutComputer layoutComputerForVersion = RowIncrementLayoutComputer.create(getMasterLayout());
                        ImmutableList<LayoutComputer.LayoutElement> versionLayout = layoutComputerForVersion.layout(versionFeatures, this.getLayoutKeyForArea().makeAreaKeyProvider());
                        versionLayout.forEach(versionLayoutElement -> {
                            KlView versionElementArea = versionLayoutElement.areaGridSettings().makeAndAddToParent(versionArea);
                            if (versionElementArea instanceof KlFeatureArea featureArea) {
                                featureArea.setFeature(versionLayoutElement.features().getOnly());
                            }
                        });
                    }
                } else {
                    throw new IllegalStateException("Expecting feature of type ObservableVersion. Found: " + layoutElement.features());
                }
            });
            fxObject().setVisible(!versionsAndSelection.selectedVersions().isEmpty());
        });
    }

    @Override
    public void setVersionAndSelectionProperty(ReadOnlyObjectProperty<VersionsAndSelection> versionsAndSelectionProperty) {
        transientSubscriptions.getAndSet(Subscription.EMPTY).unsubscribe();
        transientSubscriptions.get().and(versionsAndSelectionProperty.subscribe(this::selectionChanged));
    }

    @Override
    public ObservableList<KlVersionArea<ObservableVersion, BorderPane>> klVersionAreas() {
        return versionAreas;
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

    public static MultiVersionArea restore(KometPreferences preferences) {
        return new Factory().restore(preferences);
    }

    public static MultiVersionArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
        return new Factory().create(preferencesFactory, areaGridSettings);
    }

    public static MultiVersionArea create(KlPreferencesFactory preferencesFactory) {
        return new Factory().create(preferencesFactory);
    }


    public static class Factory implements KlMultiVersionArea.Factory<BorderPane, ObservableVersion, MultiVersionArea> {

        @Override
        public MultiVersionArea restore(KometPreferences preferences) {
            return new MultiVersionArea(preferences);
        }

        @Override
        public MultiVersionArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            return new MultiVersionArea(preferencesFactory, this);
        }
    }
}
