package dev.ikm.orchestration.provider.knowledge.layout.field.simple;

import dev.ikm.komet.layout.GridLayout;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.KlObject;
import dev.ikm.komet.layout.component.field.KlGenericFieldArea;
import dev.ikm.komet.layout.component.field.KlGenericFieldAreaFactory;
import dev.ikm.komet.layout.preferences.KlPreferenceFactoryProvider;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.field.blueprint.FieldAreaBlueprint;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.GadgetLayoutPropertySheet;
import dev.ikm.tinkar.common.service.PluggableService;
import dev.ikm.tinkar.common.util.time.DateTimeUtil;
import dev.ikm.tinkar.terms.EntityFacade;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ServiceLoader;
import java.util.prefs.BackingStoreException;

import static javafx.stage.StageStyle.UTILITY;

public class GenericField extends FieldAreaBlueprint<ToolBar, Object> implements
        KlGenericFieldArea<ToolBar> {

    public static class GenericFactory implements KlGenericFieldAreaFactory {

        @Override
        public String klGadgetName() {
            return "Generic field";
        }

        @Override
        public GenericField create(KlPreferencesFactory preferencesFactory) {
            return new GenericField(preferencesFactory, this);
        }

        @Override
        public GenericField restore(KometPreferences preferences) {
            return new GenericField(preferences);
        }

        @Override
        public Class<GenericField> klImplementationClass() {
            return GenericField.class;
        }
    }


    public static class BlueFactory implements KlGenericFieldAreaFactory {

        @Override
        public String klGadgetName() {
            return "Generic field with light blue background";
        }

        @Override
        public GenericField create(KlPreferencesFactory preferencesFactory) {
             GenericField pane = new GenericField(preferencesFactory, this);
             pane.fxGadget.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE,
                    CornerRadii.EMPTY, Insets.EMPTY)));
            return pane;
        }

        @Override
        public GenericField restore(KometPreferences preferences) {
            return new GenericField(preferences);
        }

        @Override
        public Class<GenericField> klImplementationClass() {
            return GenericField.class;
        }
    }

    Label fieldMeaning = new Label( "Field Meaning");
    Label fieldValue = new Label( "Field Value");
    Tooltip fieldPurpose = new Tooltip();
    {
        fieldPurpose.setText("Field Purpose");
        fieldMeaning.setTooltip(fieldPurpose);
        fieldValue.setTooltip(fieldPurpose);
        ((ToolBar) fxGadget).getItems().addAll(fieldMeaning, fieldValue);
    }


    public GenericField(KometPreferences preferences) {
        super(preferences, new ToolBar());
        setup();
    }

    public GenericField(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
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
        Menu changeFieldFactory = new Menu("Change field factory");
        ServiceLoader<KlGenericFieldAreaFactory> pluggableServices = PluggableService.load(KlGenericFieldAreaFactory.class);
        for (KlGenericFieldAreaFactory factory : pluggableServices) {
            MenuItem changeFieldFactoryItem = new MenuItem(factory.klGadgetName());
            changeFieldFactoryItem.setOnAction(event -> {
                LOG.info("Change field factory: " + factory.klGadgetName());

                try {
                    KometPreferences parentPreferences = this.preferences().parent();
                    KlPreferenceFactoryProvider.PreferenceFactoryWithParentPreferences prefFactory =
                            new KlPreferenceFactoryProvider.PreferenceFactoryWithParentPreferences(parentPreferences, factory.klImplementationClass());
                    KlGenericFieldArea newFieldArea = switch (factory.create(prefFactory)) {
                        case KlGenericFieldArea klGenericFieldArea -> klGenericFieldArea;
                        case KlObject klObject -> throw new IllegalStateException("Unexpected value: " + klObject);
                    };
                    GridLayout gridLayout = this.getGridLayout();
                    newFieldArea.setGridLayout(gridLayout);
                    newFieldArea.setField(getField());

                    Parent parent = fxGadget().getParent();
                    switch (parent) {
                        case Pane pane -> {
                            pane.getChildren().remove(fxGadget());
                            pane.getChildren().add(newFieldArea.fxGadget());
                            newFieldArea.subscribeToContext();
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + parent);
                    }
                    this.preferences().removeNode();
                    this.preferences().flush();
                } catch (BackingStoreException e) {
                    throw new RuntimeException(e);
                }
            });
            changeFieldFactory.getItems().add(changeFieldFactoryItem);
        }

        contextMenu.getItems().addAll(editGridLayout, changeFieldFactory);
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
