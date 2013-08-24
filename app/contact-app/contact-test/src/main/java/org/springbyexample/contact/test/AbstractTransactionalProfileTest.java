/*
 * Copyright 2007-2013 the original author or authors.
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
package org.springbyexample.contact.test;

import static org.springbyexample.contact.test.constants.security.SecurityTestConstants.DEFAULT_SECURITY_USER;
import static org.springbyexample.contact.test.constants.security.SecurityTestConstants.DEFAULT_SECURITY_USER_PASSWORD;

import org.springbyexample.mvc.test.AbstractProfileTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


/**
 * Abstract transactional profile test that sets default active profiles
 * and turns on transactional behavior for tests.
 *
 * @author David Winterfeldt
 */
@TransactionConfiguration
@Transactional
public abstract class AbstractTransactionalProfileTest extends AbstractProfileTest {

    /**
     * Set the default user on the security context.
     */
    @Override
    protected void doInit() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(DEFAULT_SECURITY_USER, DEFAULT_SECURITY_USER_PASSWORD));
    }

}
