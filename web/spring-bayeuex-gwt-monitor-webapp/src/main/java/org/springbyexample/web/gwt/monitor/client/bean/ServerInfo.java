/*
 * Copyright 2002-2008 the original author or authors.
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

package org.springbyexample.web.gwt.monitor.client.bean;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * <p>Server information.</p>
 * 
 * @author David Winterfeldt
 */
public class ServerInfo extends JavaScriptObject {

    /**
     * <p>Constructor</p>
     * 
     * <p>Overlay types always have protected, zero-arg constuctors.</p>
     */
    protected ServerInfo() { } 
      
    /**
     * Gets server name.
     */
    public final native String getName() /*-{ 
        return this.name; 
    }-*/;

    /**
     * Gets whether or not the server is active.
     */
    public final native boolean isActive()  /*-{ 
        return this.active;  
    }-*/;
 
    /**
     * Gets trade list.
     */
    public final native JsArray<TradeInfo> getTradeList()  /*-{ 
        return this.tradeList;  
    }-*/;
    
}