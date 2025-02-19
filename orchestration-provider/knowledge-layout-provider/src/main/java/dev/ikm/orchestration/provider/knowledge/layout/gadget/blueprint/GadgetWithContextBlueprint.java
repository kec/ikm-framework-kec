package dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.context.KlContextFactory;
import dev.ikm.komet.layout.context.KlContextProvider;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.context.GadgetContext;

/**
 * Represents an abstract base class for gadgets with context support. This class extends
 * {@link GadgetBlueprint} and implements {@link KlContextProvider}. It includes core functionality
 * for managing a {@link GadgetContext} associated with the gadget and provides mechanisms for
 * saving and reverting both gadget and context states. It is a sealed class, permitting
 * {@code StageBlueprint} and {@code ViewBlueprint} as its subclasses.
 *
 * @param <FX> The type parameter defining the FX gadget type used in this blueprint.
 */
public sealed abstract class GadgetWithContextBlueprint<FX> extends GadgetBlueprint<FX>
    implements KlContextProvider
    permits StageBlueprint, ViewBlueprint {

    private final GadgetContext gadgetContext;

    // restore
    public GadgetWithContextBlueprint(KometPreferences preferences, FX fxGadget) {
        super(preferences, fxGadget);
        this.gadgetContext = GadgetContext.restore(preferences, this);
    }

    // new
    public GadgetWithContextBlueprint(KlPreferencesFactory preferencesFactory, KlFactory factory,
                                      KlContextFactory contextFactory, FX fxGadget) {
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
