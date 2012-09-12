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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Abstract list converter.
 * 
 * @author David Winterfeldt
 */
public abstract class AbstractListConverter<T, V> implements ListConverter<T, V> {

    @Override
    public List<V> convertListTo(Collection<T> sourceList) {
        List<V> results = new ArrayList<V>();
        
        for (T item : sourceList) {
            results.add(convertTo(item));
        }
        
        return results;
    }

    @Override
    public List<T> convertListFrom(Collection<V> sourceList) {
        List<T> results = new ArrayList<T>();
        
        for (V item : sourceList) {
            results.add(convertFrom(item));
        }
        
        return results;
    }
    
}
