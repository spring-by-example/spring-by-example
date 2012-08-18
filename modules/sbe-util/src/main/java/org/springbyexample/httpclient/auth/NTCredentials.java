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
 * <p>NTLM Credentials bean used to create 
 * <code>org.apache.commons.httpclient.NTCredentials</code>
 * and <code>org.apache.commons.httpclient.auth.AuthScope</code>.</p>
 * 
 * <p><strong>Note</strong>: User name, password, host, and domain are required.</p>
 * 
 * @author David Winterfeldt
 */
public class NTCredentials extends Credentials implements Serializable {

    private static final long serialVersionUID = -6920896316432574273L;
    
    protected String host = null;
    protected String domain = null;
    
    /**
     * Constructor
     */
    public NTCredentials() {}
    
    /**
     * Constructor
     */
    public NTCredentials(String authScopeHost, int authScopePort,
                         String userName, String password,
                         String host, String domain) {
        super(authScopeHost, authScopePort, userName, password);
        
        this.host = host;
        this.domain = domain;
    }

    /**
     * Gets host.
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets domain.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets domain.
     */
    public void setDomain(String domain) {
        this.domain = domain;
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
        sb.append("  host=" + host);
        sb.append("  domain=" + domain);
        sb.append(" }");
        
        return sb.toString();
    }
    
}
