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

import java.util.NoSuchElementException;

import org.springmodules.validation.util.collection.ReadOnlyIterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Iterates over the direct child nodes of the given node.
 *
 * @author Uri Boness
 */
public class ChildNodesIterator extends ReadOnlyIterator {

    private NodeList nodes;

    private int length;

    private int index;

    public ChildNodesIterator(Node node) {
        nodes = node.getChildNodes();
        length = nodes.getLength();
        index = 0;
    }

    public boolean hasNext() {
        return index < length;
    }

    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Object element = nodes.item(index);
        index++;
        return element;
    }

}
