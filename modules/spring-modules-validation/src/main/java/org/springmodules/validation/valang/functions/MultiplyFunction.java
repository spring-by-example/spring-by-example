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

public class MultiplyFunction extends AbstractMathFunction {

    public MultiplyFunction(Function leftFunction, Function rightFunction, int line, int column) {
        super(leftFunction, rightFunction, line, column);
    }

    public Object getResult(Object target) {
        return getTemplate().execute(target, new FunctionCallback() {
            public Object execute(Object target) throws Exception {
                return new Double(transform(getLeftFunction().getResult(target)) * transform(getRightFunction().getResult(target)));
            }
        });
    }

}
