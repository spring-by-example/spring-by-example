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
 * @author Rob Harrop
 */
public class ConfigurableBeanValidator extends AbstractBeanValidator {

    private String formName = null;

    protected String getFormName(Class aClass) {
        return this.formName;
    }

    /**
     * @param formName The formName to set.
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }


}
