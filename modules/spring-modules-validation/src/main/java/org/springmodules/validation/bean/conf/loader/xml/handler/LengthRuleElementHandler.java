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
import org.springmodules.validation.bean.rule.LengthValidationRule;
import org.springmodules.validation.bean.rule.MaxLengthValidationRule;
import org.springmodules.validation.bean.rule.MinLengthValidationRule;
import org.w3c.dom.Element;

/**
 * An {@link AbstractPropertyValidationElementHandler} that can handle an element that represents a length range validation rule,
 * min length validation rule, or max length vaidation rule.
 *
 * @author Uri Boness
 */
public class LengthRuleElementHandler extends AbstractPropertyValidationElementHandler {

    private static final String ELEMENT_NAME = "length";

    private static final String MIN_ATTR = "min";

    private static final String MAX_ATTR = "max";

    /**
     * Constructs a new LengthRuleElementHandler.
     */
    public LengthRuleElementHandler(String namespaceUri) {
        super(ELEMENT_NAME, namespaceUri);
    }

    protected AbstractValidationRule createValidationRule(Element element) {

        String minText = element.getAttribute(MIN_ATTR);
        String maxText = element.getAttribute(MAX_ATTR);

        Integer min = (StringUtils.hasText(minText)) ? new Integer(minText) : null;
        Integer max = (StringUtils.hasText(maxText)) ? new Integer(maxText) : null;

        if (min != null && max != null) {
            return new LengthValidationRule(min.intValue(), max.intValue());
        }

        if (min != null) {
            return new MinLengthValidationRule(min.intValue());
        }

        if (max != null) {
            return new MaxLengthValidationRule(max.intValue());
        }

        throw new ValidationConfigurationException("Element '" + ELEMENT_NAME +
            "' must have either 'min' attribute, 'max' attribute, or both");
    }

}
