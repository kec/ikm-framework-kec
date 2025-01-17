package dev.ikm.orchestration.provider.knowledge.layout;

import dev.ikm.komet.layout.window.KlWindow;
import dev.ikm.tinkar.common.bind.ClassConceptBinding;

/**
 * Enum representing the keys used to manage and access user preferences
 * related to gadgets within the application. This enum defines constants
 * that are essential for storing and retrieving configuration or state
 * information for restoring windows or initializing preferences.
 */
public enum GadgetKeys implements ClassConceptBinding {
    /**
     * Boolean string representing if the preferences have been initialized.
     */
    INITIALIZED,

    /**
     * Fully qualified name of the factory class. Used to restore the KlWindow
     * from preferences.
     */
    FACTORY_CLASS,

    /**
     * Represents the name of the specific implementation of a {@link KlWindow}
     * that can be restored from preferences. This key is used to identify
     * and manage restoration of the window's state during application initialization
     * or when reloading user preferences.
     */
    NAME_FOR_RESTORE;
}
