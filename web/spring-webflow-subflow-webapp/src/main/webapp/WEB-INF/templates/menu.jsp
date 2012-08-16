<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div id="side-bar">
    <a href="<c:url value="/"/>">Home</a>
    
    <sec:authorize ifAllGranted="ROLE_USER">
        <p><fmt:message key="person.form.title"/></p>
            <a href="<c:url value="/person.html"/>"><fmt:message key="button.create"/></a> 
            <a href="<c:url value="/person/search.html"/>"><fmt:message key="button.search"/></a>
    </sec:authorize>
</div>
