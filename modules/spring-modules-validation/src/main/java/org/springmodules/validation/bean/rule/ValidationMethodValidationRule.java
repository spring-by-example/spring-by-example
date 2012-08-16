package org.springmodules.validation.bean.rule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springmodules.validation.util.condition.AbstractCondition;
import org.springmodules.validation.util.condition.Condition;
import org.springmodules.validation.util.condition.ConditionException;

/**
 * @author Uri Boness
 */
public class ValidationMethodValidationRule extends AbstractValidationRule {

    private MethodCondition condition;

    public ValidationMethodValidationRule(Method method) {
        super(method.getName() + "()");
        condition = new MethodCondition(method);
    }

    public Condition getCondition() {
        return condition;
    }


    private class MethodCondition extends AbstractCondition {

        private Method method;

        public MethodCondition(Method method) {
            this.method = method;
        }

        public boolean doCheck(Object object) {
            try {
                boolean originalAccessiblity = method.isAccessible();
                method.setAccessible(true);
                Boolean valid = (Boolean) method.invoke(object, new Object[0]);
                method.setAccessible(originalAccessiblity);
                return valid.booleanValue();
            } catch (IllegalAccessException iae) {
                throw new ConditionException("Could not validate object using validation method '" +
                    method.getName() + "'", iae);
            } catch (InvocationTargetException ite) {
                throw new ConditionException("Could not validate object using validation method '" +
                    method.getName() + "'", ite);
            }
        }
    }

}
