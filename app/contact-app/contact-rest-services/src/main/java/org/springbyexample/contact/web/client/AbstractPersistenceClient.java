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
package org.springbyexample.contact.web.client;

import java.util.Map;

import org.springbyexample.mvc.rest.client.RestClient;
import org.springbyexample.mvc.rest.service.PersistenceMarshallingService;
import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.response.EntityFindResponseResult;
import org.springbyexample.schema.beans.response.EntityResponseResult;
import org.springframework.http.HttpEntity;
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
public abstract class AbstractPersistenceClient<R extends EntityResponseResult, FR extends EntityFindResponseResult, S extends PkEntityBase>
        extends AbstractPersistenceFindClient<R, FR>
        implements PersistenceMarshallingService<R, FR, S> {

    private final String saveRequest;
    private final String updateRequest;
    private final String deletePkRequest;
    private final String deleteRequest;

    public AbstractPersistenceClient(RestClient client,
                                     String findByIdRequest, String findPaginatedRequest, String findRequest,
                                     String saveRequest, String updateRequest,
                                     String deletePkRequest, String deleteRequest,
                                     Class<R> responseClazz, Class<FR> findResponseClazz) {
        super(client,
              findByIdRequest, findPaginatedRequest, findRequest,
              responseClazz, findResponseClazz);

        this.saveRequest = saveRequest;
        this.updateRequest = updateRequest;
        this.deletePkRequest = deletePkRequest;
        this.deleteRequest = deleteRequest;
    }

    @Override
    public R create(S request) {
        R response = null;

        String url = client.createUrl(saveRequest);

        logger.debug("REST client save.  id={}  url='{}'", request.getId(), url);

        response =  client.getRestTemplate().postForObject(url, request, responseClazz);

        return response;
    }

    @Override
    public R update(S request) {
        R response = null;

        String url = client.createUrl(updateRequest);

        logger.debug("REST client update.  id={}  url='{}'", request.getId(), url);

        Map<String, Long> vars = createPkVars(request.getId());

        response = client.getRestTemplate().exchange(url, HttpMethod.PUT, new HttpEntity(request), responseClazz, vars).getBody();

        return response;
    }

    public R delete(Integer id) {
        R response = null;

        String url = client.createUrl(deletePkRequest);

        logger.debug("REST client delete.  id={}  url='{}'", id, url);

        Map<String, Long> vars = createPkVars(id);

        response = client.getRestTemplate().exchange(url, HttpMethod.DELETE, null, responseClazz, vars).getBody();

        return response;
    }

    @Override
    public R delete(S request) {
        throw new UnsupportedOperationException("Issue with DELETE and posting body.");

//        R response = null;
//
//        String url = client.createUrl(deleteRequest);
//
//        int id = request.getId();
//
//        logger.debug("REST client delete.  id={}  url='{}'", id, url);
//
//        // FIXME: problem with DELETE
////        response = client.getRestTemplate().exchange(url, HttpMethod.DELETE, null, responseClazz).getBody();
//        response = client.getRestTemplate().exchange(url, HttpMethod.PUT, new HttpEntity(request), responseClazz).getBody();
//
//        return response;
    }

}
