package dev.ikm.orchestration.provider.knowledge.layout.area;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.area.KlSupplementalArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.WidgetBlueprint;
import javafx.scene.layout.Pane;

public abstract class SupplementalAreaBlueprint<FX extends Pane> extends WidgetBlueprint<FX>
        implements KlSupplementalArea<FX> {

    public SupplementalAreaBlueprint(KometPreferences preferences, FX fxObject) {
        super(preferences, fxObject);
    }

    public SupplementalAreaBlueprint(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory, FX fxObject) {
        super(preferencesFactory, gadgetFactory, fxObject);
    }

}
