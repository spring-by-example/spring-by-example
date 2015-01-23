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

package org.springbyexample.integration.book.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.integration.book.BookOrder;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

/**
 * The endpoint for a store order.
 *
 * @author David Winterfeldt
 */
@MessageEndpoint
public class StoreEndpoint {

    final Logger logger = LoggerFactory.getLogger(StoreEndpoint.class);

    /**
     * Process an order for a pickup from the store.
     */
	public void processMessage(Message<BookOrder> message) {
		BookOrder order =  message.getPayload();

        logger.debug("In StoreEndpoint.  title='{}'  quantity={}  orderType={}",
                new Object[] { order.getTitle(),
                               order.getQuantity(),
                               order.getOrderType() });
	}

}
