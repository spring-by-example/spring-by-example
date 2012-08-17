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

package org.springmodules.validation.valang.functions;

import org.springframework.util.Assert;
import org.springmodules.validation.valang.ValangException;

/**
 * <p>Base class for functions. Function classes should extend this class.
 * <p/>
 * <p>The lifecyle of a function that extends this class is:
 * <p/>
 * <ul>
 * <li>Function instance is created through {@link AbstractFunction#AbstractFunction(Function[], int, int)}
 * <li>Spring callback interfaces are called (in this order):
 * <ul>
 * <li>{@link org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)}
 * <li>{@link org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)}
 * <li>{@link org.springframework.context.ResourceLoaderAware#setResourceLoader(org.springframework.core.io.ResourceLoader)}
 * <li>{@link org.springframework.context.MessageSourceAware#setMessageSource(org.springframework.context.MessageSource)}
 * <li>{@link org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)}
 * <li>{@link org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)}
 * </ul>
 * <li>Function properties are autowired by name if {@link AbstractFunction#isAutowireByName()} returns true
 * <li>Function properties are autowired by type if {@link AbstractFunction#isAutowireByType()} returns true
 * <li>{@link AbstractFunction#init()} is called
 * <li>Function is ready for use by validator
 * </ul>
 * <p/>
 * <p>Function implementations can implement any of the Spring callback interfaces listed above to get access to the specific objects.
 *
 * @author Steven Devijver
 * @since Apr 23, 2005
 */
public abstract class AbstractFunction implements Function {

    private Function[] arguments = null;
    private FunctionTemplate template = null;

    /**
     * Constructor
     */
    protected AbstractFunction() {}
    
    /**
     * <p>Constructor</p>
     * 
     * <p><strong>Note</strong>: Sub classes must implement this constructor 
     * unless they implement <code>ConfigurableConstructor</code>.</p>
     */
    public AbstractFunction(Function[] arguments, int line, int column) {
        super();
        setArguments(arguments);
        setTemplate(line, column);
    }

    /**
     * Gets arguments
     */
    public Function[] getArguments() {
        return arguments;
    }

    /**
     * Sets arguments.
     */
    protected void setArguments(Function[] arguments) {
        Assert.notNull(arguments, "Function parameters should not be null!");

        this.arguments = arguments;
    }

    /**
     * Gets template.
     */
    protected FunctionTemplate getTemplate() {
        return this.template;
    }

    /**
     * Sets template.
     */
    protected void setTemplate(FunctionTemplate template) {
        this.template = template;
    }

    /**
     * Sets template with line and row number by 
     * creating a new <code>FunctionTemplate</code>.
     */
    protected void setTemplate(int line, int column) {
        setTemplate(new FunctionTemplate(line, column));
    }

    /**
     * Call this method in the constructor of custom functions to define the minimum number of arguments.
     */
    protected void definedMinNumberOfArguments(int minNumberOfArguments) {
        if (getArguments().length < minNumberOfArguments) {
            throw new ValangException("Function requires at least " + minNumberOfArguments + " argument(s)", getTemplate().getLine(), getTemplate().getColumn());
        }
    }

    /**
     * Call this method in the constructor of custom functions to define the maximum number of arguments.
     */
    protected void definedMaxNumberOfArguments(int maxNumberOfArguments) {
        if (getArguments().length > maxNumberOfArguments) {
            throw new ValangException("Function cannot have more than " + maxNumberOfArguments + " arguments(s)", getTemplate().getLine(), getTemplate().getColumn());
        }
    }

    /**
     * Call this method in the constructor of custom functions to define the exact number of arguments.
     */
    protected void definedExactNumberOfArguments(int exactNumberOfArguments) {
        if (getArguments().length != exactNumberOfArguments) {
            throw new ValangException("Function must have exactly " + exactNumberOfArguments + " arguments", getTemplate().getLine(), getTemplate().getColumn());
        }
    }

    /**
     * Gets result.  Implementation of <code>Function</code>.
     */
    public final Object getResult(Object target) {
        return getTemplate().execute(target, new FunctionCallback() {
            public Object execute(Object target) throws Exception {
                return doGetResult(target);
            }
        });
    }

    /**
     * Processes result for subclasses.
     */
    protected abstract Object doGetResult(Object target) throws Exception;

    /**
     * If true properties of function will be autowired by type by the Spring bean factory.
     */
    public boolean isAutowireByType() {
        return false;
    }

    /**
     * If true properties of function will be autowired by name by the Spring bean factory.
     */
    public boolean isAutowireByName() {
        return false;
    }

    /**
     * This method is called when all properties have been set through autowiring. This method
     * can be implemented to initialize resources or verify if mandatory properties have been set.
     */
    public void init() throws Exception {

    }
}
