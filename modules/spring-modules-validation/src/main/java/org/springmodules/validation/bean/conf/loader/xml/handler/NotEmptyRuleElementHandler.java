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

import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.NotEmptyValidationRule;
import org.w3c.dom.Element;

/**
 * An {@link AbstractPropertyValidationElementHandler} implementation that can handle an element that represents a "not empty"
 * validation rule (validation rule that validates that a collection/array is not empty).
 *
 * @author Uri Boness
 */
public class NotEmptyRuleElementHandler extends AbstractPropertyValidationElementHandler {

    private static final String ELEMENT_NAME = "not-empty";

    /**
     * Constructs a new NotEmptyRuleElementHandler.
     */
    public NotEmptyRuleElementHandler(String namespaceUri) {
        super(ELEMENT_NAME, namespaceUri);
    }

    protected AbstractValidationRule createValidationRule(Element element) {
        return new NotEmptyValidationRule();
    }

}
