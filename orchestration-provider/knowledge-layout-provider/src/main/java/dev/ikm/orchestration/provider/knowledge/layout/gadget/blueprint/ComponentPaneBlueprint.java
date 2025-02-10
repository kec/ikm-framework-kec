package dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;

import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.component.KlGenericComponentPane;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;

public non-sealed abstract class ComponentPaneBlueprint<OE extends ObservableEntity> extends GadgetBlueprint<BorderPane> {
    SimpleObjectProperty<OE> componentProperty = new SimpleObjectProperty<>();
    public ComponentPaneBlueprint(KometPreferences preferences) {
        super(preferences, new BorderPane());
    }

    public ComponentPaneBlueprint(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new BorderPane());
    }

    public ObjectProperty<OE> componentProperty() {
        return componentProperty;
    }

    @Override
    protected void subGadgetSave() {

    }

    @Override
    protected void subGadgetRevert() {

    }
}
