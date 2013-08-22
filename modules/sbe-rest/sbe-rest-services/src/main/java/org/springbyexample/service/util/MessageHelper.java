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
package org.springbyexample.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;


/**
 * Message helper for creating i18n messages based on keys.
 * 
 * @author David Winterfeldt
 */
@Component
public class MessageHelper {

    private final MessageSource messageSource;
    
    @Autowired
    public MessageHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Gets an i18n message from the <code>MessageSource</code> based on the key.
     */
    public String getMessage(String key) {
        return getMessage(key, null);
    }

    /**
     * Gets an i18n message from the <code>MessageSource</code> based on the key and arguments.
     */
    public String getMessage(String key, Object[] args) {
        String result = null;

        result = messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        
        return result;
    }

}
