This is a simple example making a custom scope in Spring for Thread scope.  Every request for 
a bean will return the same instance for the same thread.  A Runnable must be wrapped in a 
ThreadScopeRunnable if destruction callbacks should occur on a thread scoped bean.

Note: See org.springframework.context.support.SimpleThreadScope in Spring 3.0 for a Spring implementation, 
      but it doesn't support destruction callbacks (this implementation does when using a custom Runnable).

This project can be built using Maven (http://maven.apache.org).  It can also be imported into 
the Eclipse IDE, but the M2Eclipse plugin should be installed since it is used to resolve 
the classpath.


Release Notes
--------------
1.0.3 - Upgraded to Spring 3.2.

1.0.2 - Upgraded to Spring 3.1.

1.0.1 - Upgraded to Spring 3.0 and added ThreadScopeCallable.

1.0 -	Initial release with a custom thread scope that supports destruction callbacks if 
        a Runnable is wrapped with ThreadScopeRunnable.