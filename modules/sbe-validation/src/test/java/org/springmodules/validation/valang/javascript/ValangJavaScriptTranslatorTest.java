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

package org.springmodules.validation.valang.javascript;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.springframework.context.support.MessageSourceAccessor;
import org.springmodules.validation.util.date.DateParseException;
import org.springmodules.validation.util.date.DefaultDateParser;
import org.springmodules.validation.valang.parser.ParseException;
import org.springmodules.validation.valang.parser.Person;
import org.springmodules.validation.valang.parser.ValangParser;
import org.springmodules.validation.valang.predicates.ValidationRule;

/**
 * Tests for {@link ValangJavaScriptTranslator}
 *
 * @author Oliver Hutchison
 */
public class ValangJavaScriptTranslatorTest extends TestCase {

    private Context cx;

    private Scriptable scope;

    public void setUp() throws Exception {
        cx = Context.enter();
        cx.setOptimizationLevel(-1);
        cx.setLanguageVersion(Context.VERSION_1_2);
        scope = cx.initStandardObjects();
        cx.evaluateReader(scope, ValangJavaScriptTranslator.getCodebaseReader(), "valang_codebase.js", 1, null);
        cx.evaluateReader(scope, new InputStreamReader(
            ValangJavaScriptTranslatorTest.class.getResourceAsStream("test_codebase_overides.js")),
            "test_codebase_overides.js", 1, null);
    }

    protected void tearDown() throws Exception {
        Context.exit();
    }

    private ValangParser getParser(String text) {
        return new ValangParser(new StringReader(text));
    }

