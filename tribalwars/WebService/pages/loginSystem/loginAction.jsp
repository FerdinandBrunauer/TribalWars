
<%@page import="datastore.Configuration"%>
<%
	String username = request.getParameter("name");
	String password = request.getParameter("password");

	if (username != null) {
		if (password != null) {
			if (username.compareTo(Configuration.getProperty(Configuration.configuration_webserviceusername, "admin")) == 0) {
				if (password.compareTo(Configuration.getProperty(Configuration.configuration_webservicepassword, "password")) == 0) {
					session.setAttribute("loggedin", true);
					response.sendRedirect("../index.jsp");
					return;
				}
			}
		}
	}

	response.sendRedirect("login.jsp");
%>