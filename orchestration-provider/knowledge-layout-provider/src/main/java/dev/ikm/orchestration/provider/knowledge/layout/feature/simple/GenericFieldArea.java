package dev.ikm.orchestration.provider.knowledge.layout.feature.simple;

import dev.ikm.komet.framework.observable.ObservableField;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.feature.KlFieldAreaForObject;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint.FieldAreaBlueprint;
import dev.ikm.orchestration.provider.knowledge.layout.version.SimpleVersionArea;
import dev.ikm.tinkar.common.util.time.DateTimeUtil;
import dev.ikm.tinkar.terms.EntityFacade;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public final class GenericFieldArea extends FieldAreaBlueprint<ToolBar, Object>
        implements KlFieldAreaForObject<ToolBar> {

    Label fieldMeaning = new Label( "Field Meaning");
    Label fieldValue = new Label( "Field Value");
    Tooltip fieldPurpose = new Tooltip();
    {
        fieldPurpose.setText("Field Purpose");
        fieldMeaning.setTooltip(fieldPurpose);
        fieldValue.setTooltip(fieldPurpose);
        fxObject().getItems().addAll(fieldMeaning, fieldValue);
    }

    public GenericFieldArea(KometPreferences preferences) {
        super(preferences, new ToolBar());
        setup();
    }

    public GenericFieldArea(KlPreferencesFactory preferencesFactory, KlArea.Factory areaFactory) {
        super(preferencesFactory, areaFactory, new ToolBar());
        setup();
    }

    private void setup() {
        fieldMeaning.setContextMenu(LayoutContextMenu.makeContextMenu(this));
        fieldValue.setContextMenu(LayoutContextMenu.makeContextMenu(this));
        fxObject().setContextMenu(LayoutContextMenu.makeContextMenu(this));
    }

    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }

    @Override
    public void contextChanged() {
        updatePropertyWrapper(getProperty(), getProperty());
    }

    protected void updatePropertyValue(ObservableField oldValue, ObservableField newValue) {
        if (newValue != null) {
            fieldMeaning.setText(context().viewCoordinate().getDescriptionTextOrNid(newValue.meaningNid()) + ": ");
            fieldPurpose.setText(context().viewCoordinate().getDescriptionTextOrNid(newValue.purposeNid()) + ": ");
            switch (newValue.value()) {
                case Long longValue -> fieldValue.setText(DateTimeUtil.format(longValue));
                case Number number -> fieldValue.setText(number.toString());
                case EntityFacade entity -> fieldValue.setText(context().viewCoordinate().getDescriptionTextOrNid(entity.nid()));
                case String string -> fieldValue.setText(string);
                case Object object -> fieldValue.setText(object.toString());
                case null -> fieldValue.setText("Null");
            }
        } else {
            fieldMeaning.setText("Null field, null meaning");
            fieldPurpose.setText("Null field, null purpose");
            fieldValue.setText("Null field, null value");
        }
    }

    public static GenericFieldArea.Factory factory() {
        return new GenericFieldArea.Factory();
    }

    public static GenericFieldArea restore(KometPreferences preferences) {
        return factory().restore(preferences);
    }


    public static class Factory implements KlFieldAreaForObject.Factory {

        @Override
        public String productName() {
            return "Standard Generic Field";
        }

        @Override
        public GenericFieldArea restore(KometPreferences preferences) {
            return new GenericFieldArea(preferences);
        }

        @Override
        public KlArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            GenericFieldArea area = new GenericFieldArea(preferencesFactory, this);
            area.setGridLayout(areaGridSettings);
            return area;
        }
    }

    public static GenericFieldArea restoreBlue(KometPreferences preferences) {
        return blueFactory().restore(preferences);
    }

    public static BlueFactory blueFactory() {
        return new BlueFactory();
    }
    public static class BlueFactory implements KlFieldAreaForObject.Factory {
        //TODO: This won't restore properly. Need a subclass with different constructor, or saving background color in preferences.
        //Maybe restore needs to restore via the factory...  And call the restore method on the factory, not the constructor!

        @Override
        public String productName() {
            return "Blue Generic Field";
        }

        public GenericFieldArea restore(KometPreferences preferences) {
            GenericFieldArea area = new GenericFieldArea(preferences);
            area.fxObject().setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE,
                    CornerRadii.EMPTY, Insets.EMPTY)));
            return area;
        }

        @Override
        public KlArea create(KlPreferencesFactory preferencesFactory, AreaGridSettings areaGridSettings) {
            GenericFieldArea area = new GenericFieldArea(preferencesFactory, this);
            area.setGridLayout(areaGridSettings);
            area.fxObject().setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE,
                    CornerRadii.EMPTY, Insets.EMPTY)));
            return area;
        }
    }
}
