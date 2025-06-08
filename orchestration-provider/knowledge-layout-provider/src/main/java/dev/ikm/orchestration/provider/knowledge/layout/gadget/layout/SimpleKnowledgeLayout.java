package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.komet.framework.observable.Feature;
import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.*;
import dev.ikm.komet.layout.area.*;
import dev.ikm.komet.layout.feature.*;
import dev.ikm.komet.layout.component.KlChronologyArea;
import dev.ikm.komet.layout.version.KlMultiVersionArea;
import dev.ikm.komet.layout.version.KlVersionArea;
import dev.ikm.komet.layout.window.KlRenderView;
import dev.ikm.tinkar.common.util.uuid.UuidT5Generator;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.beans.value.ObservableValue;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.concurrent.atomic.AtomicReference;

public class SimpleKnowledgeLayout implements KnowledgeLayout {
    final KlParent parent;
    // TODO: Persist the layoutOverrides
    final LayoutOverrides layoutOverrides;

    final LayoutKey.ForArea rootLayoutKey = LayoutKey.makeTopArea(SimpleKnowledgeLayout.class);

    public SimpleKnowledgeLayout(KlParent parent) {
        this.parent = parent;
        this.parent.setMasterLayout(this);
        this.layoutOverrides = LayoutOverrides.make(UuidT5Generator.get(parent.getClass().getName()), SimpleKnowledgeLayout.class);
    }

    @Override
    public void save() {
        this.layoutOverrides.save();
    }

    @Override
    public LayoutOverrides layoutOverrides() {
        return layoutOverrides;
    }

    @Override
    public LayoutKey.ForArea rootLayoutKey() {
        return rootLayoutKey;
    }

    private void componentChanged(ObservableValue<? extends ObservableEntity<ObservableVersion<EntityVersion>>> observableValue,
                                  ObservableEntity<ObservableVersion<EntityVersion>> oldValue,
                                  ObservableEntity<ObservableVersion<EntityVersion>> newValue) {
        this.parent.gridPaneForChildren().getChildren().clear();
        final AtomicReference<KlListOfVersionArea> versionListAreaReference = new AtomicReference<>();
        final AtomicReference<KlMultiVersionArea> multiVersionAreaReference = new AtomicReference<>();

        if (newValue != null) {
            // 0. This is where a layout root key could be generated?
            LayoutKey.LayoutKeyRecord layoutKeyForLevel = new LayoutKey.LayoutKeyRecord(UuidT5Generator.get(this.getClass().getName()));

             // 1. Get the chronology's features.
            ImmutableList<Feature> observableFeatures = newValue.getFeatures(parent.calculatorForContext());

            RowIncrementLayoutComputer rowIncrementLayoutComputer = RowIncrementLayoutComputer.create(this.parent.getMasterLayout());

            ImmutableList<LayoutComputer.LayoutElement> componentLayout = rowIncrementLayoutComputer.layout(observableFeatures,
                    layoutKeyForLevel.makeAreaKeyProvider()
            );

            // 3. For each layout area, find the property associated with that component,
            componentLayout.forEach(layoutElement -> {
                // 3a. Add the layout area to the root grid. Layout overrides would go here.
                KlView klView = layoutElement.areaGridSettings().makeAndAddToParent(this.parent);
                // 3b. Find the property needed by the layout area, and subscribe the layout area to the property.
                // TODO: this switch may not be neecessary if areas bind the properties themselves.
                switch (klView) {
                    case KlMultiVersionArea multiVersionArea -> {}
                    case KlFieldDefinitionArea fieldDefinitionArea -> {}
                    case KlFieldArea fieldArea -> {}
                    case KlListOfVersionArea versionList -> {}
                    case KlListOfFieldDefinitionArea listOfFieldDefinitionArea -> {}
                    case KlListOfFieldArea listOfFieldArea -> {}
                    case KlVersionArea versionArea -> {}
                    case KlSupplementalArea klSupplementalArea -> {}
                    case KlChronologyArea klChronologyArea -> {}
                    case KlAssociationArea klAssociationArea -> {}
                    case KlPropertyArea klPropertyArea -> {}
                    case KlTopView klTopView -> {}
                    case KlWidget klWidget -> {}
                    case KlGenericArea klGenericArea -> {}
                    case KlRenderView klRenderView -> {}
                    case KlParent klParent -> {}
                }
            });
            // This is a late binding... Is there an alternative?
            if (versionListAreaReference.get() != null) {
                if (multiVersionAreaReference.get() != null) {
                    multiVersionAreaReference.get().setVersionAndSelectionProperty(versionListAreaReference.get().versionsAndSelectionProperty());
                }
            }
        }
    }
}
