/*******************************************************************************
 * Using chartjs
 * 
 ******************************************************************************/

var rawDataFromServlet = {};


/*Defines colors taken by lines*/
var colors = {"rgba(220,220,220,0.7)",
		"rgba(200,200,200,0.7)",
		"rgba(240,240,240,0.7)",
		"rgba(260,260,260,0.7)",
		"rgba(280,280,280,0.7)"};



/**
 * probe is a list of probes
 * Receiving data formatted as such :
 * 
 * probe;date;temperature
 * 
 * from servlet.
 * 
 * Uses ajax GET Request to retrieve data
 */
function getDataFromServlet(probe) {
	/* if probe is not null retrieves data for this probe */
	
	var address = 'http://192.168.0.20:8080/brewspberry-api/TemperatureServlet';
	
	if (probe != null){
		
		address+= '?';
		
		for (uuid in probe){
			
			address +='uuid='+uuid+'&';
			
		}
		
		addess.substring(0,address.length() - 1);
		
	}			
		/* if null => all probes */
	
	
	// If query is OK setting rawDataFromServlet
	jQuery.ajax (address,{
		
		success : function (result){
			
			this.rawDataFromServlet = result;
		},
		error : function () {
			
			
		}
	}
			
	);

}

function getInitDataFromServlet(probe) {

}

function buildGraph(loop, labels, dataSets) {

	
	var ctx = document.getElementById("myChart"+loop).getContext("2d");

	
	data = buildDataSetsForChartJS(labels, dataSets);
	
	var myLineChart = new Chart(ctx).Line(data, options);

	
}

/**
 * Receiving data as a list of {probe;date;temperature} convetrting them to
 * ChartJS datasets
 * 
 * @param data
 * @returns
 */
function buildDataSetsForChartJS (data){
	
	
	for (dataSet in data){
		
		
		
	}

		    labels: ["January", "February", "March", "April", "May", "June", "July"],
		    datasets: [
		        {
		            label: "My First dataset",
		            fillColor: "rgba(220,220,220,0.2)",
		            strokeColor: "rgba(220,220,220,1)",
		            pointColor: "rgba(220,220,220,1)",
		            pointStrokeColor: "#fff",
		            pointHighlightFill: "#fff",
		            pointHighlightStroke: "rgba(220,220,220,1)",
		            data: [65, 59, 80, 81, 56, 55, 40]
		        },
		        {
		            label: "My Second dataset",
		            fillColor: "rgba(151,187,205,0.2)",
		            strokeColor: "rgba(151,187,205,1)",
		            pointColor: "rgba(151,187,205,1)",
		            pointStrokeColor: "#fff",
		            pointHighlightFill: "#fff",
		            pointHighlightStroke: "rgba(151,187,205,1)",
		            data: [28, 48, 40, 19, 86, 27, 90]
		        }
		    ]
		};
	
	
} 