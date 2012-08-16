<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1><fmt:message key="person.search.title"/></h1>

<table class="search">
    <tr>
        <th><fmt:message key="person.form.firstName"/></th>
        <th><fmt:message key="person.form.lastName"/></th>
    </tr>
<c:forEach var="person" items="${persons}" varStatus="status">
    <tr>
        <c:set var="personFormId" value="person${status.index}"/>

        <c:url var="editUrl" value="/person/form.html">
            <c:param name="id" value="${person.id}" />
        </c:url>
        <c:url var="deleteUrl" value="/person/delete.html"/>
        <form id="${personFormId}" action="${deleteUrl}" method="POST">
            <input id="id" name="id" type="hidden" value="${person.id}"/>
        </form>

    	<td>${person.firstName}</td>
        <td>${person.lastName}</td> 
    	<td>
            <a href='<c:out value="${editUrl}"/>'><fmt:message key="button.edit"/></a>
            <a href="javascript:document.forms['${personFormId}'].submit();"><fmt:message key="button.delete"/></a> 
        </td>
    </tr>
</c:forEach>
</table>