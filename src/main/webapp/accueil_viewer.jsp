<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@page import="net.brewspberry.business.beans.Brassin"%>
<%@page import="java.util.List"%>
<%@page import="net.brewspberry.business.beans.Malt"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<head>
<title>BrewViewer</title>
<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet"
	media="screen">
<link href="assets/styles.css" rel="stylesheet" media="screen">
<link href="vendors/jGrowl/jquery.jgrowl.css" rel="stylesheet"
	media="screen">
<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
<script src="js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>
</head>

<% List<Brassin> brewList = (List<Brassin>) request.getAttribute("brewList"); %>

<body>

	<jsp:include page="tpl/header.jsp"></jsp:include>


	<div class="container-fluid">

		<div class="row-fluid">
			<jsp:include page="tpl/sidebar.jsp"></jsp:include>
			<!--/span-->
			<div class="span9" id="content">
				<div class="row-fluid">
					<!-- block -->
					<div class="block">
						<div class="navbar navbar-inner block-header">
							<div class="muted pull-left">Résumé des bières</div>
						</div>
						<div class="block-content collapse in">
							<div class="span12">
								<table class="table">
									<thead>
										<tr>
											<th>#</th>
											<th>Nom</th>
											<th>Début</th>
											<th>Fin</th>
											<th>Type</th>
											<th>Progression</th>
										</tr>
									</thead>
									<tbody>
										
										<c:forEach items="${brewList}" var="brew">
											<tr>
							                  	<td>${brew.getBra_id()}</td>
							                  	<td><a href="Accueil?bid=${brew.getBra_id()}">${brew.getBra_nom()}</a></td>
							                  	<td><fmt:formatDate value="${brew.getBra_debut()}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
							                  	<td><fmt:formatDate value="${brew.getBra_fin()}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
							                  	<td>${brew.getBra_type()}</td>
							                  	<td>
									                <div class="progress progress-striped active">
														<div style="width: ${brew.getBra_statut()}%;" class="bar"></div>
													</div>
												</td>
						               		</tr>
						                </c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<!-- /block -->
				</div>
			</div>
		</div>

	</div>
	<!-- TODO : Compléter avec la page interface.html du template -->

	<jsp:include page="tpl/footer.jsp"></jsp:include>
</body>
</html>