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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import org.apache.commons.collections.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springmodules.validation.util.PrimitiveClassUtils;
import org.springmodules.validation.util.date.DefaultDateParser;
import org.springmodules.validation.valang.ValangException;
import org.springmodules.validation.valang.functions.AbstractInitializableFunction;
import org.springmodules.validation.valang.functions.BeanPropertyFunction;
import org.springmodules.validation.valang.functions.EmailFunction;
import org.springmodules.validation.valang.functions.Function;
import org.springmodules.validation.valang.functions.InRoleFunction;
import org.springmodules.validation.valang.functions.LengthOfFunction;
import org.springmodules.validation.valang.functions.LowerCaseFunction;
import org.springmodules.validation.valang.functions.NotFunction;
import org.springmodules.validation.valang.functions.RegExFunction;
import org.springmodules.validation.valang.functions.ResolveFunction;
import org.springmodules.validation.valang.functions.UpperCaseFunction;
import org.springmodules.validation.valang.predicates.BetweenTestPredicate;
import org.springmodules.validation.valang.predicates.EqualsTestPredicate;
import org.springmodules.validation.valang.predicates.GenericTestPredicate;
import org.springmodules.validation.valang.predicates.GreaterThanOrEqualTestPredicate;
import org.springmodules.validation.valang.predicates.GreaterThanTestPredicate;
import org.springmodules.validation.valang.predicates.InTestPredicate;
import org.springmodules.validation.valang.predicates.LessThanOrEqualTestPredicate;
import org.springmodules.validation.valang.predicates.LessThanTestPredicate;
import org.springmodules.validation.valang.predicates.NotBetweenTestPredicate;
import org.springmodules.validation.valang.predicates.NotEqualsTestPredicate;
import org.springmodules.validation.valang.predicates.NotInTestPredicate;
import org.springmodules.validation.valang.predicates.Operator;

/**
 * <p>Allows registration of custom functions. Custom functions can overwrite default functions.
 * <p/>
 * <p>Default functions are:
 * <p/>
 * <ul>
 * <li>len, length, size
 * <li>upper
 * <li>lower
 * <li>!
 * </ul>
 *
 * @author Steven Devijver
 * @since Apr 23, 2005
 */
public class DefaultVisitor implements ApplicationContextAware, ValangVisitor {

    final Logger logger = LoggerFactory.getLogger(DefaultVisitor.class);

    // FIX ME: use DI
    final static Map<String, Function> hGeneratedBeanFunctions = new HashMap<String, Function>();

    /**
     * The delimiter that preceeds the zero-relative subscript for an
     * indexed reference.
     */
    public static final char INDEXED_DELIM_BEGIN = '[';
    public static final char INDEXED_DELIM_END = ']';
    
    private ValangVisitor visitor = null;
    private DefaultDateParser dateParser = null;
    private ApplicationContext applicationContext = null;

    /**
     * Constructor
     */
    public DefaultVisitor() {
        this.dateParser = new DefaultDateParser();
    }

    /**
     * Get a function based on the function name and arguments.
     */
    public Function getFunction(String name, Function[] arguments, int line, int column) {
        return doGetFunction(name, arguments, line, column);
    }
    
