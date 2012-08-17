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
import java.util.Date;

import junit.framework.TestCase;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.springmodules.validation.util.date.DateParseException;
import org.springmodules.validation.util.date.DefaultDateParser;
import org.springmodules.validation.valang.functions.BeanPropertyFunction;
import org.springmodules.validation.valang.functions.LiteralFunction;

/**
 * @author Steven Devijver
 * @since 23-04-2005
 */
public class GenericTestPredicateTest extends TestCase {

    public void testNullOperatorSuccess() {
        assertTrue(null, Operator.NULL, null);
        assertFalse("five", Operator.NULL, null);
    }

    public void testNotNullOperatorSuccess() {
        assertTrue("five", Operator.NOT_NULL, null);
        assertFalse(null, Operator.NOT_NULL, null);
    }

    public void testEqualOperatorSuccess() {
        assertTrue(5, Operator.EQUAL, 5);
        assertFalse(6, Operator.EQUAL, 5);
    }

    public void testNotEqualOperatorSuccess() {
        assertTrue(5, Operator.NOT_EQUAL, 6);
        assertFalse(5, Operator.NOT_EQUAL, 5);
    }

    public void testLessThanOperatorSuccess() {
        assertTrue(5, Operator.LESS_THAN, 6);
        assertFalse(6, Operator.LESS_THAN, 5);
    }

    public void testLessThanOrEqualOperatorSuccess() {
        assertTrue(5, Operator.LESS_THAN_OR_EQUAL, 5);
        assertTrue(5, Operator.LESS_THAN_OR_EQUAL, 6);
        assertFalse(6, Operator.LESS_THAN_OR_EQUAL, 5);
    }

    public void testMoreThanOperatorSuccess() {
        assertTrue(6, Operator.GREATER_THAN, 5);
        assertFalse(5, Operator.GREATER_THAN, 6);
    }

    public void testMoreThanOrEqualOperatorSuccess() {
        assertTrue(5, Operator.GREATER_THAN_OR_EQUAL, 5);
        assertTrue(6, Operator.GREATER_THAN_OR_EQUAL, 5);
        assertFalse(5, Operator.GREATER_THAN_OR_EQUAL, 6);
    }

    public void testStringInNotInOperatorSuccess() {
        Collection coll = new ArrayList();
        coll.add(new LiteralFunction(new BigDecimal("1")));
        coll.add(new LiteralFunction(new BigDecimal("2")));
        coll.add(new LiteralFunction(new BigDecimal("3")));
        coll.add(new LiteralFunction(new BigDecimal("4")));
        coll.add(new LiteralFunction(new BigDecimal("5")));

        assertTrue(5, Operator.IN, coll);
        assertFalse(6, Operator.IN, coll);
        assertTrue(5, Operator.IN, coll.iterator());
        assertFalse(6, Operator.IN, coll.iterator());
        assertTrue(5, Operator.IN, new IteratorEnumeration(coll.iterator()));
        assertFalse(6, Operator.IN, new IteratorEnumeration(coll.iterator()));
        assertTrue(5, Operator.IN, coll.toArray());
        assertFalse(6, Operator.IN, coll.toArray());

        assertTrue(6, Operator.NOT_IN, coll);
        assertFalse(5, Operator.NOT_IN, coll);
        assertTrue(6, Operator.NOT_IN, coll.iterator());
        assertFalse(5, Operator.NOT_IN, coll.iterator());
        assertTrue(6, Operator.NOT_IN, new IteratorEnumeration(coll.iterator()));
        assertFalse(5, Operator.NOT_IN, new IteratorEnumeration(coll.iterator()));
        assertTrue(6, Operator.NOT_IN, coll.toArray());
        assertFalse(5, Operator.NOT_IN, coll.toArray());
    }

