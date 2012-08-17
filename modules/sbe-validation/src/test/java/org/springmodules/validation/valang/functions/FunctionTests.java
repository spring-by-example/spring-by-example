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

package org.springmodules.validation.valang.functions;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.springframework.util.Assert;
import org.springmodules.validation.valang.ValangException;

public class FunctionTests extends TestCase {

    public FunctionTests() {
        super();
    }

    public FunctionTests(String arg0) {
        super(arg0);
    }

    private Function getLengthOfFunction(Object value) {
        return new LengthOfFunction(new Function[]{new LiteralFunction(value)}, 1, 1);
    }

    private Function getUpperCaseFunction(Object value) {
        return new UpperCaseFunction(new Function[]{new LiteralFunction(value)}, 1, 1);
    }

    private Function getLowerCaseFunction(Object value) {
        return new LowerCaseFunction(new Function[]{new LiteralFunction(value)}, 1, 1);
    }

    private Function getNotFunction(Object value) {
        return new NotFunction(new Function[]{new LiteralFunction(value)}, 1, 1);
    }

    private Function getRegExpFunction(String pattern, String str) {
        return new RegExFunction(new Function[]{new LiteralFunction(pattern), new LiteralFunction(str)}, 1, 1);
    }

    private Function getEmailFunction(String email) {
        return new EmailFunction(new Function[]{new LiteralFunction(email)}, 1, 1);
    }

    public void testLengthOfFunctionSuccess() {

        // testing the length function on a string object
        Integer result = (Integer) getLengthOfFunction("test").getResult(null);
        assertEquals("length of stringt 'test' is 4", result.intValue(), 4);

        // testing the length function on a collection
        List list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        result = (Integer) getLengthOfFunction(list).getResult(null);
        assertEquals("length of the collection should be 3", result.intValue(), 3);

        // testing the length function on an array
        Object[] array = new Object[]{"1", "2", "3", "4", "5"};
        result = (Integer) getLengthOfFunction(array).getResult(null);
        assertEquals("length of the array should be 5", result.intValue(), 5);
    }

    public void testLengthOfFunctionFail() {
        try {
            getLengthOfFunction(null).getResult(null);
            fail("LengthOfFunction should throw ValangException!");
        } catch (ValangException e) {
            Assert.isInstanceOf(NullPointerException.class, e.getCause(), "Cause is not NullPointerException!");
        }
    }

    public void testUpperCaseFunctionSuccess() {
        String result = (String) getUpperCaseFunction("test").getResult(null);
        assertEquals("TEST", result);
    }

    public void testUpperCaseFunctionFail() {
        try {
            getUpperCaseFunction(null).getResult(null);
            fail("UpperCaseFunction should throw ValangException!");
        } catch (ValangException e) {
            Assert.isInstanceOf(NullPointerException.class, e.getCause(), "Cause is not NullPointerException!");
        }
    }

    public void testLowerCaseFunctionSuccess() {
        String result = (String) getLowerCaseFunction("tEst").getResult(null);
        assertEquals("test", result);
    }

    public void testLowerCaseFunctionFail() {
        try {
            getLowerCaseFunction(null).getResult(null);
            fail("LowerCaseFunction should throw ValangException!");
        } catch (ValangException e) {
            Assert.isInstanceOf(NullPointerException.class, e.getCause(), "Cause is not NullPointerException!");
        }
    }

    public void testNotFunctionSuccess() {
        Boolean result = (Boolean) getNotFunction(Boolean.FALSE).getResult(null);
        assertEquals(Boolean.TRUE, result);
    }

    public void testRegExpFunctionSuccess() {
        Boolean result = (Boolean) getRegExpFunction("foo|bar", "bar").getResult(null);
        assertEquals(Boolean.TRUE, result);
    }

    public void testEmailFunctionSuccess() {
        Boolean result = (Boolean) getEmailFunction("hello@world.com").getResult(null);
        assertEquals(Boolean.TRUE, result);
    }

    public void testEmailFunctionFail() {
        Boolean result = (Boolean) getEmailFunction("hello@world").getResult(null);
        assertEquals(Boolean.FALSE, result);
    }
}
