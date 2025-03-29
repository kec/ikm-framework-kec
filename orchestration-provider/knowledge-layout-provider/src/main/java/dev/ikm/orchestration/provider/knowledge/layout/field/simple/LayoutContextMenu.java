package dev.ikm.orchestration.provider.knowledge.layout.field.simple;

import dev.ikm.komet.layout.KlObject;
import dev.ikm.komet.layout.area.GridLayout;
import dev.ikm.komet.layout.attribute.KlAttributeArea;
import dev.ikm.komet.layout.attribute.factory.KlFieldAreaFactory;
import dev.ikm.komet.layout.attribute.KlGenericAttributeArea;
import dev.ikm.komet.layout.preferences.KlPreferenceFactoryProvider;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.GadgetLayoutPropertySheet;
import dev.ikm.tinkar.common.service.PluggableService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;
import java.util.prefs.BackingStoreException;

import static javafx.stage.StageStyle.UTILITY;

public class LayoutContextMenu {

    private static final Logger LOG = LoggerFactory.getLogger(LayoutContextMenu.class);


    public static ContextMenu makeContextMenu(KlAttributeArea klFieldArea, Class<? extends KlFieldAreaFactory> factoryClass) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editGridLayout = new MenuItem("Edit grid layout");
        editGridLayout.setOnAction(event -> {
            GadgetLayoutPropertySheet gadgetLayoutPropertySheet = new GadgetLayoutPropertySheet(klFieldArea);
            Stage stage = new Stage(UTILITY);
            stage.setTitle("Edit grid layout for: " + klFieldArea.context().viewCoordinate().getDescriptionTextOrNid(klFieldArea.getAttribute().meaningNid()));
            Scene scene = new Scene(new VBox(gadgetLayoutPropertySheet.getPropertySheet()));
            stage.setScene(scene);
            stage.show();
        });
        Menu changeFieldFactory = new Menu("Change field factory");
        ServiceLoader<? extends KlFieldAreaFactory> pluggableServices = PluggableService.load(factoryClass);
        for (KlFieldAreaFactory factory : pluggableServices) {
            MenuItem changeFieldFactoryItem = new MenuItem(factory.klGadgetName());
            changeFieldFactoryItem.setOnAction(event -> {
                LOG.info("Change field factory: " + factory.klGadgetName());

                try {
                    KometPreferences parentPreferences = klFieldArea.preferences().parent();
                    KlPreferenceFactoryProvider.PreferenceFactoryWithParentPreferences prefFactory =
                            new KlPreferenceFactoryProvider.PreferenceFactoryWithParentPreferences(parentPreferences, factory.klImplementationClass());
                    KlGenericAttributeArea newFieldArea = switch (factory.create(prefFactory)) {
                        case KlGenericAttributeArea klGenericFieldArea -> klGenericFieldArea;
                        case KlObject klObject -> throw new IllegalStateException("Unexpected value: " + klObject);
                    };
                    GridLayout gridLayout = klFieldArea.getGridLayout();
                    newFieldArea.setGridLayout(gridLayout);
                    newFieldArea.setAttribute(klFieldArea.getAttribute());

                    Parent parent = klFieldArea.fxObject().getParent();
                    switch (parent) {
                        case Pane pane -> {
                            pane.getChildren().remove(klFieldArea.fxObject());
                            pane.getChildren().add(newFieldArea.fxGadget());
                            newFieldArea.subscribeToContext();
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + parent);
                    }
                    klFieldArea.preferences().removeNode();
                    klFieldArea.preferences().flush();
                } catch (BackingStoreException e) {
                    throw new RuntimeException(e);
                }
            });
            changeFieldFactory.getItems().add(changeFieldFactoryItem);
        }

        contextMenu.getItems().addAll(editGridLayout, changeFieldFactory);
        return contextMenu;
    }
}
