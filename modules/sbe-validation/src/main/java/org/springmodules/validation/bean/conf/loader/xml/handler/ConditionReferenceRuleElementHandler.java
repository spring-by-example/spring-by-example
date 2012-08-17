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

package org.springmodules.validation.bean.conf.loader.xml.handler;

import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.ConditionReferenceValidationRule;
import org.w3c.dom.Element;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;

/**
 * An {@link org.springmodules.validation.bean.conf.loader.xml.handler.AbstractPropertyValidationElementHandler} that
 * can handle {@link org.springmodules.validation.bean.conf.loader.annotation.handler.ConditionRef} xml element and
 * create the appropriate property validation rule.
 *
 * @author Uri Boness
 */
public class ConditionReferenceRuleElementHandler extends AbstractPropertyValidationElementHandler implements ApplicationContextAware {

    private static final String ELEMENT_NAME = "condition-ref";

    private static final String NAME_ATTR = "name";

    private ApplicationContext applicationContext;

    public ConditionReferenceRuleElementHandler(String namespace) {
        super(ELEMENT_NAME, namespace);
    }

    protected AbstractValidationRule createValidationRule(Element element) {
        if (applicationContext == null) {
            throw new UnsupportedOperationException("This handler can only be used when deployed within a " +
                "spring application context");
        }
        String beanName = element.getAttribute(NAME_ATTR);
        return new ConditionReferenceValidationRule(beanName, applicationContext);
    }

    //================================= Interfacet: ApplicationContextAware ============================================

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
