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

package org.springmodules.validation.bean.context.web;

/**
 * Represents a mapping between a url pattern to a list of validation context tokens.
 *
 * @author Uri Boness
 */
public class ValidationContextUrlMapping {

    private String urlPattern;

    private String[] contextTokens;

    /**
     * Default contructor.
     */
    public ValidationContextUrlMapping() {
        this(null, null);
    }

    /**
     * Constructs a new mapping for the given url pattern to the given validation context tokens.
     *
     * @param urlPattern The given url pattern.
     * @param contextTokens The mapped validation context tokens.
     */
    public ValidationContextUrlMapping(String urlPattern, String[] contextTokens) {
        this.urlPattern = urlPattern;
        this.contextTokens = contextTokens;
    }


    //============================================== Setter/Getter =====================================================

    /**
     * Returns the url pattern.
     *
     * @return The url pattern.
     */
    public String getUrlPattern() {
        return urlPattern;
    }

    /**
     * Sets the url pattern for this mapping.
     *
     * @param urlPattern The url pattern for this mapping.
     */
    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    /**
     * Returns the validation context tokens of this mapping.
     *
     * @return The validation context tokens of this mapping.
     */
    public String[] getContextTokens() {
        return contextTokens;
    }

    /**
     * Sets the validation context tokens for this mapping.
     *
     * @param contextTokens The validation context tokens for this mapping.
     */
    public void setContextTokens(String[] contextTokens) {
        this.contextTokens = contextTokens;
    }

}
