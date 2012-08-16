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

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;

/**
 * An {@link AbstractInstantCondition} implementation that checks whether the checked instant comes chronologically
 * after another instant.
 *
 * @author Uri Boness
 */
public class IsAfterInstantCondition extends AbstractInstantCondition {

    private ReadableInstant earlier;

    /**
     * Constructs a new IsAfterDateCondition with a given instant to be checked against.
     *
     * @param earlier The instant that this instantCondition will checkCalendar against.
     */
    public IsAfterInstantCondition(ReadableInstant earlier) {
        this.earlier = earlier;
    }

    /**
     * Constructs a new IsAfterDateCondition with a given date to be checked against.
     *
     * @param earlier The date that this instantCondition will checkCalendar against.
     */
    public IsAfterInstantCondition(Date earlier) {
        this.earlier = new DateTime(earlier.getTime());
    }

    /**
     * Constructs a new IsAfterDateCondition with a given calendar to be checked against.
     *
     * @param earlier The calendar that this instantCondition will checkCalendar against.
     */
    public IsAfterInstantCondition(Calendar earlier) {
        this.earlier = new DateTime(earlier.getTimeInMillis(), DateTimeZone.forTimeZone(earlier.getTimeZone()));
    }

    /**
     * Checks whether the given instant comes chronologically after the instant associated with this instantCondition.
     *
     * @param instant The instance to be checked.
     * @return <code>true</code> if the given instant comes after the instant associated with this instantCondition,
     *         <code>false</code> otherwise.
     */
    protected boolean checkInstant(ReadableInstant instant) {
        return earlier.isBefore(instant);
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Returns the instant associated with this condition.
     *
     * @return The instant associated with this condition.
     */
    public ReadableInstant getEarlier() {
        return earlier;
    }

}
