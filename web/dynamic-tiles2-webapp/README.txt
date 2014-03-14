This is an example showing how to use Tiles (http://tiles.apache.org) with Spring MVC using the url 
to dynamically set the 'body' of an existing Tiles template definition.  This avoids having to 
create a definition for every single page when typically only the body will change.

This project can be built using Maven (http://maven.apache.org).  It can also be imported into 
the Eclipse IDE, but the M2Eclipse plugin should be installed since it is used to resolve 
the classpath.  The project is also setup to use the Spring IDE (http://springide.org).
	
It can also be run from the command line with 'mvn jetty:run' and accessed at the url 
'http://localhost:8080/dynamic-tiles2'.


Release Notes
--------------
1.2.2 - Upgraded to Spring 4.0.2.

1.2.1 - Upgraded to Spring 3.2.

1.2 -   ?

1.1.1 - Upgraded to Dynamic Tiles 1.2 and Spring 3.0.

1.1   - Upgraded to Dynamic Tiles 1.1, Spring 2.5.6, and latest versions of other libraries.

1.0 -	Initial release.
