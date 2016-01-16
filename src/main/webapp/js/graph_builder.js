/**
 * 
 */
$('document').ready(function() {

	// measurementsList contains contains all data displayed in graph
	var measurementsList = [];
	var pointID = 0;
	
	
	var graphbuilder = function buildGraph() {
		

		var margin = {top: 20, right: 80, bottom: 30, left: 50},
		    width = 960 - margin.left - margin.right,
		    height = 500 - margin.top - margin.bottom;

		var parseDate = d3.time.format("%Y%m%d").parse;

		var x = d3.time.scale()
		    .range([0, width]);

		var y = d3.scale.linear()
		    .range([height, 0]);

		var color = d3.scale.category10();

		var xAxis = d3.svg.axis()
		    .scale(x)
		    .orient("bottom");

		var yAxis = d3.svg.axis()
		    .scale(y)
		    .orient("left");

		var line = d3.svg.line()
		    .interpolate("basis")
		    .x(function(d) { return x(d.date); })
		    .y(function(d) { return y(d.temperature); });

		var svg = d3.select(".realtime-graph").append("svg")
		    .attr("width", width + margin.left + margin.right)
		    .attr("height", height + margin.top + margin.bottom)
		  .append("g")
		    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

		d3.tsv("data.tsv", function(error, data) {
		  if (error) throw error;

		  color.domain(d3.keys(data[0]).filter(function(key) { return key !== "date"; }));

		  data.forEach(function(d) {
		    d.date = parseDate(d.date);
		  });

		  var probes = color.domain().map(function(name) {
		    return {
		      name: name,
		      values: data.map(function(d) {
		        return {date: d.date, temperature: +d[name]};
		      })
		    };
		  });

		  x.domain(d3.extent(data, function(d) { return d.date; }));

		  y.domain([
		    d3.min(probes, function(c) { return d3.min(c.values, function(v) { return v.temperature; }); }),
		    d3.max(probes, function(c) { return d3.max(c.values, function(v) { return v.temperature; }); })
		  ]);

		  svg.append("g")
		      .attr("class", "x axis")
		      .attr("transform", "translate(0," + height + ")")
		      .call(xAxis);

		  svg.append("g")
		      .attr("class", "y axis")
		      .call(yAxis)
		    .append("text")
		      .attr("transform", "rotate(-90)")
		      .attr("y", 6)
		      .attr("dy", ".71em")
		      .style("text-anchor", "end")
		      .text("Temperature (ºF)");

		  var points = svg.selectAll(".point")
		      .data(probes)
		    .enter().append("g")
		      .attr("class", "city");

		  city.append("path")
		      .attr("class", "line")
		      .attr("d", function(d) { return line(d.values); })
		      .style("stroke", function(d) { return color(d.name); });

		  city.append("text")
		      .datum(function(d) { return {name: d.name, value: d.values[d.values.length - 1]}; })
		      .attr("transform", function(d) { return "translate(" + x(d.value.date) + "," + y(d.value.temperature) + ")"; })
		      .attr("x", 3)
		      .attr("dy", ".35em")
		      .text(function(d) { return d.name; });
		});
	};
	
	function findObjectById(id) {
	    if (root.children) {
	        for (var k in root.children) {
	            if (root.children[k].probe == id) {
	                return root.children[k];
	            }
	            else if (root.children.length) {
	                return findObjectById(root.children[k], id);
	            }
	        }
	    }
	};
	
	console.log("Kikoo")
	
	time = setInterval(function() {
		// your code
		$.soap({
			url : 'http://localhost:8081/ws/TemperatureWS',
			method : 'getAllCSVTemperatureMeasurements',

			appendMethodToURL : false,
			data : {
				arg0 : '0',
				arg1 : 'false'
				
			},	
			envAttributes: {                                // additional attributes (like namespaces) for the Envelope:
		        'xmlns:ws': 'http://ws.front.brewspberry.net/'
		    },
			success : function(soapResponse) {

				var returnTags = soapResponse.getElementByTagName("return");

				for (i = 0; i < returnTags.length; i++) {
					
					var date = returnTags[i].getElementByTagName("tmes_date")[0].childNodes[0].nodeValue;
					var probeName = returnTags[i].getElementByTagName("tmes_probe_name")[0].childNodes[0].nodeValue;
					var probeValue = returnTags[i].getElementByTagName("tmes_value")[0].childNodes[0].nodeValue;
					
					console.log('Got that : date='+date+', name='+probeName+', value='+probeValue);
					
					var JSONtoAdd = {
								"probe" : probeName,
								"data" : [
								 {
									"date" : date,
									"value" : probeValue	
								 }
								]
							};
					
					// appends a new value to correct probe value list
					var obj = findObjectById(measurementsList, probeName);
					
					obj.children.push(JSOntoAdd);
					
					buildGraph();
					
				}

			},
			error : function(SOAPResponse) {
				console.log('FATAL : SOAP call failed !!');
			}
		});

	}, 1000);
});