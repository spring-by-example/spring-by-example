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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Template for easier use of <code>HttpClient</code> and 
 * also uses Spring Web Services OXM framework to marshall/unmarshall 
 * XML from requests and responses.
 * 
 * @author David Winterfeldt
 */
@Component
public class HttpClientOxmTemplate<T> extends AbstractHttpClientTemplate<T> 
        implements InitializingBean {

    final Logger logger = LoggerFactory.getLogger(HttpClientOxmTemplate.class);

    protected Marshaller marshaller = null;
    protected Unmarshaller unmarshaller = null;
    
    /**
     * Constructor.
     */
    public HttpClientOxmTemplate() {}

    /**
     * Constructor.
     * 
     * @param   defaultUri      Default uri.
     */
    public HttpClientOxmTemplate(String defaultUri) {
        super(defaultUri, false);
    }

    /**
     * Constructor.
     * 
     * @param   defaultUri      Default uri.
     * @param   marshaller      Marshaller to use for marshalling requests.
     * @param   unmarshaller    Unmarshaller to use for unmarshalling requests.
     */
    public HttpClientOxmTemplate(String defaultUri, 
                                 Marshaller marshaller, Unmarshaller unmarshaller) {
        this(defaultUri);
        
        this.marshaller = marshaller;
        this.unmarshaller = unmarshaller;
    }
    
    /**
     * Constructor.
     * 
     * @param   defaultUri      Default uri.
     * @param   marshaller      Marshaller to use for marshalling requests.
     * @param   unmarshaller    Unmarshaller to use for unmarshalling requests.
     * @param   init            Whether or not to initialize the bean.
     */
    public HttpClientOxmTemplate(String defaultUri, 
                                 Marshaller marshaller, Unmarshaller unmarshaller,
                                 boolean init) {
        this(defaultUri, marshaller, unmarshaller);
        
        if (init) {
            try {
                afterPropertiesSet();
            } catch(Exception e) {
                throw new HttpAccessException(e.getMessage(), e);
            }
        }
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
     * Implementation of <code>InitializingBean</code> 
     * that initializes the <code>HttpClient</code> if it is <code>null</code> 
     * and also sets the connection manager to <code>MultiThreadedHttpConnectionManager</code> 
     * if it is <code>null</code> while initializing the <code>HttpClient</code>.
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(marshaller);
        Assert.notNull(unmarshaller);
        
        super.afterPropertiesSet();
    }

    /**
     * Execute post method.
     * 
     * @param   requestPayload  Request data to post after marshalling.  
     *                          The <code>Marshaller</code> should be able to 
     *                          process this instance. 
     */
    public void executePostMethod(T requestPayload) {
        executePostMethod(defaultUri, requestPayload, null, null);
    }
    
    /**
     * Execute post method.
     * 
     * @param   requestPayload  Request data to post after marshalling.  
     *                          The <code>Marshaller</code> should be able to 
     *                          process this instance. 
     * @param   callback        Callback with HTTP method's response.
     */
    public void executePostMethod(T requestPayload, ResponseCallback<?> callback) {
        executePostMethod(defaultUri, requestPayload, null, callback);
    }

    /**
     * Execute post method.
     * 
     * @param   requestPayload  Request data to post after marshalling.  
     *                          The <code>Marshaller</code> should be able to 
     *                          process this instance. 
     * @param   hParams         Parameters for the HTTP post.
     */
    public void executePostMethod(T requestPayload, Map<String, String> hParams) {
        executePostMethod(requestPayload, hParams, null);
    }

    /**
     * Execute post method.
     * 
     * @param   requestPayload  Request data to post after marshalling.  
     *                          The <code>Marshaller</code> should be able to 
     *                          process this instance. 
     * @param   hParams         Parameters for the HTTP post.
     * @param   callback        Callback with HTTP method's response.
     */
    public void executePostMethod(T requestPayload, Map<String, String> hParams,
                                  ResponseCallback<?> callback) {
        executePostMethod(defaultUri, requestPayload, hParams, callback);
    }
    
    /**
     * Execute post method.
     * 
     * @param   uri             URI to use when processing this HTTP request instead 
     *                          of using the default URI.
     * @param   requestPayload  Request data to post after marshalling.  
     *                          The <code>Marshaller</code> should be able to 
     *                          process this instance. 
     * @param   hParams         Parameters for the HTTP post.
     * @param   callback        Callback with HTTP method's response.
     */
    public void executePostMethod(String uri,  
                                  T requestPayload, Map<String, String> hParams,
                                  ResponseCallback<?> callback) {
        PostMethod post = new PostMethod(uri);
        
        if (requestPayload != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            try {
                marshaller.marshal(requestPayload, new StreamResult(out));
            } catch (IOException e) {
                throw new MarshallingFailureException(e.getMessage(), e);
            }
            
            post.setRequestEntity(new ByteArrayRequestEntity(out.toByteArray()));
        }
        
        processHttpMethodParams(post, hParams);
        
        processHttpMethod(post, callback);
    }
    
    /**
     * Processes <code>HttpMethod</code> by executing the method, 
     * validating the response, and calling the callback.
     * 
     * @param   httpMethod      <code>HttpMethod</code> to process.
     * @param   callback        Callback with HTTP method's response.
     */
    @SuppressWarnings("unchecked")
    protected void processHttpMethod(HttpMethod httpMethod, ResponseCallback callback) {
        try {
            client.executeMethod(httpMethod);
            
            validateResponse(httpMethod);
            
            if (callback != null) {
                Object value = unmarshaller.unmarshal(new StreamSource(httpMethod.getResponseBodyAsStream()));
            
                callback.doWithResponse((T)value);
            }
        } catch (HttpException e) {
            throw new HttpAccessException(e.getMessage(), e, httpMethod.getStatusCode());
        } catch (IOException e) {
            throw new HttpAccessException(e.getMessage(), e);
        } finally {
            httpMethod.releaseConnection();
        }
    }
    
}
