/*
 * Copyright © 2015 Integrated Knowledge Management (support@ikm.dev)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.ikm.plugin.service.loader;

import dev.ikm.plugin.layer.PluggableService;
import dev.ikm.plugin.layer.PluginLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * The PluginServiceLoader class is responsible for loading and managing service loaders
 * for pluggable services. It implements the PluginServiceLoader interface and also serves
 * as a PluginLifecycleListener to receive notifications about the addition or removal of
 * plugin layers.
 */
public class PluginServiceLoader implements dev.ikm.plugin.layer.PluginServiceLoader, PluginLifecycleListener {
    private static final Logger LOG = LoggerFactory.getLogger(PluginServiceLoader.class);

    /**
     * Returns a ServiceLoader for the given pluggable service class.
     * <p>
     * Note that the caller must prepare to abandon all references to the
     * loader and service if the deployment allows for dynamic removal of plugin layers,
     * as would be notified by a PluginLifecycleListener.pluginLayerBeingRemoved
     * event. If the deployment allows for dynamic removal of plugin layers,
     * then the caller must either: 1) register for the events, and act accordingly, or
     * 2) only use the services on a transient and dynamic manner on demand. Otherwise,
     * a memory leak may occur, and if an unloaded plugin layers is reloaded without
     * removing all references to the unloaded layer's classes, undefined behaviour may result.
     *
     * @param service the pluggable service class
     * @param <S>     the type of the service
     * @return a ServiceLoader object for the given service class
     */
    @Override
    public <S> ServiceLoader<S> loader(Class<S> service) {
        return ServiceLoader.load(PluginServiceLoader.class.getModule().getLayer(), service);
    }

    /**
     * Ensures that the specified service is registered in the Java module system.
     * <p>
     * This method checks if the current class's module can use the given service.
     * If not, it adds the service to the uses clause of the module.
     *
     * @param service the service class to be checked
     * @return true if the service was added to the uses clause of the module
     *         (meaning it was not already included for this module),
     *         false otherwise
     */
    @Override
    public boolean ensureUses(Class<?> service) {
        if (!this.getClass().getModule().canUse(service)) {
            this.getClass().getModule().addUses(service);
            return true;
        }
        return false;
    }

    /**
     * Notifies the PluginServiceLoader that a plugin layer has been added.
     * This method sets the PluginServiceLoader as the service provider
     * for the PluggableService.
     *
     * @param pluginLayerName the name of the plugin layer that was added
     * @param pluginLayer     the ModuleLayer representing the added plugin layer
     */
    @Override
    public void pluginLayerAdded(String pluginLayerName, ModuleLayer pluginLayer) {
        PluggableService.setServiceProvider(this);
        LOG.info("added plugin layer: " + pluginLayerName + ": " + pluginLayer);
    }

    /**
     * Notifies the PluginServiceLoader that a plugin layer is being removed.
     *
     * @param pluginLayerName the name of the plugin layer being removed
     * @param pluginLayer the ModuleLayer representing the plugin layer being removed
     */
    @Override
    public void pluginLayerBeingRemoved(String pluginLayerName, ModuleLayer pluginLayer) {
        LOG.info("removing plugin layer: " + pluginLayerName + ": " + pluginLayer);
    }
}