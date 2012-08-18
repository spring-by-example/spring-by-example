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

package org.springbyexample.integration.shoppingCart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.integration.book.BookOrder;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;

/**
 * Routes order based on order type.
 * 
 * @author David Winterfeldt
 */
@MessageEndpoint
public class OrderRouter {

    final Logger logger = LoggerFactory.getLogger(OrderRouter.class);
    
    /**
     * Process order.  Routes based on whether or 
     * not the order is a delivery or pickup.
     */
	@Router(inputChannel="processOrder")
	public String processOrder(BookOrder order) {
	    String result = null;
	    
	    logger.debug("In OrderRouter.  title='{}'  quantity={}  orderType={}", 
	                 new Object[] { order.getTitle(), 
	                                order.getQuantity(),
	                                order.getOrderType() });
	    
	    switch (order.getOrderType()) {
	        case DELIVERY:
	            result = "delivery";
	            break;
            case PICKUP:
                result = "pickup";
                break;	            
	    }
	    
		return result;
	}

}
