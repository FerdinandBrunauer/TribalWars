<%@page import="datastore.Configuration"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="loginSystem/checkLogin.jsp"%>

<jsp:include page="staticContent/header.jsp">
	<jsp:param name="title" value="Die St&auml;mme Bot - Einstellungen" />
	<jsp:param name="active" value="settings" />
	<jsp:param name="header" value="Einstellungen" />
</jsp:include>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<h3>Die St&auml;mme</h3>
				<div style="line-height: 100%;">
					<p>Bitte tragen Sie hier ihre Accountdaten ein.</p>
					<p>
						<b>Achtung!</b>
						Aus Sicherheitsgr&uuml;nden kann das Passwort nur in der Einstellungsdatei ge&auml;ndert werden.
					</p>
					<p>
						<b>Achtung</b>
						Diese Einstellungen &auml;ndern sich erst nach einem Neustart!
					</p>
				</div>
				<div class="form-group" style="max-width: 200px;">
					<label for="username">Username:</label>
					<input type="text" class="form-control" id="username" value="<%=Configuration.getProperty(
					Configuration.configuration_username, "")%>">

					<label for="worldprefix">Weltenprefix:</label>
					<select class="form-control" id="worldprefix">
						<option <%=(Configuration.getProperty(
					Configuration.configuration_worldprefix, "")
					.compareTo("de") == 0) ? "selected=\"selected\"" : ""%>>de</option>
						<option <%=(Configuration.getProperty(
					Configuration.configuration_worldprefix, "").compareTo(
					"dep") == 0) ? "selected=\"selected\"" : ""%>>dep</option>
					</select>

					<label for="worldnumber">Weltennummer:</label>
					<input type="number" min="1" max="118" class="form-control" id="worldnumber" value="<%=Configuration.getProperty(
					Configuration.configuration_worldnumber, null)%>">
				</div>
				<button type="button" class="btn btn-primary" onclick="var e = document.getElementById('worldprefix'); sendSetting('account_setting_alert', '<%=Configuration.configuration_username%>', document.getElementById('username').value);sendSetting('account_setting_alert', '<%=Configuration.configuration_worldprefix%>', e.options[e.selectedIndex].text);sendSetting('account_setting_alert', '<%=Configuration.configuration_worldnumber%>', document.getElementById('worldnumber').value);">Speichern</button>
				<div id="account_setting_alert"></div>
			</div>
		</div>
	</div>

	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<h3>Sp&auml;her</h3>
				<div style="line-height: 100%">
					<p>Bitte w&auml;hlen Sie hier die Anzahl, der mindestens benötigten Späher pro Angriff.</p>
					<p>Diese betr&auml;gt entweder 1 oder 4. Um den richtigen Wert herauszufinden, benutze bitte den Simulator.</p>
				</div>
				<div class="form-group" style="max-width: 200px;">
					<label for="spy">Anzahl:</label>
					<select class="form-control" id="spy">
						<option <%=(Configuration.getProperty(
					Configuration.configuration_minimum_spys, "4").compareTo(
					"1") == 0) ? "selected=\"selected\"" : ""%>>1</option>
						<option <%=(Configuration.getProperty(
					Configuration.configuration_minimum_spys, "4").compareTo(
					"4") == 0) ? "selected=\"selected\"" : ""%>>4</option>
					</select>
				</div>
				<button type="button" class="btn btn-primary" onclick="var e = document.getElementById('spy'); sendSetting('spy_setting_alert', '<%=Configuration.configuration_minimum_spys%>', e.options[e.selectedIndex].text);">Speichern</button>
				<div id="spy_setting_alert"></div>
			</div>
		</div>
	</div>

	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<h3>9kw.eu</h3>
				<p>
					Bitte tragen sie hier ihren
					<b>9kw.eu</b>
					API-Key ein:
				</p>
				<div class="form-group" style="max-width: 200px;">
					<label for="api-key">API-Key:</label>
					<input type="text" class="form-control" id="api-key" value="<%=Configuration.getProperty(
					Configuration.configuration_9kweu_javaapikey, null)%>">
				</div>
				<button type="button" class="btn btn-warning" onclick="testAPIKey('api_setting_alert', document.getElementById('api-key').value);">Testen</button>
				<button type="button" class="btn btn-primary" onclick="sendSetting('api_setting_alert', '<%=Configuration.configuration_9kweu_javaapikey%>', document.getElementById('api-key').value);">Speichern</button>
				<div id="api_setting_alert"></div>
			</div>
		</div>
	</div>

	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-body">
				<h3>Webservie</h3>
				<p>Hier k&ouml;nnen Sie ihre Anmeldedaten f&uuml;r den Webservice festlegen:</p>
				<div class="form-group" style="max-width: 200px;">
					<label for="name">Username:</label>
					<input type="text" class="form-control" id="name" value="<%=Configuration.getProperty(
					Configuration.configuration_webserviceusername, "admin")%>">
					<label for="password">Passwort:</label>
					<input type="text" class="form-control" id="password" value="<%=Configuration.getProperty(
					Configuration.configuration_webservicepassword, "password")%>">
				</div>
				<button type="button" class="btn btn-primary" onclick="sendSetting('webservice1_setting_alert', '<%=Configuration.configuration_webserviceusername%>', document.getElementById('name').value); sendSetting('webservice1_setting_alert', '<%=Configuration.configuration_webservicepassword%>', document.getElementById('password').value);">Speichern</button>
				<div id="webservice1_setting_alert"></div>
				<div style="margin-top: 1em; line-height: 100%">
					<p>Hier k&ouml;nnen Sie den Port einstellen, auf welchem der Webservice gestartet wird.</p>
					<p>
						<b>Achtung!</b>
						Dieser &auml;ndert sich erst nach einem Neustart!
					</p>
				</div>
				<div class="form-group" style="max-width: 200px;">
					<label for="port">Port:</label>
					<input type="text" class="form-control" id="port" value="<%=Configuration.getProperty(
					Configuration.configuration_webserviceport, "8080")%>">
				</div>
				<button type="button" class="btn btn-primary" onclick="checkPort('webservice2_setting_alert', '<%=Configuration.configuration_webserviceport%>', document.getElementById('port').value);">Speichern</button>
				<div id="webservice2_setting_alert"></div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	function sendSetting(alertID, settingsID, settingsValue) {
		$.post("actions/settingsAction.jsp", {
			settingID : settingsID,
			settingValue : settingsValue
		}, function(data) {
			$('#' + alertID).html(data);
		});
	}
	function testAPIKey(alertID, apiKey) {
		$.post("https://www.9kw.eu/index.cgi", {
			action : 'usercaptchaguthaben',
			apikey : apiKey
		}, function(data) {
			if (isNumber(data)) {
				$('#' + alertID).html('<div class="alert alert-success" role="alert" style="margin-top: 2em;">Guthaben: <b>' + data + '</b> Credits</div>');
			} else {
				$('#' + alertID).html('<div class="alert alert-danger" role="alert" style="margin-top: 2em;">Kein g&uuml;tiger API-Key!</div>');
			}
		});
	}
	function checkPort(alertID, settingsID, settingsValue) {
		if (isNumber(settingsValue) && (settingsValue < 65535) && (settingsValue > 1024)) {
			sendSetting(alertID, settingsID, settingsValue);
		} else {
			$('#' + alertID).html('<div class="alert alert-danger" role="alert" style="margin-top: 2em;">Kein g&uuml;tiger Port!</div>');
		}
	}
	function isNumber(data) {
		return !isNaN(parseFloat(data)) && isFinite(data);
	}
</script>

<%@ include file="staticContent/footer.jsp"%>