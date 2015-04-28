package webservice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tribalwars.Account;
import tribalwars.Village;

public class AjaxService extends HttpServlet {
	private static final long serialVersionUID = -527089361769286098L;

	private final Account account;

	public AjaxService(Account account) {
		this.account = account;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		if (isLoggedin(request.getSession())) {
			if (request.getParameter("method") != null) {
				switch (request.getParameter("method")) {
				case "dashboard": {
					response.getWriter().println(generateDashboard());
					break;
				}
				case "building_pattern": {
					response.getWriter().println(generateBuildingPattern());
					break;
				}
				// TODO
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

	private String generateDashboard() {
		String dashboard = "<div class=\"container\"><h1>Dashboard</h1><table class=\"table table-striped\"><thead><tr><th>Nr.</th><th>Name</th><th>Koodinaten</th><th>Holz</th><th>Lehm</th><th>Eisen</th><th>Speicher</th><th>Speertr.</th><th>Schwertk.</th><th>Axtk.</th><th>Bogensch.</th><th>Sp&auml;her</th><th>L.Kav.</th><th>B.Bogensch.</th><th>S.Kav.</th><th>Rammb&ouml;cke</th><th>Katapult</th></tr></thead><tbody>";

		Village[] villages = this.account.getMyVillages();
		int counter = 1;
		for (Village village : villages) {
			dashboard += "<tr>";
			dashboard += "<td>" + counter + "</td>";
			counter += 1;
			dashboard += "<td>" + village.getName() + "</td>";
			dashboard += "<td>" + village.getX() + "|" + village.getY() + "</td>";
			dashboard += "<td>" + village.getHolz() + "</td>";
			dashboard += "<td>" + village.getLehm() + "</td>";
			dashboard += "<td>" + village.getEisen() + "</td>";
			dashboard += "<td>" + village.getSpeicher() + "</td>";
			dashboard += "<td>" + village.getSpeertraeger() + "</td>";
			dashboard += "<td>" + village.getSchwertkaempfer() + "</td>";
			dashboard += "<td>" + village.getAxtkaempfer() + "</td>";
			dashboard += "<td>" + village.getBogenschuetzen() + "</td>";
			dashboard += "<td>" + village.getSpaeher() + "</td>";
			dashboard += "<td>" + village.getLeichteKavallerie() + "</td>";
			dashboard += "<td>" + village.getBerittenerBogenschuetze() + "</td>";
			dashboard += "<td>" + village.getSchwereKavallerie() + "</td>";
			dashboard += "<td>" + village.getRammboecke() + "</td>";
			dashboard += "<td>" + village.getKatapult() + "</td>";
			dashboard += "</tr>";
		}

		dashboard += "</tbody></table></div>";
		return dashboard;
	}

	private String generateBuildingPattern() {
		return "Hier k&ouml;nnten ihre Bauvorlagen stehen";
	}

}
