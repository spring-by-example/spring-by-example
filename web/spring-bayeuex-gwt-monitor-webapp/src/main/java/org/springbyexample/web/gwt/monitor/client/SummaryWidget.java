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

import org.springbyexample.web.gwt.monitor.client.bean.TradeSummaryInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;

/**
 * <p>Summary Widget.</p>
 * 
 * @author David Winterfeldt
 */
public class SummaryWidget extends Composite {

    final Dictionary messageResource = Dictionary.getDictionary("MessageResource");

    final ImageResourceBundle bundle = (ImageResourceBundle) GWT.create(ImageResourceBundle.class);
    final Image close = bundle.close().createImage();
    
    final NumberFormat currencyFormatter = NumberFormat.getCurrencyFormat();
    final NumberFormat decimalFormatter = NumberFormat.getDecimalFormat();
    
    final DockPanel mainPanel = new DockPanel();
    final StopLightWidget stopLight = new StopLightWidget();
    
    final String serverName;
    final String symbol;
    final String key;
    
    final Label northLabel = new Label();
    final Label southLabel = new Label();
    final AbsolutePanel centerPanel = new AbsolutePanel();
    final Label serverNameLabel;
    final Label symbolLabel;
    final Label priceLabel = new Label();
    final FlexTable extraInfoTable = new FlexTable();
    
    /**
     * This is the entry point method.
     */
    public SummaryWidget(String serverName, String symbol, 
                         String key, ClickListener closeListener) {
        this.serverName = serverName;
        this.symbol = symbol;
        this.key = key;
        
        String moreInformationLabel = messageResource.get("monitor.moreInformation");

        serverNameLabel = new Label(serverName);
        serverNameLabel.setStyleName("tradeMonitor-serverName");

        symbolLabel = new Label(symbol);
        symbolLabel.setStyleName("tradeMonitor-symbol");
        
        priceLabel.setStyleName("tradeMonitor-price");
        
        String buyTradesLabel = messageResource.get("monitor.buy.trades");
        String buyVolumeLabel = messageResource.get("monitor.buy.volume");
        String sellTradesLabel = messageResource.get("monitor.sell.trades");
        String sellVolumeLabel = messageResource.get("monitor.sell.volume");

        extraInfoTable.setWidget(0, 0, new Label(buyTradesLabel + ": "));
        extraInfoTable.setWidget(1, 0, new Label(buyVolumeLabel + ": "));
        extraInfoTable.setWidget(2, 0, new Label(sellTradesLabel + ": "));
        extraInfoTable.setWidget(3, 0, new Label(sellVolumeLabel + ": "));
        
        centerPanel.setWidth("200px");
        centerPanel.setHeight("80px");
        centerPanel.add(priceLabel, 5, 30);
        centerPanel.add(symbolLabel, 120, 25);

        DisclosurePanel dp = new DisclosurePanel(moreInformationLabel);
        dp.setAnimationEnabled(true);
        dp.setContent(extraInfoTable);

        PushButton closeButton = new PushButton(close);
        closeButton.addClickListener(closeListener);
        closeButton.setStyleName("tradeMonitor-summaryCloseButton");
        
        AbsolutePanel northPanel = new AbsolutePanel();
        northPanel.setStyleName("tradeMonitor-summaryHeader");
        northPanel.setWidth("200px");
        northPanel.setHeight("24px"); 
        northPanel.add(serverNameLabel, 0, 0);
        northPanel.add(closeButton, (200 - close.getWidth()), 0);
        
        mainPanel.add(northPanel, DockPanel.NORTH);
        mainPanel.add(northLabel, DockPanel.NORTH);
        mainPanel.add(centerPanel, DockPanel.CENTER);
//        mainPanel.add(stopLight, DockPanel.EAST);
        
        mainPanel.add(dp, DockPanel.SOUTH);
        mainPanel.add(southLabel, DockPanel.SOUTH);
        
        initWidget(mainPanel);
        setStyleName("tradeMonitor-symbolSummary");
    }

    /**
     * Gets symbol name.
     */
    public String getName() {
        return symbol;
    }

    /**
     * Gets server name.
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Gets key (unique identifier for widget).
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets stop light.
     */
    public StopLightWidget getStopLight() {
        return stopLight;
    }

    /**
     * Sets volume.
     */
    public void update(TradeSummaryInfo summary) {
        String tradesLabel = messageResource.get("monitor.trades");
        String volumeLabel = messageResource.get("monitor.volume");

        priceLabel.setText(currencyFormatter.format(summary.getCurrentPrice()));
        northLabel.setText(tradesLabel + ": " + decimalFormatter.format(summary.getTotalTrades()));
        southLabel.setText(volumeLabel + ": " + decimalFormatter.format(summary.getTotalVolume()));

        extraInfoTable.setWidget(0, 1, new Label(decimalFormatter.format(summary.getTotalBuyTrades())));
        extraInfoTable.setWidget(1, 1, new Label(decimalFormatter.format(summary.getTotalBuyVolume())));
        extraInfoTable.setWidget(2, 1, new Label(decimalFormatter.format(summary.getTotalSellTrades())));
        extraInfoTable.setWidget(3, 1, new Label(decimalFormatter.format(summary.getTotalSellVolume())));
    }

    /**
     * Indicates whether some other object is "equal to" this one. 
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SummaryWidget)) {
            return false;
        }
        
        if (key.equals(((SummaryWidget)o).getKey())) {
            return true;
        } else {
            return false;
        }
    }
    
}
