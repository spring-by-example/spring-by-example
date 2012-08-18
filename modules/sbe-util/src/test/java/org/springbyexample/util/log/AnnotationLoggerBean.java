/*
 * Copyright 2007-2012 the original author or authors.
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

package org.springbyexample.util.log;

import org.slf4j.Logger;

/**
 * Reflection based SLF4J logger bean 
 * that doesn't implement a <code>LoggerAware</code>.
 * 
 * @author David Winterfeldt
 */
public class AnnotationLoggerBean {

    @AutowiredLogger
    final Logger logger = null;
    
    Logger methodLogger = null;

    /**
     * Gets SLF4J logger.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Gets SLF4J method logger.
     */
    public Logger getMethodLogger() {
        return methodLogger;
    }

    /**
     * Gets SLF4J method logger.
     */
    @AutowiredLogger
    public void setMethodLogger(Logger methodLogger) {
        this.methodLogger = methodLogger;
    }

}
