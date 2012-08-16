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

package org.springbyexample.web.jpa.bean {

import mx.collections.ArrayCollection;
	

/**
 * <p>Person information which binds to the Java 
 * remote class <code>org.springbyexample.web.jpa.bean.Person</code>.</p>
 * 
 * @author David Winterfeldt
 */
[RemoteClass(alias="org.springbyexample.web.jpa.bean.Person")]
public class Person {
	
    public var id:int;
    public var firstName:String;
    public var lastName:String;
    public var addresses:ArrayCollection;
    public var created:Date;

    /**
     * Constructor
     */
    public function Person() {
        super();
    }
    
}

}