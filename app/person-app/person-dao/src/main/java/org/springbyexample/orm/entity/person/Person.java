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
package org.springbyexample.orm.entity.person;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springbyexample.orm.entity.AbstractAuditableEntity;


/**
 * Annotation configured person bean.
 * 
 * @author David Winterfeldt
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="TYPE", discriminatorType=DiscriminatorType.INTEGER)
public class Person extends AbstractAuditableEntity {

    private static final long serialVersionUID = -2175150694352541150L;

    private String firstName = null;
    private String lastName = null;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="PERSON_ID", nullable=false)
    private Set<Address> addresses = null;
    
    /**
     * Gets first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets list of <code>Address</code>es.
     */
    public Set<Address> getAddresses() {
        return addresses;
    }

    /**
     * Sets list of <code>Address</code>es.
     */
    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * Find an address by it's primary key.
     */
    public Address findAddressById(Integer id) {
        Address result = null;

        if (addresses != null) {
            for (Address address : addresses) {
                if (address.getId().equals(id)) {
                    result = address;

                    break;
                }
            }
        }

        return result;
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.getClass().getName() + "-");
        sb.append("  id=" + getId());
        sb.append("  firstName=" + firstName);
        sb.append("  lastName=" + lastName);

        sb.append("  addresses=[");

        if (addresses != null) {
            for (Address address : addresses) {
                sb.append(address.toString());
            }
        }

        sb.append("]");

        sb.append("  lastUpdated=" + getLastModifiedDate());
        sb.append("  lastUpdateBy=" + getLastModifiedBy());
        sb.append("  created=" + getCreatedDate());
        sb.append("  createdBy=" + getCreatedBy());

        return sb.toString();
    }

}
