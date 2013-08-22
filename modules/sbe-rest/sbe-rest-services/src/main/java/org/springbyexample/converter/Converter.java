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
package org.springbyexample.converter;



/**
 * Converts to and from two different models.
 * Typically used between a domain &amp; business model.
 * 
 * @author David Winterfeldt
 * 
 * @param   <T>     Domain model bean.
 * @param   <V>     Business model (aka DTO).
 */
public interface Converter<T, V> {

    /**
     * Converts from a domain model to the business model.
     */
    public V convertTo(T source);

    /**
     * Converts from a business model to the domain model.
     */
    public T convertFrom(V source);

}
