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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrConstants;
import org.springmodules.jcr.JcrTemplate;

/**
 * Used for recursing through the repository.
 * It also can start at a specified path (default is the root node)
 * and also matching on specific nodes if a node name 
 * is specified.  If no specific node names are 
 * specified, all nodes will be processed.
 * 
 * @author David Winterfeldt
 */
@Component
public class JcrRecurser {

    final Logger logger = LoggerFactory.getLogger(JcrRecurser.class);

    @Autowired
    protected JcrTemplate template = null;
    
    protected String path = null;
    protected String pathDelim = "/";
    protected Set<String> matchingNodes = new HashSet<String>();

    /**
     * Constructor.
     */
    public JcrRecurser() {}
    
    /**
     * Constructor.
     * 
     * @param   path            Relative path from root to start recursing.
     */
    public JcrRecurser(String path) {
        this.path = path;
    }

    /**
     * Constructor.
     * 
     * @param   matchingNodes   <code>Set</code> of node names to match for callbacks.
     */
    public JcrRecurser(Set<String> matchingNodes) {
        this.matchingNodes = matchingNodes;
    }

    /**
     * Constructor.
     * 
     * @param   path            Relative path from root to start recursing.
     * @param   matchingNodes   <code>Set</code> of node names to match for callbacks.
     */
    public JcrRecurser(String path, Set<String> matchingNodes) {
        this.path = path;
        this.matchingNodes = matchingNodes;
    }
    
    /**
     * Gets JCR template.
     */
    public JcrTemplate getTemplate() {
        return template;
    }

    /**
     * Sets JCR template.
     */
    public void setTemplate(JcrTemplate template) {
        this.template = template;
    }

    /**
     * Gets relative path from root to start recursing.
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets relative path from root to start recursing.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets path delimiter.
     */
    public String getPathDelim() {
        return pathDelim;
    }

    /**
     * Sets path delimiter.
     */
    public void setPathDelim(String pathDelim) {
        this.pathDelim = pathDelim;
    }

    /**
     * Gets matching names.  If any node names match, 
     * then the <code>JcrNodeCallback</code> will be 
     * run.  If no specific node names are specified, 
     * all nodes will be processed.
     */
    public Set<String> getMatchingNodeSet() {
        return matchingNodes;
    }

    /**
     * Sets matching names.  If any node names match, 
     * then the <code>JcrNodeCallback</code> will be 
     * run.  If no specific node names are specified, 
     * all nodes will be processed.
     */
    public void setMatchingNodeSet(Set<String> matchingNodes) {
        this.matchingNodes = matchingNodes;
    }

    /**
     * Add matching node.
     */
    public void addMatchingNode(String nodeName) {
        matchingNodes.add(nodeName);
    }

    /**
     * Recurses through all nodes processing the callback 
     * when a matching node is found.
     */
    public void recurse(final JcrNodeCallback callback) {
        template.execute(new JcrCallback() {
            public Object doInJcr(Session session) throws RepositoryException,
                    IOException {
                Node root = session.getRootNode();
                JcrConstants jcrConstants = new JcrConstants(session);
                
                Node startNode = getStartNode(root);
                
                logger.debug("Recursing beginging at '{}' node.", startNode.getName());

                recurseNodes(session, jcrConstants, startNode, callback);

                return null;
            }
        });
    }

    /**
     * Gets start node.  If a path was specified, it will find the node 
     * matching the path (ex: 'albums/album') from the root.  Otherwise 
     * the root passed in will be returned.
     */
    public Node getStartNode(Node root) throws PathNotFoundException, RepositoryException {
        Node result = null;
        
        if (!StringUtils.hasText(path)) {
            result = root;
        } else if (path.indexOf(pathDelim) == -1) {
            result = root.getNode(path);
        } else {
            String[] nodes = StringUtils.split(path, pathDelim);
            Node lastNode = root;
            
            for (String nodeName : nodes) {
                if (StringUtils.hasText(nodeName)) {
                    lastNode = lastNode.getNode(nodeName);
                }
            }
            
            result = lastNode;
        }
    
        return result;
    }
    
    /**
     * Recurse nodes.
     */
    @SuppressWarnings("unchecked")
	protected void recurseNodes(Session session, JcrConstants jcrConstants,
	                            Node node, JcrNodeCallback callback)
            throws IOException, RepositoryException {
        // process callback if no specific node names specified 
        // or there is a match
        if (matchingNodes.size() == 0 ||
            matchingNodes.contains(node.getName())) {
            callback.doInJcrNode(session, node);
        }

        if (node.hasNodes()) {
            for (Iterator<Node> i = (Iterator<Node>)node.getNodes(); i.hasNext();) {
                Node childNode = i.next();

                recurseNodes(session, jcrConstants, childNode, callback);
            }
        }
    }

}
