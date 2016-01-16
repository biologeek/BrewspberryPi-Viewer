/**
 * 
 */

malt_counter = 2;
hop_counter = 2;
yeast_counter = 2;

function addABlock(blockType) {
	var block = '';

	if (blockType == 0) {

		block = '<div class="control-group">\
					<label class="control-label" for="select01">Malt '
					+ malt_counter
					+ '</label>\
					<div class="controls">\
						<select name="malt"  id="select01" class="chzn-select malt-select">\
							<option>Malt Pilsen</option>\
							<option>Malt Caramel Vienne</option>\
							<option>Malt Caramel Munich</option>\
							<option>Malt Café</option>\
						</select>\
					</div>\
					</div>\
					<div class="control-group">\
						<label class="control-label" for="select01">Quantité\
							(kg)</label>\
						<div class="controls">\
							<input name="maltQte" class="input-xlarge focused"\
								id="focusedInput" type="text" value="10">\
						</div>\
					</div>\
				</div>';
		malt_counter += 1;
		console.log('Adding to malts');

		$('.malts-menus').append(block);

	} else if (blockType == 1) {
		block = '<div class="control-group">\
			<label class="control-label" for="select01">Houblon '
				+ hop_counter
				+ '</label>\
			<div class="controls">\
				<select name="houblon"  id="select01" class="chzn-select hop-select">\
					<option>Saaz</option>\
					<option>Tettnang</option>\
					<option>Brewer\'s Gold</option>\
				</select>\
			</div>\
				</div>\
			<div class="control-group">\
				<label class="control-label" for="select01">Quantité\
					(g)</label>\
				<div class="controls">\
					<input name="houblonQte" class="input-xlarge focused"\
						id="focusedInput" type="text" value="10">\
				</div>\
				</div>';
		hop_counter += 1;
		console.log('Adding');

		$('.hops-menus').append(block);

	} else if (blockType == 2) {
		block = '<div class="control-group">\
					<label class="control-label" for="select01">Levure '
					+ yeast_counter
					+ '</label>\
					<div class="controls">\
						<select name="levure"  id="select01" class="chzn-select yeast-select">\
							<option>Safale S04</option>\
							<option>Safale K97</option>\
							<option>Safale US05</option>\
						</select>\
					</div>\
					</div>\
					<div class="control-group">\
						<label class="control-label" for="select01">Quantité\
							(g)</label>\
						<div class="controls">\
							<input name="levureQte" class="input-xlarge focused"\
								id="focusedInput" type="text" value="10">\
						</div>\
				</div>';
		hop_counter += 1;
		console.log('Adding');

		$('.yeast-menus').append(block);

	}

}
