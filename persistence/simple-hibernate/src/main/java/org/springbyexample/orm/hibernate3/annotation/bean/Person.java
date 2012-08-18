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

package org.springbyexample.orm.hibernate3.annotation.bean;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Annotation configured person bean.
 * 
 * @author David Winterfeldt
 */
@Entity
@Table(name="PERSON")
public class Person {

    private Integer id = null;
    private String firstName = null;
    private String lastName = null;
    private Set<Address> addresses = null;
    private Date created = null;
    
    /**
     * Gets id (primary key).
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    /**
     * Sets id (primary key).
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * Gets first name.
     */
    @Column(name="FIRST_NAME")
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
    @Column(name="LAST_NAME")
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
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="PERSON_ID", nullable=false)
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
     * Gets date created.
     */
	public Date getCreated() {
		return created;
	}

    /**
     * Sets date created.
     */
	public void setCreated(Date created) {
		this.created = created;
	}
	
    /**
     * Returns a string representation of the object. 
     */
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(this.getClass().getName() + "-");
        sb.append("  id=" + id);
        sb.append("  firstName=" + firstName);
        sb.append("  lastName=" + lastName);
        
        sb.append("  addresses=[");
        
        if (addresses != null) {
        	for (Address address : addresses) {
        		sb.append(address.toString());
        	}
        }
        
        sb.append("]");
        
        sb.append("  created=" + created);
        
        return sb.toString();
    }

    /**
     * Returns a hash code value for the object.
     */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((addresses == null) ? 0 : addresses.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	/**
	 * Indicates whether some other object is equal to this one.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Person other = (Person) obj;
		if (addresses == null) {
			if (other.addresses != null)
				return false;
		} else if (!addresses.equals(other.addresses))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

}
