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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A servlet filter that sets the validation context for different url patterns.
 * <p/>
 * This filter is configured with mappings of url pattern to a list of validation context tokens. Based on this
 * configuration, the appropriate {@link org.springmodules.validation.bean.context.DefaultValidationContext} is constructed and is set on the
 * {@link org.springmodules.validation.bean.context.ValidationContextHolder}.
 *  *
 * @author Uri Boness
 */
public class ValidationContextFilter extends OncePerRequestFilter {

    private ValidationContextHandlerInterceptor interceptor;

    public ValidationContextFilter() {
        interceptor = new ValidationContextHandlerInterceptor();
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            if (interceptor.preHandle(request, response, null)) {
                filterChain.doFilter(request, response);
            }

        } catch (Exception e) {
            throw new ServletException("Could not apply valication context filter", e);
        } finally{
            try {
                interceptor.postHandle(request, response, null, null);
            } catch (Exception e) {
                // do nothing
            }
        }
    }



    //============================================== Setter/Getter =====================================================

    public void setValidationContextUrlMappings(ValidationContextUrlMapping[] validationContextUrlMappings) {
        interceptor.setValidationContextUrlMappings(validationContextUrlMappings);
    }

    public ValidationContextUrlMapping[] getValidationContextUrlMappings() {
        return interceptor.getValidationContextUrlMappings();
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        interceptor.setPathMatcher(pathMatcher);
    }

    public PathMatcher getPathMatcher() {
        return interceptor.getPathMatcher();
    }

}
