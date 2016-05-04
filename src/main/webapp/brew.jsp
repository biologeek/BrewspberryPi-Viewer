<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="net.brewspberry.business.beans.Brassin"%>
<%@page import="java.lang.Math"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	Brassin brassin = (Brassin) request.getAttribute("brew");
%>

<!-- Bootstrap -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet"
	media="screen">
<link href="assets/styles.css" rel="stylesheet" media="screen">
<link href="vendors/jGrowl/jquery.jgrowl.css" rel="stylesheet"
	media="screen">

<script   src="https://code.jquery.com/jquery-2.2.2.min.js"></script>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.2/Chart.min.js"></script>
<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

<script type="text/javascript" src="http://momentjs.com/downloads/moment-with-locales.min.js">
</script>
<script type="text/javascript" src="js/graphRefresher.js"></script>

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
<script src="vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>

<script type="text/javascript">
	function changeActionerState(brew, step, actionerUUID, actionerID, actionerType) {

		if (brew != null && step != null) {

			if (actionerID > 0) {

				jQuery
						.ajax(
								{
									url : "${actionerServiceAddress}?type=deactivate&id="
											+ actionerUUID
											+ "&bid="
											+ brew
											+ "&eid=" + step,
									context : document.body
								}).done(function() {
									switch (actionerType){
									case '1' :
										/*
										 * DS18B20
										 */
										$('#IMGACT'+actionerUUID).attr('src', 'images/thermo-off.jpg');
										break;
										

									case '2' :
										/*
										 * PUMP
										 */
										$('#IMGACT'+actionerUUID).attr('src', 'images/pump-off.jpg');
										break;
										

									case '3' :
										/*
										 * ENGINE
										 */
										$('#IMGACT'+actionerUUID).attr('src', 'images/engine-off.png');
										break;
										 
									}
						});

			}
			jQuery.ajax(
					{
						url : "${actionerServiceAddress}?type=activate&uuid="
								+ actionerUUID + "&bid=" + brew + "&eid="
								+ step,
						context : document.body
					}).done(function() {
						switch (actionerType){
						case '1' :
							/*
							 * DS18B20
							 */
							$('#IMGACT'+actionerUUID).attr('src', 'images/thermo-on.jpg');
							break;
							

						case '2' :
							/*
							 * PUMP
							 */
							$('#IMGACT'+actionerUUID).attr('src', 'images/pump-on.jpg');
							break;
							

						case '3' :
							/*
							 * ENGINE
							 */
							$('#IMGACT'+actionerUUID).attr('src', 'images/engine-on.png');
							break;
							 
						}
			});

		}

	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Brassin n°${brassin.getBra_id()}</title>
</head>

<body class="wysihtml5-supported">

	<jsp:include page="tpl/header.jsp"></jsp:include>


	<div class="container-fluid">

		<div class="row-fluid">
			<jsp:include page="tpl/sidebar.jsp"></jsp:include>
			<!--/span-->
			<div class="span9" id="content">

				<div class="block">
					<div class="navbar navbar-inner block-header">
						<div class="muted pull-left"
							style="font-weight: bold; text-align: center;">${brassin.getBra_nom()}</div>
					</div>
				</div>

				<!-- Loop over each step -->
				<c:if test="${steps.size() > 0}">
					<c:forEach begin="0" end="${steps.size()}" var="loop">
						<c:if test="${steps[loop].getEtp_id() != null}">
							<div class="row-fluid">
								<div class="span6">
									<!-- block -->
									<div class="block">
										<div class="navbar navbar-inner block-header">
											<div class="muted pull-left">Etape</div>
											<div class="pull-right">
												<span class="badge badge-info">${steps[loop].getEtp_numero()}</span>

											</div>
										</div>
										<div class="block-content collapse in">
											<table class="table table-striped"
												style="table-layout: fixed;">
												<thead>
													<tr>
														<th>#</th>
														<th>Label</th>
														<th>Durée</th>
														<th>Température</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>${steps[loop].getEtp_numero()}</td>
														<td>${steps[loop].getEtp_nom()}</td>
														<td>${steps[loop].getEtp_duree()}</td>
														<td>${steps[loop].getEtp_temperature_theorique()}</td>
													</tr>

													<tr>
														<td colspan="4"><c:forEach
																items="${availableActioners}" var="actioner">
																<a href="#"
																	onclick="changeActionerState('${steps[loop].getEtp_brassin().getBra_id()}','${steps[loop].getEtp_id()}','${actioner.getAct_uuid()}','${actioner.getAct_id()}', '${actioner.getAct_type()}')">
																	<c:if test="${actioner.getAct_type() == 1}">
																		<img id="IMGACT${actioner.getAct_uuid()}" style="height:50px; width:50px;" src="images/thermo-off.jpg" alt="${steps[loop].getEtp_nom()}" ></a>
																	</c:if>
																	
																	<c:if test="${actioner.getAct_type() == 2}">
																		<img id="IMGACT${actioner.getAct_uuid()}" style="height:50px; width:50px;" src="images/pump-off.jpg" alt="${steps[loop].getEtp_nom()}" ></a>
																	</c:if>
																	
																	<c:if test="${actioner.getAct_type() == 3}">
																		<img id="IMGACT${actioner.getAct_uuid()}" style="height:40px; width:60px;" src="images/engine-off.png" alt="${steps[loop].getEtp_nom()}" ></a>
																	</c:if>
																	
															</c:forEach></td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
									<!-- /block -->
								</div>
								<div class="span6">
									<!-- block -->
									<div class="block">
										<div class="navbar navbar-inner block-header">
											<div class="muted pull-left">Etape</div>
											<div class="pull-right">
												<span class="badge badge-info">${steps[loop].getEtp_numero()}</span>

											</div>
										</div>
										<div class="block-content collapse in" id="CHART${loop}"'>
											<canvas id="CANVAS${loop}" width="450px" height = "300px"'>
											<script>
											console.log ('Executing...')

												execute('CANVAS${loop}', '${steps[loop].getEtp_id()}', 'all', 40);
											
											</script>
											</canvas>

										</div>
									</div>
								</div>
							</div>
						</c:if>

					</c:forEach>
				</c:if>
				<div class="row-fluid">

					<!-- block -->
					<div class="block">
						<div class="navbar navbar-inner block-header">
							<div class="muted pull-left">Ajouter une étape</div>
							<div class="pull-right">
								<span class="badge badge-info">${steps.size()}</span>

							</div>
						</div>
						<div class="block-content collapse in">
							<form
								action="AddOrUpdateBrew?typeOfAdding=step&bid=${brassin.getBra_id()}"
								class="form-horizontal" method="post">
								<table class="table table-striped">
									<tbody>
										<tr>
											<td>Label</td>
											<td><input type="text" name="step_label" /><</td>

											<td>Durée théorique</td>
											<td><input type="text" name="step_duration" /></td>
										</tr>
										<tr>
											<td>Début</td>
											<td id= "stepBeginningAdd"><input type="text" name="step_beginnging" />
												<script type="text/javascript">
													$(function(){
														
														$("#stepBeginningAdd").datetimepicker();
													}												
												</script>
											</td>

											<td>Fin</td>
											<td id= "stepEndAdd"><input type="text" name="step_end" />
											<script type="text/javascript">
													$(function(){
														
														$("#stepEndAdd").datetimepicker();
													}
												</script>
												</td>
										</tr>
										<tr>
											<td>Température théorique</td>
											<td><input type="text" name="step_temperature" /></td>

											<td>Commentaire</td>
											<td><textarea name="step_comment" rows="3" cols="15"></textarea></td>
										</tr>
										<tr>
											<td>Etape n°</td>
											<td><input type="text" name="step_number"
												value="${steps.size()}" /></td>
											<td></td>
											<td></td>
											<td>
										</tr>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td><button type="submit" class="btn btn-primary">+</button></td>
										</tr>
									</tbody>
								</table>
							</form>
						</div>
						<!-- /block -->
					</div>
				</div>
				<div class="row-fluid">
					<!-- block -->
					<div class="block">

						<div class="navbar navbar-inner block-header">
							<div class="muted pull-left">Profil complet de température</div>

						</div>
						<a
							href="${tempServlet}?type=bra&bid=${brassin.getBra_id()}&width=900&height=300">
							<img alt="JFreeGraph"
							src="${tempServlet}?type=bra&bid=${brassin.getBra_id()}&width=900&height=300"
							style="height: 300px; width: 900px; margin: 0 auto; display: block;" />
						</a>

					</div>
				</div>
				<div class="row-fluid">
					<!-- block -->
					<div class="block">

						<div class="navbar navbar-inner block-header">
							<div class="muted pull-left">Bière</div>
						</div>
						<div class="block-content collapse in">
							<table class="table table-striped">

								<tr>
									<td style="width: 20%;">Nom :</td>
									<td style="width: 30%;">${brassin.getBra_beer().getBeer_name()}</td>
									<td style="width: 20%;">Style :</td>
									<td style="width: 30%;">${brassin.getBra_beer().getBeer_style()}</td>
								</tr>
								<tr>
									<td style="width: 20%;">Taux d'alcool :</td>
									<td style="width: 30%;">${brassin.getBra_beer().getBeer_alcohol()}</td>
									<td style="width: 20%;">Densité :</td>
									<td style="width: 30%;">${brassin.getBra_beer().getBeer_density()}</td>
								</tr>
								<tr>
									<td style="width: 20%;">Couleur (EBC) :</td>
									<td style="width: 30%;">${brassin.getBra_beer().getBeer_color_ebc()}</td>
									<td style="width: 20%;">Aromes :</td>
									<td style="width: 30%;">${brassin.getBra_beer().getBeer_aroma()}</td>
								</tr>
								<tr>
									<td style="width: 20%;">Note / 10 :</td>
									<td style="width: 30%;">${brassin.getBra_beer().getBeer_notation()}</td>
									<td style="width: 20%;">Bulles :</td>
									<td style="width: 30%;">${brassin.getBra_beer().getBeer_bubbles()}</td>
								</tr>
								<tr>
									<td style="width: 20%;">Commentaire :</td>
									<td style="width: 80%;" colspan="3">${brassin.getBra_beer().getBeer_comment()}</td>
								</tr>

								<tr>
									<td style="width: 20%;">Embouteillées :</td>
									<td style="width: 30%;">${brassin.getBra_beer().getBeer_init_bottles()}</td>
									<td style="width: 20%;">Restantes :</td>
									<td style="width: 30%;">${brassin.getBra_beer().getBeer_remaining_bottles()}</td>
								</tr>

								<tr>
									<td style="width: 20%;">Progression :</td>
									<td style="width: 30%;"><c:choose>
											<c:when
												test="${brassin.getBra_beer().getBeer_conso_progress() < 33}">

												<div
													class="progress progress-striped progress-success active">
													<div
														style="width: ${brassin.getBra_beer().getBeer_conso_progress()}%;"
														class="bar"></div>
												</div>

											</c:when>
											<c:when
												test="${brassin.getBra_beer().getBeer_conso_progress() >= 33 && brassin.getBra_beer().getBeer_conso_progress() < 66}">
												<div
													class="progress progress-striped progress-warning active">
													<div
														style="width: ${brassin.getBra_beer().getBeer_conso_progress()}%;"
														class="bar"></div>
												</div>
											</c:when>
											<c:when
												test="${brassin.getBra_beer().getBeer_conso_progress() >= 66}">
												<div
													class="progress progress-striped progress-danger active">
													<div
														style="width: ${brassin.getBra_beer().getBeer_conso_progress()}%;"
														class="bar"></div>
												</div>
											</c:when>


										</c:choose></td>
									<td style="width: 20%;">Première goulée :</td>
									<td style="width: 30%;"><fmt:formatDate
											value="${brassin.getBra_beer().getBeer_first_drink_date()}"
											pattern="dd/MM/yyyy à HH:mm:ss" /></td>
								</tr>

							</table>

						</div>

					</div>
				</div>

			</div>
		</div>
	</div>
	<jsp:include page="tpl/footer.jsp"></jsp:include>
</body>
</html>