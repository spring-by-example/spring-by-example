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

package org.springmodules.validation.commons;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springmodules.validation.commons.validwhen.ValidWhenLexer;
import org.springmodules.validation.commons.validwhen.ValidWhenParser;

/**
 * This class contains the default validations that are used in the validator-rules.xml file.
 * <p/>
 * In general passing in a <code>null</code> or blank will return a <code>null</code> <code>Object</code> or a
 * <code>false</code> <code>boolean</code>. However, <code>null</code>s and blanks do not result in an error being added to
 * the <code>Error</code>s.
 *
 * @author David Winterfeldt
 * @author James Turner
 * @author Rob Leland
 * @author Daniel Miller
 * @author Rob Harrop
 */
public class FieldChecks implements Serializable {


    /**
     * <code>Log</code> used by this class.
     */
    private static final Logger log = LoggerFactory.getLogger(FieldChecks.class);

    public static final String FIELD_TEST_NULL = "NULL";

    public static final String FIELD_TEST_NOTNULL = "NOTNULL";

    public static final String FIELD_TEST_EQUAL = "EQUAL";

    /**
     * Checks if the field isn't null and length of the field is greater than zero not including whitespace.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being performed.
     * @param field The <code>Field</code> object associated with the current field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any validation errors occur.
     * @return <code>true</code> if meets stated requirements, <code>false</code> otherwise.
     */
    public static boolean validateRequired(Object bean, ValidatorAction va, Field field, Errors errors) {

        String value = FieldChecks.extractValue(bean, field);

        if (GenericValidator.isBlankOrNull(value)) {
            FieldChecks.rejectValue(errors, field, va);
            return false;
        } else {
            return true;
        }

    }

