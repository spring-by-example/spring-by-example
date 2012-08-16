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
 * <p>This function returns the target bean.
 *
 * @author Steven Devijver
 * @since 29-04-2005
 */
public class TargetBeanFunction implements Function {

    public TargetBeanFunction() {
        super();
    }

    public Object getResult(Object target) {
        return target;
    }

}
