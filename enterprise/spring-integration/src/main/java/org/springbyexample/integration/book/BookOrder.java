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

package org.springbyexample.integration.book;

/**
 * Book order information.
 * 
 * @author David Winterfeldt
 */
public class BookOrder {

    public enum OrderType { DELIVERY, PICKUP }

    private final String title;
    private final int quantity;
    private final OrderType orderType;

    /**
     * Constructor
     */
    public BookOrder(String title, int quantity, OrderType orderType) {
        this.title = title;
        this.quantity = quantity;
        this.orderType = orderType;
    }

    /**
     * Gets title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets order type.
     */
    public OrderType getOrderType() {
        return orderType;
    }

}
