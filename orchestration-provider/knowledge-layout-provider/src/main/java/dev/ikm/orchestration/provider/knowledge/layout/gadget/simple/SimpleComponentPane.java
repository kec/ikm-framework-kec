package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.component.KlGenericComponentPane;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.WidgetBlueprint;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;

public class SimpleComponentPane extends WidgetBlueprint<BorderPane> implements KlGenericComponentPane<BorderPane> {

    SimpleObjectProperty<ObservableEntity> componentProperty = new SimpleObjectProperty<>();

    public SimpleComponentPane(KometPreferences preferences) {
        super(preferences, new BorderPane());
    }

    public SimpleComponentPane(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new BorderPane());
    }

    @Override
    public ObjectProperty<ObservableEntity> componentProperty() {
        return componentProperty;
    }

    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }

    @Override
    public void unsubscribeFromContext() {

    }

    @Override
    public void subscribeToContext() {

    }
}
