package dev.ikm.orchestration.provider.knowledge.layout.version;

import dev.ikm.komet.framework.observable.Feature;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.feature.KlListOfVersionArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint.FeatureListViewAreaBlueprint;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ListView;
import javafx.util.Subscription;

import java.util.concurrent.atomic.AtomicReference;


public final class SimpleVersionFeatureList
        extends FeatureListViewAreaBlueprint<Feature<ObservableVersion>>
        implements KlListOfVersionArea<ListView<Feature<ObservableVersion>>> {

    AtomicReference<Subscription> transientSubscriptions = new AtomicReference<>(Subscription.EMPTY);

    private final SimpleObjectProperty<VersionsAndSelection> versionAndSelectionProperty = new SimpleObjectProperty<>();

    {
        versionAndSelectionProperty.set(new VersionsAndSelection(fxObject().getItems(),
                fxObject().getSelectionModel().getSelectedItems()));
        transientSubscriptions.get().and(fxObject().getItems().subscribe(this::updateVersionsAndSelection));
        transientSubscriptions.get().and(fxObject().getSelectionModel().getSelectedItems().subscribe(this::updateVersionsAndSelection));
    }

    public SimpleVersionFeatureList(KometPreferences preferences) {
        super(preferences);
    }

    public SimpleVersionFeatureList(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory);
    }

    private void updateVersionsAndSelection() {
        Platform.runLater(() -> {
            versionAndSelectionProperty.set(new VersionsAndSelection(fxObject().getItems(),
                    fxObject().getSelectionModel().getSelectedItems()));
        });
    }

    @Override
    public ReadOnlyObjectProperty<VersionsAndSelection> versionsAndSelectionProperty() {
        return versionAndSelectionProperty;
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

    public static SimpleVersionFeatureList restore(KometPreferences preferences) {
        return factory().restore(preferences);
    }
    public static SimpleVersionFeatureList create(KlPreferencesFactory preferencesFactory) {
        return factory().create(preferencesFactory);
    }

    public static SimpleVersionFeatureList create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
        return factory().create(preferencesFactory, areaGridSettings);

    }
    public static final class Factory implements KlListOfVersionArea.Factory<ListView<Feature<ObservableVersion>>> {
        @Override
        public SimpleVersionFeatureList create(KlPreferencesFactory preferencesFactory) {
            return create(preferencesFactory, defaultAreaGridSettings());
        }

        @Override
        public SimpleVersionFeatureList create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            return new SimpleVersionFeatureList(preferencesFactory, areaGridSettings.makeAreaFactory());
        }

        @Override
        public SimpleVersionFeatureList restore(KometPreferences preferences) {
            return new SimpleVersionFeatureList(preferences);
        }
    }
}
