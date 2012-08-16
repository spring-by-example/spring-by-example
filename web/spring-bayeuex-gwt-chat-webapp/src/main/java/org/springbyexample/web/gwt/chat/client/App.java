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

/**
 * <p>Entry point classes define <code>onModuleLoad()</code>.</p>
 * 
 * @author David Winterfeldt
 */
public class App extends AppBase implements EntryPoint {

    /**
     * Join chat.
     */
    protected native void join(String userName) /*-{
        // Keep reference to self for use inside closure
        var app = this;
        
        $wnd.room.join(userName, 
            function(from, text) { 
                app.@org.springbyexample.web.gwt.chat.client.App::display(Ljava/lang/String;Ljava/lang/String;)(from, text);
            },
            function(user) { 
                app.@org.springbyexample.web.gwt.chat.client.App::addUser(Ljava/lang/String;)(user);
            },
            function() { 
                app.@org.springbyexample.web.gwt.chat.client.App::clearUserTable()();
            }
        );
    }-*/;

    /**
     * Leave chat.
     */
    protected native void leave(String userName) /*-{
        $wnd.room.leave();
    }-*/;

    /**
     * Send chat message.
     */
    protected native void send(String message) /*-{   
        $wnd.room.chat(message);  
    }-*/;
    
}
