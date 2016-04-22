/*******************************************************************************
 * @author : Xavier CARON
 * @version : 0.1
 * 
 * This script retrieves last temperature from service and displays it 
 * 
 ******************************************************************************/


var serviceAddress = 'http://192.168.0.20:8080/brewspberry-api/rest/getLastTemperatureValue';

var refreshDelay = 5000; // Refreshes every 5 s



/**
 * divTofillIDsList contains IDs of fields to fill in page. Values are UUIDs o each actionner 
 * 
 * @param step
 * @param uuid
 * @param divToFillIDsList
 * @returns
 */
function execute (step, uuid, divToFillIDsList){
	
	
	if (typeof step != 'undefined' && typeof uuid != 'undefined' ){
		
		var parameters = {
				"step" : step,
				"uuid" : uuid
		};
		
		
		getDataFromService(parameters, function (rawData){
			
			
			for (element in rawData){
				
				if (checkIfUUIDExistsInPage(rawData.uuid, divToFillIDsList)
				
			}
			$('#'+divToFillID).text()
			
		});
		
		
	}
	
}


function getDataFromService (params, callback){
	
	
	for (key in params){
		
		serviceAddress += '/' + key;
		
		serviceAddress += '/' + params[key];
		
	}
	
	jQuery.ajax (serviceAddress,{
		
		success : function (result){
			console.log('Call success');
	
			rawDataFromService = result;
		

			callback(rawDataFromService);
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
	
	
	
	
function checkIfUUIDExistsInPage (uuidFromService, uuidListFromPage){
	
	
	
}
}