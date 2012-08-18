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

package org.springbyexample.enterprise.solr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Component;


/**
 * Solr <code>CatalogItem</code> Spring OXM marshaller/unmarshaller.
 * 
 * @author David Winterfeldt
 */
@Component
public class CatalogItemMarshaller implements Marshaller, Unmarshaller {
    
    final Logger logger = LoggerFactory.getLogger(CatalogItemMarshaller.class);

    private static final String ADD_ELEMENT_NAME = "add";
    private static final String DOC_ELEMENT_NAME = "doc";
    private static final String FIELD_ELEMENT_NAME = "field";
    private static final String FIELD_ELEMENT_NAME_ATTRIBUTE = "name";

    /**
     * Implementation of <code>Marshaller</code>. 
     */
    @SuppressWarnings("unchecked")
    public void marshal(Object bean, Result result) throws XmlMappingException, IOException {
        List<CatalogItem> lCatalogItems = (List<CatalogItem>) bean;

        OutputStream out = null;
        XMLWriter writer = null;

        if (result instanceof StreamResult) {
            try {
                out = ((StreamResult) result).getOutputStream();

                Document document = DocumentHelper.createDocument();
                Element root = document.addElement(ADD_ELEMENT_NAME);

                for (CatalogItem item : lCatalogItems) {
                    Element doc = root.addElement(DOC_ELEMENT_NAME);

                    doc.addElement(FIELD_ELEMENT_NAME).addAttribute(FIELD_ELEMENT_NAME_ATTRIBUTE, "id")
                        .addText(item.getId());
                    doc.addElement(FIELD_ELEMENT_NAME).addAttribute(FIELD_ELEMENT_NAME_ATTRIBUTE, "manu")
                        .addText(item.getManufacturer());
                    doc.addElement(FIELD_ELEMENT_NAME).addAttribute(FIELD_ELEMENT_NAME_ATTRIBUTE, FIELD_ELEMENT_NAME_ATTRIBUTE)
                        .addText(item.getName());
                    doc.addElement(FIELD_ELEMENT_NAME).addAttribute(FIELD_ELEMENT_NAME_ATTRIBUTE, "price")
                        .addText(new Float(item.getPrice()).toString());
                    doc.addElement(FIELD_ELEMENT_NAME).addAttribute(FIELD_ELEMENT_NAME_ATTRIBUTE, "inStock")
                        .addText(BooleanUtils.toStringTrueFalse(item.isInStock()));
                    doc.addElement(FIELD_ELEMENT_NAME).addAttribute(FIELD_ELEMENT_NAME_ATTRIBUTE, "popularity")
                        .addText(new Integer(item.getPopularity()).toString());
                }

                writer = new XMLWriter(out);

                writer.write(document);
            } finally {
                try { writer.close(); } catch (Exception e) {}
                IOUtils.closeQuietly(out);
            }

        }

        logger.debug("Marshalled bean of size {}.", lCatalogItems.size());
    }


    /**
     * Implementation of <code>Unmarshaller</code>
     */
    @SuppressWarnings("unchecked")
    public Object unmarshal(Source source) throws XmlMappingException, IOException {
        List<CatalogItem> lResults = new ArrayList<CatalogItem>();

        if (source instanceof StreamSource) {
            InputStream in = null;

            try {
                in = ((StreamSource) source).getInputStream();

                SAXReader reader = new SAXReader();
                Document document = reader.read(in);

                List<Node> lNodes = document.selectNodes("//response/result[@name='response']/doc/*");

                CatalogItem item = null;

                // loop over all matching nodes in order, so can create a new bean 
                // instance on the first match and add it to the results on the last
                for (Node node : lNodes) {
                    if (BooleanUtils.toBoolean(node.valueOf("./@name='id'"))) {
                        item = new CatalogItem();
                        
                        item.setId(node.getText());
                    } else if (BooleanUtils.toBoolean(node.valueOf("./@name='inStock'"))) {
                        item.setInStock(BooleanUtils.toBoolean(node.getText()));
                    } else if (BooleanUtils.toBoolean(node.valueOf("./@name='manu'"))) {
                        item.setManufacturer(node.getText());
                    } else if (BooleanUtils.toBoolean(node.valueOf("./@name='name'"))) {
                        item.setName(node.getText());
                    } else if (BooleanUtils.toBoolean(node.valueOf("./@name='popularity'"))) {
                        item.setPopularity(Integer.parseInt(node.getText()));
                    } else if (BooleanUtils.toBoolean(node.valueOf("./@name='price'"))) {
                        item.setPrice(Float.parseFloat(node.getText()));

                        lResults.add(item);
                    }
                }
            } catch (DocumentException e) {
                throw new UnmarshallingFailureException(e.getMessage(), e);
            } finally {
                IOUtils.closeQuietly(in);
            }

            logger.debug("Unmarshalled bean of size {}.", lResults.size());
        }

        return lResults;
    }

    /**
     * Implementation of <code>Marshaller</code>.
     */
    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return (clazz.isAssignableFrom(List.class));
    }

}

