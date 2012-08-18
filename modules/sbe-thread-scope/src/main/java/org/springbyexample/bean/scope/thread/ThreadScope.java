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

package org.springbyexample.bean.scope.thread;

import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * Thread scope implementation.
 * 
 * @author David Winterfeldt
 */
public class ThreadScope implements Scope {

    /**
     * Gets bean from scope.
     */
    public Object get(String name, ObjectFactory<?> factory) {
        Object result = null;
        
        Map<String, Object> hBeans = ThreadScopeContextHolder.currentThreadScopeAttributes().getBeanMap();
        
        if (!hBeans.containsKey(name)) {
            result = factory.getObject();
            
            hBeans.put(name, result);
        } else {
            result = hBeans.get(name);
        }
        
        
        return result;
    }
    
    /**
     * Removes bean from scope.
     */
    public Object remove(String name) {
        Object result = null;
        
        Map<String, Object> hBeans = ThreadScopeContextHolder.currentThreadScopeAttributes().getBeanMap();

        if (hBeans.containsKey(name)) {
            result = hBeans.get(name);
            
            hBeans.remove(name);
        }

        return result;
    }

    public void registerDestructionCallback(String name, Runnable callback) {
        ThreadScopeContextHolder.currentThreadScopeAttributes().registerRequestDestructionCallback(name, callback);
    }

    /**
     * Resolve the contextual object for the given key, if any.
     * Which in this case will always be <code>null</code>.
     */
    public Object resolveContextualObject(String key) {
        return null;
    }

    /**
     * Gets current thread name as the conversation id.
     */
    public String getConversationId() {
        return Thread.currentThread().getName();
    }

}