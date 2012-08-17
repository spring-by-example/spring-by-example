package org.springmodules.validation.bean.conf.loader.xml.handler;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration;
import org.springmodules.validation.bean.rule.AbstractValidationRule;
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
public abstract class AbstractClassValidationElementHandler
    implements ClassValidationElementHandler, ConditionExpressionBased, FunctionExpressionBased {

    private static final String ERROR_CODE_ATTR = "code";

    private static final String MESSAGE_ATTR = "message";

    private static final String ARGS_ATTR = "args";

    private static final String APPLY_IF_ATTR = "apply-if";

    private static final String CONTEXTS_ATTR = "contexts";

    private String elementName;

    private String namespaceUrl;

    private ConditionExpressionParser conditionExpressionParser;

    private FunctionExpressionParser functionExpressionParser;

    /**
     * Constructs a new AbstractPropertyValidationElementHandler with given supported element name.
     *
     * @param elementName The supported element name.
     */
    public AbstractClassValidationElementHandler(String elementName) {
        this(elementName, null);
    }

    /**
     * Constructs a new AbstractPropertyValidationElementHandler with given supported element name and namespace.
     *
     * @param elementName The supported element name.
     * @param namespace The supported namespace.
     */
    public AbstractClassValidationElementHandler(String elementName, String namespace) {
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
    public AbstractClassValidationElementHandler(
        String elementName,
        String namespace,
        ConditionExpressionParser conditionExpressionParser,
        FunctionExpressionParser functionExpressionParser) {

        this.elementName = elementName;
        this.namespaceUrl = namespace;
        this.conditionExpressionParser = conditionExpressionParser;
        this.functionExpressionParser = functionExpressionParser;
    }

    /**
     * Determines whether the given element is supported by this handler. The check is done by comparing the element
     * tag name and namespace with the ones that are configured with this handler.
     *
     * @see org.springmodules.validation.bean.conf.loader.xml.handler.PropertyValidationElementHandler#supports(org.w3c.dom.Element, Class, java.beans.PropertyDescriptor)
     */
    public boolean supports(Element element, Class clazz) {
        String localName = element.getLocalName();
        if (!localName.equals(elementName)) {
            return false;
        }
        String ns = element.getNamespaceURI();
        return ObjectUtils.nullSafeEquals(ns, namespaceUrl);
    }

    /**
     * Creates the appropriate {@link org.springmodules.validation.bean.rule.ValidationRule} based on the given element
     * and adds it to the given configuration.
     *
     * @see org.springmodules.validation.bean.conf.loader.xml.handler.AbstractClassValidationElementHandler#handle(org.w3c.dom.Element, org.springmodules.validation.bean.conf.MutableBeanValidationConfiguration)
     */
    public void handle(Element element, MutableBeanValidationConfiguration configuration) {

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

        configuration.addGlobalRule(rule);
    }

    /**
     * By default the element handlers handle and produce rules that can be associated with both global and non-global
     * contexts.
     */
    public boolean isConditionGloballyScoped() {
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
        String code = element.getAttribute(AbstractClassValidationElementHandler.ERROR_CODE_ATTR);
        return (StringUtils.hasLength(code)) ? code : null;
    }

    /**
     * Extracts the validation rule error message from the given element. Expects a "message" attribute to indicate the
     * error message. If no such attribute exisits, returns <code>null</code> instead.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule error message.
     */
    protected String extractMessage(Element element) {
        String message = element.getAttribute(AbstractClassValidationElementHandler.MESSAGE_ATTR);
        return (StringUtils.hasLength(message)) ? message : null;
    }

    /**
     * Extracts the validation rule error arguments from the given element. Expects an "args" attribute to indicate the
     * error arguments. If no such attribute exisits, returns <code>null</code>.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule error arguments.
     */
    protected ErrorArgumentsResolver extractArgumentsResolver(Element element) {
        String argsString = element.getAttribute(AbstractClassValidationElementHandler.ARGS_ATTR);
        String[] expressions = (argsString == null) ? new String[0] : StringUtils.tokenizeToStringArray(argsString, ", ");
        if (expressions.length == 0) {
            return null;
        }
        return new FunctionErrorArgumentsResolver(expressions, functionExpressionParser);
    }

    /**
     * Extracts the validation rule applicability condition from the given element. Expects an "apply-if" attribute to
     * indicate the condition expression. If no such attribute exisits, returns <code>null</code>.
     *
     * @param element The element that represents the validation rule.
     * @return The validation rule applicability condition.
     */
    protected Condition extractApplicabilityCondition(Element element) {
        String expression = element.getAttribute(AbstractClassValidationElementHandler.APPLY_IF_ATTR);
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

    /**
     * Returns the condition expression parser associated with this handler.
     *
     * @return The condition expression parser associated with this handler.
     */
    protected ConditionExpressionParser getConditionExpressionParser() {
        return conditionExpressionParser;
    }

    /**
     * @see FunctionExpressionBased#setFunctionExpressionParser(org.springmodules.validation.util.fel.FunctionExpressionParser)
     */
    public void setFunctionExpressionParser(FunctionExpressionParser functionExpressionParser) {
        this.functionExpressionParser = functionExpressionParser;
    }

    /**
     * Returns the {@link FunctionExpressionParser} used by this handler to parse the error argument expressions.
     *
     * @return The {@link FunctionExpressionParser} used by this handler to parse the error argument expressions.
     */
    protected FunctionExpressionParser getFunctionExpressionParser() {
        return functionExpressionParser;
    }

    /**
     * Indicates whether the validation rule supports null values. Null values support means such values will be
     * passed to the rule condition during validation. If the rule doesn't support null values, such value will not
     * be evaluated by the rule condition and the validation will treat them as valid values.
     *
     * @return <code>true</code> if the validation rule support null values, <code>false</code> otherwise.
     */
    protected boolean isNullSupported() {
        return false;
    }

    /**
     * Creates the validation rule represented and initialized by and with the given element.
     *
     * @param element The element that represents the validation rule.
     * @return The newly created validation rule.
     */
    protected abstract AbstractValidationRule createValidationRule(Element element);

}
