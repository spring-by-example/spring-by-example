This is a simple example showing how to configure AspectJ's Load-time Weaving with 
Spring's <context:load-time-weaver/> to gather statistics.

Note: This example must be run with Java 5 or higher since support for the 
-javaagent JVM argument was added in Java 5. Also, Maven and Eclipse are 
currently configured to look for the org.springframework.instrument.jar in  
the local Maven repository. This can be changed at the bottom of the 
Maven pom.xml or in the Eclipse Run Dialog's argument tab.

This project can be built using Maven (http://maven.apache.org).  It can also be imported into 
the Eclipse IDE, but the M2Eclipse plugin should be installed since it is used to resolve 
the classpath.


Release Notes
--------------
1.0.3 - Upgraded to Spring 4.0.2.

1.0.2 - Upgraded to Spring 3.2.

1.0.1 - Upgraded to Spring 3.0.

1.0 -	Initial release.
