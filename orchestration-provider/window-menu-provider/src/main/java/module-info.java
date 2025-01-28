import dev.ikm.orchestration.interfaces.journal.NewJournalService;
import dev.ikm.orchestration.interfaces.menu.WindowMenuService;
import dev.ikm.orchestration.interfaces.window.WindowCreateProvider;
import dev.ikm.orchestration.interfaces.window.WindowRestoreProvider;
import dev.ikm.orchestration.provider.window.menu.NewJournalProvider;
import dev.ikm.orchestration.provider.window.menu.WindowMenuProvider;

module dev.ikm.orchestration.provider.window.service {
    requires dev.ikm.komet.details;
    requires dev.ikm.komet.framework;
    requires dev.ikm.komet.list;
    requires dev.ikm.komet.navigator;
    requires dev.ikm.komet.preferences;
    requires dev.ikm.komet.progress;
    requires dev.ikm.komet.search;
    requires dev.ikm.orchestration.interfaces;
    requires dev.ikm.tinkar.common;
    requires javafx.controls;
    requires javafx.graphics;
    requires dev.ikm.jpms.eclipse.collections;
    requires dev.ikm.jpms.eclipse.collections.api;
    requires org.slf4j;
    requires dev.ikm.komet.kview;

    uses WindowCreateProvider;
    uses WindowRestoreProvider;

    provides WindowMenuService with WindowMenuProvider;
    provides NewJournalService with NewJournalProvider;
}