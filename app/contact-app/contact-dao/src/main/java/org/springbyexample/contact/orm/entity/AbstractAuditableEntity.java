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
package org.springbyexample.contact.orm.entity;


import javax.persistence.MappedSuperclass;

import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.AbstractPersistable;


/**
 * Abstract auditable entity.
 * 
 * @author David Winterfeldt
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class AbstractAuditableEntity extends AbstractPersistable<Integer> implements Auditable<String, Integer> {

    private DateTime lastUpdated;
    private String lastUpdateUser;
    private DateTime created;
    private String createUser;

    /**
     * Overriding just for dozer automatic mapping/conversion.
     */
    @Override
    public void setId(Integer id) {
        super.setId(id);
    }

    /**
     * Gets created by audit user.
     */
    @Override
    public String getCreatedBy() {
        return createUser;
    }

    /**
     * Sets created by audit user.
     */
    @Override
    public void setCreatedBy(String createdBy) {
        this.createUser = createdBy;
    }
    
    /**
     * Gets create audit date.
     */    
    @Override
    public DateTime getCreatedDate() {
        return created;
    }

    /**
     * Sets create audit date.
     */    
    @Override
    public void setCreatedDate(DateTime creationDate) {
        this.created = creationDate;
    }

    /**
     * Gets last modified by audit user.
     */
    @Override
    public String getLastModifiedBy() {
        return lastUpdateUser;
    }
    
    /**
     * Sets last modified by audit user.
     */
    @Override
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastUpdateUser = lastModifiedBy;
    }

    /**
     * Gets last modified audit date.
     */    
    @Override
    public DateTime getLastModifiedDate() {
        return lastUpdated;
    }

    /**
     * Sets last modified audit date.
     */    
    @Override
    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastUpdated = lastModifiedDate;
    }

    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

}
