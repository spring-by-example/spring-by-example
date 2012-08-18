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

package org.springbyexample.bean.scope.thread;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests custom thread scope.
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CounterCustomScopeTest extends AbstractJUnit4SpringContextTests {

    final Logger logger = LoggerFactory.getLogger(CounterCustomScopeTest.class);
    
    @Autowired
    @Qualifier("taskExecutor")
    private TaskExecutor taskExecutor = null;

    @Autowired
    @Qualifier("executorService")
    private ThreadPoolTaskExecutor executorService = null;

    /**
     * Tests custom thread scope counter bean.
     */
    @Test
    public void testThreadCounter() throws InterruptedException {
        String beanName = "threadCounter";
        
        // create runnables with different counters
        ThreadCounterRunnable threadCounter01 = new ThreadCounterRunnable(beanName, 1);
        ThreadCounterRunnable threadCounter02 = new ThreadCounterRunnable(beanName, 2);
        ThreadCounterRunnable threadCounter03 = new ThreadCounterRunnable(beanName, 3);

        Thread thread01 = new Thread(threadCounter01);
        Thread thread02 = new Thread(threadCounter02);
        Thread thread03 = new Thread(threadCounter03);

        // start threads
        thread01.start();
        thread02.start();
        thread03.start();
    }

    /**
     * Tests custom thread scope counter beans runnables with a task executor.
     */
    @Test
    public void testThreadExecutorRunnable() {
        logger.debug("Begin TaskExecutor runnable test.");
                
        for(int i = 0; i < 20; i++) {
            if (i < 10) {
                // create regular thread scoped bean (not disposeable bean)
                ThreadCounterRunnable threadCounter = new ThreadCounterRunnable("threadCounter", i);

                // wrap in ThreadScopeRunnable so thread cleanup and 
                // destruction callbacks for beans occur
                taskExecutor.execute(new ThreadScopeRunnable(threadCounter));
            } else if (i < 20) {
                // create disposable bean (implements DisposableBean interface)
                ThreadCounterRunnable threadCounter = new ThreadCounterRunnable("disposableThreadCounter", i);

                // wrap in ThreadScopeRunnable so thread cleanup and 
                // destruction callbacks for beans occur
                taskExecutor.execute(new ThreadScopeRunnable(threadCounter));
            }
        }
        
        logger.debug("End TaskExecutor runnable test.");
    }

    /**
     * Tests custom thread scope counter beans callables with a task executor.
     */
    @Test
    public void testThreadExecutorCallable() throws InterruptedException, ExecutionException {
        logger.debug("Begin TaskExecutor callable test.");

        // expected value and future result
        Map<Integer, Future<Integer>> hExpectedValues = new HashMap<Integer, Future<Integer>>();
        
        for(int i = 0; i < 20; i++) {
            if (i < 10) {
                // create regular thread scoped bean (not disposeable bean)
                ThreadCounterCallable threadCounter = new ThreadCounterCallable("threadCounter", i);

                // wrap in ThreadScopeCallable so thread cleanup and 
                // destruction callbacks for beans occur
                Future<Integer> future = executorService.submit(new ThreadScopeCallable<Integer>(threadCounter));
                
                hExpectedValues.put(i, future);
            } else if (i < 20) {
                // create disposable bean (implements DisposableBean interface)
                ThreadCounterCallable threadCounter = new ThreadCounterCallable("disposableThreadCounter", i);

                // wrap in ThreadScopeCallable so thread cleanup and 
                // destruction callbacks for beans occur
                Future<Integer> future = executorService.submit(new ThreadScopeCallable<Integer>(threadCounter));
                
                hExpectedValues.put(i, future);
            }
        }
        
        for (Integer expectedValue : hExpectedValues.keySet()) {
            Future<Integer> future = hExpectedValues.get(expectedValue);
            Integer value = future.get();
            
            logger.info("Callable results.  expectedValue={}  futureValue={}", expectedValue, value);
            
            assertEquals("Counter callable is not '" + expectedValue + "'.", expectedValue, value);
        }
        
        logger.debug("End TaskExecutor callable test.");
    }

    /**
     * Counter runnable.
     */
    protected class ThreadCounterRunnable implements Runnable {

        private int count = 0;
        private String beanName = null;
        
        /**
         * Constructor.
         */
        public ThreadCounterRunnable(String beanName, int count) {
            this.beanName = beanName;
            this.count = count;
        }

        /**
         * Gets a counter bean and increments it X times.
         */
        public void run() {
            int counterCount = 0;
            
            for (int i = 0; i < count; i++) {
                // Get counter each loop.  Should stay the same instance for a thread or 
                // when ThreadScopeRunnable stay the same for that Runnable.
                Counter counter = (Counter) applicationContext.getBean(beanName);

                counter.increment();
                
                counterCount = counter.getCount();
            }
            
            assertEquals("Counter runnable is not '" + count + "'.", count, counterCount);
            
            logger.debug("threadId=" + Thread.currentThread().getId() +
                      "  threadName=" + Thread.currentThread().getName() +
                      "  initialCount=" + count +
                      "  count=" + counterCount);
        }
        
    }

    /**
     * Counter callable.
     */
    protected class ThreadCounterCallable implements Callable<Integer> {

        private int count = 0;
        private String beanName = null;
        
        /**
         * Constructor.
         */
        public ThreadCounterCallable(String beanName, int count) {
            this.beanName = beanName;
            this.count = count;
        }

        /**
         * Gets a counter bean and increments it X times.
         */
        public Integer call() throws Exception {
            int counterCount = 0;
            
            for (int i = 0; i < count; i++) {
                // Get counter each loop.  Should stay the same instance for a thread or 
                // when ThreadScopeCallable stay the same for that Callable.
                Counter counter = (Counter) applicationContext.getBean(beanName);

                counter.increment();
                
                counterCount = counter.getCount();
            }
            
            assertEquals("Counter callable is not '" + count + "'.", count, counterCount);
            
            logger.debug("threadId=" + Thread.currentThread().getId() +
                      "  threadName=" + Thread.currentThread().getName() +
                      "  initialCount=" + count +
                      "  count=" + counterCount);
            
            return counterCount;
        }
        
    }

}
