Spring by Example Utils

Note: The Maven JAXB plugin was used to generate the JAXB mappings used for testing, but 
so they wouldn't be included in the dist that part of the build was commented out and the 
generated classes were copied to src/test.

This project can be built using Maven (http://maven.apache.org).  It can also be imported into 
the Eclipse IDE, but the M2Eclipse plugin should be installed since it is used to resolve 
the classpath.
	

Release Notes
--------------
1.2.4 - Upgraded to Spring 3.2.

1.2.3 - Upgraded to Spring 3.1.

1.2.2 - Upgraded to Spring 3.0.

1.2.1 - Added SolrOxmClient which is built on top of HttpClientTemplate and HttpClientOxmTemplate 
        for easy access to Solr for search and updates using a JavaBean.  Upgraded to Spring 2.5.6 
        and Spring OXM 1.5.5.

1.2 - 	ImageProcessor implementation and tests added.  Also project was upgraded to use Spring 2.5.4 and 
	  	instead of having a depedency on 'spring' to depend on 'spring-beans', 'spring-context', and 'spring-core'.
	  	
1.1 - 	Added Logger BeanPostProcessor.

1.0 -	Initial checking with HttpClientTemplate and HttpClientOxmTemplate.
