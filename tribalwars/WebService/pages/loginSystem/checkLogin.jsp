
<%
	boolean loggedin;
	if (session.getAttribute("loggedin") != null) {
		loggedin = Boolean.parseBoolean(session.getAttribute("loggedin").toString());
	} else {
		loggedin = false;
	}
	if (!loggedin) {
		if (!request.getRequestURI().endsWith("login.jsp")) {
			response.sendRedirect("loginSystem/login.jsp");
		}
	} else {
		if (request.getRequestURI().endsWith("login.jsp")) {
			response.sendRedirect("../index.jsp");
		}
	}
%>