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

import java.util.concurrent.Callable;

/**
 * Thread scope <code>Callable</code> meant to be used 
 * with a thread executor.  Cached thread scope beans are 
 * cleared and bean destruction callbacks are  
 * run after the <code>Callable</code> finishes.
 * 
 * @author David Winterfeldt
 */
public class ThreadScopeCallable<V> implements Callable<V> {
    
    protected Callable<V> target = null;

    /**
     * Constructor
     */
    public ThreadScopeCallable(Callable<V> target) {
        this.target = target;
    }

    /**
     * Runs <code>Runnable</code> target and 
     * then afterword processes thread scope 
     * destruction callbacks.
     */
    public V call() throws Exception {
        try {
            return target.call();
        } finally {
            ThreadScopeContextHolder.currentThreadScopeAttributes().clear();
        }
    }

}