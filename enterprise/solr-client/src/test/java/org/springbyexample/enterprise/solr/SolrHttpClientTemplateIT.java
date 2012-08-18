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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.httpclient.HttpClientOxmTemplate;
import org.springbyexample.httpclient.HttpClientTemplate;
import org.springbyexample.httpclient.ResponseCallback;
import org.springbyexample.httpclient.ResponseStringCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests Solr using <code>HttpClientTemplate</code> and <code>HttpClientOxmTemplate</code>.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SolrHttpClientTemplateIT {

    final Logger logger = LoggerFactory.getLogger(SolrHttpClientTemplateIT.class);

    private static final String SEARCH_QUERY_PARAM = "belkin";

    private final static String CATALOG_ITEM_ID = "F8V7067-APL-KIT";
    private final static String CATALOG_ITEM_MANUFACTURER = "Belkin";
    private final static String CATALOG_ITEM_NAME = "Belkin Mobile Power Cord for iPod w/ Dock";
    private final static float CATALOG_ITEM_PRICE = 19.95f;
    private final static boolean CATALOG_ITEM_IN_STOCK = false;
    private final static int CATALOG_ITEM_POPULARITY = 1;

    private final static String COMMIT = "<commit/>";

    @Autowired
    @Qualifier("selectTemplate")
    HttpClientTemplate selectTemplate = null; 

    @Autowired
    @Qualifier("updateTemplate")
    HttpClientTemplate updateTemplate = null;
    
    @Autowired
    @Qualifier("marshallingSelectTemplate")
    HttpClientOxmTemplate<List<CatalogItem>> marshallingSelectTemplate = null;

    @Autowired
    @Qualifier("marshallingUpdateTemplate")
    HttpClientOxmTemplate<List<CatalogItem>> marshallingUpdateTemplate = null;
        
    /**
     * Initialize test record with expected results 
     * since update test changes them.
     */
    @Before
    public void init() {
        assertNotNull("HttpClientOxmTemplate is null.", marshallingUpdateTemplate);

        List<CatalogItem> lCatalogItems = new ArrayList<CatalogItem>();

        CatalogItem item = new CatalogItem();
        item.setId(CATALOG_ITEM_ID);
        item.setManufacturer(CATALOG_ITEM_MANUFACTURER);
        item.setName(CATALOG_ITEM_NAME);
        item.setPrice(CATALOG_ITEM_PRICE);
        item.setInStock(CATALOG_ITEM_IN_STOCK);
        item.setPopularity(CATALOG_ITEM_POPULARITY);

        lCatalogItems.add(item);

        marshallingUpdateTemplate.executePostMethod(lCatalogItems);

        // have to commit the updates
        updateTemplate.executePostMethod(COMMIT);        
    }
    
    /**
     * Tests search.
     */ 
    @Test
    public void testSearch() {
        assertNotNull("HttpClientOxmTemplate is null.", marshallingSelectTemplate);

        Map<String, String> hParams = new HashMap<String, String>();
        hParams.put("q", SEARCH_QUERY_PARAM);
        hParams.put("indent", "on");

        // just for debugging
        selectTemplate.executeGetMethod(hParams, new ResponseStringCallback() {
            public void doWithResponse(String response) throws IOException {
                logger.debug(response);
            }
        });

        marshallingSelectTemplate.executeGetMethod(hParams,
            new ResponseCallback<List<CatalogItem>>() {
                public void doWithResponse(List<CatalogItem> lCatalogItems) throws IOException {
                    assertNotNull("Catalog item list is null.", lCatalogItems);

                    int expectedCount = 2;
                    assertEquals("Catalog item list should be '" + expectedCount + "'.", expectedCount, lCatalogItems.size());

                    CatalogItem item = lCatalogItems.get(0);

                    logger.debug("id={}  manufacturer={}  name={} price={} inStock={} popularity={}",
                            new Object[] { item.getId(), item.getManufacturer(), item.getName(),
                                           item.getPrice(), item.isInStock(), item.getPopularity() });

                    assertEquals("Catalog item id should be '" + CATALOG_ITEM_ID + "'.",
                                 CATALOG_ITEM_ID, item.getId());
                    assertEquals("Catalog item manufacturer should be '" + CATALOG_ITEM_MANUFACTURER + "'.",
                                 CATALOG_ITEM_MANUFACTURER, item.getManufacturer());
                    assertEquals("Catalog item name should be '" + CATALOG_ITEM_NAME + "'.",
                                 CATALOG_ITEM_NAME, item.getName());
                    assertEquals("Catalog item price should be '" + CATALOG_ITEM_PRICE + "'.",
                                 CATALOG_ITEM_PRICE, item.getPrice(), 0);
                    assertEquals("Catalog item in stock should be '" + CATALOG_ITEM_IN_STOCK + "'.",
                                 CATALOG_ITEM_IN_STOCK, item.isInStock());
                    assertEquals("Catalog item popularity should be '" + CATALOG_ITEM_POPULARITY + "'.",
                                 CATALOG_ITEM_POPULARITY, item.getPopularity());
                }
        });
    }

    /**
     * Tests paginated search.
     */ 
    @Test
    public void testPaginatedSearch() {
        assertNotNull("HttpClientOxmTemplate is null.", marshallingSelectTemplate);

        Map<String, String> hParams = new HashMap<String, String>();
        hParams.put("q", "electronics");
        hParams.put("start", "5");
        hParams.put("rows", "5");
        hParams.put("indent", "on");

        // just for debugging
        selectTemplate.executeGetMethod(hParams, new ResponseStringCallback() {
            public void doWithResponse(String response) throws IOException {
                logger.debug(response);
            }
        });

        marshallingSelectTemplate.executeGetMethod(hParams,
            new ResponseCallback<List<CatalogItem>>() {
                public void doWithResponse(List<CatalogItem> lCatalogItems) throws IOException {
                    assertNotNull("Catalog item list is null.", lCatalogItems);

                    int expectedCount = 5;
                    assertEquals("Catalog item list should be '" + expectedCount + "'.", expectedCount, lCatalogItems.size());

                    CatalogItem item = lCatalogItems.get(0);

                    logger.debug("id={}  manufacturer={}  name={} price={} inStock={} popularity={}",
                            new Object[] { item.getId(), item.getManufacturer(), item.getName(),
                                           item.getPrice(), item.isInStock(), item.getPopularity() });

                    String expectedId = "3007WFP";
                    String expectedManufacturer = "Dell, Inc.";
                    String expectedName = "Dell Widescreen UltraSharp 3007WFP";
                    float expectedPrice = 2199.0f;
                    boolean expectedInStock = true;
                    int expectedPopularity = 6;
                    
                    assertEquals("Catalog item id should be '" + expectedId + "'.",
                                 expectedId, item.getId());
                    assertEquals("Catalog item manufacturer should be '" + expectedManufacturer + "'.",
                                 expectedManufacturer, item.getManufacturer());
                    assertEquals("Catalog item name should be '" + expectedName + "'.",
                                 expectedName, item.getName());
                    assertEquals("Catalog item price should be '" + expectedPrice + "'.",
                                 expectedPrice, item.getPrice(), 0);
                    assertEquals("Catalog item in stock should be '" + expectedInStock + "'.",
                                 expectedInStock, item.isInStock());
                    assertEquals("Catalog item popularity should be '" + expectedPopularity + "'.",
                                 expectedPopularity, item.getPopularity());
                }
        });
    }
    
    /**
     * Tests adding/updating records.
     */ 
    @Test
    public void testUpdate() {
        assertNotNull("HttpClientOxmTemplate is null.", marshallingUpdateTemplate);

        final int expectedPopularity = 2;
        
        List<CatalogItem> lCatalogItems = new ArrayList<CatalogItem>();

        CatalogItem item = new CatalogItem();
        item.setId(CATALOG_ITEM_ID);
        item.setManufacturer(CATALOG_ITEM_MANUFACTURER);
        item.setName(CATALOG_ITEM_NAME);
        item.setPrice(CATALOG_ITEM_PRICE);
        item.setInStock(CATALOG_ITEM_IN_STOCK);
        item.setPopularity(expectedPopularity);

        lCatalogItems.add(item);

        marshallingUpdateTemplate.executePostMethod(lCatalogItems);

        // have to commit the updates
        updateTemplate.executePostMethod(COMMIT);

        // show and test update
        Map<String, String> hParams = new HashMap<String, String>();
        hParams.put("q", SEARCH_QUERY_PARAM);

        marshallingSelectTemplate.executeGetMethod(hParams,
            new ResponseCallback<List<CatalogItem>>() {
                public void doWithResponse(List<CatalogItem> lCatalogItems) throws IOException {
                    CatalogItem item = lCatalogItems.get(0);
     
                    logger.debug("id={}  manufacturer={}  name={} price={} inStock={} popularity={}",
                            new Object[] { item.getId(), item.getManufacturer(), item.getName(),
                                           item.getPrice(), item.isInStock(), item.getPopularity() });

                    assertEquals("Catalog item popularity should be '" + expectedPopularity + "'.",
                                 expectedPopularity, item.getPopularity());
                }
        });
    }
    
}
