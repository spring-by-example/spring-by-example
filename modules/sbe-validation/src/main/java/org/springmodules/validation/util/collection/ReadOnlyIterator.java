package org.springmodules.validation.util.collection;

import java.util.Iterator;

/**
 * @author Uri Boness
 */
public abstract class ReadOnlyIterator implements Iterator {

    /**
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException("A read-only iterator cannot remove elements");
    }

}
