package dev.ikm.orchestration.provider.knowledge.layout.version;

import dev.ikm.komet.framework.observable.AttributeCategory;
import dev.ikm.komet.framework.observable.AttributeLocator;
import dev.ikm.komet.framework.observable.DirectListElementAttributeLocator;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.area.KlArea;
import dev.ikm.komet.layout.area.factory.KlAttributeAreaFactory;
import dev.ikm.komet.layout.area.factory.KlDynamicAreaFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.version.KlMultiVersionArea;
import dev.ikm.komet.layout.version.KlMultiVersionAreaFactory;
import dev.ikm.komet.layout.version.KlVersionArea;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.area.SupplementalArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.primitive.IntObjectMaps;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;

public class MultiVersionArea
        extends SupplementalArea
        implements KlMultiVersionArea<ObservableVersion, BorderPane> {

    final GridPane gridPane = new GridPane();
    final ObservableList<ObservableVersion> observableVersions = FXCollections.observableArrayList();
    final ObservableList<KlVersionArea<ObservableVersion, BorderPane>> versionAreas = FXCollections.observableArrayList();

    {
        observableVersions.subscribe(this::versionsListChanged);
        fxObject.setCenter(gridPane);
    }

    public MultiVersionArea(KometPreferences preferences) {
        super(preferences);
    }

    public MultiVersionArea(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory);
    }

    private void versionsListChanged() {
        // The order of the layout may change, need to feed them all to the layout manager.
        gridPane.getChildren().clear();
        versionAreas.forEach(KlVersionArea::unsubscribeFromContext);
        versionAreas.clear();

        MutableList<AttributeLocator> versionSpecifications = Lists.mutable.empty();
        MutableIntObjectMap<ObservableVersion> indexVersionMap = IntObjectMaps.mutable.empty();
        for (ObservableVersion observableVersion : observableVersions) {
            int index = observableVersion.entity().versions().indexOf(observableVersion);
            indexVersionMap.put(index, observableVersion);
            versionSpecifications.add(AttributeLocator.direct.list(AttributeCategory.COMPONENT_VERSION, index));
        }

        for (KlDynamicAreaFactory areaFactory : layoutComputer().create(versionSpecifications.toImmutable())) {
            KlArea area = areaFactory.makeAreaInParent(preferences());
            gridPane.getChildren().add(area.fxObject());
            if (areaFactory instanceof KlAttributeAreaFactory attributeAreaFactory &&
                attributeAreaFactory.attributeLocator() instanceof DirectListElementAttributeLocator listElementLocator &&
                area instanceof KlVersionArea versionArea) {
                versionArea.versionProperty().setValue(indexVersionMap.get(listElementLocator.index()));
                versionAreas.add(versionArea);
            }
        }

        if (observableVersions.isEmpty()) {
            fxObject.setVisible(false);
        } else {
            fxObject.setVisible(true);
        }
    }

    @Override
    public ObservableList<ObservableVersion> observableVersions() {
        return observableVersions;
    }

    @Override
    public ObservableList<KlVersionArea<ObservableVersion, BorderPane>> klVersionAreas() {
        return versionAreas;
    }

    public static class Factory implements KlMultiVersionAreaFactory<ObservableVersion, BorderPane> {

        @Override
        public MultiVersionArea create(KlPreferencesFactory preferencesFactory) {
            return new MultiVersionArea(preferencesFactory, this);
        }

        @Override
        public MultiVersionArea restore(KometPreferences preferences) {
            return new MultiVersionArea(preferences);
        }

        @Override
        public Class<? extends KlMultiVersionArea<ObservableVersion, BorderPane>> klImplementationClass() {
            return MultiVersionArea.class;
        }

        @Override
        public Class<ObservableVersion> versionType() {
            return ObservableVersion.class;
        }
    }
}
