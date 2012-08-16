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

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * An {@link AbstractCollectionCondition} that checks whether the checked collection or array is empty.
 *
 * @author Uri Boness
 */
public class IsEmptyCollectionCondition extends AbstractCollectionCondition {

    /**
     * Checks whether the given array is empty.
     *
     * @param array The array to be checked.
     * @return <code>true</code> if the given array is empty, <code>false</code> otherwise.
     */
    protected boolean checkArray(Object array) {
        return Array.getLength(array) == 0;
    }

    /**
     * Checks whether the given collection is empty.
     *
     * @param collection The collection to be checked.
     * @return <code>true</code> if the given collection is empty, <code>false</code> otherwise.
     */
    protected boolean checkCollection(Collection collection) {
        return collection.isEmpty();
    }

}
