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
package org.springmodules.validation.valang;

import java.util.Collection;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.valang.parser.SimpleValangBased;
import org.springmodules.validation.valang.parser.ValangParser;
import org.springmodules.validation.valang.predicates.ValidationRule;

/**
 * <p/>
 * An implementation of <code>Validator</code> that takes a Valang syntax string
 * to define the set of validation rules it will apply. This instance is thread-safe.
 * <p/>
 * The syntax of a Valang instruction is:
 * <p/>
 * <pre>
 * <p/>
 *  { &lt;key&gt; : &lt;expression&gt; : &lt;error_message&gt; [ : &lt;error_key&gt; [ : &lt;error_args&gt; ] ] }
 * <p/>
 * </pre>
 * <p/>
 * <p/>
 * These instructions can be repeated and will be combined in a Validator
 * instance. Each instruction will execute the expression on a target bean. If
 * the expression fails the key will be rejected with the error message, error
 * key and error arguments. If no error key is provided the key will be used as
 * error key.
 * <p/>
 * Some examples of the Valang syntax:
 * <p/>
 * <pre>
 * <p>
 *      &lt;bean id=&quot;myValidator&quot; class=&quot;org.springmodules.validation.valang.ValangValidatorFactoryBean&quot;&gt;
 *          &lt;property name=&quot;valang&quot;&gt;&lt;value&gt;&lt;![CDATA[
 *          { age : ? is not null : 'Age is a required field.' : 'age_required' }
 *          { age : ? is null or ? &gt;= minAge : 'Customers must be {0} years or older.' : 'not_old_enough' : minAge }
 *          { valueDate : ? is not null : 'Value date is a required field.' : 'valueDate_required' }
 *          { valueDate : ? is null or (? &gt;= [T&lt;d] and [T&gt;d] &gt; ?) :
 *                  'Value date must be today.' : 'valueDate_today' }
 *          { firstName : ? has text : 'First name is a required field.' : 'firstName_required' }
 *          { firstName : ? has no text or length(firstName) &lt;= 50 :
 *                  'First name must be no longer than {0} characters.' : 'firstName_length' : 50 }
 *          { size : ? has length : 'Size is a required field.' }
 *          { size : ? has no length or upper(?) in 'S', 'M', 'L', 'XL' :
 *                  'Size must be either {0}, {1}, {2} or {3}.' : 'size_error' : 'S', 'M', 'L', 'XL' }
 *          { lastName : ? has text and !(false) = true :
 *                  'Last name is required and not false must be true.' }
 *          ]]&gt;&lt;/value&gt;&lt;/property&gt;
 *      &lt;/bean&gt;
 * </p>
 * </pre>
 * <p>Enums can be dynamically resolved either based on comparing an enum type to a literal delimitted by
 * "['" + &lt;enum&gt; + "']" or it will be directly resovled if the complete path is specified.</p>
 * <pre>
 * <p>
 *      &lt;bean id=&quot;myValidator&quot; class=&quot;org.springmodules.validation.valang.ValangValidatorFactoryBean&quot;&gt;
 *          &lt;property name=&quot;valang&quot;&gt;&lt;value&gt;&lt;![CDATA[
 *          { personType : ? EQUALS ['STUDENT'] : 'Person type must be student.' }
 *          { personType : personType EQUALS ['org.springmodules.validation.example.PersonType.STUDENT'] : 'Person type must be student.' }
 *          { personType : personType EQUALS ['org.springmodules.validation.example.Person$PersonType.STUDENT'] : 'Person type must be student.' }
 *          ]]&gt;&lt;/value&gt;&lt;/property&gt;
 *      &lt;/bean&gt;
 * </p>
 * </pre>
 * <p>Where clauses are very similar to a SQL WHERE clause.  It lets you short circuit the validation in
 * case there are some rules that should only be applied after other criteria have been satisfied.  A where
 * clause doesn't generate any errors, and a where clause is optional.</p>
 * <pre>
 * <p>
 *      &lt;bean id=&quot;myValidator&quot; class=&quot;org.springmodules.validation.valang.ValangValidatorFactoryBean&quot;&gt;
 *          &lt;property name=&quot;valang&quot;&gt;&lt;value&gt;&lt;![CDATA[
 *          { age : ? > 18 WHERE lastName EQUALS 'Smith' : 'Age must be greater than 18 if your last name is Smith.' : 'valueDate_required' }
 *          ]]&gt;&lt;/value&gt;&lt;/property&gt;
 *      &lt;/bean&gt;
 * </p>
 * </pre>
 * <p/>
 * Custom property editors can be registered using
 * org.springmodules.validation.valang.CustomPropertyEditor.
 * <p>A custom visitor can be registered to use custom functions in the Valang
 * syntax.</p>
 *
 * <p>If the class name is set it will be be used for bytecode generation
 * to avoid reflection.</p>
 *
 * <p><strong>Note</strong>: By specifying the class name the <code>Validator</code>
 * will only be able to validate the class specified</p>
 *
 * @author Steven Devijver
 * @see org.springmodules.validation.util.date.DefaultDateParser
 * @see org.springframework.validation.Validator
 * @since 23-04-2005
 */
