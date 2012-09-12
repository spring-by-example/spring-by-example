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
package org.springbyexample.orm.dialect;

import java.util.Properties;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.type.Type;

/**
 * Modified PostgreSQL dialect that works that uses 
 * the default generated sequences for each table (SERIAL column type).
 * 
 * @author David Winterfeldt
 * 
 * @see    <a href="http://grails.1312388.n4.nabble.com/One-hibernate-sequence-is-used-for-all-Postgres-tables-td1351722.html"/>
 */
public class TableNameSequencePostgreSQLDialect extends PostgreSQL82Dialect {

    @Override
    public Class<?> getNativeIdentifierGeneratorClass() {
        return TableNameSequenceGenerator.class;
    }

    public static class TableNameSequenceGenerator extends SequenceGenerator {
        @Override
        public void configure(final Type type, final Properties params, final Dialect dialect) {
            if (params.getProperty(SEQUENCE) == null || params.getProperty(SEQUENCE).length() == 0) {
                StringBuilder sb = new StringBuilder();
                
                // default sequence for a table with SERIAL/BIGSERIAL is  
                // table name plus '_id_seq'
                //      ex: contact_id_seq
                String tableName = params.getProperty(PersistentIdentifierGenerator.TABLE);
                
                sb.append(tableName);
                sb.append("_id_seq");
                
                if (tableName != null) {
                    params.setProperty(SEQUENCE, sb.toString());
                }
            }
            
            super.configure(type, params, dialect);
        }
    }

}
