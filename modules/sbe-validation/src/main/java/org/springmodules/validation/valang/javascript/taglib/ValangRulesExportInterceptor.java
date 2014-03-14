/*
 * Copyright 2002-2008 the original author or authors.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springmodules.validation.valang.ValangValidator;
import org.springmodules.validation.web.servlet.mvc.BaseCommandController;

/**
 * Spring MVC interceptor implementation that will automatically export Valang
 * validation rules that are used by any of the intercepted handlers into
 * the the ModelAndView so that they are accessible to the custom tag
 * <code>ValangValidateTag</code>.
 * <p/>
 * <p>Does nothing if the intercepted handler is neither an instance of
 * <code>BaseCommandController</code>, nor a proxied <code>BaseCommandController</code>.
 * </p>
 * <p>This will also do nothing if the handler did not export a command object
 * into the model. Obviously the rules will only be picked up from all validators of type {@link ValangValidator}.
 *
 * @author Oliver Hutchison
 * @author Uri Boness
 * @author Colin Yates
 * 
 * @see ValangValidateTag
 * @see ValangValidator
 */
public class ValangRulesExportInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ValangRulesExportInterceptor.class);

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {

        BaseCommandController controller = retrieveBaseCommandControllerIfPossible(handler);
        if (controller == null) {
        	if (logger.isDebugEnabled()) {
        		logger.debug("Controller is of type " + controller.getClass() + " so ignoring");
        	}
        	return;
        }
        
        Map model = modelAndView.getModel();
        String commandName = controller.getCommandName();
        if (model == null || !model.containsKey(commandName)) {
            if (logger.isWarnEnabled()) {
                logger.debug("Handler '" + handler + "' did not export command object '" + controller.getCommandName()
                    + "'; no rules added to model");
            }
            return;
        }

        Validator[] validators = controller.getValidators();
        for (int i=0; i<validators.length; i++) {
            if (!ValangValidator.class.isInstance(validators[i])) {
                continue;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Adding Valang rules from handler '" + handler + "' to model");
            }

            ValangValidator validator = (ValangValidator)validators[i];
            ValangJavaScriptTagUtils.addValangRulesToModel(commandName, validator,  model);
        }

    }

    /**
     * Convert the specified <code>handler</code> to a {@link BaseCommandController} if possible.
     * 
     * <p>If the <code>handler</code> is type compatible with a <code>BaseCommandController</code>
     * then it will simply be cast.
     * </p>
     * <p>If the <code>handler</code> is a Spring JDK (or CGLIB) proxy, then it will unravelled
     * and returned (assuming the target is a <code>BaseCommandController</code>.
     * </p>If neither of the above strategies work, <code>null</code> will be returned.
     *   
     * @param handler the handler to convert to a <code>BaseCommandController</code>.
     * @return a <code>BaseCommandController</code> or <code>null</code> if <code>handler</code>
     * 		   cannot be converted.
     * @throws Exception 
     */
	private BaseCommandController retrieveBaseCommandControllerIfPossible(Object handler) throws Exception {
		BaseCommandController baseCommandController = null;
		
		if (BaseCommandController.class.isAssignableFrom(handler.getClass())) {
			if (logger.isDebugEnabled()) {
				logger.debug("handler is type compatible, simply cast");
			}
			baseCommandController = (BaseCommandController) handler;
		} else if (AopUtils.isAopProxy(handler)) {
			if (logger.isDebugEnabled()) {
				logger.debug("handler is AOP proxy");
			}
			
			Advised advisedObject = (Advised) handler;
			Class proxiedClass = advisedObject.getTargetClass();
			Object target = advisedObject.getTargetSource().getTarget();
			
			// convert (if possible) the target.
			baseCommandController = retrieveBaseCommandControllerIfPossible(target);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Cannot convert handler to BaseCommandController");
		}
		return baseCommandController;
	}
}
