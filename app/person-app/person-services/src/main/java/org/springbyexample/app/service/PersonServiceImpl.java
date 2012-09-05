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
package org.springbyexample.app.service;

import java.util.List;

import org.springbyexample.app.converter.person.PersonConverter;
import org.springbyexample.orm.repository.PersonRepository;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springbyexample.schema.beans.response.Message;
import org.springbyexample.schema.beans.response.MessageType;
import org.springbyexample.schema.beans.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Person service implementation.
 * 
 * @author David Winterfeldt
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;
    private final PersonConverter converter;
    
    @Autowired
    public PersonServiceImpl(PersonRepository repository, PersonConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public PersonResponse findById(Integer id) {
        Person result = converter.convertTo(repository.findOne(id));
        
        return new PersonResponse().withResult(result);
    }

    @Override
    public PersonFindResponse find() {
        List<Person> results = converter.convertListTo(repository.findAll());
        
        return new PersonFindResponse().withCount(results.size())
                                       .withResults(results);
    }

    @Override
    public PersonResponse save(Person request) {
        org.springbyexample.orm.entity.person.Person bean = converter.convertFrom(request);
        
        Person result = converter.convertTo(repository.saveAndFlush(bean)); 
        
        return new PersonResponse().withResult(result);
    }

    @Override
    public ResponseResult delete(Integer id) {
        repository.delete(id);
        
        return new ResponseResult().withMessageList(
                    new Message().withMessageType(MessageType.INFO)
                                 .withMessage("Successfully deleted record."));
    }
    
}
