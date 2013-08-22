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

import java.util.List;

import org.springbyexample.converter.ListConverter;
import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.response.EntityFindResponseResult;
import org.springbyexample.schema.beans.response.EntityResponseResult;
import org.springbyexample.service.util.DBUtil;
import org.springbyexample.service.util.MessageHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Abstract persistence find service.
 *
 * @author David Winterfeldt
 *
 * @param   <T>      Domain model bean.
 * @param   <V>      Domain transfer object (DTO).
 * @param   <R>      Generic response.
 * @param   <FR>     Find response.
 */
@Transactional(readOnly=true)
public abstract class AbstractPersistenceFindService<T extends AbstractPersistable<Integer>, V extends PkEntityBase,
                                                     R extends EntityResponseResult, FR extends EntityFindResponseResult>
        extends AbstractService implements PersistenceFindService<R, FR> {

    protected final JpaRepository<T, Integer> repository;
    protected final ListConverter<T, V> converter;

    public AbstractPersistenceFindService(JpaRepository<T, Integer> repository, ListConverter<T, V> converter,
                                          MessageHelper messageHelper) {
        super(messageHelper);

        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public R findById(Integer id) {
        T bean = repository.findOne(id);
        V result = (bean != null ? converter.convertTo(bean) : null);

        return createResponse(result);
    }

    @Override
    public FR find() {
        List<V> results = converter.convertListTo(repository.findAll(createDefaultSort()));

        return createFindResponse(results);
    }

    @Override
    public FR find(int page, int pageSize) {
        Page<T> pageResults = repository.findAll(new PageRequest(page, pageSize, createDefaultSort()));

        List<V> results = converter.convertListTo(pageResults.getContent());

        return createFindResponse(results, pageResults.getTotalElements());
    }

    /**
     * Create a response.
     */
    protected abstract R createResponse(V result);

    /**
     * Create a find response with the count representing the size of the list.
     */
    protected abstract FR createFindResponse(List<V> results);

    /**
     * Create a find response with the results representing the page request
     * and the count representing the size of the query.
     */
    protected abstract FR createFindResponse(List<V> results, long count);

    /**
     * Whether or not the primary key is valid (greater than zero).
     */
    protected boolean isPrimaryKeyValid(V request) {
        return DBUtil.isPrimaryKeyValid(request);
    }

    /**
     * Creates default sort.
     */
    private Sort createDefaultSort() {
        return new Sort("lastName", "firstName");
    }

}
