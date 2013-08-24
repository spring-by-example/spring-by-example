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

import org.junit.Test;
import org.springbyexample.contact.mvc.rest.service.PersistenceContactMarshallingService;
import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.response.EntityFindResponseResult;
import org.springbyexample.schema.beans.response.EntityResponseResult;
import org.springbyexample.web.service.AbstractPersistenceControllerTest;

/**
 * Tests persistence client against an embedded REST service
 * with the main Spring context as the parent context.
 *
 * @author David Winterfeldt
 */
public abstract class AbstractPersistenceContactControllerTest<R extends EntityResponseResult, FR extends EntityFindResponseResult, S extends PkEntityBase>
        extends AbstractPersistenceControllerTest<R, FR, S> {

    public AbstractPersistenceContactControllerTest(int id, long expectedCount) {
        super(id, expectedCount);
    }

    @Test
    @Override
    public void testDeletePk() {
        // issue with posting body for DELETE from Spring MVC, using url variable
        R response = ((PersistenceContactMarshallingService<R, FR, S>)getClient()).delete(generateDeleteRequest().getId());

        assertNotNull("Response is null.", response);

        int expectedCount = 1;
        assertEquals("messageList.size", expectedCount, response.getMessageList().size());

        logger.info(response.getMessageList().get(0).getMessage());
    }

}
