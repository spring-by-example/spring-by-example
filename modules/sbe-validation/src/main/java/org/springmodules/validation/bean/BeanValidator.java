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

package org.springmodules.validation.bean;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.bean.conf.BeanValidationConfiguration;
import org.springmodules.validation.bean.conf.CascadeValidation;
import org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationLoader;
import org.springmodules.validation.bean.conf.loader.xml.DefaultXmlBeanValidationConfigurationLoader;
import org.springmodules.validation.bean.converter.ErrorCodeConverter;
import org.springmodules.validation.bean.converter.ModelAwareErrorCodeConverter;
import org.springmodules.validation.bean.rule.ValidationRule;
import org.springmodules.validation.util.condition.Condition;

/**
 * An {@link org.springmodules.validation.validator.AbstractTypeSpecificValidator} implementation that applies all validation rules
 * on a bean of a specific type, based on an appropriate {@link org.springmodules.validation.bean.conf.BeanValidationConfiguration}. The validation
 * configuration is loaded per bean type by the configured {@link BeanValidationConfigurationLoader}.
 *
 * @author Uri Boness
 */
public class BeanValidator extends RuleBasedValidator {

    private final static Logger logger = LoggerFactory.getLogger(BeanValidator.class);

    private final static String PROPERTY_KEY_PREFIX = "[";

    private final static String PROPERTY_KEY_SUFFIX = "]";

    private BeanValidationConfigurationLoader configurationLoader;

    private ErrorCodeConverter errorCodeConverter;

    private boolean shortCircuitFieldValidation = true;

    /**
     * Constructs a new BeanValidator. By default the
     * {@link org.springmodules.validation.bean.conf.loader.SimpleBeanValidationConfigurationLoader} is
     * used as the bean validation configuration loader.
     */
    public BeanValidator() {
        this(new DefaultXmlBeanValidationConfigurationLoader());
    }

    /**
     * Constructs a new BeanValidator for the given bean class using the given validation configuration loader.
     *
     * @param configurationLoader The {@link org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationLoader} that is used to load the bean validation
     * configuration.
     */
    public BeanValidator(BeanValidationConfigurationLoader configurationLoader) {
        this.configurationLoader = configurationLoader;
        errorCodeConverter = new ModelAwareErrorCodeConverter();
    }

    /**
     * This validator supports only those classes that are supported by the validation configuration loader it uses.
     *
     * @see org.springmodules.validation.bean.RuleBasedValidator#supports(Class)
     * @see org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationLoader#supports(Class)
     */
    public boolean supports(Class clazz) {
        return configurationLoader.supports(clazz) || super.supports(clazz);
    }

    /**
     * Applies all validation rules as defined in the {@link org.springmodules.validation.bean.conf.BeanValidationConfiguration} retrieved for the given
     * bean from the configured {@link org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationLoader}.
     *
     * @see Validator#validate(Object, org.springframework.validation.Errors)
     */
    public void validate(Object obj, Errors errors) {

        // validation the object graph using the class validation manager.
        validateObjectGraphConstraints(obj, obj, errors, new HashSet());

        // applying the registered validation rules.
        super.validate(obj, errors);
    }

    //============================================== Setter/Getter =====================================================

    /**
     * Sets the error code converter this validator will use to resolve the error codes to be registered with the
     * {@link Errors} object.
     *
     * @param errorCodeConverter The error code converter this validator will use to resolve the error
     * different error codes.
     */
    public void setErrorCodeConverter(ErrorCodeConverter errorCodeConverter) {
        this.errorCodeConverter = errorCodeConverter;
    }

    /**
     * Sets the bean validation configuration loader this validator will use to load the bean validation configurations.
     *
     * @param configurationLoader The loader this validator will use to load the bean validation configurations.
     */
    public void setConfigurationLoader(BeanValidationConfigurationLoader configurationLoader) {
        this.configurationLoader = configurationLoader;
    }

    /**
     * Determines whether field validation will be short-ciruite, that is, if multiple validation rules are defined
     * on a field, the first rule to fail will stop the validation process for that field. By default the field
     * validation <b>will</b> be short-circuited.
     *
     * @param shortCircuitFieldValidation
     */
    public void setShortCircuitFieldValidation(boolean shortCircuitFieldValidation) {
        this.shortCircuitFieldValidation = shortCircuitFieldValidation;
    }

