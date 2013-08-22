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
package org.springbyexample.mvc.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springbyexample.service.AbstractService;
import org.springbyexample.converter.ListConverter;

/**
 * Annotate a REST resource with this to influence how it is exported.
 *
 * @author David Winterfeldt
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RestRequestResource {

    /**
     * Whether or not the resource is exported.
     * Defaults to <code>true</code>.
     */
    boolean export() default true;

    /**
     * Whether or not the request mapping pattern is a relative URI.
     * Defaults to <code>true</code>.
     */
    boolean relative() default true;

    /**
     * Service class to delegate requests.
     */
    Class<?> service() default AbstractService.class;

    /**
     * Optional explicit method on the service class to delegate requests,
     * otherwise interface method name is used.
     */
    String methodName() default "";

    /**
     * Optional converter to process result from method invocation.
     */
    Class<?> converter() default ListConverter.class;

}
