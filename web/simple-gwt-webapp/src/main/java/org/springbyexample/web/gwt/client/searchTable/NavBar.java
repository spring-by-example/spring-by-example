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

package org.springbyexample.web.gwt.client.searchTable;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * <p>Search table navigation composite widget.</p>
 * 
 * <p><strong>Note</strong>: Originally based on GWT DynaTable example.</p>
 * 
 * @author David Winterfeldt
 */
public class NavBar extends Composite implements ClickListener {

    public final DockPanel bar = new DockPanel();
    public final Button gotoFirst = new Button("&lt;&lt;", this);
    public final Button gotoNext = new Button("&gt;", this);
    public final Button gotoPrev = new Button("&lt;", this);
    public final HTML status = new HTML();
    
    private final DynamicTableWidget dynamicTable;

    /**
     * Constructor
     */
    public NavBar(DynamicTableWidget dynamicTable) {
        this.dynamicTable = dynamicTable;
        
        initWidget(bar);
        bar.setStyleName("navbar");
        status.setStyleName("status");

        HorizontalPanel buttons = new HorizontalPanel();
        buttons.add(gotoFirst);
        buttons.add(gotoPrev);
        buttons.add(gotoNext);
        bar.add(buttons, DockPanel.EAST);
        bar.setCellHorizontalAlignment(buttons, DockPanel.ALIGN_RIGHT);
        bar.add(status, DockPanel.CENTER);
        bar.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
        bar.setCellHorizontalAlignment(status, HasAlignment.ALIGN_RIGHT);
        bar.setCellVerticalAlignment(status, HasAlignment.ALIGN_MIDDLE);
        bar.setCellWidth(status, "100%");

        // Initialize prev & first button to disabled.
        gotoPrev.setEnabled(false);
        gotoFirst.setEnabled(false);
    }

    /**
     * On click, handle navigation events from navbar buttons.
     */
    public void onClick(Widget sender) {
        if (sender == gotoNext) {
            dynamicTable.setStartRow(dynamicTable.getStartRow() + dynamicTable.getDataRowCount());
            dynamicTable.refresh();
        } else if (sender == gotoPrev) {
            dynamicTable.setStartRow(dynamicTable.getStartRow() - dynamicTable.getDataRowCount());
            if (dynamicTable.getStartRow() < 0) {
                dynamicTable.setStartRow(0);
            }
            dynamicTable.refresh();
        } else if (sender == gotoFirst) {
            dynamicTable.setStartRow(0);
            dynamicTable.refresh();
        }
    }

}
