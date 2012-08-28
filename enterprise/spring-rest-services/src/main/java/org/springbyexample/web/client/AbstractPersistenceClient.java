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
import java.util.Map;

import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.response.FindResponseResult;
import org.springbyexample.schema.beans.response.ResponseResult;
import org.springbyexample.web.service.PersistenceMarshallingService;
import org.springframework.http.HttpMethod;


/**
 * Abstract persistence client.
 * 
 * @author David Winterfeldt
 *
 *  @param   <R>      Generic response.
 *  @param   <FR>     Find response.
 *  @param   <S>      Save request.
*/
public abstract class AbstractPersistenceClient<R extends ResponseResult, FR extends FindResponseResult, S extends PkEntityBase>
        extends AbstractPersistenceFindClient<R, FR>
        implements PersistenceMarshallingService<R, FR, S> {

    private final String saveRequest;
    private final String deleteRequest;

    public AbstractPersistenceClient(RestClient client,
                                     String findByIdRequest, String findPaginatedRequest, String findRequest,
                                     String saveRequest, String deleteRequest,
                                     Class<R> responseClazz, Class<FR> findResponseClazz) {
        super(client, 
              findByIdRequest, findPaginatedRequest, findRequest,
              responseClazz, findResponseClazz);
        
        this.saveRequest = saveRequest;
        this.deleteRequest = deleteRequest;
    }

    @Override
    public R save(S request) {
        R response = null;
        
        String url = client.createUrl(saveRequest);
        
        logger.debug("REST client save.  id={}  url='{}'", request.getId(), url);

        response =  client.getRestTemplate().postForObject(url, request, responseClazz);
        
        return response;
    }

    @Override
    public ResponseResult delete(long id) {
        ResponseResult response = null;
        
        String url = client.createUrl(deleteRequest);
        
        logger.debug("REST client delete.  id={}  url='{}'", id, url);
        
        Map<String, Long> vars = Collections.singletonMap(ID_VAR, id);
        
        response = client.getRestTemplate().exchange(url, HttpMethod.DELETE, null, ResponseResult.class, vars).getBody();

        return response;
    }

}
