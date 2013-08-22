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
package org.springbyexample.mvc.converter.handler;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springbyexample.converter.ListConverter;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * Converter handler interceptor for converting response results
 * after intial processing.
 *
 * @author David Winterfeldt
 */
public class ConverterHandlerInterceptor extends HandlerInterceptorAdapter {

    @SuppressWarnings("rawtypes")
    private final ListConverter converter;
    private final Class<?> returnTypeClass;
    private final Method getResultsMethod;
    private final Method setResultsMethod;

    public ConverterHandlerInterceptor(@SuppressWarnings("rawtypes") ListConverter converter,
                                       Class<?> returnTypeClass, Method getResultsMethod, Method setResultsMethod) {
        Assert.notNull(converter);
        Assert.notNull(returnTypeClass);
        Assert.notNull(getResultsMethod);
        Assert.notNull(setResultsMethod);

        this.converter = converter;
        this.returnTypeClass = returnTypeClass;
        this.getResultsMethod = getResultsMethod;
        this.setResultsMethod = setResultsMethod;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        Map<String, Object> model = modelAndView.getModel();

        for (String key : model.keySet()) {
            Object value = model.get(key);

            // find model value that matches service return type
            if (value.getClass().isAssignableFrom(returnTypeClass)) {
                Object results = ReflectionUtils.invokeMethod(getResultsMethod, value);

                Object convertedValue = null;

                if (!(results instanceof Collection)) {
                    convertedValue = converter.convertTo(results);
                } else {
                    convertedValue = converter.convertListTo((Collection) results);
                }

                ReflectionUtils.invokeMethod(setResultsMethod, value, convertedValue);

                break;
            }
        }
    }

}
