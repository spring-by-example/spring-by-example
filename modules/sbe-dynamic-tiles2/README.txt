This module has views that use Tiles (http://tiles.apache.org) with Spring MVC using the url 
to dynamically set the 'body' of an existing Tiles template definition.  This avoids having to 
create a definition for every single page when typically only the body will change.

This project can be built using Maven (http://maven.apache.org).  It can also be imported into 
the Eclipse IDE, but the M2Eclipse plugin should be installed since it is used to resolve 
the classpath.


Release Notes
--------------
1.2.3 - Upgraded to Spring 4.0.2.

1.2.2 - Upgraded to Spring 3.2.

1.2.1 - Upgraded to Spring 3.1.
 
1.2 -   Upgraded to Spring 3.0 and Spring JS 2.0.8.RELEASE.  The latter has a patch to make it easier to override 
        AjaxTilesView for Dynamic Tiles rendering.
        
1.1 -   Updated project to use specific Spring modules needed instead of spring.jar, upgraded to Spring 2.5.5, 
		changed DynamicTilesView to extend TilesView (based on Spring refactoring 
        in 2.5.x branch able to extend TilesView directly).  Added support for AJAX based views like Spring Web Flow provides.
        Changed main default body from 'body' to 'content' for default tiles body.
        Resolved issue with different versions of Commons Logging being imported by using a "version 99" 
        empty jar and switching logging to SLF4J.  Compiled the project with Java 5 (previous version compiled with Java 6).

1.0 -   Initial release with DynamicTilesView and TilesUrlBasedViewResolver for rendering tiles views 
        using the URL of the incoming request as the body for a template.
