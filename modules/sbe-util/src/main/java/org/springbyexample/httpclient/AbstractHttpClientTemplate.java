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

package org.springbyexample.httpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springbyexample.httpclient.auth.Credentials;
import org.springbyexample.httpclient.auth.NTCredentials;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Base <code>HttpClient</code> template class.
 * 
 * @author David Winterfeldt
 */
public abstract class AbstractHttpClientTemplate<T> implements InitializingBean, DisposableBean {

    protected HttpClient client = null;
    protected HttpConnectionManager connectionManager = null;
    protected String defaultUri = null;
    protected boolean authenticationPreemptive = false;
    protected List<Credentials> lCredentials = new ArrayList<Credentials>();

    /**
     * Constructor.
     */
    public AbstractHttpClientTemplate() {}

    /**
     * Constructor.
     * 
     * @param   defaultUri      Default uri.
     */
    public AbstractHttpClientTemplate(String defaultUri) {
        this(defaultUri, false);
    }

    /**
     * Constructor.
     * 
     * @param   defaultUri      Default uri.
     * @param   init            Whether or not to initialize the bean 
     *                          (typically for programatic use).
     */
    public AbstractHttpClientTemplate(String defaultUri, boolean init) {
        this.defaultUri = defaultUri;
        
        if (init) {
            try {
                afterPropertiesSet();
            } catch(Exception e) {
                throw new HttpAccessException(e.getMessage(), e);
            }
        }
    }
    
    /**
     * Gets http client.
     */
    public HttpClient getClient() {
        return client;
    }

    /**
     * Sets http client.
     */
    public void setClient(HttpClient client) {
        this.client = client;
    }

