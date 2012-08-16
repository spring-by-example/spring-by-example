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

package org.springmodules.validation.util.condition;

/**
 * An abstract implementation of the {@link Condition} interface. This class takes care of the
 * logical operation of the condition. It is highly recommended for condition implementation to
 * sub-class this class when possible.
 *
 * @author Uri Boness
 */
public abstract class AbstractCondition implements Condition {

    /**
     * See {@link Condition#check(Object)}.
     * <p/>
     * Delegates to the {@link #doCheck(Object)}. Provides callback methods for sub-classes to intercept
     * the checking.
     */
    public final boolean check(Object object) {
        beforeObjectChecked(object);
        boolean result = doCheck(object);
        return afterObjectChecked(object, result);
    }

    /**
     * Performs the actual checking of this condition on the checked object.
     *
     * @param object The object to be checked.
     * @return <code>true</code> if the given object adheres to this condition, <code>false</code> otherwise.
     */
    public abstract boolean doCheck(Object object);

    /**
     * See {@link Condition#and(Condition)}
     */
    public Condition and(Condition condition) {
        return Conditions.and(this, condition);
    }

    /**
     * See {@link Condition#or(Condition)}
     */
    public Condition or(Condition condition) {
        return Conditions.or(this, condition);
    }

    /**
     * A callback method that enables sub-classes intercept the object checking before it is
     * being checked. Sub-classes may override this method and perform custom assertions and prevent the
     * checking by throwing an {@link IllegalArgumentException};
     *
     * @param object
     */
    protected void beforeObjectChecked(Object object) throws IllegalArgumentException {
    }

    /**
     * A callback method that enables sub-classes to intercept the object checking after it was checked. Sub-classes
     * can override this method and change the check result. By default, this method returns the original check result.
     *
     * @param object The checked object.
     * @param originalResult The original check result as returned by the specific condition implementation.
     * @return The final check result.
     */
    protected boolean afterObjectChecked(Object object, boolean originalResult) {
        return originalResult;
    }
}
