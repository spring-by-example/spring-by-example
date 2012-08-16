<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><fmt:message key="site.title"/></title>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css"/>" />
<link type="text/css" rel="stylesheet" href="<c:url value="/resources/dijit/themes/tundra/tundra.css" />" />
<script type="text/javascript" src="<c:url value="/resources/dojo/dojo.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/spring/Spring.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/spring/Spring-Dojo.js" />"></script>
</head>
<body>
<div id="header">
	<tiles:insertAttribute name="header" />
</div>
<div id="menu">
	<tiles:insertAttribute name="menu" />
</div>
<div id="content">
	<tiles:insertAttribute name="content" />
</div>
<div id="footer">
	<tiles:insertAttribute name="footer" />
</div>
</body>
</html>
