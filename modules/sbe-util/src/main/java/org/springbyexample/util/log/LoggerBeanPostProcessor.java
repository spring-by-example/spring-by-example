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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils;

/**
 * <p>Injects loggers into new bean instances based on reflection.</p>
 * 
 * <p>Default logger factories configured are SLF4J, Apache Commons, 
 * Log4J, and JDK 1.4 Logging.</p>
 * 
 * @author Tim Voet
 * @author David Winterfeldt
 */
public class LoggerBeanPostProcessor implements BeanPostProcessor, Ordered {

    protected Map<String, String> hLoggerFactories = new HashMap<String, String>();
    
    protected String methodName = null;

    /**
     * Constructor
     */
    public LoggerBeanPostProcessor() {
        hLoggerFactories.put("org.slf4j.Logger", "org.slf4j.LoggerFactory.getLogger");
        hLoggerFactories.put("org.apache.commons.logging.Log", "org.apache.commons.logging.LogFactory.getLog");
        hLoggerFactories.put("org.apache.log4j.Logger", "org.apache.log4j.Logger.getLogger");
        hLoggerFactories.put("java.util.logging.Logger", "java.util.logging.Logger.getLogger");
    }
    
    /**
     * Sets target method name to set logger.
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    /**
     * Set logger factory <code>Map</code>.
     */
    public void setLoggerFactoryMap(Map<String, String> hLoggerFactories) {
        this.hLoggerFactories = hLoggerFactories;
    }

    /*
     * This method is used to execute before a bean's initialization callback.
     * 
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) 
            throws BeansException {
        processLogger(bean, methodName);

        return bean;
    }

    /**
     * This method is used to execute after a bean's initialization callback.
     * 
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
     */
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    /*
     * Get order for processing.
     * 
     * @see org.springframework.core.Ordered#getOrder()
     */
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * Instantiates bean specific loggers and sets them.
     */
    protected void processLogger(final Object bean, final String methodName) {
        final Class<?> clazz = bean.getClass();
        
        ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
            public void doWith(Method method) {
                if (method.getName().equals(methodName)) {
                    try {
                        injectMethod(bean, method);
                    } catch (Throwable e) {
                        throw new FatalBeanException("Problem injecting logger.  " + e.getMessage(), e);
                    }
                }
            }
        });    
    }
    
    /**
     * Processes a property descriptor to inject a logger.
     */
    public void injectMethod(Object bean, Method method) {
        PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
        
        if (pd != null) {
            String canonicalName = pd.getPropertyType().getCanonicalName();

            Object logger = getLogger(bean.getClass().getName(), canonicalName);

            if (logger != null) {
                try {                                                       
                    pd.getWriteMethod().invoke(bean, new Object[] { logger });
                } catch (Throwable e) {
                    throw new FatalBeanException("Problem injecting logger.  " + e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * Gets logger based on the logger name and type of 
     * logger (class name, ex: 'org.slf4j.Logger').
     */
    protected Object getLogger(String loggerName, String loggerType) {
        Object result = null;
        
        String staticMethod = hLoggerFactories.get(loggerType);

        if (staticMethod != null) {
            try {
                MethodInvokingFactoryBean factory = new MethodInvokingFactoryBean();
                factory.setStaticMethod(staticMethod);
                factory.setArguments(new Object[] { loggerName });
                factory.afterPropertiesSet();
                
                result = factory.getObject();
            } catch (Throwable e) {
                throw new FatalBeanException("Problem injecting logger.  " + e.getMessage(), e);
            }
        }
        
        return result;
    }
    
}