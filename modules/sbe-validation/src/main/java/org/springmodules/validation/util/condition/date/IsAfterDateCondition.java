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
 * An {@link AbstractDateCondition} implementation that checks whether the checked calendar is chronologically after
 * a specific calendar.
 *
 * @author Uri Boness
 */
public class IsAfterDateCondition extends AbstractDateCondition {

    private Calendar earlier;

    /**
     * Constructs a new IsAfterDateCondition with a given date to be compared with the checked calendar.
     *
     * @param earlier The date to be compared with the checked calendar.
     */
    public IsAfterDateCondition(Date earlier) {
        Assert.notNull(earlier, "IsAfterDateCondition cannot be initialized with a null date");
        this.earlier = Calendar.getInstance();
        this.earlier.setTime(earlier);
    }

    /**
     * Constructs a new IsAfterDateCondition with a given calendar to be compared with the checked calendar.
     *
     * @param earlier The calendar to be compared with the checked calendar.
     */
    public IsAfterDateCondition(Calendar earlier) {
        Assert.notNull(earlier, "IsAfterDateCondition cannot be initialized with a null calendar");
        this.earlier = earlier;
    }

    /**
     * Checks whether the given calendar is chornologically after the calendar associated with this condition.
     *
     * @param calendar The calendar to be checked.
     * @return <code>true</code> if the given calender comes after the calendar associated with this condition,
     *         <code>false</code> otherwise.
     */
    protected boolean checkCalendar(Calendar calendar) {
        return earlier.getTimeInMillis() < calendar.getTimeInMillis();
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the calendar associated with this condition.
     *
     * @return The calendar associated with this condition.
     */
    public Calendar getEarlier() {
        return earlier;
    }

}
