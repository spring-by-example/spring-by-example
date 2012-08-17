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

package org.springmodules.validation.bean.conf.loader;

import java.util.HashMap;
import java.util.Map;

import org.springmodules.validation.bean.conf.BeanValidationConfiguration;

/**
 * A simple implementation of {@link BeanValidationConfigurationLoader} that enables registration of bean validation
 * configuration with classes.
 * <p/>
 * This class can be used on its own or can be extended and used as configuration cache.
 *
 * @author Uri Boness
 */
public class SimpleBeanValidationConfigurationLoader implements BeanValidationConfigurationLoader {

    private Map configurationByClass;

    /**
     * Constructs a new SimpleBeanValidationConfigurationLoader.
     */
    public SimpleBeanValidationConfigurationLoader() {
        configurationByClass = new HashMap();
    }

    /**
     * @see BeanValidationConfigurationLoader#supports(Class)
     */
    public boolean supports(Class clazz) {
        return configurationByClass.containsKey(clazz);
    }

    /**
     * Returns the bean validation configuration that is associated with the given class, or one of its super classes.
     * Returns <code>null</code> if no such association was found.
     *
     * @param clazz The class of which the validation configuration is requested.
     * @return The validation configuration that is associated with the given class.
     */
    public BeanValidationConfiguration loadConfiguration(Class clazz) {
        while (clazz != null && !configurationByClass.containsKey(clazz)) {
            clazz = clazz.getSuperclass();
        }
        return (clazz != null) ? (BeanValidationConfiguration) configurationByClass.get(clazz) : null;
    }

    /**
     * Registeres the given bean validation configuration with a given class.
     *
     * @param clazz The class to which the given validation configuration will be associated.
     * @param configuration The bean validation configuration to associate with the given class.
     */
    public void setClassValidation(Class clazz, BeanValidationConfiguration configuration) {
        configurationByClass.put(clazz, configuration);
    }

}
