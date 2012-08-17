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

package org.springmodules.validation.util.condition.collection;

import org.springframework.util.Assert;
import org.springmodules.validation.util.condition.Condition;

/**
 * A base class for all collection conditions that perform checks on the collection elements.
 *
 * @author Uri Boness
 */
public abstract class AbstractCollectionElementCondition extends AbstractCollectionCondition {

    private Condition elementCondition;

    /**
     * Constructs a new AbstractCollectionElementCondition with a given element elementCondition.
     *
     * @param elementCondition The element elementCondition.
     */
    protected AbstractCollectionElementCondition(Condition elementCondition) {
        Assert.notNull(elementCondition, "Element condition cannot be null");
        this.elementCondition = elementCondition;
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the element condition associated with this elementCondition.
     *
     * @return The element condition associated with this elementCondition.
     */
    public Condition getElementCondition() {
        return elementCondition;
    }

}
