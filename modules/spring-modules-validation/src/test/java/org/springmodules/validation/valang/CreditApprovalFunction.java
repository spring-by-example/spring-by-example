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

package org.springmodules.validation.valang;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springmodules.validation.valang.functions.AbstractFunction;
import org.springmodules.validation.valang.functions.ConfigurableFunction;
import org.springmodules.validation.valang.functions.Function;

/**
 * Checks if a person can get credit approval.
 * 
 * @author David Winterfeldt
 *
 */
public class CreditApprovalFunction extends AbstractFunction 
        implements ConfigurableFunction {

    final Logger logger = LoggerFactory.getLogger(CreditApprovalFunction.class);
    
    Set<Person.CreditRating> lCreditRatings = new HashSet<Person.CreditRating>();

    /**
     * Constructor
     */
    public CreditApprovalFunction() {}

    /**
     * Constructor
     */
    public CreditApprovalFunction(Function[] arguments, int line, int column) {
        super(arguments, line, column);
        definedExactNumberOfArguments(2);
        
        lCreditRatings.add(Person.CreditRating.FAIR);
        lCreditRatings.add(Person.CreditRating.GOOD);
        lCreditRatings.add(Person.CreditRating.EXCELLENT);
    }

    /**
     * Gets number of expected arguments.
     * Implementation of <code>ConfigurableFunction</code>.
     */
    public int getExpectedNumberOfArguments() {
        return 2;
    }

    /**
     * Sets arguments, line number, and column number.
     * Implementation of <code>ConfigurableFunction</code>.
     */
    public void setArguments(int expectedNumberOfArguments, Function[] arguments,
            int line, int column) {
        // important to set template first or can cause a NullPointerException 
        // if number of arguments don't match the expected number since 
        // the template is used to create the exception
        super.setTemplate(line, column);
        super.setArguments(arguments);
        super.definedExactNumberOfArguments(expectedNumberOfArguments);
    }
    
    /**
     * Sets valid credit rating approval list.
     */
    public void setCreditRatingList(Set<Person.CreditRating> lCreditRatings) {
        this.lCreditRatings = lCreditRatings;
    }

    /**
     * If age is over 18, check if the person has good credit, 
     * and otherwise reject.
     * 
     * @return      Object      Returns a <code>boolean</code> for 
     *                          whether or not the person has good enough 
     *                          credit to get approval.
     */
    @Override
    protected Object doGetResult(Object target) {
        boolean result = true;
        
        int age = (Integer) getArguments()[0].getResult(target);
        Person.CreditRating creditRating = (Person.CreditRating)getArguments()[1].getResult(target);

        // must be over 18 to get credit approval
        if (age > 18) {
            if (!lCreditRatings.contains(creditRating)) {
                result = false;
            }
        }
        
        return result;
    }
    
}
