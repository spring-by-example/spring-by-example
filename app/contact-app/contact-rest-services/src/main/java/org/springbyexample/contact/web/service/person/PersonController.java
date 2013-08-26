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
import org.springbyexample.contact.service.person.ContactService;
import org.springbyexample.contact.web.service.AbstractController;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PersonController extends AbstractController<Person, PersonResponse, PersonFindResponse>
        implements PersonMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public PersonController(ContactService service) {
        super(service);
    }

    @Override
    @RequestMapping(value = FIND_BY_ID_REQUEST, method = RequestMethod.GET)
    public PersonResponse findById(@PathVariable(ID_VAR) Integer id) {
        logger.info("Find person.  id={}", id);

        return service.findById((int)id);
    }

    @Override
    @RequestMapping(value = FIND_PAGINATED_REQUEST, method = RequestMethod.GET)
    public PersonFindResponse find(@PathVariable(PAGE_VAR) int page,
                                   @PathVariable(PAGE_SIZE_VAR) int pageSize) {
        logger.info("Find person page.  page={}  pageSize={}", page, pageSize);

        return service.find(page, pageSize);
    }

    @Override
    @RequestMapping(value = FIND_REQUEST, method = RequestMethod.GET)
    public PersonFindResponse find() {
        logger.info("Find all persons.");

        return service.find();
    }

    @Override
    @RequestMapping(value = SAVE_REQUEST, method = RequestMethod.POST)
    public PersonResponse create(@RequestBody Person request) {
        Assert.isTrue(!isPrimaryKeyValid(request), "Create should not have a valid primary key.");

        logger.info("Save person.  id={}", request.getId());

        return service.create(request);
    }

    @Override
    @RequestMapping(value = UPDATE_REQUEST, method = RequestMethod.PUT)
    public PersonResponse update(@RequestBody Person request) {
        Assert.isTrue(isPrimaryKeyValid(request), "Update should have a valid primary key.");

        logger.info("Update person.  id={}", request.getId());

        return service.update(request);
    }

    @Override
    @RequestMapping(value = DELETE_PK_REQUEST, method = RequestMethod.DELETE)
    public PersonResponse delete(@PathVariable(ID_VAR) Integer id) {
        logger.info("Delete person.  id={}", id);

        return service.delete(new Person().withId(id));
    }

    @Override
    @RequestMapping(value = DELETE_REQUEST, method = RequestMethod.DELETE)
    public PersonResponse delete(@RequestBody Person request) {
        Assert.isTrue((request.getId() > 0), "Delete should have a valid primary key");

        int id = request.getId();

        return delete(id);
    }

}
