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

package org.springbyexample.jmx;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Server manager implementation.
 * 
 * @author David Winterfeldt
 */
@Component
@ManagedResource(objectName="org.springbyexample.jmx:name=ServerManager", 
                 description="Server manager.")
public class ServerManagerImpl implements ServerManager { 

    final Logger logger = LoggerFactory.getLogger(ServerManagerImpl.class);

    private String serverName = "springServer";
    private boolean serverRunning = true;
    private int minPoolSize = 5;
    private int maxPoolSize = 10;

    /**
     * Gets server name.
     */
    @ManagedAttribute(description="The server name.")
    public String getServerName() {
        return serverName;
    }
    
    /**
     * Whether or not the server is running.
     */
    @ManagedAttribute(description="Server's running status.")
    public boolean isServerRunning() {
        return serverRunning;
    }

    /**
     * Sets whether or not the server is running.
     */
    @ManagedAttribute(description="Whether or not the server is running.",
                      currencyTimeLimit=20,
                      persistPolicy="OnUpdate")
    public void setServerRunning(boolean serverRunning) {
        this.serverRunning = serverRunning;
    }

    /**
     * Change db connection pool size.
     * 
     * @param   min     Minimum pool size.
     * @param   max     Maximum pool size.
     * 
     * @return  int     Current pool size.
     */
    @ManagedOperation(description="Change db connection pool size.")
    @ManagedOperationParameters({
        @ManagedOperationParameter(name="min", description= "Minimum pool size."),
        @ManagedOperationParameter(name="max", description= "Maximum pool size.")})
    public int changeConnectionPoolSize(int minPoolSize, int maxPoolSize) {
        Assert.isTrue((minPoolSize > 0), 
                      "Minimum connection pool size must be larger than zero.  min=" + minPoolSize);
        Assert.isTrue((minPoolSize < maxPoolSize), 
                      "Minimum connection pool size must be smaller than the maximum." + 
                      "  min=" + minPoolSize + ", max=" + maxPoolSize);
        
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
        
        int diff = (maxPoolSize - minPoolSize);

        // randomly generate current pool size between new min and max
        Random rnd = new Random();
        int currentSize = (minPoolSize + rnd.nextInt(diff));
        
        logger.info("Changed connection pool size. min={}, max={}, current={}", 
                    new Object[] { this.minPoolSize, this.maxPoolSize, currentSize});
        
        return currentSize; 
    
    }

}
