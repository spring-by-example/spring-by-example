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

package org.springmodules.validation.bean.conf.loader.xml;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.validation.bean.conf.loader.xml.handler.ClassValidationElementHandler;
import org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler;
import org.springmodules.validation.util.cel.ConditionExpressionBased;
import org.springmodules.validation.util.cel.ConditionExpressionParser;
import org.springmodules.validation.util.cel.valang.ValangConditionExpressionParser;
import org.springmodules.validation.util.context.BasicContextAware;
import org.springmodules.validation.util.fel.FunctionExpressionBased;
import org.springmodules.validation.util.fel.FunctionExpressionParser;
import org.springmodules.validation.util.fel.parser.ValangFunctionExpressionParser;
import org.w3c.dom.Element;

/**
 * A simple implementation of {@link ValidationRuleElementHandlerRegistry} that enables registration of element
 * handlers.
 *
 * @author Uri Boness
 */
public class SimpleValidationRuleElementHandlerRegistry extends BasicContextAware
    implements ValidationRuleElementHandlerRegistry, InitializingBean, ConditionExpressionBased, FunctionExpressionBased {

    private final static Logger logger = LoggerFactory.getLogger(SimpleValidationRuleElementHandlerRegistry.class);

    private List classHandlers;

    private List propertyHandlers;

    private boolean functoinExpressionParserSet = false;

    private FunctionExpressionParser functionExpressionParser;

    private boolean conditionExpressionParserSet = false;

    private ConditionExpressionParser conditionExpressionParser;

    /**
     * Constructs a new DefaultValidationRuleElementHandlerRegistry with the default handlers.
     */
    public SimpleValidationRuleElementHandlerRegistry() {
        classHandlers = new ArrayList();
        propertyHandlers = new ArrayList();
        functionExpressionParser = new ValangFunctionExpressionParser();
        conditionExpressionParser = new ValangConditionExpressionParser();
    }

    /**
     * Registers the given class handler with this registry. The registered handler is registered in such a way that it will
     * be checked first for support (LIFC - Last In First Checked).
     */
    public void registerClassHandler(ClassValidationElementHandler handler) {
        classHandlers.add(0, handler);
    }

    /**
     * @see org.springmodules.validation.bean.conf.loader.xml.ValidationRuleElementHandlerRegistry#findClassHandler(org.w3c.dom.Element, Class)
     */
    public ClassValidationElementHandler findClassHandler(Element element, Class clazz) {
        for (Iterator iter = classHandlers.iterator(); iter.hasNext();) {
            ClassValidationElementHandler handler = (ClassValidationElementHandler) iter.next();
            if (handler.supports(element, clazz)) {
                return handler;
            }
        }
        return null;
    }

    /**
     * Registers the given property handler with this registry. The registered handler is registered in such a way that
     * it will be checked first for support. (LIFC - Last In First Checked).
     */
    public void registerPropertyHandler(PropertyValidationElementHandler handler) {
        propertyHandlers.add(0, handler);
    }

    /**
     * @see org.springmodules.validation.bean.conf.loader.xml.ValidationRuleElementHandlerRegistry#findPropertyHandler(org.w3c.dom.Element, Class, java.beans.PropertyDescriptor)
     */
    public PropertyValidationElementHandler findPropertyHandler(Element element, Class clazz, PropertyDescriptor descriptor) {
        for (Iterator iter = propertyHandlers.iterator(); iter.hasNext();) {
            PropertyValidationElementHandler handler = (PropertyValidationElementHandler) iter.next();
            if (handler.supports(element, clazz, descriptor)) {
                return handler;
            }
        }
        return null;
    }

    public void afterPropertiesSet() throws Exception {

        findConditionExpressionParserInApplicationContext();
        findFunctionExpressionParserInApplicationContext();

        for (Iterator iter = classHandlers.iterator(); iter.hasNext();) {
            ClassValidationElementHandler handler = (ClassValidationElementHandler) iter.next();
            setExpressionParsers(handler);
            initLifecycle(handler);
        }

        for (Iterator iter = propertyHandlers.iterator(); iter.hasNext();) {
            PropertyValidationElementHandler handler = (PropertyValidationElementHandler) iter.next();
            setExpressionParsers(handler);
            initLifecycle(handler);
        }
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Registeres the given class handlers with this registry.
     *
     * @param handlers The handlers to register with this registry.
     */
    public void setExtraClassHandlers(ClassValidationElementHandler[] handlers) {
        for (int i = handlers.length - 1; i >= 0; i--) {
            registerClassHandler(handlers[i]);
        }
    }

    /**
     * Resets the class handlers in this registry with the given ones.
     *
     * @param handlers The class handlers to be registered with this registry.
     */
    public void setClassHandlers(ClassValidationElementHandler[] handlers) {
        classHandlers.clear();
        setExtraClassHandlers(handlers);
    }

    /**
     * Registeres the given property handlers with this registry.
     *
     * @param handlers The handlers to register with this registry.
     */
    public void setExtraPropertyHandlers(PropertyValidationElementHandler[] handlers) {
        for (int i = handlers.length - 1; i >= 0; i--) {
            registerPropertyHandler(handlers[i]);
        }
    }

    /**
     * Resets the property handlers in this registry to the given ones (overriding the existing ones).
     *
     * @param handlers The property handlers to register with this registry.
     */
    public void setPropertyHandlers(PropertyValidationElementHandler[] handlers) {
        propertyHandlers.clear();
        setExtraPropertyHandlers(handlers);
    }

    /**
     * Return all class handlers that are registered with this registry.
     *
     * @return All class handlers that are registered with this registry.
     */
    public ClassValidationElementHandler[] getClassHandlers() {
        return (ClassValidationElementHandler[]) classHandlers.toArray(new ClassValidationElementHandler[classHandlers.size()]);
    }

    /**
     * Return all property handlers that are registered with this registry.
     *
     * @return All property handlers that are registered with this registry.
     */
    public PropertyValidationElementHandler[] getPropertyHandlers() {
        return (PropertyValidationElementHandler[]) propertyHandlers.toArray(new PropertyValidationElementHandler[propertyHandlers.size()]);
    }

    /**
     * @see org.springmodules.validation.util.fel.FunctionExpressionBased#setFunctionExpressionParser(org.springmodules.validation.util.fel.FunctionExpressionParser)
     */
    public void setFunctionExpressionParser(FunctionExpressionParser functionExpressionParser) {
        this.functoinExpressionParserSet = true;
        this.functionExpressionParser = functionExpressionParser;
    }

    /**
     * @see org.springmodules.validation.util.cel.ConditionExpressionBased#setConditionExpressionParser(org.springmodules.validation.util.cel.ConditionExpressionParser)
     */
    public void setConditionExpressionParser(ConditionExpressionParser conditionExpressionParser) {
        this.conditionExpressionParserSet = true;
        this.conditionExpressionParser = conditionExpressionParser;
    }

    //=============================================== Helper Methods ===================================================

    protected void setExpressionParsers(Object object) {
        if (ConditionExpressionBased.class.isInstance(object) && conditionExpressionParser != null) {
            ((ConditionExpressionBased) object).setConditionExpressionParser(conditionExpressionParser);
        }
        if (FunctionExpressionBased.class.isInstance(object) && functionExpressionParser != null) {
            ((FunctionExpressionBased) object).setFunctionExpressionParser(functionExpressionParser);
        }
    }

    protected void findConditionExpressionParserInApplicationContext() {
        if (conditionExpressionParserSet) {
            return;
        }
        ConditionExpressionParser parser = (ConditionExpressionParser) findObjectInApplicationContext(ConditionExpressionParser.class);
        if (parser == null) {
            return;
        }
        conditionExpressionParser = parser;
    }

    protected void findFunctionExpressionParserInApplicationContext() {
        if (functoinExpressionParserSet) {
            return;
        }
        FunctionExpressionParser parser = (FunctionExpressionParser) findObjectInApplicationContext(FunctionExpressionParser.class);
        if (parser == null) {
            return;
        }
        functionExpressionParser = parser;
    }

    protected Object findObjectInApplicationContext(Class clazz) {
        if (applicationContext == null) {
            return null;
        }
        String[] names = applicationContext.getBeanNamesForType(clazz);
        if (names.length == 0) {
            return null;
        }
        if (names.length > 1) {
            logger.warn("Multiple bean of type '" + clazz.getName() + "' are defined in the application context." +
                "Only the first encountered one will be used");
        }
        return applicationContext.getBean(names[0]);
    }

}
