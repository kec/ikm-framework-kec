package dev.ikm.orchestration.provider.knowledge.layout.field.simple;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.component.field.KlGenericFieldPane;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.field.blueprint.FieldPaneBlueprint;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.GadgetLayoutPropertySheet;
import dev.ikm.tinkar.common.util.time.DateTimeUtil;
import dev.ikm.tinkar.terms.EntityFacade;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static javafx.stage.StageStyle.UTILITY;

public class SimpleGenericFieldPane extends FieldPaneBlueprint<ToolBar, Object> implements
        KlGenericFieldPane<ToolBar> {

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
        setup();
    }

    public SimpleGenericFieldPane(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new ToolBar());
        setup();
    }

    private void setup() {
        fieldMeaning.setContextMenu(makeContextMenu());
        fieldValue.setContextMenu(makeContextMenu());
        fxGadget.setContextMenu(makeContextMenu());
    }

    private ContextMenu makeContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editGridLayout = new MenuItem("Edit grid layout");
        editGridLayout.setOnAction(event -> {
            GadgetLayoutPropertySheet gadgetLayoutPropertySheet = new GadgetLayoutPropertySheet(this);
            Stage stage = new Stage(UTILITY);
            stage.setTitle("Edit grid layout for: " + context().viewCoordinate().getDescriptionTextOrNid(getField().meaningNid()));
            Scene scene = new Scene(new VBox(gadgetLayoutPropertySheet.getPropertySheet()));
            stage.setScene(scene);
            stage.show();
        });
        contextMenu.getItems().add(editGridLayout);
        return contextMenu;
    }

    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }

    @Override
    public void subscribeToContext() {
        contextSubscriptionReference.get().and(context().viewCoordinate().subscribe(this::updateField));
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
