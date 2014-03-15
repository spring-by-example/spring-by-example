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

import java.util.List;

import org.springbyexample.contact.converter.person.ContactConverter;
import org.springbyexample.contact.converter.person.PersonStudentConverter;
import org.springbyexample.contact.converter.person.ProfessionalConverter;
import org.springbyexample.contact.converter.person.StudentConverter;
import org.springbyexample.contact.orm.repository.PersonRepository;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springbyexample.schema.beans.person.Professional;
import org.springbyexample.schema.beans.person.Student;
import org.springbyexample.schema.beans.response.Message;
import org.springbyexample.schema.beans.response.MessageType;
import org.springbyexample.service.AbstractPersistenceService;
import org.springbyexample.service.util.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Person service implementation.
 *
 * @author David Winterfeldt
 */
@Service
public class ContactServiceImpl extends AbstractPersistenceService<org.springbyexample.contact.orm.entity.person.Person, Person,
                                                                   PersonResponse, PersonFindResponse>
        implements ContactService {

    private static final String SAVE_MSG = "contact.save.msg";

    private final PersonStudentConverter personStudentConverter;
    private final StudentConverter studentConverter;
    private final ProfessionalConverter professionalConverter;

    @Autowired
    public ContactServiceImpl(PersonRepository repository,
                              ContactConverter converter, PersonStudentConverter personStudentConverter,
                              StudentConverter studentConverter, ProfessionalConverter professionalConverter,
                              MessageHelper messageHelper) {
        super(repository, converter, messageHelper);

        this.personStudentConverter = personStudentConverter;
        this.studentConverter = studentConverter;
        this.professionalConverter = professionalConverter;
    }

    @Override
    protected PersonResponse doSave(Person request) {
        org.springbyexample.contact.orm.entity.person.Person bean = null;

        if (request instanceof Student) {
            bean = studentConverter.convertFrom((Student) request);
        } else if (request instanceof Professional) {
            bean = professionalConverter.convertFrom((Professional) request);
        } else {
            bean = studentConverter.convertFrom(personStudentConverter.convertTo(request));
        }

        Person result = converter.convertTo(repository.saveAndFlush(bean));

        return createSaveResponse(result);
    }

    @Override
    public PersonFindResponse findByLastName(String lastName) {
        List<Person> results = converter.convertListTo(((PersonRepository)repository).findByLastName(lastName));

        return createFindResponse(results);
    }

    @Override
    protected PersonResponse createSaveResponse(Person result) {
        return new PersonResponse().withMessageList(new Message().withMessageType(MessageType.INFO)
                    .withMessage(getMessage(SAVE_MSG, new Object[] { result.getFirstName(), result.getLastName()})))
                    .withResults(result);
    }

    @Override
    protected PersonResponse createDeleteResponse() {
        return new PersonResponse().withMessageList(new Message().withMessageType(MessageType.INFO)
                .withMessageKey(DELETE_MSG).withMessage(getMessage(DELETE_MSG)));
    }

    @Override
    protected PersonResponse createResponse(Person result) {
        return new PersonResponse().withResults(result);
    }

    @Override
    protected PersonFindResponse createFindResponse(List<Person> results) {
        return new PersonFindResponse().withResults(results).withCount(results.size());
    }

    @Override
    protected PersonFindResponse createFindResponse(List<Person> results, long count) {
        return new PersonFindResponse().withResults(results).withCount(count);
    }

}
