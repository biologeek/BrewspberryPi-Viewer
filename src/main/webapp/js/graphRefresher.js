/*******************************************************************************
 * Using chartjs
 * 
 ******************************************************************************/


	
var rawDataFromServlet = [];

var liveChart;
var chartOptions ={animationSteps: 10};

var initServletAddress = 'http://192.168.0.100:8080/brewspberry-api/rest/initTemperatures';
var updateServletAddress = 'http://192.168.0.100:8080/brewspberry-api/rest/updateTemperatures';
	
var refreshDelay = 5000; // Refreshes every 5 s

var maxPointsNumber = 60;
var XplotTimeRangeInMinutes = 5;
var chartData = {};


var currentLastID = 0;
	//var testData = [{"date": "2016-03-16 18:15:55.0","temp": 16155,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:56.0","temp": 15980,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:57.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:58.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:59.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:00.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:01.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:02.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:03.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"},{"date": "2016-03-16 18:15:04.0","temp": 16187,"name": "PROBE0","step": 8,"id": 1277,"brew": 7,"uuid": "28-000006ddab6e"}];


//buildGraph(buildDataSetsForChartJS(testData), 'graph');

function execute (htmlID, step, probe, maxPoints){
	
	maxPointsNumber = maxPoints;
	
	// Initiating chart
	getDataFromServlet(step, probe, true, 0, function(){
		

		buildDataSetsForChartJS(function(){

			buildGraph(chartData, htmlID);		
		});
		isDataSetAtMaximumSize ();

	});


	
	
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
			
			address += '/d/'+XplotTimeRangeInMinutes;
			
		}
		if (init){
			address+='/limitTo/'+maxPointsNumber;

		}

			/* if null => all probes */
		
		//console.log('Calling '+address);
		rawDataFromServlet = null;
		// If query is OK setting rawDataFromServlet
		jQuery.ajax (address,{
			
			success : function (result){
				//console.log('Call success');
		
				rawDataFromServlet = result;
			

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
		var yValues = {};
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

			//console.log ('Array : '+xLabels+ ' '+formatDateFromJavaToJS(item.date)+' '+typeof formatDateFromJavaToJS(item.date)+ ' '+xLabels.indexOf(formatDateFromJavaToJS(item.date)));
			if (xLabels.indexOf(formatDateFromJavaToJS(item.date)) == -1){
				xLabels.push(formatDateFromJavaToJS(item.date));
			}
		
			var itemName = item.name;

			if (!yValues.hasOwnProperty(itemName)){
							
				yValues[itemName] = [];

			}
			yValues[itemName].push(item.temp);
			currentLastID = item.id;			
		});


		// Building final data for ChartJS

		chartData.labels = xLabels;
		chartData.datasets = [];
		

		if (yValues.PROBE0.length > 0){

			jQuery.each (yValues, function(i, item){
			
				chartData.datasets.push (
					{
	
					    label: i,
					    fillColor: "rgba("+detemineColorForSet()+",0.2)",
					    strokeColor: "rgba("+detemineColorForSet()+",1)",
					    pointColor: "rgba("+detemineColorForSet()+",1)",
					    pointStrokeColor: "#fff",
					    pointHighlightFill: "#fff",
					    pointHighlightStroke: "rgba("+detemineColorForSet()+",1)",
					    data: item
					
					}					
				);

			});

		}
		else {
			
			chartData.datasets.push (

				{
	
				    label: "NOTAPROBE",
				    fillColor: "rgba("+detemineColorForSet(0, yValues.length)+",0.2)",
				    strokeColor: "rgba("+detemineColorForSet(0, yValues.length)+",1)",
				    pointColor: "rgba("+detemineColorForSet(0, yValues.length)+",1)",
				    pointStrokeColor: "#fff",
				    pointHighlightFill: "#fff",
				    pointHighlightStroke: "rgba("+detemineColorForSet()+",1)",
				    data: [20.0]
					
				});	
		}
		callback();
		
	}
	
	/**
	 * Updates chart using data from service
	 */
	function updateChartWithNewData (data){
		

		if (typeof data == "string"){
			
			data = jQuery.parseJSON(data);
		}
		
		var array=[];

		jQuery.each (data, function (i, item){
			//console.log(formatDateFromJavaToJS(item.date)+ ' '+array[0]);
			//console.log(array);
			if (typeof array[formatDateFromJavaToJS(item.date)] != 'undefined'){
				array[formatDateFromJavaToJS(item.date)].push(item.temp);
			} else {
				array[formatDateFromJavaToJS(item.date)] = [item.temp];
			}
						//console.log(array);

			currentLastID = item.id;
			
		});
		isDataSetAtMaximumSize();

		for (a in array){
			liveChart.addData(array[a], a);

		}
	}
	
	
	/**
	 * Calculates color of line for the dataset with ID id
	 * 
	 * Chooses 2 random numbers, third is 0
	 * 
	 * Returns RGB color code "xxx,yyy,zzz"
	 * 
	 */
	function detemineColorForSet(){


		colorTable = [Math.floor((Math.random() * 255) + 1), Math.floor((Math.random() * 255) + 1), Math.floor((Math.random() * 255) + 1)];

		zeroIS = Math.floor((Math.random() * 2) + 1)
		
		colorTable [zeroIS] = 0;
		
		return colorTable.toString();
		
	} 
	
	function formatDateFromJavaToJS (javaDate){
		
		var date = new Date (javaDate);
		
		
		var newDate = moment(date).format('LTS');
		
		return newDate;
	}




	function isDataSetAtMaximumSize (){
			
				if (liveChart.datasets.length > 0){
					var length = liveChart.datasets.length;
				} else {
					var length = 0;
				}
				
				//console.log(length);
		
				for (var i = 0; i < length; i++){
					var remainingToRemove = liveChart.datasets[i].points.length - maxPointsNumber;
					for (var j = 0; j < remainingToRemove ; j++){
					//	console.log('Removing data : '+ liveChart.datasets[i].points.length-maxPointsNumber);
						liveChart.removeData();	
					}
				}

	}

	

