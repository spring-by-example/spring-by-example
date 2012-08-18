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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Proxy;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>Tests JMX.</p>
 * 
 * <p><strong>Note</strong>: To be able to access 
 * the JMX on the localhost using jconsole, this needs to be 
 * a Java argument: <i>-Dcom.sun.management.jmxremote</i></p>
 * 
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JmxTest {

    final Logger logger = LoggerFactory.getLogger(JmxTest.class);

    @Autowired
    private MBeanServerConnection clientConnector = null;

    @Autowired
    @Qualifier("serverManagerProxy")
    private ServerManager serverManager = null;

    /**
     * Tests <code>MBeanServerConnection</code>.
     */
    @Test
    public void testMBeanServerConnection() {   
        MBeanInfo beanInfo = null;
        
        try {
            beanInfo = clientConnector.getMBeanInfo(new ObjectName("org.springbyexample.jmx:name=ServerManager"));
        } catch (InstanceNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IntrospectionException e) {
            logger.error(e.getMessage());
        } catch (MalformedObjectNameException e) {
            logger.error(e.getMessage());
        } catch (ReflectionException e) {
            logger.error(e.getMessage());
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        int expectedAttributesSize = 2;
        
        assertNotNull("MBean info is null.", beanInfo);
        assertNotNull("MBean info attribute list is null.", beanInfo.getAttributes());
        assertEquals("Number of attributes should be " + expectedAttributesSize + ".", expectedAttributesSize, beanInfo.getAttributes().length);

        int expectedOperationsSize = 4;

        assertNotNull("MBean info operation list is null.", beanInfo.getOperations());
        assertEquals("Number of operations should be " + expectedOperationsSize + ".", expectedOperationsSize, beanInfo.getOperations().length);
    }

    /**
     * Tests <code>ServerManager</code> remote proxy.
     */
    @Test
    public void testServerManagerRemoteProxy() {
        assertNotNull("ServerManager remote proxy is null.", serverManager);
        assertTrue("ServerManager should be a JDK dynamic proxy.", Proxy.isProxyClass(serverManager.getClass()));
        
        logger.info("serverName={}, serverRunning={}", 
                    serverManager.getServerName(), serverManager.isServerRunning());
        
        int min = 10;
        int max = 20;
        
        int result = serverManager.changeConnectionPoolSize(min, max);
        
        assertTrue("Connection pool size should be between " + min + " and " + max + ".",  
                   ((result >= min) && (result <= max)));
        
        logger.info("Current connection pool size is {}.", result);
    }
    
}
