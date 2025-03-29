
import dev.ikm.komet.layout.attribute.factory.KlGenericFieldAreaFactory;
import dev.ikm.komet.layout.attribute.factory.KlPublicIdFieldAreaFactory;
import dev.ikm.komet.layout.attribute.factory.KlListOfVersionsFieldAreaFactory;
import dev.ikm.komet.layout.window.KlFxWindowFactory;
import dev.ikm.orchestration.interfaces.window.WindowCreateProvider;
import dev.ikm.orchestration.interfaces.window.WindowRestoreProvider;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.GenericAttribute;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.PublicIdAttribute;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.SimpleVersionList;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.simple.SimpleWindowFactory;
import dev.ikm.orchestration.provider.knowledge.layout.menu.NewWindowMenuProvider;
import dev.ikm.orchestration.provider.knowledge.layout.menu.WindowRestoreMenuProvider;

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

    opens dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;
    opens dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;
    opens dev.ikm.orchestration.provider.knowledge.layout.menu;
    opens dev.ikm.orchestration.provider.knowledge.layout.field.blueprint;
    opens dev.ikm.orchestration.provider.knowledge.layout.field.simple;
    opens dev.ikm.orchestration.provider.knowledge.layout.component;
    opens dev.ikm.orchestration.provider.knowledge.layout.window;

    provides KlFxWindowFactory with SimpleWindowFactory;

    provides WindowCreateProvider with NewWindowMenuProvider;
    provides WindowRestoreProvider with WindowRestoreMenuProvider;
    provides KlGenericFieldAreaFactory with
            GenericAttribute.Factory,
            GenericAttribute.BlueFactory;
    provides KlListOfVersionsFieldAreaFactory with
            SimpleVersionList.SimpleVersionListFactory;
    provides KlPublicIdFieldAreaFactory with
            PublicIdAttribute.Factory;

}