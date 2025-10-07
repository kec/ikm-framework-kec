package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.komet.framework.observable.Feature;
import dev.ikm.komet.framework.observable.ObservableEntity;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.*;
import dev.ikm.komet.layout.area.*;
import dev.ikm.komet.layout.component.KlChronologyArea;
import dev.ikm.komet.layout.area.KlMultiVersionArea;
import dev.ikm.komet.layout.area.KlAreaForVersion;
import dev.ikm.komet.layout.component.KlMultiComponentArea;
import dev.ikm.komet.layout.version.field.KlField;
import dev.ikm.komet.layout.window.KlRenderView;
import dev.ikm.tinkar.common.util.uuid.UuidT5Generator;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.beans.value.ObservableValue;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public class SimpleKnowledgeLayout implements KnowledgeLayout {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleKnowledgeLayout.class);
    final KlParent parent;
    // TODO: Persist the layoutOverrides
    final LayoutOverrides layoutOverrides;

    final LayoutKey.ForArea rootLayoutKey = LayoutKey.makeTopArea(SimpleKnowledgeLayout.class);

    public SimpleKnowledgeLayout(KlParent parent) {
        this.parent = parent;
        this.parent.setMasterLayout(this);
        this.layoutOverrides = parent.getLayoutOverrides();
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
        LOG.info("Layout component changed from {} to {}", oldValue, newValue);
        this.parent.gridPaneForChildren().getChildren().clear();
        final AtomicReference<KlAreaForListOfVersions> versionListAreaReference = new AtomicReference<>();
        final AtomicReference<KlMultiVersionArea> multiVersionAreaReference = new AtomicReference<>();

        if (newValue != null) {
            // 0. This is where a layout root key could be generated?
            LayoutKey.LayoutKeyRecord layoutKeyForLevel = new LayoutKey.LayoutKeyRecord(UuidT5Generator.get(this.getClass().getName()));

             // 1. Get the chronology's features.
            ImmutableList<? extends Feature> observableFeatures = newValue.getFeatures();

            RowIncrementLayoutComputer rowIncrementLayoutComputer = RowIncrementLayoutComputer.create(this.parent.getMasterLayout());

            LOG.info("Laying out: " + this.getClass().getSimpleName());
            ImmutableList<LayoutComputer.LayoutElement> componentLayout = rowIncrementLayoutComputer.layout(observableFeatures,
                    layoutKeyForLevel.makeAreaKeyProvider()
            );

            // 3. For each layout area, find the property associated with that component,
            componentLayout.forEach(layoutElement -> {
                // 3a. Add the layout area to the root grid. Layout overrides would go here.
                KlArea klArea = layoutElement.areaGridSettings().makeAndAddToParent(this.parent);
                klArea.setId(layoutElement);
            });
        }
    }
}
