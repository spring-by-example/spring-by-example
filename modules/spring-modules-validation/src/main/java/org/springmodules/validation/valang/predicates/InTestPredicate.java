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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AnyPredicate;
import org.apache.commons.collections.functors.FalsePredicate;
import org.springmodules.validation.valang.functions.Function;
import org.springmodules.validation.valang.functions.LiteralFunction;

/**
 * Tests if a value is in a list.
 *
 * @author David Winterfeldt
 */
public class InTestPredicate extends AbstractPropertyPredicate {
    
    /**
     * <p>Constructor taking two functions and an operator.
     *
     * @param leftFunction the left function
     * @param operator the operator.
     */
    public InTestPredicate(Function leftFunction, Operator operator, Function rightFunction, int line, int column) {
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
        
        if (leftValue instanceof Number) {
            leftValue = new BigDecimal(leftValue.toString());
        }
        if (rightValue instanceof Number) {
            rightValue = new BigDecimal(rightValue.toString());
        }

        // FIX ME: make all collections and lists more efficient (also handle arrays)
        if (rightValue instanceof Set) {
            Set<?> lComparisonValues = (Set<?>) rightValue;
            
            return lComparisonValues.contains(leftValue);
        } else {
            Collection<Predicate> predicates = new ArrayList<Predicate>();
            
            for (Iterator iter = getIterator(rightValue); iter.hasNext();) {
                Object o = iter.next();
                if (o instanceof Function) {
                    predicates.add(new EqualsTestPredicate(new LiteralFunction(leftValue), Operator.EQUAL, (Function) o, getLine(), getColumn()));
                } else {
                    predicates.add(new EqualsTestPredicate(new LiteralFunction(leftValue), Operator.EQUAL, new LiteralFunction(o), getLine(), getColumn()));
                }
            }
            
            if (predicates.isEmpty()) {
                throw new IllegalStateException("IN expression contains no elements!");
            } else if (predicates.size() == 1) {
                predicates.add(FalsePredicate.getInstance());
            }

            return AnyPredicate.getInstance(predicates).evaluate(target);
        }
    }

  }
