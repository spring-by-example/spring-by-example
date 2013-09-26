/*
 * Copyright 2007-2013 the original author or authors.
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
package org.springbyexample.mvc.json;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Jackson type resolver that includes class names for certain classes.
 *
 * @author David Winterfeldt
 */
public class RestObjectMapper extends ObjectMapper {

    // "December 19, 1989 03:24:00"
    public static final String PATTERN = "MMM dd, yyyy HH:mm:ss";

    public RestObjectMapper() {
//        registerModule(new JodaModule());

//        this.getDeserializerProvider().withAdditionalDeserializers(new JodaDeserializers.DateTimeDeserializer<ReadableInstant>(DateTime.class));
//        super.add addDeserializer(new JodaDeserializers());

//        SerializationConfig serConfig = getSerializationConfig();
//        serConfig.setDateFormat(new SimpleDateFormat(PATTERN));
//        DeserializationConfig deserializationConfig = getDeserializationConfig();
//        deserializationConfig.setDateFormat(new SimpleDateFormat(PATTERN));
//        configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

}
