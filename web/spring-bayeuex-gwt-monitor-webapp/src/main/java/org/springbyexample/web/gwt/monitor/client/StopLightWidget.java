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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * <p>Stop light widget.</p>
 * 
 * @author David Winterfeldt
 */
public class StopLightWidget extends Composite {

    final ImageResourceBundle bundle = (ImageResourceBundle) GWT.create(ImageResourceBundle.class);
    final Image green = bundle.green().createImage();
    final Image yellow = bundle.yellow().createImage();
    final Image red = bundle.red().createImage();
    
    final Grid stopLightGrid = new Grid(3, 1);
    
    /**
     * This is the entry point method.
     */
    public StopLightWidget() {
        stopLightGrid.setWidget(0, 0, green);
        stopLightGrid.setWidget(1, 0, yellow);
        stopLightGrid.setWidget(2, 0, red);
        
        initWidget(stopLightGrid);
        setStyleName("tradeMonitor-stopLight");
    }
    
    /**
     * Toggles green light.
     */
    public void toggleGreenLight() {
        stopLightGrid.setWidget(0, 0, new Label("X"));
        
        // reset light after 1/10 of a second
        Timer resetTimer = new Timer() {
            public void run() {
                stopLightGrid.setWidget(0, 0, green);
            }
        };
        
        resetTimer.schedule(100);
    }

    /**
     * Toggles yellow light.
     */
    public void toggleYellowLight() {
        stopLightGrid.setWidget(1, 0, new Label("X"));
        
        // reset light after 1/10 of a second
        Timer resetTimer = new Timer() {
            public void run() {
                stopLightGrid.setWidget(1, 0, yellow);
            }
        };
        
        resetTimer.schedule(100);
    }

    /**
     * Toggles red light.
     */
    public void toggleRedLight() {
        stopLightGrid.setWidget(0, 0, new Label("X"));
        
        // reset light after 1/10 of a second
        Timer resetTimer = new Timer() {
            public void run() {
                stopLightGrid.setWidget(2, 0, red);
            }
        };
        
        resetTimer.schedule(100);
    }

}
