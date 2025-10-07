package dev.ikm.orchestration.provider.knowledge.layout.feature.simple;

import dev.ikm.komet.framework.observable.*;
import dev.ikm.komet.identicon.LifeHash;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.area.KlAreaForPublicId;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint.FeatureAreaBlueprint;
import dev.ikm.tinkar.common.id.PublicId;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;


public final class PublicIdArea extends FeatureAreaBlueprint<PublicId, Feature<PublicId>, StackPane>
        implements KlAreaForPublicId<StackPane> {

    {
        fxObject().getChildren().forEach(node -> node.setOnContextMenuRequested(event ->
                LayoutContextMenu.makeContextMenu(this).show(fxObject(), event.getScreenX(), event.getScreenY())));
        fxObject().setOnContextMenuRequested(event ->
                LayoutContextMenu.makeContextMenu(this).show(fxObject(), event.getScreenX(), event.getScreenY()));
    }

    public PublicIdArea(KometPreferences preferences) {
        super(preferences, new StackPane());
    }

    public PublicIdArea(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory, new StackPane());
    }

    @Override
    protected void subFeatureAreaBlueprintRestoreFromPreferencesOrDefault() {
        getFeature().ifPresent(publicIdFeature -> setupPublicId(publicIdFeature.value()));
    }

    @Override
    protected void featureChanged(Feature<PublicId> oldFeature, Feature<PublicId> newFeature) {
        fxObject().getChildren().clear();
        if (newFeature != null && newFeature.value() != null) {
            setupPublicId(newFeature.value());
        }
    }

    private void setupPublicId(PublicId publicId) {
        ImageView hashImage = LifeHash.makeFxImage(publicId.idString());
        fxObject().setAlignment(Pos.TOP_LEFT);
        fxObject().getChildren().add(hashImage);
        Tooltip tooltip = new Tooltip(publicId.idString());
        Tooltip.install(hashImage, tooltip);
    }

    @Override
    protected void subAreaRevert() {

    }

    @Override
    protected void subAreaSave() {

    }


    public static Factory factory() {
        return new Factory();
    }

    public static PublicIdArea restore(KometPreferences preferences) {
        return PublicIdArea.factory().restore(preferences);
    }

    public static PublicIdArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
        return PublicIdArea.factory().create(preferencesFactory, areaGridSettings);
    }

    public static PublicIdArea create(KlPreferencesFactory preferencesFactory) {
        return PublicIdArea.factory().create(preferencesFactory);
    }

    public static class Factory implements KlAreaForPublicId.Factory {

        public Factory() {}

        @Override
        public PublicIdArea restore(KometPreferences preferences) {
            PublicIdArea publicIdArea = new PublicIdArea(preferences);
            return publicIdArea;
        }

        @Override
        public PublicIdArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            PublicIdArea publicIdArea = new PublicIdArea(preferencesFactory, this);
            publicIdArea.setAreaLayout(areaGridSettings);
            return publicIdArea;
        }

        @Override
        public PublicIdArea create(KlPreferencesFactory preferencesFactory) {
            PublicIdArea publicIdArea = new PublicIdArea(preferencesFactory, this);
            publicIdArea.setAreaLayout(defaultAreaGridSettings());
            return publicIdArea;
        }

    }

}
