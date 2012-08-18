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
import org.springbyexample.integration.book.OnlineBookOrder;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;

/**
 * Transforms an online order into a message 
 * that contains an address.
 * 
 * @author David Winterfeldt
 */
@MessageEndpoint
public class DeliveryTransformer {

    final Logger logger = LoggerFactory.getLogger(DeliveryTransformer.class);
    
    /**
     * Transforms a <code>BookOrder</code> that is a delivery 
     * into a <code>OnlineBookOrder</code>.
     */
    @Transformer(inputChannel="delivery", outputChannel="post")
	public OnlineBookOrder processMessage(BookOrder order) {
        logger.debug("In DeliveryTransformer.  title='{}'  quantity={}  orderType={}", 
                new Object[] { order.getTitle(), 
                               order.getQuantity(),
                               order.getOrderType() });
        
        return new OnlineBookOrder(order.getTitle(), order.getQuantity(), 
                                   order.getOrderType(), 
                                   "1060 West Addison Chicago, IL 60613");
	}

}
