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
        extends AbstractPersistenceFindControllerTest<R, FR, S> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public AbstractPersistenceControllerTest(int id, long expectedCount) {
        super(id, expectedCount);
    }

    /**
     * Gets find client.
     */
    protected PersistenceFindMarshallingService<R, FR> getFindClient() {
        return getClient();
    }

    /**
     * Gets client.
     */
    protected abstract PersistenceMarshallingService<R, FR, S> getClient();

    /**
     * Create save request.
     */
    protected abstract S createSaveRequest();

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

    @Test
    public void testDeletePk() {
        ResponseResult response = getClient().delete(id);
        
        assertNotNull("Response is null.", response);

        int expectedCount = 1;
        assertEquals("messageList.size", expectedCount, response.getMessageList().size());

        logger.info(response.getMessageList().get(0).getMessage());
    }

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

}
