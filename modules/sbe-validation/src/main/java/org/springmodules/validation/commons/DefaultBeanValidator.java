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

package org.springmodules.validation.commons;

import java.beans.Introspector;

import org.springframework.util.ClassUtils;

/**
 * @author Daniel Miller
 * @author Rob Harrop
 */
public class DefaultBeanValidator extends AbstractBeanValidator {

    /**
     * If <code>true</code> the full class name of each bean will be used as the form name when looking for
     * a <code>Validator</code>. If <code>false</code> the uncapitalized, short name of the class will be used.
     */
    private boolean useFullyQualifiedClassName = false;

    /**
     * Sets the value of the <code>useFullyQualifiedClassName</code>.
     */
    public void setUseFullyQualifiedClassName(boolean useFullyQualifiedClassName) {
        this.useFullyQualifiedClassName = useFullyQualifiedClassName;
    }

    /**
     * If <code>useFullyQualifiedClassName</code> is false (default value), this function returns a
     * string containing the uncapitalized, short name for the given class
     * (e.g. myBean for the class com.domain.test.MyBean). Otherwise, it  returns the value
     * returned by <code>Class.getName()</code>.
     *
     * @param cls <code>Class</code> of the bean to be validated.
     * @return the bean name.
     */
    protected String getFormName(Class cls) {
        return (this.useFullyQualifiedClassName) ? cls.getName() : Introspector.decapitalize(ClassUtils.getShortName(cls));
    }
}
