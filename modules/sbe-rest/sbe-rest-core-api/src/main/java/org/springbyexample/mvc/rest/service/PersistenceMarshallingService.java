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

import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.response.EntityFindResponseResult;
import org.springbyexample.schema.beans.response.EntityResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Persistence find marshalling service.
 * 
 * @author David Winterfeldt
 * 
 *  @param   <R>      Generic response.
 *  @param   <FR>     Find response.
 *  @param   <S>      Save request.
 */
public interface PersistenceMarshallingService<R extends EntityResponseResult, FR extends EntityFindResponseResult, S extends PkEntityBase> 
        extends PersistenceFindMarshallingService<R, FR> {

    public final static String DELETE_URI = ROOT_URI + "remove";
    
    /**
     * Save record.
     */
    @RequestMapping(value = ROOT_URI, method = RequestMethod.POST)
    public R create(@RequestBody S request);
    
    /**
     * Update record.
     */
    @RequestMapping(value = ROOT_URI, method = RequestMethod.PUT)
    public R update(@RequestBody S request);

    /**
     * Delete record.
     */
    // FIXME: server has marshalling error if DELETE
    @RequestMapping(value = DELETE_URI, method = RequestMethod.PUT)
    public R delete(@RequestBody S request);
    
}
