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

package org.springmodules.validation.util.condition.adapter;

import org.apache.commons.collections.Predicate;
import org.springframework.util.Assert;
import org.springmodules.validation.util.condition.AbstractCondition;

/**
 * A Jakarta Commmons Predicate condition adapter. This condition deligates the call to an associated {@link Predicate).
 *
 * @author Uri Boness
 */
public class CommonsPredicateCondition extends AbstractCondition {

    private Predicate predicate;

    /**
     * Constructs a new CommonsPredicateCondition with a given predicate.
     *
     * @param predicate The given predicate.
     */
    public CommonsPredicateCondition(Predicate predicate) {
        Assert.notNull(predicate, "Predicate cannot be null");
        this.predicate = predicate;
    }

    /**
     * Checks the given object against the predicate associated with this condition.
     *
     * @param object The object to be checked.
     * @return The result returned by the associated predicate when evaluting the given object.
     *         See {@link Predicate#evaluate(Object)}.
     */
    public boolean doCheck(Object object) {
        return predicate.evaluate(object);
    }

    //============================================= Setter/Getter ===================================================

    /**
     * Returns the predicate associated with this condition.
     *
     * @return The predicate associated with this condition.
     */
    public Predicate getPredicate() {
        return predicate;
    }

}
