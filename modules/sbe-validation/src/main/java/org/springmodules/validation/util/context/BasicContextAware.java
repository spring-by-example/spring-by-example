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

package org.springmodules.validation.util.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Uri Boness
 */
public class BasicContextAware implements ContextAware {

    protected ApplicationContext applicationContext = null;
    protected BeanFactory beanFactory = null;
    protected ResourceLoader resourceLoader = null;
    protected MessageSource messageSource = null;
    protected ApplicationEventPublisher applicationEventPublisher = null;

    /**
     * Implementation of <code>BeanFactoryAware</code>.
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Implementation of <code>ApplicationContextAware</code>.
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Implementation of <code>ResourceLoaderAware</code>.
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Implementation of <code>MessageSourceAware</code>.
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Implementation of <code>ApplicationEventPublisherAware</code>.
     */
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    protected void initLifecycle(BasicContextAware object) {
        object.setBeanFactory(beanFactory);
        object.setApplicationContext(applicationContext);
        object.setResourceLoader(resourceLoader);
        object.setMessageSource(messageSource);
        object.setApplicationEventPublisher(applicationEventPublisher);
    }

    protected void initLifecycle(Object object) {
        if (object == null) {
            return;
        }
        if (object instanceof BeanFactoryAware) {
            ((BeanFactoryAware) object).setBeanFactory(beanFactory);
        }
        if (object instanceof ApplicationContextAware) {
            ((ApplicationContextAware) object).setApplicationContext(applicationContext);
        }
        if (object instanceof ResourceLoaderAware) {
            ((ResourceLoaderAware) object).setResourceLoader(resourceLoader);
        }
        if (object instanceof MessageSourceAware) {
            ((MessageSourceAware) object).setMessageSource(messageSource);
        }
        if (object instanceof ApplicationEventPublisherAware) {
            ((ApplicationEventPublisherAware) object).setApplicationEventPublisher(applicationEventPublisher);
        }
    }
    
}
