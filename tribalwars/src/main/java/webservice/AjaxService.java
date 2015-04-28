package webservice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tribalwars.Account;

public class AjaxService extends HttpServlet {
	private static final long serialVersionUID = -527089361769286098L;

	//private final Account account;

	public AjaxService(Account account) {
	//	this.account = account;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		if (isLoggedin(request.getSession())) {
			if (request.getParameter("method") != null) {
				switch(request.getParameter("method")) {
				case "dashboard": {
					response.getWriter().println("dashboard");
					break;
				}
				case "building_pattern": {
					response.getWriter().println("building_pattern");
					break;
				}
				default: {
					response.getWriter().println(generateErrorMessage("Ung&uuml;ltige Anfrage!"));
					break;
				}
				}
			} else {
				response.getWriter().println(generateErrorMessage("Ung&uuml;ltige Anfrage!"));
			}
		} else {
			if ((request.getParameter("method") != null) && (request.getParameter("method").compareTo("login") == 0)) {
				// TODO login
				request.getSession().setAttribute("loggedin", "true");
			} else {
				response.getWriter().println(generateLoginForm());
			}
		}
	}

	private boolean isLoggedin(HttpSession session) {
		String loggedin = (session.getAttribute("loggedin") != null) ? session.getAttribute("loggedin").toString() : null;
		if (loggedin != null) {
			return Boolean.parseBoolean(loggedin);
		} else {
			return false;
		}
	}

	private String generateLoginForm() {
		return "<div class=\"container\"><div class=\"row\"><div class=\"col-sm-6 col-md-4 col-md-offset-4\"><h1 class=\"text-center login-title\">Bitte loggen sie sich erst ein</h1><div class=\"account-wall\"><form class=\"form-signin\" onsubmit=\"tryLogin($('#name').val(), $('#password').val());\"><input type=\"text\" class=\"form-control\" placeholder=\"Name\" name=\"name\" id=\"name\" required autofocus><input type=\"password\" class=\"form-control\" placeholder=\"Passwort\" name=\"password\" id=\"password\" required><button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Login</button></form></div></div></div></div>";
	}

	private String generateErrorMessage(String message) {
		return "<div class=\"alert alert-danger\" role=\"alert\"><span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=\"true\"></span><span class=\"sr-only\">Error:</span>" + message + "</div>";
	}

}
