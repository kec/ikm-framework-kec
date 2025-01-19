package dev.ikm.orchestration.provider.knowledge.layout.blueprint;

import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.preferences.PreferencePropertyDouble;
import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WindowAbstract extends GadgetAbstract<Window> implements KlWindow {
    private static final Logger LOG = LoggerFactory.getLogger(WindowAbstract.class);

    private final PreferencePropertyDouble locationX;
    public WindowAbstract(KometPreferences preferences) {
        super(preferences);
        Window window = klGadget();
        PreferencePropertyDouble locationY = PreferencePropertyDouble.doubleProp(this, KlWindow.PreferenceKeys.WINDOW_Y_LOCATION);

        ReadOnlyDoubleProperty xReadOnly = window.xProperty();


        //window.setX();
        locationX = PreferencePropertyDouble.doubleProp(this,
                KlWindow.PreferenceKeys.WINDOW_X_LOCATION);

    }

    public WindowAbstract(KometPreferences preferences, KlFactory factory) {
        this(preferences);
        //TODO handle factory...
    }
}
