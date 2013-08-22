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

import org.springbyexample.schema.beans.entity.PkEntityBase;


/**
 * DB utilities.
 * 
 * @author David Winterfeldt
 */
public class DBUtil {

    private DBUtil() {}
    
    /**
     * Whether or not the primary key is valid (greater than zero).
     */
    public static boolean isPrimaryKeyValid(PkEntityBase request) {
        return (request.getId() > 0);
    }

}
