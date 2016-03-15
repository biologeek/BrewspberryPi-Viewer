<%@page import="net.brewspberry.business.beans.SimpleLevure"%>
<%@page import="net.brewspberry.business.beans.SimpleHoublon"%>
<%@page import="net.brewspberry.business.beans.SimpleMalt"%>
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
	List<SimpleMalt> maltList = (List<SimpleMalt>) request.getAttribute("maltList");
		List<SimpleHoublon> hopList = (List<SimpleHoublon>) request
		.getAttribute("hopList");
		List<SimpleLevure> yeastList = (List<SimpleLevure>) request
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
	Smal_counter = 2;
	hop_counter = 2;
	yeast_counter = 2;

	function addABlock(blockType) {
		var block = '';

		if (blockType == 0) {

			var original = $('#malts-menus1');
			var newID = "malts-menus" + Smal_counter;
			console.log(original);
			var clone = original.clone(true, true);
			console.log(clone);
			$('#malts-container').append(clone.prop('id', newID));
			console.log(clone.attr('id'));
			clone.children('.control-group').first().children('label').text(
					'Malt ' + Smal_counter);
			clone.children('.control-group').first().children('.controls')
					.first().children('select').attr('id',
							'select0' + Smal_counter);
			console.log(clone.children('.control-group').first().children(
					'label').text());
			console.log(clone.children('.control-group').first().children(
					'.controls').first().children('select').attr('id'));

			Smal_counter += 1;
			console.log('Adding to malts');

			return Smal_counter - 1
		} else if (blockType == 1) {

			var original = $('#hops-menus1');
			var newID = "hops-menus" + hop_counter;
			console.log('hops-menus' + hop_counter - 1);
			console.log(original);
			var clone = original.clone(true, true);
			console.log(clone);
			$('#hops-container').append(clone.prop('id', newID));
			console.log(clone.attr('id'));
			clone.children('.control-group').first().children('label').text(
					'Malt ' + hop_counter);
			clone.children('.control-group').first().children('.controls')
					.first().children('select').attr('id',
							'select0' + hop_counter);
			console.log(clone.children('.control-group').first().children(
					'label').text());
			console.log(clone.children('.control-group').first().children(
					'.controls').first().children('select').attr('id'));

			console.log('Adding to hops');
			hop_counter += 1;
			return hop_counter - 1
		} else if (blockType == 2) {

			var original = $('#yeasts-menus1');
			var newID = "yeasts-menus" + yeast_counter;
			console.log('yeasts-menus' + yeast_counter - 1);
			console.log(original);
			var clone = original.clone(true, true);
			console.log(clone);
			$('#yeasts-container').append(clone.prop('id', newID));
			console.log(clone.attr('id'));
			clone.children('.control-group').first().children('label').text(
					'Malt ' + yeast_counter);
			clone.children('.control-group').first().children('.controls')
					.first().children('select').attr('id',
							'select0' + yeast_counter);
			console.log(clone.children('.control-group').first().children(
					'label').text());
			console.log(clone.children('.control-group').first().children(
					'.controls').first().children('select').attr('id'));

			console.log('Adding to yeasts');
			yeast_counter += 1;
			return yeast_counter - 1
		}

	}

	$(document).ready(function() {
		preSelectIngredientOnUpdate();
		preSelectStatusOnUpdate('${statutValue}');
	});

	function preSelectStatusOnUpdate(statusNumericValue) {
		if (statusNumericValue != '') {

			console.log('Selecting status option ' + statusNumericValue);
			$('#selectStatus option[value="20"]').attr("selected", true);
		}
	}
	function preSelectIngredientOnUpdate() {

		var ingredients = '${JSONIngredientsValue}';

		if (ingredients != '') {
			console.log('Updating brew with JSON');
			var ingredientsJSON = JSON.parse(ingredients);
			console.log(ingredientsJSON.ingredients);

			var Smal_counter = 0;
			var hop_counter = 0;
			var yeast_counter = 0;

			for (var i = 0; i < ingredientsJSON.ingredients.length; i++) {

				// If i == 0, fills the first menu 
				if (ingredientsJSON.ingredients[i].typeIng == "malt") {
					if (Smal_counter == 0) {
						console.log('Malt value : '
								+ ingredientsJSON.ingredients[i].id)
						$('#malts-menus1').find('select').val(
								ingredientsJSON.ingredients[i].id);

						$('#malts-menus1').find('input[name=maltQte]').val(
								ingredientsJSON.ingredients[i].qte);
						Smal_counter += 1;
					} else {
						var newBlockID = addABlock(0);
						console.log('newBlockID ' + newBlockID);
						//$('malts-menus'+newBlockID+'.select').val = i;
						console.log('Malt value : '
								+ ingredientsJSON.ingredients[i].id
								+ ' for block malts-menus' + newBlockID)
						$('malts-menus' + newBlockID).find('select').val(
								ingredientsJSON.ingredients[i].id);
						//console.log ("Blabla"+$('malts-menus'+newBlockID+'.select').val);

						$('#malts-menus' + newBlockID).find(
								'input[name=maltQte]').val(
								ingredientsJSON.ingredients[i].qte);
					}
				} else if (ingredientsJSON.ingredients[i].typeIng == "hop") {
					if (hop_counter == 0) {
						$(
								'hops-menus1.select option[value="'
										+ ingredientsJSON.ingredients[i].id
										+ '"]').attr('selected', 'selected');
						hop_counter += 1;
					} else {
						var newBlockID = addABlock(1);
						$(
								'hops-menus' + newBlockID
										+ '.select option[value="' + i + '"]')
								.attr('selected', 'selected');
					}
				} else if (ingredientsJSON.ingredients[i].typeIng == "yeast") {
					if (yeast_counter == 0) {
						$(
								'yeasts-menus1.select option[value="'
										+ ingredientsJSON.ingredients[i].id
										+ '"]').attr('selected', 'selected');
						yeast_counter += 1;
					} else {
						var newBlockID = addABlock(2);
						//$('yeasts-menus'+newBlockID+'.select').val = i;
						$(
								'yeasts-menus' + newBlockID
										+ '.select option[value="' + i + '"]')
								.attr('selected', 'selected');
					}
				} else {
					console.log("type"
							+ ingredientsJSON.ingredients.element[i].typeIng
							+ " corresponds to nothing");

				}
			}
		}
	}