    //=============================================== Helper Methods ===================================================

    /**
     * The heart of this validator. This is a recursive method that validates the given object (object) under the
     * context of the given object graph root (root). The validation rules to be applied are loaded using the
     * configured {@link org.springmodules.validation.bean.conf.loader.BeanValidationConfigurationLoader}. All errors are registered with the given {@link Errors}
     * object under the context of the object graph root.
     *
     * @param root The root of the object graph.
     * @param obj The object to be validated
     * @param errors The {@link Errors} instance where the validation errors will be registered.
     * @param validatedObjects keeps track of all the validated objects (to prevent revalidating the same objects when
     * a circular relationship exists).
     */
    protected void validateObjectGraphConstraints(Object root, Object obj, Errors errors, Set validatedObjects) {

        // cannot load any validation rules for null values
        if (obj == null) {
            return;
        }

        // if this object was already validated, the skipping this valiation.
        if (validatedObjects.contains(obj)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Skipping validation of object in path '" + errors.getObjectName() +
                    "' for it was already validated");
            }
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Validating object in path '" + errors.getNestedPath() + "'");
        }

        // loading the bean validation configuration based on the validated object class.
        Class clazz = obj.getClass();
        BeanValidationConfiguration configuration = configurationLoader.loadConfiguration(clazz);

        if (configuration == null) {
            return; // no validation configuration for this object, then there's nothing to validate.
        }

        // applying all the validation rules for the object and registering the object as "validated"
        applyBeanValidation(configuration, obj, errors);
        validatedObjects.add(obj);

        // after all the validation rules where applied, checking what properties of the object require their own
        // validation and recursively calling this method on them.
        CascadeValidation[] cascadeValidations = configuration.getCascadeValidations();
        BeanWrapper wrapper = wrapBean(obj);
        for (int i = 0; i < cascadeValidations.length; i++) {
            CascadeValidation cascadeValidation = cascadeValidations[i];
            Condition applicabilityCondition = cascadeValidation.getApplicabilityCondition();

            if (!applicabilityCondition.check(obj)) {
                continue;
            }

            String propertyName = cascadeValidation.getPropertyName();
            Class propertyType = wrapper.getPropertyType(propertyName);
            Object propertyValue = wrapper.getPropertyValue(propertyName);

            // if the property value is not there nothing to validate.
            if (propertyValue == null) {
                continue;
            }

            // if the property is an array of a collection, then iterating on it and validating each element. Note that
            // the error codes that are registered for arrays/collection elements follow the pattern supported by
            // spring's PropertyAccessor. Also note that just before each recursive call, the context of the validation
            // is appropriately adjusted using errors.pushNestedPath(...), and after each call it is being adjusted back
            // using errors.popNestedPath().
            if (propertyType.isArray()) {
                validateArrayProperty(root, propertyValue, propertyName, errors, validatedObjects);
            } else if (List.class.isAssignableFrom(propertyType) || Set.class.isAssignableFrom(propertyType)) {
                validateListOrSetProperty(root, (Collection) propertyValue, propertyName, errors, validatedObjects);
            } else if (Map.class.isAssignableFrom(propertyType)) {
                validateMapProperty(root, ((Map) propertyValue), propertyName, errors, validatedObjects);
            } else {
                // if the object is just a normal object (not an array or a collection), then applying its
                // validation rules.
                validatedSubBean(root, propertyValue, propertyName, errors, validatedObjects);
            }
        }
    }

    /**
     * Wraps the given bean in a {@link BeanWrapper}.
     *
     * @param bean The bean to be wraped.
     * @return The bean wrapper that wraps the given bean.
     */
    protected BeanWrapper wrapBean(Object bean) {
        return new BeanWrapperImpl(bean);
    }

    /**
     * Validates the elements of the given array property.
     *
     * @param root The root of the object graph that is being validated.
     * @param array The given array.
     * @param propertyName The name of the array property.
     * @param errors The {@link Errors} instance where all validation errors will be registered.
     * @param validatedObjects A registry of all objects that were already validated.
     */
    protected void validateArrayProperty(
        Object root,
        Object array,
        String propertyName,
        Errors errors,
        Set validatedObjects) {

        for (int i = 0; i < Array.getLength(array); i++) {
            String nestedPath = propertyName + PROPERTY_KEY_PREFIX + i + PROPERTY_KEY_SUFFIX;
            errors.pushNestedPath(nestedPath);
            validateObjectGraphConstraints(root, Array.get(array, i), errors, validatedObjects);
            errors.popNestedPath();
        }
    }

    /**
     * Validates the elements of the given list or set property.
     *
     * @param root The root of the object graph that is being validated.
     * @param collection The given list or set.
     * @param propertyName The name of the array property.
     * @param errors The {@link Errors} instance where all validation errors will be registered.
     * @param validatedObjects A registry of all objects that were already validated.
     */
    protected void validateListOrSetProperty(
        Object root,
        Collection collection,
        String propertyName,
        Errors errors,
        Set validatedObjects) {

        int i = 0;
        for (Iterator iter = collection.iterator(); iter.hasNext();) {
            Object element = iter.next();
            String nestedPath = propertyName + PROPERTY_KEY_PREFIX + i + PROPERTY_KEY_SUFFIX;
            errors.pushNestedPath(nestedPath);
            validateObjectGraphConstraints(root, element, errors, validatedObjects);
            errors.popNestedPath();
            i++;
        }
    }

    /**
     * Validates the elements within the given map property.
     *
     * @param root The root of the object graph that is being validated.
     * @param map The given map or set.
     * @param propertyName The name of the array property.
     * @param errors The {@link Errors} instance where all validation errors will be registered.
     * @param validatedObjects A registry of all objects that were already validated.
     */
    protected void validateMapProperty(Object root, Map map, String propertyName, Errors errors, Set validatedObjects) {
        for (Iterator entries = map.entrySet().iterator(); entries.hasNext();) {
            Entry entry = (Entry) entries.next();
            Object key = entry.getKey();
            if (!(key instanceof String)) {
                // skipping validation of elements that are mapped to non-string keys for there is no proper
                // representation of such elements as property path.
                continue;
            }
            Object value = entry.getValue();
            String nestedPath = propertyName + PROPERTY_KEY_PREFIX + String.valueOf(key) + PROPERTY_KEY_SUFFIX;
            errors.pushNestedPath(nestedPath);
            validateObjectGraphConstraints(root, value, errors, validatedObjects);
            errors.popNestedPath();
        }
    }

    /**
     * Validates the given nested property bean (sub-bean).
     *
     * @param root The root of the object graph that is being validated.
     * @param subBean The given nested property value (the sub-bean).
     * @param propertyName The name of the array property.
     * @param errors The {@link Errors} instance where all validation errors will be registered.
     * @param validatedObjects A registry of all objects that were already validated.
     */
    protected void validatedSubBean(
        Object root,
        Object subBean,
        String propertyName,
        Errors errors,
        Set validatedObjects) {

        errors.pushNestedPath(propertyName);
        validateObjectGraphConstraints(root, subBean, errors, validatedObjects);
        errors.popNestedPath();
    }

    /**
     * Applying the validation rules listed in the given validation configuration on the given object, and registering
     * all validation errors with the given {@link Errors} object.
     *
     * @param configuration The bean validation configuration that define the validation rules to be applied.
     * @param obj The validated object.
     * @param errors The {@link Errors} instance where the validation error will be registered.
     */
    protected void applyBeanValidation(BeanValidationConfiguration configuration, Object obj, Errors errors) {
        if (logger.isDebugEnabled()) {
            logger.debug("Validating global rules...");
        }
        applyGlobalValidationRules(configuration, obj, errors);

        if (logger.isDebugEnabled()) {
            logger.debug("Validating properties rules...");
        }
        applyPropertiesValidationRules(configuration, obj, errors);

        if (logger.isDebugEnabled()) {
            logger.debug("Executing custom validator...");
        }
        applyCustomValidator(configuration, obj, errors);
    }

    /**
     * Applies the global validation rules as listed in the given validation configuration on the given object, and
     * registering all global validation errors with the given {@link Errors}.
     *
     * @param configuration The bean validation configuration that holds all the global validation rules.
     * @param obj The validated object.
     * @param errors The {@link Errors} instance where all global validation errors will be registered.
     */
    protected void applyGlobalValidationRules(BeanValidationConfiguration configuration, Object obj, Errors errors) {
        ValidationRule[] globalRules = configuration.getGlobalRules();
        for (int i = 0; i < globalRules.length; i++) {
            ValidationRule rule = globalRules[i];
            if (rule.isApplicable(obj) && !rule.getCondition().check(obj)) {
                String errorCode = errorCodeConverter.convertGlobalErrorCode(rule.getErrorCode(), obj.getClass());

                // if there is a nested path in errors, the global errors should be registered as field errors
                // for the nested path. Otherwise, they should be registered as global errors. Starting from Spring 2.0-rc2
                // this is actually not required - it's just enough to call rejectValue() with null as the field name,
                // but we keep this implementation for now to support earlier versions.

                if (StringUtils.hasLength(errors.getNestedPath())) {
                    String nestedPath = errors.getNestedPath();
                    String propertyName = nestedPath.substring(0, nestedPath.length() - 1);
                    errors.popNestedPath();
                    errors.rejectValue(propertyName, errorCode, rule.getErrorArguments(obj), rule.getDefaultErrorMessage());
                    errors.pushNestedPath(propertyName);
                } else {
                    errors.reject(errorCode, rule.getErrorArguments(obj), rule.getDefaultErrorMessage());
                }
            }
        }
    }

    /**
     * Applies the property validation rules as listed in the given validation configuration on the given object, and
     * registering all property validation errors with the given {@link Errors}.
     *
     * @param configuration The bean validation configuration that holds all the property validation rules.
     * @param obj The validated object.
     * @param errors The {@link Errors} instance where all property validation errors will be registered
     * (see {@link Errors#rejectValue(String, String)}).
     */
    protected void applyPropertiesValidationRules(BeanValidationConfiguration configuration, Object obj, Errors errors) {
        String[] propertyNames = configuration.getValidatedProperties();
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = propertyNames[i];
            if (logger.isDebugEnabled()) {
                logger.debug("Validating property '" + propertyName + "' rules...");
            }
            ValidationRule[] rules = configuration.getPropertyRules(propertyName);

            // only allow one error to be associated with a property at once. This is to prevent situations where
            // dependent validation rules will fail. An example can be a "minLength()" validation rule that is dependent
            // on "notNull()" rule (there is not length to a null value), in this case, if the "notNull()" rule
            // produces an error, the "minLength()" rule should not be applied.
            validateAndShortCircuitRules(rules, propertyName, obj, errors);
        }
    }

    /**
     * Applying the given validation rules on the given property of the given object. The validation stops as soon as
     * one of the validation rules produces validation errors. This errors are then registered with the given
     * {@link Errors) instance.
     *
     * @param rules The validation rules that should be applied on the given property of the given object.
     * @param propertyName The name of the property to be validated.
     * @param obj The validated object.
     * @param errors The {@link Errors} instance where the validation errors will be registered.
     */
    protected void validateAndShortCircuitRules(ValidationRule[] rules, String propertyName, Object obj, Errors errors) {
        for (int i = 0; i < rules.length; i++) {
            ValidationRule rule = rules[i];
            if (rule.isApplicable(obj) && !rule.getCondition().check(obj)) {
                String errorCode = errorCodeConverter.convertPropertyErrorCode(rule.getErrorCode(), obj.getClass(), propertyName);
                errors.rejectValue(propertyName, errorCode, rule.getErrorArguments(obj), rule.getDefaultErrorMessage());
                if (shortCircuitFieldValidation) {
                    return;
                }
            }
        }
    }

    /**
     * Applies the custom validator of the given configuration (if one exists) on the given object.
     *
     * @param configuration The configuration from which the custom validator will be taken from.
     * @param obj The object to be validated.
     * @param errors The {@link Errors} instance where all validation errors will be registered.
     */
    protected void applyCustomValidator(BeanValidationConfiguration configuration, Object obj, Errors errors) {
        Validator validator = configuration.getCustomValidator();
        if (validator != null) {
            if (validator.supports(obj.getClass())) {
                validator.validate(obj, errors);
            }
        }
    }

}
