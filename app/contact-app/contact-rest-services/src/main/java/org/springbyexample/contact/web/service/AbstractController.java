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
package org.springbyexample.contact.web.service;

import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.response.EntityFindResponseResult;
import org.springbyexample.schema.beans.response.EntityResponseResult;
import org.springbyexample.service.PersistenceService;
import org.springbyexample.service.util.DBUtil;


/**
 * Abstract controller.
 *
 * @author David Winterfeldt
 *
 * @param   <V>      Domain transfer object (DTO).
 * @param   <R>      Generic response.
 * @param   <FR>     Find response.
 */
public abstract class AbstractController<V extends PkEntityBase,
                                         R extends EntityResponseResult, FR extends EntityFindResponseResult> {

    protected final PersistenceService<V, R, FR> service;

    public AbstractController(PersistenceService<V, R, FR> service) {
        this.service = service;
    }

    /**
     * Whether or not the primary key is valid (greater than zero).
     */
    protected boolean isPrimaryKeyValid(V request) {
        return DBUtil.isPrimaryKeyValid(request);
    }

    /**
     * Calls create or update based on whether the primary key is valid.
     */
    protected R save(V request) {
        if (!isPrimaryKeyValid(request)) {
            return service.create(request);
        } else {
            return service.update(request);
        }
    }

}
