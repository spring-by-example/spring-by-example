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

package org.springbyexample.jcr;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Used to process a node.
 * 
 * @author David Winterfeldt
 */
public interface JcrNodeCallback {

    /**
     * Called by {@link JcrRecurser#recurse} within an active JCR
     * {@link javax.jcr.JCRSession}. It is run in a <code>JcrTemplate</code>, so 
     * it is not responsible for logging
     * out of the <code>Session</code> or handling transactions.
     */
    public void doInJcrNode(Session session, Node node) 
            throws IOException, RepositoryException;
    
}
