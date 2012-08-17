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

package org.springmodules.validation.util.condition;

/**
 * A base class for all type specific conditions. A type specific instantCondition can only checkCalendar specific object types.
 *
 * @author Uri Boness
 */
public abstract class TypeSpecificCondition extends AbstractCondition {

    private Class[] types;

    /**
     * Construts a new TypeSpecificCondition with the given supported type. Sub classes should call this constructor
     * if they only support on object type.
     *
     * @param type The object type this condition supports.
     */
    public TypeSpecificCondition(Class type) {
        this(new Class[]{type});
    }

    /**
     * Constructs a new TypeSpecificCondition with a the give list of supported types. Sub-classes should call this
     * constructor if they support more then one object type.
     *
     * @param types The object types this condition supports.
     */
    public TypeSpecificCondition(Class[] types) {
        this.types = types;
    }

    /**
     * Checks whether the checked object is of one of the types supported by this condition.
     *
     * @param object The object to be checked.
     * @throws IllegalArgumentException if the checked object type is not supported by this condition.
     */
    protected void beforeObjectChecked(Object object) {

        boolean foundMatchingType = false;

        for (int i = 0; i < types.length; i++) {
            if (types[i].isAssignableFrom(object.getClass())) {
                foundMatchingType = true;
                break;
            }
        }

        if (!foundMatchingType) {
            StringBuffer message = new StringBuffer(getClass().getName());
            message.append("can only validation values of the following types: ");
            for (int j = 0; j < types.length; j++) {
                if (j != 0) {
                    message.append(", ");
                }
                message.append(types[j].getName());
            }
            throw new IllegalArgumentException(message.toString());
        }
    }

}
