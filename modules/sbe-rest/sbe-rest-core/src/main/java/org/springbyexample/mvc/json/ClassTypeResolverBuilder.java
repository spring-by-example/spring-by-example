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

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;


/**
 * Jackson type resolver that includes class names for certain classes.
 *
 * @author David Winterfeldt
 */
public class ClassTypeResolverBuilder extends StdTypeResolverBuilder {

    private static final String CLASS_TYPE_PROPERTY = "__class";

    public ClassTypeResolverBuilder() {
        init(JsonTypeInfo.Id.CLASS, null);
        inclusion(JsonTypeInfo.As.PROPERTY);
        typeProperty(CLASS_TYPE_PROPERTY);
    }

    @Override
    public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType,
                                                  Collection<NamedType> subtypes) {
        return (isSupportedJsonClassType(baseType) ? super.buildTypeDeserializer(config, baseType, subtypes) : null);
    }

    @Override
    public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType,
                                              Collection<NamedType> subtypes) {
        return (isSupportedJsonClassType(baseType) ? super.buildTypeSerializer(config, baseType, subtypes) : null);
    }

    /**
     * Checks if the class type should be used.
     */
    private boolean isSupportedJsonClassType(JavaType baseType) {
        if (baseType.isArrayType() ||
            baseType.isCollectionLikeType() || baseType.isMapLikeType() ||
            java.util.Date.class.isAssignableFrom(baseType.getRawClass())) {
            return false;
        } else {
            return !baseType.isFinal();
        }
    }

}
