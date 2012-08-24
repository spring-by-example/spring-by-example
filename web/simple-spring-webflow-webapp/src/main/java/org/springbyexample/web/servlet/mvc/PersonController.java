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

package org.springbyexample.web.servlet.mvc;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.web.orm.entity.Person;
import org.springbyexample.web.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Person form controller.
 * 
 * @author David Winterfeldt
 */
@Controller
public class PersonController {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    static final String SEARCH_VIEW_PATH_KEY = "/person/search";
    
    private static final String DELETE_PATH_KEY = "/person/delete";
    
    private static final String SEARCH_VIEW_KEY = "redirect:search.html";
    private static final String SEARCH_MODEL_KEY = "persons";

    private final PersonService service;

    @Autowired
    public PersonController(PersonService service) {
        this.service = service;
    }

    /**
     * <p>Deletes a person.</p>
     * 
     * <p>Expected HTTP POST and request '/person/delete'.</p>
     */
    @RequestMapping(value=DELETE_PATH_KEY, method=RequestMethod.POST)
    public String delete(@RequestParam("id") Integer id) {
        logger.info("'{}'  id={}", DELETE_PATH_KEY, id);
        
        service.delete(id);

        return SEARCH_VIEW_KEY;
    }

    /**
     * <p>Searches for all persons and returns them in a 
     * <code>Collection</code>.</p>
     * 
     * <p>Expected HTTP GET and request '/person/search'.</p>
     */
    @RequestMapping(value=SEARCH_VIEW_PATH_KEY, method=RequestMethod.GET)
    public @ModelAttribute(SEARCH_MODEL_KEY) Collection<Person> search() {
        return service.find();
    }

}
