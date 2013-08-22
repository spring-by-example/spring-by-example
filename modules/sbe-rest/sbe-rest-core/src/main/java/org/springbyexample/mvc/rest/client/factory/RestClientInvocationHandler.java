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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.mvc.rest.client.RestClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestClientException;


/**
 * REST client <code>InvocationHandler</code>.
 *
 * @author David Winterfeldt
 */
public class RestClientInvocationHandler implements InvocationHandler {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final RestClient client;
    private final Map<Method, ClientRequestHandlerInfo> methodRequestHandlers;
//    private final String primaryKeyPropertyName = "id";

    public RestClientInvocationHandler(RestClient client,
                                       Map<Method, ClientRequestHandlerInfo> methodRequestHandlers) {
        this.client = client;
        this.methodRequestHandlers = methodRequestHandlers;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object invoke(Object arg0, Method method, Object[] args) throws Throwable {
        // some BeanPostProcessor's check if a bean is managed by an internal Map
        if ("hashCode".equals(method.getName())) {
            return methodRequestHandlers.hashCode();
        }

        ClientRequestHandlerInfo clientRequestHandlerInfo = methodRequestHandlers.get(method);
        RequestMethod httpMethod = clientRequestHandlerInfo.getMethod();

        String uri = client.createUrl(clientRequestHandlerInfo.getPattern());
        Map<String, Object> urlVariables = new HashMap<String, Object>();
        Object request = null;

        if (args != null && args.length > 0) {
            Assert.isTrue((args.length ==  method.getParameterAnnotations().length),
                          "Args and method param annotation length must match.");

            for (int i = 0; i < method.getParameterAnnotations().length; i++) {
                Annotation[] annotations = method.getParameterAnnotations()[i];

                // expecting just one method param annotation
//                if (!RequestMethod.DELETE.equals(httpMethod)) {
                    Assert.notEmpty(annotations, "Must be just one annotation on the method param.");
//                }

                Annotation annotation = (annotations.length > 0 ? annotations[0] : null);

//                if (!RequestMethod.DELETE.equals(httpMethod)) {
                    Assert.isTrue(((annotation instanceof PathVariable) || (annotation instanceof RequestParam) ||
                                   (annotation instanceof RequestBody)),
                                  "Method param annotation must be PathVariable || RequestParam || RequestBody.");
//                }

                String key = null;

                if (annotation instanceof PathVariable) {
                    key = ((PathVariable) annotation).value();
                } else if (annotation instanceof RequestParam) {
                    key = ((RequestParam) annotation).value();
                } else {
                    request = args[i];
                }

                // might be null if RequestBody
                if (key != null) {
                    urlVariables.put(key, args[i]);
                }
            }
        }

        logger.debug("REST client call. url='{}'  httpMethod={}", uri, httpMethod);

        Class<?> responseClazz = clientRequestHandlerInfo.getResponseClazz();

        switch (httpMethod) {
            case POST: {
                return client.getRestTemplate().postForObject(uri, request, responseClazz, urlVariables);
            } case PUT: {
                return client.getRestTemplate().exchange(uri, HttpMethod.PUT, new HttpEntity(request), responseClazz, urlVariables).getBody();
            } case DELETE: {
                return client.getRestTemplate().exchange(uri, HttpMethod.DELETE, new HttpEntity(request), responseClazz, urlVariables).getBody();

//                HttpEntityRequestCallback requestCallback = new HttpEntityRequestCallback(request, responseClazz);
//                HttpMessageConverterExtractor<ResponseResult> responseExtractor =
//                                new HttpMessageConverterExtractor<ResponseResult>(responseClazz, client.getRestTemplate().getMessageConverters());
//                return client.getRestTemplate().execute(uri, HttpMethod.DELETE, requestCallback, responseExtractor, urlVariables);
            } default: {
                if (urlVariables.isEmpty()) {
                    return client.getRestTemplate().getForObject(uri, responseClazz);
                } else {
                    return client.getRestTemplate().getForObject(uri, responseClazz, urlVariables);
                }
            }
        }
    }

    /**
     * Request callback implementation that prepares the request's accept
     * headers.
     *
     * <strong>Note</strong>: Copied from {@code RestTemplate}.
     */
    private class AcceptHeaderRequestCallback implements RequestCallback {
        private final Class<?> responseType;

        private AcceptHeaderRequestCallback(Class<?> responseType) {
            this.responseType = responseType;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            if (responseType != null) {
                List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
                for (HttpMessageConverter<?> messageConverter : client.getRestTemplate().getMessageConverters()) {
                    if (messageConverter.canRead(responseType, null)) {
                        List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
                        for (MediaType supportedMediaType : supportedMediaTypes) {
                            if (supportedMediaType.getCharSet() != null) {
                                supportedMediaType = new MediaType(supportedMediaType.getType(),
                                        supportedMediaType.getSubtype());
                            }
                            allSupportedMediaTypes.add(supportedMediaType);
                        }
                    }
                }
                if (!allSupportedMediaTypes.isEmpty()) {
                    MediaType.sortBySpecificity(allSupportedMediaTypes);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Setting request Accept header to " + allSupportedMediaTypes);
                    }
                    request.getHeaders().setAccept(allSupportedMediaTypes);
                }
            }
        }
    }

    /**
     * Request callback implementation that writes the given object to the
     * request stream.
     *
     * <strong>Note</strong>: Copied from {@code RestTemplate}.
     */
    private class HttpEntityRequestCallback extends AcceptHeaderRequestCallback {
        private final HttpEntity requestEntity;

        private HttpEntityRequestCallback(Object requestBody) {
            this(requestBody, null);
        }

        @SuppressWarnings("unchecked")
        private HttpEntityRequestCallback(Object requestBody, Class<?> responseType) {
            super(responseType);
            if (requestBody instanceof HttpEntity) {
                this.requestEntity = (HttpEntity) requestBody;
            } else if (requestBody != null) {
                this.requestEntity = new HttpEntity(requestBody);
            } else {
                this.requestEntity = HttpEntity.EMPTY;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
            super.doWithRequest(httpRequest);
            if (!requestEntity.hasBody()) {
                HttpHeaders httpHeaders = httpRequest.getHeaders();
                HttpHeaders requestHeaders = requestEntity.getHeaders();
                if (!requestHeaders.isEmpty()) {
                    httpHeaders.putAll(requestHeaders);
                }
                if (httpHeaders.getContentLength() == -1) {
                    httpHeaders.setContentLength(0L);
                }
            } else {
                Object requestBody = requestEntity.getBody();
                Class<?> requestType = requestBody.getClass();
                HttpHeaders requestHeaders = requestEntity.getHeaders();
                MediaType requestContentType = requestHeaders.getContentType();
                for (HttpMessageConverter messageConverter : client.getRestTemplate().getMessageConverters()) {
                    if (messageConverter.canWrite(requestType, requestContentType)) {
                        if (!requestHeaders.isEmpty()) {
                            httpRequest.getHeaders().putAll(requestHeaders);
                        }
                        if (logger.isDebugEnabled()) {
                            if (requestContentType != null) {
                                logger.debug("Writing [" + requestBody + "] as \"" + requestContentType + "\" using ["
                                        + messageConverter + "]");
                            } else {
                                logger.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
                            }
                        }
                        messageConverter.write(requestBody, requestContentType, httpRequest);
                        return;
                    }
                }
                String message = "Could not write request: no suitable HttpMessageConverter found for request type ["
                        + requestType.getName() + "]";
                if (requestContentType != null) {
                    message += " and content type [" + requestContentType + "]";
                }

                throw new RestClientException(message);
            }
        }
    }

}
