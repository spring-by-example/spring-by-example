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
package org.springbyexample.web.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.Cache;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.mvc.test.AbstractProfileTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;

/**
 * Base class for REST controller tests.
 *
 * @author David Winterfeldt
 */
@ContextConfiguration({ "classpath:/org/springbyexample/web/mvc/rest-controller-test-context.xml" })
public abstract class AbstractRestControllerTest extends AbstractProfileTest {

    final Logger logger = LoggerFactory.getLogger(AbstractRestControllerTest.class);

    @Autowired
    private EmbeddedJetty embeddedJetty;

    /**
     * Reset the DB before each test.
     */
    @Override
    protected void doInit() {
        reset();
    }

    /**
     * Reset the database and entity manager cache.
     */
    protected void reset() {
        resetSchema();
        resetCache();

        logger.info("DB schema and entity manager cache reset.");
    }

    /**
     * Resets DB schema.
     */
    private void resetSchema() {
        ApplicationContext ctx = embeddedJetty.getApplicationContext();
        DataSource dataSource = ctx.getBean(DataSource.class);
        @SuppressWarnings("unchecked")
        List<Resource> databaseScripts = (List<Resource>) ctx.getBean("databaseScriptsList");

        Connection con = null;
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();

        try {
            con = dataSource.getConnection();

            resourceDatabasePopulator.setScripts(databaseScripts.toArray(new Resource[0]));

            resourceDatabasePopulator.populate(con);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try { con.close(); } catch (Exception e) {}
        }
    }

    /**
     * Reset cache.
     */
    private void resetCache() {
        ApplicationContext ctx = embeddedJetty.getApplicationContext();
        EntityManagerFactory entityManagerFactory = ctx.getBean(EntityManagerFactory.class);

        Cache cache = entityManagerFactory.getCache();

        if (cache != null) {
            cache.evictAll();
        }
    }

}
