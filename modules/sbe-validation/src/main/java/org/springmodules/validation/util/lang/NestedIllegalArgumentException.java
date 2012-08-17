package org.springmodules.validation.util.lang;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Uri Boness
 */
public class NestedIllegalArgumentException extends NestedRuntimeException {

    /**
     * Construct a <code>NestedRuntimeException</code> with the specified detail message.
     *
     * @param msg the detail message
     */
    public NestedIllegalArgumentException(String msg) {
        super(msg);
    }

    /**
     * Construct a <code>NestedRuntimeException</code> with the specified detail message
     * and nested exception.
     *
     * @param msg the detail message
     * @param cause the nested exception
     */
    public NestedIllegalArgumentException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
