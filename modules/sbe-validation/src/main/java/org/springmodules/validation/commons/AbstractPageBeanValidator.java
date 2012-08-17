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

import org.apache.commons.validator.Validator;

/**
 * @author Uri Boness
 */
public abstract class AbstractPageBeanValidator extends AbstractBeanValidator implements PageAware {

    private final static int DEFAULT_PAGE = -1;

    private int page = DEFAULT_PAGE;

    protected AbstractPageBeanValidator() {
    }

    protected AbstractPageBeanValidator(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    protected void initValidator(Validator validator) {
        validator.setPage(page);
    }

    protected void cleanupValidator(Validator validator) {
        validator.setPage(DEFAULT_PAGE);
    }
}
