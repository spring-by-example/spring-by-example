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
package org.springbyexample.service;

import org.springbyexample.service.util.MessageHelper;


/**
 * Abstract service.
 *
 * @author David Winterfeldt
 */
public abstract class AbstractService {

    private final MessageHelper messageHelper;

    public AbstractService(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    /**
     * Gets an i18n message from the <code>MessageSource</code> based on the key.
     */
    protected String getMessage(String key) {
        return messageHelper.getMessage(key);
    }

    /**
     * Gets an i18n message from the <code>MessageSource</code> based on the key and arguments.
     */
    protected String getMessage(String key, Object[] args) {
        return messageHelper.getMessage(key, args);
    }

}
