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

import org.springframework.util.ClassUtils;

/**
 * Converts simple error codes to an error code that expresses the class and perhaps
 * the property that are associated with the error code.
 *
 * @author Uri Boness
 */
public class ModelAwareErrorCodeConverter implements ErrorCodeConverter {

    final static String ERROR_CODE_SEPERATOR_PREFIX = "[";

    final static String ERROR_CODE_SEPERATOR_SUFFIX = "]";

    final static String PROPERTY_SEPERATOR = ".";

    private boolean useFullyQualifiedClassName;

    /**
     * Constructs a new ModelAwareErrorCodeConverter that uses class simple names. For example, the validation error code
     * <code>error_code</code> for class <code>org.springmodules.validation.sample.Person</code> will be converted
     * to <code>Person[error_code]</code>.
     */
    public ModelAwareErrorCodeConverter() {
        this(false);
    }

    /**
     * Constructs a new ModelAwareErrorCodeConverter. The given argument idicateds whether a fully qualified name should
     * be used for the converted error codes. For example, if the validation error code <code>error_code</code> for
     * class <code>org.springmodules.validation.sample.Person</code> will be converted with fully qualified name set
     * to <code>true</code>, the converted error code will be
     * <code>org.springmodules.validation.sample.Person[error_code]</code>.
     */
    public ModelAwareErrorCodeConverter(boolean useFullyQualifiedClassName) {
        this.useFullyQualifiedClassName = useFullyQualifiedClassName;
    }

    /**
     * Converts the given error code to the following format:<br/> <code>short_class_name[errorCode]</code></br>
     * where <code>short_class_name</code> is the name of the given class with its package stripped, and
     * <code>error_code</code> is the given error code.
     *
     * @param errorCode The given error code (the one to convert)
     * @param clazz The given class
     * @return The converted error code.
     */
    public String convertGlobalErrorCode(String errorCode, Class clazz) {
        String className = (useFullyQualifiedClassName) ? clazz.getName() : ClassUtils.getShortName(clazz);
        return new StringBuffer(className)
            .append(ERROR_CODE_SEPERATOR_PREFIX)
            .append(errorCode)
            .append(ERROR_CODE_SEPERATOR_SUFFIX)
            .toString();
    }

    /**
     * Converts the given error code to the following format:<br/>
     * <code>short_class_name.property_name[errorCode]</code></br>
     * where <code>short_class_name</code> is the name of the given class with its package stripped,
     * <code>property_name</code> is the given property name, and <code>error_code</code> is the given
     * error code.
     *
     * @param errorCode The given error code (the one to convert)
     * @param clazz The given class
     * @param propertyName The property name
     * @return The converted error code.
     */
    public String convertPropertyErrorCode(String errorCode, Class clazz, String propertyName) {
        String className = (useFullyQualifiedClassName) ? clazz.getName() : ClassUtils.getShortName(clazz);
        return new StringBuffer(className)
            .append(PROPERTY_SEPERATOR)
            .append(propertyName)
            .append(ERROR_CODE_SEPERATOR_PREFIX)
            .append(errorCode)
            .append(ERROR_CODE_SEPERATOR_SUFFIX)
            .toString();
    }

    //=============================================== Setter/Getter ====================================================

    /**
     * Determines whether the converted error codes will use the fully qualified class names of the validated class. If
     * not, the simple class name will be used instead.
     *
     * @param useFullyQualifiedClassName
     */
    public void setUseFullyQualifiedClassName(boolean useFullyQualifiedClassName) {
        this.useFullyQualifiedClassName = useFullyQualifiedClassName;
    }

}
