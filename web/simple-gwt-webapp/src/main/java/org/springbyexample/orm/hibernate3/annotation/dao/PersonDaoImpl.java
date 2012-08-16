/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springbyexample.orm.hibernate3.annotation.dao;


import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springbyexample.orm.hibernate3.annotation.bean.Address;
import org.springbyexample.orm.hibernate3.annotation.bean.Person;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Person DAO implementation.
 * 
 * @author David Winterfeldt
 */
@Repository
@Transactional(readOnly = true)
public class PersonDaoImpl implements PersonDao {

    protected HibernateTemplate template = null;

    /**
     * Sets Hibernate session factory.
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        template = new HibernateTemplate(sessionFactory);
    }
    
    /**
     * Find persons.
     */
    @SuppressWarnings("unchecked")
    public Person findPersonById(Integer id) {
        return (Person) template.get(Person.class, id);
    }
    
    /**
     * Find persons using a start index and max number of results.
     */
    @SuppressWarnings("unchecked")
    public Collection<Person> findPersons(final int startIndex, final int maxResults) {
        return (Collection<Person>) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(Person.class);
                criteria.addOrder(Order.asc("lastName"));
                criteria.addOrder(Order.asc("firstName"));
                criteria.setFirstResult(startIndex);
                criteria.setMaxResults(maxResults);
                
                return new TreeSet(criteria.list());
            }
        });
    }

    /**
     * Find persons.
     */
    @SuppressWarnings("unchecked")
    public Collection<Person> findPersons() {
        return template.find("from Person p order by p.lastName, p.firstName");
    }

    /**
     * Find persons by last name.
     */
    @SuppressWarnings("unchecked")
    public Collection<Person> findPersonsByLastName(String lastName) {
        return template.find("from Person p where p.lastName = ? order by p.lastName, p.firstName", lastName);
    }
    
    /**
     * Saves person.
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Person save(Person person) {
        Person result = (Person) template.merge(person);
        
        return result;
    }

    /**
     * Deletes person.
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void delete(Person person) {
        template.delete(person);
    }

    /**
     * Saves address to person.
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Person saveAddress(Integer id, Address address) {
        Person person = findPersonById(id);

        if (person.getAddresses().contains(address)) {
            person.getAddresses().remove(address);
        }
        
        person.getAddresses().add(address);
        
        return save(person);
    }
    
    /**
     * Deletes address to person.
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Person deleteAddress(Integer id, Integer addressId) {
        Person person = findPersonById(id);
        
        Address address = new Address();
        address.setId(addressId);
        
        if (person.getAddresses().contains(address)) {
            person.getAddresses().remove(address);
        }
        
        return save(person);
    }
    
}