    protected Collection<ValidationRule> parseRules(String text) {
        try {
            return getParser(text).parseValidation();
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean validate(Object target, String text) {
        return validate(target, text, null);
    }

    protected boolean validate(Object target, String text, MessageSourceAccessor messageSource) {
        try {
            Collection<ValidationRule> rules = parseRules(text);
            StringWriter code = new StringWriter();
            new ValangJavaScriptTranslator().writeJavaScriptValangValidator(code, "someName", false, rules,
                messageSource);
            code.write("._validateAndReturnFailedRules()");
            ScriptableObject.putProperty(scope, "formObject", Context.javaToJS(target, scope));
            NativeArray result = (NativeArray) cx.evaluateString(scope, code.toString(), "code", 1, null);
            return result.getLength() == 0;
        }
        catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
//        finally {
//            System.out.println(Context.toString(ScriptableObject.getProperty(scope, "logMessages")));
//        }

    }

    public void testTranslator1SimpleRule() {
        String text = "{age : age <= 120 : 'We do not do business with the undead.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator2TwoSimpleRules() {
        String text = "{age : age >= 18 : 'Our customers must be 18 years or older.'}\n"
            + "{age : age <= 120 : 'We do not do business with the undead.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(150, "Steven"), text));
    }

    public void testTranslator3LengthRule() {
        String text = "{firstName : length (firstName) < 7 and length (firstName) > -1 : 'First name must be no longer than 30 characters.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(7, "Benjamin"), text));
    }

    public void testTranslator4NullRule() {
        String text = "{firstName : firstName is null : 'First name must be null.'}";
        assertTrue(validate(new Person(20, null), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator5NotNullRule() {
        String text = "{firstName : firstName is not null : 'First name must not be null.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(20, null), text));
    }

    public void testTranslator6HasLengthRule() {
        String text = "{firstName : firstName has length : 'First name is required.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(20, ""), text));
    }

    public void testTranslator7HasNoLengthRule() {
        String text = "{firstName : firstName has no length : 'First name is not allowed.'}";
        assertTrue(validate(new Person(20, null), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator8HasTextRule() {
        String text = "{firstName : firstName has text : 'First name is required.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(20, "    "), text));
    }

    public void testTranslator9HasNoTextRule() {
        String text = "{firstName : firstName has no text and !(false) = true : 'First name is not allowed.'}";
        assertTrue(validate(new Person(20, "   "), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator10NotRule() {
        String text = "{firstName : not length (firstName) > 7 : 'First name must be not longer than 7 characters.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(7, "Benjamin"), text));
    }

    public void testTranslator11ComplexNotRule() {
        String text = "{firstName : not length (firstName) > 7 or not(len (firstName) > 5 and len (firstName) > 7) : 'First name is not valid'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "Coraline"), text));
    }

    public void testTranslator12ComplexRule1() {
        String text = "{firstName : (length (firstName) > 5 and age <= 30) or (firstName has length and age > 20) : 'Arrggh!!'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertTrue(validate(new Person(30, "Marie-Claire"), text));
        assertFalse(validate(new Person(7, "test"), text));
    }

    // FIX ME: not working currently
//    public void testTranslator13InRule() {
//        String text = "{size : upper(?) in upper(lower('S')), upper(upper(lower(lower('M')))), upper('L'), 'XL' : 'Not a valid size.'}";
//        assertTrue(validate(new Person("M"), text));
//        assertFalse(validate(new Person("XXL"), text));
//    }

    // FIX ME: not working currently
//    public void testTranslator14NotInRule() {
//        String text = "{firstName : firstName not in 'Bill', 'George' : 'We do not do business with Bill and George.'}";
//        assertTrue(validate(new Person(30, "Steven"), text));
//        assertFalse(validate(new Person(60, "Bill"), text));
//    }

    public void testTranslator15LengthBetweenRule() {
        String text = "{firstName : length (firstName) between 0 and 6 : 'First name is required and must be not longer than 6 characters.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "Marie-Claire"), text));
    }

    public void testTranslator16LengthNotBetweenRule() {
        String text = "{firstName : firstName is null or length (firstName) not between 0 and 6 : 'First name must not have a length between 0 and 6.'}";
        assertTrue(validate(new Person(30, "Marie-Claire"), text));
        assertTrue(validate(new Person(20, null), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator17BetweenRule() {
        String text = "{age : age = 0 or age between 18 and 120 : 'Age must be between 18 and 120.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(7, "Benjamin"), text));
    }

    public void testTranslator18NotBetweenRule() {
        String text = "{age : ? = 0 or age not between 20 and 30 : 'Age must not be between 20 and 30.'}";
        assertTrue(validate(new Person(7, "Benjamin"), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator19Dates1() throws DateParseException {
        String text = "{ dateOfBirth : dateOfBirth >= [1970-01-01] : 'You must be born after 1 January 1970.' }";
        assertTrue(validate(new Person(new DefaultDateParser().parse("1974-11-24")), text));
        assertFalse(validate(new Person(new DefaultDateParser().parse("1950-07-14")), text));
    }

    public void testTranslator20Dates2() throws DateParseException {
        String text = "{ dateOfBirth : ? is null or (dateOfBirth >= [T<d] and [T>d] > dateOfBirth) : 'You must be born today.' }";
        assertTrue(validate(new Person(new DefaultDateParser().parse("T")), text));
        assertFalse(validate(new Person(new DefaultDateParser().parse("T-1d")), text));
    }

    public void testTranslatorErrorKey() {
        MessageSourceAccessor mockAccessor = new MessageSourceAccessor(null) {
            public String getMessage(String code, String defaultMessage) {
                assertEquals("18_years_or_older", code);
                assertEquals("Customers must be 18 years or older.", defaultMessage);
                return defaultMessage;
            }
        };
        String text = "{ age : age >= 18 : 'Customers must be 18 years or older.' : '18_years_or_older' }";
        assertTrue(validate(new Person(30, "Steven"), text, mockAccessor));
        assertFalse(validate(new Person(7, "Benjamin"), text, mockAccessor));
    }

    public void testTranslatorErrorArgs() {
        MessageSourceAccessor mockAccessor = new MessageSourceAccessor(null) {
            public String getMessage(String code, String defaultMessage) {
                assertEquals("not_old_enough", code);
                assertEquals("Customers must be older than {0}.", defaultMessage);
                return defaultMessage;
            }
        };
        String text = "{ age : age >= minAge : 'Customers must be older than {0}.' : 'not_old_enough' : minAge }";
        assertTrue(validate(new Person(33, "Steven"), text, mockAccessor));
        assertFalse(validate(new Person(7, "Benjamin"), text, mockAccessor));
    }

    //
    // TODO: The following commented tests all fail.  
    //
    //    public void testTranslator23InCollection() {
    //        String text =
    //            "{ size : ? in @sizes and ? in @map[sizes] : 'Size is not correct.' }";
    //        ;
    //        assertTrue(validate(new Person("M"), text));
    //        assertFalse(validate(new Person("XXL"), text));
    //    }
    //    
    //    public void testTranslator23MapKey() {
    //        String text = 
    //            "{ firstName : ? = map[firstName] : 'First name is not correct.' }"
    //        ;
    //        assertTrue(validate(new Person(30, "Steven"), text));
    //        assertFalse(validate(new Person(30, "Marie-Claire"), text));
    //    }
    //    
    //    public void testTranslator25MapTarget() {
    //        Map customer = new HashMap();
    //        customer.put("name", "Steven");
    //        Map order = new HashMap();
    //        order.put("customer", customer);
    //        
    //        String text =
    //            "{ customer.name : ? = 'Steven' : 'Customer name is incorrect.' }"
    //        ;
    //        assertTrue(validate(order, text));
    //    }

    public void testTranslator26IsBlank() {
        String text = "{ firstName : ? is blank : 'First name is not blank.' }";
        assertTrue(validate(new Person(30, ""), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator27IsNotBlank() {
        String text = "{ firstName : ? is not blank : 'First name is blank.' }";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, ""), text));
    }

    public void testTranslator28IsWord() {
        String text = "{ firstName : ? is word : 'First name must be one word.' }";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "Steven Devijver"), text));
    }

    public void testTranslator29IsNotWord() {
        String text = "{ firstName : ? is not word : 'First name must not be one word.' }";
        assertTrue(validate(new Person(30, "Steven Devijver"), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator30IsUpperCase() {
        String text = "{ firstName : ? is uppercase : 'First name must be upper case.' }";
        assertTrue(validate(new Person(30, "STEVEN"), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator31IsNotUpperCase() {
        String text = "{ firstName : ? is not uppercase : 'First name must not be upper case.' }";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "STEVEN"), text));
    }

    public void testTranslator32IsLowerCase() {
        String text = "{ firstName : ? is lowercase : 'First name must be lower case.' }";
        assertTrue(validate(new Person(30, "steven"), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator33IsNotLowerCase() {
        String text = "{ firstName : ? is not lowercase : 'First name must not be lower case.' }";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "steven"), text));
    }

    public void testTranslator34SimpleMath() {
        String text = "{ firstName : (2 + 3 = 5 and 3 - 1 = 2) and (3 * 3 = 9 and 9 / 3 = 3) and 10 % 3 = 1 and 21 div 7 = 3 and 7 mod 3 = 1 : '' }";
        assertTrue(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator35ComplexMath() {
        String text = "{ firstName : 2 - 3 + 5 = 4 and (2 + 3) - 4 = 1 and ((2 - 3) + 4) - 1 = 2 and (length(?) - 2) + 1 = 5 and 2 = ((age / 2) / (1/2)) % 7 : '' }";
        assertTrue(validate(new Person(30, "Steven"), text));
    }

    public void testTranslator36EscapedString() {
        String text = "{firstName : 'Steven\\'' = firstName and matches('(Steven|Hans|Erwin)\\'', firstName) = true and length('\\\\') = 1 : ''}";
        assertTrue(validate(new Person(30, "Steven'"), text));
    }

    public void testTranslator37LengthWithNullValue() {
        String text = "{firstName : length(?) > 2: ''}";
        assertFalse(validate(new Person("Size'"), text));
    }

    public void testTranslator37Email() {
        Person person = new Person("size");
        person.setEmail("a@b.com");
        String text = "{email : email(?) == true: ''}";
        assertTrue(validate(person, text));
    }

    public void testTranslator37EmailWithNull() {
        Person person = new Person("size");
        String text = "{email : email(?) == true: ''}";
        assertFalse(validate(person, text));
    }

    public void testTranslator37InvalidEmail() {
        Person person = new Person("size");
        person.setEmail("-@b.com");
        String text = "{email : email(?) == true: ''}";
        assertFalse(validate(person, text));
    }
}