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

package org.springmodules.validation.valang.javascript.taglib;

import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;

import org.springframework.util.Assert;
import org.springframework.validation.Validator;
import org.springmodules.validation.valang.ValangValidator;
import org.springmodules.validation.web.servlet.mvc.BaseCommandController;

/**
 * Static helper methods that place a <code>ValangValidator</code> into a model so
 * that is is accessible to the JSP custom tag {@link ValangValidateTag}.
 *
 * @author Oliver Hutchison
 * @author Uri Boness
 */
public abstract class ValangJavaScriptTagUtils {

    /**
     * Inserts the valang validator from the provided controller into
     * the model using the controller's name as the validation rule's key.
     *
     * @param controller the controller that will provide the command name
     * and validator
     * @param model the model into which the validation rules will be placed
     * @throws IllegalArgumentException if the controller does not specify a
     * command name
     * @throws IllegalArgumentException if the controller's validator is not
     * an instance of {@link org.springmodules.validation.valang.ValangValidator}
     */
    public static void addValangRulesToModel(BaseCommandController controller, Map model) {
        Assert.hasText(controller.getCommandName(), "controller must define a command name");
        Validator validator = controller.getValidator();
        Assert.isInstanceOf(ValangValidator.class, validator, "controller's validator of class '"
            + (validator != null ? validator.getClass().getName() : "[null]")
            + "' must be an instance of 'ValangValidator'");
        addValangRulesToModel(controller.getCommandName(), (ValangValidator) validator, model);
    }

    /**
     * Inserts the provided validator into the model using the provided command
     * name as the validation rule's key. If there some rules that are already associated with the given command, the
     * new rules will be added to them.
     *
     * @param commandName the command name
     * @param validator the valang validator
     * @param model the model into which the validation rules will be placed
     */
    public static void addValangRulesToModel(String commandName, ValangValidator validator, Map model) {
        Assert.notNull(commandName, "commandName is required.");
        Assert.notNull(validator, "validator is required.");
        String key = ValangValidateTag.VALANG_RULES_KEY_PREFIX + commandName;
        Collection rules = (Collection)model.get(key);
        if (rules == null) {
            rules = new ArrayList();
        }
        rules.addAll(validator.getRules());
        model.put(key, rules);
    }
}