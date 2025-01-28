package dev.ikm.orchestration.interfaces.window;

import org.controlsfx.control.action.Action;
import org.eclipse.collections.api.list.ImmutableList;

public interface WindowCreateProvider {
    ImmutableList<Action> createWindowActions();
}
