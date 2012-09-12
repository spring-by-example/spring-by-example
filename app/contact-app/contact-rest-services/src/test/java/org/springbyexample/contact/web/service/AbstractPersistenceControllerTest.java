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
package org.springbyexample.contact.web.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.contact.web.service.PersistenceMarshallingService;
import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.response.EntityFindResponseResult;
import org.springbyexample.schema.beans.response.EntityResponseResult;
import org.springbyexample.schema.beans.response.ResponseResult;

/**
 * Tests persistence client against an embedded REST service 
 * with the main Spring context as the parent context.
 * 
 * @author David Winterfeldt
 */
public abstract class AbstractPersistenceControllerTest<R extends EntityResponseResult, FR extends EntityFindResponseResult, S extends PkEntityBase> 
        extends AbstractRestControllerTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final int id;
    protected final long expectedCount;

    public AbstractPersistenceControllerTest(int id, long expectedCount) {
        this.id = id;
        this.expectedCount = expectedCount;
    }
    
    /**
     * Gets client
     */
    protected abstract PersistenceMarshallingService<R, FR, S> getClient();

    /**
     * Create save request.
     */
    protected abstract S createSaveRequest();

    /**
     * Tests if record is valid.
     */
    protected void verifyRecord(S record) {
        verifyRecord(record, false);
    }

    /**
     * Tests if record is valid and can specify whether or not it was a save.
     */
    protected abstract void verifyRecord(S record, boolean save);

    @Test
    @SuppressWarnings("unchecked")
    public void testFindById() {
        R response = getClient().findById(id);
        
        assertNotNull("Response is null.", response);

        verifyRecord((S) response.getResults());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPaginatedFind() {
        int page = 0;
        int pageSize = 2;
        
        FR response = getClient().find(page, pageSize);
        assertNotNull("Response is null.", response);
        
        assertEquals("count", expectedCount, response.getCount());
        
        assertNotNull("Response results is null.", response.getResults());
        verifyRecord((S) response.getResults().get(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFind() {
        FR response = getClient().find();
        assertNotNull("Response is null.", response);

        assertEquals("count", expectedCount, response.getCount());
        
        assertNotNull("Response results is null.", response.getResults());
        verifyRecord((S) response.getResults().get(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSave() {
        S request = createSaveRequest();
        
        R response = getClient().save(request);
        
        assertNotNull("Response is null.", response);
        
        verifyRecord((S) response.getResults(), true);

        int expectedCount = 1;
        assertEquals("messageList.size", expectedCount, response.getMessageList().size());

        logger.info(response.getMessageList().get(0).getMessage());
    }

//    @Test
//    @SuppressWarnings("unchecked")
//    public void testUpdate() {
//        R response = getClient().findById(id);
//        
//        verifyRecord((S) response.getResults(), true);
//        
//        S request = response.getResults();
//        request.setCreateUser("Testing");
//        
//        getClient().update(request);
//    }

    @Test
    public void testDeletePk() {
        ResponseResult response = getClient().delete(id);
        
        assertNotNull("Response is null.", response);

        int expectedCount = 1;
        assertEquals("messageList.size", expectedCount, response.getMessageList().size());

        logger.info(response.getMessageList().get(0).getMessage());
    }

//    @Test
//    public void testDelete() {
//        ResponseResult response = getClient().delete(createSaveRequest());
//        
//        assertNotNull("Response is null.", response);
//        
//        int expectedCount = 1;
//        assertEquals("messageList.size", expectedCount, response.getMessageList().size());
//        
//        logger.info(response.getMessageList().get(0).getMessage());
//    }

    /**
     * Tests if primary key is valid.
     */
    protected void verifyPrimaryKey(int id, boolean save) {
        if (!save) {
            assertEquals("'id'", id, this.id);
        } else {
            assertTrue("Primary key should be greater than zero.", (id > 0));
        }
    }

    /**
     * Tests if audit info is valid.
     */
    protected void verifyAuditInfo(DateTime lastUpdated, String lastUpdateUser,
                                   DateTime created, String createUser) {
        DateTime now = DateTime.now();
        
        assertNotNull("'lastUpdated' is null", lastUpdated);
        assertNotNull("'lastUpdateUser' is null", lastUpdateUser);
        assertNotNull("'created' is null", created);
        assertNotNull("'createUser' is null", createUser);
        
        assertTrue("'lastUpdated' should be before now.", (lastUpdated.isBefore(now)));
        assertTrue("'created' should be before now.", (created.isBefore(now)));
    }

}
