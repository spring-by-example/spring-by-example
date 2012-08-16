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

package org.springmodules.validation.util.condition.bean;

import org.springframework.beans.BeanWrapper;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * An {@link AbstractBeanCondition} implementation that checks whether specific properties of the checked bean
 * have the same value.
 *
 * @author Uri Boness
 */
public class EqualPropertiesBeanCondition extends AbstractBeanCondition {

    private String[] propertyNames;

    /**
     * Constructs a new EqualPropertiesBeanCondition with a given list of names of the properties to be compared.
     *
     * @param propertyNames The name of the properties to be compared.
     * @throws IllegalArgumentException when the given property names array contains zero or one names.
     */
    public EqualPropertiesBeanCondition(String[] propertyNames) {
        Assert.notNull(propertyNames, getClass().getName() + " cannot be initialized with null array of property names");
        Assert.notEmpty(propertyNames, getClass().getName() + " cannot be initialized with less then 2 property names");
        for (int i = 0; i < propertyNames.length; i++) {
            Assert.notNull(propertyNames[i], getClass().getName() + " cannot be initialized with null property name");
        }
        this.propertyNames = propertyNames;
    }

    /**
     * Constructs a new EqualPropertiesBeanCondition with two given names of the properties to be compared.
     *
     * @param property1 The name of the first property.
     * @param property2 The name of the second property.
     */
    public EqualPropertiesBeanCondition(String property1, String property2) {
        this(new String[]{property1, property2});
    }

    /**
     * Checks whether a set of properties of the given bean have the same value. The properties are resolved based on
     * the property names associated with this condition.
     *
     * @param bean The bean to be checked.
     * @return <code>true</code> if all the compared properties are equal, <code>false</code> otherwise.
     */
    protected boolean checkBean(BeanWrapper bean) {
        Object value = bean.getPropertyValue(propertyNames[0]);
        for (int i = 1; i < propertyNames.length; i++) {
            Object currentValue = bean.getPropertyValue(propertyNames[i]);
            if (!ObjectUtils.nullSafeEquals(value, currentValue)) {
                return false;
            }
        }
        return true;
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the property names associated with this condition.
     *
     * @return The property names associated with this condition.
     */
    public String[] getPropertyNames() {
        return propertyNames;
    }

}
