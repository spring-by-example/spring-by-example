<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="side-bar">
    <a href="<c:url value="/"/>">Home</a>
    
    <p><fmt:message key="chat.title"/></p>
        <a href="<c:url value="/dojo-chat.htm"/>"><fmt:message key="dojoChatForm.title"/></a> 
        <a href="<c:url value="/gwt-chat.htm"/>"><fmt:message key="gwtChatForm.title"/></a>
</div>