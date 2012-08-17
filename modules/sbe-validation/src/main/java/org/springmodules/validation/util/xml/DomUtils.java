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

package org.springmodules.validation.util.xml;

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility methods/functions to deal with org.w3c.dom constructs.
 *
 * @author Uri Boness
 */
public class DomUtils {

    public static Element firstChildElement(Element element) {
        Iterator iter = childElements(element);
        return (iter.hasNext()) ? (Element) iter.next() : null;
    }

    public static Iterator childElements(Element element) {
        return new SubElementsIterator(element);
    }

    public static Iterator childNodes(Node node) {
        return new ChildNodesIterator(node);
    }

    public static Element getSingleSubElement(Element parent, String elementNamespace, String localName) {
        NodeList nodes = parent.getElementsByTagNameNS(elementNamespace, localName);
        return (nodes.getLength() > 0) ? (Element) nodes.item(0) : null;
    }

    // this class should not be instanciated.
    private DomUtils() {
    }

}

