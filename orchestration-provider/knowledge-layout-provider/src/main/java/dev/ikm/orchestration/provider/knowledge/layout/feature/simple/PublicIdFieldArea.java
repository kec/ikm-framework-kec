package dev.ikm.orchestration.provider.knowledge.layout.feature.simple;

import dev.ikm.komet.framework.observable.ObservableField;
import dev.ikm.komet.identicon.LifeHash;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.feature.KlFieldAreaForPublicId;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.context.ViewContextMenuButtonArea;
import dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint.FeatureAreaBlueprint;
import dev.ikm.tinkar.common.id.PublicId;
import javafx.beans.property.Property;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public final class PublicIdFieldArea extends FeatureAreaBlueprint<StackPane, ObservableField<PublicId>>
        implements KlFieldAreaForPublicId<StackPane> {

    public PublicIdFieldArea(KometPreferences preferences) {
        super(preferences, new StackPane());
        setup();
    }

    public PublicIdFieldArea(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory, new StackPane());
        setup();
    }

    private void setup() {
        fxObject().getChildren().forEach(node -> node.setOnContextMenuRequested(event ->
                LayoutContextMenu.makeContextMenu(this).show(fxObject(), event.getScreenX(), event.getScreenY())));
        fxObject().setOnContextMenuRequested(event ->
            LayoutContextMenu.makeContextMenu(this).show(fxObject(), event.getScreenX(), event.getScreenY()));
    }

    @Override
    protected void propertyWrapperUpdated(Property<ObservableField<PublicId>> oldValue, Property<ObservableField<PublicId>> newValue) {
        fxObject().getChildren().clear();
        if (newValue != null) {
            PublicId publicId = newValue.getValue().value();
            ImageView hashImage = LifeHash.makeFxImage(publicId.idString());
            fxObject().setAlignment(Pos.TOP_LEFT);
            fxObject().getChildren().add(hashImage);
        }
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

    public static PublicIdFieldArea restore(KometPreferences preferences) {
        return PublicIdFieldArea.factory().restore(preferences);
    }

    public static PublicIdFieldArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
        return PublicIdFieldArea.factory().create(preferencesFactory, areaGridSettings);
    }

    public static PublicIdFieldArea create(KlPreferencesFactory preferencesFactory) {
        return PublicIdFieldArea.factory().create(preferencesFactory);
    }

    public static class Factory implements KlFieldAreaForPublicId.Factory {

        public static Factory get() {
            return new Factory();
        }

        @Override
        public PublicIdFieldArea restore(KometPreferences preferences) {
            return new PublicIdFieldArea(preferences);
        }

        @Override
        public PublicIdFieldArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            PublicIdFieldArea publicIdFieldArea = new PublicIdFieldArea(preferencesFactory, this);
            publicIdFieldArea.setGridLayout(areaGridSettings);
            return publicIdFieldArea;
        }

        @Override
        public PublicIdFieldArea create(KlPreferencesFactory preferencesFactory) {
            PublicIdFieldArea publicIdFieldArea = new PublicIdFieldArea(preferencesFactory, this);
            publicIdFieldArea.setGridLayout(defaultAreaGridSettings());
            return publicIdFieldArea;
        }

    }

}
