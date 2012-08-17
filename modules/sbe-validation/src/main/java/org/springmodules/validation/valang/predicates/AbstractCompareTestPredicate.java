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
import org.springmodules.validation.valang.functions.Function;


/**
 * Base class for comparing less than and greater than comparisons.
 *
 * @author David Winterfeldt
 */
public abstract class AbstractCompareTestPredicate extends AbstractPropertyPredicate {

    private final String operatorSymbol;
    
    /**
     * <p>Constructor taking two functions and an operator.
     *
     * @param leftFunction the left function
     * @param operator the operator.
     */
    public AbstractCompareTestPredicate(Function leftFunction, Operator operator, Function rightFunction, 
                                        int line, int column, String operatorSymbol) {
        super(leftFunction, operator, rightFunction, line, column);
        this.operatorSymbol = operatorSymbol;
    }


    /**
     * <p>The evaluate method takes the result of both functions and tests with the operator.
     *
     * @param   target      The target bean.
     * @return  boolean      Whether or not the test passed.
     */
    public final boolean evaluate(Object target) {
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
            
            return doEvaluate((BigDecimal) leftValue, (BigDecimal) rightValue);
        } else if (leftValue instanceof Date && rightValue instanceof Date) {
            return doEvaluate((Date) leftValue, (Date) rightValue);
        } else {
            throw new ValangException("The " + operatorSymbol  + " operator only supports two date or two number values!", getLine(), getColumn());
        }
    }
    
    /**
     * Evaluates a <code>BigDecimal</code> and is called by 
     * <code>evaluate</code>.
     */
    protected abstract boolean doEvaluate(BigDecimal leftValue, BigDecimal rightValue);

    /**
     * Evaluates a <code>Date</code> and is called by 
     * <code>evaluate</code>.
     */
    protected abstract boolean doEvaluate(Date leftValue, Date rightValue);

}
