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

package org.springbyexample.httpclient.solr;



/**
 * Optional attributes for a commit or optimize request.
 * 
 * @author David Winterfeldt
 */
public class SolrRequestAttributes {

    private Integer maxSegments = null;
    private Boolean waitFlush = null;
    private Boolean waitSearcher = null;
   
    /**
     * Gets max segments.
     * Used to optimize down to at most this number of segments.
     * Defaults to '1'.
     */
    public Integer getMaxSegments() {
        return maxSegments;
    }
    
    /**
     * Sets max segments.
     * Used to optimize down to at most this number of segments.
     * Defaults to '1'.
     */
    public void setMaxSegments(Integer maxSegments) {
        this.maxSegments = maxSegments;
    }
    
    /**
     * Gets whether or not to wait flush.
     * Will block until index changes are flushed to disk.
     * Defaults to <code>true</code>.
     */
    public Boolean getWaitFlush() {
        return waitFlush;
    }

    /**
     * Sets whether or not to wait flush.
     * Will block until index changes are flushed to disk.
     * Defaults to <code>true</code>.
     */
    public void setWaitFlush(Boolean waitFlush) {
        this.waitFlush = waitFlush;
    }

    /**
     * Gets whether or not to wait for the searcher.
     * Will block until a new searcher is opened and registered as the main query searcher, making the changes visible.
     * Defaults to <code>true</code>.
     */
    public Boolean getWaitSearcher() {
        return waitSearcher;
    }

    /**
     * Sets whether or not to wait for the searcher.
     * Will block until a new searcher is opened and registered as the main query searcher, making the changes visible.
     * Defaults to <code>true</code>.
     */
    public void setWaitSearcher(Boolean waitSearcher) {
        this.waitSearcher = waitSearcher;
    }

    
}

