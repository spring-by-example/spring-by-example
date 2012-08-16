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

package org.springbyexample.web.flex.log {

import mx.core.mx_internal;
import mx.logging.targets.LineFormattedTarget;

use namespace mx_internal;

/**
 * <p>Logging target for making information acessible as a <code>String</code>.</p>
 * 
 * @author David Winterfeldt
 */
public class StringBufferTarget extends LineFormattedTarget {
        
    [Bindable]
    public var buffer:String = new String();

    /**
     * Constructor
     */
    public function StringBufferTarget() {
        super();
    }

    /**
     * Logs message.
     */
    override mx_internal function internalLog(msg:String) : void {
        this.buffer += msg + "\n";
    }
    
    /**
     * Clears log history.
     */
     public function clear():void {
        buffer = new String();
     }
    
}

}