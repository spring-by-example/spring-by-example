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

package org.springbyexample.web.cometd.monitor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.cometd.Bayeux;
import org.cometd.Channel;
import org.cometd.Client;
import org.mortbay.cometd.BayeuxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Bayeux Trade Monitor Service.  Generates test messages.</p>
 * 
 * @author David Winterfeldt
 */
@Component
public class TradeMonitorService extends BayeuxService {
    
    final Logger logger = LoggerFactory.getLogger(TradeMonitorService.class);
    
    private static final String NYSE_GATEWAY = "nyse-gateway";
    private static final String NASDAQ_GATEWAY = "nasdaq-gateway";
    
    private static final String PG_SYMBOL = "PG";
    private static final String NYX_SYMBOL = "NYX";
    private static final String MS_SYMBOL = "MS";
    private static final String IBM_SYMBOL = "IBM";
    private static final String GM_SYMBOL = "GM";
    private static final String ATT_SYMBOL = "ATT";
    
    private static final String JAVA_SYMBOL = "JAVA";
    private static final String ORCL_SYMBOL = "ORCL";
    private static final String GOOG_SYMBOL = "GOOG";
    private static final String MSFT_SYMBOL = "MSFT";
    private static final String YHOO_SYMBOL = "YHOO";

    final static Timer timer = new Timer();

    final Map<Channel, SummaryInfo> hTrades = new ConcurrentHashMap<Channel, SummaryInfo>();
    final TradeComparator tradeComparator = new TradeComparator();

    /**
     * Constructor
     */
    @Autowired
    public TradeMonitorService(Bayeux bayeux) {
        super(bayeux, "monitor");        
    }
    