    /**
     * Get bytecode generation function.
     * 
     * @param       className       Name of the <code>Class</code> to generated 
     *                              a <code>Function</code> to retrieve a property.
     * @param       propertyName    The name of the property to retrieve a value from in the function.
     *                              For example, expects 'message' and will call <code>getMessage()</code>.
     *                              
     * @return      Function        Bytecode generated <code>Function</code> based on the class and property name. 
     */
    public Function getFunction(String className, String propName) {
        Function result = null;
        String propertyName = propName;
        
        // FIX ME: consolidate logic checking between this and BeanPropertyFunction
        
        // FIXME: make excluded list: 'jsonMessageString'
        if (!StringUtils.hasText(className) || propertyName.startsWith("this") || 
            (propertyName.indexOf(INDEXED_DELIM_END) != -1 && (propName.length() > (propertyName.indexOf(INDEXED_DELIM_END) + 1))) ||
            propertyName.toLowerCase().indexOf("jsonMessageString".toLowerCase()) != -1) {
            result = new BeanPropertyFunction(propertyName);
        } else {
            String classFunctionName = null;
            String property = null;
            String reflectProperty = null;
            String indexedKey = null;
            
            boolean listOrMapProperty = propertyName.indexOf(INDEXED_DELIM_BEGIN) != -1;
            
            if (listOrMapProperty) {
                int indexedDelimEnd = propertyName.indexOf(INDEXED_DELIM_END);
                
                indexedKey = propertyName.substring((propertyName.indexOf(INDEXED_DELIM_BEGIN) + 1), indexedDelimEnd);
                propertyName = propertyName.substring(0, propertyName.indexOf(INDEXED_DELIM_BEGIN));
            }
            
            classFunctionName = className + org.apache.commons.lang.StringUtils.capitalize(propertyName) + 
                                (indexedKey != null ? org.apache.commons.lang.StringUtils.capitalize(indexedKey) : "") + 
                                "BeanPropertyFunction$$Valang";
            
            
            String key = classFunctionName;

            if (hGeneratedBeanFunctions.containsKey(key)) {
                result = hGeneratedBeanFunctions.get(key);
            } else {
                try {
                    Class<?> clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
                    Class<?> methodReturnType = null;
                    boolean indexedProperty = propertyName.indexOf(".") != -1;
                    
                    if (indexedProperty) {
                        String[] properties = org.apache.commons.lang.StringUtils.split(propertyName, ".");
                        
                        int count = 1;
                        StringBuilder sb = new StringBuilder();
                        Class<?> nestedMethodReturnType = null;
    
                        for (String prop : properties) {
                            reflectProperty = "get" + org.apache.commons.lang.StringUtils.capitalize(prop);
                            
                            sb.append(reflectProperty);
                            sb.append("()");
                              
                            if (count < properties.length) {
                                sb.append(".");
                            }
                            
                            if (nestedMethodReturnType == null) {
                                Method nestedMethod = ReflectionUtils.findMethod(clazz, reflectProperty);
                                nestedMethodReturnType = nestedMethod.getReturnType();
                            } else {
                                Method nestedMethod = ReflectionUtils.findMethod(nestedMethodReturnType, reflectProperty);
                                nestedMethodReturnType = nestedMethod.getReturnType();                                    
                            }
                            
                            count++;
                        }
                        
                        property = sb.toString();
//                        property = processProperty(propertyName, wrapper); //sb.toString();
                        methodReturnType = nestedMethodReturnType;
                    } else {
                        StringBuilder sb = new StringBuilder();
         
                        sb.append("get");
                        sb.append(org.apache.commons.lang.StringUtils.capitalize(propertyName));
                        
                        reflectProperty = sb.toString();
                        
                        sb.append("()");
                        
                        property = sb.toString();
   
                        methodReturnType = ReflectionUtils.findMethod(clazz, reflectProperty).getReturnType();
                    }
                    
                    Class<?> primitiveClass = PrimitiveClassUtils.resolvePrimitiveClassName(methodReturnType);

                    ClassPool pool = ClassPool.getDefault();
                    CtClass cc = pool.makeClass(classFunctionName);

                    cc.addInterface(pool.get("org.springmodules.validation.valang.functions.Function"));

                    StringBuilder generatedMethod = new StringBuilder();
                    generatedMethod.append("public Object getResult(Object target) {");
                    generatedMethod.append("    return ");

                    if (primitiveClass != null) {
                        generatedMethod.append(" new " + primitiveClass.getName() + "(");
                    }
                    
                    generatedMethod.append(" ((" + className + ")target).");
                    
                    generatedMethod.append(property);
                    
                    if (primitiveClass != null) {
                        generatedMethod.append(")");
                    } else if (listOrMapProperty && 
                               org.apache.commons.lang.ClassUtils.isAssignable(methodReturnType, Map.class)) {
                        generatedMethod.append(".get(\"" + indexedKey + "\")");
                    } else if (listOrMapProperty && 
                               org.apache.commons.lang.ClassUtils.isAssignable(methodReturnType, List.class)) {
                        generatedMethod.append(".get(" + indexedKey + ")");
                    } else if (methodReturnType.isArray()) {
                        int arrayIndex = Integer.parseInt(indexedKey);
                        
                        generatedMethod.append("[" + arrayIndex + "]");
                    }
                    
                    generatedMethod.append(";");
                    generatedMethod.append("}");

                    CtMethod m = CtNewMethod.make(generatedMethod.toString(), cc);
                    cc.addMethod(m);

                    result = (Function)cc.toClass().newInstance();

                    hGeneratedBeanFunctions.put(key, result);
                    
                    logger.info("Generated bytecode for {}.{} as '{}'.", 
                                new Object[] { className, 
                                               property + "()" + (indexedKey != null ? "[" + indexedKey + "]" : ""), 
                                               classFunctionName }); 
                } catch (Exception e) {
                    logger.error("Problem generating bytecode for {}.{}.  {}", 
                                 new Object[] { classFunctionName, 
                                                org.apache.commons.lang.StringUtils.capitalize(propertyName),
                                                e.getMessage() });
                    
                    // fallback to standard bean property function
                    result = new BeanPropertyFunction(propName);
                }
            }
        }
        
        return result;
    }
    
