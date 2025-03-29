package dev.ikm.orchestration.provider.knowledge.layout.field.simple;

import dev.ikm.komet.framework.observable.AttributeCategory;
import dev.ikm.komet.framework.observable.AttributeLocator;
import dev.ikm.komet.framework.observable.DirectSingularAttributeLocator;
import dev.ikm.komet.identicon.LifeHash;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.LayoutComputer;
import dev.ikm.komet.layout.area.GridLayout;
import dev.ikm.komet.layout.area.factory.KlDynamicAreaFactory;
import dev.ikm.komet.layout.attribute.KlPublicIdAttributeArea;
import dev.ikm.komet.layout.attribute.factory.KlPublicIdFieldAreaFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.field.blueprint.AttributeAreaBlueprint;
import dev.ikm.tinkar.common.id.PublicId;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

public class PublicIdAttribute extends AttributeAreaBlueprint<StackPane, PublicId>
        implements KlPublicIdAttributeArea<StackPane> {

    public static class Factory implements KlPublicIdFieldAreaFactory<StackPane> {

        public String klGadgetName() {
            return "Identicon field";
        }

        @Override
        public PublicIdAttribute create(KlPreferencesFactory preferencesFactory) {
            return new PublicIdAttribute(preferencesFactory, this);
        }

        @Override
        public PublicIdAttribute create(KlPreferencesFactory preferencesFactory, GridLayout gridLayoutForArea) {
            PublicIdAttribute publicIdField = create(preferencesFactory);
            publicIdField.setGridLayout(gridLayoutForArea);
            return publicIdField;
        }

        @Override
        public PublicIdAttribute create(KlPreferencesFactory preferencesFactory,
                                        LayoutComputer layoutComputer) {
            PublicIdAttribute publicIdField = new PublicIdAttribute(preferencesFactory, this);
            DirectSingularAttributeLocator attributeLocator = AttributeLocator.direct.singular(AttributeCategory.PUBLIC_ID_FIELD);
            ImmutableList<KlDynamicAreaFactory> layoutList = layoutComputer.create(
                    Lists.immutable.of(attributeLocator));
            publicIdField.setGridLayout(layoutList.getOnly().gridLayout());
            return publicIdField;
        }

        @Override
        public KlPublicIdAttributeArea<StackPane> restore(KometPreferences preferences) {
            return new PublicIdAttribute(preferences);
        }

        @Override
        public Class<? extends KlPublicIdAttributeArea<StackPane>> klImplementationClass() {
            return PublicIdAttribute.class;
        }
    }

    public PublicIdAttribute(KometPreferences preferences) {
        super(preferences, new StackPane());
        setup();
    }

    public PublicIdAttribute(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new StackPane());
        setup();
    }

    private void setup() {
        fxObject.getChildren().forEach(node -> node.setOnContextMenuRequested(event ->
                LayoutContextMenu.makeContextMenu(this, KlPublicIdFieldAreaFactory.class).show(fxObject, event.getScreenX(), event.getScreenY())));
        fxObject.setOnContextMenuRequested(event ->
            LayoutContextMenu.makeContextMenu(this, KlPublicIdFieldAreaFactory.class).show(fxObject, event.getScreenX(), event.getScreenY()));
    }

    @Override
    protected void updateField() {
        fxObject().getChildren().clear();
        if (getField().value() != null) {
            PublicId publicId = getField().value();
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

    @Override
    public void subscribeToContext() {

    }
}
