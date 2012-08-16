<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div align="right">
    <div>
        <fmt:message key="button.locale"/>:

            <sec:authorize ifAllGranted="ROLE_USER">
                <c:set var="urlBase" value="/index.html" />
            </sec:authorize>
            
            <c:url var="englishLocaleUrl" value="${urlBase}">
                <c:param name="locale" value="" />
            </c:url>
            <c:url var="spanishLocaleUrl" value="${urlBase}">
                <c:param name="locale" value="es" />
            </c:url>
        
            <a href='<c:out value="${englishLocaleUrl}"/>'><fmt:message key="locale.english"/></a>
            <a href='<c:out value="${spanishLocaleUrl}"/>'><fmt:message key="locale.spanish"/></a>
    </div>
    
    <div>&nbsp;</div>
    
    <div><fmt:message key="site.footer"/></div>
</div>