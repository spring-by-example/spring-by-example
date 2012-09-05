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
package org.springbyexample.web.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    final static Integer ID = 1;

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
    protected abstract void verifyRecord(S record);

    @Test
    @SuppressWarnings("unchecked")
    public void testFindById() {
        R response = getClient().findById(ID);
        
        assertNotNull("Response is null.", response);

        verifyRecord((S) response.getResult());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPaginatedFind() {
        int page = 0;
        int pageSize = 2;
        
        FR response = getClient().find(page, pageSize);
        assertNotNull("Response is null.", response);
        
        int expectedCount = 2;
        assertEquals("count", expectedCount, response.getCount());
        
        assertNotNull("Response results is null.", response.getResults());
        verifyRecord((S) response.getResults().get(0));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFind() {
        FR response = getClient().find();
        assertNotNull("Response is null.", response);

        int expectedCount = 2;
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
        
        verifyRecord((S) response.getResult());

        int expectedCount = 1;
        assertEquals("messageList.size", expectedCount, response.getMessageList().size());

        logger.info(response.getMessageList().get(0).getMessage());
    }

    @Test
    public void testDelete() {
        ResponseResult response = getClient().delete(ID);
        
        assertNotNull("Response is null.", response);

        int expectedCount = 1;
        assertEquals("messageList.size", expectedCount, response.getMessageList().size());

        logger.info(response.getMessageList().get(0).getMessage());
    }
    
}
