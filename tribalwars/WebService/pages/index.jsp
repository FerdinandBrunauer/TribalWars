<%@page import="tribalwars.Village"%>
<%@page import="tribalwars.Account"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ include file="loginSystem/checkLogin.jsp"%>

<jsp:include page="staticContent/header.jsp">
	<jsp:param name="title" value="Die St&auml;mme Bot - Dashboard" />
	<jsp:param name="active" value="dashboard" />
	<jsp:param name="header" value="Dashboard" />
</jsp:include>

<%
	Village[] villages = Account.getInstance().getMyVillages();
	pageContext.setAttribute("villages", villages);
	pageContext.setAttribute("startLoop", request.getParameterMap().containsKey("from") ? Integer.parseInt(request.getParameter("from")) : 0);
	if (Integer.parseInt(pageContext.getAttribute("startLoop").toString()) < 0) {
		pageContext.setAttribute("startLoop", 0);
	}
	pageContext.setAttribute("endLoop", ((Integer.parseInt(pageContext.getAttribute("startLoop").toString()) + 10) < villages.length) ? Integer.parseInt(pageContext.getAttribute("startLoop").toString()) + 9 : villages.length - 1);
%>

<c:if test="${fn:length(villages) > 0}">
	<table class="table table-striped table-bordered">
		<tr>
			<th>Dorfname</th>
			<th>Dorfposition</th>
			<th><img alt="Holz" src="../bower_components/images/wood.png">&nbsp;Holz</th>
			<th><img alt="Lehm" src="../bower_components/images/stone.png">&nbsp;Lehm</th>
			<th><img alt="Eisen" src="../bower_components/images/iron.png">&nbsp;Eisen</th>
			<th><img alt="Speicher" src="../bower_components/images/booty.png">&nbsp;Speicher</th>
			<th><img alt="Bev&ouml;lkerung" src="../bower_components/images/face.png">&nbsp;Bev&ouml;lkerung</th>
		</tr>
		<c:forEach begin="${startLoop}" end="${endLoop}" var="index">
			<tr>
				<td><c:out value="${villages[index].dorfname}" /></td>
				<td><c:out value="${villages[index].x}" />|<c:out value="${villages[index].y}" /></td>
				<td><c:out value="${villages[index].holz}" /></td>
				<td><c:out value="${villages[index].lehm}" /></td>
				<td><c:out value="${villages[index].eisen}" /></td>
				<td><c:out value="${villages[index].speicher}" /></td>
				<td><c:out value="${villages[index].population}" />/<c:out value="${villages[index].maximalPopulation}" /></td>
			</tr>
		</c:forEach>
	</table>
	<div class="btn-toolbar" role="toolbar" style="padding-bottom: 2em;">
		<div class="btn-group" role="group">
			<button type="button" class="btn btn-default" onclick="location.href='index.jsp?from=${startLoop-10}'">Zur&uuml;ck</button>
			<button type="button" class="btn btn-default" onclick="location.href='index.jsp?from=${endLoop + 1}'">Weiter</button>
		</div>
	</div>
</c:if>
<c:if test="${fn:length(villages) < 1}">
	<div class="alert alert-danger" role="alert">
		<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> <span class="sr-only">Error:</span> Keine D&ouml;rfer vorhanden!
	</div>
</c:if>

<%@ include file="staticContent/footer.jsp"%>