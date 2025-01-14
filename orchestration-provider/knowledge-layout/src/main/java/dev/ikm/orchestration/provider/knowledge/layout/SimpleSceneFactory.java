package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.KlWidget;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.layout.window.KlScene;
import dev.ikm.komet.layout.window.KlSceneFactory;
import dev.ikm.komet.preferences.KometPreferences;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class SimpleSceneFactory implements KlSceneFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleSceneFactory.class);

    @Override
    public KlScene create(KlWidget widget, Supplier<KometPreferences> preferenceSupplier) {
        return new KlSceneRecord(new Scene(widget.klWidget()), widget, preferenceSupplier.get());
    }

    @Override
    public KlScene restore(KometPreferences preferences) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Class klInterfaceClass() {
        return KlScene.class;
    }

    @Override
    public Class klImplementationClass() {
        return KlSceneRecord.class;
    }
}
