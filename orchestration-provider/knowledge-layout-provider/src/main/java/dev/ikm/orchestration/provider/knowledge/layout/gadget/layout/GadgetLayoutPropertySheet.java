package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.komet.layout.GridLayout;
import dev.ikm.komet.layout.KlWidget;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.stage.WindowEvent;
import javafx.util.Subscription;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.Optional;

public class GadgetLayoutPropertySheet implements MapChangeListener {
    /*
        https://stackoverflow.com/questions/24238858/property-sheet-example-with-use-of-a-propertyeditor-controlsfx
     */

    SimpleIntegerProperty columnIndex = new SimpleIntegerProperty(this, "Column index", GridLayout.DEFAULT.columnIndex());
    SimpleIntegerProperty rowIndex = new SimpleIntegerProperty(this, "Row index", GridLayout.DEFAULT.rowIndex());
    SimpleIntegerProperty columnSpan = new SimpleIntegerProperty(this, "Column span", GridLayout.DEFAULT.columnSpan());
    SimpleIntegerProperty rowSpan = new SimpleIntegerProperty(this, "Row span", GridLayout.DEFAULT.rowSpan());
    SimpleObjectProperty<Priority> hGrow = new SimpleObjectProperty<>(this, "Horizontal grow", GridLayout.DEFAULT.hGrow());
    SimpleObjectProperty<Priority> vGrow = new SimpleObjectProperty<>(this, "Vertical grow", GridLayout.DEFAULT.vGrow());
    SimpleObjectProperty<HPos> hAlighment = new SimpleObjectProperty<>(this, "Horizontal alignment", GridLayout.DEFAULT.hAlignment());
    SimpleObjectProperty<VPos> vAlignment = new SimpleObjectProperty<>(this, "Vertical alignment", GridLayout.DEFAULT.vAlignment());

    SimpleDoubleProperty marginTop = new SimpleDoubleProperty(this, "Margin top", GridLayout.DEFAULT.margin().getTop());
    SimpleDoubleProperty marginRight = new SimpleDoubleProperty(this, "Margin right", GridLayout.DEFAULT.margin().getRight());
    SimpleDoubleProperty marginBottom = new SimpleDoubleProperty(this, "Margin bottom", GridLayout.DEFAULT.margin().getBottom());
    SimpleDoubleProperty marginLeft = new SimpleDoubleProperty(this, "Margin left", GridLayout.DEFAULT.margin().getLeft());

    SimpleDoubleProperty maxHeight = new SimpleDoubleProperty(this, "Max height", GridLayout.DEFAULT.maxHeight());
    SimpleDoubleProperty maxWidth = new SimpleDoubleProperty(this, "Max width", GridLayout.DEFAULT.maxWidth());
    SimpleDoubleProperty preferredHeight = new SimpleDoubleProperty(this, "Preferred height", GridLayout.DEFAULT.preferredHeight());
    SimpleDoubleProperty preferredWidth = new SimpleDoubleProperty(this, "Preferred width", GridLayout.DEFAULT.preferredWidth());
    SimpleBooleanProperty fillHeight = new SimpleBooleanProperty(this, "Fill height", GridLayout.DEFAULT.fillHeight());
    SimpleBooleanProperty fillWidth = new SimpleBooleanProperty(this, "Fill width", GridLayout.DEFAULT.fillWidth());
    final PropertySheet propertySheet = new PropertySheet();
    final KlWidget klWidget;

