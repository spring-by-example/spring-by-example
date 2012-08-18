/*
 * Copyright 2007-2012 the original author or authors.
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

package org.springbyexample.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.webflow.core.FlowException;
import org.springframework.webflow.execution.FlowExecutionOutcome;
import org.springframework.webflow.execution.repository.NoSuchFlowExecutionException;
import org.springframework.webflow.mvc.servlet.AbstractFlowHandler;

/**
 * Person flow handler.
 * 
 * @author David Winterfeldt
 */
@Component
public class PersonFlowHandler extends AbstractFlowHandler {

    /**
     * Where the flow should go when it ends.
     */
    @Override
    public String handleExecutionOutcome(FlowExecutionOutcome outcome,
                                         HttpServletRequest request, HttpServletResponse response) {
        return getContextRelativeUrl(PersonController.SEARCH_VIEW_PATH_KEY);
    }

    /**
     * Where to redirect if there is an exception not handled by the flow.
     */
    @Override
    public String handleException(FlowException e, 
                                  HttpServletRequest request, HttpServletResponse response) {
        if (e instanceof NoSuchFlowExecutionException) {
            return getContextRelativeUrl(PersonController.SEARCH_VIEW_PATH_KEY);
        } else {
            throw e;
        }
    }
    
    /**
     * Gets context relative url with an '.html' extension.
     */
    private String getContextRelativeUrl(String view) {
        return "contextRelative:" + view + ".html";
    }

}
