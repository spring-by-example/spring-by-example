<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<c:url var="editUrl" value="/person.htm">
    <c:param name="id" value="${param.id}" />
</c:url>
<c:url var="deleteUrl" value="/person/delete.htm">
    <c:param name="id" value="${param.id}" />
</c:url>

<a href='<c:out value="${editUrl}"/>'><fmt:message key="button.edit"/></a>
<sec:authorize ifAllGranted="ROLE_ADMIN">
    <a href='<c:out value="${deleteUrl}"/>'><fmt:message key="button.delete"/></a>
</sec:authorize>
