/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.validation.bean.conf.loader.xml;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationLoader;

/**
 * A base class for all bean validation configuration loaders that are resource based (that is, load configuration from
 * files, urls, etc...)
 *
 * @author Uri Boness
 */
public abstract class AbstractResourceBasedBeanValidationConfigurationLoader
    implements BeanValidationConfigurationLoader, InitializingBean {

    final Logger logger = LoggerFactory.getLogger(AbstractResourceBasedBeanValidationConfigurationLoader.class);

    private final static String DEFAULT_RESOURCE_EXTENTION = ".vld.xml";

    private Map configurationByClass;

    private Resource[] resources;

    /**
     * Constructs a new AbstractResourceBasedBeanValidationConfigurationLoader.
     */
    public AbstractResourceBasedBeanValidationConfigurationLoader() {
        this(new Resource[0]);
    }

    /**
     * Constructs a new AbstractResourceBasedBeanValidationConfigurationLoader with the given resources.
     *
     * @param resources The resources from which this loader will load the bean validation configurations.
     */
    public AbstractResourceBasedBeanValidationConfigurationLoader(Resource[] resources) {
        this.resources = resources;
        configurationByClass = new HashMap();
    }

    /**
     * Loads the bean validation configuration for the given class from the configured resources.
     *
     * @see org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationLoader#loadConfiguration(Class)
     */
    public final BeanValidationConfiguration loadConfiguration(Class clazz) {
        BeanValidationConfiguration configuration = (BeanValidationConfiguration) configurationByClass.get(clazz);
        if (configuration != null) {
            return configuration;
        }
        return loadDefaultConfiguration(clazz);
    }

    /**
     * Determines whether the given class is supported by this loader.
     *
     * @see BeanValidationConfigurationLoader#supports(Class)
     */
    public final boolean supports(Class clazz) {
        return configurationByClass.get(clazz) != null;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        for (int i = 0; i < resources.length; i++) {
            Map configurations = loadConfigurations(resources[i]);
            configurationByClass.putAll(configurations);
        }
    }

    /**
     * Loads all the bean validation configurations from the given resource and returns them as a map where the
     * value is a {@link BeanValidationConfiguration} instance and the key is the associated class.
     *
     * @param resource The resource from which the configurations will be loaded.
     * @return A map of bean validation configuration by the associated class.
     */
    protected abstract Map loadConfigurations(Resource resource);

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the resouces this loader uses to load bean validation configurations.
     *
     * @return The resouces this loader uses to load bean validation configurations.
     */
    public Resource[] getResources() {
        return resources;
    }

    /**
     * Sets the resources this loader will use to load bean validation configurations.
     *
     * @param resources The resources this loader will use to load bean validation configurations.
     */
    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    /**
     * If this loader is configured with only on resource, use this method to retreive it.
     *
     * @return The single resource this loader uses to load bean validation configuration from. If the loader uses
     *         multiple resources, the first one is returned.
     */
    public Resource getResource() {
        return (resources[0] != null) ? resources[0] : null;
    }

    /**
     * Sets a single resource this loader will use to load the bean validation configurations from.
     *
     * @param resource The one resource this loader will use to load the bean validation configurations from.
     */
    public void setResource(Resource resource) {
        setResources(new Resource[]{resource});
    }

    //=============================================== Helper Methods ===================================================

    /**
     * Loads the default validation configuration for the given class, caches it and returns it. The configuration
     * resource for the given class is resolved by the {@link #createDefaultConfigurationFileName(Class)} method.
     *
     * @param clazz The class for which the default configuration should be loaded.
     * @return The default validation configuration of the given class.
     */
    protected BeanValidationConfiguration loadDefaultConfiguration(Class clazz) {
        String fileName = createDefaultConfigurationFileName(clazz);
        Resource resource = new ClassPathResource(fileName, clazz);
        if (resource.exists()) {
            Map configurationByClass = loadConfigurations(resource);
            this.configurationByClass.putAll(configurationByClass);
            return (BeanValidationConfiguration) configurationByClass.get(clazz);
        }
        logger.warn("Could not find the default validation configuration for class '" + clazz.getName() + "'");
        this.configurationByClass.put(clazz, null);
        return null;
    }

    /**
     * Creates the default configuration file name for the given class. This method will search in the classpath for
     * a file that is located where the class itself is located, named after the class, and has the extention of
     * ".vld.xml". For example, the default configuration file name for class
     * <code>Person</code> will be <code>Person.vld.xml</code>.
     *
     * @param clazz The class for which the default configuration will be created.
     * @return The default validatoin configuration resource for the given class.
     */
    protected String createDefaultConfigurationFileName(Class clazz) {
        return ClassUtils.getShortName(clazz) + DEFAULT_RESOURCE_EXTENTION;
    }

}
