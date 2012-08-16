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

package org.springmodules.validation.valang.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * <p>Function taking the value from a bean property or a java.util.Map instance.
 *
 * @author Steven Devijver
 * @since Apr 23, 2005
 */
public class BeanPropertyFunction implements Function {

    final Logger logger = LoggerFactory.getLogger(BeanPropertyFunction.class);
    
    private String field = null;
    
    /**
     * Constructor
     */
    public BeanPropertyFunction(String field) {
        super();
        this.field = field;
    }

    /**
     * Gets property/field name.
     * @return
     */
    public String getField() {
        return this.field;
    }

    /**
     * Gets result.  Implementation of <code>Function</code>.
     */
    public Object getResult(Object target) {
        String fieldName = getField();
        
        if ("this".equals(fieldName)) {
            return target;
        }
        if (fieldName.startsWith("this.")) {
            fieldName = field.substring(5);
        }
        if (target instanceof Map) {
            return getValue((Map) target, split(fieldName));
        }        
        if (target instanceof BeanWrapper) {
            return ((BeanWrapper) target).getPropertyValue(fieldName);
        }
        return new BeanWrapperImpl(target).getPropertyValue(fieldName);
    }

    private String[] split(String path) {
        return StringUtils.split(path, '.');
    }

    private String[] pop(String[] path) {
        Collection coll = new ArrayList();
        for (int i = 1; i < path.length; i++) {
            coll.add(path[i]);
        }
        return (String[]) coll.toArray(new String[]{});
    }

    private String concat(String[] path) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < path.length; i++) {
            sb.append(path[i]);
            sb.append(".");
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private Object getValue(Map map, String[] path) {
        if (path.length > 0) {
            Object result = MapUtils.getObject(map, path[0]);
            if (path.length > 1) {
                return new BeanPropertyFunction(concat(pop(path))).getResult(result);
            } else {
                return result;
            }
        }

        return null;
    }
}