    public void testBetweenNotBetweenOperatorSuccess() {
        Collection coll = new ArrayList();
        coll.add(new LiteralFunction(new BigDecimal("1")));
        coll.add(new LiteralFunction(new BigDecimal("5")));

        assertTrue(5, Operator.BETWEEN, coll);
        assertFalse(6, Operator.BETWEEN, coll);
        assertTrue(5, Operator.BETWEEN, coll.iterator());
        assertFalse(6, Operator.BETWEEN, coll.iterator());
        assertTrue(5, Operator.BETWEEN, new IteratorEnumeration(coll.iterator()));
        assertFalse(6, Operator.BETWEEN, new IteratorEnumeration(coll.iterator()));
        assertTrue(5, Operator.BETWEEN, coll.toArray());
        assertFalse(6, Operator.BETWEEN, coll.toArray());

        assertTrue(6, Operator.NOT_BETWEEN, coll);
        assertFalse(5, Operator.NOT_BETWEEN, coll);
        assertTrue(6, Operator.NOT_BETWEEN, coll.iterator());
        assertFalse(5, Operator.NOT_BETWEEN, coll.iterator());
        assertTrue(6, Operator.NOT_BETWEEN, new IteratorEnumeration(coll.iterator()));
        assertFalse(5, Operator.NOT_BETWEEN, new IteratorEnumeration(coll.iterator()));
        assertTrue(6, Operator.NOT_BETWEEN, coll.toArray());
        assertFalse(5, Operator.NOT_BETWEEN, coll.toArray());
    }

    public void testStringEqualsOperatorSuccess() {
        assertTrue("five", Operator.EQUAL, "five");
        assertFalse("five", Operator.EQUAL, "six");
    }

    public void testStringNotEqualsOperatorSuccess() {
        assertTrue("five", Operator.NOT_EQUAL, "six");
        assertFalse("five", Operator.NOT_EQUAL, "five");
    }

    public void testMoreThanOperatorFail() {
        try {
            assertTrue("six", Operator.GREATER_THAN, "five");
            fail();
        } catch (Exception e) {
        }
    }

    public void testInNotInOperatorSuccess() {
        Collection coll = new ArrayList();
        coll.add(new LiteralFunction("one"));
        coll.add(new LiteralFunction("two"));
        coll.add(new LiteralFunction("three"));
        coll.add(new LiteralFunction("four"));
        coll.add(new LiteralFunction("five"));

        assertTrue("five", Operator.IN, coll);
        assertFalse("six", Operator.IN, coll);

        assertTrue("six", Operator.NOT_IN, coll);
        assertFalse("five", Operator.NOT_IN, coll);
    }

    public void testNullNotNullOperatorSuccess() {
        assertTrue(null, Operator.NULL, null);
        assertFalse("five", Operator.NULL, null);

        assertTrue("five", Operator.NOT_NULL, null);
        assertFalse(null, Operator.NOT_NULL, null);
    }

    public void testHasTextHasNoTextOperatorSuccess() {
        assertTrue("five", Operator.HAS_TEXT, null);
        assertFalse("    ", Operator.HAS_TEXT, null);
        assertFalse(null, Operator.HAS_TEXT, null);

        assertTrue("    ", Operator.HAS_NO_TEXT, null);
        assertTrue(null, Operator.HAS_NO_TEXT, null);
        assertFalse("five", Operator.HAS_NO_TEXT, null);
    }

    public void testHasLengthHasNotLengthOperatorSuccess() {
        assertTrue("five", Operator.HAS_LENGTH, null);
        assertFalse("", Operator.HAS_LENGTH, null);
        assertFalse(null, Operator.HAS_LENGTH, null);

        assertTrue("", Operator.HAS_NO_LENGTH, null);
        assertTrue(null, Operator.HAS_NO_LENGTH, null);
        assertFalse("five", Operator.HAS_NO_LENGTH, null);
    }

    public void testIsBlankIsNotBlankOperatorSuccess() {
        assertTrue("", Operator.IS_BLANK, null);
        assertTrue(null, Operator.IS_BLANK, null);
        assertFalse("five", Operator.IS_BLANK, null);

        assertTrue("five", Operator.IS_NOT_BLANK, null);
        assertFalse("", Operator.IS_NOT_BLANK, null);
        assertFalse(null, Operator.IS_NOT_BLANK, null);
    }

    public void testIsWordIsNotWordOperatorSuccess() {
        assertTrue("five", Operator.IS_WORD, null);
        assertFalse("five six", Operator.IS_WORD, null);

        assertTrue("five six", Operator.IS_NOT_WORD, null);
        assertFalse("five", Operator.IS_NOT_WORD, null);
    }

    public void testIsUpperCaseIsNotUpperCaseOperatorSuccess() {
        assertTrue("FIVE", Operator.IS_UPPERCASE, null);
        assertFalse("five", Operator.IS_UPPERCASE, null);

        assertTrue("five", Operator.IS_NOT_UPPERCASE, null);
        assertFalse("FIVE", Operator.IS_NOT_UPPERCASE, null);
    }

