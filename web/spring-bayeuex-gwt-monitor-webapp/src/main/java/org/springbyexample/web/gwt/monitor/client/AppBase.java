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

import java.util.HashMap;
import java.util.Map;

import org.springbyexample.web.gwt.monitor.client.bean.ServerInfo;
import org.springbyexample.web.gwt.monitor.client.bean.TradeInfo;
import org.springbyexample.web.gwt.monitor.client.bean.TradeSummaryInfo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.IncrementalCommand;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>Entry point classes define <code>onModuleLoad()</code>.</p>
 * 
 * @author David Winterfeldt
 */
public class AppBase implements EntryPoint {

    final ImageResourceBundle bundle = (ImageResourceBundle) GWT.create(ImageResourceBundle.class);
    final Image green = bundle.green().createImage();
    final Image red = bundle.red().createImage();
    
    final DockPanel mainPanel = new DockPanel();
    final DecoratedStackPanel serverPanel = new DecoratedStackPanel();
    final FlowPanel centerTable = new FlowPanel();

    final Map<String, Integer> hServersMenuIndex = new HashMap<String, Integer>();
    final Map<String, SummaryWidget> hSummaryWidgets = new HashMap<String, SummaryWidget>();
    
    final static String SERVER_PANEL_WIDTH = "200px";
    
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        RootPanel cometDiv = RootPanel.get("comet");
        
