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

package org.springmodules.validation.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;

/**
 * @author Daniel Miller
 * @author Rob Harrop
 */
public abstract class MessageUtils {

    /**
     * Gets the <code>ActionError</code> based on the <code>ValidatorAction</code> message and the <code>Field</code>'s
     * arg objects.
     * <p/>
     */
    public static String getMessageKey(ValidatorAction va, Field field) {
        return (field.getMsg(va.getName()) != null ? field.getMsg(va.getName())
            : va.getMsg());
    }

    /**
     * <p/>
     * Gets the message arguments based on the current
     * <code>ValidatorAction</code> and <code>Field</code>. The array
     * returned here is an array of MessageSourceResolvable's that will be
     * resolved at a later time.
     * </p>
     * <p/>
     * <p/>
     * Note: this implementation is especially crappy (only four arguments are
     * supported), but it's the best we can do until the next version of
     * validator-validator is out of beta.
     * </p>
     * <p/>
     * -param actionName
     * action name.
     *
     * @param field the validator field.
     * @return array of message keys.
     */
    public static Object[] getArgs(ValidatorAction va, Field field) {

        List args = new ArrayList();
        String actionName = va.getName();

        if (field.getArg(actionName, 0) != null) {
            args.add(0, MessageUtils.getMessage(field.getArg(actionName, 0)));
        }

        if (field.getArg(actionName, 1) != null) {
            args.add(1, MessageUtils.getMessage(field.getArg(actionName, 1)));
        }

        if (field.getArg(actionName, 2) != null) {
            args.add(2, MessageUtils.getMessage(field.getArg(actionName, 2)));
        }

        if (field.getArg(actionName, 3) != null) {
            args.add(3, MessageUtils.getMessage(field.getArg(actionName, 3)));
        }

        return args.toArray();
    }

    /**
     * Get the message associated with the argument. If the resource flag is set
     * to false, use the text specified in the argument key directly. Otherwise,
     * create a MessageSourceResolvable with the argument key as its code.
     */
    public static Object getMessage(Arg arg) {
        if (arg.isResource()) {
            return MessageUtils.createMessage(arg.getKey());
        } else {
            return arg.getKey();
        }
    }

    /**
     * Create a MessageSourceResolvable using the string value of the parameter
     * as a code.
     * <p/>
     * <p/>
     * Note: this implementation uses the key of the Fields message for the
     * given ValidatorAction as the default message.
     * </p>
     *
     * @param obj Object whose string value is the code for this message.
     * @return MessageSourceResolvable for the given Object.
     */
    public static MessageSourceResolvable createMessage(Object obj) {
        String[] codes = new String[]{String.valueOf(obj)};
        String defaultMsg = codes[0];
        return new DefaultMessageSourceResolvable(codes, null, defaultMsg);
    }

    /**
     * Get a message for the given validator action and field from the specified
     * message source.
     * <p/>
     * <p/>
     * Note: this implementation uses the key of the Fields message for the
     * given ValidatorAction as the default message.
     * </p>
     *
     * @param messages MessageSource from which to get the message.
     * @param locale Locale for for this message.
     * @param va ValidatorAction for this message.
     * @param field Field field for this message.
     */
    public static String getMessage(MessageSource messages, Locale locale,
                                    ValidatorAction va, Field field) {
        String code = MessageUtils.getMessageKey(va, field);
        Object[] args = MessageUtils.getArgs(va, field);
        return messages.getMessage(code, args, code, locale);
    }
}
