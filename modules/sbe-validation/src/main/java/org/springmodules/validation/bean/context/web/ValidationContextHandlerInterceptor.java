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

package org.springmodules.validation.bean.context.web;

import java.beans.PropertyEditorManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springmodules.validation.bean.context.ValidationContextUtils;

/**
 * An interceptor that sets the validation context based on configured mappings of url patterns to validation context
 * tokens. This interceptor is the Spring MVC counterpart of the {@link ValidationContextFilter}.
 *
 * @author Uri Boness
 */
public class ValidationContextHandlerInterceptor extends HandlerInterceptorAdapter {

    private final static Logger logger = LoggerFactory.getLogger(ValidationContextHandlerInterceptor.class);

    static {
        PropertyEditorManager.registerEditor(ValidationContextUrlMapping[].class,
                ValidationContextUrlMappingArrayPropertyEditor.class);
    }

    private ValidationContextUrlMapping[] validationContextUrlMappings;

    private PathMatcher pathMatcher;

    public ValidationContextHandlerInterceptor() {
        validationContextUrlMappings = new ValidationContextUrlMapping[0];
        pathMatcher = new AntPathMatcher();
    }

    /**
     * Creates and sets the validation context based on the request URI. The validation context tokens are determined
     * by the configured {@link ValidationContextUrlMapping}'s.
     *
     * @see HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, Object)
     * @see #setValidationContextUrlMappings(ValidationContextUrlMapping[])
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String[] contextTokens = new String[0];

        for (int i=0; i<validationContextUrlMappings.length; i++) {
            String pattern = validationContextUrlMappings[i].getUrlPattern();
            if (pathMatcher.match(pattern, request.getRequestURI())) {
                contextTokens = validationContextUrlMappings[i].getContextTokens();
                break;
            }
        }

        if (logger.isInfoEnabled() && contextTokens.length == 0) {
            logger.info("No validation context url mapping matches url '" + request.getRequestURI() +
                    "'. Setting validation context without supported tokens...");
        }

        ValidationContextUtils.setContext(contextTokens);


        return true;
    }

    /**
     * Clears
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ValidationContextUtils.clearContext();
    }


    //============================================== Setter/Getter =====================================================

    public ValidationContextUrlMapping[] getValidationContextUrlMappings() {
        return validationContextUrlMappings;
    }

    public void setValidationContextUrlMappings(ValidationContextUrlMapping[] validationContextUrlMappings) {
        this.validationContextUrlMappings = validationContextUrlMappings;
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

}
