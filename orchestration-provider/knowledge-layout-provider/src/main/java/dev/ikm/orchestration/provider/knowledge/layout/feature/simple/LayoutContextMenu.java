package dev.ikm.orchestration.provider.knowledge.layout.feature.simple;

import dev.ikm.komet.layout.KlObject;
import dev.ikm.komet.layout.KlParent;
import dev.ikm.komet.layout.area.AreaGridSettings;
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.feature.KlFieldArea;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.layout.AreaLayoutPropertySheet;
import dev.ikm.tinkar.common.service.PluggableService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;
import java.util.prefs.BackingStoreException;

import static javafx.stage.StageStyle.UTILITY;

public class LayoutContextMenu {

    private static final Logger LOG = LoggerFactory.getLogger(LayoutContextMenu.class);


    public static ContextMenu makeContextMenu(KlFieldArea klFieldArea) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editGridLayout = new MenuItem("Edit grid layout");
        editGridLayout.setOnAction(event -> {
            AreaLayoutPropertySheet areaLayoutPropertySheet = new AreaLayoutPropertySheet(klFieldArea);
            Stage stage = new Stage(UTILITY);
            if (klFieldArea.getField() != null) {
                stage.setTitle("Edit grid layout for: " +
                        klFieldArea.context().viewCoordinate().getDescriptionTextOrNid(klFieldArea.getField().meaningNid()));
            } else {
                stage.setTitle("Edit grid layout for: " +
                        klFieldArea.regularNames().getAny());
            }
            Scene scene = new Scene(new VBox(areaLayoutPropertySheet.getPropertySheet()));
            stage.setScene(scene);
            stage.show();
        });
        Menu changeFieldFactory = new Menu("Change field factory");

        KlArea.Factory<?, ?> areaFactory = klFieldArea.getAreaLayout().makeAreaFactory();
        ImmutableList<Class<?>> areaFactoryServiceTypes = areaFactory.areaFactoryServiceTypes();
        areaFactoryServiceTypes.forEach(factoryClass -> {
            ServiceLoader<? extends KlFieldArea.Factory> pluggableServices = (ServiceLoader<? extends KlFieldArea.Factory>) PluggableService.load(factoryClass);
            for (KlFieldArea.Factory factory : pluggableServices) {
                MenuItem changeFieldFactoryItem = new MenuItem(factory.factoryName());
                changeFieldFactoryItem.setOnAction(event -> {
                    LOG.info("Change field factory: " + factory.factoryName());
                    try {
                        KometPreferences parentPreferences = klFieldArea.preferences().parent();
                        AreaGridSettings areaGridSettingsWithDiscoveredFactory = klFieldArea.getAreaLayout().withAreaFactoryClassName(factory.getClass().getName());

                        KlObject klObject = KlObject.getKlPeer(klFieldArea.fxObject().getParent());
                        if (klObject instanceof KlParent<?> klParent) {
                            klParent.gridPaneForChildren().getChildren().remove(klFieldArea.fxObject());
                            KlFieldArea newFieldArea = (KlFieldArea) areaGridSettingsWithDiscoveredFactory.makeAndAddToParent(klParent);
                            newFieldArea.setProperty(klFieldArea.getProperty());
                            klFieldArea.unsubscribeFromContext();
                            newFieldArea.subscribeToContext();
                            klFieldArea.preferences().removeNode();
                            klFieldArea.preferences().flush();
                        } else {
                            throw new IllegalStateException("Parent of klFieldArea does not have a KlParent peer: " + klFieldArea);
                        }
                    } catch (BackingStoreException e) {
                        throw new RuntimeException(e);
                    }
                });
                changeFieldFactory.getItems().add(changeFieldFactoryItem);
            }
        });

        contextMenu.getItems().addAll(editGridLayout, changeFieldFactory);
        return contextMenu;
    }
}
