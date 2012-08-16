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

package org.springmodules.validation.util.condition.common;

import org.springframework.util.Assert;
import org.springmodules.validation.util.condition.AbstractCondition;

/**
 * A condition that checks whether the checked object is of a specific type.
 *
 * @author Uri Boness
 */
public class InstanceOfCondition extends AbstractCondition {

    private Class clazz;

    /**
     * Constructs a new InstanceOfCondition with a given class the checked object will be checked against.
     *
     * @param clazz The class the checked object will be checked against.
     */
    public InstanceOfCondition(Class clazz) {
        Assert.notNull(clazz, "Class cannot be null");
        this.clazz = clazz;
    }

    /**
     * Checks whether the given object is an instance of the class associated with this condition.
     *
     * @param object The object to be checked.
     * @return <code>true</code> if the given object is an instance of the class associated with this condition,
     *         <code>false</code> otherwise. Note: if the given object is <code>null</code>, <code>false</code>
     *         will be returned.
     */
    public boolean doCheck(Object object) {
        if (object == null) {
            return false;
        }
        return clazz.isAssignableFrom(object.getClass());
    }

    //============================================= Setter/Getter ===================================================

    /**
     * Returns the class associated with this condition.
     *
     * @return The class associated with this condition.
     */
    public Class getClazz() {
        return clazz;
    }

}
