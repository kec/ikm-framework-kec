package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.KlWidget;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class SimpleWidgetFactory implements KlFactory<KlWidget> {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWidgetFactory.class);

    public KlWidget<Label> create(KlPreferencesFactory preferencesSupplier) {
        return SimpleWidget.create(preferencesSupplier.get(), this);
    }

    @Override
    public KlWidget<Label> restore(KometPreferences preferences) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Class<KlWidget> klInterfaceClass() {
        return KlWidget.class;
    }

    @Override
    public Class<? extends KlWidget<Label>> klImplementationClass() {
        return SimpleWidget.class;
    }
}
