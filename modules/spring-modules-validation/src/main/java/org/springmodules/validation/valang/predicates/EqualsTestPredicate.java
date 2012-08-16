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

import java.math.BigDecimal;
import java.util.Date;

import org.springmodules.validation.valang.ValangException;
import org.springmodules.validation.valang.functions.EnumLiteralFunction;
import org.springmodules.validation.valang.functions.Function;

/**
 * Tests if a value is equal to another.
 *
 * @author David Winterfeldt
 */
public class EqualsTestPredicate extends AbstractPropertyPredicate {

    /**
     * <p>Constructor taking two functions and an operator.
     *
     * @param leftFunction the left function
     * @param operator the operator.
     */
    public EqualsTestPredicate(Function leftFunction, Operator operator, Function rightFunction, int line, int column) {
        super(leftFunction, operator, rightFunction, line, column);
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
        
        if (leftValue instanceof Number && rightValue instanceof Number) {
            if (!(leftValue instanceof BigDecimal)) {
                leftValue = new BigDecimal(leftValue.toString());
            }
            if (!(rightValue instanceof BigDecimal)) {
                rightValue = new BigDecimal(rightValue.toString());
            }

            return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) == 0;
        } else if (leftValue instanceof Date && rightValue instanceof Date) {
            return ((Date) leftValue).getTime() == ((Date) rightValue).getTime();
        // if one variable is an enum, convert other to a string and try to match
        } else if (getLeftFunction() instanceof EnumLiteralFunction || getRightFunction() instanceof EnumLiteralFunction) {
            Enum<?> enumValue = null;
            Enum<?> convertedEnumValue = null;
            String value = null;
            
            try {
                if (getRightFunction() instanceof EnumLiteralFunction) {
                    enumValue = (Enum<?>) leftValue;
                    value = rightValue.toString().trim();                       
                } else if (getLeftFunction() instanceof EnumLiteralFunction) {
                    enumValue = (Enum<?>) rightValue;
                    value = leftValue.toString().trim();
                }

                // tried generating bytecode, but was twice as slow
                Class<?> enumClass = enumValue.getClass();
                convertedEnumValue = (Enum<?>) enumClass.getField(value).get(null);

                return enumValue.equals(convertedEnumValue);
            } catch (Throwable e) {
                throw new ValangException("Field [" + value + "] isn't an enum value on " + enumValue.getClass().getName() + ".", 
                                          getLine(), getColumn());
            }
        } else {
            return leftValue.equals(rightValue);
        }
    }

}
