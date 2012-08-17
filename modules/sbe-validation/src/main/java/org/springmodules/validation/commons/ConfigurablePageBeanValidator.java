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

/**
 * A bean validator that is aware of the page attribute in the form configuration.
 * This may be useful for partial bean validation (that is, selected properties of the bean) needed
 * for example in web wizard controllers.
 *
 * @author Uri Boness
 */
public class ConfigurablePageBeanValidator extends AbstractPageBeanValidator {

    private String formName;

    /**
     * Default constructor (javabean support)
     */
    public ConfigurablePageBeanValidator() {
    }

    /**
     * Constructs a new DefaultPageBeanValidator with a given page to validate.
     *
     * @param page The page that should be validated by this validator.
     */
    public ConfigurablePageBeanValidator(int page) {
        super(page);
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
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

}
