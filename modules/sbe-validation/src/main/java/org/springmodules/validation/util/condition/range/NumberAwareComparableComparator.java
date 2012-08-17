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

package org.springmodules.validation.util.condition.range;

import java.math.BigDecimal;


/**
 * A comparator that compares {@link Comparable} instances but treats comparison of {@link Number} instances
 * in a special manner. In order to support comparison of numbers regardless of their type, this comparator transform
 * the numbers to {@link BigDecimal} and then does the comparison.
 *
 * @author Uri Boness
 */
public class NumberAwareComparableComparator extends ComparableComparator {

    /**
     * Compares the two given objects. Expects both of them to be {@link Comparable} instances.
     *
     * @see ComparableComparator#compare(Object, Object)
     */
    public int compare(Object o1, Object o2) {
        if (Number.class.isInstance(o1) && Number.class.isInstance(o2)) {
            return compareNumbers((Number) o1, (Number) o2);
        }
        return super.compare(o1, o2);

    }

    /**
     * Compares the two given numbers. These numbers are compared regardless of their type.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return possitive number if <code>n1 &gt; n2</code>, negative number if <code>n1 &lt; n2</code>, or 0 (Zero) if
     *         <code>n1 == n2</code>.
     */
    protected int compareNumbers(Number n1, Number n2) {
        BigDecimal bd1 = new BigDecimal(n1.toString());
        BigDecimal bd2 = new BigDecimal(n2.toString());
        return bd1.compareTo(bd2);
    }

}
