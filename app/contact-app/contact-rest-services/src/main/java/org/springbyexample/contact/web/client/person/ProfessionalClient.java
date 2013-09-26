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
package org.springbyexample.contact.web.client.person;

import org.springbyexample.contact.web.client.AbstractPersistenceClient;
import org.springbyexample.contact.web.service.person.ProfessionalMarshallingService;
import org.springbyexample.mvc.rest.client.RestClient;
import org.springbyexample.schema.beans.person.Professional;
import org.springbyexample.schema.beans.person.ProfessionalFindResponse;
import org.springbyexample.schema.beans.person.ProfessionalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Professional client.
 *
 * @author David Winterfeldt
 */
@Component
public class ProfessionalClient extends AbstractPersistenceClient<ProfessionalResponse, ProfessionalFindResponse, Professional>
        implements ProfessionalMarshallingService {

    @Autowired
    public ProfessionalClient(RestClient client) {
        super(client,
              FIND_BY_ID_REQUEST, FIND_PAGINATED_REQUEST, FIND_REQUEST,
              SAVE_REQUEST, UPDATE_REQUEST, DELETE_PK_REQUEST, DELETE_REQUEST,
              ProfessionalResponse.class, ProfessionalFindResponse.class);
    }

}
