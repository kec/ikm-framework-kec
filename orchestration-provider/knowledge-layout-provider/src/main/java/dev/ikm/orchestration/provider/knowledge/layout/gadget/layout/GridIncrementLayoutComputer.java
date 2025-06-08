package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.komet.framework.observable.Feature;
import dev.ikm.komet.framework.observable.FeatureLocator;
import dev.ikm.komet.layout.KnowledgeLayout;
import dev.ikm.komet.layout.LayoutComputer;
import dev.ikm.komet.layout.LayoutKey;
import dev.ikm.komet.layout.area.*;
import dev.ikm.orchestration.provider.knowledge.layout.feature.simple.GenericFieldArea;
import dev.ikm.orchestration.provider.knowledge.layout.version.MultiVersionArea;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * GridIncrementLayoutComputer is an abstract class designed to compute and manage
 * grid-based layouts incrementally. It serves as a foundational class, providing
 * a structure for defining specific grid stepping behavior for layout management.
 * Implementing classes are expected to specify the stepping strategy by overriding
 * the abstract `step` method.
 * <p>
 * This class interacts with a designated KlWidget instance, tying its lifecycle to
 * the widget by setting itself as the layout computer during construction and clearing
 * the reference during closure.
 * <p>
 * Responsibilities of this class include:
 * <p> - Managing the computation of layout positions for a set of attribute locators.
 * <p> - Utilizing the provided `GridStepper` instance internally to increment and
 * <p>   decrement row and column positions based on specific grid stepping logic.
 * <p> - Handling different layout categories to determine the type of factories
 * (e.g., KlAttributeAreaFactory, KlSupplementalAreaFactory) required for creating
 * layout components.
 * <p>
 * Key Methods:
 * <p> - `create`: Accepts a list of attribute locators to generate the layout configuration
 * for the grid. It processes various layout categories and configures the appropriate
 * factories based on the category.
 * <p> - `step`: Abstract method to be implemented by subclasses to define the specific grid
 * stepping behavior (e.g., row-wise or column-wise increment).
 * <p>
 * Nested Class:
 * <p> - `Stepper`: A concrete implementation of the GridStepper interface to handle low-level
 * grid position operations. It tracks and manipulates the current row and column values
 * for grid layouts.
 */

public abstract class GridIncrementLayoutComputer implements LayoutComputer {
    private static final Logger LOG = LoggerFactory.getLogger(GridIncrementLayoutComputer.class);

    private final KnowledgeLayout masterLayout;
    public GridIncrementLayoutComputer(KnowledgeLayout masterLayout) {
        this.masterLayout = masterLayout;
    }

    @Override
    public KnowledgeLayout masterLayout() {
        return masterLayout;
    }

    protected abstract GridStep step();

    public ImmutableList<LayoutElement> layout(ImmutableList<Feature> features,
                                               LayoutKey.AreaKeyProvider areaKeyProvider) {
        MutableList<LayoutElement> layoutList = Lists.mutable.empty();
        LayoutKey.ForArea layoutKeyForForArea = areaKeyProvider.make(this);

        GridStepper stepper = new Stepper();

        features.forEach(feature -> {
            // The layout computer can decide the type of factory based on the feature, or
            // can simply default to a generic layout choice known to the layout computer, and
            // all refinement by the user.
            AreaGridSettings areaGridSettings = layoutOverrides().getOrDefault(
                    stepper.nextForFeature(layoutKeyForForArea, feature.locator(), GenericFieldArea.Factory.class.getName()));
            layoutList.add(new LayoutElement(areaGridSettings, Lists.immutable.of(feature)));

            if (feature.locator() instanceof FeatureLocator.ChronologyProperty.VersionList ||
                    feature.locator()  instanceof FeatureLocator.VersionProperty.Semantic.FieldList ||
                    feature.locator()  instanceof FeatureLocator.VersionProperty.Pattern.FieldDefinitionList) {
                // Add a filler below each list
                AreaGridSettings fillerAreaGridSettings = layoutOverrides().getOrDefault(stepper.nextForSupplemental(layoutKeyForForArea, MultiVersionArea.Factory.class.getName()));
                layoutList.add(new LayoutElement(fillerAreaGridSettings, Lists.immutable.empty()));
            }
        });

        return layoutList.toImmutable();
    }

    /**
     * The Stepper class provides an implementation of the GridStepper interface.
     * It maintains and manipulates a two-dimensional grid layout by tracking
     * the current row and column positions. The positions can be incremented,
     * decremented, reset or retrieved, while adhering to the GridStepper contract.
     */
    public static class Stepper implements GridStepper {
        final private AtomicInteger row = new AtomicInteger(0);
        final private AtomicInteger column = new AtomicInteger(0);
        private GridStep step = GridStep.ROW;

        @Override
        public void setStep(GridStep step) {
            this.step = step;
        }

        @Override
        public void reset() {
            row.set(0);
            column.set(0);
        }

        @Override
        public int row() {
            return row.get();
        }

        @Override
        public int column() {
            return column.get();
        }

        @Override
        public AreaGridSettings nextForFeature(LayoutKey.ForArea layoutKeyForArea, FeatureLocator propertyLocator, String factoryName) {
            increment();
            LayoutKey.Property layoutKeyForProperty = layoutKeyForArea.makePropertyLayoutKey(propertyLocator);
            return new AreaGridSettings(column(), row(), layoutKeyForProperty.forArea(), factoryName);
        }

        @Override
        public AreaGridSettings nextForSupplemental(LayoutKey.ForArea layoutKeyForArea, String factoryName) {
            increment();
            AreaGridSettings areaGridSettings = new AreaGridSettings(this, LayoutKey.EMPTY, factoryName);
            LayoutKey.Supplemental supplementalLayoutKey = layoutKeyForArea.makeSupplementalLayoutKey(areaGridSettings);
            return areaGridSettings.withLayoutKeyForArea(supplementalLayoutKey.forArea());
        }

        private void increment() {
            switch (step) {
                case ROW -> row.incrementAndGet();
                case COLUMN -> column.incrementAndGet();
                case ROW_AND_COLUMN -> {
                    row.incrementAndGet();
                    column.incrementAndGet();
                }
            }
        }
    }
}
