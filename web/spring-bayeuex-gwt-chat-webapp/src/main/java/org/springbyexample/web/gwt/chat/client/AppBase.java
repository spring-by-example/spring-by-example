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

package org.springbyexample.web.gwt.chat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>Entry point classes define <code>onModuleLoad()</code>.</p>
 * 
 * @author David Winterfeldt
 */
public class AppBase implements EntryPoint {

    final Dictionary messageResource = Dictionary.getDictionary("MessageResource");
    
    final Label label = new Label();
    
    final DockPanel chatPanel = new DockPanel();
    FlexTable userTable = new FlexTable();
    final ScrollPanel userScrollPanel = new ScrollPanel(userTable);
    FlexTable chatTable = new FlexTable();
    final ScrollPanel scrollPanel = new ScrollPanel(chatTable);

    final HorizontalPanel joinChatPanel = new HorizontalPanel();
    final TextBox joinChatTextBox = new TextBox();
    
    final HorizontalPanel sendChatPanel = new HorizontalPanel();
    final TextBox chatTextBox = new TextBox();
    
    private String userName = null;
    
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        RootPanel cometDiv = RootPanel.get("comet");
        
        if (cometDiv != null) {
            // Chat example
            String joinLabel = messageResource.get("gwtChatForm.join");
            String sendLabel = messageResource.get("gwtChatForm.send");
            String leaveLabel = messageResource.get("gwtChatForm.leave");
            
            userTable.setTitle("Chat User Display");
            chatTable.setTitle("Chat Display");
            
            userScrollPanel.setStyleName("members");
            scrollPanel.setStyleName("chat");
            joinChatPanel.setStyleName("join");

            scrollPanel.setWidth("500");
            scrollPanel.setHeight("300");

            // begin - join chat                        
            joinChatTextBox.setTitle("Join chat.");
            joinChatTextBox.addKeyboardListener(new KeyboardListener() {
                public void onKeyDown(Widget sender, char keyCode, int modifiers) {
                }

                public void onKeyPress(Widget sender, char keyCode,
                        int modifiers) {
                    if (keyCode == KEY_ENTER) {
                        userName = joinChatTextBox.getText();
                        join(userName);
                        
                        joinChatPanel.setVisible(false);
                        chatPanel.add(sendChatPanel, DockPanel.SOUTH);
                    }                    
                }

                public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                }                
            });
            
            Button joinButton = new Button();
            joinButton.setText(joinLabel);
            joinButton.addClickListener(new ClickListener() {
                public void onClick(Widget sender) {
                    userName = joinChatTextBox.getText();
                    join(userName);

                    joinChatPanel.setVisible(false);
                    chatPanel.add(sendChatPanel, DockPanel.SOUTH);
                    
                    chatTextBox.setFocus(true);
                }
            });
            
            joinChatPanel.add(joinChatTextBox);
            joinChatPanel.add(joinButton);
            // end - join chat            

            
            // begin - send chat                        
            chatTextBox.setTitle("Send a message.");
            chatTextBox.addKeyboardListener(new KeyboardListener() {
                public void onKeyDown(Widget sender, char keyCode, int modifiers) {
                }

                public void onKeyPress(Widget sender, char keyCode,
                        int modifiers) {
                    if (keyCode == KEY_ENTER) {
                        send(chatTextBox.getText());
                        chatTextBox.setText("");
                    }                    
                }

                public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                }                
            });
            
            Button sendButton = new Button();
            sendButton.setText(sendLabel);
            sendButton.addClickListener(new ClickListener() {
                public void onClick(Widget sender) {
                    send(chatTextBox.getText());
                    chatTextBox.setText("");
                }
            });

            Button leaveButton = new Button();
            leaveButton.setText(leaveLabel);
            leaveButton.addClickListener(new ClickListener() {
                public void onClick(Widget sender) {
                    leave(userName);
                    chatTextBox.setText("");
                    chatPanel.remove(sendChatPanel);

                     clearUserTable();
                     clearChatTable();
                     
                    joinChatPanel.setVisible(true);
                }
            });

            sendChatPanel.add(chatTextBox);
            sendChatPanel.add(sendButton);
            sendChatPanel.add(leaveButton);
            // end - send chat            
            
            chatPanel.add(userScrollPanel, DockPanel.WEST);
            chatPanel.add(scrollPanel, DockPanel.CENTER);
            chatPanel.add(joinChatPanel, DockPanel.SOUTH);
            
            cometDiv.add(chatPanel);
        }
    }

    /**
     * Join chat.
     */
    protected void join(String userName) {
    }

    /**
     * Leave chat.
     */
    protected void leave(String userName) {
    }

    /**
     * Shows chat message.
     */
    public void display(String from, String text) { //, Boolean special) {
        int row = chatTable.getRowCount() + 1;
       
        chatTable.setText(row, 0, from);
        
        chatTable.setText(row, 1, text);

        scrollPanel.scrollToBottom();
    }

    /**
     * Send chat message.
     */
    protected void send(String message) {
    }

    /**
     * Shows chat users.
     */
    public void addUser(String user) {
        int row = userTable.getRowCount() + 1;
       
        userTable.setText(row, 0, user);
        
        //userScrollPanel.setScrollPosition(row * 20);
    }

    /**
     * Clear user chat table.
     */
    public void clearUserTable() {
        userTable = new FlexTable();
        userScrollPanel.setWidget(userTable);
    }

    /**
     * Clear chat table.
     */
    public void clearChatTable() {
        chatTable = new FlexTable();
        scrollPanel.setWidget(chatTable);
    }
    
}
