package dev.ikm.orchestration.provider.knowledge.layout.field.simple;

import dev.ikm.komet.framework.observable.ObservableField;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.field.blueprint.FieldPaneBlueprint;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;

public class SimpleFieldPane extends FieldPaneBlueprint<ToolBar> {

    Label fieldMeaning = new Label( "Field Meaning");
    Label fieldValue = new Label( "Field Value");
    Tooltip fieldPurpose = new Tooltip();
    {
        fieldPurpose.setText("Field Purpose");
        fieldMeaning.setTooltip(fieldPurpose);
        fieldValue.setTooltip(fieldPurpose);
        fxGadget.getItems().addAll(fieldMeaning, fieldValue);
    }


    public SimpleFieldPane(KometPreferences preferences) {
        super(preferences, new ToolBar());
    }

    public SimpleFieldPane(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new ToolBar());
    }

    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }

    @Override
    public void unsubscribeFromContext() {

    }

    @Override
    public void subscribeToContext() {

    }

    @Override
    protected void updateField() {
        fieldMeaning.setText(context().viewCoordinate().getDescriptionTextOrNid(getField().meaningNid()));
        fieldPurpose.setText(context().viewCoordinate().getDescriptionTextOrNid(getField().purposeNid()));
        fieldValue.setText(getField().v);
        getField()
    }
}
