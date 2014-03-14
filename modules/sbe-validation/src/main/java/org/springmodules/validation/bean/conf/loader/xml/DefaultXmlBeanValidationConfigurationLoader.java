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
package org.springmodules.validation.bean.conf.loader.xml;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springmodules.validation.bean.conf.CascadeValidation;
import org.springmodules.validation.bean.conf.DefaultBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.conf.ValidationConfigurationException;
import org.springmodules.validation.bean.conf.loader.xml.handler.ClassValidationElementHandler;
import org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler;
import org.springmodules.validation.bean.rule.PropertyValidationRule;
import org.springmodules.validation.bean.rule.ValidationMethodValidationRule;
import org.springmodules.validation.bean.rule.ValidationRule;
import org.springmodules.validation.bean.rule.resolver.ErrorArgumentsResolver;
import org.springmodules.validation.bean.rule.resolver.FunctionErrorArgumentsResolver;
import org.springmodules.validation.util.cel.ConditionExpressionBased;
import org.springmodules.validation.util.cel.ConditionExpressionParser;
import org.springmodules.validation.util.cel.valang.ValangConditionExpressionParser;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.common.AlwaysTrueCondition;
import org.springmodules.validation.util.fel.FunctionExpressionBased;
import org.springmodules.validation.util.fel.FunctionExpressionParser;
import org.springmodules.validation.util.fel.parser.ValangFunctionExpressionParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The default xml bean validation configuration loader. This loader expects the following xml format:
 * <p/>
 * <pre>
 * &lt;validation [package="org.springmodules.validation.sample"]>
 *      &lt;class name="Person"&gt;
 *          &lt;global>
 *              &lt;any/&gt;...
 *          &lt;/global>
 *          &lt;property name="firstName" [valid="true|false"]&gt;
 *              &lt;any/&gt;...
 *          &lt;/property>
 *      &lt;/class>
 * &lt;/validation>
 * </pre>
 * <p/>
 * Please note the following:
 * <p/>
 * <ul>
 * <li>Each &lt;validation&gt; element can contain multiple &lt;class&gt; elements.</li>
 * <li>
 * A &lt;class&gt; element can have only on &lt;global&gt; elements and multiple &lt;property&gt; elements. This
 * elements hold validation rules to be bound globaly to the class instance or to specific properties.
 * </li>
 * <li>Both &lt;global&gt; and &lt;property&gt; elements can accept any element where each element represents
 * a validation rule. These validation rule elements will eventually be evaluated in the order they are defined.
 * When one of these rules fail, the evaluation stops
 * </li>
 * <li>
 * A &lt;property&gt; may have a 'valid' attribute to indicate that the property value needs to be validated as
 * well (cascade validation).
 * </li>
 * <li>
 * The &lt;validation&gt; element may have a 'package' attribute. This will serve as a default package for all
 * &lt;class&gt; elements (meaing there is not need to specify the fully qualified name in the 'name' attribute
 * of this element.
 * </li>
 * <li>
 * This XML format has a unique namespace which is defined by {@link #DEFAULT_NAMESPACE_URL}.
 * </li>
 * </ul>
 * <p/>
 * The validation rule element (sub-elements of &lt;global&gt; and &lt;property&gt;) are resolved using
 * validation rule element handlers. This class holds a registry for such handlers, where new handlers can
 * be registered as well. The default registry is {@link DefaultValidationRuleElementHandlerRegistry}.
 *
 * @author Uri Boness
 */
public class DefaultXmlBeanValidationConfigurationLoader extends AbstractXmlBeanValidationConfigurationLoader
    implements ConditionExpressionBased, FunctionExpressionBased, ApplicationContextAware {

    public static final String DEFAULT_NAMESPACE_URL = "http://www.springmodules.org/validation/bean";

    private final Logger logger = LoggerFactory.getLogger(DefaultXmlBeanValidationConfigurationLoader.class);

    private static final String CLASS_TAG = "class";

    private static final String GLOBAL_TAG = "global";

    private static final String PROPERTY_TAG = "property";

    private static final String METHOD_TAG = "method";

    private static final String VALIDATOR_BEAN_TAG = "validator-ref";

    private static final String VALIDATOR_TAG = "validator";

    private static final String PACKAGE_ATTR = "package";

    private static final String NAME_ATTR = "name";

    private static final String CASCADE_ATTR = "cascade";

    private static final String CASCADE_CONDITION_ATTR = "cascade-condition";

    private static final String CLASS_ATTR = "class";

    private static final String CODE_ATTR = "code";

    private static final String MESSAGE_ATTR = "message";

    private static final String ARGS_ATTR = "args";

    private static final String APPLY_IF_ATTR = "apply-if";

    private static final String CONTEXTS_ATTR = "contexts";

    private static final String FOR_PROPERTY_ATTR = "for-property";

    private ValidationRuleElementHandlerRegistry handlerRegistry;

    private boolean conditionParserExplicitlySet = false;

    private ConditionExpressionParser conditionExpressionParser;

    private boolean functionParserExplicitlySet = false;

    private FunctionExpressionParser functionExpressionParser;

    private ApplicationContext applicationContext;

    /**
     * Constructs a new DefaultXmlBeanValidationConfigurationLoader with the default validation rule
     * element handler registry.
     */
    public DefaultXmlBeanValidationConfigurationLoader() {
        this(new DefaultValidationRuleElementHandlerRegistry());
    }

    /**
     * Constructs a new DefaultXmlBeanValidationConfigurationLoader with the given validation rule
     * element handler registry.
     *
     * @param handlerRegistry The validation rule element handler registry that will be used by this loader.
     */
    public DefaultXmlBeanValidationConfigurationLoader(ValidationRuleElementHandlerRegistry handlerRegistry) {
        this(handlerRegistry, new ValangConditionExpressionParser(), new ValangFunctionExpressionParser());
    }

    /**
     * Constructs a new DefaultXmlBeanValidationConfigurationLoader with the given validation rule
     * element handler registry.
     *
     * @param handlerRegistry The validation rule element handler registry that will be used by this loader.
     * @param conditionExpressionParser The condition parser this loader should use to parse the cascade validation conditions.
     * @param functionExpressionParser The function parser this loader should use to parse the error arguments.
     */
    public DefaultXmlBeanValidationConfigurationLoader(
        ValidationRuleElementHandlerRegistry handlerRegistry,
        ConditionExpressionParser conditionExpressionParser,
        FunctionExpressionParser functionExpressionParser) {

        this.handlerRegistry = handlerRegistry;
        this.conditionExpressionParser = conditionExpressionParser;
        this.functionExpressionParser = functionExpressionParser;
    }

    /**
     * Loads the validation configuration from the given document that was created from the given resource.
     *
     * @see AbstractXmlBeanValidationConfigurationLoader#loadConfigurations(org.w3c.dom.Document, String)
     */
    @Override
    protected Map loadConfigurations(Document document, String resourceName) {
        Map configurations = new HashMap();
        Element validationDefinition = document.getDocumentElement();
        String packageName = validationDefinition.getAttribute(PACKAGE_ATTR);
        NodeList nodes = validationDefinition.getElementsByTagNameNS(DEFAULT_NAMESPACE_URL, CLASS_TAG);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element classDefinition = (Element) nodes.item(i);
            String className = classDefinition.getAttribute(NAME_ATTR);
            className = (StringUtils.hasLength(packageName)) ? packageName + "." + className : className;
            Class clazz;
            try {
                clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
            } catch (ClassNotFoundException cnfe) {
                logger.error("Could not load class '" + className + "' as defined in '" + resourceName + "'", cnfe);
                continue;
            }
            configurations.put(clazz, handleClassDefinition(clazz, classDefinition));
        }
        return configurations;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        initContext(handlerRegistry);
        super.afterPropertiesSet();
        findConditionExpressionParserInApplicationContext();
        findFunctionExpressionParserInApplicationContext();
        Assert.notNull(conditionExpressionParser);
        Assert.notNull(functionExpressionParser);
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Sets the element handler registry this loader will use to fetch the handlers while loading
     * validation configuration.
     *
     * @param registry The element handler registry to be used by this loader.
     */
    public void setElementHandlerRegistry(ValidationRuleElementHandlerRegistry registry) {
        this.handlerRegistry = registry;
    }

    /**
     * Returns the element handler registry used by this loader.
     *
     * @return The element handler registry used by this loader.
     */
    public ValidationRuleElementHandlerRegistry getElementHandlerRegistry() {
        return handlerRegistry;
    }

    /**
     * @see ConditionExpressionBased#setConditionExpressionParser(org.springmodules.validation.util.cel.ConditionExpressionParser)
     */
    @Override
    public void setConditionExpressionParser(ConditionExpressionParser conditionExpressionParser) {
        this.conditionParserExplicitlySet = true;
        this.conditionExpressionParser = conditionExpressionParser;
    }

    /**
     * @see FunctionExpressionBased#setFunctionExpressionParser(org.springmodules.validation.util.fel.FunctionExpressionParser)
     */
    @Override
    public void setFunctionExpressionParser(FunctionExpressionParser functionExpressionParser) {
        this.functionParserExplicitlySet = true;
        this.functionExpressionParser = functionExpressionParser;
    }

    /**
     * @see ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    //=============================================== Helper Methods ===================================================

    protected void initContext(Object object) throws Exception {
        if (object instanceof ApplicationContextAware) {
            ((ApplicationContextAware) object).setApplicationContext(applicationContext);
        }
        if (object instanceof InitializingBean) {
            ((InitializingBean) object).afterPropertiesSet();
        }
    }

    /**
     * Creates and builds a bean validation configuration based for the given class, based on the given &lt;class&gt;
     * element.
     *
     * @param element The &lt;class&gt; element.
     * @param clazz The class for which the validation configuration is being loaded.
     * @return The created bean validation configuration.
     */
    public BeanValidationConfiguration handleClassDefinition(Class clazz, Element element) {

        DefaultBeanValidationConfiguration configuration = new DefaultBeanValidationConfiguration();

        NodeList nodes = element.getElementsByTagNameNS(DEFAULT_NAMESPACE_URL, VALIDATOR_TAG);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element validatorDefinition = (Element) nodes.item(i);
            handleValidatorDefinition(validatorDefinition, clazz, configuration);
        }

        nodes = element.getElementsByTagNameNS(DEFAULT_NAMESPACE_URL, VALIDATOR_BEAN_TAG);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element validatorBeanDefinition = (Element) nodes.item(i);
            handleValidatorBeanDefinition(validatorBeanDefinition, clazz, configuration);
        }

        nodes = element.getElementsByTagNameNS(DEFAULT_NAMESPACE_URL, GLOBAL_TAG);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element globalDefinition = (Element) nodes.item(i);
            handleGlobalDefinition(globalDefinition, clazz, configuration);
        }

        nodes = element.getElementsByTagNameNS(DEFAULT_NAMESPACE_URL, METHOD_TAG);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element methodDefinition = (Element) nodes.item(i);
            handleMethodDefinition(methodDefinition, clazz, configuration);
        }

        nodes = element.getElementsByTagNameNS(DEFAULT_NAMESPACE_URL, PROPERTY_TAG);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element propertyDefinition = (Element) nodes.item(i);
            handlePropertyDefinition(propertyDefinition, clazz, configuration);
        }

        return configuration;
    }

    protected void handleValidatorDefinition(Element validatorDefinition, Class clazz, MutableBeanValidationConfiguration configuration) {
        String className = validatorDefinition.getAttribute(CLASS_ATTR);
        configuration.addCustomValidator(constructValidator(className));
    }

    protected void handleValidatorBeanDefinition(Element definition, Class clazz, MutableBeanValidationConfiguration configuration) {
        if (applicationContext == null) {
            throw new UnsupportedOperationException(VALIDATOR_BEAN_TAG + " configuration cannot be applied for " +
                "this configuration loader was not deployed in an application context");
        }
        String beanName = definition.getAttribute(NAME_ATTR);
        Validator validator = applicationContext.getBean(beanName, Validator.class);
        configuration.addCustomValidator(validator);
    }

    /**
     * Handles the &lt;global&gt; element and updates the given configuration with the global validation rules.
     *
     * @param globalDefinition The &lt;global&gt; element.
     * @param clazz The validated class.
     * @param configuration The bean validation configuration to update.
     */
    protected void handleGlobalDefinition(Element globalDefinition, Class clazz, MutableBeanValidationConfiguration configuration) {
        NodeList nodes = globalDefinition.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element ruleDefinition = (Element) node;
            ClassValidationElementHandler handler = handlerRegistry.findClassHandler(ruleDefinition, clazz);
            if (handler == null) {
                logger.error("Could not handle element '" + ruleDefinition.getTagName() +
                    "'. Please make sure the proper validation rule definition handler is registered");
                throw new ValidationConfigurationException("Could not handler element '" + ruleDefinition.getTagName() + "'");
            }
            handler.handle(ruleDefinition, configuration);
        }
    }

    protected void handleMethodDefinition(Element methodDefinition, Class clazz, MutableBeanValidationConfiguration configuration) {
        String methodName = methodDefinition.getAttribute(NAME_ATTR);
        if (!StringUtils.hasText(methodName)) {
            logger.error("Could not parse method element. Missing or empty 'name' attribute");
            throw new ValidationConfigurationException("Could not parse method element. Missing 'name' attribute");
        }

        String errorCode = methodDefinition.getAttribute(CODE_ATTR);
        String message = methodDefinition.getAttribute(MESSAGE_ATTR);
        String argsString = methodDefinition.getAttribute(ARGS_ATTR);
        String conditionString = methodDefinition.getAttribute(APPLY_IF_ATTR);
        String propertyName = methodDefinition.getAttribute(FOR_PROPERTY_ATTR);
        String contextsString = methodDefinition.getAttribute(CONTEXTS_ATTR);

        ValidationMethodValidationRule rule = createMethodValidationRule(
            clazz,
            methodName,
            errorCode,
            message,
            argsString,
            contextsString,
            conditionString
        );

        if (StringUtils.hasText(propertyName)) {
            validatePropertyExists(clazz, propertyName);
            configuration.addPropertyRule(propertyName, rule);
        } else {
            configuration.addGlobalRule(rule);
        }
    }

    protected ValidationMethodValidationRule createMethodValidationRule(
        Class clazz,
        String methodName,
        String errorCode,
        String message,
        String argsString,
        String contextsString,
        String applyIfString) {

        Method method = ReflectionUtils.findMethod(clazz, methodName);
        if (method == null) {
            throw new ValidationConfigurationException("Method named '" + methodName +
                "' was not found in class hierarchy of '" + clazz.getName() + "'.");
        }

        if (!StringUtils.hasText(errorCode)) {
            errorCode = methodName + "()";
        }
        if (!StringUtils.hasText(message)) {
            message = errorCode;
        }
        if (!StringUtils.hasText(argsString)) {
            argsString = "";
        }
        ErrorArgumentsResolver argsResolver = buildErrorArgumentsResolver(argsString);
        Condition applyIfCondition = new AlwaysTrueCondition();
        if (StringUtils.hasText(applyIfString)) {
            applyIfCondition = conditionExpressionParser.parse(applyIfString);
        }

        String[] contexts = null;
        if (StringUtils.hasText(contextsString)) {
            contexts = StringUtils.commaDelimitedListToStringArray(contextsString);
        }

        ValidationMethodValidationRule rule = new ValidationMethodValidationRule(method);
        rule.setErrorCode(errorCode);
        rule.setDefaultErrorMessage(message);
        rule.setErrorArgumentsResolver(argsResolver);
        rule.setApplicabilityCondition(applyIfCondition);
        rule.setContextTokens(contexts);

        return rule;
    }

    protected ErrorArgumentsResolver buildErrorArgumentsResolver(String argsString) {
        String[] args = StringUtils.tokenizeToStringArray(argsString, ", ");
        return new FunctionErrorArgumentsResolver(args, functionExpressionParser);
    }


    /**
     * Handles the given &lt;property&gt; element and updates the given bean validation configuration with the property
     * validation rules.
     *
     * @param propertyDefinition The &lt;property&gt; element.
     * @param clazz The validated class.
     * @param configuration The bean validation configuration to update.
     */
    protected void handlePropertyDefinition(Element propertyDefinition, Class clazz, MutableBeanValidationConfiguration configuration) {
        String propertyName = propertyDefinition.getAttribute(NAME_ATTR);
        if (!StringUtils.hasText(propertyName)) {
            logger.error("Could not parse property element. Missing or empty 'name' attribute");
            throw new ValidationConfigurationException("Could not parse property element. Missing 'name' attribute");
        }

        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(clazz, propertyName);
        if (propertyDescriptor == null) {
            logger.error("Property '" + propertyName + "' does not exist in class '" + clazz.getName() + "'");
        }

        if (propertyDefinition.hasAttribute(CASCADE_ATTR) && "true".equals(propertyDefinition.getAttribute(CASCADE_ATTR)))
        {
            CascadeValidation cascadeValidation = new CascadeValidation(propertyName);
            if (propertyDefinition.hasAttribute(CASCADE_CONDITION_ATTR)) {
                String conditionExpression = propertyDefinition.getAttribute(CASCADE_CONDITION_ATTR);
                cascadeValidation.setApplicabilityCondition(conditionExpressionParser.parse(conditionExpression));
            }
            configuration.addCascadeValidation(cascadeValidation);
        }

        NodeList nodes = propertyDefinition.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element ruleDefinition = (Element) node;
            PropertyValidationElementHandler handler = handlerRegistry.findPropertyHandler(ruleDefinition, clazz, propertyDescriptor);
            if (handler == null) {
                logger.error("Could not handle element '" + ruleDefinition.getTagName() +
                    "'. Please make sure the proper validation rule definition handler is registered");
                throw new ValidationConfigurationException("Could not handle element '" + ruleDefinition.getTagName() + "'");
            }
            handler.handle(ruleDefinition, propertyName, configuration);
        }
    }

    protected PropertyValidationRule createPropertyRule(String propertyName, ValidationRule rule) {
        return new PropertyValidationRule(propertyName, rule);
    }

    protected Validator constructValidator(String className) {
        try {
            Class<?> clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
            if (!Validator.class.isAssignableFrom(clazz)) {
                throw new ValidationConfigurationException("class '" + className + "' is not a Validator implementation");
            }
            return (Validator) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new ValidationConfigurationException("Could not load validator class '" + className + "'");
        } catch (IllegalAccessException e) {
            throw new ValidationConfigurationException("Could not instantiate validator '" + className +
                "'. Make sure it has a default constructor.");
        } catch (InstantiationException e) {
            throw new ValidationConfigurationException("Could not instantiate validator '" + className +
                "'. Make sure it has a default constructor.");
        }
    }

    protected void findConditionExpressionParserInApplicationContext() {
        if (applicationContext == null || conditionParserExplicitlySet) {
            return;
        }
        String[] names = applicationContext.getBeanNamesForType(ConditionExpressionParser.class);
        if (names.length == 0) {
            return;
        }
        if (names.length > 1) {
            logger.warn("Multiple condition expression parsers are defined in the application context. " +
                "Only the first encountered one will be used");
        }
        conditionExpressionParser = (ConditionExpressionParser) applicationContext.getBean(names[0]);
    }

    protected void findFunctionExpressionParserInApplicationContext() {
        if (applicationContext == null || functionParserExplicitlySet) {
            return;
        }
        String[] names = applicationContext.getBeanNamesForType(FunctionExpressionParser.class);
        if (names.length == 0) {
            return;
        }
        if (names.length > 1) {
            logger.warn("Multiple function expression parsers are defined in the application context. " +
                "Only the first encountered one will be used");
        }
        functionExpressionParser = (FunctionExpressionParser) applicationContext.getBean(names[0]);
    }

    protected void validatePropertyExists(Class clazz, String property) {
        BeanUtils.getPropertyDescriptor(clazz, property);
    }
}
