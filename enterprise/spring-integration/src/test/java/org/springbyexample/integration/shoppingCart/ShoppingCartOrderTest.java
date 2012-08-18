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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.integration.book.BookOrder;
import org.springbyexample.integration.book.ShoppingCart;
import org.springbyexample.integration.book.ShoppingCartOrder;
import org.springbyexample.integration.book.BookOrder.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests multiple book orders that can route to 
 * delivery or pickup.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ShoppingCartOrderTest {

    final Logger logger = LoggerFactory.getLogger(ShoppingCartOrderTest.class);

    private final int count = 1; //00 * 1000;
    
    @Autowired
    private ShoppingCartOrder order = null;

    /**
     * Tests shopping cart book orders.
     */
    @Test
    public void testShoppingCartOrder() {
        long start = System.nanoTime();

        List<BookOrder> lBookOrders = new ArrayList<BookOrder>();
        
        BookOrder message01 = new BookOrder("Stranger in a Strange Land", 2, OrderType.PICKUP);
        BookOrder message02 = new BookOrder("Tender is the Night", 1, OrderType.PICKUP);
        BookOrder message03 = new BookOrder("Stranger in a Strange Land", 1, OrderType.DELIVERY);
        
        lBookOrders.add(message01);
        lBookOrders.add(message02);
        lBookOrders.add(message03);
        
        ShoppingCart message = new ShoppingCart(lBookOrders);
        
        for (int i = 1; i <= count; i++) {            
            order.process(message);
        }
        
        long end = System.nanoTime();
        
        logger.info("Shopping cart order pickup test took {}ns", (end - start)/count);
    }

}
