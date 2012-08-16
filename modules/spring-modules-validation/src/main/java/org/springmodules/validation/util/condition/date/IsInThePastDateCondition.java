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

/**
 * An {@link org.springmodules.validation.util.condition.date.AbstractDateCondition} implementation that checks whether the
 * checked date is in the past.
 *
 * @author Uri Boness
 */
public class IsInThePastDateCondition extends AbstractDateCondition {

    /**
     * Checks whether the given calendar is in the past.
     *
     * @param calendar The checked calendar.
     * @return <code>true</code> if the given calendar is in the past, <code>false</code> otherwise.
     */
    protected boolean checkCalendar(Calendar calendar) {
        return calendar.getTimeInMillis() < System.currentTimeMillis();
    }

}
