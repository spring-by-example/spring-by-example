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

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springmodules.validation.valang.functions.AbstractInitializableFunction;
import org.springmodules.validation.valang.functions.Function;

/**
 * @author Uri Boness
 */
public class EqualsFunction extends AbstractInitializableFunction {

    protected void validateArguments(Function[] arguments) {
        Assert.isTrue(arguments.length == 2, "EqualsFunction can only be initialized with two arguments");
    }

    protected Object getResult(Object target, Function[] arguments) {
        Object value1 = arguments[0].getResult(target);
        Object value2 = arguments[1].getResult(target);
        return (ObjectUtils.nullSafeEquals(value1, value2)) ? Boolean.TRUE : Boolean.FALSE;
    }

}