    /**
     * Note that if you don't declare a listener as final in this way, and just use method references, or
     * a direct lambda expression, you will not be able to remove the listener, since each method reference will create
     * a new object, and they won't compare equal using object identity.
     * https://stackoverflow.com/questions/42146360/how-do-i-remove-lambda-expressions-method-handles-that-are-used-as-listeners
     */
    private final MapChangeListener<Object, Object> mapChangeListener = this::onChanged;

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
            new PropertyWrapper(marginTop),
            new PropertyWrapper(marginRight),
            new PropertyWrapper(marginBottom),
            new PropertyWrapper(marginLeft),
            new PropertyWrapper(maxHeight),
            new PropertyWrapper(maxWidth),
            new PropertyWrapper(preferredHeight),
            new PropertyWrapper(preferredWidth),
            new PropertyWrapper(fillHeight),
            new PropertyWrapper(fillWidth)
    };

    public GridLayout layoutRecord() {
        return new GridLayout(
                columnIndex.intValue(),
                rowIndex.intValue(),
                columnSpan.intValue(),
                rowSpan.intValue(),
                hGrow.get(),
                vGrow.get(),
                hAlighment.get(),
                vAlignment.get(),
                new Insets(marginTop.get(), marginRight.get(), marginBottom.get(), marginLeft.get()),
                maxHeight.get(),
                maxWidth.get(),
                preferredHeight.get(),
                preferredWidth.get(),
                fillHeight.get(),
                fillWidth.get()
        );
    }


    public GadgetLayoutPropertySheet(KlWidget<? extends Node> klWidget) {
        this.klWidget = klWidget;
        propertySheet.getItems().addAll(items);
        propertySheet.setMode(PropertySheet.Mode.NAME);
        propertySheet.setSearchBoxVisible(false);
        propertySheet.setModeSwitcherVisible(false);
        // Set initial values
        columnIndex.setValue(klWidget.getColumnIndex());
        rowIndex.setValue(klWidget.getRowIndex());
        columnSpan.setValue(klWidget.getColspan());
        rowSpan.setValue(klWidget.getRowspan());
        hGrow.setValue(klWidget.getHgrow());
        vGrow.setValue(klWidget.getVgrow());
        hAlighment.setValue(klWidget.getHalignment());
        vAlignment.setValue(klWidget.getValignment());
        marginTop.setValue(klWidget.getMargins().getTop());
        marginRight.setValue(klWidget.getMargins().getRight());
        marginBottom.setValue(klWidget.getMargins().getBottom());
        marginLeft.setValue(klWidget.getMargins().getLeft());
        maxHeight.setValue(klWidget.getMaxHeight());
        maxWidth.setValue(klWidget.getMaxWidth());
        preferredHeight.setValue(klWidget.getPrefHeight());
        preferredWidth.setValue(klWidget.getPrefWidth());
        fillHeight.setValue(klWidget.getFillHeight());
        fillWidth.setValue(klWidget.getFillWidth());
        // subscribe klWidget to GadgetLayoutPropertySheet
        subscription
                .and(columnIndex.subscribe(newValue -> klWidget.setColumnIndex(newValue.intValue())))
                .and(rowIndex.subscribe(newValue -> klWidget.setRowIndex(newValue.intValue())))
                .and(columnSpan.subscribe(newValue -> klWidget.setColspan(newValue.intValue())))
                .and(rowSpan.subscribe(newValue -> klWidget.setRowspan(newValue.intValue())))
                .and(hGrow.subscribe(newValue -> klWidget.setHgrow(newValue)))
                .and(vGrow.subscribe(newValue -> klWidget.setVgrow(newValue)))
                .and(hAlighment.subscribe(newValue -> klWidget.setHalignment(newValue)))
                .and(vAlignment.subscribe(newValue -> klWidget.setValignment(newValue)))
                .and(marginTop.subscribe(newValue -> klWidget.setMargins(new Insets(newValue.doubleValue(), marginRight.get(), marginBottom.get(), marginLeft.get()))))
                .and(marginRight.subscribe(newValue -> klWidget.setMargins(new Insets(marginTop.get(), newValue.doubleValue(), marginBottom.get(), marginLeft.get()))))
                .and(marginBottom.subscribe(newValue -> klWidget.setMargins(new Insets(marginTop.get(), marginRight.get(), newValue.doubleValue(), marginLeft.get()))))
                .and(marginLeft.subscribe(newValue -> klWidget.setMargins(new Insets(marginTop.get(), marginRight.get(), marginBottom.get(), newValue.doubleValue()))))
                .and(maxHeight.subscribe(newValue -> klWidget.setMaxHeight(newValue.doubleValue())))
                .and(maxWidth.subscribe(newValue -> klWidget.setMaxWidth(newValue.doubleValue())))
                .and(preferredHeight.subscribe(newValue -> klWidget.setPrefHeight(newValue.doubleValue())))
                .and(preferredWidth.subscribe(newValue -> klWidget.setPrefWidth(newValue.doubleValue())))
                .and(fillHeight.subscribe(newValue -> klWidget.setFillHeight(newValue)))
                .and(fillWidth.subscribe(newValue -> klWidget.setFillWidth(newValue)));

        // Subscribe GadgetLayoutPropertySheet to klWidget

        klWidget.maxHeightPropertyOptional().ifPresent(maxHeightProperty ->
                subscription.and(maxHeightProperty.subscribe(newValue -> maxHeightProperty.set(newValue.doubleValue()))));
        klWidget.maxWidthPropertyOptional().ifPresent(maxWidthProperty ->
                subscription.and(maxWidthProperty.subscribe(newValue -> maxWidthProperty.set(newValue.doubleValue()))));
        klWidget.prefHeightPropertyOptional().ifPresent(preferredHeightProperty ->
                subscription.and(preferredHeightProperty.subscribe(newValue -> preferredHeightProperty.set(newValue.doubleValue()))));
        klWidget.prefWidthPropertyOptional().ifPresent(preferredWidthProperty ->
                subscription.and(preferredWidthProperty.subscribe(newValue -> preferredWidthProperty.set(newValue.doubleValue()))));

        //NOTE: using a listener for the Observable map instead of just an invalidation listener...
        klWidget.properties().addListener(mapChangeListener);
        switch (klWidget) {
            case Node node -> node.parentProperty().subscribe(this::parentChanged);
            default -> {}
        }
        klWidget.klWidget().getScene().getWindow().setOnCloseRequest(this::onCloseRequest);

    }

    /**
     * Handles the close request event for the window associated with the klWidget.
     * This method ensures that resources and listeners are properly unsubscribed
     * to avoid memory leaks or unintended behavior after the window is closed.
     *
     * @param windowEvent the {@code WindowEvent} triggered when a close request occurs
     */
    private void onCloseRequest(WindowEvent windowEvent) {
        unsubscribe();
    }


    /**
     * Handles changes to the parent property of a Node. This method is invoked when the parent
     * of the associated {@code klWidget} changes. If the Node is detached from its parent (oldParent
     * is not null and newParent is null), any associated resources or listeners are unsubscribed
     * to prevent memory leaks or unintended behavior.
     *
     * @param oldParent the previous parent of the Node, may be null if the Node had no parent before
     * @param newParent the new parent of the Node, may be null if the Node has been detached from its parent
     */
    private void parentChanged(Parent oldParent, Parent newParent) {
        if (oldParent != null && newParent == null) {
            this.unsubscribe();
        }
    }

    /**
     * Unsubscribes and removes listeners associated with the klWidget.
     * This method is used to clean up resources and event listeners to avoid memory leaks
     * or unintended behavior when the klWidget is no longer needed or its lifecycle ends.
     *
     * Specifically:
     * - Removes the mapChangeListener from the klWidget's properties.
     * - Unsubscribes the existing subscription, stopping any further events or updates.
     */
    private void unsubscribe() {
        klWidget.properties().removeListener(mapChangeListener);
        subscription.unsubscribe();
    }

    /**
     * Handles changes to the map of properties and updates specific layout-related attributes
     * based on the detected changes. This method listens for added properties in the map
     * and performs corresponding updates to the property values.
     *
     * @param change An instance of {@link MapChangeListener.Change} that represents a change
     *               in the map of properties. This object contains details about the key-value
     *               pair that was added, removed, or modified.
     */
    @Override
    public void onChanged(Change change) {
        if (change.wasAdded()) {
            Object key = change.getKey();
            Object value = change.getValueAdded();
            if (key instanceof String && value instanceof Object) {
                String propertyName = (String) key;
                Object propertyValue = value;
                switch (propertyName) {
                    case "gridpane-column" -> columnIndex.setValue((Integer) propertyValue);
                    case "gridpane-row" -> rowIndex.setValue((Integer) propertyValue);
                    case "gridpane-column-span" -> columnSpan.setValue((Integer) propertyValue);
                    case "gridpane-row-span" -> rowSpan.setValue((Integer) propertyValue);
                    case "gridpane-halignment" -> hAlighment.setValue((HPos) propertyValue);
                    case "gridpane-valignment" -> vAlignment.setValue((VPos) propertyValue);
                    case "gridpane-margin" -> {
                        marginTop.setValue(((Insets) propertyValue).getTop());
                        marginRight.setValue(((Insets) propertyValue).getRight());
                        marginBottom.setValue(((Insets) propertyValue).getBottom());
                        marginLeft.setValue(((Insets) propertyValue).getLeft());
                    }
                    case "gridpane-hgrow" -> hGrow.setValue((Priority) propertyValue);
                    case "gridpane-vgrow" -> vGrow.setValue((Priority) propertyValue);
                    case "gridpane-fill-height" ->  fillHeight.setValue((Boolean) propertyValue);
                    case "gridpane-fill-width" ->  fillWidth.setValue((Boolean) propertyValue);
                }
            }
        }
    }

    /**
     * Retrieves the associated {@code PropertySheet} instance used for configuring
     * and managing layout properties in a grid-based system.
     *
     * @return the {@code PropertySheet} associated with this object, providing
     *         access to layout property configuration and customization.
     */
    public PropertySheet getPropertySheet() {
        return propertySheet;
    }

    /**
     * A wrapper class for a {@link Property}, implementing the
     * {@link PropertySheet.Item} interface to enable integration
     * with the ControlsFX PropertySheet. The wrapper allows custom handling and representation
     * of properties within a property sheet.
     *
     * This class provides functionalities for accessing property metadata such as name,
     * value, description, category, type, and editability. Additionally, it supports
     * observation of the property's value and custom handling for property editors.
     */
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
                case StringProperty _ -> String.class;
                case IntegerProperty _ -> Integer.class;
                case DoubleProperty _ -> Double.class;
                case BooleanProperty _ -> Boolean.class;
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
