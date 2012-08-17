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


/**
 * <p>Container for an enum value.</p>  
 * 
 * <p>This is done for performance reasons since based on tests 
 * checking each time if a class is an enum degrades performance.</p>
 *
 * @author David Winterfeldt
 */
public class EnumLiteralFunction extends LiteralFunction {

    /**
     * Constructor
     */
    public EnumLiteralFunction(Object literal) {
        super(literal);
    }

}
