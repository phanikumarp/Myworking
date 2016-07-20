var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http) {
 $scope.getQuerystringNameValue =function(name){

          var winURL = window.location.href;
          var queryStringArray = winURL.split("?");  

          if(typeof queryStringArray[1]=='undefined') return
      
          var queryStringParamArray = queryStringArray[1].split("&");
          var nameValue = null;
          var queryStringNameValueArray;
          
          for ( var i=0; i<queryStringParamArray.length; i++ ){           
              queryStringNameValueArray = queryStringParamArray[i].split("=");

              if ( name == queryStringNameValueArray[0] ){
                  nameValue = queryStringNameValueArray[1];
              }                       
          }

          return nameValue;
        }
   
   $scope.appId = $scope.getQuerystringNameValue('appId');
   console.log("sreeeeeeeee" +$scope.appId);
	$scope.showComparator='application';
	$scope.selectedId = 'false';
	loadUrl();
	$scope.serviceVersionComparator = function(){
		$scope.selectedId = 'true';
		d3.selectAll("svg").remove();
		$scope.errorMessage = '';
		loadUrl();
	}
	$scope.applicationVersionComparator = function(){
		$scope.showApplication =true;
		$scope.selectedId = 'false';
		d3.selectAll("svg").remove();
		$scope.errorMessage = '';
		loadUrl();
	}


	function loadUrl(){
		console.log("======================="+$scope.selectedId);
		$http({
			method: 'GET',
			url: 'http://54.67.112.100:8161/n42-services/resources/svc/getVersionDetails',
			params: {appId :$scope.appId,serviceVersion:$scope.selectedId}
		}).then(function successCallback(response) {
			console.log("calling versions" +JSON.stringify(response));
			responseData(response);
		}, function errorCallback(response) {

		});
	}

	function responseData(data){

		// If $scope.selectedId is false then we are displaying apllicationVersionComparator data
		$scope.serviceArray=[];
		$scope.applicationArray=[];
		if($scope.selectedId == 'false') {
			for(var i=0; i<data.data.Application_versions.length;i++){
				$scope.appService = data.data.Application_versions[i];
				$scope.applicationArray.push($scope.appService);
				console.log("app data issssssssssssssss:::"+JSON.stringify($scope.appService));
			}
		}
		else {
			for(var i =0;i<data.data.Application_versions.length;i++){
				$scope.service = data.data.Application_versions[i];
				$scope.serviceArray.push($scope.service);
			}
		}
		/*related to services application versions*/	
		$scope.serviceTable = false;
		$scope.selectedApplicationVersionId ='';
		$scope.selectedServiceApplicationVersion = function(serviceAppVersion){
			$scope.showService = true;
			$scope.selectedApplicationVersionId = serviceAppVersion.applicationVersionId;
			console.log("jfdlkafla"+ JSON.stringify(serviceAppVersion));
			$scope.subServiceArray = [];
			for(var i =0;i<serviceAppVersion.services.length;i++){
				$scope.subServiceArray.push(serviceAppVersion.services[i]);

			}
		}
		$scope.showService = false;

		/*selected service versions*/

		$scope.selectedServiceId = '';
		$scope.selectedServiceName = '';
		$scope.selectedServiceApplcationServiceName = function(appService){
			$scope.serviceTable = true;
			$scope.selectedServiceId=appService.serviceId;
			$scope.selectedServiceName=appService.serviceName;
			console.log("selected service id is ::::::: "+$scope.selectedServiceId);
			$scope.serviceVersions = [];
			for(var i =0;i<appService.service_Versions.length;i++){
				$scope.serviceVersions.push(appService.service_Versions[i]);
			}
		}

		$scope.showHideRadio = false;
		$scope.showButton=false;
		$scope.namevalue = false;
		$scope.showTable = false;
		$scope.selection = [];
		$scope.selectionServiceVersionId=[];
		$scope.selectedServiceVersion = function(ele){
			$scope.showButton = true;
			var idx = $scope.selection.indexOf(ele.serviceVersion);
			console.log("idx is " + JSON.stringify(ele.serviceVersion));
			if (idx > -1) {
				$scope.selection.splice(idx, 1);
				$scope.selectionServiceVersionId.splice(idx, 1);
			}

			// is newly selected
			else {
				$scope.selection.push(ele.serviceVersion);
				$scope.selectionServiceVersionId.push(ele.versionId);
			}
			console.log("length is:::" + $scope.selection);
			if ($scope.selection.length == 2) {

				$scope.showButton = true;
			}
			else if($scope.selection.length > 2 ||$scope.selection.length<2){
				$scope.showButton = false;
			}
		}

		$scope.applicationVersionId =[];
		$scope.selectedApplicationVersion = function(appelements){
			$scope.showButton = true;
			console.log("application idxyyyyyyyyyyy is " + JSON.stringify(appelements));
			var idx = $scope.selection.indexOf(appelements.appVersion);
			if (idx > -1) {
				$scope.selection.splice(idx, 1);
				$scope.applicationVersionId.splice(idx,1);
			}

			// is newly selected
			else {
				$scope.selection.push(appelements.appVersion);
				$scope.applicationVersionId.push(appelements.applicationVersionId);
			}
			if ($scope.selection.length == 2) {
				$scope.showButton = true;
			}
			else if($scope.selection.length > 2 ||$scope.selection.length<2){
				$scope.showButton = false;
			}
		}
	}
	
$scope.comparedStatsData = '';
	$scope.showPlot = function() {
		d3.selectAll("svg").remove();
		if($scope.selectedId == 'false'){
			console.log("app length is:::" + $scope.applicationVersionId);
			$http({
				method: 'GET',
				url: 'http://54.67.112.100:8161/n42-services/resources/svc/svcChecker',
				params: {appId:$scope.appId,versions:$scope.applicationVersionId,serviceVersion:$scope.selectedId}
			}).then(function successCallback(response) {
				console.log("graph data for application version" +JSON.stringify(response));
				if(!response.data.error) {
				$scope.comparedStatsData = response.data.comparedStats;
				for(var i = 0; i<$scope.applicationVersionId.length; i++){
console.log("test1" +$scope.applicationVersionId[i]+ 'versionfromjson:  ' +response.data.data[i].versionId +'  ivalue: '+$scope.applicationVersionId.length);
					if($scope.applicationVersionId[i] == response.data.data[i].versionId){
				//		plotData(response);
						var data = response.data.data[i];
						d3Graph(data);

					}
				}
			}
			else {
				$scope.errorMessage = response.data.error;
			}
			}, function errorCallback(response) {
				//console.log('error' +response);
				$scope.errorMessage = "No Data Available for Selected Versions, Please select different ";
			});
		}
		else{
console.log('appVersionId:' +$scope.selectedApplicationVersionId);
			$http({
				method: 'GET',
				url: 'http://54.67.112.100:8161/n42-services/resources/svc/svcChecker',
				params: {appId:$scope.appId,appVersionId:$scope.selectedApplicationVersionId,versions:$scope.selectionServiceVersionId,serviceVersion:$scope.selectedId,serviceName:$scope.selectedServiceName}
			}).then(function successCallback(response) {
if(!response.data.error) {
				//console.log("graph data for Service version" +JSON.stringify(response));
	$scope.comparedStatsData = response.data.comparedStats;
				for(var i = 0; i<$scope.selectionServiceVersionId.length; i++){
					if($scope.selectionServiceVersionId[i] == response.data.data[i].versionId){
				//		plotData(response);
						var data = response.data.data[i];
						d3Graph(data);
					}
				}
			}
			else {
				$scope.errorMessage = response.data.error;
			}

			}, function errorCallback(response) {
				//console.log('error' +response);
				$scope.errorMessage = "No Data Available for Selected Versions, Please select different ";
			});
		}

	}

	// scatter plot graph	
	function d3Graph(dt) {
		$scope.showTable = true;
		console.log("scatter plot inside functtion");
		//console.log("inside functtion"+JSON.stringify(dt));
		// just to have some space around items. 
		var data = dt.graphData;
		//console.log("inside graphData functtion"+JSON.stringify(data));
		
		var bucketData = dt.bucketsData;

		//console.log("inside bucketdata functtion"+JSON.stringify(bucketData));

		// just to have some space around items. 
		var margins = {
				"left" : 40,
				"right" : 30,
				"top" : 30,
				"bottom" : 40
		};

		var width = 330;
		var height = 270;

		// this will be our colour scale. An Ordinal scale.
		var colors = d3.scale.category10();
		// we add the SVG component to the scatter-load div
		var svg = d3.select("#scatter-load").append(
		"svg").attr("width", width).attr(
				"height", height).append("g").attr(
						"transform",
						"translate(" + margins.left + ","
						+ margins.top + ")");

		// this sets the scale that we're using for the X axis. 
		// the domain define the min and max variables to show. In this case, it's the min and max prices of items.
		// this is made a compact piece of code due to d3.extent which gives back the max and min of the price variable within the dataset
		var x = d3.scale.linear().domain(
				d3.extent(data, function(d) {
					return d.x;
				}))
				// the range maps the domain to values from 0 to the width minus the left and right margins (used to space out the visualization)
				.range([0,width - margins.left - margins.right ]);

		// this does the same as for the y axis but maps from the rating variable to the height to 0. 
		var y = d3.scale.linear().domain(
				d3.extent(data, function(d) {
					return d.y;
				}))
				// Note that height goes first due to the weird SVG coordinate system
				.range([height - margins.top - margins.bottom, 0 ]);

		// we add the axes SVG component. At this point, this is just a placeholder. The actual axis will be added in a bit
		svg.append("g").attr("class", "x axis").attr(
				"transform",
				"translate(0," + y.range()[0] + ")");
		svg.append("g").attr("class", "y axis");


		for(var i = 0; i < bucketData.length; i++) {
			svg.append('line')
			.attr('x1',x(bucketData[i].a1))
			.attr('x2',x(bucketData[i].a2))
			.attr('y1',y(bucketData[i].b1))
			.attr('y2',y(bucketData[i].b2))
			.style("stroke-width", 2)
			.style("stroke-dasharray", 2)
			.style("stroke", "red")
			.style("fill", "none");
		}

		// this is our X axis label. Nothing too special to see here.
		svg.append("text").attr("fill", "#000")
		.attr("text-anchor", "end").attr("x",
				width / 2).attr("y",
						height - 35)
						.text("Values of X");

		// this is our X axis label. Nothing too special to see here.
		svg.append("text").attr("fill", "#000")
		.attr("text-anchor", "end").attr("y",
				width / 2).attr("y",
						height - 250)
						.attr('transform', 'rotate(-90)')
						.text("Values of y");

		// this is the actual definition of our x and y axes. The orientation refers to where the labels appear - for the x axis, below or above the line, and for the y axis, left or right of the line. Tick padding refers to how much space between the tick and the label. There are other parameters too - see https://github.com/mbostock/d3/wiki/SVG-Axes for more information
		var xAxis = d3.svg.axis().scale(x).orient(
		"bottom").tickPadding(2);
		var yAxis = d3.svg.axis().scale(y).orient(
		"left").tickPadding(2);

		// this is where we select the axis we created a few lines earlier. See how we select the axis item. in our svg we appended a g element with a x/y and axis class. To pull that back up, we do this svg select, then 'call' the appropriate axis object for rendering.    
		svg.selectAll("g.y.axis").call(yAxis);
		svg.selectAll("g.x.axis").call(xAxis);

		// now, we can get down to the data part, and drawing stuff. We are telling D3 that all nodes (g elements with class node) will have data attached to them. The 'key' we use (to let D3 know the uniqueness of items) will be the name. Not usually a great key, but fine for this example.
		var chocolate = svg.selectAll("g.node").data(
				data, function(d) {
					return d.x;
				});

		// we 'enter' the data, making the SVG group (to contain a circle and text) with a class node. This corresponds with what we told the data it should be above.

		var chocolateGroup = chocolate.enter().append(
		"g").attr("class", "node")
		// this is how we set the position of the items. Translate is an incredibly useful function for rotating and positioning items 
		.attr(
				'transform',
				function(d) {
					return "translate(" + x(d.x) + ","
					+ y(d.y) + ")";
				});

		// we add our first graphics element! A circle! 
		chocolateGroup.append("circle").attr("r", 1.2)
		.attr("class", "dot")
		.style("fill", function(d) {        
			return "#4582B4";
			;}) 
	}


	//	$scope.selection = '';
})
