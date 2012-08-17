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

package org.springmodules.validation.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.springmodules.validation.util.condition.Condition;

/**
 * An wrapper iterator that filters out items from the underlying iterator based on a condition.
 *
 * @author Uri Boness
 */
public class FilteredIterator extends ReadOnlyIterator {

    private Condition condition;

    private Iterator iterator;

    private boolean initialized;

    private boolean finished;

    private Object nextElement;

    /**
     * Constructs a new FilteredIterator with given iterator to wrap and a condition. The constructed iterator will
     * return only the element that adhere to the given condition.
     *
     * @param iterator The iterator to wrap.
     * @param condition The condition that determines what elements should be returned by this iterator.
     */
    public FilteredIterator(Iterator iterator, Condition condition) {
        this.iterator = iterator;
        this.condition = condition;
    }

    public boolean hasNext() {
        initializeIfNeeded();
        return !finished;
    }

    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Object result = nextElement;
        advanceIterator();
        return result;
    }

    //=============================================== Helper Methods ===================================================

    protected void initializeIfNeeded() {
        if (!initialized) {
            advanceIterator();
            initialized = true;
        }
    }

    protected void advanceIterator() {
        boolean assigned = false;
        while (iterator.hasNext()) {
            Object element = iterator.next();
            if (condition.check(element)) {
                nextElement = element;
                assigned = true;
                break;
            }
        }
        finished = !assigned;
    }

}
