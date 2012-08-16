<h1>Home</h1>

<p>This is an example showing how to use Tiles (http://tiles.apache.org) with Spring MVC using the url 
to dynamically set the 'body' of an existing Tiles template definition.  This avoids having to 
create a definition for every single page when typically only the body will change.</p>

<p>The main body that you are reading is in <i>/WEB-INF/jsp/index.jsp</i> and the request for 
<i>/index.html</i> is rendered using the main Tiles template definition located in 
<i>/WEB-INF/tiles-defs/templates.xml</i> and the url is used to dynamically set the <i>index.jsp</i> 
as the body.</p>