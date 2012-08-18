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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.integration.book.BookOrder;
import org.springbyexample.integration.book.Delivery;
import org.springbyexample.integration.book.Pickup;
import org.springbyexample.integration.book.ShoppingCart;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Splitter;

/**
 * Splits a shopping cart order into separate delivery and pickup orders.
 * 
 * @author David Winterfeldt
 */
@MessageEndpoint
public class ShoppingCartOrderSplitter {

    final Logger logger = LoggerFactory.getLogger(ShoppingCartOrderSplitter.class);
    
    /**
     * Splits a shopping cart order into separate book orders.
     */
    @Splitter(inputChannel="shoppingCartOrder", outputChannel="deliveryPickupOrder")
    public ShoppingCart[] split(ShoppingCart shoppingCart) {
        logger.debug("In ShoppingCartOrderSplitter.  bookOrderCount={}", 
	                  shoppingCart.getBookOrderList().size());
	    
	    List<BookOrder> lDeliveries = new ArrayList<BookOrder>();
	    List<BookOrder> lPickups = new ArrayList<BookOrder>();
	    
	    for (BookOrder order : shoppingCart.getBookOrderList()) {
	        switch (order.getOrderType()) {
                case DELIVERY:
                    lDeliveries.add(order);
                    break;
                case PICKUP:
                    lPickups.add(order);
                    break;              
	        }
	    }
	    
		return new ShoppingCart[] { new Delivery(lDeliveries), new Pickup(lPickups) };
	}

}
