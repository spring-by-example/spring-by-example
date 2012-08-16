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

package org.springbyexample.web.gwt.monitor.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * <p>Entry point classes define <code>onModuleLoad()</code>.</p>
 * 
 * @author David Winterfeldt
 */
public class App extends AppBase implements EntryPoint {

    /**
     * Initializes widget.
     */
    @Override
    protected void init() {
        // overriding setup for GWT debug mode
    }
    
    /**
     * Subscribe to trade monitor events.
     */
    @Override
    protected native void subscribe() /*-{
        // Keep reference to self for use inside closure
        var app = this;
        
        $wnd.monitor.subscribe(
        	function(servers) { 
            	app.@org.springbyexample.web.gwt.monitor.client.App::displayServers(Lcom/google/gwt/core/client/JsArray;)(servers);
         	},
		  	function(trade) {
            	app.@org.springbyexample.web.gwt.monitor.client.App::displayTrade(Lorg/springbyexample/web/gwt/monitor/client/bean/TradeSummaryInfo;)(trade);
         	}
         );
    }-*/;

    /**
     * Subscribe to trade monitor events.
     * 
     * @param       channel     Channel to subscribe to for trade summary information.
     */
    @Override
    protected native void subscribe(String channel) /*-{
        $wnd.monitor.subscribeToChannel(channel);
    }-*/;

    /**
     * Unsubscribe from trade monitor events.
     * 
     * @param       channel     Channel to unsubscribe from (for trade summary information).
     */
    @Override
    protected native void unsubscribe(String channel) /*-{
        $wnd.monitor.unsubscribeFromChannel(channel);
    }-*/;

}
