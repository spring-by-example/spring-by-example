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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author Steven Devijver
 * @since 29-04-2005
 */
public class BeanPropertyFunctionTest extends TestCase {

    public BeanPropertyFunctionTest() {
        super();
    }

    public BeanPropertyFunctionTest(String arg0) {
        super(arg0);
    }

    public class Customer {

        private String name = null;

        public Customer() {
            super();
        }

        public Customer(String name) {
            this();
            setName(name);
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public void test1() {
        BeanPropertyFunction f = new BeanPropertyFunction("name");
        assertEquals("Steven", f.getResult(new Customer("Steven")));
    }

    public void test2() {
        BeanPropertyFunction f = new BeanPropertyFunction("name");
        BeanWrapper bw = new BeanWrapperImpl(new Customer("Steven"));
        assertEquals("Steven", f.getResult(bw));
    }

    public void test3() {
        BeanPropertyFunction f = new BeanPropertyFunction("customer.address.city");
        Map address = new HashMap();
        address.put("city", "Antwerpen");
        Map customer = new HashMap();
        customer.put("address", address);
        Map target = new HashMap();
        target.put("customer", customer);
        assertEquals("Antwerpen", f.getResult(target));
    }

    public void test4() {
        BeanPropertyFunction f = new BeanPropertyFunction("test.customer.name");
        Map test = new HashMap();
        test.put("customer", new Customer("Steven"));
        Map target = new HashMap();
        target.put("test", test);
        assertEquals("Steven", f.getResult(target));
    }

    public void test5() {
        LengthOfFunction f = new LengthOfFunction(
            new Function[]{
                new BeanPropertyFunction("test.customer.name")
            },
            0, 2
        );
        Map test = new HashMap();
        test.put("customer", new Customer("Uri"));
        Map target = new HashMap();
        target.put("test", test);
        assertEquals(new Integer(3), f.getResult(target));
    }

    public void test6() {
        BeanPropertyFunction f = new BeanPropertyFunction("this");
        Customer customer = new Customer("Uri");
        Object result = f.getResult(customer);
        assertEquals(customer, result);
    }

    public void test7() {
        BeanPropertyFunction f = new BeanPropertyFunction("this.name");
        Customer customer = new Customer("Uri");
        Object result = f.getResult(customer);
        assertEquals("Uri", result);
    }

    public void test8() {
        BeanPropertyFunction f = new BeanPropertyFunction("this.test.customer.name");
        Map test = new HashMap();
        test.put("customer", new Customer("Steven"));
        Map target = new HashMap();
        target.put("test", test);
        assertEquals("Steven", f.getResult(target));
    }
}
