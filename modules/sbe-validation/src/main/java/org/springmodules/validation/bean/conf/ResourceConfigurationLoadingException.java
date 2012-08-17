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

package org.springmodules.validation.bean.conf;

import org.springframework.core.io.Resource;

/**
 * Thrown when a {@link org.springmodules.validation.bean.conf.loader.xml.AbstractResourceBasedBeanValidationConfigurationLoader} implementation fails to load
 * configurations from a resource.
 *
 * @author Uri Boness
 */
public class ResourceConfigurationLoadingException extends RuntimeException {

    private Resource resource;

    public ResourceConfigurationLoadingException(Resource resource) {
        super("Could not load configuration from resource '" + resource.getDescription() + "'");
        this.resource = resource;
    }

    public ResourceConfigurationLoadingException(Resource resource, Throwable cause) {
        super("Could not load configuration from resource '" + resource.getDescription() + "'", cause);
        this.resource = resource;
    }

    //=============================================== Setter/Getter ====================================================

    public Resource getResource() {
        return resource;
    }

}
