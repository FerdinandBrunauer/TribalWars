<%@page import="tribalwars.Account"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="datastore.Database"%>
<%@page import="tribalwars.Village"%>

<%
	Village[] villages = Account.getInstance().getMyVillages();
	pageContext.setAttribute("villages", villages);

	JSONArray array = new JSONArray();

	for (Village village : villages) {
		JSONObject temp = new JSONObject();
		temp.put("label", village.getDorfname());
		temp.put("data", Database.getFarmCount(village.getID()));
		array.put(temp);
	}
%>

<%=array%>
