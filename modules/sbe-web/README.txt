The Spring by Example Web module has classes to help integrate GWT with Spring 
for standard use, Spring Bayeux integration for Comet on Jetty, and some utiliies 
for processing images.

This project can be built using Maven (http://maven.apache.org).  It can also be imported into 
the Eclipse IDE, but the M2Eclipse plugin should be installed since it is used to resolve 
the classpath.

	

Release Notes
--------------
1.1.5 - Upgraded to Spring 4.0.2.

1.1.4 - Upgraded to Spring 3.2.

1.1.3 - Upgraded to Spring 3.1.

1.1.2 - Upgraded to Spring 3.0 and removed GenericHtmlController since it can be handled by configuration.

1.1.1 - Changed SpringContinuationCometdServlet to get Bayeux bean by type instead of name.

1.1 -   Added support for a Spring GWT Controller and Spring Comet Integration with Bayeux on Jetty.

1.0 -	Initial checking with an image interceptor and a generic html annotation-based controller.
