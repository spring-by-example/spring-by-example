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

/**
 * A strategy to convert one error code to another.
 *
 * @author Uri Boness
 */
public interface ErrorCodeConverter {

    /**
     * Converts the given global error code associated with the given class to another error code.
     *
     * @param errorCode The global error code to convert.
     * @param clazz The class that the given error code is associated with.
     * @return The converted error code.
     */
    String convertGlobalErrorCode(String errorCode, Class clazz);

    /**
     * Converts the given property error code that is associated with the given class and property to another
     * error code.
     *
     * @param errorCode The property error code to convert.
     * @param clazz The class that is associated with the given error code.
     * @param propertyName The property that is associated with the given error code.
     * @return The converted error code.
     */
    String convertPropertyErrorCode(String errorCode, Class clazz, String propertyName);

}
