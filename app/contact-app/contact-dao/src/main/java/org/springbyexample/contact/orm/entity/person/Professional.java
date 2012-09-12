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
package org.springbyexample.contact.orm.entity.person;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Annotation configured person professional bean.
 * 
 * @author David Winterfeldt
 */
@Entity
@Table(name="PERSON_PROFESSIONAL")
@DiscriminatorValue("2")
public class Professional extends Person {

    private static final long serialVersionUID = 8199967229715812072L;

    private String companyName = null;

    /**
     * Gets company name.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets company name.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
}
