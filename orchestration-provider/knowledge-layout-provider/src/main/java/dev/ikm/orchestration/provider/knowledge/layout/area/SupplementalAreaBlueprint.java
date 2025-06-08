package dev.ikm.orchestration.provider.knowledge.layout.area;

import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.KlParent;
import dev.ikm.komet.layout.area.KlSupplementalArea;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.AreaBlueprint;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public abstract class SupplementalAreaBlueprint extends AreaBlueprint<BorderPane>
        implements KlSupplementalArea<BorderPane>, KlParent<BorderPane> {

    protected final GridPane gridPaneForChildren = new GridPane();

    {
        fxObject().setCenter(gridPaneForChildren);
    }

    public SupplementalAreaBlueprint(KometPreferences preferences) {
        super(preferences, new BorderPane(new GridPane()));
    }

    public SupplementalAreaBlueprint(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory, new BorderPane(new GridPane()));
    }

    @Override
    public final GridPane gridPaneForChildren() {
        return gridPaneForChildren;
    }

    public interface Factory<KL extends KlSupplementalArea<BorderPane>>
            extends KlSupplementalArea.Factory<BorderPane, KL> {
    }

}
