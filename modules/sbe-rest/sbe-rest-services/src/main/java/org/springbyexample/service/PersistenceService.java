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
package org.springbyexample.service;

import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.response.EntityFindResponseResult;
import org.springbyexample.schema.beans.response.EntityResponseResult;


/**
 * Persistence service interface.
 *
 * @author David Winterfeldt
 *
 * @param   <V>      Domain transfer object (DTO).
 * @param   <R>      Generic response.
 * @param   <FR>     Find response.
 */
public interface PersistenceService<V extends PkEntityBase,
                                    R extends EntityResponseResult, FR extends EntityFindResponseResult>
        extends PersistenceFindService<R, FR> {

    /**
     * Creates a record.
     */
    public R create(V request);

    /**
     * Updates a record.
     */
    public R update(V request);

    /**
     * Deletes person.
     */
    public R delete(V request);

}
