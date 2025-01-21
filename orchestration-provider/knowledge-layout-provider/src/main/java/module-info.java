import dev.ikm.komet.layout.window.KlWindowFactory;
import dev.ikm.orchestration.provider.knowledge.layout.SimpleWindowFactory;

module dev.ikm.orchestration.provider.knowledge.layout {
    requires dev.ikm.komet.layout;
    requires dev.ikm.komet.preferences;
    requires dev.ikm.orchestration.interfaces;
    requires org.controlsfx.controls;
    requires dev.ikm.tinkar.common;
    requires dev.ikm.jpms.eclipse.collections.api;
    requires org.slf4j;
    requires dev.ikm.tinkar.coordinate;

    opens dev.ikm.orchestration.provider.knowledge.layout to javafx.fxml, javafx.graphics;
    opens dev.ikm.orchestration.provider.knowledge.layout.blueprint to javafx.fxml, javafx.graphics;

    provides KlWindowFactory with SimpleWindowFactory;
}