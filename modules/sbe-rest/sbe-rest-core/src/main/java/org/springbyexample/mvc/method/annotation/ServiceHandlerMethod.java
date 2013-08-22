/*
 * Copyright 2007-2013 the original author or authors.
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
package org.springbyexample.mvc.method.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;


/**
 * <code>HandlerMethod</code> extension to allow setting the method parameters.
 *
 * @author David Winterfeldt
 */
public class ServiceHandlerMethod extends HandlerMethod {

    private Method restBridgedMethod;

    public ServiceHandlerMethod(Object bean, Method method, Method restBridgedMethod) {
        super(bean, method);
        
        this.restBridgedMethod = restBridgedMethod;
        
        // process method parameters
        getMethodParameters();
    }

    @Override
    public MethodParameter[] getMethodParameters() {
        MethodParameter[] parameters = super.getMethodParameters();
        
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = new HandlerMethodParameter(i);
        }

        return parameters;
    }

    @Override
    public HandlerMethod createWithResolvedBean() {
        return this;
    }

    private class HandlerMethodParameter extends MethodParameter {

        protected HandlerMethodParameter(int index) {
            super(ServiceHandlerMethod.this.restBridgedMethod, index);
        }

        @Override
        public Class<?> getDeclaringClass() {
            return ServiceHandlerMethod.this.getBeanType();
        }

        @Override
        public <T extends Annotation> T getMethodAnnotation(Class<T> annotationType) {
            return ServiceHandlerMethod.this.getMethodAnnotation(annotationType);
        }

    }

}
