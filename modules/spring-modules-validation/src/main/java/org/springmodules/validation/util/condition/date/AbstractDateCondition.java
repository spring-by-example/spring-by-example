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

package org.springmodules.validation.util.condition.date;

import java.util.Calendar;
import java.util.Date;

import org.springmodules.validation.util.condition.NonNullAcceptingTypeSpecificCondition;

/**
 * A base class for all date-time related conditions.
 *
 * @author Uri Boness
 */
public abstract class AbstractDateCondition extends NonNullAcceptingTypeSpecificCondition {

    protected AbstractDateCondition() {
        super(new Class[]{Date.class, Calendar.class});
    }

    /**
     * See {@link org.springmodules.validation.util.condition.Condition#check(Object)}.
     * <p/>
     * This method casts or converts the given object to a {@link Calendar} instance and
     * delegates the call to {@link #checkCalendar(java.util.Calendar)}.
     *
     * @throws IllegalArgumentException when the passed in object is either <code>null</code> or of a type other than
     * {@link Calendar} or {@link Date}.
     */
    public final boolean doCheck(Object object) {

        if (object instanceof Calendar) {
            return checkCalendar((Calendar) object);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) object);

        return checkCalendar(calendar);
    }

    /**
     * Checks this condition on the given calendar.
     *
     * @param calendar The calendar to be checks.
     * @return <code>true</code> if checkCalendar passed, <code>false</code> if the checkCalendar failed.
     */
    protected abstract boolean checkCalendar(Calendar calendar);

}
