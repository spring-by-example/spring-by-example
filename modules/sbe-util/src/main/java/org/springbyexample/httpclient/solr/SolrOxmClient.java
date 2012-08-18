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

package org.springbyexample.httpclient.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.httpclient.HttpClientOxmTemplate;
import org.springbyexample.httpclient.HttpClientTemplate;
import org.springbyexample.httpclient.ResponseCallback;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.util.Assert;


/**
 * Specialized client for processing requests with Solr 
 * that are marshalled and unmarshalled.
 * 
 * @author David Winterfeldt
 */
public class SolrOxmClient<T> implements InitializingBean, DisposableBean {

    final Logger logger = LoggerFactory.getLogger(SolrOxmClient.class);

    public final static String SEARCH_QUERY_PARAM = "q";
    
    protected final static String SELECT_URL_SUFFIX = "/select";
    protected final static String UPDATE_URL_SUFFIX = "/update";

    protected final static String DELETE_ELEMENT_NAME = "delete";
    protected final static String DELETE_ID_ELEMENT_NAME = "id";
    protected final static String DELETE_QUERY_ELEMENT_NAME = "query";

    protected final static String COMMIT_ELEMENT_NAME = "commit";
    protected final static String ROLLBACK_REQUEST = "<rollback/>";
    protected final static String OPTIMIZE_ELEMENT_NAME = "optimize";

    protected HttpClientOxmTemplate<List<T>> selectTemplate = null;
    protected HttpClientOxmTemplate<List<T>> updateTemplate = null;
    protected HttpClientTemplate postTemplate = null;

    protected String baseUrl = null;
    protected Marshaller marshaller = null;
    protected Unmarshaller unmarshaller = null;

    /**
     * Gets base url (ex: http://localhost:8983/solr).
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Sets base url (ex: http://localhost:8983/solr).
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Gets Solr select url.
     */
    protected String getSelectUrl() {
        return baseUrl + SELECT_URL_SUFFIX;
    }

    /**
     * Gets Solr update url.
     */
    protected String getUpdateUrl() {
        return baseUrl + UPDATE_URL_SUFFIX;
    }


    /**
     * Gets marshaller.
     */
    public Marshaller getMarshaller() {
        return marshaller;
    }

