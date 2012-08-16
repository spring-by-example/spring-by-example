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
import org.springframework.beans.BeanWrapperImpl;
import org.springmodules.validation.util.condition.NonNullAcceptingCondition;

/**
 * A parent class to all bean related conditions. Such condition may perform checks on beans
 * properties.
 *
 * @author Uri Boness
 */
public abstract class AbstractBeanCondition extends NonNullAcceptingCondition {

    /**
     * See {@link org.springmodules.validation.util.condition.Condition#check(Object)}. This method creates a
     * {@link BeanWrapper} instance of the checked object and delegates the call to
     * {@link #checkBean(org.springframework.beans.BeanWrapper)};
     *
     * @throws IllegalArgumentException when the passed in object is <code>null</code>.
     */
    public final boolean doCheck(Object object) {
        return checkBean(new BeanWrapperImpl(object));
    }

    protected abstract boolean checkBean(BeanWrapper beanWrapper);

}
