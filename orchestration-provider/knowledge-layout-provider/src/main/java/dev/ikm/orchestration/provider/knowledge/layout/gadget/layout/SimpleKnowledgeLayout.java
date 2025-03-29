package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.komet.framework.observable.AttributeLocator;
import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableField;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KnowledgeLayout;
import dev.ikm.komet.layout.area.KlArea;
import dev.ikm.komet.layout.area.factory.KlAttributeAreaFactory;
import dev.ikm.komet.layout.area.factory.KlDynamicAreaFactory;
import dev.ikm.komet.layout.attribute.KlAttributeArea;
import dev.ikm.komet.layout.component.KlGenericComponentArea;
import dev.ikm.orchestration.provider.knowledge.layout.DefaultLayoutFactory;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;

public class SimpleKnowledgeLayout implements KnowledgeLayout {

    final DefaultLayoutFactory layoutFactory = new DefaultLayoutFactory();

    final ObjectProperty<ObservableEntity<ObservableVersion<EntityVersion>>> componentProperty;

    final GridPane rootGrid = new GridPane();
    final KlGenericComponentArea<BorderPane> genericComponentArea;

    public SimpleKnowledgeLayout(KlGenericComponentArea<BorderPane> genericComponentArea) {
        this.componentProperty = genericComponentArea.componentProperty();
        this.componentProperty.addListener(this::componentChanged);
        this.genericComponentArea = genericComponentArea;
        genericComponentArea.fxObject().setCenter(rootGrid);
    }

    private void componentChanged(ObservableValue<? extends ObservableEntity<ObservableVersion<EntityVersion>>> observableValue,
                                  ObservableEntity<ObservableVersion<EntityVersion>> oldValue,
                                  ObservableEntity<ObservableVersion<EntityVersion>> newValue) {
        rootGrid.getChildren().clear();
        if (newValue != null) {
            ImmutableMap<AttributeLocator, ObservableField> componentFields = newValue.getObservableAttributes();
            ImmutableList<KlDynamicAreaFactory> componentLayout = layoutFactory.create(componentFields.keysView().toImmutableSortedList());
            componentLayout.forEach(areaSpecifier -> {
                KlArea klArea = areaSpecifier.makeAreaInParentAndAddToGrid(rootGrid, genericComponentArea.preferences());
                if (klArea instanceof KlAttributeArea fieldArea &&
                        areaSpecifier instanceof KlAttributeAreaFactory fieldAreaSpecifierForArea) {
                    fieldArea.setAttribute(componentFields.get(fieldAreaSpecifierForArea.attributeLocator()));
                }
            });
        }
    }

    @Override
    public KlArea rootArea() {
        return genericComponentArea;
    }
}