    public void testIsLowerCaseIsNotLowerCaseOperatorSuccess() {
        assertTrue("five", Operator.IS_LOWERCASE, null);
        assertFalse("Five", Operator.IS_LOWERCASE, null);

        assertTrue("Five", Operator.IS_NOT_LOWERCASE, null);
        assertFalse("five", Operator.IS_NOT_LOWERCASE, null);
    }

    public void testBooleanEqualsOperatorSuccess() {
        assertTrue(Boolean.TRUE, Operator.EQUAL, Boolean.TRUE);
        assertFalse(Boolean.TRUE, Operator.EQUAL, Boolean.FALSE);
    }

    public void testBooleanNotEqualOperatorSuccess() {
        assertTrue(Boolean.TRUE, Operator.NOT_EQUAL, Boolean.FALSE);
        assertFalse(Boolean.TRUE, Operator.NOT_EQUAL, Boolean.TRUE);
    }

    public void testDateEqualsOperatorSuccess() {
        assertTrue(md("20050409"), Operator.EQUAL, md("2005-04-09"));
        assertFalse(md("20050409"), Operator.EQUAL, md("2005-04-08"));
    }

    public void testDateNotEqualOperatorSuccess() {
        assertTrue(md("20050409"), Operator.NOT_EQUAL, md("2005-04-08"));
        assertFalse(md("20050409"), Operator.NOT_EQUAL, md("2005-04-09"));
    }

    public void testDateLessThanOperatorSuccess() {
        assertTrue(md("20050409"), Operator.LESS_THAN, md("2005-04-10"));
        assertFalse(md("20050409"), Operator.LESS_THAN, md("2005-04-08"));
    }

    public void testDateLessThanOrEqualOperatorSuccess() {
        assertTrue(md("20050409"), Operator.LESS_THAN_OR_EQUAL, md("2005-04-10"));
        assertFalse(md("20050409"), Operator.LESS_THAN_OR_EQUAL, md("2005-04-08"));
    }

    public void testDateMoreThanOperatorSuccess() {
        assertTrue(md("20050409"), Operator.GREATER_THAN, md("2005-04-08"));
        assertFalse(md("20050409"), Operator.GREATER_THAN, md("2005-04-09"));
    }

    public void testDateMoreThanOrEqualOperatorSuccess() {
        assertTrue(md("20050409"), Operator.GREATER_THAN_OR_EQUAL, md("2005-04-08"));
        assertFalse(md("20050409"), Operator.GREATER_THAN_OR_EQUAL, md("2005-04-10"));
    }

    public Date md(String s) {
        try {
            return new DefaultDateParser().parse(s);
        } catch (DateParseException e) {
            throw new RuntimeException(e);
        }
    }


    public GenericTestPredicateTest() {
        super();
    }

    public GenericTestPredicateTest(String arg0) {
        super(arg0);
    }

    public class GenericContainer {

        private Object value = null;

        public GenericContainer(Object value) {
            super();
            this.value = value;
        }

        public Object getValue() {
            return this.value;
        }
    }

    private boolean runTest(Object leftValue, Operator operator, Object rightValue) {
        return new GenericTestPredicate(new BeanPropertyFunction("value"), operator, new LiteralFunction(rightValue), 0, 0).evaluate(new GenericContainer(leftValue));
    }

    private void assertTrue(Object leftValue, Operator operator, Object rightValue) {
        assertTrue(runTest(leftValue, operator, rightValue));
    }

    private void assertFalse(Object leftValue, Operator operator, Object rightValue) {
        assertFalse(runTest(leftValue, operator, rightValue));
    }

    private void assertTrue(int leftValue, Operator operator, int rightValue) {
        assertTrue(new Integer(leftValue), operator, new Integer(rightValue));
    }

    private void assertFalse(int leftValue, Operator operator, int rightValue) {
        assertFalse(new Integer(leftValue), operator, new Integer(rightValue));
    }

    private void assertTrue(int leftValue, Operator operator, Object rightValue) {
        assertTrue(new Integer(leftValue), operator, rightValue);
    }

    private void assertFalse(int leftValue, Operator operator, Object rightValue) {
        assertFalse(new Integer(leftValue), operator, rightValue);
    }
}
