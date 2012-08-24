<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h1><fmt:message key="person.form.title"/></h1>

<div id="messages">
    <c:if test="${not empty statusMessageKey}">
       <p><fmt:message key="${statusMessageKey}"/></p>
    </c:if>

    <spring:hasBindErrors name="person">
        <h2>Errors</h2>
        <div class="formerror">
            <ul>
            <c:forEach var="error" items="${errors.allErrors}">
                <li>${error.defaultMessage}</li>
            </c:forEach>
            </ul>
        </div>
    </spring:hasBindErrors>
</div>

<form:form modelAttribute="person">
    <form:hidden path="id" />

    <fieldset>
        <div class="form-row">
            <label for="firstName"><fmt:message key="person.form.firstName"/>:</label>
            <span class="input"><form:input path="firstName" /></span>
        </div>       
        <div class="form-row">
            <label for="lastName"><fmt:message key="person.form.lastName"/>:</label>
            <span class="input"><form:input path="lastName" /></span>
        </div>
        <div class="form-buttons">
            <div class="button">
                <input type="submit" id="save" name="_eventId_save" value="<fmt:message key="button.save"/>"/>&#160;
                <input type="submit" name="_eventId_cancel" value="<fmt:message key="button.cancel"/>"/>&#160;     
            </div>    
        </div>
    </fieldset>
</form:form>

<c:if test="${person.id > 0}">
    <div style="clear:both;">
        <a href="${flowExecutionUrl}&_eventId=addAddress" ><fmt:message key="address.form.button.add"/></a>
    </div>
    
    <div>&nbsp;</div>
    
    <c:if test="${empty person.addresses}">
        <div>&nbsp;</div>
    </c:if>
    
    <c:if test="${not empty person.addresses}">
        <table class="search">
            <tr>
                <th><fmt:message key="address.form.address"/></th>
                <th><fmt:message key="address.form.city"/></th>
                <th><fmt:message key="address.form.state"/></th>
                <th><fmt:message key="address.form.zipPostal"/></th>
                <th><fmt:message key="address.form.country"/></th>
            </tr>
        <c:forEach var="address" items="${person.addresses}">
            <tr>
                <td>${address.address}</td>
                <td>${address.city}</td>
                <td>${address.state}</td>
                <td>${address.zipPostal}</td>
                <td>${address.country}</td> 
                <td>
                    <a href="${flowExecutionUrl}&_eventId=editAddress&addressId=${address.id}" ><fmt:message key="button.edit"/></a>
                    <sec:authorize ifAllGranted="ROLE_ADMIN">
                        <a href="${flowExecutionUrl}&_eventId=deleteAddress&addressId=${address.id}" ><fmt:message key="button.delete"/></a>
                    </sec:authorize>
                </td>
            </tr>
        </c:forEach>
        </table>
    </c:if>
</c:if>
