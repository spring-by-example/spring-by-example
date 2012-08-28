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
package org.springbyexample.web.service;

import org.springbyexample.schema.beans.response.FindResponseResult;
import org.springbyexample.schema.beans.response.ResponseResult;


/**
 * Persistence find marshalling service.
 * 
 * @author David Winterfeldt
 * 
 *  @param   <R>      Generic response.
 *  @param   <FR>     Find response.
 */
public interface PersistenceFindMarshallingService<R extends ResponseResult, FR extends FindResponseResult> {

    public final static String PATH_DELIM = "/";
    
    public final static String ID_VAR = "id";
    public final static String PAGE_VAR = "page";
    public final static String PAGE_SIZE_VAR = "page-size";

    public final static String PAGE_PATH = PATH_DELIM + PAGE_VAR;
    public final static String PAGE_SIZE_PATH = PATH_DELIM + PAGE_SIZE_VAR;
    public final static String PAGINATED = PAGE_PATH + PATH_DELIM + "{" + PAGE_VAR + "}" + PAGE_SIZE_PATH + PATH_DELIM + "{" + PAGE_SIZE_VAR + "}";
    
    
    /**
     * Find by primary key.
     */
    public R findById(long id);

    /**
     * Find a paginated record set.
     */
    public FR find(int page, int pageSize);

    /**
     * Find all records.
     */
    public FR find();

}
