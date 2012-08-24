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
package org.springbyexample.web.orm.entity;

import java.util.Date;

import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;


/**
 * Annotation configured address bean.
 * 
 * @author David Winterfeldt
 */
@Entity
public class Address extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = 7851794269407495684L;

    private String address = null;
    private String city = null;
    private String state = null;
    private String zipPostal = null;
    private String country = null;
    private Date created = null;

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
     * Gets country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets country.
     */
    public void setCountry(String country) {
        this.country = country;
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
        sb.append("  id=" + getId());
        sb.append("  addresss=" + address);
        sb.append("  city=" + city);
        sb.append("  state=" + state);
        sb.append("  zipPostal=" + zipPostal);
        sb.append("  country=" + country);
        sb.append("  created=" + created);

        return sb.toString();
    }

}
