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
 * <p>Interface to be implemented by any object that should have an 
 * SLF4J logger injected using it's interface.</p>
 * 
 * @author David Winterfeldt
 */
public interface Slf4JLoggerAware extends LoggerAware<Logger> {

    /**
     * Set appropriate logger for this class.
     */
    public void setLogger(Logger logger);
    
}