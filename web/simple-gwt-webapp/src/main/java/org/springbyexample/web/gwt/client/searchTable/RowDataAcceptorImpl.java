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
import com.google.gwt.user.client.ui.Grid;

/**
 * <p>Implementation of <code>RowDataAcceptor</code>.
 * 
 * <p><strong>Note</strong>: Originally based on GWT DynaTable example.</p>
 * 
 * @author David Winterfeldt
 */
public class RowDataAcceptorImpl implements RowDataAcceptor {

    private final Grid grid;
    private final NavBar navbar;
    private ErrorDialog errorDialog = null;
    private static final String NO_CONNECTION_MESSAGE = "<p>Unable to connect to the server to retrieve data for the table.</p>";
    
    /**
     * Constructor
     */
    public RowDataAcceptorImpl(Grid grid, NavBar navbar) {
        this.grid = grid;
        this.navbar = navbar;
    }
    
    /**
     * Accepts data to format search table.
     */
    public String accept(int startRow, String[][] rows) {
        // remove header row
        int destRowCount = (grid.getRowCount() - 1);
        int destColCount = grid.getCellCount(0);
        assert (rows.length <= destRowCount) : "Too many rows";

        int srcRowIndex = 0;
        int srcRowCount = rows.length;
        int destRowIndex = 1; // skip header row
        for (; srcRowIndex < srcRowCount; ++srcRowIndex, ++destRowIndex) {
            String[] srcRowData = rows[srcRowIndex];
            assert (srcRowData.length == destColCount) : " Column count mismatch";
            for (int srcColIndex = 0; srcColIndex < destColCount; ++srcColIndex) {
                String cellHTML = srcRowData[srcColIndex];

                // link column
                if (srcColIndex == 2) {
                    grid.setHTML(destRowIndex, srcColIndex, cellHTML);
                } else {
                    grid.setText(destRowIndex, srcColIndex, cellHTML);
                }
            }
        }

        // Clear remaining table rows.
        //
        boolean isLastPage = false;
        for (; destRowIndex < destRowCount + 1; ++destRowIndex) {
            isLastPage = true;
            for (int destColIndex = 0; destColIndex < destColCount; ++destColIndex) {
                grid.clearCell(destRowIndex, destColIndex);
            }
        }

        // Synchronize the nav buttons.
        navbar.gotoNext.setEnabled(!isLastPage);
        navbar.gotoFirst.setEnabled(startRow > 0);
        navbar.gotoPrev.setEnabled(startRow > 0);

        String statusMessage = (startRow + 1) + " - " + (startRow + srcRowCount);
        
        return statusMessage;
    }

    /**
     * Processes failure.
     */
    public String failed(Throwable caught) {
        if (errorDialog == null) {
            errorDialog = new ErrorDialog();
        }
        if (caught instanceof InvocationException) {
            errorDialog.setText("An RPC server could not be reached");
            errorDialog.setBody(NO_CONNECTION_MESSAGE);
        } else {
            errorDialog.setText("Unexcepted Error processing remote call");
            errorDialog.setBody(caught.getMessage());
        }
        errorDialog.center();
        
        return "Error";
    }
    
}
