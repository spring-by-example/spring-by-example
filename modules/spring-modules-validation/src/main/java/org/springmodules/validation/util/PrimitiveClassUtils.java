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

package org.springmodules.validation.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Primitive class utilities.</p>
 * 
 * <p><strong>Note</strong>: Derived from <code>org.springframework.util.ClassUtils</code> 
 * and ideally would be added to this class.</p>
 * 
 * @author David Winterfeldt
 */
public class PrimitiveClassUtils {

    /**
     * Map with primitive type name as key and corresponding object
     * type as value, for example: "int" -> "Integer.class".
     */
    private static final Map<Class<?>, Class<?>> primitiveToObjectMap = new HashMap<Class<?>, Class<?>>(16);


    static {
        primitiveToObjectMap.put(boolean.class, Boolean.class);
        primitiveToObjectMap.put(byte.class, Byte.class);
        primitiveToObjectMap.put(char.class, Character.class);
        primitiveToObjectMap.put(double.class, Double.class);
        primitiveToObjectMap.put(float.class, Float.class);
        primitiveToObjectMap.put(int.class, Integer.class);
        primitiveToObjectMap.put(long.class, Long.class);
        primitiveToObjectMap.put(short.class, Short.class);
    }

    /**
     * Resolve the given primitive class name as primitive wrapper class, if appropriate,
     * according to the JVM's naming rules for primitive classes.
     *
     * @param       name        The name of the potentially primitive class.
     * @return      Class       The primitive wrapper class, or <code>null</code> if the name does not denote
     * a primitive class or primitive array class
     */
    public static Class<?> resolvePrimitiveClassName(Class<?> primitiveClass) {
        Class<?> result = null;
        
        if (primitiveToObjectMap.containsKey(primitiveClass)) {
            result = (Class<?>) primitiveToObjectMap.get(primitiveClass);
        }
        
        return result;
    }
    
}
