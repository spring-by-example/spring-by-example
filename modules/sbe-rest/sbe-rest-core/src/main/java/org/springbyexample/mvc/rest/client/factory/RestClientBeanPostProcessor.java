/*
 * Copyright 2007-2013 the original author or authors.
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
package org.springbyexample.mvc.rest.client.factory;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.mvc.bind.annotation.RestResource;
import org.springbyexample.mvc.method.annotation.RestServiceComponentProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Creates REST client beans instances from type and method-level
 * {@link RequestMapping @RequestMapping} annotations in
 * {@link @RestResource @RestResource} classes.
 *
 * @author David Winterfeldt
 */
public class RestClientBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private String marshallingServiceSuffix = "MarshallingService";
    private String clientSuffix = "Client";
    private String clientFactorySuffix = "ClientFactory";
    
    private List<String> basePackages;
    private RestServiceComponentProvider scanner;

    /**
     * Sets marshalling service suffix.
     */
    public void setMarshallingServiceSuffix(String marshallingServiceSuffix) {
        this.marshallingServiceSuffix = marshallingServiceSuffix;
    }

    /**
     * Sets client bean name suffix.
     */
    public void setClientSuffix(String clientSuffix) {
        this.clientSuffix = clientSuffix;
    }

    /**
     * Sets client factory bean name suffix.
     */
    public void setClientFactorySuffix(String clientFactorySuffix) {
        this.clientFactorySuffix = clientFactorySuffix;
    }

    /**
     * Sets base packages to scan for <code>RestResource</code>s.
     */
    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }
    
    /**
     * Sets scanner.
     */
    public void setScanner(RestServiceComponentProvider scanner) {
        this.scanner = scanner;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (String basePackage : basePackages) {
            Collection<BeanDefinition> components = scanner.findCandidateComponents(basePackage);
            
            for (final BeanDefinition definition : components) {
                final Class<?> marshallingServiceClass;
                
                try {
                    marshallingServiceClass = ClassUtils.forName(definition.getBeanClassName(), ClassUtils.getDefaultClassLoader());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Error loading interface class.", e);
                } catch (LinkageError e) {
                    throw new RuntimeException("Error loading interface class.", e);
                }
                
                RestResource restResource = AnnotationUtils.findAnnotation(marshallingServiceClass, RestResource.class);

                if (restResource.exportClient()) {
                    registerClient(registry, marshallingServiceClass);
                }
            }
        }
    }

    /**
     * Register client.
     */
    private void registerClient(BeanDefinitionRegistry registry, Class<?> marshallingServiceClass) {
        String packageName = marshallingServiceClass.getPackage().getName();
        String simpleName = marshallingServiceClass.getSimpleName();

        // strip off marshalling service suffix if used
        if (simpleName.endsWith(marshallingServiceSuffix)) {
            simpleName = simpleName.substring(0, (simpleName.length() - marshallingServiceSuffix.length()));
        }

        String beanName = packageName + "." + simpleName + clientSuffix;
        String factoryBeanName = packageName + "." + simpleName + clientFactorySuffix;

        // register client bean
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(marshallingServiceClass);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        beanDefinition.setFactoryMethodName("getBean");

        registry.registerBeanDefinition(beanName, beanDefinition);

        // register client factory bean
        GenericBeanDefinition factoryBeanDefinition = new GenericBeanDefinition();
        factoryBeanDefinition.setBeanClass(RestClientFactoryBean.class);
        factoryBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        MutablePropertyValues values = new MutablePropertyValues();
        values.addPropertyValue("marshallingServiceClass", marshallingServiceClass);
        
        factoryBeanDefinition.setPropertyValues(values);
        
        registry.registerBeanDefinition(factoryBeanName, factoryBeanDefinition);
    }
    
}
