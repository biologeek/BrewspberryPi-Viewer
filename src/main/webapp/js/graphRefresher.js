/*******************************************************************************
 * Using chartjs
 * 
 ******************************************************************************/

var rawDataFromServlet = [];

var liveChart;
var chartOptions ={animationSteps: 10};

var initServletAddress = 'http://192.168.0.20:8080/brewspberry-api/rest/initTemperatures';
var updateServletAddress = 'http://192.168.0.20:8080/brewspberry-api/rest/updateTemperatures';
	
var refreshDelay = 2000; // Refreshes every 2 s

var XplotTimeRangeInMinutes = 5;
var chartData = {};


var currentLastID = 0;
	//var testData = [{"date": "2016-03-16 18:15:55.0","temp": 16155,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:56.0","temp": 15980,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:57.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:58.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:59.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:00.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:01.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:02.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:03.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:04.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"}];
	var rawDataFromServlet = [{"date": "2016-03-16 18:15:55.0","temp": 16155,"name": "PROBE1","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:55.0","temp": 19155,"name": "PROBE1","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:55.0","temp": 16155,"name": "PROBE1","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:55.0","temp": 16155,"name": "PROBE1","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:55.0","temp": 15155,"name": "PROBE1","step": 8,"id": 1279,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:56.0","temp": 15980,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:57.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:58.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:59.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:00.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:01.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:02.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:03.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:04.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"}];


window.onload = function(){
buildDataSetsForChartJS(function(){

				buildGraph(chartData, 'graph');		

			});

	setInterval (function (){
			
			updateChartWithNewData ([{"id" : currentLastID+1, "name": "PROBE0","uuid": "28-000006ddab6e", "temp":Math.random()*20000, "date" : new Date()}]);
			updateChartWithNewData ([{"id" : currentLastID+1, "name": "PROBE1", "uuid": "28-123456789", "temp":Math.random()*20000, "date" : new Date()}]);
			
		}, refreshDelay);
}
//buildGraph(buildDataSetsForChartJS(testData), 'graph');

