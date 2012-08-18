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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springmodules.jcr.JcrConstants;

/**
 * Processes JCR content repository and 
 * writes out all files into a root directory in the 
 * same hierarchical structure they were found in.
 * 
 * @author David Winterfeldt
 */
public class JcrContentExporter {

    final Logger logger = LoggerFactory.getLogger(JcrContentExporter.class);
    
    protected JcrContentRecurser contentRecurser = null;
    protected String rootFolder = null;

    /**
     * Sets JCR content recurser.
     */
    public void setContentRecurser(JcrContentRecurser contentRecurser) {
        this.contentRecurser = contentRecurser;
    }

    /**
     * Sets root folder where repository content will be copied.
     */
    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }
    
    /**
     * Process content nodes.
     * 
     *  @return     List     List of content node paths processed.     
     */
    public List<String> process() {
        final List<String> lResults = new ArrayList<String>();
        
        contentRecurser.recurse(new JcrNodeCallback() {
            public void doInJcrNode(Session session, Node node)
                    throws IOException, RepositoryException {
                JcrConstants jcrConstants = new JcrConstants(session);
                // file name node is above content
                String fileName = node.getParent().getName();
                // file path is two above content
                String path = node.getParent().getParent().getPath();
                
                logger.debug("In content recurser callback." + 
                             "  fileName='{}' path='{}'", fileName, path);

                File fileDir = new File(rootFolder + path);
                File file = new File(fileDir, fileName);
        
                Property dataProperty = node.getProperty(jcrConstants.getJCR_DATA());
        
                InputStream in = null;
                OutputStream out = null;
                
                try {
                    FileUtils.forceMkdir(fileDir);
                    
                    in = dataProperty.getStream();
                    out = new FileOutputStream(file);
                    
                    IOUtils.copy(in, out);
                    
                    lResults.add(path + File.pathSeparator + fileName);
                } finally {
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }
            }
        });
        
        return lResults;
    }

}
