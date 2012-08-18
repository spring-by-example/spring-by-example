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

package org.springbyexample.ws.service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springbyexample.person.schema.beans.GetPersonsRequest;
import org.springbyexample.person.schema.beans.Person;
import org.springbyexample.person.schema.beans.PersonResponse;

/**
 * Person service interface.
 * 
 * @author David Winterfeldt
 */
public interface MarshallingPersonService {
    
    public final static String NAMESPACE = "http://www.springbyexample.org/person/schema/beans";
    public final static String GET_PERSONS_REQUEST = "get-persons-request";

    /**
     * Gets person list.
     */
    public PersonResponse getPersons(GetPersonsRequest request);

}
