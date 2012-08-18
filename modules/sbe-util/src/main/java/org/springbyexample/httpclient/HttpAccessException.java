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

import org.springframework.core.NestedRuntimeException;

/**
 * HTTP runtime exception for any HTTP error.
 * 
 * @author David Winterfeldt
 */
public class HttpAccessException extends NestedRuntimeException {

    private static final long serialVersionUID = 3144858033479971939L;
    
    protected int statusCode = -1;
    
    /**
     * Constructor.
     * 
     * @param   msg         Error message.
     */
    public HttpAccessException(String msg) {
        super(msg);
    }

    /**
     * Constructor.
     * 
     * @param   msg         Error message.
     * @param   statusCode  HTTP status code.    
     */
    public HttpAccessException(String msg, int statusCode) {
        this(msg);
        this.statusCode = statusCode;
    }

    /**
     * Constructor.
     * 
     * @param   msg         Error message.
     * @param   cause       Cause of error.
     */
    public HttpAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructor.
     * 
     * @param   msg         Error message.
     * @param   cause       Cause of error.
     * @param   statusCode  HTTP status code.    
     */
    public HttpAccessException(String msg, Throwable cause, int statusCode) {
        this(msg, cause);
        this.statusCode = statusCode;
    }
    
}
