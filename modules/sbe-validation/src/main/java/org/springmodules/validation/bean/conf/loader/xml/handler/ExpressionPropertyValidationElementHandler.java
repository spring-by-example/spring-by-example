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

package org.springmodules.validation.bean.conf.loader.xml.handler;

import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.ValidationConfigurationException;
import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.ExpressionValidationRule;
import org.w3c.dom.Element;

/**
 * An {@link AbstractPropertyValidationElementHandler} that can handle elements that represent valang validation rules.
 * This handler creates a valang conditoin out of a &lt;valang&gt; element.
 *
 * @author Uri Boness
 */
public class ExpressionPropertyValidationElementHandler extends AbstractPropertyValidationElementHandler {

    private static final String ELEMENT_NAME = "expression";

    private static final String CONDITION_ATTR = "condition";

    private static final String SCOPE_ATTR = "scope";

    private static final String PROPERTY_SCOPE_VALUE = "property";

    private static final String GLOBAL_SCOPE_VALUE = "global";

    /**
     * Constructs a new ExpressionPropertyValidationElementHandler.
     */
    public ExpressionPropertyValidationElementHandler(String namespaceUri) {
        super(ELEMENT_NAME, namespaceUri);
    }

    protected AbstractValidationRule createValidationRule(Element element) {
        String expression = element.getAttribute(CONDITION_ATTR);
        if (!StringUtils.hasText(expression)) {
            throw new ValidationConfigurationException("Element '" + ELEMENT_NAME + "' must have a '" + CONDITION_ATTR + "' attribute");
        }
        return new ExpressionValidationRule(getConditionExpressionParser(), expression);
    }

    public boolean isConditionGloballyScoped(Element element) {
        if (!element.hasAttribute(SCOPE_ATTR)) {
            return true;
        }
        String value = element.getAttribute(SCOPE_ATTR);
        if (GLOBAL_SCOPE_VALUE.equals(value)) {
            return true;
        }
        if (PROPERTY_SCOPE_VALUE.equals(value)) {
            return false;
        }
        throw new ValidationConfigurationException("Unknown value '" + value + "' for attribute '" + SCOPE_ATTR + "'");
    }

}
