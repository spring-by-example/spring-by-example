/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springmodules.validation.bean.rule;

import org.springmodules.validation.util.condition.Condition;
import org.springframework.context.ApplicationContext;

/**
 * @author Uri Boness
 */
public class ConditionReferenceValidationRule extends AbstractValidationRule {

    public final static String DEFAULT_ERROR_CODE = "spring.condition";

    private final Condition condition;

    public ConditionReferenceValidationRule(String beanName, ApplicationContext applicationContext) {
        super(DEFAULT_ERROR_CODE, createErrorArgumentsResolver(new Object[] { beanName }));
        condition = (Condition)applicationContext.getBean(beanName, Condition.class);
    }

    public Condition getCondition() {
        return condition;
    }

    protected boolean supportsNullValues() {
        return true;
    }
}
