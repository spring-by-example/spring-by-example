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


/**
 * <p>Trade summary information.</p>
 * 
 * @author David Winterfeldt
 */
public class TradeSummaryInfo extends TradeInfo {

    /**
     * <p>Constructor</p>
     * 
     * <p>Overlay types always have protected, zero-arg constuctors.</p>
     */
    protected TradeSummaryInfo() { } 

// FIXME: buy and sell volume
    
    /**
     * Gets server name.
     */
    public final native String getServerName() /*-{ 
        return this.serverName; 
    }-*/;
    
    /**
     * Gets current price.
     */
    public final native double getCurrentPrice()  /*-{ 
        return this.currentPrice;  
    }-*/;

    /**
     * Gets maximum price.
     */
    public final native double getMaxPrice()  /*-{ 
        return this.maxPrice;  
    }-*/;

    /**
     * Gets minimum price.
     */
    public final native double getMinPrice()  /*-{ 
        return this.minPrice;  
    }-*/;
 
    /**
     * Gets total buy trades.
     */
    public final native int getTotalBuyTrades()  /*-{ 
        return this.totalBuyTrades;  
    }-*/;

    /**
     * Gets total buy volume.
     */
    public final native int getTotalBuyVolume()  /*-{ 
        return this.totalBuyVolume;  
    }-*/;

    /**
     * Gets average buy price.
     */
    public final native double getAverageBuyPrice()  /*-{ 
        return this.averageBuyPrice;  
    }-*/;

    /**
     * Gets total sell trades.
     */
    public final native int getTotalSellTrades()  /*-{ 
        return this.totalSellTrades;  
    }-*/;

    /**
     * Gets total sell volume.
     */
    public final native int getTotalSellVolume()  /*-{ 
        return this.totalSellVolume;  
    }-*/;

    /**
     * Gets average sell price.
     */
    public final native double getAverageSellPrice()  /*-{ 
        return this.averageSellPrice;  
    }-*/;

}