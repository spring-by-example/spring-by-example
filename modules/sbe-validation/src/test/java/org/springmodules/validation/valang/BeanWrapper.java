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

package org.springmodules.validation.valang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean wrapper for <code>Map</code>s and <code>Array</code>s.
 * 
 * @author David Winterfeldt
 */
public class BeanWrapper {

    private List<Object> lVars = new ArrayList<Object>();
    private Map<String, Object> hVars = new HashMap<String, Object>();
    private Object[] vars = null;

    /**
     * Gets list vars.
     */
    public List<Object> getListVars() {
        return lVars;
    }

    /**
     * Sets list vars.
     */
    public void setListVars(List<Object> lVars) {
        this.lVars = lVars;
    }

    /**
     * Gets variable <code>Map</code>.
     */
    public Map<String, Object> getMapVars() {
        return hVars;
    }

    /**
     * sets variable <code>Map</code>.
     */
    public void setMapVars(Map<String, Object> hVars) {
        this.hVars = hVars;
    }

    /**
     * Gets array vars.
     */
    public Object[] getVars() {
        return vars;
    }

    /**
     * Sets array vars.
     */
    public void setVars(Object[] vars) {
        this.vars = vars;
    }

}
