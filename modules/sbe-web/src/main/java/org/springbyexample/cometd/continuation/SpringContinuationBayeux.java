/*
 * Copyright 2007-2014 the original author or authors.
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
package org.springbyexample.cometd.continuation;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.cometd.DataFilter;
import org.mortbay.cometd.continuation.ContinuationBayeux;
import org.mortbay.cometd.continuation.ContinuationCometdServlet;
import org.mortbay.cometd.filter.JSONDataFilter;
import org.mortbay.util.ajax.JSON;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;

/**
 * A subclass of {@link ContinuationBayeux} that allows
 * full initialization of bayeux outside the {@link ContinuationCometdServlet}.
 *
 * @author David Winterfeldt
 */
public class SpringContinuationBayeux extends ContinuationBayeux implements ServletContextAware {

    protected ServletContext servletContext = null;

    protected String filters = null;
    protected long maxInterval  = 10000;

    /**
     * Implementation of <code>ServletContextAware</code>.
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Gets filters as JSON objects.
     */
    public String getFilters() {
        return filters;
    }

    /**
     * Sets filters as JSON objects.
     */
    public void setFilters(String filters) {
        this.filters = filters;
    }

    /**
     * <p>Sets max interval.</p>
     *
     * <p><strong>Note</strong>: Overriding parent because it sets value on
     * internal instance before initialization.</p>
     */
    @Override
    public void setMaxInterval(long maxInterval) {
        this.maxInterval = maxInterval;
    }


    /**
     * Initializes bayeux.
     */
    @PostConstruct
    public void init() {
        this.initialize(servletContext);

        super.setMaxInterval(maxInterval);

        // based on AbstractCometdServlet.init()
        if (filters != null) {
            try {
                Object[] objects = (Object[]) JSON.parse(new StringReader(filters));

                for (int i = 0; objects != null && i < objects.length; i++) {
                    Map<?, ?> filter_def = (Map<?, ?>) objects[i];

                    String className = (String) filter_def.get("filter");
                    DataFilter filter = (DataFilter) ClassUtils.forName(className, ClassUtils.getDefaultClassLoader()).newInstance();

                    if (filter instanceof JSONDataFilter)
                        ((JSONDataFilter) filter).init(filter_def.get("init"));

                    getChannel((String) filter_def.get("channels"), true).addDataFilter(filter);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Unable to initialize bayuex filters.  " + e.getMessage());
            } catch (InstantiationException e) {
                throw new IllegalArgumentException("Unable to initialize bayuex filters.  " + e.getMessage());
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Unable to initialize bayuex filters.  " + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Unable to initialize bayuex filters.  " + e.getMessage());
            } catch (LinkageError e) {
                throw new IllegalArgumentException("Unable to initialize bayuex filters.  " + e.getMessage());
            }
        }
    }

}
