<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="author" content="Ferdinand Brunauer">
<title><%=(request.getParameterMap().containsKey("title")) ? request.getParameter("title") : "Die St&auml;mme Bot"%></title>
<link rel="shortcut icon" href="../../bower_components/favicon2.ico" type="image/png" />
<link rel="icon" href="../../bower_components/favicon2.ico" type="image/png" />
<link href="../../bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="../../bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">
<link href="../../dist/css/sb-admin-2.css" rel="stylesheet">
<link href="../../bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<script src="../../bower_components/jquery/dist/jquery.min.js" type="text/javascript"></script>
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
	<div id="wrapper">
		<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
		<div class="navbar-header">
			<a class="navbar-brand">Die St&auml;mme Bot</a>
		</div>
		<div class="navbar-default sidebar" role="navigation">
			<div class="sidebar-nav navbar-collapse">
				<ul class="nav" id="side-menu">
					<li>
						<a href="index.jsp" class="<%=(request.getParameter("active").compareTo("dashboard") == 0) ? "active" : ""%>">
							<i class="fa fa-home fa-fw"></i> Dashboard
						</a>
					</li>
					<li>
						<a href="charts.jsp" class="<%=(request.getParameter("active").compareTo("charts") == 0) ? "active" : ""%>">
							<i class="fa fa-bar-chart fa-fw"></i> Statistiken
						</a>
					</li>
					<li>
						<a href="#">
							<i class="fa fa-th-list fa-fw"></i> Vorlagen<span class="fa arrow"></span>
						</a>
						<ul class="nav nav-second-level">
							<li>
								<a href="flot.html">
									<i class="fa fa-list-alt fa-fw"></i>Bauvorlagen
								</a>
							</li>
							<li>
								<a href="morris.html">
									<i class="fa fa-list-alt fa-fw"></i>Truppenvorlagen
								</a>
							</li>
							<li>
								<a href="morris.html">
									<i class="fa fa-list-alt fa-fw"></i>Zuweisungen
								</a>
							</li>
						</ul>
						<!-- /.nav-second-level -->
					</li>
					<li>
						<a href="settings.jsp" class="<%=(request.getParameter("active").compareTo("settings") == 0) ? "active" : ""%>">
							<i class="fa fa-sliders fa-fw"></i> Einstellungen
						</a>
					</li>
				</ul>
			</div>
		</div>
		</nav>
		<div id="page-wrapper" style="min-height: 285px;">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header"><%=(request.getParameterMap().containsKey("header")) ? request.getParameter("header") : "Die St&auml;mme Bot"%></h1>
				</div>
			</div>