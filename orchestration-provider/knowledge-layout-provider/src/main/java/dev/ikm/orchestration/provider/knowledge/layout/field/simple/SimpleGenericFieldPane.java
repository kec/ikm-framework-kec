package dev.ikm.orchestration.provider.knowledge.layout.field.simple;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.component.field.KlGenericFieldPane;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.field.blueprint.FieldPaneBlueprint;
import dev.ikm.tinkar.common.util.time.DateTimeUtil;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.terms.EntityFacade;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;

public class SimpleGenericFieldPane extends FieldPaneBlueprint<Parent, Object> implements
        KlGenericFieldPane {

    Label fieldMeaning = new Label( "Field Meaning");
    Label fieldValue = new Label( "Field Value");
    Tooltip fieldPurpose = new Tooltip();
    {
        fieldPurpose.setText("Field Purpose");
        fieldMeaning.setTooltip(fieldPurpose);
        fieldValue.setTooltip(fieldPurpose);
        ((ToolBar) fxGadget).getItems().addAll(fieldMeaning, fieldValue);
    }


    public SimpleGenericFieldPane(KometPreferences preferences) {
        super(preferences, new ToolBar());
    }

    public SimpleGenericFieldPane(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
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
        context().viewCoordinate().subscribe(this::updateField);
    }

    @Override
    protected void updateField() {
        if (getField() != null) {
            fieldMeaning.setText(context().viewCoordinate().getDescriptionTextOrNid(getField().meaningNid()) + ": ");
            fieldPurpose.setText(context().viewCoordinate().getDescriptionTextOrNid(getField().purposeNid()) + ": ");
            switch (getField().value()) {
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
}
