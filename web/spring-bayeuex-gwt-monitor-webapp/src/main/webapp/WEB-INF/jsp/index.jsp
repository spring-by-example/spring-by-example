<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="<c:url value="/js/monitor.js"/>"></script>

<script language="javascript" src="<c:url value="/org.springbyexample.web.gwt.monitor.App/org.springbyexample.web.gwt.monitor.App.nocache.js"/>"></script>

<!-- OPTIONAL: include this if you want history support -->
<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>

<%-- Used by GWT for Internationalization --%>
<script language="javascript">
//<!--        
    var MessageResource = {
        "monitor.moreInformation": "<fmt:message key="monitor.moreInformation"/>",
        "monitor.trades": "<fmt:message key="monitor.trades"/>",
        "monitor.volume": "<fmt:message key="monitor.volume"/>",
        "monitor.buy.trades": "<fmt:message key="monitor.buy.trades"/>",
        "monitor.buy.volume": "<fmt:message key="monitor.buy.volume"/>",
        "monitor.sell.trades": "<fmt:message key="monitor.sell.trades"/>",
        "monitor.sell.volume": "<fmt:message key="monitor.sell.volume"/>"
    };
// -->
</script>

<div id="comet"></div>
