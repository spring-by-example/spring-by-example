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

import java.math.BigDecimal;

import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.ValidationConfigurationException;
import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.MaxValidationRule;
import org.springmodules.validation.bean.rule.MinValidationRule;
import org.springmodules.validation.bean.rule.RangeValidationRule;
import org.w3c.dom.Element;

/**
 * An {@link AbstractPropertyValidationElementHandler} implementation that can handle an element that represent a range
 * validation rule, min validation rule, or max validation rule.
 *
 * @author Uri Boness
 */
public class RangeRuleElementHandler extends AbstractPropertyValidationElementHandler {

    /**
     * The defult error code for the min validation rule.
     */
    public static final String DEFAULT_MIN_ERROR_CODE = "range";

    /**
     * The defult error code for the max validation rule.
     */
    public static final String DEFAULT_MAX_ERROR_CODE = "range";

    private static final String ELEMENT_NAME = "range";

    private static final String MIN_ATTR = "min";

    private static final String MAX_ATTR = "max";

    /**
     * Constructs a new RangeRuleElementHandler.
     */
    public RangeRuleElementHandler(String namespaceUri) {
        super(ELEMENT_NAME, namespaceUri);
    }

    protected AbstractValidationRule createValidationRule(Element element) {

        String minText = element.getAttribute(MIN_ATTR);
        String maxText = element.getAttribute(MAX_ATTR);

        BigDecimal min = (StringUtils.hasText(minText)) ? new BigDecimal(minText) : null;
        BigDecimal max = (StringUtils.hasText(maxText)) ? new BigDecimal(maxText) : null;

        if (min != null && max != null) {
            return new RangeValidationRule(min, max);
        }

        if (min != null) {
            return new MinValidationRule(min);
        }

        if (max != null) {
            return new MaxValidationRule(max);
        }

        throw new ValidationConfigurationException("Element '" + ELEMENT_NAME +
            "' must have either 'min' attribute, 'max' attribute, or both");
    }

}
