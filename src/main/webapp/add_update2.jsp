<%@page import="net.brewspberry.business.beans.Levure"%>
<%@page import="net.brewspberry.business.beans.Houblon"%>
<%@page import="net.brewspberry.business.beans.Malt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.util.List"%>
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

<!--/.fluid-container-->
<link href="vendors/datepicker.css" rel="stylesheet" media="screen">
<link href="vendors/uniform.default.css" rel="stylesheet" media="screen">
<link href="vendors/chosen.min.css" rel="stylesheet" media="screen">

<link href="vendors/wysiwyg/bootstrap-wysihtml5.css" rel="stylesheet"
	media="screen">

<script src="vendors/jquery-1.9.1.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="vendors/jquery.uniform.min.js"></script>
<script src="vendors/chosen.jquery.min.js"></script>
<script src="vendors/bootstrap-datepicker.js"></script>

<script src="vendors/wysiwyg/wysihtml5-0.3.0.js"></script>
<script src="vendors/wysiwyg/bootstrap-wysihtml5.js"></script>

<script src="vendors/wizard/jquery.bootstrap.wizard.min.js"></script>

<script type="text/javascript"
	src="vendors/jquery-validation/dist/jquery.validate.min.js"></script>
<script src="assets/form-validation.js"></script>

<script src="assets/scripts.js"></script>



<%
	List<Malt> maltList = (List<Malt>) request.getAttribute("maltList");
		List<Houblon> hopList = (List<Houblon>) request
		.getAttribute("hopList");
		List<Levure> yeastList = (List<Levure>) request
		.getAttribute("yeastList");
%>
<script>
	jQuery(document).ready(function() {
		FormValidation.init();
	});

	$(function() {
		$(".datepicker").datepicker();
		$(".uniform_on").uniform();
		$(".chzn-select").chosen();
		$('.textarea').wysihtml5();

		$('#rootwizard').bootstrapWizard(
				{
					onTabShow : function(tab, navigation, index) {
						var $total = navigation.find('li').length;
						var $current = index + 1;
						var $percent = ($current / $total) * 100;
						$('#rootwizard').find('.bar').css({
							width : $percent + '%'
						});
						// If it's the last tab then hide the last button and show the finish instead
						if ($current >= $total) {
							$('#rootwizard').find('.pager .next').hide();
							$('#rootwizard').find('.pager .finish').show();
							$('#rootwizard').find('.pager .finish')
									.removeClass('disabled');
						} else {
							$('#rootwizard').find('.pager .next').show();
							$('#rootwizard').find('.pager .finish').hide();
						}
					}
				});
		$('#rootwizard .finish').click(function() {
			alert('Finished!, Starting over!');
			$('#rootwizard').find("a[href*='tab1']").trigger('click');
		});
	});
</script>



<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
<script src="vendors/modernizr-2.6.2-respond-1.1.0.min.js"></script>


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
						<div class="muted pull-left">Ajouter un brassin</div>
					</div>
					<div class="block-content collapse in">
						<div class="span12">
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
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- TODO : Compléter avec la page interface.html du template -->

	<jsp:include page="tpl/footer.jsp"></jsp:include>


</body>
</html>