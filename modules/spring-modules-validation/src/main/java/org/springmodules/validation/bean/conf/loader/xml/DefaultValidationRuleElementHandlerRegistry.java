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

package org.springmodules.validation.bean.conf.loader.xml;

import org.springmodules.validation.bean.conf.loader.xml.handler.*;
import org.springmodules.validation.bean.conf.loader.xml.handler.jodatime.InstantInFutureRuleElementHandler;
import org.springmodules.validation.bean.conf.loader.xml.handler.jodatime.InstantInPastRuleElementHandler;
import org.springmodules.validation.util.LibraryUtils;

/**
 * A {@link SimpleValidationRuleElementHandlerRegistry} that already registers the following default element handlers:
 * <p/>
 * <ol>
 * <li>{@link NotNullRuleElementHandler}</li>
 * <li>{@link LengthRuleElementHandler}</li>
 * <li>{@link NotBlankRuleElementHandler}</li>
 * <li>{@link EmailRuleElementHandler}</li>
 * <li>{@link RegExpRuleElementHandler}</li>
 * <li>{@link SizeRuleElementHandler}</li>
 * <li>{@link NotEmptyRuleElementHandler}</li>
 * <li>{@link NotBlankRuleElementHandler}</li>
 * <li>{@link RangeRuleElementHandler}</li>
 * <li>{@link ExpressionPropertyValidationElementHandler}</li>
 * <li>{@link DateInPastRuleElementHandler}</li>
 * <li>{@link DateInFutureRuleElementHandler}</li> *
 * <li>{@link InstantInFutureRuleElementHandler} (only if joda-time library is available in the classpath)</li> *
 * <li>{@link InstantInPastRuleElementHandler} (only if joda-time library is available in the classpath)</li> *
 * </ol>
 *
 * @author Uri Boness
 */
public class DefaultValidationRuleElementHandlerRegistry extends SimpleValidationRuleElementHandlerRegistry {

    /**
     * Constructs a new DefaultValidationRuleElementHandlerRegistry with the default handlers.
     */
    public DefaultValidationRuleElementHandlerRegistry() {

        String namepsaceUri = DefaultXmlBeanValidationConfigurationLoader.DEFAULT_NAMESPACE_URL;

        // registering class handlers
        registerClassHandler(new ExpressionClassValidationElementHandler(namepsaceUri));

        // registering property handlers
        registerPropertyHandler(new NotNullRuleElementHandler(namepsaceUri));
        registerPropertyHandler(new LengthRuleElementHandler(namepsaceUri));
        registerPropertyHandler(new EmailRuleElementHandler(namepsaceUri));
        registerPropertyHandler(new RegExpRuleElementHandler(namepsaceUri));
        registerPropertyHandler(new SizeRuleElementHandler(namepsaceUri));
        registerPropertyHandler(new NotEmptyRuleElementHandler(namepsaceUri));
        registerPropertyHandler(new NotBlankRuleElementHandler(namepsaceUri));
        registerPropertyHandler(new RangeRuleElementHandler(namepsaceUri));
        registerPropertyHandler(new ExpressionPropertyValidationElementHandler(namepsaceUri));
        registerPropertyHandler(new DateInPastRuleElementHandler(namepsaceUri));
        registerPropertyHandler(new DateInFutureRuleElementHandler(namepsaceUri));
        registerPropertyHandler(new ConditionReferenceRuleElementHandler(namepsaceUri));
        if (LibraryUtils.JODA_TIME_IN_CLASSPATH) {
            registerPropertyHandler(new InstantInPastRuleElementHandler(namepsaceUri));
            registerPropertyHandler(new InstantInFutureRuleElementHandler(namepsaceUri));
        }
    }

}
