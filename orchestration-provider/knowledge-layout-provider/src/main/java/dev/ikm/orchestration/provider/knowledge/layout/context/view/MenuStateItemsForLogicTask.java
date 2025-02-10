package dev.ikm.orchestration.provider.knowledge.layout.context.view;

import dev.ikm.komet.framework.view.ObservableCoordinate;
import dev.ikm.komet.framework.view.ObservableView;
import dev.ikm.tinkar.coordinate.view.calculator.ViewCalculator;
import javafx.beans.property.Property;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.concurrent.Callable;

import static dev.ikm.orchestration.provider.knowledge.layout.context.view.ViewMenuFactory.*;

public class MenuStateItemsForLogicTask implements Callable<MenuItem>, ScopedValue.CallableOp<MenuItem, Exception> {
    @Override
    public MenuItem call() throws Exception {
        ObservableView observableView = OBSERVABLE_VIEW.get();
        ViewCalculator viewCalculator = VIEW_CALCULATOR.get();

        return ViewMenuFactory.makeStateMenu(observableView.logicCoordinate(), "Logic coordinate", viewCalculator);
    }
}
