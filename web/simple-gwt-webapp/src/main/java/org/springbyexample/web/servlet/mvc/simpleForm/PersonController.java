/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springbyexample.web.servlet.mvc.simpleForm;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.orm.hibernate3.annotation.bean.Address;
import org.springbyexample.orm.hibernate3.annotation.bean.Person;
import org.springbyexample.orm.hibernate3.annotation.dao.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


/**
 * Person form controller.
 * 
 * @author David Winterfeldt
 */
@Controller
public class PersonController {

    final Logger logger = LoggerFactory.getLogger(PersonController.class);
    
    static final String SEARCH_VIEW_KEY = "/person/search";
    static final String SEARCH_MODEL_KEY = "persons";

    @Autowired
    protected PersonDao personDao = null;

    /**
     * Deletes a person.
     */
    @RequestMapping(value="/person/delete.htm")
    public ModelAndView delete(@RequestParam("id") Integer id) {
        Person person = new Person();
        person.setId(id);
        
        personDao.delete(person);

        return new ModelAndView(SEARCH_VIEW_KEY, SEARCH_MODEL_KEY, search());
    }

    /**
     * Searches for all persons and returns them in a 
     * <code>Collection</code> as 'persons' in the 
     * <code>ModelMap</code>.
     */
    @RequestMapping(value="/person/search.htm")
    @ModelAttribute(SEARCH_MODEL_KEY)
    public Collection<Person> search() {
        Collection<Person> lResults = personDao.findPersons();
        
        return lResults;
    }

}
