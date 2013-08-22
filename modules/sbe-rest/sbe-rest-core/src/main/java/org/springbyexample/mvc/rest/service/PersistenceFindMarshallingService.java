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
package org.springbyexample.mvc.rest.service;

import org.springbyexample.schema.beans.response.EntityFindResponseResult;
import org.springbyexample.schema.beans.response.EntityResponseResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Persistence find marshalling service.
 * 
 * @author David Winterfeldt
 * 
 *  @param   <R>      Generic response.
 *  @param   <FR>     Find response.
 */
public interface PersistenceFindMarshallingService<R extends EntityResponseResult, FR extends EntityFindResponseResult> {

    public final static String PATH_DELIM = "/";
    public final static String PARAM_DELIM = "?";
    public final static String PARAM_VALUE_DELIM = "&";
    
    public final static String ID_VAR = "id";
    public final static String PAGE_VAR = "page";
    public final static String PAGE_SIZE_VAR = "page-size";

    public final static String PAGE_PATH = PATH_DELIM + PAGE_VAR;
    public final static String PAGE_SIZE_PATH = PATH_DELIM + PAGE_SIZE_VAR;
    public final static String PAGINATED = PAGE_PATH + PATH_DELIM + "{" + PAGE_VAR + "}" + PAGE_SIZE_PATH + PATH_DELIM + "{" + PAGE_SIZE_VAR + "}";
    
    public final static String ROOT_URI = PATH_DELIM;
    public final static String FIND_BY_ID_URI = PATH_DELIM + "{" + ID_VAR + "}";
    
    /**
     * Find by primary key.
     */
    @RequestMapping(value = FIND_BY_ID_URI, method = RequestMethod.GET)
    public R findById(@PathVariable(ID_VAR) Integer id);

    /**
     * Find a paginated record set.
     */
    @RequestMapping(value = PAGINATED, method = RequestMethod.GET)
    public FR find(@PathVariable(PAGE_VAR) int page, @PathVariable(PAGE_SIZE_VAR) int pageSize);

    /**
     * Find all records.
     */
    @RequestMapping(value = ROOT_URI, method = RequestMethod.GET)
    public FR find();

}
