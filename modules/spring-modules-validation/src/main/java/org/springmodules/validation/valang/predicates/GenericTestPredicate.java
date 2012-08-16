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

package org.springmodules.validation.valang.predicates;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springmodules.validation.valang.functions.Function;

/**
 * <p>GenericTestPredicate can test if a property value is null or not null.
 *
 * @author Steven Devijver
 * @since 23-04-2005
 */
public class GenericTestPredicate extends AbstractPropertyPredicate {

    final Logger logger = LoggerFactory.getLogger(GenericTestPredicate.class);
    
    private final EqualsTestPredicate equalsTestPredicate;
    private final NotEqualsTestPredicate notEqualsTestPredicate;
    private final LessThanTestPredicate lessThanTestPredicate;
    private final LessThanOrEqualTestPredicate lessThanOrEqualTestPredicate;
    private final GreaterThanTestPredicate greaterThanTestPredicate;
    private final GreaterThanOrEqualTestPredicate greaterThanOrEqualTestPredicate;
    private final InTestPredicate inTestPredicate;
    private final NotInTestPredicate notInTestPredicate;
    private final BetweenTestPredicate betweenTestPredicate;
    private final NotBetweenTestPredicate notBetweenTestPredicate;
    
    /**
     * <p>Constructor taking two functions and an operator.
     *
     * @param leftFunction the left function
     * @param operator the operator.
     */
    public GenericTestPredicate(Function leftFunction, Operator operator, Function rightFunction, int line, int column) {
        super(leftFunction, operator, rightFunction, line, column);
        
        this.equalsTestPredicate = new EqualsTestPredicate(leftFunction, operator, rightFunction, line, column);
        this.notEqualsTestPredicate = new NotEqualsTestPredicate(leftFunction, operator, rightFunction, line, column);
        this.lessThanTestPredicate = new LessThanTestPredicate(leftFunction, operator, rightFunction, line, column);
        this.lessThanOrEqualTestPredicate = new LessThanOrEqualTestPredicate(leftFunction, operator, rightFunction, line, column);
        this.greaterThanTestPredicate = new GreaterThanTestPredicate(leftFunction, operator, rightFunction, line, column);
        this.greaterThanOrEqualTestPredicate = new GreaterThanOrEqualTestPredicate(leftFunction, operator, rightFunction, line, column);
        this.inTestPredicate = new InTestPredicate(leftFunction, operator, rightFunction, line, column);
        this.notInTestPredicate = new NotInTestPredicate(leftFunction, operator, rightFunction, line, column);
        this.betweenTestPredicate = new BetweenTestPredicate(leftFunction, operator, rightFunction, line, column);
        this.notBetweenTestPredicate = new NotBetweenTestPredicate(leftFunction, operator, rightFunction, line, column);
    }


    /**
     * <p>The evaluate method takes the result of both functions and tests with the operator.
     *
     * @param   target      The target bean.
     * @return  boolean      Whether or not the test passed.
     */
    public boolean evaluate(Object target) {
        // constructor checks that left function is never null
        Object leftValue = getLeftFunction().getResult(target);
        Object rightValue = (getRightFunction() != null ? getRightFunction().getResult(target) : null);

        switch(getOperator()) {
            case NULL:
                return leftValue == null;
            case NOT_NULL:
                return leftValue != null;
            case EQUAL:
                return equalsTestPredicate.evaluate(target);
            case NOT_EQUAL:
                return notEqualsTestPredicate.evaluate(target);
            case LESS_THAN:
                return lessThanTestPredicate.evaluate(target);
            case LESS_THAN_OR_EQUAL:
                return lessThanOrEqualTestPredicate.evaluate(target);
            case GREATER_THAN:
                return greaterThanTestPredicate.evaluate(target);
            case GREATER_THAN_OR_EQUAL:
                return greaterThanOrEqualTestPredicate.evaluate(target);
            case IN:
                return inTestPredicate.evaluate(target);
            case NOT_IN:
                return notInTestPredicate.evaluate(target);
            case BETWEEN:
                return betweenTestPredicate.evaluate(target);
            case NOT_BETWEEN:
                return notBetweenTestPredicate.evaluate(target);
            case HAS_LENGTH:
                return org.springframework.util.StringUtils.hasLength(leftValue != null ? leftValue.toString() : null);
            case HAS_NO_LENGTH:
                return !org.springframework.util.StringUtils.hasLength(leftValue != null ? leftValue.toString() : null);
            case HAS_TEXT:
                return org.springframework.util.StringUtils.hasText(leftValue != null ? leftValue.toString() : null);
            case HAS_NO_TEXT:
                return !org.springframework.util.StringUtils.hasText(leftValue != null ? leftValue.toString() : null);
            case IS_BLANK:
                return isBlank(leftValue != null ? leftValue.toString() : null);
            case IS_NOT_BLANK:
                return !isBlank(leftValue != null ? leftValue.toString() : null);
            case IS_WORD:
                return isWord(leftValue != null ? leftValue.toString() : null);
            case IS_NOT_WORD:
                return !isWord(leftValue != null ? leftValue.toString() : null);
            case IS_LOWERCASE:
                return isLowerCase(leftValue != null ? leftValue.toString() : null);
            case IS_NOT_LOWERCASE:
                return !isLowerCase(leftValue != null ? leftValue.toString() : null);
            case IS_UPPERCASE:
                return isUpperCase(leftValue != null ? leftValue.toString() : null);
            case IS_NOT_UPPERCASE:
                return !isUpperCase(leftValue != null ? leftValue.toString() : null);
        }
        
        throw new IllegalStateException("Operator class [" + getOperator().getClass().getName() + "] not supported!");
    }

    /**
     * Checks if the value is a word.
     */
    private boolean isWord(String value) {
        return StringUtils.isAlphanumeric(value);
    }

    /**
     * Checks if the value is lower case.
     */
    private boolean isLowerCase(String value) {
        return (value.length() > 0 && value.toLowerCase().equals(value));
    }

    /**
     * Checks if the value is upper case.
     */
    private boolean isUpperCase(String value) {
        return value.length() > 0 && value.toUpperCase().equals(value);
    }

    /**
     * Checks if the value is blank.
     */
    private boolean isBlank(String value) {
        return StringUtils.isBlank(value);
    }
    
}
