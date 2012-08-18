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

package org.springbyexample.springModulesJcr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.JcrConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

/**
 * Tests <code>JcrTemplate</code> and traversing nodes.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JcrNodeCreationIT {

    final Logger logger = LoggerFactory.getLogger(JcrNodeCreationIT.class);

    @Autowired
    private JcrTemplate template = null;

    private String nodeName = "fileFolder";

    /**
     * Adds node if it doesn't exist.
     */
    @Test
    public void testAddNodeIfDoesntExist() {
        assertNotNull("JCR Template is null.", template);

        template.execute(new JcrCallback() {
            public Object doInJcr(Session session) throws RepositoryException {
                Node root = session.getRootNode();

                logger.debug("Starting from root node.  node={}", root);

                Node node = null;

                if (root.hasNode(nodeName)) {
                    node = root.getNode(nodeName);

                    logger.debug("Node exists.  node={}", node);
                } else {
                    node = root.addNode(nodeName);

                    session.save();

                    logger.info("Saved node.  node={}", node);
                }

                assertNotNull("Node is null.", node);

                return node;
            }
        });
    }

    /**
     * Adds file to repository.
     */
    @Test
    public void testAddFileIfDoesntExist() {
        @SuppressWarnings("unused")
        Node node = (Node) template.execute(new JcrCallback() {
            @SuppressWarnings("unchecked")
            public Object doInJcr(Session session) throws RepositoryException,
                    IOException {
                Node resultNode = null;

                Node root = session.getRootNode();
                logger.info("starting from root node.  node={}", root);

                // should have been created in previous test
                Node folderNode = root.getNode(nodeName);

                String fileName = "log4j.xml";

                if (folderNode.hasNode(fileName)) {
                    logger.debug("File already exists.  file={}", fileName);
                } else {
                    InputStream in = this.getClass().getResourceAsStream("/" + fileName);

                    Node fileNode = folderNode.addNode(fileName, JcrConstants.NT_FILE);

                    // create the mandatory child node - jcr:content
                    resultNode = fileNode.addNode(JcrConstants.JCR_CONTENT, JcrConstants.NT_RESOURCE);

                    resultNode.setProperty(JcrConstants.JCR_MIMETYPE, "text/xml");
                    resultNode.setProperty(JcrConstants.JCR_ENCODING, "UTF-8");
                    resultNode.setProperty(JcrConstants.JCR_DATA, in);
                    Calendar lastModified = Calendar.getInstance();
                    lastModified.setTimeInMillis(System.currentTimeMillis());
                    resultNode.setProperty(JcrConstants.JCR_LASTMODIFIED, lastModified);

                    session.save();

                    IOUtils.closeQuietly(in);

                    logger.debug("Created '{}' file in folder.", fileName);
                }

                assertTrue("File node, '" + fileName + "', doesn't exist.", folderNode.hasNode(fileName));
                assertTrue("File content node, '" + fileName + "', doesn't exist.", 
                           folderNode.getNode(fileName).hasNode(JcrConstants.JCR_CONTENT));

                Node contentNode = folderNode.getNode(fileName).getNode(JcrConstants.JCR_CONTENT);

                Property dataProperty = contentNode.getProperty(JcrConstants.JCR_DATA);

                List<String> list = (List<String>) IOUtils.readLines(dataProperty.getStream());

                assertEquals("First line isn't correct.", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>", 
                             list.get(0));
                assertEquals("Last line isn't correct.", "</log4j:configuration>", 
                             list.get(21));

                return resultNode;
            }
        });
    }

}
