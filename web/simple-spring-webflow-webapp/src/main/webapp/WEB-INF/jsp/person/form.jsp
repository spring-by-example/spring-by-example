<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h1><fmt:message key="person.form.title"/></h1>

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
                <input type="submit" id="save" name="_eventId_save" value="<fmt:message key="button.save"/>" 
                    onclick="Spring.remoting.submitForm('save', 'person', {fragments:'content'}); return false;"/>&#160;
                <input type="submit" name="_eventId_cancel" value="<fmt:message key="button.cancel"/>"/>&#160;    
            </div>    
        </div>
    </fieldset>
</form:form>
