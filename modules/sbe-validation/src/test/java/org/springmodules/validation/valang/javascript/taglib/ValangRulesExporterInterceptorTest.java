/*
 * Copyright 2004-2009 the original author or authors.
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

package org.springmodules.validation.valang.javascript.taglib;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springmodules.validation.valang.ValangValidator;
import org.springmodules.validation.valang.web.servlet.mvc.SimpleFormController;

/**
 * Unit test for the ValangRulesExporterInterceptorTests.
 * 
 * @author Colin Yates
 * @since 0.9
 *
 */
public class ValangRulesExporterInterceptorTest extends TestCase {
	
	private ValangRulesExportInterceptor interceptor = new ValangRulesExportInterceptor();
	private MockHttpServletRequest request = new MockHttpServletRequest("GET", "/whatever.html");
	private MockHttpServletResponse response = new MockHttpServletResponse();
	private SimpleFormController controller = new SimpleFormController();
	private MyTestBean command = new MyTestBean();
	private ModelAndView modelAndView = new ModelAndView();
	private ValangValidator validator = new ValangValidator();
	
	public void setUp() throws Exception {
		// setup the validator
		this.validator.setValang("{ name : ? is not null : 'Name is a required field.' : 'name_required' }");
		this.validator.afterPropertiesSet();
		
		// setup SFC
		this.controller.setCommandClass(this.command.getClass());
		this.modelAndView.addObject(this.controller.getCommandName(), this.command);
		this.controller.setValidator(this.validator);
	}
	
	/**
	 * Exercise the normal behaviour
	 */
	public void testHappyCase() throws Exception {		
		this.interceptor.postHandle(this.request, this.response, this.controller, this.modelAndView);
		
		Map map = this.modelAndView.getModel();
		assertTrue("expected validation rule to be added", map.containsKey("ValangRules.command"));
	}
	
	/**
	 * Verify that if the {@link #controller} is a proxies <code>BaseCommandController</code>,
	 * it still works correctly.
	 */
	public void testHappyCaseWithProxiedBaseCommandController() throws Exception {
		ProxyFactory factory = new ProxyFactory(this.controller);
		Object proxiedController = factory.getProxy();
		
		this.interceptor.postHandle(this.request, this.response, proxiedController, this.modelAndView);
		
		Map map = this.modelAndView.getModel();
		assertTrue("expected validation rule to be added", map.containsKey("ValangRules.command"));
	}

	/**
	 * Verify that if the {@link #controller} is a proxy, but isn't a {@link BaseCommandController}
	 *  it still works correctly (o.e. no-op).
	 */
	// FIX ME: not working
//	public void testHappyCaseWithProxiedPlainController() throws Exception {
//		Controller plainController = new PlainController();
//		ProxyFactory factory = new ProxyFactory(plainController);
//		Object proxiedController = factory.getProxy();
//		
//		this.interceptor.postHandle(this.request, this.response, proxiedController, this.modelAndView);
//		
//		Map map = this.modelAndView.getModel();
//		assertFalse("non BaseCommandControllers should be ignored", map.containsKey("ValangRules.command"));
//	}

	/**
	 * Verify that the handler works correctly (i.e. is a no-op) when the {@link #controller}
	 * isn't a {@link org.springframework.web.servlet.mvc.BaseCommandController}.
	 */
	// FIX ME: not working
//	public void testWhenHandlerIsntBaseCommandController() throws Exception {
//		Controller plainController = new PlainController();
//		this.interceptor.postHandle(this.request, this.response, plainController, this.modelAndView);
//		
//		Map map = this.modelAndView.getModel();
//		assertFalse("expected non BaseCommandControllers to be ignored", map.containsKey("ValangRules.command"));
//	}
	
	/**
	 * Verify that the handler works correctly (i.e. is a no-op) if the
	 * {@link #command} command object hasn't been added to the {@link #controller}.
	 */
	public void testWithNoCommand() throws Exception {
		this.modelAndView = new ModelAndView();
		this.interceptor.postHandle(this.request, this.response, this.controller, this.modelAndView);
		
		Map map = this.modelAndView.getModel();
		assertFalse("validation shouldn't be added if no command", map.containsKey("ValangRules.command"));
	}
	
	/**
	 * Verify that the handler works correctly (i.e. no-op) with a 
	 * {@link org.springframework.validation.Validator native} validator. 
	 */
	public void testWithNonValangValidators() throws Exception {
		Validator plainValidator = new Validator() {
			public boolean supports(Class arg0) {
				return false;
			}

			public void validate(Object arg0, Errors arg1) {
			}			
		};
		this.controller.setValidator(plainValidator);
		this.interceptor.postHandle(this.request, this.response, this.controller, this.modelAndView);
		
		Map map = this.modelAndView.getModel();
		assertFalse("native validators should be ignored", map.containsKey("ValangRules.command"));
	}
}

class MyTestBean {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

class PlainController implements Controller {
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
};