</script>

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
							<form action="AddOrUpdateBrew" class="form-horizontal"
								method="post">
								<fieldset>
									<legend>Brassin</legend>

									<div class="control-group">
										<label class="control-label" for="date01">Date de
											début :</label>
										<div class="controls">
											<input type="text" name="dateDebut" class="span6"
												id="typeahead" data-provide="typeahead" data-items="4"
												value="${dateDebutValue}">

											<p class="help-block">Format YYYY-MM-DD hh:mm:ss</p>
										</div>
									</div>

									<div class="control-group">
										<label class="control-label" for="date01">Date de fin
											:</label>
										<div class="controls">
											<input type="text" name="dateFin" class="span6"
												id="typeahead" data-provide="typeahead" data-items="4"
												value="${dateFinValue}">

											<p class="help-block">Format YYYY-MM-DD hh:mm:ss</p>
										</div>
									</div>

									<div class="control-group">
										<label class="control-label" for="typeahead">Nom du
											brassin</label>
										<div class="controls">
											<input type="text" name="brassinNom" class="span6"
												id="typeahead" data-provide="typeahead" data-items="4"
												value="${brassinNomValue}">

										</div>
									</div>

									<div class="control-group">
										<label class="control-label" for="typeahead">Quantité
											(en L) </label>
										<div class="controls">
											<input type="text" name="brassinQte" class="span6"
												id="typeahead" data-provide="typeahead" data-items="4"
												value="${brassinQteValue}">

										</div>
									</div>

									<div class="control-group">
										<label class="control-label" for="select01">Statut</label>
										<div class="controls">
											<select name="statutBrassin" id="selectStatus"
												class="chzn-select">
												<option value="10">Brassage</option>
												<option value="20">Fermentation</option>
												<option value="30">Garde</option>
												<option value="40">Filtration</option>
												<option value="50">Embouteillée</option>
												<option value="70">A déguster</option>
												<option value="80">En cours de dégustation</option>
												<option value="90">Terminée</option>
											</select>
										</div>
									</div>


									<br /> <br />
									<legend>Ingrédients</legend>

									<div class="malts-container" id="malts-container">
										<div class="malts-menus" id="malts-menus1">
											<div class="control-group">
												<label class="control-label" for="select01">Malt 1</label>
												<div class="controls">
													<select name="malt" id="select01" class="malt-select">

														<c:forEach items="${maltList}" var="malt">
															<option value="${malt.getIng_id()}">Malt
																${malt.getSmal_cereale()} ${malt.getSmal_type()} -
																${malt.getIng_desc()}</option>
														</c:forEach>

													</select>

												</div>
											</div>
											<div class="control-group">
												<label class="control-label" for="select01">Quantité
													(kg)</label>
												<div class="controls">
													<input name="maltQte" class="input-xlarge focused"
														id="focusedInput" type="text" value="10">
												</div>
											</div>
										</div>


										<div class="control-group">
											<label class="control-label" for="select01">Ajouter
												un malt</label>
											<div class="controls">
												<button type="button" class="btn btn-mini"
													onclick="addABlock(0);">+</button>
											</div>
										</div>
									</div>



									<br /> <br />


									<div class="hops-container" id="hops-container">

										<div class="hops-menus" id="hops-menus1">
											<div class="control-group">
												<label class="control-label" for="select01">Houblon
													1</label>
												<div class="controls">
													<select name="houblon" id="select01" class="hop-select">

														<c:forEach items="${hopList}" var="hop">
															<option value="${hop.getIng_id()}">Houblon
																${hop.getShbl_variete()} (${hop.getShbl_acide_alpha()} %)</option>
														</c:forEach>

													</select>

												</div>
											</div>
											<div class="control-group">
												<label class="control-label" for="select01">Quantité
													(g)</label>
												<div class="controls">
													<input name="houblonQte" class="input-xlarge focused"
														id="focusedInput" type="text" value="10">
												</div>
											</div>
											<div class="control-group">
												<label class="control-label" for="select01">Type</label>
												<div class="controls">
													<select name="houblonType" id="select01" class="hop-select">
														<option value="0">Houblon amérisant</option>
														<option value="1">Houblon aromatique</option>
													</select>
												</div>
											</div>
										</div>

										<div class="control-group">
											<label class="control-label" for="select01">Ajouter
												un houblon</label>
											<div class="controls">
												<button type="button" class="btn btn-mini"
													onclick="addABlock(1);">+</button>
											</div>
										</div>
									</div>




									<br /> <br />



									<div class="lev-container" id="yeasts-container">

										<div class="yeasts-menus" id="yeasts-menus1">
											<div class="control-group">
												<label class="control-label" for="select01">Levure 1</label>
												<div class="controls">
													<select name="levure" id="select01" class="yeast-select">

														<c:forEach items="${yeastList}" var="yeast">
															<option value="${yeast.getIng_id()}">Levure
																${yeast.getIng_desc()}</option>
														</c:forEach>

													</select>
												</div>
											</div>
											<div class="control-group">
												<label class="control-label" for="select01">Quantité
													(g)</label>
												<div class="controls">
													<input name="levureQte" class="input-xlarge focused"
														id="focusedInput" type="text" value="10">
												</div>

											</div>
										</div>


										<div class="control-group">
											<label class="control-label" for="select01">Ajouter
												une levure</label>
											<div class="controls">
												<button type="button" class="btn btn-mini"
													onclick="addABlock(2);">+</button>
											</div>
										</div>
									</div>

									<br /> <br /> <input type="hidden" name="brassinID"
										value="${brassinIDValue}" /> <input type="hidden"
										name="registerStep" value="1" />
									<input type="hidden" name = "typeOfAdding" value = "brew">
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