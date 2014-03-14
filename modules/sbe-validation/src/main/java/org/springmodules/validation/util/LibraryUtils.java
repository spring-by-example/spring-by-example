/*
 * Copyright 2004-2014 the original author or authors.
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
package org.springmodules.validation.util;

import org.springframework.util.ClassUtils;

/**
 * @author Uri Boness
 */
public class LibraryUtils {

    /**
     * Determines whether Joda-Time can be found in the classpath.
     */
    public final static boolean JODA_TIME_IN_CLASSPATH = isClassInClasspath("org.joda.time.Instant");

    /**
     * Returns whether the given class can be found in the classpath.
     *
     * @param className The fully qualified name of the class to search for.
     * @return <code>true</code> if the given class is in the classpath, <code>false</code> otherwise.
     */
    public static boolean isClassInClasspath(String className) {
        try {
            return ClassUtils.forName(className, ClassUtils.getDefaultClassLoader()) != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Constructor
     */
    private LibraryUtils() {}

}
