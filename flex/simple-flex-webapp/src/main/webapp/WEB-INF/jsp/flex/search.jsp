<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1><fmt:message key="person.search.title"/></h1>

<!--  BEGIN Browser History required section -->
<link rel="stylesheet" type="text/css" href="history/history.css" />
<!--  END Browser History required section -->

<script src="AC_OETags.js" language="javascript"></script>

<!--  BEGIN Browser History required section -->
<script src="history/history.js" language="javascript"></script>
<!--  END Browser History required section -->

<style>
body { margin: 0px; overflow:hidden }
</style>
<script language="JavaScript" type="text/javascript">
<!--
// -----------------------------------------------------------------------------
// Globals
// Major version of Flash required
var requiredMajorVersion = 9;
// Minor version of Flash required
var requiredMinorVersion = 0;
// Minor version of Flash required
var requiredRevision = 124;
// -----------------------------------------------------------------------------
// -->
</script>

<script language="JavaScript" type="text/javascript">
<!--
// Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
var hasProductInstall = DetectFlashVer(6, 0, 65);

// Version check based upon the values defined in globals
var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);

if ( hasProductInstall && !hasRequestedVersion ) {
    // DO NOT MODIFY THE FOLLOWING FOUR LINES
    // Location visited after installation is complete if installation is required
    var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
    var MMredirectURL = window.location;
    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
    var MMdoctitle = document.title;

    AC_FL_RunContent(
        "src", "playerProductInstall",
        "FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+"",
        "width", "800",
        "height", "600",
        "align", "middle",
        "id", "search",
        "quality", "high",
        "bgcolor", "#869ca7",
        "name", "search",
        "allowScriptAccess","sameDomain",
        "type", "application/x-shockwave-flash",
        "pluginspage", "http://www.adobe.com/go/getflashplayer"
    );
} else if (hasRequestedVersion) {
    // if we've detected an acceptable version
    // embed the Flash Content SWF when all tests are passed
    AC_FL_RunContent(
            "src", "search",
            "width", "800",
            "height", "600",
            "align", "middle",
            "id", "search",
            "quality", "high",
            "bgcolor", "#869ca7",
            "name", "search",
            "allowScriptAccess","sameDomain",
            "type", "application/x-shockwave-flash",
            "pluginspage", "http://www.adobe.com/go/getflashplayer"
    );
  } else {  // flash is too old or we can't detect the plugin
    var alternateContent = 'Alternate HTML content should be placed here. '
    + 'This content requires the Adobe Flash Player. '
    + '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
    document.write(alternateContent);  // insert non-flash content
  }
// -->
</script>
<noscript>
    <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553560000"
            id="search" width="800" height="600"
            codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
            <param name="movie" value="search.swf" />
            <param name="quality" value="high" />
            <param name="bgcolor" value="#869ca7" />
            <param name="allowScriptAccess" value="sameDomain" />
            <embed src="search.swf" quality="high" bgcolor="#869ca7"
                width="800" height="600" name="search" align="middle"
                play="true"
                loop="false"
                quality="high"
                allowScriptAccess="sameDomain"
                type="application/x-shockwave-flash"
                pluginspage="http://www.adobe.com/go/getflashplayer">
            </embed>
    </object>
</noscript>
