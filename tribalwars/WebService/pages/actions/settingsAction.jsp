<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="datastore.Configuration"%>
<%
	boolean success;
	try {
		Configuration.setProperty(request.getParameter("settingID"), request.getParameter("settingValue"));
		success = true;
	} catch (Exception ignore) {
		success = false;
	}
	pageContext.setAttribute("success", success);
%>

<c:choose>
	<c:when test="${success}">
		<div class="alert alert-success" role="alert" style="margin-top: 2em;">Erfolgreich gespeichert!</div>
	</c:when>
	<c:otherwise>
		<div class="alert alert-danger" role="alert" style="margin-top: 2em;">Es ist ein unerwartetes Problem aufgetreten!</div>
	</c:otherwise>
</c:choose>