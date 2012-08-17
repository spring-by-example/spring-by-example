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

package org.springmodules.validation.valang.functions;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * A function that accepts a one string argument that indicates a security role. This function
 * returns a <code>true</code> if the current user is in the passed in role, and <code>false</code>
 * otherwise.
 * <br/><br/>
 * This method uses Acegi's <code>SecurityContextHolder.getContext().getAuthentication()</code> to
 * get the current user.
 * <br/><br/>
 * This function may be used to apply different validation rules based on the logged in user roles.
 *
 * @author Uri Boness
 * @since May 25, 2006
 */
public class InRoleFunction extends AbstractFunction {

    public InRoleFunction(Function[] arguments, int line, int column) {
        super(arguments, line, column);
        definedExactNumberOfArguments(1);
    }

    protected Object doGetResult(Object target) {

        Object role = getArguments()[0].getResult(target);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Boolean.FALSE;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority.equals(role)) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

}
