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

import org.springmodules.validation.util.collection.FilteredIterator;
import org.springmodules.validation.util.condition.AbstractCondition;
import org.w3c.dom.Node;

/**
 * An iterator that iterates over the child elements of a given node.
 *
 * @author Uri Boness
 */
public class SubElementsIterator extends FilteredIterator {

    public SubElementsIterator(Node node) {
        super(new ChildNodesIterator(node), new AbstractCondition() {
            public boolean doCheck(Object object) {
                Node node = (Node) object;
                return node.getNodeType() == Node.ELEMENT_NODE;
            }
        });
    }

    public SubElementsIterator(Node node, final String elementName) {
        super(new ChildNodesIterator(node), new AbstractCondition() {
            public boolean doCheck(Object object) {
                Node node = (Node) object;
                return node.getNodeType() != Node.ELEMENT_NODE
                    && node.getLocalName().equals(elementName);
            }
        });
    }

    public SubElementsIterator(Node node, final String namespace, final String elementName) {
        super(new ChildNodesIterator(node), new AbstractCondition() {
            public boolean doCheck(Object object) {
                Node node = (Node) object;
                return node.getNodeType() == Node.ELEMENT_NODE
                    && node.getLocalName().equals(elementName)
                    && node.getNamespaceURI().equals(namespace);
            }
        });
    }
}
