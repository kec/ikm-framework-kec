package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.komet.layout.KlGadget;
import dev.ikm.komet.layout.KlWidget;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Subscription;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.Optional;

public class GadgetLayoutPropertySheet {
    /*
        https://stackoverflow.com/questions/24238858/property-sheet-example-with-use-of-a-propertyeditor-controlsfx
     */

    SimpleIntegerProperty columnIndex = new SimpleIntegerProperty(this, "Column index", 0);
    SimpleIntegerProperty rowIndex = new SimpleIntegerProperty(this, "Row index", 0);
    SimpleIntegerProperty columnSpan = new SimpleIntegerProperty(this, "Column span", 1);
    SimpleIntegerProperty rowSpan = new SimpleIntegerProperty(this, "Row span", 1);
    SimpleObjectProperty<Priority> hGrow = new SimpleObjectProperty<>(this, "Horizontal grow", Priority.NEVER);
    SimpleObjectProperty<Priority> vGrow = new SimpleObjectProperty<>(this, "Vertical grow", Priority.NEVER);
    SimpleObjectProperty<HPos> hAlighment = new SimpleObjectProperty<>(this, "Horizontal alignment", HPos.LEFT);
    SimpleObjectProperty<VPos> vAlignment = new SimpleObjectProperty<>(this, "Vertical alignment", VPos.TOP);
    SimpleObjectProperty<Insets> margin = new SimpleObjectProperty<>(this, "Margin", Insets.EMPTY);
    SimpleDoubleProperty maxHeight = new SimpleDoubleProperty(this, "Max height", Double.MAX_VALUE);
    SimpleDoubleProperty maxWidth = new SimpleDoubleProperty(this, "Max width", Double.MAX_VALUE);
    SimpleDoubleProperty preferredHeight = new SimpleDoubleProperty(this, "Preferred height", Region.USE_COMPUTED_SIZE);
    SimpleDoubleProperty preferredWidth = new SimpleDoubleProperty(this, "Preferred width", Region.USE_COMPUTED_SIZE);

    final PropertySheet propertySheet = new PropertySheet();
    final KlWidget klWidget;
    final Subscription subscription = Subscription.EMPTY;


    PropertySheet.Item[] items = {
            new PropertyWrapper(columnIndex),
            new PropertyWrapper(rowIndex),
            new PropertyWrapper(columnSpan),
            new PropertyWrapper(rowSpan),
            new PropertyWrapper(hGrow),
            new PropertyWrapper(vGrow),
            new PropertyWrapper(hAlighment),
            new PropertyWrapper(vAlignment),
            new PropertyWrapper(margin),
            new PropertyWrapper(maxHeight),
            new PropertyWrapper(maxWidth),
            new PropertyWrapper(preferredHeight),
            new PropertyWrapper(preferredWidth)
    };

    public LayoutRecord layoutRecord() {
        return new LayoutRecord(
                columnIndex.intValue(),
                rowIndex.intValue(),
                columnSpan.intValue(),
                rowSpan.intValue(),
                hGrow.get(),
                vGrow.get(),
                hAlighment.get(),
                vAlignment.get(),
                margin.get(),
                maxHeight.get(),
                maxWidth.get(),
                preferredHeight.get(),
                preferredWidth.get()
        );
    }


    public GadgetLayoutPropertySheet(KlWidget<?> klWidget) {
        this.klWidget = klWidget;
        propertySheet.getItems().addAll(items);
        propertySheet.setMode(PropertySheet.Mode.NAME);
        // Set initial values
        columnIndex.setValue(klWidget.getColumnIndex());
        rowIndex.setValue(klWidget.getRowIndex());
        columnSpan.setValue(klWidget.getColspan());
        rowSpan.setValue(klWidget.getRowspan());
        hGrow.setValue(klWidget.getHgrow());
        vGrow.setValue(klWidget.getVgrow());
        hAlighment.setValue(klWidget.getHalignment());
        vAlignment.setValue(klWidget.getValignment());
        margin.setValue(klWidget.getMargins());
        maxHeight.setValue(klWidget.getMaxHeight());
        maxWidth.setValue(klWidget.getMaxWidth());
        preferredHeight.setValue(klWidget.getPrefHeight());
        preferredWidth.setValue(klWidget.getPrefWidth());
        // Bind to widget
        subscription.and(
                columnIndex.subscribe(newValue -> klWidget.setColumnIndex(newValue.intValue()))
        ).and(
                rowIndex.subscribe(newValue -> klWidget.setRowIndex(newValue.intValue()))
        ).and(
                columnSpan.subscribe(newValue -> klWidget.setColspan(newValue.intValue()))
        ).and(
                rowSpan.subscribe(newValue -> klWidget.setRowspan(newValue.intValue()))
        ).and(
                hGrow.subscribe(newValue -> klWidget.setHgrow(newValue))
        ).and(
                vGrow.subscribe(newValue -> klWidget.setVgrow(newValue))
        ).and(
                hAlighment.subscribe(newValue -> klWidget.setHalignment(newValue))
        ).and(
                vAlignment.subscribe(newValue -> klWidget.setValignment(newValue))
        ).and(
                margin.subscribe(newValue -> klWidget.setMargins(newValue))
        ).and(
                maxHeight.subscribe(newValue -> klWidget.setMaxHeight(newValue.doubleValue()))
        ).and(
                maxWidth.subscribe(newValue -> klWidget.setMaxWidth(newValue.doubleValue()))
        ).and(
                preferredHeight.subscribe(newValue -> klWidget.setPrefHeight(newValue.doubleValue()))
        ).and(
                preferredWidth.subscribe(newValue -> klWidget.setPrefWidth(newValue.doubleValue()))
        );
    }

    public PropertySheet getPropertySheet() {
        return propertySheet;
    }

    class PropertyWrapper implements PropertySheet.Item {
        Property wrappedProperty;

        public PropertyWrapper(Property<?> wrappedProperty) {
            this.wrappedProperty = wrappedProperty;
        }

        public Property<?> getWrappedProperty() {
            return wrappedProperty;
        }

        @Override
        public String getName() {
            return wrappedProperty.getName();
        }

        @Override
        public Object getValue() {
            return wrappedProperty.getValue();
        }

        @Override
        public void setValue(Object value) {
            wrappedProperty.setValue(value);
        }

        @Override
        public String getDescription() {
            return getName();
        }

        @Override
        public String getCategory() {
            return "Layout";
        }

        @Override
        public boolean isEditable() {
            return true;
        }

        @Override
        public Optional<ObservableValue<? extends Object>> getObservableValue() {
            return Optional.ofNullable(wrappedProperty);
        }

        @Override
        public Class<?> getType() {
            return switch (wrappedProperty) {
                case StringProperty stringProperty -> String.class;
                case IntegerProperty integerProperty -> Integer.class;
                case DoubleProperty doubleProperty -> Double.class;
                case BooleanProperty booleanProperty -> Boolean.class;
                case ObjectProperty objectProperty when objectProperty.get() instanceof Insets -> Insets.class;
                case ObjectProperty objectProperty when objectProperty.get() instanceof Priority -> Priority.class;
                case ObjectProperty objectProperty when objectProperty.get() instanceof HPos -> HPos.class;
                case ObjectProperty objectProperty when objectProperty.get() instanceof VPos -> VPos.class;
                default -> Object.class;
            };
        }

        @Override
        public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
            return Optional.empty();
        }
    }
}
