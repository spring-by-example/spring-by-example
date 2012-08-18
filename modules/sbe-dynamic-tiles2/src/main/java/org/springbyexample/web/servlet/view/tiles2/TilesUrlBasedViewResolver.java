/*
 * Copyright 2007-2012 the original author or authors.
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

package org.springbyexample.web.servlet.view.tiles2;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * <p>Extends <code>UrlBasedViewResolver</code> and provides 
 * some properties to set tiles values if the view is a 
 * <code>DynamicTilesView</code>.</p>
 *
 * @author David Winterfeldt
 */
public class TilesUrlBasedViewResolver extends UrlBasedViewResolver {

	private String tilesDefinitionName = null;
	private String tilesBodyAttributeName = null;
	private String tilesDefinitionDelimiter = null;

	/**
	 * Main template name.
	 */
	public void setTilesDefinitionName(String tilesDefinitionName) {
		this.tilesDefinitionName = tilesDefinitionName;
	}

	/**
	 * Tiles body attribute name. 
	 */
	public void setTilesBodyAttributeName(String tilesBodyAttributeName) {
		this.tilesBodyAttributeName = tilesBodyAttributeName;
	}

	/**
	 * Sets Tiles definition delimiter.  
	 */
	public void setTilesDefinitionDelimiter(String tilesDefinitionDelimiter) {
		this.tilesDefinitionDelimiter = tilesDefinitionDelimiter;
	}

	/**
	 * Does everything the <code>UrlBasedViewResolver</code> does and 
	 * also sets some Tiles specific values on the view.
	 * 
	 * @param viewName the name of the view to build
	 * @return the View instance
	 * @throws Exception if the view couldn't be resolved
	 * @see #loadView(String, java.util.Locale)
	 */
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		AbstractUrlBasedView view = super.buildView(viewName);
		
		// if DynamicTilesView, set tiles specific values
		if (view instanceof DynamicTilesView) {
			DynamicTilesView dtv = (DynamicTilesView)view;
			
			if (StringUtils.hasLength(tilesDefinitionName)) {
				dtv.setTilesDefinitionName(tilesDefinitionName);
			}
			
			if (StringUtils.hasLength(tilesBodyAttributeName)) {
				dtv.setTilesBodyAttributeName(tilesBodyAttributeName);
			}

			if (tilesDefinitionDelimiter != null) {
				dtv.setTilesDefinitionDelimiter(tilesDefinitionDelimiter);
			}
		}
		
		return view;
	}
	
}