    private Function doGetFunction(String name, Function[] arguments, int line, int column) {

        Function function = resolveCustomFunction(name, arguments, line, column);
        if (function != null) {
            return function;
        }

        function = resolveFunctionFromApplicationContext(name, arguments, line, column);
        if (function != null) {
            return function;
        }

        function = resolveDefaultFunction(name, arguments, line, column);
        if (function != null) {
            return function;
        }

        throw new ValangException("Could not find function [" + name + "]", line, column);
    }

    private Function resolveCustomFunction(String name, Function[] arguments, int line, int column) {
        if (getVisitor() == null) {
            return null;
        }
        return getVisitor().getFunction(name, arguments, line, column);
    }

    private Function resolveFunctionFromApplicationContext(String name, Function[] arguments, int line, int column) {
        if (applicationContext == null || !applicationContext.containsBean(name)) {
            return null;
        }
        
        Object bean = applicationContext.getBean(name);
        
        if (!AbstractInitializableFunction.class.isInstance(bean)) {
            logger.warn("Bean '" + name + "' is not of a '" + AbstractInitializableFunction.class.getName() + "' type");
            return null;
        }
        
        AbstractInitializableFunction function = (AbstractInitializableFunction) bean;
        function.init(arguments, line, column);
        
        return function;
    }

    private Function resolveDefaultFunction(String name, Function[] arguments, int line, int column) {
        if ("len".equals(name) || "length".equals(name) || "size".equals(name) || "count".equals(name)) {
            return new LengthOfFunction(arguments, line, column);
        } else if ("upper".equals(name)) {
            return new UpperCaseFunction(arguments, line, column);
        } else if ("lower".equals(name)) {
            return new LowerCaseFunction(arguments, line, column);
        } else if ("!".equals(name)) {
            return new NotFunction(arguments, line, column);
        } else if ("resolve".equals(name)) {
            return new ResolveFunction(arguments, line, column);
        } else if ("match".equals(name) || "matches".equals(name)) {
            return new RegExFunction(arguments, line, column);
        } else if ("inRole".equals(name)) {
            return new InRoleFunction(arguments, line, column);
        } else if ("email".equals(name)) {
            return new EmailFunction(arguments, line, column);
        }

        return null;
    }

    /**
     * Gets visitor.
     */
    public ValangVisitor getVisitor() {
        return this.visitor;
    }

    /**
     * <p>Register a custom visitor to look up custom functions. Lookup of functions
     * will first be delegated to this visitor. If no function has been returned (null)
     * lookup will be handled by DefaultVisitor.
     *
     * @param   visitor     The custom visitor.
     */
    public void setVisitor(ValangVisitor visitor) {
        this.visitor = visitor;
    }

    /**
     * Gets predicate.
     * 
     * @param       leftFunction        Left comparison <code>Function</code>.
     * @param       operator            <code>Operation</code> for comparison.
     * @param       rightFunction       Right comparison <code>Function</code>.
     * @param       line                Line number of parsed expression.
     * @param       column              Column number of parsed expression.
     * 
     * @return      Predicate           Comparison predicate based on input.
     */
    public Predicate getPredicate(Function leftFunction, Operator operator, Function rightFunction, int line, int column) {
        Predicate result = null;

        switch (operator) {
            case EQUAL:
                result =  new EqualsTestPredicate(leftFunction, operator, rightFunction, line, column);
                break;
            case NOT_EQUAL:
                result =  new NotEqualsTestPredicate(leftFunction, operator, rightFunction, line, column);
                break;
            case LESS_THAN:
                result =  new LessThanTestPredicate(leftFunction, operator, rightFunction, line, column);
                break;
            case LESS_THAN_OR_EQUAL:
                result =  new LessThanOrEqualTestPredicate(leftFunction, operator, rightFunction, line, column);
                break;
            case GREATER_THAN:
                result =  new GreaterThanTestPredicate(leftFunction, operator, rightFunction, line, column);
                break;
            case GREATER_THAN_OR_EQUAL:
                result =  new GreaterThanOrEqualTestPredicate(leftFunction, operator, rightFunction, line, column);
                break;
            case BETWEEN:
                result =  new BetweenTestPredicate(leftFunction, operator, rightFunction, line, column);
                break;
            case NOT_BETWEEN:
                result =  new NotBetweenTestPredicate(leftFunction, operator, rightFunction, line, column);
                break;
            case IN:
                result =  new InTestPredicate(leftFunction, operator, rightFunction, line, column);
                break;
            case NOT_IN:
                result =  new NotInTestPredicate(leftFunction, operator, rightFunction, line, column);
                break;
            default:
                result = new GenericTestPredicate(leftFunction, operator, rightFunction, line, column);
                break;
        }
            
        return result;
    }

    /**
     * Gets date parser.
     */
    public DefaultDateParser getDateParser() {
        return dateParser;
    }

    /**
     * Implementation of <code>ApplicationContextAware</code>.
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
}
