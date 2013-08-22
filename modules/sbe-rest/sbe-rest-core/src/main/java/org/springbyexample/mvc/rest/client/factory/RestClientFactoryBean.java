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
package org.springbyexample.mvc.rest.client.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.mvc.bind.annotation.RestRequestResource;
import org.springbyexample.mvc.bind.annotation.RestResource;
import org.springbyexample.mvc.rest.client.RestClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethodSelector;


/**
 * Creates a REST client bean instances from type and method-level
 * {@link RequestMapping @RequestMapping} annotations in
 * {@link @RestResource @RestResource} classes.
 *
 * @author David Winterfeldt
 */
public class RestClientFactoryBean {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final RestClient client;
    private Class<?> marshallingServiceClass;

    private Object bean;

    @Autowired
    public RestClientFactoryBean(RestClient client) {
        this.client = client;
    }

    public void setMarshallingServiceClass(Class<?> marshallingServiceClass) {
        this.marshallingServiceClass = marshallingServiceClass;
    }

    public Object getBean() throws BeansException {
        logger.info("########## {} ######", marshallingServiceClass);

        if (bean == null) {
            RestResource restResource = AnnotationUtils.findAnnotation(marshallingServiceClass, RestResource.class);
            Class<?> serviceClass = restResource.service();

            Map<Method, ClientRequestHandlerInfo> methodRequestHandlers = new HashMap<Method, ClientRequestHandlerInfo>();

            Set<Method> methods = HandlerMethodSelector.selectMethods(marshallingServiceClass, new MethodFilter() {
                @Override
                public boolean matches(Method method) {
                    return (AnnotationUtils.findAnnotation(method, RequestMapping.class) != null);
                }
            });

            for (Method interfaceMethod : methods) {
                Method method = BridgeMethodResolver.findBridgedMethod(interfaceMethod);
                Class<?> responseClazz = method.getReturnType();

                RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
                RestRequestResource restRequestResource = AnnotationUtils.findAnnotation(method, RestRequestResource.class);
                boolean export = (restRequestResource != null ? restRequestResource.export() : true);

                // if the method declaring class is in the parent, get the return type from the service or converter (because of erasure)
                if (!marshallingServiceClass.equals(method.getDeclaringClass())) {
                    String restRequestResourceMethodName = (restRequestResource != null ? restRequestResource.methodName() : null);
                    String methodName = (StringUtils.hasText(restRequestResourceMethodName) ? restRequestResourceMethodName : method.getName());
                    Class<?>[] paramTypes = method.getParameterTypes();

                    try {
                        Method serviceMethod = ReflectionUtils.findMethod(serviceClass, methodName, paramTypes);
                        responseClazz = serviceMethod.getReturnType();
                    } catch(IllegalStateException e) {
                    }
                }

                if (export) {
                    boolean relative = (restRequestResource != null ? restRequestResource.relative() : true);

                    // just get first param if more than one
                    StringBuilder uri = new StringBuilder();

                    if (relative) {
                        for (String pattern : requestMapping.value()) {
                            // add REST resource path prefix to URI,
                            // if relative path is just '/' add an empty string
                            uri.append(restResource.path());
                            uri.append((!"/".equals(pattern) ? pattern : ""));

                            break;
                        }
                    } else {
                        uri.append(requestMapping.value()[0]);
                    }

                    String[] params = requestMapping.params();

                    if (params != null && params.length > 0) {
                        uri.append("?");

                        for (int i = 0; i < params.length; i++) {
                            String param = params[i];

                            uri.append(param);
                            uri.append("={");
                            uri.append(param);
                            uri.append("}");

                            if (i < (params.length - 1)) {
                                uri.append("&");
                            }
                        }
                    }

                    RequestMethod requestMethod = requestMapping.method()[0];

                    if (RequestMethod.POST.equals(requestMethod) || RequestMethod.PUT.equals(requestMethod)
                            || RequestMethod.DELETE.equals(requestMethod)) {
                        responseClazz = restResource.responseClass();
                    }
//                    else if (RequestMethod.DELETE.equals(requestMethod)) {
//                        responseClazz = ResponseResult.class;
//                    }

                    ClientRequestHandlerInfo clientRequestHandlerInfo =
                            new ClientRequestHandlerInfo(uri.toString(), responseClazz, requestMethod);

                    methodRequestHandlers.put(method, clientRequestHandlerInfo);
                } else {
                    // configure to throw UnsupportedOperationException
                }

            }

            bean = Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), new Class[] { marshallingServiceClass },
                                          new RestClientInvocationHandler(client, methodRequestHandlers));
        }

        return bean;
    }

}
