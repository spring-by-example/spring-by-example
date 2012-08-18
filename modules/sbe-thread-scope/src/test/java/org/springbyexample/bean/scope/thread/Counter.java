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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Count implementation.
 * 
 * @author David Winterfeldt
 */
public class Counter {

    final Logger logger = LoggerFactory.getLogger(Counter.class);
    
    protected int count = 0;

    /**
     * Gets count.
     */
    public int getCount() {
        return count;
    }

    /**
     * Increments count.
     */
    public synchronized int increment() {
        return ++count;
    }   

    /**
     * Resets bean.
     */
    public void reset() throws Exception {
        logger.debug("Processing reset.");
        
        count = 0;
    }
    
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("{");
        sb.append(this.getClass().getName() + "-");
        sb.append("  count=" + count);
        sb.append("}");
        
        return sb.toString();
    }

}