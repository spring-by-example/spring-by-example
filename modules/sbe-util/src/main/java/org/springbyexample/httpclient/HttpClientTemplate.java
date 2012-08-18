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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Template for easier use of <code>HttpClient</code>.
 * 
 * @author David Winterfeldt
 */
@Component
public class HttpClientTemplate extends AbstractHttpClientTemplate<RequestEntity> {

    final Logger logger = LoggerFactory.getLogger(HttpClientTemplate.class);

    /**
     * Constructor.
     */
    public HttpClientTemplate() {}

    /**
     * Constructor.
     * 
     * @param   defaultUri      Default uri.
     */
    public HttpClientTemplate(String defaultUri) {
        super(defaultUri, false);
    }

    /**
     * Constructor.
     * 
     * @param   defaultUri      Default uri.
     * @param   init            Whether or not to initialize the bean.
     */
    public HttpClientTemplate(String defaultUri, boolean init) {
        super(defaultUri, init);
    }

    /**
     * Execute post method.
     * 
     * @param   input           Byte array <code>RequestEntity</code> to post 
     *                          for the request's data.
     */
    public void executePostMethod(byte[] input) {
        executePostMethod(input, null);
    }
    
    /**
     * Execute post method.
     * 
     * @param   input           Byte array <code>RequestEntity</code> to post 
     *                          for the request's data.
     * @param   callback        Callback with HTTP method's response.
     */
    public void executePostMethod(byte[] input, ResponseCallback<?> callback) {
        executePostMethod(defaultUri,  
                          (input != null ? new ByteArrayRequestEntity(input) : null), 
                          null, callback);
    }
    
    /**
     * Execute post method.
     * 
     * @param   input           <code>String</code> to post 
     *                          for the request's data.
     */
    public void executePostMethod(String input) {
        executePostMethod(input, null);
    }

    /**
     * Execute post method.
     * 
     * @param   input           <code>String</code> to post 
     *                          for the request's data.
     * @param   callback        Callback with HTTP method's response.
     */
    public void executePostMethod(String input, ResponseCallback<?> callback) {
        executePostMethod((input != null ? new ByteArrayInputStream(input.getBytes()) : null),
                          callback);
    }

    /**
     * Execute post method.
     * 
     * @param   input           <code>InputStream</code> to post 
     *                          for the request's data.
     */
    public void executePostMethod(InputStream input) {
        executePostMethod(input, null);
    }

    /**
     * Execute post method.
     * 
     * @param   input           <code>InputStream</code> to post 
     *                          for the request's data.
     * @param   callback        Callback with HTTP method's response.
     */
    public void executePostMethod(InputStream input, ResponseCallback<?> callback) {
        executePostMethod(defaultUri, new InputStreamRequestEntity(input), null, callback);
    }
    
    /**
     * Execute post method.
     * 
     * @param   uri             URI to use when processing this HTTP request instead 
     *                          of using the default URI.
     * @param   requestPayload  <code>RequestEntity</code> data to post.
     * @param   hParams         Parameters for the HTTP post.
     * @param   callback        Callback with HTTP method's response.
     */
    public void executePostMethod(String uri,  
                                  RequestEntity requestPayload, Map<String, String> hParams,
                                  ResponseCallback<?> callback) {
        PostMethod post = new PostMethod(uri);
        
        if (requestPayload != null) {
            post.setRequestEntity(requestPayload);
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
    protected void processHttpMethod(HttpMethod httpMethod, ResponseCallback<?> callback) {
        try {
            client.executeMethod(httpMethod);
            
            validateResponse(httpMethod);
            
            if (callback instanceof ResponseByteCallback) {
                ((ResponseByteCallback)callback).doWithResponse(httpMethod.getResponseBody());
            } else if (callback instanceof ResponseStreamCallback) {
                ((ResponseStreamCallback)callback).doWithResponse(httpMethod.getResponseBodyAsStream());
            } else if (callback instanceof ResponseStringCallback) {
                ((ResponseStringCallback)callback).doWithResponse(httpMethod.getResponseBodyAsString());
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
