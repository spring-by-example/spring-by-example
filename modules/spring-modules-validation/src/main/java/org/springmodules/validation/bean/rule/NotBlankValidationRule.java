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

package org.springmodules.validation.bean.rule;

import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.Conditions;

/**
 * An {@link AbstractValidationRule} that validates and checks that a string value
 * is not blank.
 *
 * @author Uri Boness
 */
public class NotBlankValidationRule extends AbstractValidationRule {

    public final static String DEFAULT_ERROR_CODE = "not.blank";

    /**
     * Constructs a new NotBlankValidationRule.
     */
    public NotBlankValidationRule() {
        super(DEFAULT_ERROR_CODE);
    }

    /**
     * Returns the condition of this validation rule.
     *
     * @see AbstractValidationRule#getCondition()
     */
    public Condition getCondition() {
        return Conditions.notBlank();
    }

}
