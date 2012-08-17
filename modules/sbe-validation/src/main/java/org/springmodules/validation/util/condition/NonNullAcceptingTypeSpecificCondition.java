package org.springmodules.validation.util.condition;

import org.springframework.util.Assert;

/**
 * A base class to all type specific conditions that cannot checkCalendar <code>null</code> values.
 *
 * @author Uri Boness
 */
public abstract class NonNullAcceptingTypeSpecificCondition extends TypeSpecificCondition {

    /**
     * Constructs a new NonNullAcceptingTypeSpecificCondition with a given support type. Sub-classes should call this
     * constructor if they support only a single object type.
     *
     * @param type The object type this condition supports.
     */
    public NonNullAcceptingTypeSpecificCondition(Class type) {
        super(type);
    }

    /**
     * Constructs a new NonNullAcceptingTypeSpecificCondition with given supported types. Sub-classes should call this
     * constructor if they support more than one object type.
     *
     * @param types The object types supported by this condition.
     */
    public NonNullAcceptingTypeSpecificCondition(Class[] types) {
        super(types);
    }

    /**
     * See {@link TypeSpecificCondition#beforeObjectChecked(Object)}.
     * <p/>
     * Also checks whether the checked object is <code>null</code>.
     *
     * @param object The checked object.
     * @throws IllegalArgumentException if the object is either <code>null</code> or is not of the types supported
     * by this condition.
     */
    protected void beforeObjectChecked(Object object) {
        Assert.notNull(object, getClass().getName() + " cannot checkCalendar 'null' values");
        super.beforeObjectChecked(object);
    }

}
