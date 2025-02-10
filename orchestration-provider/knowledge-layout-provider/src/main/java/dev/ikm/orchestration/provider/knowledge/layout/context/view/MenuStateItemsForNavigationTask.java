package dev.ikm.orchestration.provider.knowledge.layout.context.view;

import dev.ikm.komet.framework.concurrent.TaskWrapper;
import dev.ikm.komet.framework.temp.FxGet;
import dev.ikm.komet.framework.view.ObservableView;
import dev.ikm.komet.framework.view.ViewMenuTask;
import dev.ikm.tinkar.common.id.PublicIdStringKey;
import dev.ikm.tinkar.common.service.TinkExecutor;
import dev.ikm.tinkar.coordinate.stamp.StampPathImmutable;
import dev.ikm.tinkar.coordinate.view.VertexSort;
import dev.ikm.tinkar.coordinate.view.VertexSortNaturalOrder;
import dev.ikm.tinkar.coordinate.view.VertexSortNone;
import dev.ikm.tinkar.coordinate.view.calculator.ViewCalculator;
import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.List;
import java.util.concurrent.Callable;

import static dev.ikm.orchestration.provider.knowledge.layout.context.view.ViewMenuFactory.OBSERVABLE_VIEW;
import static dev.ikm.orchestration.provider.knowledge.layout.context.view.ViewMenuFactory.VIEW_CALCULATOR;

public class MenuStateItemsForNavigationTask implements Callable<MenuItem>, ScopedValue.CallableOp<MenuItem, Exception> {
    @Override
    public MenuItem call() throws Exception {
        ObservableView observableView = OBSERVABLE_VIEW.get();
        ViewCalculator viewCalculator = VIEW_CALCULATOR.get();

        return ViewMenuFactory.makeStateMenu(observableView.navigationCoordinate(), "Navigation coordinate", viewCalculator);
    }

}
