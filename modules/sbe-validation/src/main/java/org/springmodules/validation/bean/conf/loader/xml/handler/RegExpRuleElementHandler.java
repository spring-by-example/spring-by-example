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
import org.springmodules.validation.bean.rule.RegExpValidationRule;
import org.w3c.dom.Element;

/**
 * An {@link AbstractPropertyValidationElementHandler} implementation that knows how to handle elements that represent
 * regular expression validation rules.
 *
 * @author Uri Boness
 */
public class RegExpRuleElementHandler extends AbstractPropertyValidationElementHandler {

    private static final String ELEMENT_NAME = "regexp";

    private static final String EXPRESSION_ATTR = "expression";

    /**
     * Constructs a new RegExpRuleElementHandler.
     */
    public RegExpRuleElementHandler(String namespaceUri) {
        super(ELEMENT_NAME, namespaceUri);
    }

    protected AbstractValidationRule createValidationRule(Element element) {
        String expression = element.getAttribute(EXPRESSION_ATTR);
        if (!StringUtils.hasText(expression)) {
            throw new ValidationConfigurationException("Element '" + ELEMENT_NAME + "' must have an 'expression' attribute");
        }
        return new RegExpValidationRule(expression);
    }

}
