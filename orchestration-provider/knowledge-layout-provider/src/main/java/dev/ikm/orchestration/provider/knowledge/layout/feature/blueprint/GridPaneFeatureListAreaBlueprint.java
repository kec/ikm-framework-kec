package dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint;

import dev.ikm.komet.framework.observable.*;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public abstract class GridPaneFeatureListAreaBlueprint<LE extends LocatableFeature>
        extends FeatureAreaBlueprint<BorderPane, FeatureList<LE>> {
    final GridPane listElementGrid = new GridPane();

    public GridPaneFeatureListAreaBlueprint(KometPreferences preferences) {
        super(preferences, new BorderPane());
    }

    public GridPaneFeatureListAreaBlueprint(KlPreferencesFactory preferencesFactory, KlArea.Factory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new BorderPane());
    }

    public final FeatureList<LE> getList() {
        return featurePropertyWrapper.get().getValue();
    }

     public final ObservableList<LE> selectedItems() {
        return FXCollections.emptyObservableList();
    }
}
