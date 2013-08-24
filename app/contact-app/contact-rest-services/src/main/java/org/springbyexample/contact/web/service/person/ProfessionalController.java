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
package org.springbyexample.contact.web.service.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.person.Professional;
import org.springbyexample.schema.beans.person.ProfessionalFindResponse;
import org.springbyexample.schema.beans.person.ProfessionalResponse;
import org.springbyexample.schema.beans.response.Message;
import org.springbyexample.schema.beans.response.MessageType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
public class ProfessionalController implements ProfessionalMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    final static Integer ID = 1;
    final static String FIRST_NAME = "Joe";
    final static String LAST_NAME = "Smith";
    final static String COMPANY_NAME = "Spring Travel";

    final static Integer SECOND_ID = 2;
    final static String SECOND_FIRST_NAME = "John";
    final static String SECOND_LAST_NAME = "Jackson";
    final static String SECOND_COMPANY_NAME = "Spring Coffee";

    @Override
    @RequestMapping(value = FIND_BY_ID_REQUEST, method = RequestMethod.GET)
    public ProfessionalResponse findById(@PathVariable(ID_VAR) Integer id) {
        logger.info("Find professional.  id={}", id);

        return new ProfessionalResponse()
                    .withResults(new Professional().withId(ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME)
                                                  .withCompanyName(COMPANY_NAME));
    }

    @Override
    @RequestMapping(value = FIND_PAGINATED_REQUEST, method = RequestMethod.GET)
    public ProfessionalFindResponse find(@PathVariable(PAGE_VAR) int page,
                                   @PathVariable(PAGE_SIZE_VAR) int pageSize) {
        logger.info("Find Professional page.  page={}  pageSize={}", page, pageSize);

        return new ProfessionalFindResponse()
                    .withCount(2)
                    .withResults(new Professional().withId(ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME)
                                                   .withCompanyName(COMPANY_NAME),
                                 new Professional().withId(SECOND_ID).withFirstName(SECOND_FIRST_NAME).withLastName(SECOND_LAST_NAME)
                                                   .withCompanyName(SECOND_COMPANY_NAME));
    }

    @Override
    @RequestMapping(value = FIND_REQUEST, method = RequestMethod.GET)
    public ProfessionalFindResponse find() {
        logger.info("Find all Professionals.");

        return new ProfessionalFindResponse()
                    .withCount(2)
                    .withResults(new Professional().withId(ID).withFirstName(FIRST_NAME).withLastName(LAST_NAME)
                                                   .withCompanyName(COMPANY_NAME),
                                 new Professional().withId(SECOND_ID).withFirstName(SECOND_FIRST_NAME).withLastName(SECOND_LAST_NAME)
                                                   .withCompanyName(SECOND_COMPANY_NAME));
    }

    @Override
    @RequestMapping(value = SAVE_REQUEST, method = RequestMethod.POST)
    public ProfessionalResponse create(@RequestBody Professional request) {
        logger.info("Save Professional.  id={}", request.getId());

        return new ProfessionalResponse().withMessageList(
                    new Message().withMessageType(MessageType.INFO)
                                 .withMessage(String.format("Successfully saved record.  id='%d'", request.getId())))
                    .withResults(request);
    }

    @Override
    @RequestMapping(value = UPDATE_REQUEST, method = RequestMethod.PUT)
    public ProfessionalResponse update(@RequestBody Professional request) {
        Assert.isTrue((request.getId() >  0), "Update should have a valid primary key");

        logger.info("Update person.  id={}", request.getId());

        return new ProfessionalResponse().withMessageList(
                new Message().withMessageType(MessageType.INFO)
                             .withMessage(String.format("Successfully saved record.  id='%d'", request.getId())))
                .withResults(request);
    }

    @Override
    @RequestMapping(value = DELETE_PK_REQUEST, method = RequestMethod.DELETE)
    public ProfessionalResponse delete(@PathVariable(ID_VAR) Integer id) {
        logger.info("Delete Professional.  id={}", id);

        return new ProfessionalResponse().withMessageList(
                    new Message().withMessageType(MessageType.INFO)
                                 .withMessage(String.format("Successfully deleted record.  id='%d'", id)));
    }

    @Override
    @RequestMapping(value = DELETE_REQUEST, method = RequestMethod.DELETE)
    public ProfessionalResponse delete(@RequestBody Professional request) {
        Assert.isTrue((request.getId() > 0), "Delete should have a valid primary key");

        int id = request.getId();

        logger.info("Delete professional.  id={}", id);

        return new ProfessionalResponse().withMessageList(
                new Message().withMessageType(MessageType.INFO)
                             .withMessage(String.format("Successfully deleted record.  id='%d'", id)));
    }

}
