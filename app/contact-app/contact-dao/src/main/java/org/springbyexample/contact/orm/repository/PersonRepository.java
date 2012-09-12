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
package org.springbyexample.contact.orm.repository;

import java.util.List;

import org.springbyexample.contact.orm.entity.person.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * Person repository.
 * 
 * @author David Winterfeldt
 */
public interface PersonRepository extends JpaRepository<Person, Integer> {
    
    public final static String FIND_BY_ADDRESS_QUERY = "SELECT p " + 
                                                       "FROM Person p LEFT JOIN p.addresses a " +
                                                       "WHERE a.address = :address";

    /**
     * Find person like first name.
     */
    public List<Person> findByFirstNameLike(String firstName);

    /**
     * Find person by last name.
     */
    public List<Person> findByLastName(String lastName);

    /**
     * Find person by address.
     */
    @Query(FIND_BY_ADDRESS_QUERY)
    public List<Person> findByAddress(@Param("address") String address);

    /**
     * Find person by address.
     */
    @Query(FIND_BY_ADDRESS_QUERY)
    public Page<Person> findByAddress(@Param("address") String address, Pageable page);

    /**
     * Find person by first and last name.
     */
    public List<Person> findByName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    
}
