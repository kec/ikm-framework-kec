import dev.ikm.komet.layout.window.KlWindowFactory;
import dev.ikm.orchestration.provider.knowledge.layout.SimpleWindowFactory;

module dev.ikm.orchestration.provider.knowledge.layout {
    requires dev.ikm.jpms.eclipse.collections.api;
    requires dev.ikm.jpms.eclipse.collections;
    requires dev.ikm.komet.layout;
    requires dev.ikm.komet.preferences;
    requires dev.ikm.orchestration.interfaces;
    requires org.slf4j;
    requires org.controlsfx.controls;

    opens dev.ikm.orchestration.provider.knowledge.layout to javafx.fxml, javafx.graphics;

    provides KlWindowFactory with SimpleWindowFactory;
}