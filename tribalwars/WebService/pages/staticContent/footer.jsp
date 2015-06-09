<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

</div>
</div>

<footer class="bs-docs-footer" role="contentinfo">
	<div class="container" style="padding-top: 10px">
		<p>
			Sourcecode einsehbar auf
			<a href="https://github.com/FerdinandBrunauer/TribalWars">GitHub</a>
			.
		</p>
		<p>
			Lizensiert unter der
			<a rel="license" href="http://opensource.org/licenses/MIT" target="_blank">MIT-Lizenz</a>
			. Dokumentation:
			<a rel="license" href="https://creativecommons.org/licenses/by/3.0/" target="_blank">CC BY 3.0</a>
			.
		</p>
	</div>
</footer>

<script src="../../bower_components/bootstrap/dist/js/bootstrap.min.js" type="text/javascript"></script>
<script src="../../bower_components/metisMenu/dist/metisMenu.min.js" type="text/javascript"></script>
<script src="../../bower_components/raphael/raphael-min.js" type="text/javascript"></script>
<script src="../../bower_components/morrisjs/morris.min.js" type="text/javascript"></script>
<script src="../../js/morris-data.js" type="text/javascript"></script>
<script src="../../dist/js/sb-admin-2.js" type="text/javascript"></script>

<c:choose>
	<c:when test="${param.charting}">
		<script src="../bower_components/flot/excanvas.min.js" type="text/javascript"></script>
		<script src="../bower_components/flot/jquery.flot.js" type="text/javascript"></script>
		<script src="../bower_components/flot/jquery.flot.pie.js" type="text/javascript"></script>
		<script src="../bower_components/flot/jquery.flot.resize.js" type="text/javascript"></script>
		<script src="../bower_components/flot/jquery.flot.time.js" type="text/javascript"></script>
		<script src="../bower_components/flot.tooltip/js/jquery.flot.tooltip.min.js" type="text/javascript"></script>
	</c:when>
</c:choose>

</body>
</html>