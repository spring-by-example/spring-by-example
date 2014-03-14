/*
 * Copyright 2004-2014 the original author or authors.
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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.BeanValidator;
import org.springmodules.validation.bean.conf.ValidationConfigurationException;
import org.springmodules.validation.bean.conf.loader.xml.DefaultValidationRuleElementHandlerRegistry;
import org.springmodules.validation.bean.conf.loader.xml.DefaultXmlBeanValidationConfigurationLoader;
import org.springmodules.validation.bean.conf.loader.xml.handler.ClassValidationElementHandler;
import org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler;
import org.springmodules.validation.util.io.FileIterator;
import org.springmodules.validation.util.xml.DomUtils;
import org.springmodules.validation.util.xml.SubElementsIterator;
import org.w3c.dom.Element;

/**
 * @author Uri Boness
 */
public class XmlBasedValidatorBeanDefinitionParser extends AbstractBeanDefinitionParser implements ValidationBeansParserConstants {

    final static String ELEMENT_NAME = "xml-based-validator";

    private final static String ERROR_CODE_CONVERTER_ATTR = "errorCodeConverter";

    private final static String CLASS_ATTR = "class";

    private final static String PATTERN_ATTR = "pattern";

    private final static String DIR_ATTR = "dir";

    private final static String LOCATION_ATTR = "location";

    private final static String RESOURCE_ELEMENT = "resource";

    private final static String RESOURCE_DIR_ELEMENT = "resource-dir";

    private final static String ELEMENT_HANDLERS_ELEMENT = "element-handlers";

    private final static String HANDLER_ELEMENT = "handler";

    private static final String HANDLER_REGISTRY_PREFIX = "__handler_registry_";

    private static final String CONFIGURATION_LOADER_PREFIX = "__configuration_loader_";

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {

        BeanDefinitionBuilder registryBuilder = BeanDefinitionBuilder.rootBeanDefinition(DefaultValidationRuleElementHandlerRegistry.class);
        parseHandlerElements(element, registryBuilder);
        AbstractBeanDefinition beanDefinition = registryBuilder.getBeanDefinition();
        String validatorId = resolveId(element, beanDefinition, parserContext);
        String registryId = HANDLER_REGISTRY_PREFIX + validatorId;
        parserContext.getRegistry().registerBeanDefinition(registryId, beanDefinition);

        String loaderId = CONFIGURATION_LOADER_PREFIX + validatorId;
        BeanDefinitionBuilder loaderBuilder = BeanDefinitionBuilder.rootBeanDefinition(DefaultXmlBeanValidationConfigurationLoader.class);
        parseResourcesElements(element, loaderBuilder);
        loaderBuilder.addPropertyReference("elementHandlerRegistry", registryId);
        parserContext.getRegistry().registerBeanDefinition(loaderId, loaderBuilder.getBeanDefinition());

        BeanDefinitionBuilder validatorBuilder = BeanDefinitionBuilder.rootBeanDefinition(BeanValidator.class);
        if (element.hasAttribute(ERROR_CODE_CONVERTER_ATTR)) {
            validatorBuilder.addPropertyReference("errorCodeConverter", element.getAttribute(ERROR_CODE_CONVERTER_ATTR));
        }
        validatorBuilder.addPropertyReference("configurationLoader", loaderId);

        return validatorBuilder.getBeanDefinition();
    }

    /**
     * Returns the {@link org.springmodules.validation.bean.BeanValidator} class.
     *
     * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#doParse(org.w3c.dom.Element, org.springframework.beans.factory.support.BeanDefinitionBuilder)
     */
    protected Class getBeanClass(Element element) {
        return BeanValidator.class;
    }

    //=============================================== Helper Methods ===================================================

    protected void parseResourcesElements(Element element, BeanDefinitionBuilder loaderBuilder) {
        List resources = new ArrayList();
        for (Iterator subElements = new SubElementsIterator(element); subElements.hasNext();) {
            Element subElement = (Element) subElements.next();
            if (subElement.getLocalName().equals(RESOURCE_ELEMENT)) {
                resources.add(createResource(subElement));
            } else if (subElement.getLocalName().equals(RESOURCE_DIR_ELEMENT)) {
                resources.addAll(createResources(subElement));
            }
        }
        loaderBuilder.addPropertyValue("resources", resources.toArray(new Resource[resources.size()]));
    }

    protected Resource createResource(Element resourceDefinition) {
        String path = resourceDefinition.getAttribute(LOCATION_ATTR);
        if (!StringUtils.hasText(path)) {
            throw new ValidationConfigurationException("Resoruce path is required and cannot be empty");
        }
        return new DefaultResourceLoader().getResource(path);
    }

    protected List createResources(Element resourcesDefinition) {
        String dirName = resourcesDefinition.getAttribute(DIR_ATTR);
        final String pattern = resourcesDefinition.getAttribute(PATTERN_ATTR);
        final AntPathMatcher matcher = new AntPathMatcher();
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return matcher.match(pattern, file.getName());
            }
        };
        List resources = new ArrayList();
        for (Iterator files = new FileIterator(dirName, filter); files.hasNext();) {
            File file = (File) files.next();
            resources.add(new FileSystemResource(file));
        }
        return resources;
    }

    protected void parseHandlerElements(Element element, BeanDefinitionBuilder registryBuilder) {
        Element functionsElement = DomUtils.getSingleSubElement(element, VALIDATION_BEANS_NAMESPACE, ELEMENT_HANDLERS_ELEMENT);
        if (functionsElement == null) {
            return;
        }

        Iterator handlerElements = new SubElementsIterator(functionsElement, VALIDATION_BEANS_NAMESPACE, HANDLER_ELEMENT);
        List propertyHandlers = new ArrayList();
        List classHandlers = new ArrayList();
        while (handlerElements.hasNext()) {
            Element handlerElement = (Element) handlerElements.next();
            String className = handlerElement.getAttribute(CLASS_ATTR);
            Object handler = loadAndInstantiate(className);
            if (PropertyValidationElementHandler.class.isInstance(handler)) {
                propertyHandlers.add(handler);
            } else if (ClassValidationElementHandler.class.isInstance(handler)) {
                classHandlers.add(handler);
            } else {
                throw new ValidationConfigurationException("class '" + className + "' is not a property hanlder nor a class handler");
            }
        }
        registryBuilder.addPropertyValue(
            "extraPropertyHandlers",
            propertyHandlers.toArray(new PropertyValidationElementHandler[propertyHandlers.size()])
        );
        registryBuilder.addPropertyValue(
            "extraClassHandlers",
            classHandlers.toArray(new ClassValidationElementHandler[classHandlers.size()])
        );
    }

    //=============================================== Helper Methods ===================================================

    /**
     * Loads and instantiates the given class.
     *
     * @param className The name of the given class.
     */
    protected Object loadAndInstantiate(String className) {
        Class<?> clazz;
        try {
            clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
            return clazz.newInstance();
        } catch (ClassNotFoundException cnfe) {
            throw new ValidationConfigurationException("Could not load class '" + className + "'", cnfe);
        } catch (IllegalAccessException iae) {
            throw new ValidationConfigurationException("Could not instantiate class '" + className + "'", iae);
        } catch (InstantiationException ie) {
            throw new ValidationConfigurationException("Could not instantiate class '" + className + "'", ie);
        }
    }
}
