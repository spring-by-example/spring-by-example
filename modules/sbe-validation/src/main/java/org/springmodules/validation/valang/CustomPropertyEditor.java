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

package org.springmodules.validation.valang;

import java.beans.PropertyEditor;

/**
 * <p>Container class to register a custom property editor.
 * <i>propertyEditor</i> and <i>requiredType</i> properties
 * are mandatory, <i>propertyPath</i> property is optional.
 *
 * @author Steven Devijver
 * @since Apr 24, 2005
 */
public class CustomPropertyEditor {

    private Class requiredType = null;

    private PropertyEditor propertyEditor = null;

    private String propertyPath = null;

    public CustomPropertyEditor() {
        super();
    }

    /**
     * <p>Get custom property editor.
     *
     * @return custom property editor
     */
    public PropertyEditor getPropertyEditor() {
        return propertyEditor;
    }

    /**
     * <p>Sets custom property editor.
     *
     * @param propertyEditor custom property editor
     */
    public void setPropertyEditor(PropertyEditor propertyEditor) {
        this.propertyEditor = propertyEditor;
    }

    /**
     * <p>Gets the property path (optional).
     *
     * @return property path
     */
    public String getPropertyPath() {
        return propertyPath;
    }

    /**
     * <p>Sets the property path (optional).
     *
     * @param propertyPath
     */
    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    /**
     * <p>Gets the required type.
     *
     * @return required type
     */
    public Class getRequiredType() {
        return requiredType;
    }

    /**
     * <p>Sets the required type.
     *
     * @param requiredType the required type
     */
    public void setRequiredType(Class requiredType) {
        this.requiredType = requiredType;
    }
}
