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

import java.beans.PropertyDescriptor;

import org.springmodules.validation.bean.conf.loader.xml.handler.ClassValidationElementHandler;
import org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler;
import org.w3c.dom.Element;

/**
 * A registry of {@link org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler}'s and {@link ClassValidationElementHandler}'s.
 *
 * @author Uri Boness
 */
public interface ValidationRuleElementHandlerRegistry {

    /**
     * Returns the class validation element handler that can handle the given element.
     *
     * @param element The element to be handled.
     * @param clazz The validated class.
     * @return The class validation element handler that can handle the given element.
     */
    ClassValidationElementHandler findClassHandler(Element element, Class clazz);

    /**
     * Returns the property validation element handler that can handle the given element.
     *
     * @param element The element be handled.
     * @param clazz The validated class.
     * @param descriptor The property descriptor of the validated property.
     * @return The property validation element handler that can handle the given element.
     */
    PropertyValidationElementHandler findPropertyHandler(Element element, Class clazz, PropertyDescriptor descriptor);

}
