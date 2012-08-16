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
import mx.resources.IResourceManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

import org.springbyexample.web.flex.event.LocaleChangeEvent;
        

/**
 * <p>Locale change command.</p>
 * 
 * @author David Winterfeldt
 */     
public class LocaleChangeCommand implements ICommand {

	private const logger:ILogger = Log.getLogger("org.springbyexample.web.flex.controller.command.LocaleChangeCommand");

	private var resourceManager:IResourceManager;

    /**
     * Constructor
     */
    public function LocaleChangeCommand() {
        super();
    }
    	        
    /**
     * Implementation of <code>ICommand</code>.
     */
    public function execute(event:CairngormEvent):void { 
    	if (resourceManager == null) {
	    	var lce:LocaleChangeEvent = event as LocaleChangeEvent;
	    		    	
	    	resourceManager = lce.resourceManager;
    	}

		var ro:RemoteObject = new RemoteObject("i18nService");  
    	ro.getLocale();

		ro.addEventListener(ResultEvent.RESULT, updateLocale);
	}

    /**
     * Updates locale.
     */ 
    private function updateLocale(event:ResultEvent):void {
		var locale:String = event.result as String;
		
		logger.info("Locale from server is '" + locale + "'.");
		
		if (locale == 'es') {
			changeLocale("es_ES");
  		} else {
  			changeLocale("en_US");
  		}
    }
    
    /**
     * Change locale.
     */ 
    private function changeLocale(locale:String):void {
  		resourceManager.localeChain = [ locale ];
  		
  		logger.info("Changed locale to '" + locale + "'.");    	
    }
    	
}

}
