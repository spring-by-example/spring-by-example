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

import java.beans.PropertyDescriptor;
import java.util.Calendar;
import java.util.Date;

import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.DateInTheFutureValidationRule;
import org.w3c.dom.Element;

/**
 * An {@link AbstractPropertyValidationElementHandler} implementation that can handle an element that represents a "date in
 * the future" validation rule - a rule that validates that a given date occured in the future.
 *
 * @author Uri Boness
 */
public class DateInFutureRuleElementHandler extends AbstractPropertyValidationElementHandler {

    private static final String ELEMENT_NAME = "in-future";

    /**
     * Constructs a new DateInFutureRuleElementHandler.
     */
    public DateInFutureRuleElementHandler(String namespaceUri) {
        super(ELEMENT_NAME, namespaceUri);
    }

    /**
     * In addition to the element name and namespace check, this handler only support properties of types
     * {@link java.util.Date} and {@link java.util.Calendar}.
     *
     * @see org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler#supports(org.w3c.dom.Element, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Element element, Class clazz, PropertyDescriptor descriptor) {
        return super.supports(element, clazz, descriptor)
            &&
            (
                Date.class.isAssignableFrom(descriptor.getPropertyType())
                    ||
                    Calendar.class.isAssignableFrom(descriptor.getPropertyType())
            );
    }

    protected AbstractValidationRule createValidationRule(Element element) {
        return new DateInTheFutureValidationRule();
    }

}
