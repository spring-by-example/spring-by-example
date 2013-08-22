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
package org.springbyexample.mvc.method.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.converter.ListConverter;
import org.springbyexample.mvc.bind.annotation.RestRequestResource;
import org.springbyexample.mvc.bind.annotation.RestResource;
import org.springbyexample.mvc.converter.handler.ConverterHandlerInfo;
import org.springbyexample.mvc.converter.handler.ConverterHandlerInterceptor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.HandlerMethodSelector;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


/**
 * Creates {@link RequestMappingInfo} instances from type and method-level
 * {@link RequestMapping @RequestMapping} annotations in
 * {@link @RestResource @RestResource} classes.
 *
 * @author David Winterfeldt
 */
public class ServiceHandlerMapping extends RequestMappingHandlerMapping {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private List<String> basePackages;
    private RestServiceComponentProvider scanner;
    private ConverterHandlerInfo converterHandlerInfo;

    // temporary variable used to store the method
    // while registerHandlerMethod is called and overridden createHandlerMethod is called from it
    private Method restBridgedMethod;

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

    /**
     * Sets converter handler info.
     */
    public void setConverterHandlerInfo(ConverterHandlerInfo converterHandlerInfo) {
        Assert.notNull(converterHandlerInfo);
        Assert.notNull(converterHandlerInfo.getPropertyName());

        this.converterHandlerInfo = converterHandlerInfo;
    }

    @Override
    protected void initHandlerMethods() {
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

                Set<Method> methods = HandlerMethodSelector.selectMethods(marshallingServiceClass, new MethodFilter() {
                    @Override
                    public boolean matches(Method method) {
                        return (getMappingForMethod(method, marshallingServiceClass) != null);
                    }
                });

                for (Method method : methods) {
                    registerHandler(marshallingServiceClass, method);
                }
            }
        }

        initInterceptors();
        handlerMethodsInitialized(getHandlerMethods());
    }

    /**
     * Register handler.
     */
    private void registerHandler(Class<?> marshallingServiceClass, Method method) {
        ApplicationContext ctx = getApplicationContext();

        RestResource restResource = AnnotationUtils.findAnnotation(marshallingServiceClass, RestResource.class);
        Class<?> serviceClass = restResource.service();

        RestRequestResource restRequestResource = AnnotationUtils.findAnnotation(method, RestRequestResource.class);
        boolean export = (restRequestResource != null ? restRequestResource.export() : true);
        boolean relative = (restRequestResource != null ? restRequestResource.relative() : true);

        if (export) {
            Class<?> handlerServiceClass = serviceClass;
            String methodName = method.getName();
            Class<?>[] paramTypes = method.getParameterTypes();

            if (restRequestResource != null) {
                // explicit service specified
                if (restRequestResource.service() != ServiceValueConstants.DEFAULT_SERVICE_CLASS) {
                    handlerServiceClass = restRequestResource.service();
                }

                // explicit method name specified
                if (StringUtils.hasText(restRequestResource.methodName())) {
                    methodName = restRequestResource.methodName();
                }
            }

            Object handler = ctx.getBean(handlerServiceClass);
            Method serviceMethod = ClassUtils.getMethod(handlerServiceClass, methodName, paramTypes);
            RequestMappingInfo mapping = getMappingForMethod(method, marshallingServiceClass);

            if (relative) {
                List<String> patterns = new ArrayList<String>();

                for (String pattern : mapping.getPatternsCondition().getPatterns()) {
                    // add REST resource path prefix to URI,
                    // if relative path is just '/' add an empty string
                    patterns.add(restResource.path() + (!"/".equals(pattern) ? pattern : ""));
                }

                // create a new mapping based on the patterns (patterns are unmodifiable in existing RequestMappingInfo)
                mapping = new RequestMappingInfo(
                            new PatternsRequestCondition(patterns.toArray(ArrayUtils.EMPTY_STRING_ARRAY),
                                                         getUrlPathHelper(), getPathMatcher(),
                                                         useSuffixPatternMatch(), useTrailingSlashMatch()),
                            mapping.getMethodsCondition(), mapping.getParamsCondition(), mapping.getHeadersCondition(),
                            mapping.getConsumesCondition(), mapping.getProducesCondition(), mapping.getCustomCondition());
            }

            // need to set param types to use in createHandlerMethod before calling registerHandlerMethod
            restBridgedMethod = BridgeMethodResolver.findBridgedMethod(method);

            // mapping is key, HandlerMethod is value
            registerHandlerMethod(handler, serviceMethod, mapping);

            processConverters(restRequestResource, mapping, serviceMethod);
        }
    }

    @Override
    protected HandlerMethod createHandlerMethod(Object handler, Method method) {
        HandlerMethod handlerMethod = super.createHandlerMethod(handler, method);

        if (restBridgedMethod != null) {
            handlerMethod = new ServiceHandlerMethod(handler, method, restBridgedMethod);
        }

        return handlerMethod;
    }

    /**
     * Process and setup any converter handlers if one is configured on <code>RestRequestResource</code>.
     */
    private void processConverters(RestRequestResource restRequestResource, RequestMappingInfo mapping,
                                   Method serviceMethod) {
        ApplicationContext ctx = getApplicationContext();
        Class<?> converterClass = (restRequestResource != null ? restRequestResource.converter() : null);

        if (converterClass != null && converterClass != ServiceValueConstants.DEFAULT_CONVERTER_CLASS) {
            @SuppressWarnings("rawtypes")
            ListConverter converter = (ListConverter) ctx.getBean(converterClass);

            String[] pathPatterns = mapping.getPatternsCondition().getPatterns().toArray(ArrayUtils.EMPTY_STRING_ARRAY);

            String methodSuffix = StringUtils.capitalize(converterHandlerInfo.getPropertyName());
            String getterMethodName = "get" + methodSuffix;
            final String setterMethodName = "set" + methodSuffix;

            final Class<?> returnTypeClass = serviceMethod.getReturnType();
            Method getResultsMethod = ReflectionUtils.findMethod(returnTypeClass, getterMethodName);
            final Class<?> resultReturnTypeClass = getResultsMethod.getReturnType();
            Method setResultsMethod = ReflectionUtils.findMethod(returnTypeClass, setterMethodName, resultReturnTypeClass);
            final AtomicReference<Method> altSetResultsMethod = new AtomicReference<Method>();

            // issue with ReflectionUtils, setterResultsMethod sometimes null from the command line (not getter?)
            if (setResultsMethod == null) {
                ReflectionUtils.doWithMethods(returnTypeClass, new MethodCallback() {
                    @Override
                    public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                        if (setterMethodName.equals(method.getName())) {
                            altSetResultsMethod.set(method);
                        logger.debug("Unable to use ReflectionUtils to find setter. returnTypeClass={}  method={} resultReturnTypeClass={}",
                                new Object[] { returnTypeClass, method, resultReturnTypeClass });
                        }
                    }
                });
            }

            HandlerInterceptor interceptor = new ConverterHandlerInterceptor(converter, returnTypeClass,
                    getResultsMethod, (setResultsMethod != null ? setResultsMethod : altSetResultsMethod.get()));

            MappedInterceptor mappedInterceptor = new MappedInterceptor(pathPatterns, interceptor);
            setInterceptors(new Object[]{ mappedInterceptor });

            logger.info("Registered converter post handler for {} with {}.", pathPatterns, converterClass.getCanonicalName());
        }
    }

}
