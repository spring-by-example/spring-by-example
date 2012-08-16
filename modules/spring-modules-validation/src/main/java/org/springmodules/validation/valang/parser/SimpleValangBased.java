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

package org.springmodules.validation.valang.parser;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import org.springmodules.validation.valang.functions.FunctionDefinition;
import org.springmodules.validation.valang.functions.FunctionWrapper;

/**
 * A simple implementation of {@link ValangBased}.
 *
 * @author Uri Boness
 */
public class SimpleValangBased implements ApplicationContextAware, ValangBased {

    private ApplicationContext applicationContext = null;
    private Map<String, Object> hCustomFunctions = new HashMap<String, Object>();
    private Map dateParsers = new HashMap();
    
    /**
     * Implementation of <code>ApplicationContextAware</code>.
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    /**
     * @see ValangBased#setCustomFunctions(java.util.Map)
     */
    public void setCustomFunctions(Map<String, Object> hCustomFunctions) {
        this.hCustomFunctions = hCustomFunctions;
    }

    /**
     * @see ValangBased#addCustomFunction(String, String)
     */
    public void addCustomFunction(String functionName, String functionClassName) {
        hCustomFunctions.put(functionName, functionClassName);
    }

    /**
     * @see ValangBased#setDateParsers(java.util.Map)
     */
    public void setDateParsers(Map parserByRegexp) {
        dateParsers = (parserByRegexp != null) ? parserByRegexp : new HashMap();
    }

    public Map<String, Object> getCustomFunctions() {
        return hCustomFunctions;
    }

    public Map getDateParsers() {
        return dateParsers;
    }

    /**
     * Creates a new {@link ValangParser} that is already configured with the proper custom functions and date
     * parsers.
     * 
     * @param       expression      Valang language validation expression.
     *
     * @return A new {@link ValangParser}.
     */
    public ValangParser createValangParser(String expression) {
        return createValangParser(expression, null);
    }

    /**
     * Creates a new {@link ValangParser} that is already configured with the proper custom functions and date
     * parsers and uses the class name to perform bytecode generation to avoid reflection.
     *
     * @param       expression      Valang language validation expression.
     * @param       className       Name of the <code>Class</code> to generated 
     *                              a <code>Function</code> to retrieve a property.
     *
     * @return A new {@link ValangParser}.
     */
    public ValangParser createValangParser(String expression, String className) {
        ValangParser parser = new ValangParser(expression, className, getAllCustomFunctions(), dateParsers);
        
        // visitor needs context to look up bean references for functions (like a list (ex: person IN @personList))
        if (parser.getVisitor() instanceof ApplicationContextAware) {
            ((ApplicationContextAware)parser.getVisitor()).setApplicationContext(applicationContext);
        }

        return parser;
    }

    public void initValang(Object object) {
        if (object instanceof ValangBased) {
            ((ValangBased) object).setCustomFunctions(hCustomFunctions);
            ((ValangBased) object).setDateParsers(dateParsers);
        }
    }

    /**
     * Returns all the custom functions that can be found. This method returns all the custom functions
     * that were explicitly registered with this instance and all custom functions that can be found in the
     * application context (if one is set) by looking for {@link org.springmodules.validation.valang.functions.FunctionDefinition}
     * beans.
     *
     * @return All the custom function that can be found.
     */
    public Map<String, Object> getAllCustomFunctions() {
        Map<String, Object> functionByName = new HashMap<String, Object>();
        
        functionByName.putAll(findAllCustomFunctionsInApplicationContext());
        
        // let explicitly configured custom functions override discovered ones
        functionByName.putAll(hCustomFunctions);
        
        return functionByName;
    }

    /**
     * Finds all <code>FunctionDefinition</code> and  <code>FunctionWrapper</code> beans 
     * in the Spring context.
     */
    protected Map<String, Object> findAllCustomFunctionsInApplicationContext() {
        Map<String, Object> hFunctions = new HashMap<String, Object>();

        if (applicationContext != null) {
            // find FunctionDefinitions
            String[] beanNames = applicationContext.getBeanNamesForType(FunctionDefinition.class);
            
            if (beanNames != null) {        
                for (int i = 0; i < beanNames.length; i++) {
                    FunctionDefinition functionDefinition = (FunctionDefinition) applicationContext.getBean(beanNames[i]);
        
                    hFunctions.put(functionDefinition.getName(), functionDefinition.getClassName());
                }
            }
            
            // find FunctionWrappers
            beanNames = applicationContext.getBeanNamesForType(FunctionWrapper.class);
            
            if (beanNames != null) {
                for (int i = 0; i < beanNames.length; i++) {
                    String beanName = beanNames[i];
                    
                    FunctionWrapper functionWrapper = (FunctionWrapper) applicationContext.getBean(beanName);
                    
                    String key = functionWrapper.getFunctionName();
                    
                    if (!StringUtils.hasText(key)) {
                        key = beanName;
                    }
                    
                    hFunctions.put(key, functionWrapper);
                }
            }
        }
        
        return hFunctions;
    }

}
