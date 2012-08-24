/*
 * Copyright 2007-2012 the original author or authors.
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
package org.springbyexample.web.service;

import java.util.Collection;

import org.springbyexample.web.orm.entity.Address;
import org.springbyexample.web.orm.entity.Person;
import org.springframework.security.access.annotation.Secured;


/**
 * Person service interface.
 * 
 * @author David Winterfeldt
 */
public interface PersonService {

    /**
     * Find person by id.
     */
    public Person findById(Integer id);

    /**
     * Find persons.
     */
    public Collection<Person> find();

    /**
     * Saves person.
     */
    public Person save(Person person);

    /**
     * Deletes person.
     */
    @Secured ({"ROLE_ADMIN"})
    public void delete(Integer id);
    
    /**
     * Saves address to a person.
     */
    public Person saveAddress(Integer id, Address address);

    /**
     * Delete address from a person.
     */
    public Person deleteAddress(Integer id, Integer addressId);

}
