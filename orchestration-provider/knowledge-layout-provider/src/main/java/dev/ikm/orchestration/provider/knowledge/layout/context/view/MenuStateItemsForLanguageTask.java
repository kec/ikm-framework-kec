package dev.ikm.orchestration.provider.knowledge.layout.context.view;

import dev.ikm.komet.framework.view.ObservableView;
import dev.ikm.tinkar.coordinate.view.calculator.ViewCalculator;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.concurrent.Callable;

import static dev.ikm.orchestration.provider.knowledge.layout.context.view.ViewMenuFactory.*;

public class MenuStateItemsForLanguageTask implements Callable<MenuItem>, ScopedValue.CallableOp<MenuItem, Exception> {
    @Override
    public MenuItem call() throws Exception {
        ObservableView observableView = OBSERVABLE_VIEW.get();
        ViewCalculator viewCalculator = VIEW_CALCULATOR.get();

        Menu languageStatesMenu = new Menu("Language coordinates");
        for (int i = 0; i < observableView.languageCoordinates().size(); i++) {
            Menu stateMenu = ViewMenuFactory.makeStateMenu(observableView.languageCoordinates().get(i),
                    indexToWord(i) + " coordinate ", viewCalculator);
            languageStatesMenu.getItems().add(stateMenu);
        }

        return languageStatesMenu;
    }
}
