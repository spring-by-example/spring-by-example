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
 * Represents a instantCondition that can be checked against an object.
 *
 * @author Uri Boness
 */
public interface Condition {

    /**
     * Returns whether the given object adheres to this condition.
     *
     * @param object The checked object.
     * @return <code>true</code> if the object adheres to this condition, <code>false</code> otherwise.
     */
    boolean check(Object object);

    /**
     * Creates and returns a new condition that represents the logical AND of this condition and the given one.
     *
     * @param condition The condition to intersect with this condition.
     * @return A new condition that represents the logical AND of this condition and the given one.
     */
    Condition and(Condition condition);

    /**
     * Creates and returns a new condition that represents the logical OR of this condition and the given one.
     *
     * @param condition The condition to unite with this condition.
     * @return A new condition that represents the logical OR of this condition and the given one.
     */
    Condition or(Condition condition);

}
