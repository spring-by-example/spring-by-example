/*
 * Copyright 2007-2012 the original author or authors.
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

package org.springbyexample.util.log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * <p>Injects loggers into new bean instances based on   
 * fields marked with {@link AutowiredLogger} annotation.</p>
 * 
 * @author David Winterfeldt
 */
public class AnnotationLoggerBeanPostProcessor extends LoggerBeanPostProcessor {

    private Class<? extends Annotation> autowiredAnnotationType = AutowiredLogger.class;
    
    /**
     * Gets autowired logger annotation type.
     * Defaults to <code>AutowiredLogger</code>.
     */
    protected Class<? extends Annotation> getAutowiredAnnotationType() {
        return this.autowiredAnnotationType;
    }

    /**
     * Sets autowired logger annotation type.
     * Defaults to <code>AutowiredLogger</code>.
     */
    public void setAutowiredAnnotationType(Class<? extends Annotation> autowiredAnnotationType) {
        Assert.notNull(autowiredAnnotationType, "Autowired logger annotation type must not be null.");
        this.autowiredAnnotationType = autowiredAnnotationType;
    }

    /**
     * <p>Processes a beans fields for injection if it has a {@link AutowiredLogger} annotation.</p>
     * 
     * <p><strong>Note</strong>: Method name isn't used by this implementation.</p>
     */
    protected void processLogger(final Object bean, final String methodName) {
        // FIX ME: cache a classes inject data?
        
        final Class<?> clazz = bean.getClass();

        ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {
            public void doWith(Field field) {
                Annotation annotation = field.getAnnotation(getAutowiredAnnotationType());
                
                if (annotation != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        throw new IllegalStateException("Autowired annotation is not supported on static fields");
                    }
                    
                    ReflectionUtils.makeAccessible(field);
                    
                    Object logger = getLogger(bean.getClass().getName(),
                                              field.getType().getName());
                    
                    ReflectionUtils.setField(field, bean, logger);
                }
            }
        });
        
        ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
            public void doWith(Method method) {
                Annotation annotation = method.getAnnotation(getAutowiredAnnotationType());
                
                if (annotation != null) {
                    if (Modifier.isStatic(method.getModifiers())) {
                        throw new IllegalStateException("Autowired logger annotation is not supported on static methods");
                    }
                    
                    if (method.getParameterTypes().length == 0) {
                        throw new IllegalStateException("Autowired logger annotation requires at least one argument: " + method);
                    }
                    
                    injectMethod(bean, method);
                }
            }
        });
    }

}