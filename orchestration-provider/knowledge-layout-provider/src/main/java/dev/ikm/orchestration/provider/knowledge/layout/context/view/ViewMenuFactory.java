package dev.ikm.orchestration.provider.knowledge.layout.context.view;

import dev.ikm.komet.framework.view.*;
import dev.ikm.tinkar.common.util.text.NaturalOrder;
import dev.ikm.tinkar.coordinate.edit.Activity;
import dev.ikm.tinkar.coordinate.stamp.StateSet;
import dev.ikm.tinkar.coordinate.view.calculator.ViewCalculator;
import dev.ikm.tinkar.entity.StampService;
import dev.ikm.tinkar.terms.ConceptFacade;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class ViewMenuFactory {

    static final ScopedValue<ObservableView> OBSERVABLE_VIEW = ScopedValue.newInstance();
    static final ScopedValue<ViewCalculator> VIEW_CALCULATOR = ScopedValue.newInstance();

    /**
     * Creates a Menu object based on the provided ObservableView and ViewCalculator.
     *
     * @param observableView the observable view that provides the context for menu creation
     * @param viewCalculator the view calculator used to determine preferred entity strings and other calculations
     * @return a Menu object constructed using the provided parameters
     */
    public static Menu create(ObservableView observableView, ViewCalculator viewCalculator) {

        try (StructuredTaskScope.ShutdownOnFailure scope = new StructuredTaskScope.ShutdownOnFailure()) {
            ScopedValue.Carrier svc = ScopedValue.where(OBSERVABLE_VIEW, observableView).where(VIEW_CALCULATOR, viewCalculator);

            Supplier<List<MenuItem>> overrideMenuItemsSupplier = scope.fork(() -> svc.call(new OverrideMenuItems()));

            Supplier<MenuItem> menuStateItemsForLanguageSupplier = scope.fork(() -> svc.call(new MenuStateItemsForLanguageTask()));
            Supplier<MenuItem> menuStateItemsForLogicSupplier = scope.fork(() -> svc.call(new MenuStateItemsForLogicTask()));
            Supplier<MenuItem> menuStateItemsForNavigationSupplier = scope.fork(() -> svc.call(new MenuStateItemsForNavigationTask()));
            Supplier<MenuItem> menuStateItemsForStampSupplier = scope.fork(() -> svc.call(new MenuStateItemsForStampTask()));
            Supplier<MenuItem> menuStateItemsForViewSupplier = scope.fork(() -> svc.call(new MenuStateItemsForViewTask()));


            Supplier<MenuItem> menuChangeItemsForLanguage = scope.fork(() -> svc.call(new MenuChangeItemsForLanguageTask()));
            Supplier<MenuItem> menuChangeItemsForLogic = scope.fork(() -> svc.call(new MenuChangeItemsForLogicTask()));
            Supplier<MenuItem> menuChangeItemsForNavigation = scope.fork(() -> svc.call(new MenuChangeItemsForNavigationTask()));
            Supplier<MenuItem> menuChangeItemsForStamp = scope.fork(() -> svc.call(new MenuChangeItemsForStampTask()));
            Supplier<MenuItem> menuChangeItemsForView = scope.fork(() -> svc.call(new MenuChangeItemsForViewTask()));

            scope.join().throwIfFailed();

            Menu viewMenu = new Menu(observableView.getName());
            List<MenuItem> overrideMenuItems = overrideMenuItemsSupplier.get();

            if (!overrideMenuItems.isEmpty()) {
                overrideMenuItems.forEach(viewMenu.getItems()::add);
                viewMenu.getItems().add(new SeparatorMenuItem());
            }

            viewMenu.getItems().add(menuStateItemsForStampSupplier.get());
            viewMenu.getItems().add(menuStateItemsForLanguageSupplier.get());
            viewMenu.getItems().add(menuStateItemsForLogicSupplier.get());
            viewMenu.getItems().add(menuStateItemsForNavigationSupplier.get());
            viewMenu.getItems().add(menuStateItemsForViewSupplier.get());

            viewMenu.getItems().add(new SeparatorMenuItem());

            viewMenu.getItems().add(menuChangeItemsForStamp.get());
            viewMenu.getItems().add(menuChangeItemsForLanguage.get());
            viewMenu.getItems().add(menuChangeItemsForLogic.get());
            viewMenu.getItems().add(menuChangeItemsForNavigation.get());
            viewMenu.getItems().add(menuChangeItemsForView.get());

            return viewMenu;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getPropertyNameWithOverride(ViewCalculator viewCalculator, Property<?> baseProperty) {
        if (baseProperty instanceof PropertyWithOverride propertyWithOverride) {
            return propertyWithOverride.getOverrideName(viewCalculator);
        }
        return viewCalculator.toPreferredEntityStringOrInputString(baseProperty.getName());
    }

    static String getNameAndValueString(ViewCalculator viewCalculator, Property<?> baseProperty) {
        String propertyName = getPropertyNameWithOverride(viewCalculator, baseProperty);
        StringBuilder sb = new StringBuilder(propertyName + ": ");
        Object value = baseProperty.getValue();
        if (value instanceof Collection collection) {
            if (collection.isEmpty()) {
                if (propertyName.toLowerCase().startsWith("modules")) {
                    StringBuilder collectionBuilder = new StringBuilder("\u2004\u2004\u2004\u2004\u2004");
                    viewCalculator.toEntityString(StampService.get().getModulesInUse(),
                            viewCalculator::toEntityStringOrPublicIdAndNid,
                            collectionBuilder);
                    sb.append(" (*)\n").append(collectionBuilder);
                } else {
                    viewCalculator.toEntityString(value, viewCalculator::getPreferredDescriptionStringOrNid, sb);
                }
            } else {
                Object obj = collection.iterator().next();
                if (obj instanceof ConceptFacade) {
                    StringBuilder collectionBuilder = new StringBuilder("\u2004\u2004\u2004\u2004\u2004");
                    viewCalculator.toEntityString(value, viewCalculator::toEntityStringOrPublicIdAndNid, collectionBuilder);
                    sb.append("\n").append(collectionBuilder);
                } else {
                    if (collection instanceof Set) {
                        Object[] objects = collection.toArray();
                        Arrays.sort(objects, (o1, o2) ->
                                NaturalOrder.compareStrings(o1.toString(), o2.toString()));
                        sb.append(Arrays.toString(objects));
                    } else {
                        sb.append(collection.toString());
                    }
                }
            }

        } else if (value instanceof Activity) {
            sb.append(((Activity) value).toUserString());
        } else if (value instanceof StateSet) {
            sb.append(((StateSet) value).toUserString());
        } else {
            viewCalculator.toEntityString(value, viewCalculator::getPreferredDescriptionStringOrNid, sb);
        }
        return sb.toString();
    }

    protected static Menu makeStateMenu(ObservableCoordinate observableCoordinate, String menuText, ViewCalculator viewCalculator) {
        Menu stateMenu = new Menu(menuText);
        for (Property<?> baseProperty : observableCoordinate.getBaseProperties()) {
            stateMenu.getItems().add(new MenuItem(getNameAndValueString(viewCalculator, baseProperty)));
        }
        return stateMenu;
    }

    protected static void addChangeStates(List<MenuItem> menuItems, String menuText, ObjectProperty<StateSet> statusProperty) {
        Menu changeAllowedStatusMenu = new Menu(menuText);
        menuItems.add(changeAllowedStatusMenu);

        for (StateSet statusSet : new StateSet[]{StateSet.ACTIVE, StateSet.ACTIVE_AND_INACTIVE,
                StateSet.ACTIVE_INACTIVE_AND_WITHDRAWN, StateSet.INACTIVE, StateSet.WITHDRAWN}) {
            CheckMenuItem item = new CheckMenuItem(statusSet.toUserString());
            item.setSelected(statusSet.equals(statusProperty.get()));
            item.setDisable(item.isSelected());
            item.setOnAction(event -> {
                Platform.runLater(() -> {
                    statusProperty.setValue(statusSet);
                });
                event.consume();
            });
            changeAllowedStatusMenu.getItems().add(item);
        }
    }

    protected static String indexToWord(int index) {
        return switch (index) {
            case 0 -> "First ";
            case 1 -> "Second ";
            case 2 -> "Third ";
            case 3 -> "Fourth ";
            case 4 -> "Fifth ";
            case 5 -> "Sixth ";
            case 6 -> "Seventh ";
            case 7 -> "Eighth ";
            case 8 -> "Ninth ";
            case 9 -> "Tenth ";
            default -> Integer.toString(index);
        };
    }
}
