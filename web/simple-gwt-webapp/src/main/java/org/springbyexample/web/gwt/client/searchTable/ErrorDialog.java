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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Error dialog.
 * 
 * <p><strong>Note</strong>: Originally based on GWT DynaTable example.</p>
 * 
 * @author David Winterfeldt
 */
public class ErrorDialog extends DialogBox implements ClickListener {

    private HTML body = new HTML("");

    /**
     * Constructor
     */
    public ErrorDialog() {
        setStylePrimaryName("searchTableErrorDialog");
        Button closeButton = new Button("Close", this);
        VerticalPanel panel = new VerticalPanel();
        panel.setSpacing(4);
        panel.add(body);
        panel.add(closeButton);
        panel.setCellHorizontalAlignment(closeButton, VerticalPanel.ALIGN_RIGHT);
        setWidget(panel);
    }

    /**
     * Gets html body.
     */
    public String getBody() {
        return body.getHTML();
    }

    /**
     * Sets html body.
     */
    public void setBody(String html) {
        body.setHTML(html);
    }

    /**
     * Hide on click event.
     */
    public void onClick(Widget sender) {
        hide();
    }

}