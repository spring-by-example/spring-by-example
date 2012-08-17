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

package org.springmodules.validation.bean.conf.loader.xml.handler;

import java.beans.PropertyDescriptor;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.rule.AbstractValidationRule;
import org.springmodules.validation.bean.rule.PropertyValidationRule;
import org.springmodules.validation.bean.rule.resolver.ErrorArgumentsResolver;
import org.springmodules.validation.bean.rule.resolver.FunctionErrorArgumentsResolver;
import org.springmodules.validation.util.cel.ConditionExpressionBased;
import org.springmodules.validation.util.cel.ConditionExpressionParser;
import org.springmodules.validation.util.cel.valang.ValangConditionExpressionParser;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.fel.FunctionExpressionBased;
import org.springmodules.validation.util.fel.FunctionExpressionParser;
import org.springmodules.validation.util.fel.parser.ValangFunctionExpressionParser;
import org.w3c.dom.Element;

/**
 * A base class for common {@link PropertyValidationElementHandler}
 * implementations that represent validation rules. This base handler idetifies the supported elements by their
 * tag names (qualified and local). In addition, it assumes the following common attributes:
 * <p/>
 * <ul>
 * <li>code - Indicates the error code of the validatoin rule</li>
 * <li>message - The default error message of the validation rule</li>
 * <li>args - A comma separated list of error arguments</li>
 * <li>apply-if - An condition expression that determines the applicability of the validation rule</li>
 * </ul>
 * <p/>
 * Note: The apply-if attribute is being parsed by the
 * {@link org.springmodules.validation.util.cel.ConditionExpressionParser} that is associated with this handler. It
 * uses {@link org.springmodules.validation.util.cel.valang.ValangConditionExpressionParser} by default.
 *
 * @author Uri Boness
 */
