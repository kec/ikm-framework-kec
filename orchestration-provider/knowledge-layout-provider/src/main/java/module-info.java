
import dev.ikm.komet.layout.KlArea;
import dev.ikm.komet.layout.area.KlAreaForPublicId;
import dev.ikm.komet.layout.area.KlAreaForListOfVersions;
import dev.ikm.komet.layout.area.KlMultiVersionArea;
import dev.ikm.komet.layout.window.KlFxWindowFactory;
import dev.ikm.orchestration.interfaces.window.WindowCreateProvider;
import dev.ikm.orchestration.interfaces.window.WindowRestoreProvider;
import dev.ikm.orchestration.provider.knowledge.layout.feature.simple.GenericArea;
import dev.ikm.orchestration.provider.knowledge.layout.feature.simple.PublicIdArea;
import dev.ikm.orchestration.provider.knowledge.layout.window.WindowFactory;
import dev.ikm.orchestration.provider.knowledge.layout.menu.NewWindowMenuProvider;
import dev.ikm.orchestration.provider.knowledge.layout.menu.WindowRestoreMenuProvider;
import dev.ikm.orchestration.provider.knowledge.layout.version.MultiVersionArea;
import dev.ikm.orchestration.provider.knowledge.layout.version.SimpleVersionList;

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
    requires dev.ikm.tinkar.component;
    requires org.scenicview.scenicview;
    requires javafx.base;
    requires javafx.controls;

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
    opens dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;


    provides KlFxWindowFactory with WindowFactory;

   provides WindowCreateProvider with NewWindowMenuProvider;
    provides WindowRestoreProvider with WindowRestoreMenuProvider;
    provides KlArea.Factory with
             GenericArea.Factory,
             GenericArea.BlueFactory,
             SimpleVersionList.Factory,
             PublicIdArea.Factory,
             MultiVersionArea.Factory;
    provides KlAreaForListOfVersions.Factory with SimpleVersionList.Factory;
    provides KlAreaForPublicId.Factory with PublicIdArea.Factory;
    provides KlMultiVersionArea.Factory with MultiVersionArea.Factory;


}