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

package org.springmodules.validation.bean.conf.namespace;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.ValidationConfigurationException;
import org.springmodules.validation.util.cel.valang.ValangConditionExpressionParser;
import org.springmodules.validation.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * The {@link AbstractBeanDefinitionParser} to parser &lt;valang-condition-parser&gt; elements.
 *
 * @author Uri Boness
 */
public class ValangConditionParserDefinitionParser extends AbstractBeanDefinitionParser implements ValidationBeansParserConstants {

    final static String ELEMENT_NAME = "valang-condition-parser";

    private final static String FUNCTION_ELEMENT = "function";

    private final static String NAME_ATTR = "name";

    private final static String CLASS_ATTR = "class";

    private final static String DATE_PARSER_ELEMENT = "date-parser";

    private final static String REGEXP_ATTR = "regexp";

    private final static String PATTERN_ATTR = "pattern";

    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ValangConditionExpressionParser.class);

        Map functionByName = new HashMap();
        Map dateParsers = new HashMap();

        for (Iterator elements = DomUtils.childElements(element); elements.hasNext();) {
            Element child = (Element) elements.next();
            if (isFunctionDefinition(child)) {
                registerFunction(child, functionByName);
            } else if (isDateParserDefinition(child)) {
                registerDateParser(child, dateParsers);
            } else {
                throw new ValidationConfigurationException("unknown element '" + child.getTagName() + "'");
            }
        }

        builder.addPropertyValue("customFunctions", functionByName);
        builder.addPropertyValue("dateParsers", dateParsers);

        return builder.getBeanDefinition();
    }

    //=============================================== Helper Methods ===================================================

    protected boolean isFunctionDefinition(Element element) {
        return VALIDATION_BEANS_NAMESPACE.equals(element.getNamespaceURI()) &&
            FUNCTION_ELEMENT.equals(element.getLocalName());
    }

    protected void registerFunction(Element functionDefinition, Map functionByName) {
        String name = functionDefinition.getAttribute(NAME_ATTR);
        String className = functionDefinition.getAttribute(CLASS_ATTR);
        if (!StringUtils.hasText(name) || !StringUtils.hasText(className)) {
            throw new ValidationConfigurationException("Both '" + NAME_ATTR + "' and '" + CLASS_ATTR +
                "' attributes of element '" + FUNCTION_ELEMENT + "' are required");
        }
        functionByName.put(name, className);
    }

    protected boolean isDateParserDefinition(Element element) {
        return VALIDATION_BEANS_NAMESPACE.equals(element.getNamespaceURI()) &&
            DATE_PARSER_ELEMENT.equals(element.getLocalName());
    }

    protected void registerDateParser(Element dateParserDefinition, Map patternByRegexp) {
        String regexp = dateParserDefinition.getAttribute(REGEXP_ATTR);
        String pattern = dateParserDefinition.getAttribute(PATTERN_ATTR);
        if (!StringUtils.hasText(regexp) || !StringUtils.hasText(pattern)) {
            throw new ValidationConfigurationException("Both '" + REGEXP_ATTR + "' and '" +
                PATTERN_ATTR + "' attributes of element '" + DATE_PARSER_ELEMENT + "' are required");
        }
        patternByRegexp.put(regexp, pattern);
    }

}
