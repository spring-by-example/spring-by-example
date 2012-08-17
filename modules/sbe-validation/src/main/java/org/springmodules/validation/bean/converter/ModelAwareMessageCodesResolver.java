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

package org.springmodules.validation.bean.converter;

import java.io.Serializable;

import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

/**
 * A {@link MessageCodesResolver} implementation that can be used along with the {@link ModelAwareErrorCodeConverter}.
 * This resolver extracts the basic error code from the model-aware one and uses an internal message code converter to
 * resolve all error codes for the this basic error code. The model-aware one is then added to the top of the error codes
 * list. For example, if the internal messagee code resolver is {@link DefaultMessageCodesResolver}, the model-aware
 * error code is <code>Person.firstName[not.blank]</code>, and the basic error code is <code>not.blank</code>,
 * then the resolved error codes will be:
 * <ul>
 * <li>Person.firstName[not.blank]</li>
 * <li>not.blank.person.firstName</li>
 * <li>not.blank.firstName</li>
 * <li>not.blank.java.lang.String</li>
 * <li>not.blank</li>
 * </ul>
 *
 * @author Uri Boness
 */
public class ModelAwareMessageCodesResolver implements MessageCodesResolver, Serializable {

    private MessageCodesResolver internalResolver;

    /**
     * Constructs a new ModelAwareErrorCodeConverter with {@link DefaultMessageCodesResolver} as the internal resolver.
     */
    public ModelAwareMessageCodesResolver() {
        this(new DefaultMessageCodesResolver());
    }

    /**
     * Constructs a new ModelAwareErrorCodeConverter with a given internal message codes resolver.
     *
     * @param internalResolver The internal message codes resolver to be used.
     */
    public ModelAwareMessageCodesResolver(MessageCodesResolver internalResolver) {
        this.internalResolver = internalResolver;
    }

    /**
     * @see MessageCodesResolver#resolveMessageCodes(String, String)
     */
    public String[] resolveMessageCodes(String errorCode, String objectName) {
        int prefixIndex = errorCode.lastIndexOf(ModelAwareErrorCodeConverter.ERROR_CODE_SEPERATOR_PREFIX);
        int suffixIndex = errorCode.lastIndexOf(ModelAwareErrorCodeConverter.ERROR_CODE_SEPERATOR_SUFFIX);

        if (prefixIndex < 0 || suffixIndex < 0 || suffixIndex != errorCode.length() - 1) {
            return internalResolver.resolveMessageCodes(errorCode, objectName);
        }

        String basicCode = errorCode.substring(prefixIndex + 1, suffixIndex);
        String[] basicCodes = internalResolver.resolveMessageCodes(basicCode, objectName);
        String[] codes = new String[basicCodes.length + 1];
        System.arraycopy(basicCodes, 0, codes, 1, basicCodes.length);
        codes[0] = errorCode;

        return codes;
    }

    /**
     * @see MessageCodesResolver#resolveMessageCodes(String, String, String, Class)
     */
    public String[] resolveMessageCodes(String errorCode, String objectName, String field, Class fieldType) {
        int prefixIndex = errorCode.lastIndexOf(ModelAwareErrorCodeConverter.ERROR_CODE_SEPERATOR_PREFIX);
        int suffixIndex = errorCode.lastIndexOf(ModelAwareErrorCodeConverter.ERROR_CODE_SEPERATOR_SUFFIX);

        if (prefixIndex < 0 || suffixIndex < 0 || suffixIndex != errorCode.length() - 1) {
            return internalResolver.resolveMessageCodes(errorCode, objectName, field, fieldType);
        }

        String basicCode = errorCode.substring(prefixIndex + 1, suffixIndex);
        String[] basicCodes = internalResolver.resolveMessageCodes(basicCode, objectName, field, fieldType);
        String[] codes = new String[basicCodes.length + 1];
        System.arraycopy(basicCodes, 0, codes, 1, basicCodes.length);
        codes[0] = errorCode;

        return codes;
    }
}
