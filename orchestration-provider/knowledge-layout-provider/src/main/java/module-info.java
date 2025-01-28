import dev.ikm.komet.layout.window.KlWindowFactory;
import dev.ikm.orchestration.interfaces.window.WindowCreateProvider;
import dev.ikm.orchestration.interfaces.window.WindowRestoreProvider;
import dev.ikm.orchestration.provider.knowledge.layout.menu.NewWindowMenuProvider;
import dev.ikm.orchestration.provider.knowledge.layout.menu.WindowRestoreMenuProvider;
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

    opens dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint to javafx.fxml, javafx.graphics;
    opens dev.ikm.orchestration.provider.knowledge.layout.gadget.simple to javafx.fxml, javafx.graphics;
    opens dev.ikm.orchestration.provider.knowledge.layout.menu to javafx.fxml, javafx.graphics;

    provides KlWindowFactory with SimpleWindowFactory;

    provides WindowCreateProvider with NewWindowMenuProvider;
    provides WindowRestoreProvider with WindowRestoreMenuProvider;
}