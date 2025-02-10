package dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.context.KlContextFactory;
import dev.ikm.komet.layout.context.KlContextProvider;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.context.GadgetContext;

public sealed abstract class GadgetWithContextBlueprint<T> extends GadgetBlueprint<T>
    implements KlContextProvider
    permits StageBlueprint, ViewBlueprint {

    private final GadgetContext gadgetContext;

    // restore
    public GadgetWithContextBlueprint(KometPreferences preferences, T fxGadget) {
        super(preferences, fxGadget);
        this.gadgetContext = GadgetContext.restore(preferences, this);
    }

    // new
    public GadgetWithContextBlueprint(KlPreferencesFactory preferencesFactory, KlFactory factory,
                                      KlContextFactory contextFactory, T fxGadget) {
        super(preferencesFactory, factory, fxGadget);
        //TODO see if this cast can be eliminated somehow...
        this.gadgetContext = (GadgetContext) contextFactory.create(this);
    }

    @Override
    public GadgetContext context() {
        return gadgetContext;
    }

    @Override
    protected final void subGadgetSave() {
        gadgetContext.save();
        subContextSave();
    }

    @Override
    protected final void subGadgetRevert() {
        gadgetContext.revert();
        subContextRevert();
    }

    protected abstract void subContextSave();
    protected abstract void subContextRevert();
}
