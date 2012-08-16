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

import java.util.Comparator;

import org.springframework.util.Assert;


/**
 * Comparator that adapts Comparables to the Comparator interface.
 * Mainly for internal use in other Comparators, when supposed
 * to work on Comparables.
 * 
 * <p><strong>Note</strong>: Copied from Spring 2.5 to avoid redoing work using this.
 * 
 * @author Keith Donald
 * @since 1.2.2
 * @see Comparable
 */
public class ComparableComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        Assert.isTrue(o1 instanceof Comparable, "The first object provided is not Comparable");
        Assert.isTrue(o2 instanceof Comparable, "The second object provided is not Comparable");
        return ((Comparable) o1).compareTo(o2);
    }

}
