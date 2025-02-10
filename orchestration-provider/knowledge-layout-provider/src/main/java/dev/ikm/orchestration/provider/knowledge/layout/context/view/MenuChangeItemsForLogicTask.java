package dev.ikm.orchestration.provider.knowledge.layout.context.view;

import dev.ikm.komet.framework.view.ObservableView;
import dev.ikm.tinkar.coordinate.view.calculator.ViewCalculator;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.concurrent.Callable;

import static dev.ikm.orchestration.provider.knowledge.layout.context.view.ViewMenuFactory.OBSERVABLE_VIEW;
import static dev.ikm.orchestration.provider.knowledge.layout.context.view.ViewMenuFactory.VIEW_CALCULATOR;

public class MenuChangeItemsForLogicTask implements Callable<MenuItem>, ScopedValue.CallableOp<MenuItem, Exception> {
    @Override
    public MenuItem call() throws Exception {
        ObservableView observableView = OBSERVABLE_VIEW.get();
        ViewCalculator viewCalculator = VIEW_CALCULATOR.get();
        Menu changeLogic = new Menu("Change logic");
        changeLogic.getItems().add(new MenuItem("Change logic not implemented"));
        return changeLogic;
    }
}
