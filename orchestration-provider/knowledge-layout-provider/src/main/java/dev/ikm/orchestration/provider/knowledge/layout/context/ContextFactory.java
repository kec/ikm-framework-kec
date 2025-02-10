package dev.ikm.orchestration.provider.knowledge.layout.context;

import dev.ikm.komet.layout.context.KlContext;
import dev.ikm.komet.layout.context.KlContextFactory;
import dev.ikm.komet.layout.context.KlContextProvider;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.tinkar.common.id.PublicId;
import dev.ikm.tinkar.common.id.PublicIdStringKey;
import dev.ikm.tinkar.common.id.PublicIds;
import dev.ikm.tinkar.coordinate.Coordinates;
import dev.ikm.tinkar.coordinate.view.ViewCoordinateRecord;

import java.util.UUID;

public class ContextFactory implements KlContextFactory {
    private final ViewCoordinateRecord viewCoordinateRecord;

    private ContextFactory(ViewCoordinateRecord viewCoordinateRecord) {
        this.viewCoordinateRecord = viewCoordinateRecord;
    }

    @Override
    public KlContext create(KlContextProvider klContextProvider) {
        PublicId publicId = PublicIds.of(UUID.randomUUID());
        PublicIdStringKey publicIdStringKey = new PublicIdStringKey(publicId, "Context for " + klContextProvider.getClass().getSimpleName());
        return new GadgetContext(klContextProvider, viewCoordinateRecord, publicIdStringKey);
    }

    @Override
    public KlContext restore(KometPreferences preferences, KlContextProvider klContextProvider) {
        return GadgetContext.restore(preferences, klContextProvider);
    }

    public static ContextFactory getDefault() {
        return new ContextFactory(Coordinates.View.DefaultView());
    }

    public static ContextFactory getWithViewCoordinate(ViewCoordinateRecord viewCoordinateRecord) {
        return new ContextFactory(viewCoordinateRecord);
    }
}
