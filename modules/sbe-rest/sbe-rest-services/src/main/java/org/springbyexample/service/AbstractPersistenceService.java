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

import org.springbyexample.converter.ListConverter;
import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.response.EntityFindResponseResult;
import org.springbyexample.schema.beans.response.EntityResponseResult;
import org.springbyexample.service.util.MessageHelper;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * Person service implementation.
 *
 * @author David Winterfeldt
 *
 * @param   <T>      Domain model bean.
 * @param   <V>      Domain transfer object (DTO).
 * @param   <R>      Generic response.
 * @param   <FR>     Find response.
 */
public abstract class AbstractPersistenceService<T extends AbstractPersistable<Integer>, V extends PkEntityBase,
                                                 R extends EntityResponseResult, FR extends EntityFindResponseResult>
        extends AbstractPersistenceFindService<T, V, R, FR>
        implements PersistenceService<V, R, FR> {

    protected static final String DELETE_MSG = "delete.msg";

    public AbstractPersistenceService(JpaRepository<T, Integer> repository, ListConverter<T, V> converter,
                                      MessageHelper messageHelper) {
        super(repository, converter, messageHelper);
    }

    @Override
    @Transactional
    public R create(V request) {
        Assert.isTrue(!isPrimaryKeyValid(request), "Create should not have a valid primary key.");

        return doSave(request);
    }

    @Override
    @Transactional
    public final R update(V request) {
        Assert.isTrue(isPrimaryKeyValid(request), "Update should have a valid primary key.");

        return doSave(request);
    }

    @Override
    @Transactional
    public R delete(V request) {
        return doDelete(request);
    }

    /**
     * Processes save.  Can be overridden for custom save logic.
     */
    protected R doSave(V request) {
        V result = null;

        T convertedRequest = converter.convertFrom(request);

        // issues with lock version updating if flush isn't called
        T bean = repository.saveAndFlush(convertedRequest);

        result = converter.convertTo(bean);

        return createSaveResponse(result);
    }

    /**
     * Processes delete. Can be overridden for custom save logic.
     */
    protected R doDelete(V request) {
        repository.delete(request.getId());
        repository.flush();

        return createDeleteResponse();
    }

    /**
     * Create a save response.
     */
    protected abstract R createSaveResponse(V result);

    /**
     * Create a delete response.
     */
    protected abstract R createDeleteResponse();

}
