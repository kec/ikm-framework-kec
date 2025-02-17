package dev.ikm.orchestration.provider.knowledge.layout.field.simple;

import dev.ikm.komet.layout.component.field.KlGenericFieldPane;
import dev.ikm.komet.layout.component.field.KlGenericFieldPaneFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;

public class SimpleGenericFieldPaneFactory implements KlGenericFieldPaneFactory {
    @Override
    public SimpleGenericFieldPane create(KlPreferencesFactory preferencesFactory) {
        return new SimpleGenericFieldPane(preferencesFactory, this);
    }

    @Override
    public SimpleGenericFieldPane restore(KometPreferences preferences) {
        return new SimpleGenericFieldPane(preferences);
    }

    @Override
    public Class<SimpleGenericFieldPane> klImplementationClass() {
        return SimpleGenericFieldPane.class;
    }
}
