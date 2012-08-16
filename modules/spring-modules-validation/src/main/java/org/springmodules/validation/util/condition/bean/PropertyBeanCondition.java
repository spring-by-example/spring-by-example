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
import org.springmodules.validation.util.condition.Condition;

/**
 * An {@link AbstractBeanCondition} implementation that checks the value of a specific property of the checked bean
 * using a specific condition.
 *
 * @author Uri Boness
 */
public class PropertyBeanCondition extends AbstractBeanCondition {

    private String propertyName;

    private Condition propertyCondition;

    /**
     * Constructs a new PropertyBeanCondition with a given name of the property to be checked by the given condition.
     *
     * @param propertyName The name of the property to be checked.
     * @param propertyCondition The condition that will be used to checkCalendar the property value.
     */
    public PropertyBeanCondition(String propertyName, Condition propertyCondition) {
        Assert.notNull(propertyName, "Property name cannot be null");
        Assert.notNull(propertyCondition, "Property condition cannot be null");
        this.propertyName = propertyName;
        this.propertyCondition = propertyCondition;
    }

    /**
     * Checks the value of the property of the given bean using the property condition associated with this condition. The
     * property to be checked is resolved by the property name associated with this condition.
     *
     * @param bean The bean to be checked.
     * @return <code>true</code> if the property condition associated with this condition returns <code>true</code> when
     *         checking the bean's property, <code>false</code> otherwise.
     */
    protected boolean checkBean(BeanWrapper bean) {
        Object value = bean.getPropertyValue(propertyName);
        return propertyCondition.check(value);
    }

    //============================================= Setter/Getter ===================================================

    /**
     * Return the property name associated with this condition.
     *
     * @return The property name associated with this condition.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns the condition that is used to checkCalendar the property of the checked bean.
     *
     * @return The condition that is used to checkCalendar the property of the checked bean.
     */
    public Condition getPropertyCondition() {
        return propertyCondition;
    }

}
