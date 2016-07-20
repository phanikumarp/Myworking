var app = angular.module('myApp', []);
		app.controller('myCtrl', function($scope, $http) {
			
			$scope.showHideRadio = false;
			$scope.selectedId = 'false';
		//	loadUrl();
			$scope.serviceVersionComparator = function(){
				$scope.showHideRadio = true;

				$scope.selectedId = 'true';
			//	loadUrl();
			}
			$scope.applicationVersionComparator = function(){
				$scope.selectedId = 'false';
				//loadUrl();
			}
			$http.get("json/servicenew.json").then(function(data) {
				//	$scope.Service1= false;
					$scope.arr = [];
					$scope.selectedFile = [];
					$scope.serviceArray = [];
					
					for(var i =0;i<data.data.Application_versions.length;i++){
						$scope.service = data.data.Application_versions[i];
						$scope.serviceArray.push($scope.service);
					}
			})
			
			
			
			$http.get("json/applicationnew.json").then(function(data) {
				$scope.arr1 = [];
				$scope.applicationArray = [];
				for(var i=0; i<data.data.Application_versions.length;i++){
					$scope.appService = data.data.Application_versions[i];
					$scope.applicationArray.push($scope.appService);
				}
				
			})
					
		/*related to services dropdown*/	
			$scope.serviceTable = false;
			$scope.selectedApplicationVersionId ='';
			$scope.selectedServiceApplicationVersion = function(serviceAppVersion){
				$scope.showService = "true";
				$scope.selectedApplicationVersionId = serviceAppVersion.AppVersionId;
			
				$scope.subServiceArray = [];
				for(var i =0;i<serviceAppVersion.services.length;i++){
					$scope.subServiceArray.push(serviceAppVersion.services[i]);
					
				}
			}
			$scope.showService = "false";
			
			/*selected service versions*/
			$scope.selectedServiceId = '';
			$scope.selectedServiceApplcationServiceName = function(appService){
				$scope.serviceTable = true;
				$scope.selectedServiceId=appService.serviceId;
				$scope.serviceVersions = [];
				for(var i =0;i<appService.service_Versions.length;i++){
					$scope.serviceVersions.push(appService.service_Versions[i]);
				}
			}

			
			$scope.showButton=false;
			$scope.namevalue = false;
			$scope.selection = [];
			$scope.selectionServiceVersionId=[];
			$scope.selectedServiceVersion = function(ele){
				//$scope.showButton = true;
				var idx = $scope.selection.indexOf(ele.serviceVersion);
				if (idx > -1) {
					$scope.selection.splice(idx, 1);
					$scope.selectionServiceVersionId.splice(idx, 1);
				}

				// is newly selected
				else {
					$scope.selection.push(ele.serviceVersion);
					$scope.selectionServiceVersionId.push(ele.versionId);
				}
				if ($scope.selection.length == 2) {
					
					$scope.showButton = true;
				}
				else if($scope.selection.length > 2 ||$scope.selection.length<2){
					$scope.showButton = false;
				}
			}

			$scope.applicationVersionId =[];
			$scope.selectedApplicationVersion = function(appelements){
			//	$scope.showButton = false;
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
				else if($scope.selection.length > 2 ||$scope.selection.length<2 ){
					$scope.showButton = false;
				}
			}
			console.log("this is for"+$scope.selection.length);
		
			$scope.showPlot = function() {
				
				console.log("In service seleted app version Id :::: "+$scope.selectedApplicationVersionId);
				console.log("In service seleted service:::: "+$scope.selectedServiceId);
				console.log("In service seleted service version Id :::: "+$scope.selectionServiceVersionId);
				console.log("application ver id :::"+$scope.applicationVersionId);

				
				$http.get("plot.json").then(
						function(dt) {
							d3.selectAll("svg").remove();
							console.log("when submit selection values are :::"
									+ JSON.stringify($scope.selection));
							console.log("data is ::" + JSON.stringify(dt));
							var data;
							if ($scope.selection.indexOf("SV-1.0.1") !== -1) {
								data = dt.data.data[0].Version1;
								d3Graph();
								console
										.log("ssssssssss"
												+ JSON.stringify(data));
								console.log("in if " + $scope.selection);
								$scope.message = 'artNr already exists!';
							}
							if ($scope.selection.indexOf("SV-1.0.2") !== -1) {
								data = dt.data.data[1].Version2;
								d3Graph();
								
								
								console.log("sssssssss444444444s"
										+ JSON.stringify(data));
								console.log("in if " + $scope.selection);
								$scope.message = 'artNr already exists!';
							}
						 	if ($scope.selection.indexOf("SV-1.0.3") !== -1) {
								data = dt.data.data[2].Version3;
								d3Graph();
								console.log("sssssssss444444444s"
										+ JSON.stringify(data));
								console.log("in if " + $scope.selection);
								$scope.message = 'artNr already exists!';
							}
							if ($scope.selection.indexOf("SV-1.0.12") !== -1) {
								data = dt.data.data[3].Version4;
								d3Graph();
								console.log("sssssssss444444444s"
										+ JSON.stringify(data));
								console.log("in if " + $scope.selection);
								$scope.message = 'artNr already exists!';
							} 
							
							/*Application version graph*/
							
							if ($scope.selection.indexOf("Version-1.0.1") !== -1) {
								data = dt.data.data[4].Version5;
								d3Graph();
								console.log("sssssssss444444444s"
										+ JSON.stringify(data));
								console.log("in if " + $scope.selection);
								$scope.message = 'artNr already exists!';
							} 
							if ($scope.selection.indexOf("Version-1.0.2") !== -1) {
								data = dt.data.data[5].Version6;
								d3Graph();
								console.log("sssssssss444444444s"
										+ JSON.stringify(data));
								console.log("in if " + $scope.selection);
								$scope.message = 'artNr already exists!';
							} 
							if ($scope.selection.indexOf("Version-1.0.3") !== -1) {
								data = dt.data.data[6].Version7;
								d3Graph();
								console.log("sssssssss444444444s"
										+ JSON.stringify(data));
								console.log("in if " + $scope.selection);
								$scope.message = 'artNr already exists!';
							} 

							// scatter plot graph	
							function d3Graph() {
								
								console.log("scatter plot inside functtion");
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
								
								
								
								// add the tooltip area to the webpage
								var tooltip = d3.select("body").append("div")
								    .attr("class", "tooltip")
								    .style("opacity", 0);

								// this sets the scale that we're using for the X axis. 
								// the domain define the min and max variables to show. In this case, it's the min and max prices of items.
								// this is made a compact piece of code due to d3.extent which gives back the max and min of the price variable within the dataset
								var x = d3.scale.linear().domain(
										d3.extent(data, function(d) {
											return d.x;
										}))
								// the range maps the domain to values from 0 to the width minus the left and right margins (used to space out the visualization)
								.range(
										[
												0,
												width - margins.left
														- margins.right ]);

								// this does the same as for the y axis but maps from the rating variable to the height to 0. 
								var y = d3.scale.linear().domain(
										d3.extent(data, function(d) {
											return d.y;
										}))
								// Note that height goes first due to the weird SVG coordinate system
								.range(
										[
												height - margins.top
														- margins.bottom, 0 ]);

								// we add the axes SVG component. At this point, this is just a placeholder. The actual axis will be added in a bit
								svg.append("g").attr("class", "x axis").attr(
										"transform",
										"translate(0," + y.range()[0] + ")");
								svg.append("g").attr("class", "y axis");

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

								// now we add some text, so we can see what each item is.
								/*  chocolateGroup.append("text")
								     .style("text-anchor", "middle")
								     .attr("dy", -10)
								     .text(function (d) {
								         // this shouldn't be a surprising statement.
								         return d.name;
								 }); */
								//}
								     //   d3.select("#scatter-load").remove();
							}
						});
			}
			//	$scope.selection = '';
		})