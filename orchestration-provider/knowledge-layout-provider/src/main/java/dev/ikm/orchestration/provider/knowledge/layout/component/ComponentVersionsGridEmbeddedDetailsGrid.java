package dev.ikm.orchestration.provider.knowledge.layout.component;


import dev.ikm.komet.framework.observable.AttributeLocator;
import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableField;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import org.eclipse.collections.api.map.ImmutableMap;

public class ComponentVersionsGridEmbeddedDetailsGrid
        extends ComponentAreaBlueprint {

    public ComponentVersionsGridEmbeddedDetailsGrid(KometPreferences preferences) {
        super(preferences);
    }

    public ComponentVersionsGridEmbeddedDetailsGrid(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory);
    }

    @Override
    public ObservableList<ObservableVersion<EntityVersion>> selectedVersions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void componentChanged(ObservableEntity<ObservableVersion<EntityVersion>> oldValue,
                                    ObservableEntity<ObservableVersion<EntityVersion>> newValue) {
        if (newValue != null) {
            ImmutableMap<AttributeLocator, ObservableField> fieldMap = newValue.getObservableAttributes();
            fxObject.setCenter(new Label("Field layout work in progress"));
        } else {
            fxObject.setCenter(new Label("No component selected"));
        }
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
