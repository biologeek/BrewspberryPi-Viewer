/*******************************************************************************
 * Using chartjs
 * 
 ******************************************************************************/
$(document).ready(function(){
	var rawDataFromServlet = {};
	
	
	/* Defines colors taken by lines */
	var colors = ["220,220,220",
			"200,200,200",
			"240,240,240",
			"260,260,260",
			"280,280,280"];
	
	var initServletAddress = 'http://192.168.0.20:8080/brewspberry-api/rest/initTemperatures';
	var updateServletAddress = 'http://192.168.0.20:8080/brewspberry-api/rest/updateTemperatures';
	
	
	/**
	 * probe is a list of probes Receiving data formatted as such :
	 * 
	 * probe;date;temperature
	 * 
	 * from servlet.
	 * 
	 * Uses ajax GET Request to retrieve data
	 */
	function getDataFromServlet(probe, init, lastID) {
		/* if probe is not null retrieves data for this probe */
	
		
		var address = '';
		/*
		 * If it's not an init, only updates with last temperatures
		 */
		if (init){
			address = initServletAddress;
		} else {
			address = updateServletAddress;
		}
		
		if (typeof probe == "string" && probe != 'all'){
				
				address +='/u/'+uuid+'';
			
			
		} else {
			
			alert ('UUID is not a string')
		}
			/* if null => all probes */
		
		
		// If query is OK setting rawDataFromServlet
		jQuery.get (address,{
			
			success : function (result){
				
				this.rawDataFromServlet = result;
				console.log('Got :'+result);
			},
			error : function () {
				
				/*
				 * Will be error message displayed in jsp
				 * 
				 */			
			}
		}
				
		);
	
	}
	
	function buildGraph(loop, labels, dataSets, divID) {
	
		
		var ctx = document.getElementById(divID).getContext("2d");
	
		getDataFromServlet(probe, init, lastID);
		data = buildDataSetsForChartJS(labels, dataSets);
		
		var myLineChart = new Chart(ctx).Line(data, options);
	
		
	}
	
	/**
	 * Receiving data as a list of {probe;date;temperature} convetrting them to
	 * ChartJS datasets. Example :
	 * 
	 * [{"date":"2016-03-16
	 * 18:15:55.0","temp":16187,"name":"PROBE0","step":8,"id":1277,"brew":7,"uuid":"28-000006ddab6e"},...]
	 * 
	 * @param data
	 * @returns
	 */
	function buildDataSetsForChartJS (data){
		
		var xLabels = [];
		var yValues = {}
		var datasets = {};
		
		
		if (typeof data == "string"){
			
			data = jQuery.parseJSON(data);
		}
		
		
		// For each item
		jQuery.each (data, function (i, item){
			// item : {"date":"2016-03-16
			// 18:15:55.0","temp":16187,"name":"PROBE0","step":8,"id":1277,"brew":7,"uuid":"28-000006ddab6e"}
			
			
			// yValues = {"PROBE1":[temperatures, ...], "PROBE2":[temperatures,
			// ...], "PROBE3":[temperatures, ...]...}
			
			console.log ('PROBES : '+item.name);
			
			xLabels.push(item.date);
			
			var itemName = item.name;
			if (yValues.hasOwnProperty(itemName))){
				
				yValues.itemName.push(item.temp);
				
			}
			
			
		});

		// Building final data for ChartJS

		data.labels = xlabels;
		data.datasets = [];
		
		
		jQuery.each (yValues, function(i, item){
			
			console.log (item);
			data.datasets.push (
				{
	
		            label: item.key,
		            fillColor: "rgba("+colors[i]+",0.2)",
		            strokeColor: "rgba("+colors[i]+",1)",
		            pointColor: "rgba("+colors[i]+",1)",
		            pointStrokeColor: "#fff",
		            pointHighlightFill: "#fff",
		            pointHighlightStroke: "rgba("+colors[i]+",1)",
		            data: item
					
				}					
			);			
		});
		
		return data;
		
	}
	
	
	/**
	 * Calculates color of line for the dataset with ID id
	 * 
	 * Chooses 2 random numbers, third is 0
	 * 
	 * Returns RGB color code "xxx,yyy,zzz"
	 * 
	 */
	function detemineColorForSet(id, totalIDs){


		colorTable = [Math.floor((Math.random() * 255) + 1), Math.floor((Math.random() * 255) + 1), Math.floor((Math.random() * 255) + 1)];

		zeroIS = Math.floor((Math.random() * 2) + 1)
		
		colorTable [zeroIS] = 0;
		
		return colorTable.toString();
		
	} 


});