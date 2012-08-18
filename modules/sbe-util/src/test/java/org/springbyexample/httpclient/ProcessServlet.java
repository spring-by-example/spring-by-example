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

package org.springbyexample.httpclient;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Test servlet.
 * 
 * @author David Winterfeldt
 */
public class ProcessServlet extends HttpServlet {

    final Logger logger = LoggerFactory.getLogger(ProcessServlet.class);
    
    private static final long serialVersionUID = 3326542834660736731L;
    
    /**
     * Processes HTTP Get.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
            
        String paramInputValue = request.getParameter(HttpClientTemplateTest.GET_PARAM_INPUT_KEY);
        
        if (!StringUtils.hasText(paramInputValue)) {
            out.print(HttpClientTemplateTest.GET_RESULT);
        
            out.flush();
        } else {
            out.print("<" + HttpClientTemplateTest.GET_PARAM_INPUT_KEY + ">");
            out.print(paramInputValue);
            out.print("</" + HttpClientTemplateTest.GET_PARAM_INPUT_KEY + ">");
            
            out.flush();            
        }
        
        logger.debug("Processed get.");
     }
 

    /**
     * Processes HTTP Post.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        
        String lowerParam = request.getParameter(HttpClientTemplateTest.LOWER_PARAM);
        
        // no param, expecting a data stream in post
        if (!StringUtils.hasText(lowerParam)) {
            String post = IOUtils.toString(request.getInputStream());

            out.print(getPostDataResult());
            out.flush();
            
            logger.debug("Processed data post.  '{}'", post);
        } else {
            String result = lowerParam.toLowerCase();
            
            out.print("<result>");
            out.print(result);
            out.print("</result>");
            
            out.flush();
        
            logger.debug("Processed post lower case.  orig='{}'  result='{}'", lowerParam, result);
        }
     }
 
    /**
     * Gets a post with data's result.
     */
    protected String getPostDataResult() {
        return HttpClientTemplateTest.POST_DATA_RESULT;
    }
    
}