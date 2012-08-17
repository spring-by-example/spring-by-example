/*
 * Copyright 2004-2009 the original author or authors.
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

package org.springmodules.validation.valang;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Person information.
 * 
 * @author David Winterfeldt
 */
public class Person {

    public enum CreditRating { BAD, POOR, FAIR, GOOD, EXCELLENT }
    
	private String firstName = null;
	private String lastName = null;
	private int age = 0;
    private CreditStatus creditStatus = null;
    private CreditRating creditRating = null;
	private Date lastUpdated = null;
	private List<Address> addresses = new ArrayList<Address>();
	
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
     * Gets last name.
     */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
    /**
     * Gets age.
     */
	public int getAge() {
		return age;
	}

    /**
     * Sets age.
     */
	public void setAge(int age) {
		this.age = age;
	}

	/**
     * Gets credit status.
     */
    public CreditStatus getCreditStatus() {
        return creditStatus;
    }

    /**
     * Sets credit status.
     */
    public void setCreditStatus(CreditStatus creditStatus) {
        this.creditStatus = creditStatus;
    }

	/**
	 * Gets credit rating.
	 */
    public CreditRating getCreditRating() {
        return creditRating;
    }

    /**
     * Sets credit rating.
     */
    public void setCreditRating(CreditRating creditRating) {
        this.creditRating = creditRating;
    }

	/**
	 * Gets last updated date.
	 */
	public Date getLastUpdated() {
        return lastUpdated;
    }

	/**
     * Sets last updated date.
     */
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Gets address list.
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * Sets address list.
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
    
}
