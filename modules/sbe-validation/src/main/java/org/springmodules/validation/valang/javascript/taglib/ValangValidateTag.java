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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springmodules.validation.valang.ValangValidator;
import org.springmodules.validation.valang.javascript.ValangJavaScriptTranslator;
import org.springmodules.validation.valang.parser.ParseException;
import org.springmodules.validation.valang.parser.ValangParser;
import org.springmodules.validation.web.servlet.mvc.BaseCommandController;

/**
 * Generates JavaScript validation code from a set valang validation
 * rules. The generated code requires a set of JavaScript objects to
 * have been placed into the page either by using the {@link ValangCodebaseTag}
 * or by directly including the code from the file "valang_codebase.js"
 * located in the org.springmodules.validation.valang.javascript package.
 * <p/>
 * <p>It is expected that the validation rules to be translated are placed
 * into the model using one of the methods from {@link ValangJavaScriptTagUtils}
 * or using the Spring MVC interceptor {@link ValangRulesExportInterceptor},
 * however it is not required that rules be placed into the model to use this
 * tag.
 * <p/>
 * <p>If the tag has any body content this will be interpreted as a set of
 * additional valang rules that will be appended to the set of rules located
 * for the provided command name; or if no command name is specified the
 * translated body content will be used to provide all rules.
 * <p/>
 * <p>NOTE: this tag must be placed inside the HTML form tags that the validation
 * rules are expected to apply too; failure to do this will result in a JavaScript
 * exception being thrown when the page loads.
 *
 * @author Oliver Hutchison
 */
public class ValangValidateTag extends RequestContextAwareTag implements BodyTag {

    /**
     * Prefix which, when appended with a command name, is used to identify valang
     * validation rules placed into the model.
     */
    public static final String VALANG_RULES_KEY_PREFIX = "ValangRules.";

    private final ValangJavaScriptTranslator translator = new ValangJavaScriptTranslator();

    private String commandName;

    private BodyContent bodyContent;

    /**
     * Sets the name of the command which will be validated by the generated JavaScript.
     * If this value is specified it is expected that a collection of valang rules for
     * the specified command name have been placed in the model using one of the
     * methods from {@link ValangJavaScriptTagUtils}.
     *
     * @see ValangJavaScriptTagUtils#addValangRulesToModel(BaseCommandController, Map)
     * @see ValangJavaScriptTagUtils#addValangRulesToModel(String, ValangValidator, Map)
     */
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    protected int doStartTagInternal() {
        return EVAL_BODY_BUFFERED;
    }

    public void doInitBody() {
        // do nothing
    }

    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        try {
            Collection rules = new ArrayList();
            if (commandName != null) {
                rules.addAll(getRulesForCommand());
            }
            if (bodyContent != null) {
                rules.addAll(parseRulesFromBodyContent());
            }
            if (rules.size() == 0) {
                throw new JspException("no valang validation rules were found");
            }

            JspWriter out = pageContext.getOut();
            out.write("<script type=\"text/javascript\" id=\"");
            out.write(commandName + "ValangValidator");
            out.write("\">");
            translator.writeJavaScriptValangValidator(out, commandName, true, rules, new MessageSourceAccessor(
                getRequestContext().getWebApplicationContext(), getRequestContext().getLocale()));
            out.write("</script>");
            return EVAL_PAGE;
        }
        catch (IOException e) {
            throw new JspException("Could not write validation rules", e);
        }
    }

    protected Collection parseRulesFromBodyContent() throws JspException {
        try {
            return new ValangParser(bodyContent.getReader()).parseValidation();
        }
        catch (ParseException e) {
            throw new JspException("Could not parse valang", e);
        }
    }

    protected Collection getRulesForCommand() {
        Collection rules = (Collection) pageContext.findAttribute(VALANG_RULES_KEY_PREFIX + commandName);
        Assert.notNull(rules, "No valang rules for command '" + commandName + "' were found in the page context.");
        return rules;
    }

    public void doFinally() {
        super.doFinally();
        commandName = null;
    }
}