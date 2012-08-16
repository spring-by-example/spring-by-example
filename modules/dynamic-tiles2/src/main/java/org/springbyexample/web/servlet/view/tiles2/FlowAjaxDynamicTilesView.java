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

package org.springbyexample.web.servlet.view.tiles2;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.springframework.webflow.execution.View;

/**
 * <p>If the request isn't an AJAX request, <code>DynamicTilesView</code> 
 * will handle the request.  Otherwise it is expected that an AJAX 
 * view render will be next in the chain and can handle the request.</p>
 * 
 * <p><strong>Note</strong>: All code is copied from <code>AjaxDynamicTilesView</code> (giving author credit to original author).  
 * Necessary to duplicate logic since <code>getRenderFragments</code> is protected, so no way to delegate to 
 * the original code.</p>
 * 
 * @author Jeremy Grelle
 */
public class FlowAjaxDynamicTilesView extends AjaxDynamicTilesView {

    /**
     * <p>Gets rendered fragments.</p>
     * 
     * <p><strong>Note</strong>: Copied from <code>FlowAjaxTilesView</code>.</p>
     */
    protected String[] getRenderFragments(Map model, HttpServletRequest request, HttpServletResponse response) {
        RequestContext context = RequestContextHolder.getRequestContext();
        
        if (context == null) {
            return super.getRenderFragments(model, request, response);
        } else {
            String[] fragments = (String[]) context.getFlashScope().get(View.RENDER_FRAGMENTS_ATTRIBUTE);
            if (fragments == null) {
                return super.getRenderFragments(model, request, response);
            }
            return fragments;
        }
    }
    
}
