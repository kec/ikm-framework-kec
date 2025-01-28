import dev.ikm.komet.layout.window.KlWindowFactory;
import dev.ikm.orchestration.interfaces.menu.MenuService;
import dev.ikm.orchestration.interfaces.menu.WindowMenuService;
import dev.ikm.orchestration.interfaces.window.WindowCreateProvider;
import dev.ikm.orchestration.provider.knowledge.layout.NewWindowMenuProvider;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.SimpleWindowFactory;

module dev.ikm.orchestration.provider.knowledge.layout {
    requires dev.ikm.komet.layout;
    requires dev.ikm.komet.preferences;
    requires dev.ikm.orchestration.interfaces;
    requires org.controlsfx.controls;
    requires dev.ikm.tinkar.common;
    requires dev.ikm.jpms.eclipse.collections.api;
    requires org.slf4j;
    requires dev.ikm.tinkar.coordinate;
    requires dev.ikm.jpms.eclipse.collections;
    requires dev.ikm.tinkar.entity;

    opens dev.ikm.orchestration.provider.knowledge.layout to javafx.fxml, javafx.graphics;
    opens dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint to javafx.fxml, javafx.graphics;
    opens dev.ikm.orchestration.provider.knowledge.layout.gadget.simple to javafx.fxml, javafx.graphics;

    provides KlWindowFactory with SimpleWindowFactory;

    provides WindowCreateProvider with NewWindowMenuProvider;

}