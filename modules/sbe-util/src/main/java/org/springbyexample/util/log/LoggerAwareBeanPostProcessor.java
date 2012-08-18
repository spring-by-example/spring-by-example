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

import org.springframework.beans.BeansException;

/**
 * <p>Injects loggers into new bean instances based on the different 
 * <code>LoggerAware</code> interfaces.</p>
 * 
 * @author David Winterfeldt
 */
public class LoggerAwareBeanPostProcessor extends LoggerBeanPostProcessor {

    protected final static String LOGGER_AWARE_METHOD_NAME = "setLogger";

    /*
     * This method is used to execute before a bean's initialization callback.
     * 
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) 
            throws BeansException {
        if (bean instanceof LoggerAware<?>) {
            processLogger(bean, LOGGER_AWARE_METHOD_NAME);
        }

        return bean;
    }

}