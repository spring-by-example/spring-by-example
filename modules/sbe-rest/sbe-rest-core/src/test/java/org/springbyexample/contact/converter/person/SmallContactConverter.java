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
package org.springbyexample.contact.converter.person;

import org.springbyexample.converter.AbstractListConverter;
import org.springbyexample.schema.beans.person.Person;
import org.springframework.stereotype.Component;


/**
 * Small contact converter.
 * 
 * @author David Winterfeldt
 */
@Component
public class SmallContactConverter extends AbstractListConverter<Person, Person> {

    @Override
    public Person convertTo(Person source) {
        Person result = new Person()
                .withId(source.getId())
                .withFirstName(source.getFirstName())
                .withLastName(source.getLastName())
                .withLockVersion(source.getLockVersion());
//                .withCreateUser(source.getCreateUser())
//                .withCreated(source.getCreated())
//                .withLastUpdateUser(source.getLastUpdateUser())
//                .withLastUpdated(source.getLastUpdated());
        
        return result;
    }

    @Override
    public Person convertFrom(Person source) {
        throw new UnsupportedOperationException("Convert from not supported.");
    }
    
}