    /**
     * Gets connection manager.
     */
    public HttpConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Sets connection manager.
     */
    public void setConnectionManager(HttpConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Gets default uri.
     */
    public String getDefaultUri() {
        return defaultUri;
    }

    /**
     * Sets default uri.
     */
    public void setDefaultUri(String defaultUri) {
        this.defaultUri = defaultUri;
    }

    /**
     * Whether or not authentication is preemptive.
     * If <code>true</code>, authentication credentials 
     * will be sent before a challenge is issued 
     * for an authentication scope with credentials.
     * Defaults to <code>false</code>.
     */
    public boolean isAuthenticationPreemptive() {
        return authenticationPreemptive;
    }

    /**
     * Sets whether or not authentication is preemptive.
     * If <code>true</code>, authentication credentials 
     * will be sent before a challenge is issued 
     * for an authentication scope with credentials.
     * Defaults to <code>false</code>.
     */
    public void setAuthenticationPreemptive(boolean authenticationPreemptive) {
        this.authenticationPreemptive = authenticationPreemptive;
    }

    /**
     * Gets HTTP authorization credentials.
     */
    public List<Credentials> getCredentials() {
        return lCredentials;
    }

    /**
     * Sets HTTP authorization credentials.
     */
    public void setCredentials(List<Credentials> credentials) {
        this.lCredentials = credentials;
    }
    
    /**
     * Implementation of <code>InitializingBean</code> 
     * that initializes the <code>HttpClient</code> if it is <code>null</code> 
     * and also sets the connection manager to <code>MultiThreadedHttpConnectionManager</code> 
     * if it is <code>null</code> while initializing the <code>HttpClient</code>.
     */
    public void afterPropertiesSet() throws Exception {
        // make sure credentials are properly set
        for (Credentials credentials : lCredentials) {
            Assert.notNull(credentials.getAuthScopeHost());
            Assert.isTrue(credentials.getAuthScopePort() > 0);
            Assert.notNull(credentials.getUserName());
            Assert.notNull(credentials.getPassword());
            
            if (credentials instanceof NTCredentials) {
                Assert.notNull(((NTCredentials)credentials).getHost());
                Assert.notNull(((NTCredentials)credentials).getDomain());
            }
        }
        
        if (client == null) {
            if (connectionManager == null) {
                connectionManager = new MultiThreadedHttpConnectionManager();
            }

            client = new HttpClient(connectionManager);
        }

        client.getParams().setAuthenticationPreemptive(authenticationPreemptive);
        
        for (Credentials credentials : lCredentials) {
            AuthScope authScope = new AuthScope(credentials.getAuthScopeHost(), 
                                                credentials.getAuthScopePort(), 
                                                AuthScope.ANY_REALM);
            
            org.apache.commons.httpclient.Credentials httpCredentials = null;
            
            if (credentials instanceof NTCredentials) {
                httpCredentials = new org.apache.commons.httpclient.NTCredentials(
                        credentials.getUserName(), 
                        credentials.getPassword(),
                        ((NTCredentials)credentials).getHost(),
                        ((NTCredentials)credentials).getDomain());     
            } else {
                httpCredentials = new UsernamePasswordCredentials(credentials.getUserName(), 
                                                                  credentials.getPassword());
            }
            
            client.getState().setCredentials(authScope, httpCredentials);
        }
    }

    /**
     * Implementation of <code>DisposableBean</code> that 
     * shuts down the connection manager if it is an instance of 
     * <code>MultiThreadedHttpConnectionManager</code>.
     */
    public void destroy() throws Exception {
        if (client.getHttpConnectionManager() instanceof MultiThreadedHttpConnectionManager) {
            ((MultiThreadedHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
        }
    }

    /**
     * Execute get method.
     */
    public void executeGetMethod() {
        executeGetMethod(defaultUri, null, null);
    }
    
    /**
     * Execute get method.
     * 
     * @param   callback        Callback with HTTP method's response.
     */
    public void executeGetMethod(ResponseCallback<?> callback) {
        executeGetMethod(defaultUri, null, callback);
    }

    /**
     * Execute get method.
     * 
     * @param   hParams         Parameters for the HTTP get.
     */
    public void executeGetMethod(Map<String, String> hParams) {
        executeGetMethod(defaultUri, hParams, null);
    }
    
    /**
     * Execute get method.
     * 
     * @param   hParams         Parameters for the HTTP get.
     * @param   callback        Callback with HTTP method's response.
     */
    public void executeGetMethod(Map<String, String> hParams, ResponseCallback<?> callback) {
        executeGetMethod(defaultUri, hParams, callback);
    }
    
    /**
     * Execute get method.
     * 
     * @param   uri             URI to use when processing this HTTP request instead 
     *                          of using the default URI.
     * @param   hParams         Parameters for the HTTP get.
     */
    public void executeGetMethod(String uri, Map<String, String> hParams) {
        executeGetMethod(uri, hParams, null);
    }
    
    /**
     * Execute get method.
     * 
     * @param   uri             URI to use when processing this HTTP request instead 
     *                          of using the default URI.
     * @param   hParams         Parameters for the HTTP get.
     * @param   callback        Callback with HTTP method's response.
     */
    public void executeGetMethod(String uri, Map<String, String> hParams, ResponseCallback<?> callback) {
        GetMethod get = new GetMethod(uri);

        processHttpMethodParams(get, hParams);
        
        processHttpMethod(get, callback);
    }

    /**
     * Execute post method.
     */
    public void executePostMethod() {
        executePostMethod(defaultUri, null, null, null);
    }

    /**
     * Execute post method.
     * 
     * @param   callback        Callback with HTTP method's response.
     */
    public void executePostMethod(ResponseCallback<?> callback) {
        executePostMethod(defaultUri, null, null, callback);
    }

    /**
     * Execute post method.
     * 
     * @param   hParams         Parameters for the HTTP post.
     */
    public void executePostMethod(Map<String, String> hParams) {
        executePostMethod(defaultUri, null, hParams, null);
    }
    
    /**
     * Execute post method.
     * 
     * @param   hParams         Parameters for the HTTP post.
     * @param   callback        Callback with HTTP method's response.
     */
    public void executePostMethod(Map<String, String> hParams, ResponseCallback<?> callback) {
        executePostMethod(defaultUri, null, hParams, callback);
    }

    /**
     * Execute post method.
     * 
     * @param   uri             URI to use when processing this HTTP request instead 
     *                          of using the default URI.
     * @param   requestPayload  Request data to post.
     * @param   hParams         Parameters for the HTTP post.

     */
    public void executePostMethod(String uri,
                                  T requestPayload, Map<String, String> hParams) {
        executePostMethod(uri, requestPayload, hParams, null);
    }
    
    /**
     * Execute post method.
     * 
     * @param   uri             URI to use when processing this HTTP request instead 
     *                          of using the default URI.
     * @param   requestPayload  Request data to post.
     * @param   hParams         Parameters for the HTTP post.
     * @param   callback        Callback with HTTP method's response.

     */
    public abstract void executePostMethod(String uri,
                                           T requestPayload, Map<String, String> hParams,
                                           ResponseCallback<?> callback);
    
    /**
     * Processes <code>HttpMethod</code> by executing the method, 
     * validating the response, and calling the callback.
     * 
     * @param   httpMethod      <code>HttpMethod</code> to process.
     * @param   callback        Callback with HTTP method's response.
     */
    protected abstract void processHttpMethod(HttpMethod httpMethod, ResponseCallback<?> callback);

    /**
     * Processes <code>HttpMethod</code> parameters.
     * 
     * @param   httpMethod      <code>HttpMethod</code> to process.
     * @param   hParams         Parameters for the HTTP get.
     */
    protected void processHttpMethodParams(HttpMethod httpMethod, Map<String, String> hParams) {
        if (hParams != null) {
            List<NameValuePair> lParams = new ArrayList<NameValuePair>();
            
            for (Object key : hParams.keySet()) {
                Object value = hParams.get(key);
                
                lParams.add(new NameValuePair(key.toString(), value.toString()));
            }

            if (httpMethod instanceof GetMethod) {
                ((GetMethod)httpMethod).setQueryString(lParams.toArray(new NameValuePair[] {}));
            } else if (httpMethod instanceof PostMethod) {
                ((PostMethod)httpMethod).setRequestBody(lParams.toArray(new NameValuePair[] {}));
            }
        }
    }

    /**
     * Validate response.
     * 
     * @param   httpMethod      <code>HttpMethod</code> to validate.
     */
    protected void validateResponse(HttpMethod httpMethod) {
        if (httpMethod.getStatusCode() >= 300) {
            throw new HttpAccessException(
                    "Did not receive successful HTTP response: status code = " + 
                    httpMethod.getStatusCode() + 
                    ", status message = [" + httpMethod.getStatusText() + "]", httpMethod.getStatusCode());
        }
    }
    
}
