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

package org.springmodules.validation.bean.conf.loader.xml.handler.jodatime;

import java.beans.PropertyDescriptor;

import org.joda.time.Instant;
import org.springmodules.validation.bean.conf.loader.xml.handler.AbstractPropertyValidationElementHandler;
import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.InstantInThePastValidationRule;
import org.w3c.dom.Element;

/**
 * An {@link AbstractPropertyValidationElementHandler} implementation that can handle an element that represents an "in the
 * past" joda-time instant validation rule.
 *
 * @author Uri Boness
 */
public class InstantInPastRuleElementHandler extends AbstractPropertyValidationElementHandler {

    private static final String ELEMENT_NAME = "in-past";

    /**
     * Constructs a new InstantInPastRuleElementHandler.
     */
    public InstantInPastRuleElementHandler(String namespaceUri) {
        super(ELEMENT_NAME, namespaceUri);
    }

    /**
     * In addition to the element name and namespace check, this handler only support properties of type
     * {@link org.joda.time.Instant}.
     *
     * @see org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler#supports(org.w3c.dom.Element, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Element element, Class clazz, PropertyDescriptor descriptor) {
        return super.supports(element, clazz, descriptor) && Instant.class.isAssignableFrom(descriptor.getPropertyType());
    }

    protected AbstractValidationRule createValidationRule(Element element) {
        return new InstantInThePastValidationRule();
    }

}