    /**
     * Init sending monitor test messages.
     */
    @PostConstruct
    protected void init() {
        Bayeux bayeux = getBayeux();
        final Channel serversChannel = bayeux.getChannel("/monitor/servers", true);
        final Client client = getClient();

        addChannel(NYSE_GATEWAY, ATT_SYMBOL, "AT&T", 24, 20, 25);
        addChannel(NYSE_GATEWAY, GM_SYMBOL, "General Motors", 5, 5, 9);
        addChannel(NYSE_GATEWAY, IBM_SYMBOL, IBM_SYMBOL, 80, 80, 100);
        addChannel(NYSE_GATEWAY, MS_SYMBOL, "Morgan Stanley", 17, 10, 26);
        addChannel(NYSE_GATEWAY, NYX_SYMBOL, "NYSE Euronext", 27, 23, 32);
        addChannel(NYSE_GATEWAY, PG_SYMBOL, "Proctor & Gamble", 63, 57, 64);

        addChannel(NASDAQ_GATEWAY, JAVA_SYMBOL, "Sun Microsystems, Inc.", 4, 4, 12);
        addChannel(NASDAQ_GATEWAY, ORCL_SYMBOL, "Oracle Corporation", 17, 16, 23);
        addChannel(NASDAQ_GATEWAY, GOOG_SYMBOL, "Google Inc.", 331, 330, 510);
        addChannel(NASDAQ_GATEWAY, MSFT_SYMBOL, "Microsoft Corporation", 21, 21, 28);
        addChannel(NASDAQ_GATEWAY, YHOO_SYMBOL, "Yahoo! Inc.", 12, 12, 20);

        // start now, publish every 10 seconds
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                publishServerList(serversChannel, client);
            }
        }, 0, 5000); 
        
        // start now, publish every second
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                publishTrades(client);
            }
        }, 0, 200);
    }
    
    /**
     * Publish server list.
     */
    protected void publishServerList(Channel serversChannel, Client client) {
        List<Map<String, Object>> lServers = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> lNyseSymbols = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> lNasdaqSymbols = new ArrayList<Map<String, Object>>();
        
        for (SummaryInfo summary : hTrades.values()) {
            if (NYSE_GATEWAY.equals(summary.getServerName())) {
                lNyseSymbols.add(summary.getTradeMap());
            } else if (NASDAQ_GATEWAY.equals(summary.getServerName())) {
                lNasdaqSymbols.add(summary.getTradeMap());
            }
        }
        
        // sort list by symbol
        Collections.sort(lNyseSymbols, tradeComparator);
        Collections.sort(lNasdaqSymbols, tradeComparator);
        
        Map<String, Object> hServer1 = new HashMap<String, Object>();
        hServer1.put("name", NYSE_GATEWAY);
        hServer1.put("active", true);
        hServer1.put("tradeList", lNyseSymbols);

        Map<String, Object> hServer2 = new HashMap<String, Object>();
        hServer2.put("name", NASDAQ_GATEWAY);
        hServer2.put("active", true);
        hServer2.put("tradeList", lNasdaqSymbols);

        lServers.add(hServer1);
        lServers.add(hServer2);

        serversChannel.publish(client, lServers, null);
    }

    /**
     * Publish trades.
     */
    protected void publishTrades(Client client) {
        Random random = new Random();

        for (Channel channel : hTrades.keySet()) {
            SummaryInfo summary = hTrades.get(channel);

            // randomly skip a symbols trade if it's not IBM, P&G, or Google
            if (IBM_SYMBOL.equals(summary.getSymbol()) || 
                PG_SYMBOL.equals(summary.getSymbol()) ||
                GOOG_SYMBOL.equals(summary.getSymbol()) ||
                random.nextInt(5) < 4) {
                int volume = 500 + random.nextInt(500);
                double price = random.nextDouble();
                
                boolean buy = random.nextBoolean();

                summary.increment(volume, price, buy);
                
                channel.publish(client, summary.getTradeSummaryMap(), null);
            }
        }
    }
    
    /**
     * Add channel to trade <code>Map</code>.
     */
    protected void addChannel(String gateway, String symbol, String companyName, 
                              double intialPrice, double minPrice, double maxPrice) {
        Bayeux bayeux = getBayeux();
        
        String channel = "/monitor/server/" + gateway + "/" + symbol.toLowerCase();
        SummaryInfo summary = new SummaryInfo(gateway, symbol, companyName, 
                                              intialPrice, minPrice, maxPrice);
        
        hTrades.put(bayeux.getChannel(channel, true), summary);
    }

    /**
     * Trades summary information.
     */
    class SummaryInfo {
        private String serverName = null;
        private String symbol = null;
        private String name = null;
        
        private int totalTrades = 0;
        private int totalVolume = 0;

        private double minBoundaryPrice = 0;
        private double maxBoundaryPrice = 0;
        
        private double currentPrice = 0;
        private double maxPrice = 0;
        private double minPrice = Double.MAX_VALUE;

        private int totalBuyTrades = 0;
        private int totalBuyVolume = 0;
        private double totalBuyPrice = 0;
        private double averageBuyPrice = 0;

        private int totalSellTrades = 0;
        private int totalSellVolume = 0;
        private double totalSellPrice = 0;
        private double averageSellPrice = 0;

        /**
         * Constructor
         */
        public SummaryInfo(String serverName, String symbol, String name,
                           double initalPrice, double minBoundaryPrice, double maxBoundaryPrice) {
            this.serverName = serverName;
            this.symbol = symbol;
            this.name = name;
            this.currentPrice = initalPrice;
            this.minBoundaryPrice = minBoundaryPrice;
            this.maxBoundaryPrice = maxBoundaryPrice;
        }

        /**
         * Gets server name.
         */
        public String getServerName() {
            return serverName;
        }
        
        /**
         * Gets symbol.
         */
        public String getSymbol() {
            return symbol;
        }

        /**
         * Gets current price.
         */
        public double getCurrentPrice() {
            return currentPrice;
        }

        /**
         * Increment trades and volume.
         */
        public void increment(int volume, double changeInPrice, boolean buy) {
            totalTrades++;
            totalVolume += volume;

            // change buy/sell to opposite if min/max boundary crossed
            if (currentPrice < minBoundaryPrice) {
                buy = true;
            } else if (currentPrice > maxBoundaryPrice) {
                buy = false;
            }

            if (buy) {
                currentPrice += changeInPrice;
            } else {
                currentPrice -= changeInPrice;
            }
            
            if (currentPrice > maxPrice) {
                this.maxPrice = currentPrice;
            }

            if (currentPrice < minPrice) {
                this.minPrice = currentPrice;
            }
            
            if (buy) {
                totalBuyTrades++;
                totalBuyVolume += volume;
                totalBuyPrice += currentPrice;
                averageBuyPrice = totalBuyPrice/totalBuyTrades;
            } else {
                totalSellTrades++;
                totalSellVolume += volume;
                totalSellPrice += currentPrice;
                averageSellPrice = totalSellPrice/totalSellTrades;
            }
        }
        
        /**
         * Gets <code>TradeInfo</code> <code>Map</code>.
         */
        public Map<String, Object> getTradeMap() {
            Map<String, Object> hResults = new HashMap<String, Object>();
            
            hResults.put("symbol", symbol);
            hResults.put("name", name);
            
            hResults.put("totalTrades", totalTrades);
            hResults.put("totalVolume", totalVolume);
            
            return hResults;
        }

        /**
         * Gets <code>TradeSummaryInfo</code> <code>Map</code>.
         */
        public Map<String, Object> getTradeSummaryMap() {
            Map<String, Object> hResults = new HashMap<String, Object>(getTradeMap());
            
            DecimalFormat df = new DecimalFormat("0.0000");
            
            hResults.put("serverName", serverName);
            hResults.put("currentPrice", df.format(currentPrice));
            hResults.put("maxPrice", df.format(maxPrice));
            hResults.put("minPrice", df.format(minPrice));

            hResults.put("totalBuyTrades", totalBuyTrades);
            hResults.put("totalBuyVolume", totalBuyVolume);
            hResults.put("averageBuyPrice", df.format(averageBuyPrice));

            hResults.put("totalSellTrades", totalSellTrades);
            hResults.put("totalSellVolume", totalSellVolume);
            hResults.put("averageSellPrice", df.format(averageSellPrice));

            return hResults;
        }

    }
 
    /**
     * Sorts a <code>Map</code> based on it's symbol key.
     */
    class TradeComparator implements Comparator<Map<String, Object>> {
        
        /**
         * Implementation of <code>Comparator</code>.
         */
        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
            String symbol1 = (String) o1.get("symbol");
            String symbol2 = (String) o2.get("symbol");
            
            return symbol1.compareTo(symbol2);
        }
        
    }
    
}