    /**
     * Checks if the field isn't null based on the values of other fields.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access other
     * field values.
     * -param request
     * Current request object.
     * @return true if meets stated requirements, false otherwise.
     */
    public static boolean validateRequiredIf(Object bean, ValidatorAction va,
                                             Field field, Errors errors,
                                             org.apache.commons.validator.Validator validator) {

        Object form = validator.getParameterValue(org.apache.commons.validator.Validator.BEAN_PARAM);

        boolean required = false;

        String value = FieldChecks.extractValue(bean, field);

        int i = 0;
        String fieldJoin = "AND";
        if (!GenericValidator.isBlankOrNull(field.getVarValue("fieldJoin"))) {
            fieldJoin = field.getVarValue("fieldJoin");
        }

        if (fieldJoin.equalsIgnoreCase("AND")) {
            required = true;
        }

        while (!GenericValidator.isBlankOrNull(field.getVarValue("field[" + i
            + "]"))) {
            String dependProp = field.getVarValue("field[" + i + "]");
            String dependTest = field.getVarValue("fieldTest[" + i + "]");
            String dependTestValue = field.getVarValue("fieldValue[" + i + "]");
            String dependIndexed = field.getVarValue("fieldIndexed[" + i + "]");

            if (dependIndexed == null) {
                dependIndexed = "false";
            }

            String dependVal = null;
            boolean thisRequired = false;
            if (field.isIndexed() && dependIndexed.equalsIgnoreCase("true")) {
                String key = field.getKey();
                if ((key.indexOf("[") > -1) && (key.indexOf("]") > -1)) {
                    String ind = key.substring(0, key.indexOf(".") + 1);
                    dependProp = ind + dependProp;
                }
            }

            dependVal = ValidatorUtils.getValueAsString(form, dependProp);
            if (dependTest.equals(FieldChecks.FIELD_TEST_NULL)) {
                thisRequired = (dependVal != null) && (dependVal.length() > 0);
            }

            if (dependTest.equals(FieldChecks.FIELD_TEST_NOTNULL)) {
                thisRequired = (dependVal != null) && (dependVal.length() > 0);
            }

            if (dependTest.equals(FieldChecks.FIELD_TEST_EQUAL)) {
                thisRequired = dependTestValue.equalsIgnoreCase(dependVal);
            }

            if (fieldJoin.equalsIgnoreCase("AND")) {
                required = required && thisRequired;
            } else {
                required = required || thisRequired;
            }

            i++;
        }

        if (required) {
            if (GenericValidator.isBlankOrNull(value)) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * Checks if the field matches the regular expression in the field's mask
     * attribute.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return true if field matches mask, false otherwise.
     */
    public static boolean validateMask(Object bean, ValidatorAction va,
                                       Field field, Errors errors) {
        String mask = field.getVarValue("mask");
        String value = FieldChecks.extractValue(bean, field);
        try {
            if (!GenericValidator.isBlankOrNull(value)
                && !GenericValidator.matchRegexp(value, mask)) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            } else {
                return true;
            }
        }
        catch (Exception e) {
            FieldChecks.log.error(e.getMessage(), e);
        }
        return true;
    }

    /**
     * Checks if the field can safely be converted to a byte primitive.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return A Byte if valid, null otherwise.
     */
    public static Byte validateByte(Object bean, ValidatorAction va,
                                    Field field, Errors errors) {

        Byte result = null;
        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)) {
            result = GenericTypeValidator.formatByte(value);
            if (result == null) {
                FieldChecks.rejectValue(errors, field, va);
            }
        }
        return result;
    }

    /**
     * Checks if the field can safely be converted to a short primitive.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return A Short if valid, otherwise null.
     */
    public static Short validateShort(Object bean, ValidatorAction va,
                                      Field field, Errors errors) {
        Short result = null;

        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)) {
            result = GenericTypeValidator.formatShort(value);
            if (result == null) {
                FieldChecks.rejectValue(errors, field, va);
            }
        }
        return result;
    }

    /**
     * Checks if the field can safely be converted to an int primitive.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return An Integer if valid, a null otherwise.
     */
    public static Integer validateInteger(Object bean, ValidatorAction va,
                                          Field field, Errors errors) {
        Integer result = null;

        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)) {
            result = GenericTypeValidator.formatInt(value);

            if (result == null) {
                FieldChecks.rejectValue(errors, field, va);
            }
        }

        return result;
    }

    /**
     * Checks if the field can safely be converted to a long primitive.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return A Long if valid, a null otherwise.
     */
    public static Long validateLong(Object bean, ValidatorAction va,
                                    Field field, Errors errors) {
        Long result = null;

        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)) {
            result = GenericTypeValidator.formatLong(value);

            if (result == null) {
                FieldChecks.rejectValue(errors, field, va);
            }
        }
        return result;
    }

    /**
     * Checks if the field can safely be converted to a float primitive.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return A Float if valid, a null otherwise.
     */
    public static Float validateFloat(Object bean, ValidatorAction va,
                                      Field field, Errors errors) {
        Float result = null;
        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)) {
            result = GenericTypeValidator.formatFloat(value);

            if (result == null) {
                FieldChecks.rejectValue(errors, field, va);
            }
        }

        return result;
    }

    /**
     * Checks if the field can safely be converted to a double primitive.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return A Double if valid, a null otherwise.
     */
    public static Double validateDouble(Object bean, ValidatorAction va,
                                        Field field, Errors errors) {
        Double result = null;
        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)) {
            result = GenericTypeValidator.formatDouble(value);

            if (result == null) {
                FieldChecks.rejectValue(errors, field, va);
            }
        }

        return result;
    }

    /**
     * Checks if the field is a valid date. If the field has a datePattern
     * variable, that will be used to format
     * <code>java.text.SimpleDateFormat</code>. If the field has a
     * datePatternStrict variable, that will be used to format
     * <code>java.text.SimpleDateFormat</code> and the length will be checked
     * so '2/12/1999' will not pass validation with the format 'MM/dd/yyyy'
     * because the month isn't two digits. If no datePattern variable is
     * specified, then the field gets the DateFormat.SHORT format for the
     * locale. The setLenient method is set to <code>false</code> for all
     * variations.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return A Date if valid, a null if blank or invalid.
     */
    public static Date validateDate(Object bean, ValidatorAction va,
                                    Field field, Errors errors) {

        Date result = null;
        String value = FieldChecks.extractValue(bean, field);
        String datePattern = field.getVarValue("datePattern");
        String datePatternStrict = field.getVarValue("datePatternStrict");

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                if (datePattern != null && datePattern.length() > 0) {
                    result = GenericTypeValidator.formatDate(value,
                        datePattern, false);
                } else if (datePatternStrict != null
                    && datePatternStrict.length() > 0) {
                    result = GenericTypeValidator.formatDate(value,
                        datePatternStrict, true);
                }
            }
            catch (Exception e) {
                FieldChecks.log.error(e.getMessage(), e);
            }

            if (result == null) {
                FieldChecks.rejectValue(errors, field, va);
            }
        }

        return result;
    }

    /**
     * Checks if a fields value is within a range (min &amp; max specified in
     * the vars attribute).
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return True if in range, false otherwise.
     * @deprecated As of Struts 1.1, replaced by
     *             {@see #validateIntRange(java.lang.Object,
     *             org.apache.validator.validator.ValidatorAction,
     *             org.apache.validator.validator.Field,
     *             org.apache.struts.action.Errors,
     *             javax.servlet.http.HttpServletRequest)}
     */
    public static boolean validateRange(Object bean, ValidatorAction va,
                                        Field field, Errors errors) {
        return FieldChecks.validateIntRange(bean, va, field, errors);
    }

    /**
     * Checks if a fields value is within a range (min &amp; max specified in
     * the vars attribute).
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * @return <code>true</code> if in range, <code>false</code> otherwise.
     */
    public static boolean validateIntRange(Object bean, ValidatorAction va,
                                           Field field, Errors errors) {
        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                int intValue = Integer.parseInt(value);
                int min = Integer.parseInt(field.getVarValue("min"));
                int max = Integer.parseInt(field.getVarValue("max"));

                if (!GenericValidator.isInRange(intValue, min, max)) {
                    FieldChecks.rejectValue(errors, field, va);

                    return false;
                }
            }
            catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a fields value is within a range (min &amp; max specified in
     * the vars attribute).
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return True if in range, false otherwise.
     */
    public static boolean validateDoubleRange(Object bean, ValidatorAction va,
                                              Field field, Errors errors) {

        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                double doubleValue = Double.parseDouble(value);
                double min = Double.parseDouble(field.getVarValue("min"));
                double max = Double.parseDouble(field.getVarValue("max"));

                if (!GenericValidator.isInRange(doubleValue, min, max)) {
                    FieldChecks.rejectValue(errors, field, va);

                    return false;
                }
            }
            catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a fields value is within a range (min &amp; max specified in
     * the vars attribute).
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return True if in range, false otherwise.
     */
    public static boolean validateFloatRange(Object bean, ValidatorAction va,
                                             Field field, Errors errors) {

        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                float floatValue = Float.parseFloat(value);
                float min = Float.parseFloat(field.getVarValue("min"));
                float max = Float.parseFloat(field.getVarValue("max"));
                if (!GenericValidator.isInRange(floatValue, min, max)) {
                    FieldChecks.rejectValue(errors, field, va);
                    return false;
                }
            }
            catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the field is a valid credit card number.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return The credit card as a Long, a null if invalid, blank, or null.
     */
    public static Long validateCreditCard(Object bean, ValidatorAction va,
                                          Field field, Errors errors) {

        Long result = null;
        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)) {
            result = GenericTypeValidator.formatCreditCard(value);

            if (result == null) {
                FieldChecks.rejectValue(errors, field, va);
            }
        }

        return result;
    }

    /**
     * Checks if a field has a valid e-mail address.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return True if valid, false otherwise.
     */
    public static boolean validateEmail(Object bean, ValidatorAction va,
                                        Field field, Errors errors) {

        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)
            && !GenericValidator.isEmail(value)) {
            FieldChecks.rejectValue(errors, field, va);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the field's length is less than or equal to the maximum value.
     * A <code>Null</code> will be considered an error.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return True if stated conditions met.
     */
    public static boolean validateMaxLength(Object bean, ValidatorAction va,
                                            Field field, Errors errors) {

        String value = FieldChecks.extractValue(bean, field);

        if (value != null) {
            try {
                int max = Integer.parseInt(field.getVarValue("maxlength"));

                if (!GenericValidator.maxLength(value, max)) {
                    FieldChecks.rejectValue(errors, field, va);

                    return false;
                }
            }
            catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the field's length is greater than or equal to the minimum
     * value. A <code>Null</code> will be considered an error.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return True if stated conditions met.
     */
    public static boolean validateMinLength(Object bean, ValidatorAction va,
                                            Field field, Errors errors) {

        String value = FieldChecks.extractValue(bean, field);

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                int min = Integer.parseInt(field.getVarValue("minlength"));

                if (!GenericValidator.minLength(value, min)) {
                    FieldChecks.rejectValue(errors, field, va);

                    return false;
                }
            }
            catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the field matches the boolean expression specified in
     * <code>test</code> parameter.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * @return <code>true</code> if meets stated requirements,
     *         <code>false</code> otherwise.
     */
    public static boolean validateValidWhen(
        Object bean,
        ValidatorAction va,
        Field field,
        Errors errors,
        Validator validator) {

        Object form = validator.getParameterValue(Validator.BEAN_PARAM);
        String value = null;
        boolean valid = false;
        int index = -1;

        if (field.isIndexed()) {
            String key = field.getKey();

            final int leftBracket = key.indexOf("[");
            final int rightBracket = key.indexOf("]");

            if ((leftBracket > -1) && (rightBracket > -1)) {
                index =
                    Integer.parseInt(key.substring(leftBracket + 1, rightBracket));
            }
        }

        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        String test = field.getVarValue("test");
        if (test == null) {
            String msg = "ValidWhen Error 'test' parameter is missing for field ' " + field.getKey() + "'";
            errors.rejectValue(field.getKey(), msg);
            log.error(msg);
            return false;
        }

        // Create the Lexer
        ValidWhenLexer lexer = null;
        try {
            lexer = new ValidWhenLexer(new StringReader(test));
        } catch (Exception ex) {
            String msg = "ValidWhenLexer Error for field ' " + field.getKey() + "' - " + ex;
            errors.rejectValue(field.getKey(), msg);
            log.error(msg);
            log.debug(msg, ex);
            return false;
        }

        // Create the Parser
        ValidWhenParser parser = null;
        try {
            parser = new ValidWhenParser(lexer);
        } catch (Exception ex) {
            String msg = "ValidWhenParser Error for field ' " + field.getKey() + "' - " + ex;
            errors.rejectValue(field.getKey(), msg);
            log.error(msg);
            log.debug(msg, ex);
            return false;
        }


        parser.setForm(form);
        parser.setIndex(index);
        parser.setValue(value);

        try {
            parser.expression();
            valid = parser.getResult();

        } catch (Exception ex) {
            String msg = "ValidWhen Error for field ' " + field.getKey() + "' - " + ex;
            errors.rejectValue(field.getKey(), msg);
            log.error(msg);
            log.debug(msg, ex);

            return false;
        }

        if (!valid) {
            rejectValue(errors, field, va);
            return false;
        }

        return true;
    }

    /**
     * Extracts the value of the given bean. If the bean is <code>null</code>, the returned value is also <code>null</code>.
     * If the bean is a <code>String</code> then the bean itself is returned. In all other cases, the <code>ValidatorUtils</code>
     * class is used to extract the bean value using the <code>Field</code> object supplied.
     *
     * @see ValidatorUtils#getValueAsString(Object, String)
     */
    protected static String extractValue(Object bean, Field field) {
        String value = null;

        if (bean == null) {
            return null;
        } else if (bean instanceof String) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        return value;
    }

    /**
     * Convinience method to perform the work of rejecting a field's value.
     *
     * @param errors the errors
     * @param field the field that was rejected
     * @param va the validator action
     */
    public static void rejectValue(Errors errors, Field field, ValidatorAction va) {
        String fieldCode = field.getKey();

        // this is required to translate the mapping access used by commons validator (delegated to commons beanutils)
        // which uses '(' and ')' to the notation used by spring validator (delegated to PropertyAccessor) which
        // uses '[' and ']'.
        fieldCode = fieldCode.replace('(', '[').replace(')', ']');

        String errorCode = MessageUtils.getMessageKey(va, field);
        Object[] args = MessageUtils.getArgs(va, field);

        if (FieldChecks.log.isDebugEnabled()) {
            FieldChecks.log.debug("Rejecting value [field='" + fieldCode + "', errorCode='"
                + errorCode + "']");
        }

        errors.rejectValue(fieldCode, errorCode, args, errorCode);
    }

    /**
     * Returns true if <code>obj</code> is null or a String.
     */
    private static boolean isString(Object obj) {
        return (obj == null) ? true : String.class.isInstance(obj);
    }
}
