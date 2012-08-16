/*
 * Copyright 2002-2009 the original author or authors.
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

package org.springbyexample.web.flex.controller.command {

import com.adobe.cairngorm.commands.ICommand;
import com.adobe.cairngorm.control.CairngormEvent;

import mx.logging.ILogger;
import mx.logging.Log;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

import org.springbyexample.web.flex.event.PersonDeleteEvent;
import org.springbyexample.web.flex.event.PersonSearchEvent;
        

/**
 * <p>Person delete command.</p>
 * 
 * @author David Winterfeldt
 */     
public class PersonDeleteCommand implements ICommand {

    private const logger:ILogger = Log.getLogger("org.springbyexample.web.flex.controller.command.PersonDeleteCommand");

    /**
     * Constructor
     */
    public function PersonDeleteCommand() {
        super();
    }
                
    /**
     * Implementation of <code>ICommand</code>.
     */
    public function execute(event:CairngormEvent):void { 
        var pde:PersonDeleteEvent = event as PersonDeleteEvent;
        var id:int = pde.id;
        
        logger.info("Delete person.  id=" + id);
        
        var ro:RemoteObject = new RemoteObject("personService");  
        ro.remove(id);
        
        ro.addEventListener(ResultEvent.RESULT, updateSearch);
    }
    
    /**
     * Updates search.
     */ 
    private function updateSearch(event:ResultEvent):void {
        var pse:PersonSearchEvent = new PersonSearchEvent();
        pse.dispatch();
    }
    	
}

}
