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

import org.springbyexample.web.gwt.client.searchTable.SearchTableDataProvider.RowDataAcceptor;

import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>Table widget than can paginate and dynamically update 
 * rows and cells</p>.
 * 
 * <p><strong>Note</strong>: Originally based on GWT DynaTable example.</p>
 * 
 * @author David Winterfeldt
 */
public class DynamicTableWidget extends Composite {

    private final Grid grid = new Grid();
    private final NavBar navbar = new NavBar(this);
    private final RowDataAcceptor acceptor = new RowDataAcceptorImpl(grid, navbar);
    private final DockPanel outer = new DockPanel();
    private final SearchTableDataProvider provider;
    private int startRow = 0;

    /**
     * Constructor
     */
    public DynamicTableWidget(SearchTableDataProvider provider,
                              String[] columns, String[] columnStyles, int rowCount) {
        if (columns.length == 0) {
            throw new IllegalArgumentException("expecting a positive number of columns");
        }

        if (columnStyles != null && columns.length != columnStyles.length) {
            throw new IllegalArgumentException("expecting as many styles as columns");
        }

        this.provider = provider;
        initWidget(outer);
        grid.setStyleName("table");
        outer.add(navbar, DockPanel.NORTH);
        outer.add(grid, DockPanel.CENTER);
        initTable(columns, columnStyles, rowCount);
        setStyleName("personSearchTableWidget");
    }

    /**
     * Refreshes table.
     */
    public void refresh() {
        // Disable buttons temporarily to stop the user from running off the end.
        navbar.gotoFirst.setEnabled(false);
        navbar.gotoPrev.setEnabled(false);
        navbar.gotoNext.setEnabled(false);

        setStatusText("Please wait...");
        
        provider.updateRowData(startRow, grid.getRowCount() - 1, acceptor);
    }

    /**
     * Sets a specific cells data.
     */
    protected void setCell(int destRowIndex, int srcColIndex, String value) {
        grid.setHTML(destRowIndex, srcColIndex, value);
    }

    /**
     * Sets row count.
     */
    public void setRowCount(int rows) {
        grid.resizeRows(rows);
    }

    /**
     * Sets status text.
     */
    public void setStatusText(String text) {
        navbar.status.setText(text);
    }

    /**
     * Clears status text.
     */
    public void clearStatusText() {
        navbar.status.setHTML("&nbsp;");
    }

    /**
     * Gets start row.
     */
    int getStartRow() {
        return startRow;
    }

    /**
     * Sets start row.
     */
    void setStartRow(int startRow) {
        this.startRow = startRow;
    }
    
    /**
     * Gets data row count.
     */
    int getDataRowCount() {
        return grid.getRowCount() - 1;
    }

    /**
     * Initialize table.
     */
    private void initTable(String[] columns, String[] columnStyles, int rowCount) {
        // Set up the header row. It's one greater than the number of visible rows.
        grid.resize(rowCount + 1, columns.length);
        
        for (int i = 0, n = columns.length; i < n; i++) {
            grid.setText(0, i, columns[i]);
            if (columnStyles != null) {
                grid.getCellFormatter().setStyleName(0, i,
                        columnStyles[i] + " header");
            }
        }

        // set odd/even styles on grid rows
        for (int i = 0; i < rowCount; i++) {
            grid.getRowFormatter().setStyleName(i, (i % 2 == 0 ? "even" : "odd"));
        }
    }

}
