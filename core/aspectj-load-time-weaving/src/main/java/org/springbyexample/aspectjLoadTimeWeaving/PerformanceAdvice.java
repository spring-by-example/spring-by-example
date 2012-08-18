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

package org.springbyexample.aspectjLoadTimeWeaving;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

/**
 * Advice for performance tracing.
 * 
 * @author David Winterfeldt
 */
@Aspect
public class PerformanceAdvice {

    @Pointcut("execution(public * org.springbyexample.aspectjLoadTimeWeaving..*.*(..))")
    public void aspectjLoadTimeWeavingExamples() {
    }

    @Around("aspectjLoadTimeWeavingExamples()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        final Logger logger = LoggerFactory.getLogger(pjp.getSignature().getDeclaringType());

        StopWatch sw = new StopWatch(getClass().getSimpleName());

        try {
            sw.start(pjp.getSignature().getName());

            return pjp.proceed();
        } finally {
            sw.stop();

            logger.debug(sw.prettyPrint());
        }
    }

}