function execute (htmlID, step, probe){
	
	
	// Initiating chart
	getDataFromServlet(step, probe, true, 0, function(){
		
		console.log('Callback : '+rawDataFromServlet);
		cutTheChartAfterXMinutes(XplotTimeRangeInMinutes);
		buildDataSetsForChartJS(function(){

			buildGraph(chartData, htmlID);		

		});
		
	});

	console.log (rawDataFromServlet);

	
	
	// Updating
	setInterval (function (){
		
		getDataFromServlet (step, probe, false, currentLastID, function (){
			updateChartWithNewData(rawDataFromServlet);

		});
		
	}, refreshDelay);
	
	
}
	
	/**
	 * probe is a list of probes Receiving data formatted as such :
	 * 
	 * probe;date;temperature
	 * 
	 * from servlet.
	 * 
	 * Uses ajax GET Request to retrieve data
	 */
	function getDataFromServlet(step, probe, init, lastID, callback) {
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

		if (typeof step == "string" ){
				
				address +='/e/'+step+'';
			
			
		} else {
			
			alert ('step is not a string')
		}
		
		if (typeof probe == "string"){
				
				address +='/u/'+probe+'';
			
			
		} else {
			
			alert ('UUID is not a string')
		}



		if (!init && lastID > 0){
			
			address+= '/l/'+lastID;
			
		} else if (!init){
			
			console.log ('OK I\'m cool, setting ID to 1');
			address+= '/l/1';
		}
		if (!init){
			
			console.log ('/d/'+XplotTimeRangeInMinutes);
			address += '/d/'+XplotTimeRangeInMinutes;
			
		}

			/* if null => all probes */
		
		console.log('Calling '+address);
		// If query is OK setting rawDataFromServlet
		jQuery.ajax (address,{
			
			success : function (result){
				console.log('Call success');

				if (init){
					rawDataFromServlet = result;
				} else {
					rawDataFromServlet.concat(result);
				}


				callback();
			},
			error : function (request, status, error) {
				
				/*
				 * Will be error message displayed in jsp
				 * 
				 */
				

				console.log ('** Error when calling API !!');
				console.log ('** Status : '+status);
				rawDataFromServlet = new Array();
			}
		}
				
		);


	
	}
	
	function buildGraph(dataSets, divID) {
		console.log (document.getElementById('graph'));
	
		var ctx = document.getElementById(divID).getContext("2d");
		liveChart = new Chart(ctx).Line(dataSets, chartOptions);
	
		
	}
	
	/**
	 * Receiving data as a list of {probe;date;temperature} converting them to
	 * ChartJS datasets. Example :
	 * 
	 * [{"date":"2016-03-16
	 * 18:15:55.0","temp":16187,"name":"PROBE0","step":8,"id":1277,"brew":7,"uuid":"28-000006ddab6e"},...]
	 * 
	 * @param data
	 * @returns
	 */
	function buildDataSetsForChartJS (callback){
		
		var xLabels = [];
		var yValues = {}
		var datasets = {};
		var data = [];
		
		//console.log (rawDataFromServlet);		

		if (typeof rawDataFromServlet == "string"){
			
			data = jQuery.parseJSON(rawDataFromServlet);
		} else {
			
			data = rawDataFromServlet;
		}
		

		// For each item
		jQuery.each (data, function (i, item){
			// item : {"date":"2016-03-16
			// 18:15:55.0","temp":16187,"name":"PROBE0","step":8,"id":1277,"brew":7,"uuid":"28-000006ddab6e"}
			
			
			// yValues = {"PROBE1":[temperatures, ...], "PROBE2":[temperatures,
			// ...], "PROBE3":[temperatures, ...]...}
			
			//console.log ('PROBES : '+item.name);
			
			
			// Everytime a new date is added in xLabels
			xLabels.push(formatDateFromJavaToJS(item.date));
			
			var itemName = item.name;

			if (!yValues.hasOwnProperty(itemName)){
							
				yValues[itemName] = [];

			}
				yValues[itemName].push(item.temp);
			
			currentLastID = item.id;			
		});

		//console.log (xLabels)
		// Building final data for ChartJS

		chartData.labels = xLabels;
		chartData.datasets = [];
		
		//console.log (yValues);

		
		jQuery.each (yValues, function(i, item){
			
			//console.log (item);
			chartData.datasets.push (
				{
	
				    label: i,
				    fillColor: "rgba("+detemineColorForSet(i, yValues.length)+",0.2)",
				    strokeColor: "rgba("+detemineColorForSet(i, yValues.length)+",1)",
				    pointColor: "rgba("+detemineColorForSet(i, yValues.length)+",1)",
				    pointStrokeColor: "#fff",
				    pointHighlightFill: "#fff",
				    pointHighlightStroke: "rgba("+detemineColorForSet(i, yValues.length)+",1)",
				    data: item
					
				}					
			);
			//console.log(chartData);

		});

		callback();
		
	}
	
	/**
	 * Updates chart using data from service
	 */
	function updateChartWithNewData (data){
		
		cutTheChartAfterXMinutes(XplotTimeRangeInMinutes);
		if (typeof data == "string"){
			
			data = jQuery.parseJSON(data);
		}
		
		jQuery.each (data, function (i, item){
			//console.log (item);
			console.log(item);	
			liveChart.addData([item.temp], formatDateFromJavaToJS(item.date));
			
			currentLastID = item.id;
			
		});
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
	
	function formatDateFromJavaToJS (javaDate){
		
		var date = new Date (javaDate);
		
		
		var newDate = moment(date).format('LTS');
		
		//console.log (newDate);
		return newDate;
	}


	function cutTheChartAfterXMinutes (minutes){
		
		var newChartData = [];
		jQuery.each(rawDataFromServlet, function (i, item){

			if (typeof item.date != 'undefined'){
					//console.log(moment(item.date)+' : '+moment().subtract(minutes, 'minutes'));
				if (moment(item.date).isSameOrAfter(moment().subtract(minutes, 'minutes'))){
					
					liveChart.removeData();

					
				}
			}
			
		});
		

	}