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

import org.springmodules.validation.valang.functions.Function;

/**
 * Tests if a value is not equal to another.
 *
 * @author David Winterfeldt
 */
public class NotEqualsTestPredicate extends EqualsTestPredicate {

    /**
     * <p>Constructor taking two functions and an operator.
     *
     * @param leftFunction the left function
     * @param operator the operator.
     */
    public NotEqualsTestPredicate(Function leftFunction, Operator operator, Function rightFunction, int line, int column) {
        super(leftFunction, operator, rightFunction, line, column);
    }


    /**
     * <p>The evaluate method takes the result of both functions and tests with the operator.
     *
     * @param   target      The target bean.
     * @return  boolean      Whether or not the test passed.
     */
    public boolean evaluate(Object target) {
        return !super.evaluate(target);
    }

}
