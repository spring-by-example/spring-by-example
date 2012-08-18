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

package org.springbyexample.orm.hibernate3.dao;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests transactions using Spring's transaction unit test 
 * framework.  The spring configuration doesn't use 
 * &lt;tx:annotation-driven/&gt; so the <code>@Transactional</code> 
 * annotation in the <code>PersonDaoImpl</code> class isn't used.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration
@Transactional
public class PersonDaoTransactionUnitRollbackTest extends PersonDaoTransactionUnitTest {

    final Logger logger = LoggerFactory.getLogger(PersonDaoTransactionUnitRollbackTest.class);

    /**
     * Tests person table and changes the first records last name.
     * The default rollback is <code>true</code>, but this changes 
     * this one method to <code>false</code>.
     */
    @Test
    @Rollback(false)
    public void testHibernateTemplate() throws SQLException {
        super.testHibernateTemplate();
    }
    
    /**
     * Tests that the size and first record match what is expected 
     * after the transaction.
     */
    @AfterTransaction
    public void afterTransaction() {
        testPerson(false, CHANGED_LAST_NAME);
    }
    
}
