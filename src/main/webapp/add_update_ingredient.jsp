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

<script type="text/javascript">
			
	function updatePageWithSpecificFields() {

		console.log ("Entering updatePageWithSpecificFields");
		var value = $("#ingredient_type").val();
		
		

		console.log ("Got : "+value);
		var codeToAppend;

		cleanUpSpecificPart("specific-parameters");
		
		switch (value) {

		case 'malt':
			

			codeToAppend = '<div class="control-group"> \
		<label class="control-label" for="date01">Malt cereal :</label> \
		<div class="controls"> \
			<select name="smal_cereale" id="select01" class="hop-select">\
			${maltSpecificCerealMenuOptions} \
			</select> \
		</div> \
	</div> \
		<div class="control-group"> \
		<label class="control-label" for="date01">Type of malt :</label> \
		<div class="controls"> \
			<select name="smal_type" id="select01" class="hop-select"> \
				${maltTypeSpecificMenuOptions} \
			</select> \
		</div> \
	</div> \
	<div class="control-group"> \
		<label class="control-label" for="date01">Color of malt (EBC) :</label> \
		<div class="controls"> \
			<input type="text" name="smal_couleur" class="span6" \
				id="typeahead" data-provide="typeahead" data-items="4" \
				value="${smal_couleur_val}"> \
		</div> \
	</div>';

			break;

		case 'hop':

			codeToAppend = '<div class="control-group"> \
					<label class="control-label" for="date01">Hop variety :</label> \
					<div class="controls"> \
					<input type="text" name="shbl_variete" class="span6" \
						id="typeahead" data-provide="typeahead" data-items="4" \
						value="${shbl_variete_val}">\
					</div> \
				</div> \
				<div class="control-group"> \
					<label class="control-label" for="date01">Alpha acids :</label> \
					<div class="controls"> \
						<input type="text" name="shbl_acide_alpha" class="span6" \
							id="typeahead" data-provide="typeahead" data-items="4" \
							value="${shbl_acide_alpha_val}">\
							</div> \
					</div> \
				<div class="control-group"> \
				<label class="control-label" for="date01">Aromas :</label> \
				<div class="controls"> \
					<input type="text" name="shbl_aromes" class="span6" \
						id="typeahead" data-provide="typeahead" data-items="4" \
						value="${shbl_aromes_val}"> \
						</div> \
						</div> \
				<div class="control-group"> \
				<label class="control-label" for="date01">Use :</label> \
				<div class="controls"> \
					<select name="shbl_type" id="select01" class="hop-select"> \
						<option value="default" selected="selected"></option> \
						${hopTypeSpecificMenuOptions} \
					</select> \
							</div> \
					</div> \
	</div>';

			break;

		case 'yeast':

			codeToAppend = '<div class="control-group"> \
					<label class="control-label" for="date01">Yeast species :</label> \
					<div class="controls"> \
					<select name="smal_type" id="select01" class="hop-select"> \
					<option value="default" selected="selected"></option> \
					<option value="Saccharomyces cerevisiae">Saccharomyces cerevisiae</option> \
					<option value="Saccharomyces uvarum">Saccharomyces uvarum</option> \
				</select> \
					</div> \
				</div> \
				<div class="control-group"> \
					<label class="control-label" for="date01">Floculation :</label> \
					<div class="controls"> \
					<select name="slev_floculation" id="select01" class="hop-select"> \
						<option value="default" selected="selected"></option> \
						<option value="faible">Low</option> \
						<option value="moyenne">Medium</option> \
						<option value="forte">High</option> \
					</select> \
					</div> \
					</div> \
				<div class="control-group"> \
					<label class="control-label" for="date01">Aromas :</label> \
				<div class="controls"> \
					<input type="text" name="slev_aromes" class="span6" \
						id="typeahead" data-provide="typeahead" data-items="4" \
						value="${slev_aromes_val}"> \
			</div>';
			break;

		}
		
		
		
		$("#specific-parameters").append(codeToAppend);

	}
	
	function cleanUpSpecificPart(spec){
		
		$("#"+spec).empty();
		
	}
	</script>

<script type="text/javascript">
jQuery(document).ready(function(){
	console.log('Top success : ${topSuccess}');
	// Adds success field if ingredient is created
	if (${topSuccess} == '1') {
		
		console.log('Prepending content')
		$('#content').prepend ('<div class=\"alert alert-success\"><button type=\"button\" class=\"close\" data-dismiss=\"alert\">&times;</button><h4>Success</h4>The operation completed successfully</div>');
	}
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
			<div class="span9 main-block" id="content">
			
				<div class="block">
					<div class="navbar navbar-inner block-header">
						<div class="muted pull-left">Ajouter un brassin</div>
					</div>
					<div class="block-content collapse in">
						<div class="span12">
							<form action="ingredientServlet" class="form-horizontal"
								method="post">
								<fieldset>
									<legend>Ingrédient</legend>

									<div class="control-group">
										<label class="control-label" for="date01">Type of
											ingredient:</label>
										<div class="controls">

											<select name="ingredient_type" id="ingredient_type"
												class="hop-select"
												onchange="updatePageWithSpecificFields();">
												<option value="default" selected="selected"></option>
												<c:forEach items="${ingredientTypes}" var="ing">
													<option value="${ing.key}"
														<c:if test="${ing.key == ingredientType}"> selected="selected" </c:if>>
														${ing.value}</option>

												</c:forEach>

											</select>
										</div>
									</div>


									<div class="control-group">
										<label class="control-label" for="date01">Description
											:</label>
										<div class="controls">


											<input type="text" name="ing_description" class="span6"
												id="typeahead" data-provide="typeahead" data-items="4"
												value="${ing_fdescription_val}">
										</div>
									</div>
									<div id="specific-parameters"></div>

									<br /> <input type="hidden" name="ingredient_id"
										value="${ingredient_id}" />

									<div class="control-group">
										<label class="control-label" for="date01">Provider :</label>
										<div class="controls">


											<input type="text" name="ing_fournisseur" class="span6"
												id="typeahead" data-provide="typeahead" data-items="4"
												value="${ing_fournisseur_val}">
										</div>
									</div>

									<div class="form-actions">
										<button type="submit" class="btn btn-primary">Save
											changes</button>
										<button type="reset" class="btn">Cancel</button>
									</div>
								</fieldset>
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