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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a REST resource with this to have interface's elibile methods exported.
 * 
 * @author David Winterfeldt
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestResource {

    /**
     * Whether or not the resource is exported.
     * Defaults to <code>true</code>.
     */
    boolean export() default true;

    /**
     * Whether or not the resource is exported as a client.
     * Defaults to <code>true</code>.
     */
    boolean exportClient() default true;

    /**
     * Service class to delegate requests.
     */
    Class<?> service();

    /**
     * Optional value for path prefix, primarily intended for inherited methods.
     */
    String path();
    
    /**
     * Optional value to define a response for HTTP posts.
     */
    Class<?> responseClass();
    
}
