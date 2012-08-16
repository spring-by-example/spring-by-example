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

import org.springmodules.validation.bean.conf.BeanValidationConfiguration;

/**
 * A strategy to load {@link org.springmodules.validation.bean.conf.BeanValidationConfiguration}'s for bean clases.
 *
 * @author Uri Boness
 */
public interface BeanValidationConfigurationLoader {

    /**
     * Loads the bean validation configuration for the given class.
     *
     * @param clazz The class for which the validation configuration will be loaded.
     * @return The bean validation configuration for the given class or <code>null</code> if no configuration could be
     *         found.
     */
    BeanValidationConfiguration loadConfiguration(Class clazz);

    /**
     * Indicates whether the given class is supported by this loader. The {@link #loadConfiguration(Class)} will return
     * a validation configuration only for the supported classes. This method should typically be called to determine
     * whether the {@link #loadConfiguration(Class)} should be called.
     *
     * @param clazz The class to be checked.
     * @return <code>true</code> if this loader can load a valication configuration for the given class,
     *         <code>false</code> otherwise.
     */
    boolean supports(Class clazz);

}
