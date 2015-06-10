<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="datastore.Database"%>

<%@ include file="loginSystem/checkLogin.jsp"%>

<jsp:include page="staticContent/header.jsp">
	<jsp:param name="title" value="Die St&auml;mme Bot - Statistiken" />
	<jsp:param name="active" value="charts" />
	<jsp:param name="header" value="Statistiken" />
</jsp:include>

<div class="row">
	<div class="col-lg-6">
		<div class="panel panel-default">
			<div class="panel-body">
				<h3>Allgemeines</h3>
				<p>
					Gespeicherte Berichte:
					<b><%=Database.getCountReports()%></b>
				</p>
				<p>
					Durchschnittliche ersp&auml;hte Rohstoffe:
					<b><%=new DecimalFormat("#.00").format(Database
					.getAvgSpyedResources())%></b>
				</p>
				<p>
					Maximal ersp&auml;hte Rohstoffe:
					<b><%=Database.getMaximalRessourcesSpyed()%></b>
				</p>
				<p>
					Gespeicherte Farmen:
					<b><%=Database.getCountFarms()%></b>
				</p>
				<p>
					Durchschnittliche Farmen pro Dorf:
					<b><%=new DecimalFormat("#.00").format(Database
					.getAvgCountFarm())%></b>
				</p>
				<p>
					Durchschnittliche Distanz zu einer Farm:
					<b><%=new DecimalFormat("#.00").format(Database
					.getAvgDistanceFarm())%></b>
				</p>
				<p>
					Maximale Distanz zu einer Farm:
					<b><%=new DecimalFormat("#.00").format(Database
					.getMaxDistanceFarm())%></b>
				</p>
				<p>
					Gespeicherte Farmangriffe:
					<b><%=Database.getCountFarmAttack()%></b>
				</p>
				<p>
					Anzahl ge&auml;nderter Eintr&auml;ge in der Datenbank seit dem Start:
					<b><%=Database.getTotalChanges()%></b>
				</p>
			</div>
		</div>
	</div>

	<div class="col-lg-6">
		<div class="panel panel-default">
			<div class="panel-heading">Farmen pro Dorf</div>
			<div class="panel-body">
				<div class="flot-chart">
					<div class="flot-chart-content" id="flot-pie-chart"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var chartError = function(req, status, err) {
			console.log('An error occurred: ', status, err);
		};

		function chartDataReceived(data) {
			$.plot($("#flot-pie-chart"), data, {
				series : {
					pie : {
						show : true
					}
				},
				grid : {
					hoverable : true
				},
				tooltip : true,
				tooltipOpts : {
					content : "%p.0%, %s", // show percentages, rounding to 2 decimal places
					shifts : {
						x : 20,
						y : 0
					},
					defaultTheme : false
				}
			});
		}

		$.ajax({
			url : 'actions/chartdata_FarmsPerVillage.jsp',
			method : 'GET',
			dataType : 'json',
			success : chartDataReceived,
			error : chartError
		});
	});
</script>

<jsp:include page="staticContent/footer.jsp">
	<jsp:param name="charting" value="true" />
</jsp:include>