This is an example showing how to use Spring MVC annotations to make a CRUD webapp
with Flex and Spring BlazeDS Integration used for displaying the search results.

To build multiple locales with Flex, by default only en_US is supplied so
the following command needs to be run to create the es_ES locale for the Flex SDK.
 
	ex: /Applications/Adobe\ Flex\ Builder\ 3\ Plug-in/sdks/3.2.0/bin/copylocale en_US es_ES

This project can be built using Maven (http://maven.apache.org).  It can also be imported into 
the Eclipse IDE, but the M2Eclipse plugin should be installed since it is used to resolve 
the classpath.  The project is also setup to use the Spring IDE (http://springide.org).
	
It can also be run from the command line with 'mvn jetty:run-war' and accessed at the url 
'http://localhost:8080/simple-flex'.


Release Notes
--------------
1.1.3 - Upgraded to use Spring 4.0.2.

1.1.2 - Upgraded to use Spring Data JPA.

1.1.1 - Upgraded to Spring 3.2.

1.1   - Upgraded to Spring 3.0 and made project match Simple Form Annotation Config Webapp.

1.0.1 - Working Flex Maven build and 'mvn jetty:run' works.

1.0   -	Initial release.
