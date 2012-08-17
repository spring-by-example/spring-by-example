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

package org.springmodules.validation.util.condition.date.jodatime;

import org.joda.time.ReadableInstant;
import org.springmodules.validation.util.condition.NonNullAcceptingTypeSpecificCondition;
import org.springmodules.validation.util.condition.TypeSpecificCondition;

/**
 * A base class for all joda-time date related conditions.
 *
 * @author Uri Boness
 */
public abstract class AbstractInstantCondition extends NonNullAcceptingTypeSpecificCondition {

    /**
     * Constructs a new AbstractInstantCondition.
     */
    protected AbstractInstantCondition() {
        super(ReadableInstant.class);
    }

    /**
     * See {@link TypeSpecificCondition#doCheck(Object)}.
     * <p/>
     * Casts the given object to a {@link ReadableInstant} and delegates the call to
     * {@link #checkInstant(org.joda.time.ReadableInstant)}.
     */
    public final boolean doCheck(Object object) {
        return checkInstant((ReadableInstant) object);
    }

    protected abstract boolean checkInstant(ReadableInstant instant);

}
