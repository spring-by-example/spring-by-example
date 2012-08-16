/*
 * Copyright 2002-2009 the original author or authors.
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

package org.springbyexample.web.flex.model {

import com.adobe.cairngorm.model.ModelLocator;

import mx.collections.ArrayCollection;
        

/**
 * <p>Person search model locator.</p>
 * 
 * @author David Winterfeldt
 */     
[Bindable]
public class PersonSearchModelLocator implements ModelLocator {

    public var personData:ArrayCollection = new ArrayCollection();
    
    private static var _instance:PersonSearchModelLocator = null; 

    /**
     * Constructor
     */
    public function PersonSearchModelLocator() {
        super();
    }    
        
    /**
     * Implementation of <code>ModelLocator</code>.
     */
    public static function getInstance():PersonSearchModelLocator { 
    	if (_instance == null)  { 
    		_instance = new PersonSearchModelLocator(); 
        } 
            
        return _instance; 
    } 

}

}
