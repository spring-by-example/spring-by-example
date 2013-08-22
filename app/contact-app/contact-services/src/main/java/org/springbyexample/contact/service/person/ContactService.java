/*
 * Copyright 2007-2013 the original author or authors.
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
package org.springbyexample.contact.service.person;

import static org.springbyexample.contact.security.Role.ADMIN;
import static org.springbyexample.contact.security.Role.USER;

import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springbyexample.service.PersistenceService;
import org.springframework.security.access.annotation.Secured;


/**
 * Contact service interface.
 *
 * @author David Winterfeldt
 */
public interface ContactService extends PersistenceService<Person, PersonResponse, PersonFindResponse> {

    @Override
    @Secured ({ USER })
    public PersonResponse findById(Integer id);

    /**
     * Find by last name.
     */
    @Secured ({ USER })
    public PersonFindResponse findByLastName(String lastName);

    @Override
    @Secured ({ USER })
    public PersonFindResponse find();

    @Override
    @Secured ({ USER })
    public PersonFindResponse find(int page, int pageSize);

    @Override
    @Secured ({ USER })
    public PersonResponse create(Person person);

    @Override
    @Secured ({ USER })
    public PersonResponse update(Person person);

    @Override
    @Secured ({ ADMIN })
    public PersonResponse delete(Person person);

}
