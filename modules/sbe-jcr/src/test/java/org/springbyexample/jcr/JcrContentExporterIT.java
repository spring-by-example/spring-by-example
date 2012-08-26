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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.JcrConstants;
import org.junit.Before;
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
 * Tests <code>JcrContentProcessor</code>.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JcrContentExporterIT {

    final Logger logger = LoggerFactory.getLogger(JcrContentExporterIT.class);

    @Autowired
    protected JcrTemplate template = null;

    @Autowired
    private JcrContentExporter processor = null;

    @Before
    public void init() {
        template.execute(new JcrCallback() {
            public Object doInJcr(Session session) throws RepositoryException,
                    IOException {
                String folderName = "fileFolder";
                List<String> lFiles = Arrays.asList(new String[] { "log4j.xml",
                                                                   "jackrabbit-repository.xml" });       
        
                Node root = session.getRootNode();
                Node folderNode = null;
                
                if (root.hasNode(folderName)) {
                    folderNode = root.getNode(folderName);
                } else {
                    folderNode = root.addNode(folderName);

                    session.save();
                }
                
                for (String fileName : lFiles) {
                    saveFile(session, folderNode, fileName);
                }
                
                // save file under other folder
                if (!folderNode.hasNode("org")) {
                    Node orgFolderNode = folderNode.addNode("org").addNode("springbyexample").addNode("jcr");
                    
                    session.save();

                    InputStream in = this.getClass().getResourceAsStream("/org/springbyexample/jcr/JcrContentExporterIT-context.xml");
                    
                    saveFile(session, orgFolderNode, "JcrContentExporterTest-context.xml", in);
                    
                    IOUtils.closeQuietly(in);
                }
                
                return null;
            }
        });
    }
    
    /**
     * Tests recursing nodes.
     */
    @Test
    public void testJcrContentProcessor() {
        assertNotNull("JcrContentProcessor is null.", processor);
        
        List<String> lResults = processor.process();
        
        int size = 3;
        
        assertEquals("Number of processed results should be " + size +".", size, lResults.size());
    }

    /**
     * Saves file.
     */
    private void saveFile(Session session, Node folderNode, String fileName) 
            throws RepositoryException {
        InputStream in = this.getClass().getResourceAsStream("/" + fileName);
        
        saveFile(session, folderNode, fileName, in);
        
        IOUtils.closeQuietly(in);
    }
    
    /**
     * Saves file.
     */
    private void saveFile(Session session, Node folderNode, String fileName, InputStream in) 
            throws RepositoryException {
        if (!folderNode.hasNode(fileName)) {
            Node fileNode = folderNode.addNode(fileName, JcrConstants.NT_FILE);
    
            // create the mandatory child node - jcr:content
            Node node = fileNode.addNode(JcrConstants.JCR_CONTENT, JcrConstants.NT_RESOURCE);
    
            node.setProperty(JcrConstants.JCR_MIMETYPE, "text/xml");
            node.setProperty(JcrConstants.JCR_ENCODING, "UTF-8");
            node.setProperty(new org.springmodules.jcr.JcrConstants(session).getJCR_DATA(), in);
            Calendar lastModified = Calendar.getInstance();
            lastModified.setTimeInMillis(System.currentTimeMillis());
            node.setProperty(JcrConstants.JCR_LASTMODIFIED, lastModified);
    
            session.save();
        }
    }

}
