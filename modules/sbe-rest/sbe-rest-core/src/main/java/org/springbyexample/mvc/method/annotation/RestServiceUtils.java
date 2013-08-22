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
package org.springbyexample.mvc.method.annotation;

import org.springframework.util.ClassUtils;


/**
 * REST service utilities.
 * 
 * @author David Winterfeldt
 */
public class RestServiceUtils {

    private RestServiceUtils() {}

    /**
     * Checks whether or not the class name is valid.
     */
    public static boolean isRestServiceInterface(String interfaceName) {
        Class<?> clazz = null;
        
        try {
            clazz = ClassUtils.forName(interfaceName, ClassUtils.getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading interface class.", e);
        } catch (LinkageError e) {
            throw new RuntimeException("Error loading interface class.", e);
        }
        
        return isRestServiceInterface(clazz);
    }

    /**
     * Checks whether or not the class is valid.
     */
    public static boolean isRestServiceInterface(Class<?> clazz) {
        return (clazz.isInterface());
//        return !(PersistenceMarshallingService.class.equals(clazz)) && ClassUtils.isAssignable(PersistenceMarshallingService.class, clazz);
    }
    
}

