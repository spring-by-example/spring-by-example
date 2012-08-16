/*
 * Copyright 2004-2009 the original author or authors.
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

package org.springmodules.validation.bean.conf.loader.xml;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springmodules.validation.bean.conf.ResourceConfigurationLoadingException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * An {@link AbstractResourceBasedBeanValidationConfigurationLoader} implementation that serves as a base class
 * for all xml baed bean validation configuration loaders.
 *
 * @author Uri Boness
 */
public abstract class AbstractXmlBeanValidationConfigurationLoader extends AbstractResourceBasedBeanValidationConfigurationLoader {

    private final Logger logger = LoggerFactory.getLogger(AbstractXmlBeanValidationConfigurationLoader.class);

    /**
     * todo: document
     *
     * @see org.springmodules.validation.bean.conf.loader.xml.AbstractResourceBasedBeanValidationConfigurationLoader#loadConfigurations(org.springframework.core.io.Resource)
     */
    protected final Map loadConfigurations(Resource resource) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(resource.getInputStream());
            return loadConfigurations(document, resource.getDescription());
        } catch (IOException ioe) {
            logger.error("Could not read resource '" + resource.getDescription() + "'", ioe);
            throw new ResourceConfigurationLoadingException(resource, ioe);
        } catch (ParserConfigurationException pce) {
            logger.error("Could not parse xml resource '" + resource.getDescription() + "'", pce);
            throw new ResourceConfigurationLoadingException(resource, pce);
        } catch (SAXException se) {
            logger.error("Could not parse xml resource '" + resource.getDescription() + "'", se);
            throw new ResourceConfigurationLoadingException(resource, se);
        } catch (Throwable t) {
            logger.error("Could not parse xml resource '" + resource.getDescription() + "'", t);
            throw new ResourceConfigurationLoadingException(resource, t);
        }
    }

    /**
     * todo: document
     */
    protected abstract Map loadConfigurations(Document document, String resourceName);


}
