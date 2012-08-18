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

package org.springbyexample.httpclient.auth;

import java.io.Serializable;

/**
 * <p>Credentials bean used to create 
 * <code>org.apache.commons.httpclient.UsernamePasswordCredentials</code> 
 * and <code>org.apache.commons.httpclient.auth.AuthScope</code>.</p>
 * 
 * <p><strong>Note</strong>: User authorization scope's host, 
 * authorization scope's port, name, password are both required.</p>
 * 
 * @author David Winterfeldt
 */
public class Credentials implements Serializable {

    private static final long serialVersionUID = 427351901207639483L;
    
    protected String authScopeHost = null;
    protected int authScopePort = 80;
    protected String userName = null;
    protected String password = null;
    
    /**
     * Constructor
     */
    public Credentials() {}
    
    /**
     * Constructor
     */
    public Credentials(String authScopeHost, int authScopePort,
                       String userName, String password) {
        this.authScopeHost = authScopeHost;
        this.authScopePort = authScopePort;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Gets authorization scope's host.
     */
    public String getAuthScopeHost() {
        return authScopeHost;
    }

    /**
     * Sets authorization scope's host.
     */
    public void setAuthScopeHost(String autoScopeHost) {
        this.authScopeHost = autoScopeHost;
    }

    /**
     * Gets authorization scope's port.
     */
    public int getAuthScopePort() {
        return authScopePort;
    }

    /**
     * Sets authorization scope's port.
     */
    public void setAuthScopePort(int authScopePort) {
        this.authScopePort = authScopePort;
    }

    /**
     * Gets user name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * Gets password.
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Returns a string representation of the object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(this.getClass().getName());
        sb.append(" { ");
        sb.append("authScopeHost=" + authScopeHost);
        sb.append("  authScopePort=" + authScopePort);
        sb.append("  userName=" + userName);
        sb.append("  password=" + password);
        sb.append(" }");
        
        return sb.toString();
    }
    
}