public class ValangValidator extends SimpleValangBased implements Validator, InitializingBean {

    private String valang = null;
    private String className = null;
    private Class<?> supportClass = null;
    private Collection<CustomPropertyEditor> customPropertyEditors = null;
    private Collection<ValidationRule> rules = null;

    /**
     * Constructor
     */
    public ValangValidator() {
        super();
    }

    /**
     * This property sets the Valang syntax.
     *
     * @param   valang      The Valang validation expression.
     */
    public void setValang(String valang) {
        this.valang = valang;
    }

    /**
     * <p>Sets the class name to be used for bytecode generation
     * to avoid reflection.</p>
     *
     * <p><strong>Note</strong>: By specifying this <code>Validator</code>
     * will only be able to validate the class specified</p>
     *
     * @param   className       The fully qualified class name to perform validation on.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Gets custom property editors.
     */
    private Collection<CustomPropertyEditor> getCustomPropertyEditors() {
        return this.customPropertyEditors;
    }

    /**
     * Sets custom property editors on BeanWrapper instances (optional).
     *
     * @param customPropertyEditors the custom editors.
     * @see BeanWrapper#registerCustomEditor(Class, String,
     *      java.beans.PropertyEditor)
     * @see BeanWrapper#registerCustomEditor(Class,
     *      java.beans.PropertyEditor)
     */
    public void setCustomPropertyEditors(Collection<CustomPropertyEditor> customPropertyEditors) {
        this.customPropertyEditors = customPropertyEditors;
    }

    /**
     * Gets Valang expression.
     */
    private String getValang() {
        return this.valang;
    }

    /**
     * Gets list of validation rules.
     */
    public Collection<ValidationRule> getRules() {
        return rules;
    }

    /**
     * Implementation of <code>InitializingBean</code>.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasLength(getValang(), "'valang' property must be set!");

        ValangParser parser = null;

        if (!StringUtils.hasText(className)) {
            parser = createValangParser(valang);
        } else {
            parser = createValangParser(valang, className);

            // if className is set, this is the only supported class
            // for this validator
            supportClass = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
        }

        rules = parser.parseValidation();
    }

    /**
     * What validation class is supported.
     * Implementation of <code>Validator</code>.
     */
    @Override
    public boolean supports(Class clazz) {
        boolean result = true;

        if (supportClass != null) {
            result = supportClass.isAssignableFrom(clazz);
        }

        return result;
    }

    /**
     * <p>Validate the supplied <code>target</code> object, which must be
     * of a {@link Class} for which the {@link #supports(Class)} method
     * typically has (or would) return <code>true</code>.</p>
     *
     * <p>Implementation of <code>Validator</code>.</p>
     */
    @Override
    public void validate(Object target, Errors errors) {
        Object bean = null;
        BeanWrapper beanWrapper = null;

        if (!StringUtils.hasText(className)) {
            beanWrapper = (target instanceof BeanWrapper) ? (BeanWrapper) target : new BeanWrapperImpl(target);

            if (getCustomPropertyEditors() != null) {
                for (CustomPropertyEditor customPropertyEditor : getCustomPropertyEditors()) {
                    if (customPropertyEditor.getRequiredType() == null) {
                        throw new IllegalArgumentException("[requiredType] is required on CustomPropertyEditor instances!");
                    } else if (customPropertyEditor.getPropertyEditor() == null) {
                        throw new IllegalArgumentException("[propertyEditor] is required on CustomPropertyEditor instances!");
                    }

                    if (StringUtils.hasLength(customPropertyEditor.getPropertyPath())) {
                        beanWrapper.registerCustomEditor(customPropertyEditor.getRequiredType(),
                            customPropertyEditor.getPropertyPath(), customPropertyEditor.getPropertyEditor());
                    } else {
                        beanWrapper.registerCustomEditor(customPropertyEditor.getRequiredType(),
                            customPropertyEditor.getPropertyEditor());
                    }
                }
            }

            bean = beanWrapper;
        } else {
            bean = target;
        }

        for (ValidationRule rule : rules) {
            rule.validate(bean, errors);
        }
    }

}