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

package org.springmodules.validation.valang.parser;

import java.util.*;

public class Person {

    int age = 0;
    String firstName = null;
    String size = null;
    Date dateOfBirth = null;
    String email = null;
    int minAge = 18;

    public Person(int age, String firstName) {
        setAge(age);
        setFirstName(firstName);
    }

    public Person(String size) {
        setSize(size);
    }

    public Person(Date dateOfBirth) {
        setDateOfBirth(dateOfBirth);
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getMinAge() {
        return this.minAge;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object[] getSizes() {
        return new Object[]{"S", "M", "L", "XL"};
    }

    public Collection getTags() {
        Collection tags = new ArrayList();
        tags.add("tag1");
        tags.add("tag2");
        return tags;
    }

    public Map getMap() {
        Map map = new HashMap();
        map.put("firstName", "Steven");
        map.put("sizes", getSizes());
        map.put("Test Key", "Value");
        return map;
    }

}