package dev.ikm.orchestration.provider.knowledge.layout.gadget.simple;

import dev.ikm.komet.layout.KlSceneEnhancer;
import javafx.scene.Scene;
import org.scenicview.ScenicView;

public class ScenicViewProvider implements KlSceneEnhancer {

    @Override
    public void accept(Scene scene) {
        ScenicView.show(scene);
    }
}
