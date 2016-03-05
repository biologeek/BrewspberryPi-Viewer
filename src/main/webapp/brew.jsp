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

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
<script src="js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>

<script type="text/javascript">
	function changeActionerState(brew, step, actionerUUID, actionerID) {

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
							$(this).addClass("done");
						});

			}
			jQuery
					.ajax(
							{
								url : "${actionerServiceAddress}?type=activate&uuid="
										+ actionerUUID
										+ "&bid="
										+ brew
										+ "&eid=" + step,
								context : document.body
							}).done(function() {
						$(this).addClass("done");
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

				<c:forEach begin="0" end="${stepCounter}" var="loop">
					<c:if test="${steps[loop*2].getEtp_id() != null}">
						<div class="row-fluid">
							<div class="span6">
								<!-- block -->
								<div class="block">
									<div class="navbar navbar-inner block-header">
										<div class="muted pull-left">Etape</div>
										<div class="pull-right">
											<span class="badge badge-info">${steps[loop*2].getEtp_numero()}</span>

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
													<td>${steps[loop*2].getEtp_numero()}</td>
													<td>${steps[loop*2].getEtp_nom()}</td>
													<td>${steps[loop*2].getEtp_duree()}</td>
													<td>${steps[loop*2].getEtp_temperature_theorique()}</td>
												</tr>
												<tr>
													<td colspan="4"><a
														href="${tempServlet}?type=etp&eid=${steps[loop*2].getEtp_id()}&width=300&height=300">
															<img alt="JFreeGraph"
															src="${tempServlet}?type=etp&eid=${steps[loop*2].getEtp_id()}&width=300&height=300"
															style="height: 250px; width: 300px; margin: 0 auto; display: block;" />
													</a></td>
												</tr>

												<tr>
													<td colspan="4"><c:forEach
															items="${availableActioners}" var="actioner">
															<a href="#"
																onclick="changeActionerState('${steps[loop*2].getEtp_brassin().getBra_id()}','${steps[loop*2].getEtp_id()}','${actioner.getAct_uuid()}','${actioner.getAct_id()}')">${actioner.getAct_nom()}</a>
														</c:forEach></td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								<!-- /block -->
							</div>
							<c:if test="${loop*2+1 < steps.size()}">
								<div class="span6">
									<!-- block -->
									<div class="block">
										<div class="navbar navbar-inner block-header">
											<div class="muted pull-left">Etape</div>
											<div class="pull-right">
												<span class="badge badge-info">${steps[loop*2+1].getEtp_numero()}</span>

											</div>
										</div>
										<div class="block-content collapse in">
											<table class="table table-striped">
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
														<td>${steps[loop*2+1].getEtp_numero()}</td>
														<td>${steps[loop*2+1].getEtp_nom()}</td>
														<td>${steps[loop*2+1].getEtp_duree()}</td>
														<td>${steps[loop*2+1].getEtp_temperature_theorique()}</td>
													</tr>
													<tr>

														<td colspan="4"><a
															href="${tempServlet}?type=etp&eid=${steps[loop*2+1].getEtp_id()}&width=300&height=300">
																<img alt="JFreeGraph"
																src="${tempServlet}?type=etp&eid=${steps[loop*2+1].getEtp_id()}&width=300&height=300"
																style="height: 250px; width: 300px; margin: 0 auto; display: block;" />
														</a></td>
													</tr>
													<tr>
														<td colspan="4"><c:forEach
																items="${availableActioners}" var="actioner">
																<a href="#"
																	onclick="changeActionerState('${steps[loop*2+1].getEtp_brassin().getBra_id()}','${steps[loop*2+1].getEtp_id()}','${actioner.getAct_uuid()}','${actioner.getAct_id()}')">${actioner.getAct_nom()}</a>
															</c:forEach></td>
													</tr>
												</tbody>
											</table>
										</div>

									</div>
									<!-- /block -->
								</div>
							</c:if>
						</div>
						<c:set var="loop" value="${loop+1}"></c:set>
					</c:if>
				</c:forEach>
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
											<td><input type="text" name="step_label" /></td>

											<td>Durée théorique</td>
											<td><input type="text" name="step_duration" /></td>
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