        if (cometDiv != null) {
            serverPanel.setWidth(SERVER_PANEL_WIDTH);

            mainPanel.setTitle("Trade Monitor");
            mainPanel.add(serverPanel, DockPanel.WEST);            
            mainPanel.add(centerTable, DockPanel.CENTER);

            centerTable.setStyleName("tradeMonitor-centerTable");
            
            cometDiv.add(mainPanel);
            
            subscribe();
            init();
        }
    }

    /**
     * Initializes widget.
     */
    protected void init() {
        // below or running in debug mode
        String serverName = "exchange-gateway";

        Panel panel = new VerticalPanel();
        panel.setWidth(SERVER_PANEL_WIDTH);

        ListBox multiBox = new ListBox(false);
        multiBox.setWidth("11em");
        multiBox.setVisibleItemCount(5);

        multiBox.addItem("ATT");
        multiBox.addItem("GM");
        multiBox.addItem("IBM");
        multiBox.addItem("MS");
        multiBox.addItem("NYX");
        multiBox.addItem("PG");

        panel.add(multiBox);

        String serverHeader = getHeaderString(serverName, green);
        serverPanel.add(panel, serverHeader, true);
        
        String key = getTradeKey(serverName, "nyx");
        SummaryWidget summary = new SummaryWidget(serverName, "NYX", key, new SummaryCloseListener(key));
        centerTable.add(summary);
        hSummaryWidgets.put(key, summary);
        
        key = getTradeKey(serverName, "ibm");
        summary = new SummaryWidget(serverName, "IBM", key, new SummaryCloseListener(key));
        centerTable.add(summary);
        hSummaryWidgets.put(key, summary);
        
        key = getTradeKey(serverName, "ms");
        summary = new SummaryWidget(serverName, "MS", key, new SummaryCloseListener(key));
        centerTable.add(summary);
        hSummaryWidgets.put(key, summary);

//        Timer timer = new Timer() {
//            public void run() {
//                final int random = Random.nextInt(3);
//
//                if (random == 0) {
//                    nyx.getStopLight().toggleGreenLight();
//                } else if (random == 1) {
//                    nyx.getStopLight().toggleYellowLight();
//                } else if (random == 2) {
//                    nyx.getStopLight().toggleRedLight();
//                }
//            }
//        };
//        
//        timer.scheduleRepeating(1000);
    }
    
    /**
     * Subscribe to trade monitor events.
     */
    protected void subscribe() {
    }

    /**
     * Subscribe to trade monitor events.
     * 
     * @param       channel     Channel to subscribe to for trade summary information.
     */
    protected void subscribe(String channel) {
    }

    /**
     * Unsubscribe from trade monitor events.
     * 
     * @param       channel     Channel to unsubscribe from (for trade summary information).
     */
    protected void unsubscribe(String channel) {
    }

    /**
     * Displays servers.
     */
    public void displayServers(final JsArray<ServerInfo> servers) {
        DeferredCommand.addCommand(new IncrementalCommand() {
            Map<String, ServerInfo> hNewServers = new HashMap<String, ServerInfo>();
            int i = 0;

            public boolean execute() {
                ServerInfo server = servers.get(i);
                String key = server.getName();
                            
                displayServer(server);
                
                hNewServers.put(key, server);
               
                // if there are more servers, return true, then increment counter
                return (i++ < servers.length());
            }
        });
    }

    /**
     * Displays server.
     */
    public void displayServer(final ServerInfo server) {
        String key = server.getName();
        boolean active = server.isActive();
        
        int index = -1;
        ListBox multiBox = null;
        
        if (hServersMenuIndex.containsKey(key)) {
            index = hServersMenuIndex.get(key);
            
            VerticalPanel panel = (VerticalPanel)serverPanel.getWidget(index);
            multiBox = (ListBox) panel.getWidget(0);
        } else {
            Panel panel = new VerticalPanel();
            panel.setWidth(SERVER_PANEL_WIDTH);
            
            multiBox = new ListBox(false);
            multiBox.setTitle(key);
            multiBox.setWidth("11em");
            multiBox.setVisibleItemCount(5);
            multiBox.setStyleName("tradeMonitor-stackPanelListBox");
            
            multiBox.addClickListener(new ClickListener() {
                public void onClick(Widget sender) {
                    ListBox lb = (ListBox) sender; 
                    int selectedIndex = lb.getSelectedIndex();

                    String serverName = lb.getTitle();
                    String symbol = lb.getItemText(selectedIndex);
                    
                    addSummaryWidget(serverName, symbol);
                }
            });


            panel.add(multiBox);

            index = serverPanel.getWidgetCount();
            hServersMenuIndex.put(key, index);

            serverPanel.add(panel, key, true);
        }

        String serverHeader = getHeaderString(key, (active ? green : red)); 
        serverPanel.setStackText(index, serverHeader, true);

        multiBox.clear();
        
        JsArray<TradeInfo> trades = server.getTradeList();
        
        for (int i = 0; i < trades.length(); i++) {
            TradeInfo trade = trades.get(i);
            
            multiBox.addItem(trade.getSymbol());
        }
    }

    /**
     * Display trade summary.
     */
    public void displayTrade(final TradeSummaryInfo trade) {
        String key = getTradeKey(trade.getServerName(), trade.getSymbol());
        SummaryWidget summary = hSummaryWidgets.get(key);
        
        summary.update(trade);
    }
    
    /**
     * Adds summary widget.
     */
    protected void addSummaryWidget(final String serverName, final String symbol) {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                // server name and symbol
                String key = getTradeKey(serverName, symbol);
                
                if (!hSummaryWidgets.containsKey(key)) {
                    SummaryWidget summary = new SummaryWidget(serverName, symbol, key, new SummaryCloseListener(key));
                    
                    centerTable.add(summary);
                    
                    hSummaryWidgets.put(key, summary);
                    
                    // subscribe this summary to it's feed
                    subscribe(getChannel(serverName, symbol));
                }
            }
        });
    }

    /**
     * Removes a summary widget.
     */
    protected void removeSummaryWidget(String tradeKey) {
        if (hSummaryWidgets.containsKey(tradeKey)) {
            SummaryWidget summary = hSummaryWidgets.remove(tradeKey);
            
            unsubscribe(getChannel(summary.getServerName(), summary.getName()));
            
            centerTable.remove(summary);
        }
    }
    
    /**
     * Gets trade key.
     * 
     * @param       serverName      Server name.
     * @param       symbol          Symbol name.
     * 
     * @return      String          The trade key is ${server.name}-${symbol}.
     */
    protected String getTradeKey(String serverName, String symbol) {
        return serverName + "-" + symbol;
    }

    /**
     * Gets channel name. 
     */
    protected String getChannel(String serverName, String symbol) {
        return "/monitor/server/" + serverName + "/" + symbol.toLowerCase();
    }
    
    /**
     * Get a string representation of the header that includes an image and some
     * text.
     * 
     * @param       text        The header text.
     * @param       image       The {@link Image} to add next to the header.
     * 
     * @return      String      The header as a string.
     */
    private String getHeaderString(String text, Image image) {
        // Add the image and text to a horizontal panel
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setSpacing(5);
        hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        hPanel.add(image);
        HTML headerText = new HTML(text);
        headerText.setStyleName("tradeMonitor-StackPanelHeader");
        hPanel.add(headerText);

        // Return the HTML string for the panel
        return hPanel.getElement().getString();
    }
    
    /**
     * Close listener for a summary widget.
     */
    class SummaryCloseListener implements ClickListener {

        final String key;
        
        /**
         * Constructor
         */
        public SummaryCloseListener(String key) {
            this.key = key;
        }
        
        /**
         * Implementation of <code>ClickListener</code>.
         */
        public void onClick(Widget sender) {
            removeSummaryWidget(key);
        }
        
    }

}
