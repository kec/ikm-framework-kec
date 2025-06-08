
import dev.ikm.komet.layout.feature.KlFieldAreaForObject;
import dev.ikm.komet.layout.feature.KlFieldAreaForPublicId;
import dev.ikm.komet.layout.feature.KlListOfVersionArea;
import dev.ikm.komet.layout.version.KlMultiVersionArea;
import dev.ikm.komet.layout.window.KlFxWindowFactory;
import dev.ikm.orchestration.interfaces.window.WindowCreateProvider;
import dev.ikm.orchestration.interfaces.window.WindowRestoreProvider;
import dev.ikm.orchestration.provider.knowledge.layout.feature.simple.GenericFieldArea;
import dev.ikm.orchestration.provider.knowledge.layout.feature.simple.PublicIdFieldArea;
import dev.ikm.orchestration.provider.knowledge.layout.window.WindowFactory;
import dev.ikm.orchestration.provider.knowledge.layout.menu.NewWindowMenuProvider;
import dev.ikm.orchestration.provider.knowledge.layout.menu.WindowRestoreMenuProvider;
import dev.ikm.orchestration.provider.knowledge.layout.version.MultiVersionArea;
import dev.ikm.orchestration.provider.knowledge.layout.version.SimpleVersionFeatureList;

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
    requires dev.ikm.komet.framework;
    requires dev.ikm.komet.identicon;
    requires javafx.graphics;

    opens dev.ikm.orchestration.provider.knowledge.layout.component;
    opens dev.ikm.orchestration.provider.knowledge.layout.feature.blueprint;
    opens dev.ikm.orchestration.provider.knowledge.layout.feature.simple;
    opens dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;
    opens dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;
    opens dev.ikm.orchestration.provider.knowledge.layout.menu;
    opens dev.ikm.orchestration.provider.knowledge.layout.version;
    opens dev.ikm.orchestration.provider.knowledge.layout.window;
    opens dev.ikm.orchestration.provider.knowledge.layout.area;
    opens dev.ikm.orchestration.provider.knowledge.layout.context;

    provides KlFxWindowFactory with WindowFactory;

   provides WindowCreateProvider with NewWindowMenuProvider;
    provides WindowRestoreProvider with WindowRestoreMenuProvider;
    provides KlFieldAreaForObject.Factory with
                GenericFieldArea.Factory,
                GenericFieldArea.BlueFactory;
    provides KlListOfVersionArea.Factory with SimpleVersionFeatureList.Factory;
    provides KlFieldAreaForPublicId.Factory with PublicIdFieldArea.Factory;
    provides KlMultiVersionArea.Factory with MultiVersionArea.Factory;


}