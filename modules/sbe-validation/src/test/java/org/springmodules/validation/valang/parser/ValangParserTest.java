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

package org.springmodules.validation.valang.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springmodules.validation.util.date.DateParseException;
import org.springmodules.validation.util.date.DefaultDateParser;
import org.springmodules.validation.valang.predicates.ValidationRule;


/**
 * @author Steven Devijver
 */
public class ValangParserTest extends TestCase {

    public ValangParserTest() {
        super();
    }

    public ValangParserTest(String arg0) {
        super(arg0);
    }

    private ValangParser getParser(String text) {
        return new ValangParser(new StringReader(text));
    }

    private Collection parseRules(String text) {
        try {
            return getParser(text).parseValidation();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validate(Object target, String text) {
        Collection rules = parseRules(text);
        Errors errors = new BindException(target, "person");

        Object tmpTarget = null;
        if (!(target instanceof Map)) {
            tmpTarget = new BeanWrapperImpl(target);
        } else {
            tmpTarget = target;
        }
        for (Iterator iter = rules.iterator(); iter.hasNext();) {
            ValidationRule rule = (ValidationRule) iter.next();
            rule.validate(tmpTarget, errors);
        }
        return !errors.hasErrors();
    }

    public void testParser1SimpleRule() {
        String text =
            "{age : age <= 120 : 'We do not do business with the undead.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
    }

    public void testParser2TwoSimpleRules() {
        String text =
            "{age : age >= 18 : 'Our customers must be 18 years or older.'}\n" +
                "{age : age is less than or equals 120 : 'We do not do business with the undead.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(150, "Steven"), text));
    }

    public void testParser3LengthRule() {
        String text =
            "{firstName : length (firstName) < 7 and length (firstName) > -1 : 'First name must be no longer than 30 characters.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(7, "Benjamin"), text));
    }

    public void testParser4NullRule() {
        String text = "{firstName : firstName is null : 'First name must be null.'}";
        assertTrue(validate(new Person(20, null), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testParser5NotNullRule() {
        String text = "{firstName : firstName is not null : 'First name must not be null.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(20, null), text));
    }

    public void testParser6HasLengthRule() {
        String text = "{firstName : firstName has length : 'First name is required.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(20, ""), text));
    }

    public void testParser7HasNoLengthRule() {
        String text =
            "{firstName : firstName has no length : 'First name is not allowed.'}";
        assertTrue(validate(new Person(20, null), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testParser8HasTextRule() {
        String text =
            "{firstName : firstName has text : 'First name is required.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(20, "    "), text));
    }

    public void testParser9HasNoTextRule() {
        String text =
            "{firstName : firstName has no text and !(false) = true : 'First name is not allowed.'}";
        assertTrue(validate(new Person(20, "   "), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testParser10NotRule() {
        String text =
            "{firstName : not length (firstName) > 7 : 'First name must be not longer than 7 characters.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(7, "Benjamin"), text));
    }

    public void testParser11ComplexNotRule() {
        String text =
            "{firstName : not length (firstName) > 7 or not(len (firstName) > 5 and len (firstName) > 7) : 'First name is not valid'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "Coraline"), text));
    }

    public void testParser12ComplexRule1() {
        String text =
            "{firstName : (length (firstName) > 5 and age <= 30) or (firstName has length and age > 20) : 'Arrggh!!'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertTrue(validate(new Person(30, "Marie-Claire"), text));
        assertFalse(validate(new Person(7, "test"), text));
    }

    // FIX ME: not working
//    public void testParser13InRule() {
//        String text = "{size : upper(?) in upper(lower('S')), upper(upper(lower(lower('M')))), upper('L'), 'XL' : 'Not a valid size.'}";
//        assertTrue(validate(new Person("M"), text));
//        assertFalse(validate(new Person("XXL"), text));
//    }

    public void testParser14NotInRule() {
        String text =
            "{firstName : firstName not in 'Bill', 'George' : 'We do not do business with Bill and George.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(60, "Bill"), text));
    }

    public void testParser15LengthBetweenRule() {
        String text =
            "{firstName : length (firstName) between 0 and 6 : 'First name is required and must be not longer than 6 characters.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "Marie-Claire"), text));
    }

    public void testParser15LengthIsBetweenRule() {
        String text =
            "{firstName : length (firstName) is between 0 and 6 : 'First name is required and must be not longer than 6 characters.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "Marie-Claire"), text));
    }

    public void testParser16LengthNotBetweenRule() {
        String text =
            "{firstName : firstName is null or length (firstName) not between 0 and 6 : 'First name must not have a length between 0 and 6.'}";
        assertTrue(validate(new Person(30, "Marie-Claire"), text));
        assertTrue(validate(new Person(20, null), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testParser16LengthIsNotBetweenRule() {
        String text =
            "{firstName : firstName is null or length (firstName) is not between 0 and 6 : 'First name must not have a length between 0 and 6.'}";
        assertTrue(validate(new Person(30, "Marie-Claire"), text));
        assertTrue(validate(new Person(20, null), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testParser17BetweenRule() {
        String text =
            "{age : age = 0 or age between 18 and 120 : 'Age must be between 18 and 120.'}";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(7, "Benjamin"), text));
    }

    public void testParser18NotBetweenRule() {
        String text =
            "{age : ? = 0 or age not between 20 and 30 : 'Age must not be between 20 and 30.'}";
        assertTrue(validate(new Person(7, "Benjamin"), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }


    public void testParser19Dates1() throws DateParseException {
        String text =
            "{ dateOfBirth : dateOfBirth >= [1970-01-01] : 'You must be born after 1 january 1970.' }";
        assertTrue(validate(new Person(new DefaultDateParser().parse("1974-11-24")), text));
        assertFalse(validate(new Person(new DefaultDateParser().parse("1950-07-14")), text));
    }

    public void testParser20Dates2() throws DateParseException {
        String text =
            "{ dateOfBirth : ? is null or (dateOfBirth >= [T<d] and [T>d] > dateOfBirth) : 'You must be born today.' }";
        assertTrue(validate(new Person(new DefaultDateParser().parse("T")), text));
        assertFalse(validate(new Person(new DefaultDateParser().parse("T-1d")), text));
    }

    public void testParser21ErrorKey() {
        String text =
            "{ age : age >= 18 : 'Customers must be 18 years or older.' : '18_years_or_older' }";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(7, "Benjamin"), text));
    }

    public void testParser22ErrorArgs() {
        String text =
            "{ age : ? >= minAge : 'Customers must be older than {0}.' : 'not_old_enough' : minAge }";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(7, "Benjamin"), text));
    }

    // FIX ME: not working
//    public void testParser23InCollection() {
//        String text = "{ size : ? in @sizes and ? in @map[sizes] : 'Size is not correct.' }";
//        assertTrue(validate(new Person("M"), text));
//        assertFalse(validate(new Person("XXL"), text));
//    }

    public void testParser23MapKey() {
        String text =
            "{ firstName : ? = map[firstName] : 'First name is not correct.' }";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "Marie-Claire"), text));
    }

    public void testParser25MapTarget() {
        Map customer = new HashMap();
        customer.put("name", "Steven");
        Map order = new HashMap();
        order.put("customer", customer);

        String text =
            "{ customer.name : ? = 'Steven' : 'Customer name is incorrect.' }";
        assertTrue(validate(order, text));
    }

    public void testParser26IsBlank() {
        String text =
            "{ firstName : ? is blank : 'First name is not blank.' }";
        assertTrue(validate(new Person(30, ""), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testParser27IsNotBlank() {
        String text =
            "{ firstName : ? is not blank : 'First name is blank.' }";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, ""), text));
    }

    public void testParser28IsWord() {
        String text =
            "{ firstName : ? is word : 'First name must be one word.' }";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "Steven Devijver"), text));
    }

    public void testParser29IsNotWord() {
        String text =
            "{ firstName : ? is not word : 'First name must not be one word.' }";
        assertTrue(validate(new Person(30, "Steven Devijver"), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testParser30IsUpperCase() {
        String text =
            "{ firstName : ? is uppercase : 'First name must be upper case.' }";
        assertTrue(validate(new Person(30, "STEVEN"), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testParser31IsNotUpperCase() {
        String text =
            "{ firstName : ? is not uppercase : 'First name must not be upper case.' }";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "STEVEN"), text));
    }

    public void testParser32IsLowerCase() {
        String text =
            "{ firstName : ? is lowercase : 'First name must be lower case.' }";
        assertTrue(validate(new Person(30, "steven"), text));
        assertFalse(validate(new Person(30, "Steven"), text));
    }

    public void testParser33IsNotLowerCase() {
        String text =
            "{ firstName : ? is not lowercase : 'First name must not be lower case.' }";
        assertTrue(validate(new Person(30, "Steven"), text));
        assertFalse(validate(new Person(30, "steven"), text));
    }

    public void testParser34SimpleMath() {
        String text =
            "{ firstName : (2 + 3 = 5 and 3 - 1 = 2) and (3 * 3 = 9 and 9 / 3 = 3) and 10 % 3 = 1 and 21 div 7 = 3 and 7 mod 3 = 1 : '' }";
        assertTrue(validate(new Person(30, "Steven"), text));
    }

    public void testParser35ComplexMath() {
        String text =
            "{ firstName : 2 - 3 + 5 = 4 and (2 + 3) - 4 = 1 and ((2 - 3) + 4) - 1 = 2 and (length(?) - 2) + 1 = 5 and 2 = ((age / 2) / (1/2)) % 7 : '' }";
        assertTrue(validate(new Person(30, "Steven"), text));
    }

    public void testParser36EscapedString() {
        String text =
            "{firstName : 'Steven\\'' = firstName and matches('(Steven|Hans|Erwin)\\'', firstName) = true and length('\\\\') = 1 : ''}";
        assertTrue(validate(new Person(30, "Steven'"), text));
    }

    public void testParser37LengthRuleWithCollection() {
        String text = "{tags: length(?) < 3 and length(?) > 1 : 'Number of tags is 2, therefore greater than 1 and less than 3'}";
        assertTrue(validate(new Person(30, "Steven"), text));
    }

    public void testParser38LengthRuleWithArray() {
        String text = "{sizes: length(?) == 4 : 'Number of sizes is 4'}";
        assertTrue(validate(new Person(30, "Steven"), text));
    }

    public void testParser39InRoleRule() {
        SecurityContext context = new SecurityContextImpl();
        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        roles.add(new GrantedAuthorityImpl("ADMIN_ROLE"));
        roles.add(new GrantedAuthorityImpl("USER_ROLE"));

        context.setAuthentication(new TestingAuthenticationToken("username", "username", roles));
        SecurityContextHolder.setContext(context);

        String text = "{firstName: inRole('USER_ROLE') == true : 'Current user must be in USER_ROLE'}";
        assertTrue(validate(new Person(30, "Steven"), text));
    }

    public void testParser40IsRule() {
        String text = "{age: age is 10 : 'age is 10'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertFalse(validate(new Person(20, "Uri"), text));
    }

    public void testParser41IsNotRule() {
        String text = "{age: age is not 5 : 'age is 10 so it is not 5'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertFalse(validate(new Person(5, "Uri"), text));
    }

    public void testParser42EqualsRule() {
        String text = "{age: age equals 10 : 'age equals 10'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertFalse(validate(new Person(20, "Uri"), text));
    }

    public void testParser43NotEqualsRule() {
        String text = "{age: age not equals 5 : 'age equals 10 so it is not 5'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertFalse(validate(new Person(5, "Uri"), text));
    }

    public void testParser44LessThanRule() {
        String text = "{age: age less than 20 : 'age is 10 so it must be less than 20'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertFalse(validate(new Person(20, "Uri"), text));
        assertFalse(validate(new Person(30, "Uri"), text));
    }

    public void testParser45IsLessThanRule() {
        String text = "{age: age less than 20 : 'age is 10 so it must be less than 20'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertFalse(validate(new Person(20, "Uri"), text));
        assertFalse(validate(new Person(30, "Uri"), text));
    }

    public void testParser46LessThanOrEqualsRule() {
        String text = "{age: age less than or equals 20 : 'age is 10 so it must be less than 20'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertTrue(validate(new Person(20, "Uri"), text));
        assertFalse(validate(new Person(30, "Uri"), text));
    }

    public void testParser47IsLessThanOrEqualsRule() {
        String text = "{age: age is less than or equals 20 : 'age is 10 so it must be less than 20'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertTrue(validate(new Person(20, "Uri"), text));
        assertFalse(validate(new Person(30, "Uri"), text));
    }

    public void testParser48GreaterThanRule() {
        String text = "{age: age greater than 5 : 'age is 10 so it must be greater than 5'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertFalse(validate(new Person(5, "Uri"), text));
        assertFalse(validate(new Person(3, "Uri"), text));
    }

    public void testParser49IsGreaterThanRule() {
        String text = "{age: age is greater than 5 : 'age is 10 so it must be greater than 5'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertFalse(validate(new Person(5, "Uri"), text));
        assertFalse(validate(new Person(3, "Uri"), text));
    }

    public void testParser50GreaterThanOrEqualsRule() {
        String text = "{age: age greater than or equals 5 : 'age is 10 so it must be greater than 5'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertTrue(validate(new Person(5, "Uri"), text));
        assertFalse(validate(new Person(3, "Uri"), text));
    }

    public void testParser51IsGreaterThanOrEqualsRule() {
        String text = "{age: age is greater than or equals 5 : 'age is 10 so it must be greater than 5'}";
        assertTrue(validate(new Person(10, "Uri"), text));
        assertTrue(validate(new Person(5, "Uri"), text));
        assertFalse(validate(new Person(3, "Uri"), text));
    }

    public void testParser52EmailRule() {
        String text = "{age: email(firstName) == true : 'first name is hello@world.com and it is a valid email'}";
        assertTrue(validate(new Person(10, "hello@world.com"), text));
    }

    public void testParser53ArrayAccess() {
        String text = "{sizes : length(sizes[3]) == 2 : 'length of XL is 2'}";
        assertTrue(validate(new Person(10, "Uri"), text));
    }

    public void testParser54ListAccess() {
        String text = "{tags : length(tags[1]) == 4 : 'length of tag2 is 4'}";
        assertTrue(validate(new Person(10, "Uri"), text));
    }

    // FIX ME: not working
//    public void testParser55UnicodeMessage() {
//        String text = "{tags : true == false :'\u4E20'}";
//        assertFalse(validate(new Person(10, "Uri"), text));
//    }

    public void testParser56MapAccess() {
        String text = "{map : map[firstName] EQUALS 'Steven' : 'name should be equal'}";
        assertTrue(validate(new Person(10, "Uri"), text));
    }

    public void testParser57MapAccessWithRadomTextAsKey() {
        String text = "{map : map[Test Key] EQUALS 'Value' : ''}";
        assertTrue(validate(new Person(10, "Uri"), text));
    }

    public void testParser58MapAccessWithRadomTextAsKey() {
        String text = "{map[Test Key] : ? is not null : ''}\n"
            + "{map[Test Key] : ? EQUALS 'Value' : ''}";
        assertTrue(validate(new Person(10, "Uri"), text));
    }
}
