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

/**
 * Thread scope context holder.  It stores a 
 * <code>ThreadScopeAttributes</code> in a <code>ThreadLocal</code> 
 * variable to proved thread scoped access.
 * 
 * @author David Winterfeldt
 */
public class ThreadScopeContextHolder {
    
    private static final ThreadLocal<ThreadScopeAttributes> threadScopeAttributesHolder = new InheritableThreadLocal<ThreadScopeAttributes>() {
        protected ThreadScopeAttributes initialValue() {
            return new ThreadScopeAttributes();
        }
    };

    /**
     * Gets <code>ThreadScopeAttributes</code>.
     */
    public static ThreadScopeAttributes getThreadScopeAttributes() {
        return threadScopeAttributesHolder.get();
    }

    /**
     * Sets <code>ThreadScopeAttributes</code>.
     */
    public static void setThreadScopeAttributes(ThreadScopeAttributes accessor) {
        ThreadScopeContextHolder.threadScopeAttributesHolder.set(accessor);
    }

    /**
     * Gets current <code>ThreadScopeAttributes</code>.
     */
    public static ThreadScopeAttributes currentThreadScopeAttributes() throws IllegalStateException {
        ThreadScopeAttributes accessor = threadScopeAttributesHolder.get();
        
        if (accessor == null) {
            throw new IllegalStateException("No thread scoped attributes.");
        }
        
        return accessor;
    }


}