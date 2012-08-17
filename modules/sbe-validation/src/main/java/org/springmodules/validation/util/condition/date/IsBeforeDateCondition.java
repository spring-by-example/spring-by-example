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

import org.springframework.util.Assert;

/**
 * An {@link org.springmodules.validation.util.condition.date.AbstractDateCondition} implementation that checks
 * whether the checked calendar is chronologically before a specific calendar.
 *
 * @author Uri Boness
 */
public class IsBeforeDateCondition extends AbstractDateCondition {

    private Calendar later;

    /**
     * Constructs a new IsBeforeDateCondition with a given date to be compared with the checked calendar.
     *
     * @param later The date to be compared with the checked calendar.
     */
    public IsBeforeDateCondition(Date later) {
        Assert.notNull(later, "IsBeforeDateCondition cannot be initialized with a null date");
        this.later = Calendar.getInstance();
        this.later.setTime(later);
    }

    /**
     * Constructs a new IsBeforeDateCondition with a given calendar to be compared with the checked calendar.
     *
     * @param later The calendar to be compared with the checked calendar.
     */
    public IsBeforeDateCondition(Calendar later) {
        Assert.notNull(later, "IsBeforeDateCondition cannot be initialized with a null calendar");
        this.later = later;
    }

    /**
     * Checks whether the given calendar is chornologically before the calendar associated with this condition.
     *
     * @param calendar The calendar to be checked.
     * @return <code>true</code> if the given calender comes before the calendar associated with this condition,
     *         <code>false</code> otherwise.
     */
    protected boolean checkCalendar(Calendar calendar) {
        return later.getTimeInMillis() > calendar.getTimeInMillis();
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the calendar associated with this condition.
     *
     * @return The calendar associated with this condition.
     */
    public Calendar getLater() {
        return later;
    }

}
