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

/**
 * <p>Trade information.</p>
 * 
 * @author David Winterfeldt
 */
public class TradeInfo extends JavaScriptObject {

    /**
     * <p>Constructor</p>
     * 
     * <p>Overlay types always have protected, zero-arg constuctors.</p>
     */
    protected TradeInfo() { } 
    
    /**
     * Gets name.
     */
    public final native String getName() /*-{ 
        return this.name; 
    }-*/;

    /**
     * Gets symbol.
     */
    public final native String getSymbol()  /*-{ 
        return this.symbol;  
    }-*/;

    /**
     * Gets total trades.
     */
    public final native int getTotalTrades()  /*-{ 
        return this.totalTrades;  
    }-*/;
    
    /**
     * Gets total volume.
     */
    public final native int getTotalVolume()  /*-{ 
        return this.totalVolume;  
    }-*/;
   
}