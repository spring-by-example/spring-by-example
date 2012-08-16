package org.springmodules.validation.util.condition.common;

import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;

/**
 * @author Uri Boness
 */
public class ConditionProxyCondition extends AbstractCondition {

    private Condition internalCondition;

    public ConditionProxyCondition() {
    }

    public ConditionProxyCondition(Condition internalCondition) {
        this.internalCondition = internalCondition;
    }

    public boolean doCheck(Object object) {
        return internalCondition.check(object);
    }

    public Condition getInternalCondition() {
        return internalCondition;
    }

    public void setInternalCondition(Condition internalCondition) {
        this.internalCondition = internalCondition;
    }

}
