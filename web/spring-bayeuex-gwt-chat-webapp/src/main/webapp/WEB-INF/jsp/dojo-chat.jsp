<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h1><fmt:message key="dojoChatForm.title"/></h1>

<script type="text/javascript" src="<c:url value="/js/chat.js"/>"></script>

<div id="chatroom">
<div id="chat"></div>
<div id="members"></div>
<div id="input">
<div id="join">
    <fmt:message key="dojoChatForm.userName"/>:&nbsp;<input id="username" type="text" />
    <input id="joinB" class="button" type="submit" name="join" value="Join" />
</div>
<div id="joined" class="hidden">
    <fmt:message key="dojoChatForm.chat"/>:&nbsp;
    <input id="phrase" type="text" /> 
    <input id="sendB" class="button" type="submit" name="join" value="<fmt:message key="dojoChatForm.send"/>" /> 
    <input id="leaveB" class="button" type="submit" name="join" value="<fmt:message key="dojoChatForm.leave"/>" />
</div>
</div>
</div>
