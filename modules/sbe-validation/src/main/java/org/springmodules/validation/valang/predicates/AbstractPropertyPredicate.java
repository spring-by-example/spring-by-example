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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.util.Assert;
import org.springmodules.validation.valang.functions.Function;

/**
 * <p>AbstractPropertyPredicate deals with extracting values from properties and as such
 * is a utilty base class.
 *
 * @author Steven Devijver
 * @since 23-04-2005
 */
public abstract class AbstractPropertyPredicate implements Predicate {

    private final Function leftFunction;
    private final Operator operator;
    private final Function rightFunction;
    private final int line;
    private final int column;

    /**
     * Constructor
     */
    public AbstractPropertyPredicate(Function leftFunction, Operator operator, Function rightFunction, 
                                     int line, int column) {
        Assert.notNull(leftFunction, "Left function parameter should not be null!.");
        Assert.notNull(operator, "Operator parameter should not be null!.");
        
        this.leftFunction = leftFunction;
        this.operator = operator;
        this.rightFunction = rightFunction;
        this.line = line;
        this.column = column;
    }

    /**
     * Gets left function.
     */
    public final Function getLeftFunction() {
        return this.leftFunction;
    }
    
    /**
     * Gets operator.
     */
    public final Operator getOperator() {
        return this.operator;
    }

    /**
     * Gets right function.
     */
    public final Function getRightFunction() {
        return this.rightFunction;
    }

    /**
     * Gets line.
     */
    public int getLine() {
        return line;
    }

    /**
     * Sets line.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Gets <code>Iterator</code>.
     */
    protected final Iterator getIterator(Object literal) {
        if (literal instanceof Collection) {
            return ((Collection) literal).iterator();
        } else if (literal instanceof Iterator) {
            return (Iterator) literal;
        } else if (literal instanceof Enumeration) {
            return IteratorUtils.asIterator((Enumeration) literal);
        } else if (literal instanceof Object[]) {
            return IteratorUtils.arrayIterator((Object[]) literal);
        } else {
            throw new ClassCastException("Could not convert literal value to iterator!");
        }
    }

    /**
     * Gets array.
     */
    protected final Object[] getArray(Object literal) {
        if (literal instanceof Collection) {
            return ((Collection) literal).toArray();
        } else if (literal instanceof Iterator) {
            return IteratorUtils.toArray((Iterator) literal);
        } else if (literal instanceof Enumeration) {
            return IteratorUtils.toArray(IteratorUtils.asIterator((Enumeration) literal));
        } else if (literal instanceof Object[]) {
            return (Object[]) literal;
        } else {
            throw new ClassCastException("Could not convert literal value to array!");
        }
    }

    /**
     * Evaluate expression.
     */
    public abstract boolean evaluate(Object target);

//    protected abstract Predicate getPredicate(Function leftFunction, Operator operator, Function rightFunction, int line, int column);
    
}
