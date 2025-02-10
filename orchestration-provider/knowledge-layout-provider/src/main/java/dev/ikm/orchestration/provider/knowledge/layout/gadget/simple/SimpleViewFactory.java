package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.KlView;
import dev.ikm.komet.layout.KlViewFactory;
import dev.ikm.komet.layout.context.KlContextFactory;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.context.ContextFactory;
import dev.ikm.tinkar.coordinate.view.ViewCoordinateRecord;

public class SimpleViewFactory implements KlViewFactory {
    @Override
    public KlView create(KlPreferencesFactory preferencesFactory) {
        return new SimpleView(preferencesFactory, this, ContextFactory.getDefault());
    }

    @Override
    public KlView createWithContext(KlPreferencesFactory preferencesFactory, KlContextFactory contextFactory) {
        return new SimpleView(preferencesFactory, this, contextFactory);
    }

    @Override
    public KlView restore(KometPreferences preferences) {
        return new SimpleView(preferences);
    }

    @Override
    public Class<KlView> klInterfaceClass() {
        return KlView.class;
    }

    @Override
    public Class<? extends KlView> klImplementationClass() {
        return SimpleView.class;
    }

    @Override
    public KlView createWithView(KlPreferencesFactory preferencesFactory, ViewCoordinateRecord viewCoordinateRecord) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
