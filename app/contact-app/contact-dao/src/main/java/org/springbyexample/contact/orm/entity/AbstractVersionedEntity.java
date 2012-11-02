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
import javax.persistence.Version;


/**
 * Abstract auditable and optimistic lock enabled entity.
 * 
 * @author David Winterfeldt
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class AbstractVersionedEntity extends AbstractAuditableEntity {

    @Version
    protected int lockVersion;

    /**
     * Gets lock version, used for optimistic locking.
     */
    public int getLockVersion() {
        return lockVersion;
    }

    /**
     * <p>Sets lock version, used for optimistic locking.</p>
     * 
     * <p><strong>Note</strong>: Shouldn't be set except by JPA, 
     * setter needed for matching getter/setter for conversion 
     * between entities and JAXB beans.
     */
    public void setLockVersion(int lockVersion) {
        this.lockVersion = lockVersion;
    }
    
}
