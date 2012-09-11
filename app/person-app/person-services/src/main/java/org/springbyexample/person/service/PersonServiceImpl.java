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
package org.springbyexample.person.service;

import java.util.List;

import org.springbyexample.person.converter.person.PersonConverter;
import org.springbyexample.person.converter.person.ProfessionalConverter;
import org.springbyexample.person.converter.person.StudentConverter;
import org.springbyexample.person.orm.repository.PersonRepository;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springbyexample.schema.beans.person.Professional;
import org.springbyexample.schema.beans.person.Student;
import org.springbyexample.schema.beans.response.Message;
import org.springbyexample.schema.beans.response.MessageType;
import org.springbyexample.schema.beans.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Person service implementation.
 * 
 * @author David Winterfeldt
 */
@Service
@Transactional(readOnly=true)
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;
    private final PersonConverter converter;
    private final StudentConverter studentConverter;
    private final ProfessionalConverter professionalConverter;
    
    @Autowired
    public PersonServiceImpl(PersonRepository repository, 
                             PersonConverter converter, 
                             StudentConverter studentConverter, ProfessionalConverter professionalConverter) {
        this.repository = repository;
        this.converter = converter;
        this.studentConverter = studentConverter;
        this.professionalConverter = professionalConverter;
    }

    @Override
    public PersonResponse findById(Integer id) {
        Person result = converter.convertTo(repository.findOne(id));
        
        return new PersonResponse().withResults(result);
    }

    @Override
    public PersonFindResponse find() {
        List<Person> results = converter.convertListTo(repository.findAll(createDefaultSort()));
        
        return new PersonFindResponse().withCount(results.size())
                                       .withResults(results);
    }

    @Override
    public PersonFindResponse find(int page, int pageSize) {
        Page<org.springbyexample.person.orm.entity.person.Person> pageResults = 
                repository.findAll(new PageRequest(page, pageSize, createDefaultSort()));

        List<Person> results = converter.convertListTo(pageResults.getContent());
        
        return new PersonFindResponse().withCount(pageResults.getTotalElements())
                                       .withResults(results);
    }

    @Override
    @Transactional
    public PersonResponse save(Person request) {
        org.springbyexample.person.orm.entity.person.Person bean = null;
        
        if (request instanceof Student) {
            bean = studentConverter.convertFrom((Student) request);
        } else if (request instanceof Professional) {
                bean = professionalConverter.convertFrom((Professional) request);
        } else {
            bean = converter.convertFrom(request);
        }
        
        Person result = converter.convertTo(repository.saveAndFlush(bean)); 
        
        return new PersonResponse().withMessageList(
                    new Message().withMessageType(MessageType.INFO)
                                 .withMessage(String.format("Successfully saved record.  id='%d'", request.getId())))
                    .withResults(result);
    }

    @Override
    @Transactional
    public ResponseResult delete(Integer id) {
        repository.delete(id);

        return new ResponseResult().withMessageList(
                    new Message().withMessageType(MessageType.INFO)
                                 .withMessage(String.format("Successfully deleted record.  id='%d'", id)));
    }
    
    /**
     * Creates default sort.
     */
    private Sort createDefaultSort() {
        return new Sort("lastName", "firstName");
    }
    
}
