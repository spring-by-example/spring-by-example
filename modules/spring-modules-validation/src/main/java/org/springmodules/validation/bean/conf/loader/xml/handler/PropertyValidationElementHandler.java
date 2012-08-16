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

package org.springmodules.validation.bean.conf.loader.xml.handler;

import java.beans.PropertyDescriptor;

import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.w3c.dom.Element;

/**
 * An handler that handles property level validation dom elements and manipulates the validation configuration
 * appropriately.
 *
 * @author Uri Boness
 */
public interface PropertyValidationElementHandler {

    /**
     * Determines whether this handler can handle the given element.
     *
     * @param element The element to be handled.
     * @param clazz The validated clazz.
     * @param descriptor The property descriptor of the validated property.
     * @return <code>true</code> if this handler can handle the given element, <code>false</code> otherwise.
     */
    boolean supports(Element element, Class clazz, PropertyDescriptor descriptor);

    /**
     * Handles the given element and and manipulates the given configuration appropriately.
     *
     * @param element The element to be handled.
     * @param propertyName The name of the validated property.
     * @param configuration The configuration to be manipulated.
     */
    void handle(Element element, String propertyName, MutableBeanValidationConfiguration configuration);

}
