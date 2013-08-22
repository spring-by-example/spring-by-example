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
package org.springbyexample.contact.web.service.person;

import static org.springbyexample.contact.web.service.person.ContactMarshallingService.PATH;

import org.springbyexample.contact.converter.person.SmallContactConverter;
import org.springbyexample.contact.service.person.ContactService;
import org.springbyexample.mvc.bind.annotation.RestRequestResource;
import org.springbyexample.mvc.bind.annotation.RestResource;
import org.springbyexample.mvc.rest.service.PersistenceMarshallingService;
import org.springbyexample.schema.beans.person.Person;
import org.springbyexample.schema.beans.person.PersonFindResponse;
import org.springbyexample.schema.beans.person.PersonResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contact marshalling service.
 *
 * @author David Winterfeldt
 */
@RestResource(service=ContactService.class, path=PATH, responseClass=PersonResponse.class)
public interface ContactMarshallingService extends PersistenceMarshallingService<PersonResponse, PersonFindResponse, Person> {

    final static String PATH = "/person-test";
    final static String SMALL_URI = "/small";
    final static String SMALL_PATH = "/small-person";

    public final static String SMALL_FIND_BY_ID_REQUEST = SMALL_PATH + PATH_DELIM + "{" + ID_VAR + "}";

    public final static String SMALL_FIND_PAGINATED_REQUEST = SMALL_PATH + PAGINATED;

    public final static String LAST_NAME_VAR = "lastName";
    public final static String LAST_NAME_PARAMS = PARAM_DELIM + LAST_NAME_VAR + "={" + LAST_NAME_VAR + "}";
    public final static String FIND_BY_LAST_NAME_CLIENT_REQUEST = PATH + LAST_NAME_PARAMS;
    public final static String SMALL_FIND_BY_LAST_NAME_CLIENT_REQUEST = PATH + SMALL_URI + LAST_NAME_PARAMS;

    @RequestMapping(value = SMALL_FIND_BY_ID_REQUEST, method = RequestMethod.GET)
    @RestRequestResource(relative=false, methodName="findById", converter=SmallContactConverter.class)
    public PersonResponse smallFindById(@PathVariable(ID_VAR) Integer id);

    @RequestMapping(value=SMALL_FIND_PAGINATED_REQUEST, method = RequestMethod.GET)
    @RestRequestResource(relative=false, methodName="find", converter=SmallContactConverter.class)
    public PersonFindResponse smallFind(@PathVariable(PAGE_VAR) int page, @PathVariable(PAGE_SIZE_VAR) int pageSize);

    @RequestMapping(value = PATH_DELIM, method = RequestMethod.GET, params= { LAST_NAME_VAR })
    public PersonFindResponse findByLastName(@RequestParam(LAST_NAME_VAR) String lastName);

    @RequestMapping(value = SMALL_URI, method = RequestMethod.GET, params= { LAST_NAME_VAR })
    @RestRequestResource(methodName="findByLastName", converter=SmallContactConverter.class)
    public PersonFindResponse smallFindByLastName(@RequestParam(LAST_NAME_VAR) String lastName);

}
