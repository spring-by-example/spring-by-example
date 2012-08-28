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
package org.springbyexample.web.service.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springbyexample.schema.beans.response.Message;
import org.springbyexample.schema.beans.response.MessageType;
import org.springbyexample.schema.beans.response.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Person form controller.
 * 
 * @author David Winterfeldt
 */
@Controller
public class PersonController implements PersonMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    final static Integer ID = 1;
    final static String FIRST_NAME = "Joe";
    final static String LAST_NAME = "Smith";

    final static Integer SECOND_ID = 2;
    final static String SECOND_FIRST_NAME = "John";
    final static String SECOND_LAST_NAME = "Jackson";

    @Override
    @RequestMapping(value = FIND_BY_ID_REQUEST, method = RequestMethod.GET)
    public PersonResponse findById(@PathVariable(ID_VAR) long id) {
        logger.info("Find person.  id={}", id);

        return new PersonResponse()
                    .withResult(new Person().withId(ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME));
    }

    @Override
    @RequestMapping(value = FIND_PAGINATED_REQUEST, method = RequestMethod.GET)
    public PersonFindResponse find(@PathVariable(PAGE_VAR) int page,
                                   @PathVariable(PAGE_SIZE_VAR) int pageSize) {
        logger.info("Find person page.  page={}  pageSize={}", page, pageSize);

        return new PersonFindResponse()
                    .withCount(2)
                    .withResults(new Person().withId(ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME),
                                 new Person().withId(SECOND_ID).withFirstName(SECOND_FIRST_NAME).withLastName(SECOND_LAST_NAME));
    }

    @Override
    @RequestMapping(value = FIND_REQUEST, method = RequestMethod.GET)
    public PersonFindResponse find() {
        logger.info("Find all persons.");

        return new PersonFindResponse()
                    .withCount(2)
                    .withResults(new Person().withId(ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME),
                                 new Person().withId(SECOND_ID).withFirstName(SECOND_FIRST_NAME).withLastName(SECOND_LAST_NAME));
    }

    @Override
    @RequestMapping(value = SAVE_REQUEST, method = RequestMethod.POST)
    public PersonResponse save(@RequestBody Person request) {
        logger.info("Save person.  id={}", request.getId());

        return new PersonResponse().withMessageList(
                    new Message().withMessageType(MessageType.INFO)
                                 .withMessage(String.format("Successfully saved record.  id='%d'", request.getId())))
                    .withResult(request);
    }

    @Override
    @RequestMapping(value = DELETE_REQUEST, method = RequestMethod.DELETE)
    public ResponseResult delete(@PathVariable(ID_VAR) long id) {
        logger.info("Delete person.  id={}", id);

        return new ResponseResult().withMessageList(
                    new Message().withMessageType(MessageType.INFO)
                                 .withMessage(String.format("Successfully deleted record.  id='%d'", id)));
    }

}
