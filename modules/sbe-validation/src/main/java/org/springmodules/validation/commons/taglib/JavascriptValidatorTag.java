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

package org.springmodules.validation.commons.taglib;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.validator.*;
import org.apache.commons.validator.util.ValidatorUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springmodules.validation.commons.MessageUtils;
import org.springmodules.validation.commons.ValidatorFactory;

/**
 * Custom tag that generates JavaScript for client side validation based
 * on the validation rules loaded by a <code>ValidatorFactory</code>.
 * <p/>
 * <p>The validator resources needed for this tag are retrieved from a
 * ValidatorFactory bean defined in the web application context or one
 * of its parent contexts. The bean is resolved by type
 * (<code>org.springmodules.commons.validator.ValidatorFactory</code>).</p>
 *
 * @author David Winterfeldt.
 * @author Daniel Miller
 */
public class JavascriptValidatorTag extends BodyTagSupport {

    protected RequestContext requestContext;

    /**
     * The name of the form that corresponds with the action name
     * in struts-config.xml. Specifying a form name places a
     * &lt;script&gt; &lt;/script&gt; around the javascript.
     */
    protected String formName = null;

    /**
     * The line ending string.
     */
    protected static String lineEnd = System.getProperty("line.separator");

    /**
     * The current page number of a multi-part form.
     * Only valid when the formName attribute is set.
     */
    protected int page = 0;

    /**
     * This will be used as is for the JavaScript validation method name if it has a value.  This is
     * the method name of the main JavaScript method that the form calls to perform validations.
     */
    protected String methodName = null;

    /**
     * The static JavaScript methods will only be printed if this is set to "true".
     */
    protected String staticJavascript = "true";

    /**
     * The dynamic JavaScript objects will only be generated if this is set to "true".
     */
    protected String dynamicJavascript = "true";

    /**
     * The src attribute for html script element (used to include an external script
     * resource). The src attribute is only recognized
     * when the formName attribute is specified.
     */
    protected String src = null;

    /**
     * The JavaScript methods will enclosed with html comments if this is set to "true".
     */
    protected String htmlComment = "true";

    /**
     * The generated code should be XHTML compliant when "true". When true,
     * this setting prevents the htmlComment setting from having an effect.
     */
    protected String xhtml = "false";

    /**
     * Hide JavaScript methods in a CDATA section for XHTML when "true".
     */
    protected String cdata = "true";

    private String htmlBeginComment = "\n<!-- Begin \n";

    private String htmlEndComment = "//End --> \n";

    /**
     * Gets the key (form name) that will be used
     * to retrieve a set of validation rules to be
     * performed on the bean passed in for validation.
     */
    public String getFormName() {
        return formName;
    }

    /**
     * Sets the key (form name) that will be used
     * to retrieve a set of validation rules to be
     * performed on the bean passed in for validation.
     * Specifying a form name places a
     * &lt;script&gt; &lt;/script&gt; tag around the javascript.
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }

    /**
     * Gets the current page number of a multi-part form.
     * Only field validations with a matching page numer
     * will be generated that match the current page number.
     * Only valid when the formName attribute is set.
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets the current page number of a multi-part form.
     * Only field validations with a matching page numer
     * will be generated that match the current page number.
     * Only valid when the formName attribute is set.
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Gets the method name that will be used for the Javascript
     * validation method name if it has a value.  This overrides
     * the auto-generated method name based on the key (form name)
     * passed in.
     */
    public String getMethod() {
        return methodName;
    }

