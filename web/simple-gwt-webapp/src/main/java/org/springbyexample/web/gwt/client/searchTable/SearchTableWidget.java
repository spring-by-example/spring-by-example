/*
 * Copyright 2004-2008 the original author or authors.
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

import org.springbyexample.web.gwt.client.Service;
import org.springbyexample.web.gwt.client.ServiceAsync;
import org.springbyexample.web.gwt.client.bean.Person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;

/**
 * <p>A composite widget for search tables</p>.
 * 
 * <p><strong>Note</strong>: Originally based on GWT DynaTable example.</p>
 * 
 * @author David Winterfeldt
 */
public class SearchTableWidget extends Composite {

    private final DynamicTableWidget dynamicTable;
    private final PersonProvider personProvider = new PersonProvider();

    /**
     * Constructor
     */
    public SearchTableWidget(int visibleRows) {
        Dictionary messageResource = Dictionary.getDictionary("MessageResource");

        String[] columns = new String[] { messageResource.get("person.form.firstName"), 
                                          messageResource.get("person.form.lastName"),
                                          "" };
        
        String[] styles = new String[] { "firstName", "lastName", "links" };
        
        dynamicTable = new DynamicTableWidget(personProvider, columns, 
                                              styles, visibleRows);
        initWidget(dynamicTable);
    }

    /**
     * On load, refresh the table.
     */
    @Override
    protected void onLoad() {
        dynamicTable.refresh();
    }

    /**
     * A data provider that bridges the provides row level updates from the data
     * available through a <@link SchoolCalendarService>.
     * 
     * @author David Winterfeldt
     */
    private class PersonProvider implements SearchTableDataProvider {

        private final ServiceAsync service;
        private int lastMaxRows = -1;
        private Person[] lastPeople;
        private int lastStartRow = -1;

        /**
         * Constructor
         */
        public PersonProvider() {
            service = (ServiceAsync) GWT.create(Service.class);
            ServiceDefTarget target = (ServiceDefTarget) service;
            String moduleRelativeURL = GWT.getHostPageBaseURL() + "service.do";
            target.setServiceEntryPoint(moduleRelativeURL);
        }

        public void updateRowData(final int startRow, final int maxRows,
                                  final RowDataAcceptor acceptor) {
            final StringBuffer sb = new StringBuffer();
            
            // Check the simple cache first.
            if (startRow == lastStartRow) {
                if (maxRows == lastMaxRows) {
                    // Use the cached batch.
                    pushResults(acceptor, startRow, lastPeople);
                }
            }

            // Fetch the data remotely.
            service.findPersons(startRow, maxRows, new AsyncCallback<Person[]>() {
                public void onFailure(Throwable caught) {
                    acceptor.failed(caught);
                }

                public void onSuccess(Person[] result) {
                    lastStartRow = startRow;
                    lastMaxRows = maxRows;
                    lastPeople = result;
                    
                    pushResults(acceptor, startRow, result);
                }

            });
        }

        private void pushResults(RowDataAcceptor acceptor, int startRow, Person[] people) {
            String[][] rows = new String[people.length][];
            for (int i = 0, n = rows.length; i < n; i++) {
                Person person = people[i];
                rows[i] = new String[3];
                rows[i][0] = person.getFirstName();
                rows[i][1] = person.getLastName();
                rows[i][2] = "";

                // increment by one because of table header
                int row = i + 1;
                processLinkRequest(person.getId(), acceptor, row);
            }

            String message = acceptor.accept(startRow, rows);
            dynamicTable.setStatusText(message);
        }

        private void processLinkRequest(Integer personId,
                final RowDataAcceptor acceptor, final int row) {
            String url = GWT.getHostPageBaseURL() + "fragment/search_link.htm" + 
                         "?ajaxSource=true&fragments=content&" + "id=" + personId;
            url = URL.encode(url);

            RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, url);

            try {
                Request response = rb.sendRequest(null, new RequestCallback() {
                    public void onError(Request request, Throwable exception) {
                    }

                    public void onResponseReceived(Request request, Response response) {
                        dynamicTable.setCell(row, 2, response.getText());
                    }
                });
            } catch (RequestException e) {
                Window.alert("Failed to send the request: " + e.getMessage());
            }
        }
    }

}
