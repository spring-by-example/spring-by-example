<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h1><fmt:message key="gwtChatForm.title"/></h1>

<script type="text/javascript" src="<c:url value="/js/gwt-chat.js"/>"></script>

<script language="javascript" src="<c:url value="/org.springbyexample.web.gwt.chat.App/org.springbyexample.web.gwt.chat.App.nocache.js"/>"></script>

<!-- OPTIONAL: include this if you want history support -->
<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>

<%-- Used by GWT for Internationalization --%>
<script language="javascript">
//<!--        
    var MessageResource = {
        "gwtChatForm.userName": "<fmt:message key="gwtChatForm.userName"/>",
        "gwtChatForm.chat": "<fmt:message key="gwtChatForm.chat"/>",
        "gwtChatForm.join": "<fmt:message key="gwtChatForm.join"/>",
        "gwtChatForm.send": "<fmt:message key="gwtChatForm.send"/>",
        "gwtChatForm.leave": "<fmt:message key="gwtChatForm.leave"/>"
    };
// -->
</script>

<div id="comet"></div>
