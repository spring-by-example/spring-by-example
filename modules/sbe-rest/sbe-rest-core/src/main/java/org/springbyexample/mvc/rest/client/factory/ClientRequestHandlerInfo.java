/*
 * Copyright 2007-2013 the original author or authors.
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
package org.springbyexample.mvc.rest.client.factory;

import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Client request handler information.
 *
 * @author David Winterfeldt
 */
public class ClientRequestHandlerInfo {

    private final String pattern;
    private final Class<?> responseClazz;
    private final RequestMethod method;

    public ClientRequestHandlerInfo(String pattern, Class<?> responseClazz, RequestMethod method) {
        this.pattern = pattern;
        this.responseClazz = responseClazz;
        this.method = method;
    }

    /**
     * Gets pattern.
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Gets response class.
     */
    public Class<?> getResponseClazz() {
        return responseClazz;
    }

    /**
     * Gets request method.
     */
    public RequestMethod getMethod() {
        return method;
    }
 
}
