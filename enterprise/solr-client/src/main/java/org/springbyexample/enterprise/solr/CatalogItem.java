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

package org.springbyexample.enterprise.solr;


/**
 * Catalog item information.
 * 
 * @author David Winterfeldt
 */
public class CatalogItem {

    private String id = null;
    private String manufacturer = null;
    private String name = null;
    private float price = 0.0f;
    private boolean inStock = false;
    private int popularity = 0;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public boolean isInStock() {
        return inStock;
    }
    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
    public int getPopularity() {
        return popularity;
    }
    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

}

