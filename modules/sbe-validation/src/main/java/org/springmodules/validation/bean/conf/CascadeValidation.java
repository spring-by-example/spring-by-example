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

package org.springmodules.validation.bean.conf;

import org.springframework.util.ObjectUtils;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.common.AlwaysTrueCondition;

/**
 * Represents a cascade validation definition. Holds the name
 * of the property to which the validation will be cascaded and
 * the applicability condition to determine whether the cascading
 * should be applied.
 *
 * @author Uri Boness
 */
public class CascadeValidation {

    private String propertyName;

    private Condition applicabilityCondition;

    /**
     * Constructs a new CascadeValidation with a given property name. This cascading always applies.
     *
     * @param propertyName The name of the property to which the validation will be cascaded.
     */
    public CascadeValidation(String propertyName) {
        this(propertyName, new AlwaysTrueCondition());
    }

    /**
     * Constructs a new CascadeValidation with a given property name and applicability condition.
     *
     * @param propertyName The name of the property to which the validation will be cascaded.
     * @param applicabilityCondition The condition to determine whether the cascading will be applied.
     */
    public CascadeValidation(String propertyName, Condition applicabilityCondition) {
        this.propertyName = propertyName;
        this.applicabilityCondition = applicabilityCondition;
    }

    /**
     * Returns the name of the property to which the validation will be cascaded.
     *
     * @return The name of the property to which the validation will be cascaded.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns the condition that determines whether the cascading should be applied.
     *
     * @return The condition that determines whether the cascading should be applied.
     */
    public Condition getApplicabilityCondition() {
        return applicabilityCondition;
    }

    /**
     * Sets the condition that determines whether the cascading should be applied.
     *
     * @param applicabilityCondition The condition that determines whether the cascading should be applied.
     */
    public void setApplicabilityCondition(Condition applicabilityCondition) {
        this.applicabilityCondition = applicabilityCondition;
    }


    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        CascadeValidation that = (CascadeValidation) object;

        if (!propertyName.equals(that.propertyName)) {
            return false;
        }

        return (ObjectUtils.nullSafeEquals(applicabilityCondition, that.applicabilityCondition));
    }

    public int hashCode() {
        int result;
        result = propertyName.hashCode();
        result = 29 * result + (applicabilityCondition != null ? applicabilityCondition.hashCode() : 0);
        return result;
    }
}