    /**
     * Sets the method name that will be used for the Javascript
     * validation method name if it has a value.  This overrides
     * the auto-generated method name based on the key (form name)
     * passed in.
     */
    public void setMethod(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Gets whether or not to generate the static
     * JavaScript.  If this is set to 'true', which
     * is the default, the static JavaScript will be generated.
     */
    public String getStaticJavascript() {
        return staticJavascript;
    }

    /**
     * Sets whether or not to generate the static
     * JavaScript.  If this is set to 'true', which
     * is the default, the static JavaScript will be generated.
     */
    public void setStaticJavascript(String staticJavascript) {
        this.staticJavascript = staticJavascript;
    }

    /**
     * Gets whether or not to generate the dynamic
     * JavaScript.  If this is set to 'true', which
     * is the default, the dynamic JavaScript will be generated.
     */
    public String getDynamicJavascript() {
        return dynamicJavascript;
    }

    /**
     * Sets whether or not to generate the dynamic
     * JavaScript.  If this is set to 'true', which
     * is the default, the dynamic JavaScript will be generated.
     */
    public void setDynamicJavascript(String dynamicJavascript) {
        this.dynamicJavascript = dynamicJavascript;
    }

    /**
     * Gets the src attribute's value when defining
     * the html script element.
     */
    public String getSrc() {
        return src;
    }

    /**
     * Sets the src attribute's value when defining
     * the html script element. The src attribute is only recognized
     * when the formName attribute is specified.
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * Gets whether or not to delimit the
     * JavaScript with html comments.  If this is set to 'true', which
     * is the default, the htmlComment will be surround the JavaScript.
     */
    public String getHtmlComment() {
        return htmlComment;
    }

    /**
     * Sets whether or not to delimit the
     * JavaScript with html comments.  If this is set to 'true', which
     * is the default, the htmlComment will be surround the JavaScript.
     */
    public void setHtmlComment(String htmlComment) {
        this.htmlComment = htmlComment;
    }

    /**
     * Returns the cdata setting "true" or "false".
     *
     * @return String - "true" if JavaScript will be hidden in a CDATA section
     */
    public String getCdata() {
        return cdata;
    }

    /**
     * Sets the cdata status.
     *
     * @param cdata The cdata to set
     */
    public void setCdata(String cdata) {
        this.cdata = cdata;
    }

    /**
     * Gets whether or not to generate the xhtml code.
     * If this is set to 'true', which is the default,
     * XHTML will be generated.
     */
    public String getXhtml() {
        return xhtml;
    }

    /**
     * Sets whether or not to generate the xhtml code.
     * If this is set to 'true', which is the default,
     * XHTML will be generated.
     */
    public void setXhtml(String xhtml) {
        this.xhtml = xhtml;
    }

    /**
     * Render the JavaScript for to perform validations based on the form name.
     *
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        StringBuffer results = new StringBuffer();

        Locale locale = RequestContextUtils.getLocale((HttpServletRequest) pageContext.getRequest());

        ValidatorResources resources = getValidatorResources();

        Form form = resources.getForm(locale, formName);
        if (form != null) {
            if ("true".equalsIgnoreCase(dynamicJavascript)) {
                MessageSource messages = getMessageSource();

                List lActions = new ArrayList();
                List lActionMethods = new ArrayList();

                // Get List of actions for this Form
                for (Iterator i = form.getFields().iterator(); i.hasNext();) {
                    Field field = (Field) i.next();

                    for (Iterator x = field.getDependencyList().iterator(); x.hasNext();) {
                        Object o = x.next();

                        if (o != null && !lActionMethods.contains(o)) {
                            lActionMethods.add(o);
                        }
                    }
                }

                // Create list of ValidatorActions based on lActionMethods
                for (Iterator i = lActionMethods.iterator(); i.hasNext();) {
                    String depends = (String) i.next();
                    ValidatorAction va = resources.getValidatorAction(depends);

                    // throw nicer NPE for easier debugging
                    if (va == null) {
                        throw new NullPointerException("Depends string \""
                            + depends
                            + "\" was not found in validator-rules.xml.");
                    }

                    String javascript = va.getJavascript();
                    if (javascript != null && javascript.length() > 0) {
                        lActions.add(va);
                    } else {
                        i.remove();
                    }
                }

                Collections.sort(lActions, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        ValidatorAction va1 = (ValidatorAction) o1;
                        ValidatorAction va2 = (ValidatorAction) o2;

                        if ((va1.getDepends() == null || va1.getDepends().length() == 0)
                            && (va2.getDepends() == null || va2.getDepends().length() == 0)) {
                            return 0;
                        } else if (
                            (va1.getDepends() != null && va1.getDepends().length() > 0)
                                && (va2.getDepends() == null || va2.getDepends().length() == 0)) {
                            return 1;
                        } else if (
                            (va1.getDepends() == null || va1.getDepends().length() == 0)
                                && (va2.getDepends() != null && va2.getDepends().length() > 0)) {
                            return -1;
                        } else {
                            return va1.getDependencyList().size() - va2.getDependencyList().size();
                        }
                    }
                });

                String methods = null;
                for (Iterator i = lActions.iterator(); i.hasNext();) {
                    ValidatorAction va = (ValidatorAction) i.next();

                    if (methods == null) {
                        methods = va.getMethod() + "(form)";
                    } else {
                        methods += " && " + va.getMethod() + "(form)";
                    }
                }

                results.append(getJavascriptBegin(methods));

                for (Iterator i = lActions.iterator(); i.hasNext();) {
                    ValidatorAction va = (ValidatorAction) i.next();
                    String jscriptVar = null;
                    String functionName = null;

                    if (va.getJsFunctionName() != null && va.getJsFunctionName().length() > 0) {
                        functionName = va.getJsFunctionName();
                    } else {
                        functionName = va.getName();
                    }

                    results.append("    function " + functionName + " () { \n");
                    for (Iterator x = form.getFields().iterator(); x.hasNext();) {
                        Field field = (Field) x.next();

                        // Skip indexed fields for now until there is a good way to handle
                        // error messages (and the length of the list (could retrieve from scope?))
                        if (field.isIndexed()
                            || field.getPage() != page
                            || !field.isDependency(va.getName())) {

                            continue;
                        }

                        String message = MessageUtils.getMessage(messages, locale, va, field);

                        message = (message != null) ? message : "";

                        jscriptVar = this.getNextVar(jscriptVar);

                        results.append("     this."
                            + jscriptVar
                            + " = new Array(\""
                            + field.getKey()
                            + "\", \""
                            + message
                            + "\", ");

                        results.append("new Function (\"varName\", \"");

                        Map vars = field.getVars();
                        // Loop through the field's variables.
                        Iterator varsIterator = vars.keySet().iterator();
                        while (varsIterator.hasNext()) {
                            String varName = (String) varsIterator.next();
                            Var var = (Var) vars.get(varName);
                            String varValue = var.getValue();
                            String jsType = var.getJsType();

                            // skip requiredif variables field, fieldIndexed, fieldTest, fieldValue
                            if (varName.startsWith("field")) {
                                continue;
                            }

                            if (Var.JSTYPE_INT.equalsIgnoreCase(jsType)) {
                                results.append("this."
                                    + varName
                                    + "="
                                    + ValidatorUtils.replace(varValue,
                                    "\\",
                                    "\\\\")
                                    + "; ");
                            } else if (Var.JSTYPE_REGEXP.equalsIgnoreCase(jsType)) {
                                results.append("this."
                                    + varName
                                    + "=/"
                                    + ValidatorUtils.replace(varValue,
                                    "\\",
                                    "\\\\")
                                    + "/; ");
                            } else if (Var.JSTYPE_STRING.equalsIgnoreCase(jsType)) {
                                results.append("this."
                                    + varName
                                    + "='"
                                    + ValidatorUtils.replace(varValue,
                                    "\\",
                                    "\\\\")
                                    + "'; ");
                                // So everyone using the latest format doesn't need to change their xml files immediately.
                            } else if ("mask".equalsIgnoreCase(varName)) {
                                results.append("this."
                                    + varName
                                    + "=/"
                                    + ValidatorUtils.replace(varValue,
                                    "\\",
                                    "\\\\")
                                    + "/; ");
                            } else {
                                results.append("this."
                                    + varName
                                    + "='"
                                    + ValidatorUtils.replace(varValue,
                                    "\\",
                                    "\\\\")
                                    + "'; ");
                            }
                        }

                        results.append(" return this[varName];\"));\n");
                    }
                    results.append("    } \n\n");
                }
            } else if ("true".equalsIgnoreCase(staticJavascript)) {
                results.append(this.getStartElement());
                if ("true".equalsIgnoreCase(htmlComment)) {
                    results.append(htmlBeginComment);
                }
            }
        }

        if ("true".equalsIgnoreCase(staticJavascript)) {
            results.append(getJavascriptStaticMethods(resources));
        }

        if (form != null
            && ("true".equalsIgnoreCase(dynamicJavascript)
            || "true".equalsIgnoreCase(staticJavascript))) {

            results.append(getJavascriptEnd());
        }


        JspWriter writer = pageContext.getOut();
        try {
            writer.print(results.toString());
        }
        catch (IOException e) {
            throw new JspException(e.getMessage());
        }

        return (SKIP_BODY);

    }

    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
        //		bundle = Globals.MESSAGES_KEY;
        formName = null;
        page = 0;
        methodName = null;
        staticJavascript = "true";
        dynamicJavascript = "true";
        htmlComment = "true";
        cdata = "true";
        src = null;
    }

    /**
     * Returns the opening script element and some initial javascript.
     */
    protected String getJavascriptBegin(String methods) {
        StringBuffer sb = new StringBuffer();
        String name =
            formName.substring(0, 1).toUpperCase()
                + formName.substring(1, formName.length());

        sb.append(this.getStartElement());

        if (this.isXhtml() && "true".equalsIgnoreCase(this.cdata)) {
            sb.append("//<![CDATA[\r\n");
        }

        if (!this.isXhtml() && "true".equals(htmlComment)) {
            sb.append(htmlBeginComment);
        }
        sb.append("\n     var bCancel = false; \n\n");

        if (methodName == null || methodName.length() == 0) {
            sb.append("    function validate"
                + name
                + "(form) {                                                                   \n");
        } else {
            sb.append("    function "
                + methodName
                + "(form) {                                                                   \n");
        }
        sb.append("        if (bCancel) \n");
        sb.append("      return true; \n");
        sb.append("        else \n");

        // Always return true if there aren't any Javascript validation methods
        if (methods == null || methods.length() == 0) {
            sb.append("       return true; \n");
        } else {
            sb.append("       return " + methods + "; \n");
        }

        sb.append("   } \n\n");

        return sb.toString();
    }

    protected String getJavascriptStaticMethods(ValidatorResources resources) {
        StringBuffer sb = new StringBuffer();

        sb.append("\n\n");

        Iterator actions = resources.getValidatorActions().values().iterator();
        while (actions.hasNext()) {
            ValidatorAction va = (ValidatorAction) actions.next();
            if (va != null) {
                String javascript = va.getJavascript();
                if (javascript != null && javascript.length() > 0) {
                    sb.append(javascript + "\n");
                }
            }
        }

        return sb.toString();
    }

    /**
     * Returns the closing script element.
     */
    protected String getJavascriptEnd() {
        StringBuffer sb = new StringBuffer();

        sb.append("\n");
        if (!this.isXhtml() && "true".equals(htmlComment)) {
            sb.append(htmlEndComment);
        }

        if (this.isXhtml() && "true".equalsIgnoreCase(this.cdata)) {
            sb.append("//]]>\r\n");
        }

        sb.append("</script>\n\n");

        return sb.toString();
    }

    /**
     * The value <code>null</code> will be returned at the end of the sequence.
     * &nbsp;&nbsp;&nbsp; ex: "zz" will return <code>null</code>
     */
    private String getNextVar(String input) {
        if (input == null) {
            return "aa";
        }

        input = input.toLowerCase();

        for (int i = input.length(); i > 0; i--) {
            int pos = i - 1;

            char c = input.charAt(pos);
            c++;

            if (c <= 'z') {
                if (i == 0) {
                    return c + input.substring(pos, input.length());
                } else if (i == input.length()) {
                    return input.substring(0, pos) + c;
                } else {
                    return input.substring(0, pos) + c + input.substring(pos, input.length() - 1);
                }
            } else {
                input = replaceChar(input, pos, 'a');
            }

        }

        return null;

    }

    /**
     * Replaces a single character in a <code>String</code>
     */
    private String replaceChar(String input, int pos, char c) {
        if (pos == 0) {
            return c + input.substring(pos, input.length());
        } else if (pos == input.length()) {
            return input.substring(0, pos) + c;
        } else {
            return input.substring(0, pos) + c + input.substring(pos, input.length() - 1);
        }
    }

    /**
     * Constructs the beginning &lt;script&gt; element depending on xhtml status.
     */
    private String getStartElement() {
        StringBuffer start = new StringBuffer("<script type=\"text/javascript\"");

        // there is no language attribute in xhtml
        if (!this.isXhtml()) {
            start.append(" language=\"Javascript1.1\"");
        }

        if (this.src != null) {
            start.append(" src=\"" + src + "\"");
        }

        start.append("> \n");
        return start.toString();
    }

    /**
     * Returns true if this is an xhtml page.
     */
    private boolean isXhtml() {
        return "true".equalsIgnoreCase(xhtml);
    }

    /**
     * Use the application context itself for default message resolution.
     */
    private MessageSource getMessageSource() {
        try {
            this.requestContext =
                new RequestContext((HttpServletRequest) this.pageContext.getRequest());
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            pageContext.getServletContext().log("Exception in custom tag", ex);
        }
        return requestContext.getWebApplicationContext();
    }

    /**
     * Get the validator resources from a ValidatorFactory defined in the
     * web application context or one of its parent contexts.
     * The bean is resolved by type (org.springmodules.commons.validator.ValidatorFactory).
     *
     * @return ValidatorResources from a ValidatorFactory
     */
    private ValidatorResources getValidatorResources() {
        WebApplicationContext ctx = (WebApplicationContext)
            pageContext.getRequest().getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (ctx == null) {
            // look in main application context (i.e. applicationContext.xml)
            ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
        }
        ValidatorFactory factory = (ValidatorFactory)
            BeanFactoryUtils.beanOfTypeIncludingAncestors(ctx, ValidatorFactory.class, true, true);
        return factory.getValidatorResources();
    }

}
