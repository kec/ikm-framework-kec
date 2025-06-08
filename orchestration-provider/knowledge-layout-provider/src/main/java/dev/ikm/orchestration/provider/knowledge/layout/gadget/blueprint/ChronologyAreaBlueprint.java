package dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;

import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.layout.component.KlChronologyArea;
import dev.ikm.komet.layout.component.KlGenericChronologyArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;

public non-sealed abstract class ChronologyAreaBlueprint<OE extends ObservableEntity>
        extends StateAndContextBlueprint<BorderPane>
        implements KlGenericChronologyArea {
    SimpleObjectProperty<OE> chronologyProperty = new SimpleObjectProperty<>();
    public ChronologyAreaBlueprint(KometPreferences preferences) {
        super(preferences, new BorderPane());
    }

    public ChronologyAreaBlueprint(KlPreferencesFactory preferencesFactory, KlChronologyArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory, new BorderPane());
    }


    public ObjectProperty<OE> chronologyProperty() {
        return chronologyProperty;
    }

    @Override
    protected void subContextSave() {

    }

    @Override
    protected void subContextRevert() {

    }
}
