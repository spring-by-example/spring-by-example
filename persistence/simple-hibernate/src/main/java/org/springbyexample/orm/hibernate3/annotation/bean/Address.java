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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Annotation configured address bean.
 * 
 * @author David Winterfeldt
 */
@Entity
@Table(name="ADDRESS")
public class Address {

    private Integer id = null;
    private String address = null;
    private String city = null;
    private String state = null;
    private String zipPostal = null;
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
     * Gets address.
     */
	public String getAddress() {
		return address;
	}

    /**
     * Sets address.
     */
	public void setAddress(String address) {
		this.address = address;
	}

    /**
     * Gets city.
     */
	public String getCity() {
		return city;
	}

    /**
     * Sets city.
     */
	public void setCity(String city) {
		this.city = city;
	}

    /**
     * Gets state.
     */
	public String getState() {
		return state;
	}

    /**
     * Sets state.
     */
	public void setState(String state) {
		this.state = state;
	}

    /**
     * Gets zip or postal code.
     */
	@Column(name="ZIP_POSTAL")
	public String getZipPostal() {
		return zipPostal;
	}

    /**
     * Sets zip or postal code.
     */
	public void setZipPostal(String zipPostal) {
		this.zipPostal = zipPostal;
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
        sb.append("  addresss=" + address);
        sb.append("  city=" + city);
        sb.append("  state=" + state);
        sb.append("  zipPostal=" + zipPostal);
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
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result
				+ ((zipPostal == null) ? 0 : zipPostal.hashCode());
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
		final Address other = (Address) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (zipPostal == null) {
			if (other.zipPostal != null)
				return false;
		} else if (!zipPostal.equals(other.zipPostal))
			return false;
		return true;
	}

}
