#!/bin/bash

# can add '*:*:*:contact:contact' to ~/.pgpass to not enter password
# psql either needs to be in the path or set POSTGRES_HOME

if [ -z $POSTGRES_HOME ]; then
  PSQL=`which psql`
else
  PSQL=$POSTGRES_HOME/bin/psql
fi

$PSQL -U postgres -f contact-dao/src/main/resources/sql/postgres/create_database.sql

result=$?
if [ $result -ne 0 ]
then
   echo "Create Database script unsuccessful: " $result
   exit $result
fi

$PSQL -U postgres -f contact-dao/src/main/resources/sql/postgres/schema.sql

result=$?
if [ $result -ne 0 ]
then
   echo "Schema creation script unsuccessful: " $result
   exit $result
fi

$PSQL -U postgres -f contact-dao/src/main/resources/sql/postgres/security_schema.sql

result=$?
if [ $result -ne 0 ]
then
   echo "Security Schema creation script unsuccessful: " $result
   exit $result
fi

