<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script language="javascript" src="<c:url value="/org.springbyexample.web.gwt.App/org.springbyexample.web.gwt.App.nocache.js"/>"></script>

<!-- OPTIONAL: include this if you want history support -->
<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>

<%-- Used by GWT Search Table Widget for Internationalization --%>
<script language="javascript">
//<!--        
    var MessageResource = {
        "person.form.firstName": "<fmt:message key="person.form.firstName"/>",
        "person.form.lastName": "<fmt:message key="person.form.lastName"/>"
    };
// -->
</script>
        
<h1><fmt:message key="person.search.title"/></h1>

<div id="search-table"></div>
