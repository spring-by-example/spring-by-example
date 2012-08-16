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

/**
 * An interface for providing row-level updates of data, intended here to used
 * to update a DynaTableWidget.
 * 
 * <p><strong>Note</strong>: Originally based on GWT DynaTable example.</p>
 * 
 * @author David Winterfeldt
 */
public interface SearchTableDataProvider {

    /**
     * An interface allow a widget to accept or report failure when a row data
     * is issued for update.
     */
    interface RowDataAcceptor {
        String accept(int startRow, String[][] rows);

        String failed(Throwable caught);
    }

    void updateRowData(int startRow, int maxRows, RowDataAcceptor acceptor);
    
}
