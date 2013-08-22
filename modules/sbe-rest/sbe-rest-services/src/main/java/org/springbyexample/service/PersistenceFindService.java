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

import org.springbyexample.schema.beans.response.EntityFindResponseResult;
import org.springbyexample.schema.beans.response.EntityResponseResult;


/**
 * Persistence find service interface.
 *
 * @author David Winterfeldt
 *
 * @param   <R>      Generic response.
 * @param   <FR>     Find response.
 */
public interface PersistenceFindService<R extends EntityResponseResult, FR extends EntityFindResponseResult> {

    /**
     * Find a record with an id.
     */
    public R findById(Integer id);

    /**
     * Find all records.
     */
    public FR find();

    /**
     * Find a paginated record set.
     */
    public FR find(int page, int pageSize);

}
