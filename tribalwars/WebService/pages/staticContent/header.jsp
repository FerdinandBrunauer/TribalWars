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
<link href="../../bower_components/morrisjs/morris.css" rel="stylesheet">
<link href="../../bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
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
							<i class="fa fa-dashboard fa-fw"></i> Dashboard
						</a>
					</li>
					<li>
						<a href="#">
							<i class="fa fa-th fa-fw"></i> Vorlagen<span class="fa arrow"></span>
						</a>
						<ul class="nav nav-second-level">
							<li>
								<a href="flot.html">Bauvorlagen</a>
							</li>
							<li>
								<a href="morris.html">Truppenvorlagen</a>
							</li>
							<li>
								<a href="morris.html">Zuweisungen</a>
							</li>
						</ul>
						<!-- /.nav-second-level -->
					</li>
					<li>
						<a href="settings.jsp">
							<i class="fa fa-sliders fa-fw"></i> Einstellungen
						</a>
					</li>
				</ul>
			</div>
		</div>
		</nav>
		<div id="page-wrapper" style="min-height: 273px;">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header"><%=(request.getParameterMap().containsKey("header")) ? request.getParameter("header") : "Die St&auml;mme Bot"%></h1>
				</div>
			</div>