public abstract class AbstractPropertyValidationElementHandler
    implements PropertyValidationElementHandler, ConditionExpressionBased, FunctionExpressionBased {

    private static final String ERROR_CODE_ATTR = "code";

    private static final String MESSAGE_ATTR = "message";

    private static final String ARGS_ATTR = "args";

    private static final String APPLY_IF_ATTR = "apply-if";

    private static final String CONTEXTS_ATTR = "contexts";

    private String elementName;

    private String namespaceUri;

    private ConditionExpressionParser conditionExpressionParser;

    private FunctionExpressionParser functionExpressionParser;

    /**
     * Constructs a new AbstractPropertyValidationElementHandler with given supported element name.
     *
     * @param elementName The supported element name.
     */
    public AbstractPropertyValidationElementHandler(String elementName) {
        this(elementName, null);
    }

    /**
     * Constructs a new AbstractPropertyValidationElementHandler with given supported element name and namespace.
     *
     * @param elementName The supported element name.
     * @param namespace The supported namespace.
     */
    public AbstractPropertyValidationElementHandler(String elementName, String namespace) {
        this(elementName, namespace, new ValangConditionExpressionParser(), new ValangFunctionExpressionParser());
    }

    /**
     * Constructs a new AbstractPropertyValidationElementHandler with given supported element name and namespace and a
     * condition handler to parse the <code>apply-if</code> expressions.
     *
     * @param elementName The supported element name.
     * @param namespace The supported namespace.
     * @param conditionExpressionParser The condition expression parser to be used to parse the various condition expressions.
     * @param functionExpressionParser The function expression parser to be used to parse the error arguments expressions.
     */
    public AbstractPropertyValidationElementHandler(
        String elementName,
        String namespace,
        ConditionExpressionParser conditionExpressionParser,
        FunctionExpressionParser functionExpressionParser) {

        this.elementName = elementName;
        this.namespaceUri = namespace;
        this.conditionExpressionParser = conditionExpressionParser;
        this.functionExpressionParser = functionExpressionParser;
    }

    /**
     * Determines whether the given element is supported by this handler. The check is done by comparing the element
     * tag name and namespace with the ones that are configured with this handler.
     *
     * @see org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler#supports(org.w3c.dom.Element, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Element element, Class clazz, PropertyDescriptor descriptor) {
        String localName = element.getLocalName();
        if (!localName.equals(elementName)) {
            return false;
        }
        String ns = element.getNamespaceURI();
        return ObjectUtils.nullSafeEquals(ns, namespaceUri);
    }

    /**
     * Creates the appropriate {@link org.springmodules.validation.bean.rule.ValidationRule} based on the given element
     * and adds it to the given configuration.
     *
     * @see AbstractPropertyValidationElementHandler#handle(org.w3c.dom.Element, String, org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration)
     */
    public void handle(Element element, String propertyName, MutableBeanValidationConfiguration configuration) {

        AbstractValidationRule rule = createValidationRule(element);

        String errorCode = extractErrorCode(element);
        if (errorCode != null) {
            rule.setErrorCode(errorCode);
        }

        String message = extractMessage(element);
        if (message != null) {
            rule.setDefaultErrorMessage(message);
        }

        ErrorArgumentsResolver argumentsResolver = extractArgumentsResolver(element);
        if (argumentsResolver != null) {
            rule.setErrorArgumentsResolver(argumentsResolver);
        }

        Condition applicabilityCondition = extractApplicabilityCondition(element);
        if (applicabilityCondition != null) {
            rule.setApplicabilityCondition(applicabilityCondition);
        }

        String[] applicableContexts = extractApplicableContexts(element);
        if (applicableContexts != null) {
            rule.setContextTokens(applicableContexts);
        }

        if (isConditionGloballyScoped(element)) {
            configuration.addPropertyRule(propertyName, rule);
        } else {
            PropertyValidationRule propertyRule = new PropertyValidationRule(propertyName, rule);

            // By definition, the applicability condition should be evaluated on the validated bean and not on the
            // validated bean property. Thus we need to explicitely set the applicability condition on the validation
            // rule otherwise the default applicability condition to be evaluated on the property value.
            if (applicabilityCondition != null) {
                propertyRule.setApplicabilityCondition(applicabilityCondition);
            }

            if (applicableContexts != null) {
                propertyRule.setContextTokens(applicableContexts);
            }

            configuration.addPropertyRule(propertyName, propertyRule);
        }

    }

    /**
     * By default the element handlers handle and produce rules that can be associated with both global and non-global
     * contexts.
     */
    public boolean isConditionGloballyScoped(Element element) {
        return false;
    }

    /**
     * Extracts the validation rule error code from the given element. Expects a "code" attribute to indicate the
     * error code. If no such attribute exisits, returns <code>null</code>.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule error code.
     */
    protected String extractErrorCode(Element element) {
        String code = element.getAttribute(AbstractPropertyValidationElementHandler.ERROR_CODE_ATTR);
        return (StringUtils.hasLength(code)) ? code : null;
    }

    /**
     * Extracts the validation rule error message from the given element. Expects a "message" attribute to indicate the
     * error message. If no such attribute exisits, returns {@link #extractErrorCode(org.w3c.dom.Element)} instead.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule error message.
     */
    protected String extractMessage(Element element) {
        String message = element.getAttribute(AbstractPropertyValidationElementHandler.MESSAGE_ATTR);
        return (StringUtils.hasLength(message)) ? message : null;
    }

    /**
     * Extracts the validation rule error arguments from the given element. Expects an "args" attribute to indicate the
     * error arguments. If no such attribute exisits, returns an empty array.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule error arguments.
     */
    protected ErrorArgumentsResolver extractArgumentsResolver(Element element) {
        String argsString = element.getAttribute(ARGS_ATTR);
        String[] expressions = (argsString == null) ? new String[0] : StringUtils.tokenizeToStringArray(argsString, ", ");
        if (expressions.length == 0) {
            return null;
        }
        return new FunctionErrorArgumentsResolver(expressions, functionExpressionParser);
    }

    /**
     * Extracts the validation rule applicability condition from the given element. Expects an "apply-if" attribute to
     * indicate the condition expression. If no such attribute exisits, an {@link org.springmodules.validation.util.condition.common.AlwaysTrueCondition} is returned. The
     * configured {@link org.springmodules.validation.util.cel.ConditionExpressionParser} is used to parse the found expression to a condition.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule applicability condition.
     */
    protected Condition extractApplicabilityCondition(Element element) {
        String expression = element.getAttribute(APPLY_IF_ATTR);
        return (StringUtils.hasText(expression)) ? conditionExpressionParser.parse(expression) : null;
    }

    /**
     * Extracts the names of the validation context in which the valiation rule is applicable. Expects a "contexts"
     * attribute to hold a comma-separated list of context names. If no such attribute exists or if it holds an empty
     * string, <code>null </code> is returned. As the contract of {@link AbstractValidationRule#setContextTokens(String[])}
     * defines <code>null</code> means that the rule always applies regardless of the context.
     *
     * @param element The element that represents the validation rule.
     * @return The names of the validation contexts in which the
     */
    protected String[] extractApplicableContexts(Element element) {
        String contextString = element.getAttribute(CONTEXTS_ATTR);
        return (StringUtils.hasText(contextString)) ? StringUtils.commaDelimitedListToStringArray(contextString) : null;
    }

    /**
     * @see ConditionExpressionBased#setConditionExpressionParser(org.springmodules.validation.util.cel.ConditionExpressionParser)
     */
    public void setConditionExpressionParser(ConditionExpressionParser conditionExpressionParser) {
        this.conditionExpressionParser = conditionExpressionParser;
    }

    protected ConditionExpressionParser getConditionExpressionParser() {
        return conditionExpressionParser;
    }

    /**
     * @see FunctionExpressionBased#setFunctionExpressionParser(org.springmodules.validation.util.fel.FunctionExpressionParser)
     */
    public void setFunctionExpressionParser(FunctionExpressionParser functionExpressionParser) {
        this.functionExpressionParser = functionExpressionParser;
    }

    protected FunctionExpressionParser getFunctionExpressionParser() {
        return functionExpressionParser;
    }

    /**
     * Creates the validation rule represented and initialized by and with the given element.
     *
     * @param element The element that represents the validation rule.
     * @return The newly created validation rule.
     */
    protected abstract AbstractValidationRule createValidationRule(Element element);

}