    /**
     * Sets marshaller.
     */
    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    /**
     * Gets unmarshaller.
     */
    public Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }

    /**
     * Sets unmarshaller.
     */
    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    /**
     * <p>Initializes <code>HttpClientTemplate</code>s.</p>
     * 
     * <p>Implementation of <code>InitializingBean</code>.</p>
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(baseUrl);
        Assert.notNull(marshaller);
        Assert.notNull(unmarshaller);
        
        selectTemplate = new HttpClientOxmTemplate<List<T>>(getSelectUrl());
        selectTemplate.setMarshaller(marshaller);
        selectTemplate.setUnmarshaller(unmarshaller);
        // call init
        selectTemplate.afterPropertiesSet();

        updateTemplate = new HttpClientOxmTemplate<List<T>>(getUpdateUrl());
        updateTemplate.setMarshaller(marshaller);
        updateTemplate.setUnmarshaller(unmarshaller);
        // call init
        updateTemplate.afterPropertiesSet();

        postTemplate = new HttpClientTemplate(getUpdateUrl(), true);        
    }

    /**
     * <p>Calls destroy on <code>HttpClientTemplate</code>s.</p>
     *  
     * <p>Implementation of <code>DisposableBean</code>.</p>
     */
    public void destroy() throws Exception {
        selectTemplate.destroy();
        updateTemplate.destroy();
        postTemplate.destroy();
    }

    /**
     * Search.
     * 
     * @param   query       Search query.
     */ 
    public List<T> search(String query) {
        Map<String, String> hParams = new HashMap<String, String>();
        hParams.put(SEARCH_QUERY_PARAM, query);

        return search(hParams);
    }

    /**
     * Search.
     * 
     * @param   query       Search query.
     * @param   hParams     <code>Map</code> of query parameters.
     */ 
    public List<T> search(String query, Map<String, String> hParams) {
        hParams.put(SEARCH_QUERY_PARAM, query);

        return search(hParams);
    }

    /**
     * Search.
     * 
     * @param   hParams     <code>Map</code> of query parameters.
     */ 
    public List<T> search(final Map<String, String> hParams) {
        final List<T> lResults = new ArrayList<T>();

        selectTemplate.executeGetMethod(hParams, new ResponseCallback<List<T>>() {
            public void doWithResponse(List<T> lBeans) throws IOException {
                lResults.addAll(lBeans);
            }
        });

        return lResults;
    }

    /**
     * Updates a list of beans and automatically commits the updates.
     * 
     * @param   lBeans      List of beans to create or update.
     */
    public void update(List<T> lBeans) {
        update(lBeans, true);
    }

    /**
     * Updates a list of beans.
     * 
     * @param   lBeans      List of beans to create or update.
     * @param   commit      Whether or not to commit the request.
     */
    public void update(List<T> lBeans, boolean commit) {
        updateTemplate.executePostMethod(lBeans);

        if (commit) {
            commit();
        }
    }

    /**
     * Deletes a record based on an id and automatically commits the delete.
     * 
     * @param   id          ID to delete.
     */
    public void deleteById(String id) {
        deleteById(id, true);
    }

    /**
     * Deletes a record based on an id.
     * 
     * @param   id          ID to delete.
     * @param   commit      Whether or not to commit the request.
     */
    public void deleteById(String id, boolean commit) {
        delete(getEnclosingElement(DELETE_ID_ELEMENT_NAME, id), commit);
    }
    
    /**
     * Deletes an id and automatically commits the delete.
     * 
     * @param   query       ID to delete.
     */
    public void deleteByQuery(String query) {
        deleteByQuery(query, true);
    }

    /**
     * Deletes an id and automatically commits the delete.
     * 
     * @param   query       ID to delete.
     * @param   commit      Whether or not to commit the request.
     */
    public void deleteByQuery(String query, boolean commit) {
        delete(getEnclosingElement(DELETE_QUERY_ELEMENT_NAME, query), commit);
    }

    /**
     * Deletes.
     * 
     * @param   query       Query should either be an <i>id</i> element or 
     *                      <i>query</i> element.
     * @param   commit      Whether or not to commit the request.
     */
    protected void delete(String query, boolean commit) {
        String request = getEnclosingElement(DELETE_ELEMENT_NAME, query);
        
        postTemplate.executePostMethod(request);
        
        if (commit) {
            commit();
        }
        
        logger.info("Processed delete.  request='{}' commit={}", request, commit);
    }

    /**
     * Commits pending transactions.
     */
    public void commit() {
        commit(null);
    }

    /**
     * Commits pending transactions using specified attributes.
     */
    public void commit(SolrRequestAttributes attrs) {
        String request = getElementWithAttributes(COMMIT_ELEMENT_NAME, attrs);
        
        postTemplate.executePostMethod(request);
        
        logger.debug("Processed commit.  request={}", request);
    }

    /**
     * Rollback pending transactions.
     */
    public void rollback() {
        postTemplate.executePostMethod(ROLLBACK_REQUEST);
        
        logger.info("Processed rollback.");
    }

    /**
     * Optimize.
     */
    public void optimize() {
        optimize(null);
    }

    /**
     * Optimize using specified attributes.
     */
    public void optimize(SolrRequestAttributes attrs) {
        String request = getElementWithAttributes(OPTIMIZE_ELEMENT_NAME, attrs);
        
        postTemplate.executePostMethod(request);
        
        logger.debug("Processed optimize.  request={}", request);
    }

    /**
     * Gets an element enclosing a value. 
     */
    private String getEnclosingElement(String elementName, String value) {
        StringBuilder result = new StringBuilder();
        
        result.append("<" +elementName + ">");
        result.append(value);
        result.append("</" +elementName + ">");

        return result.toString();
    }

    /**
     * Gets an element with attributes. 
     */
    private String getElementWithAttributes(String elementName, SolrRequestAttributes attrs) {
        StringBuilder result = new StringBuilder();

        result.append("<");
        result.append(elementName);

        if (attrs != null) {
            if (attrs.getMaxSegments() != null) {
                result.append(" maxSegments=\"" + attrs.getMaxSegments() + "\"");
            }
    
            if (attrs.getWaitFlush() != null) {
                result.append(" waitFlush=\"" + attrs.getWaitFlush() + "\"");
            }
    
            if (attrs.getWaitSearcher() != null) {
                result.append(" waitSearcher=\"" + attrs.getWaitSearcher() + "\"");
            }
        }
        
        result.append("/>");

        return result.toString();
    }
    
}

