/*
 * Copyright 2007-2012 the original author or authors.
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
package org.springbyexample.web.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.response.FindResponseResult;
import org.springbyexample.schema.beans.response.ResponseResult;
import org.springbyexample.web.service.PersistenceFindMarshallingService;


/**
 * Abstract persistence find client.
 * 
 * @author David Winterfeldt
 * 
 *  @param   <R>      Generic response.
 *  @param   <FR>     Find response.
 */
public abstract class AbstractPersistenceFindClient<R extends ResponseResult, FR extends FindResponseResult> 
        implements PersistenceFindMarshallingService<R, FR> {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected final RestClient client;
    
    private final String findByIdRequest;
    private final String findPaginatedRequest;
    private final String findRequest;
    protected final Class<R> responseClazz;
    protected final Class<FR> findResponseClazz;
    

    public AbstractPersistenceFindClient(RestClient client,
                                         String findByIdRequest, String findPaginatedRequest, String findRequest,
                                         Class<R> responseClazz, Class<FR> findResponseClazz) {
        this.client = client;
        this.findByIdRequest = findByIdRequest;
        this.findPaginatedRequest = findPaginatedRequest;
        this.findRequest = findRequest;
        this.responseClazz = responseClazz;
        this.findResponseClazz = findResponseClazz;
    }

    @Override
    public R findById(long id) {
        R response = null;
        
        String url = client.createUrl(findByIdRequest);
        
        logger.debug("REST client findById.  id={}  url='{}'", id, url);
        
        Map<String, Long> vars = Collections.singletonMap(ID_VAR, id);
        
        response = client.getRestTemplate().getForObject(url, responseClazz, vars);
        
        return response;
    }

    @Override
    public FR find(int page, int pageSize) {
        FR response = null;
        
        String url = client.createUrl(findPaginatedRequest);
        
        logger.debug("REST client paginated find.  page={}  pageSize={}  url='{}'", 
                     new Object[] { page, pageSize, url});

        response = client.getRestTemplate().getForObject(url, findResponseClazz, createPageVars(page, pageSize));
        
        return response;
    }

    @Override
    public FR find() {
        FR response = null;
        
        String url = client.createUrl(findRequest);
        
        logger.debug("REST client find. url='{}'", url);

        response = client.getRestTemplate().getForObject(url, findResponseClazz);
        
        return response;
    }

    /**
     * Create page vars for a paginated request.
     */
    public Map<String, Integer> createPageVars(int page, int pageSize) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        
        result.put(PAGE_VAR, page);
        result.put(PAGE_SIZE_VAR, pageSize);
        
        return result;
    }

}
