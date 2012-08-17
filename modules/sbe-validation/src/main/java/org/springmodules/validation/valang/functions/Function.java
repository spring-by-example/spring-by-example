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
 * <p>A function is an implementation that returns a value based on one of more arguments.
 * A function instance is created for each occurrence of the function in the validation language.</p>
 *
 * @author Steven Devijver
 * @since Apr 23, 2005
 */
public interface Function {

    /**
     * <p>Gets the result of the function.
     *
     * @param target the target bean
     * @return the result of the function
     */
    public Object getResult(Object target);

}
