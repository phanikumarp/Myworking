var app = angular.module('svcApp', ['ui.router']);
app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

	$urlRouterProvider.otherwise('/');
	$stateProvider
	 .state('home', {
 	    url:'/',
			templateUrl : '/n42-canary-analysis/public/svcHome.html',
			controller  : 'SvcHomeCtrl'
		}).state('trendAnalysis', {
    	    url:'/trendAnalysis/:serviceName',
			templateUrl : '/n42-canary-analysis/public/trendAnalysis.html',
			controller  : 'TrendAnalysisCtrl'
		});
}]);
app.controller('SvcHomeCtrl', function($scope, $http, $location, $stateParams,$rootScope) {
	
	$scope.selectedMetric = function(metricName){
		$scope.metricName = metrics.metricName;
		console.log("name of"+$scope.metricName);
		
	}
	
	
	$scope.trendAnalysis = function(serviceName){
		$rootScope.serviceName = serviceName;
		console.log("loaidfjdsfldij");
		$location.path('/trendAnalysis/'+serviceName);
		
	}
//	$http({
//		method:"GET",
//		url:"http://52.8.104.253:8161/n42-cas-services/resources/svc/compareSVC?serviceName="+MySQL+"&version1="+v1.0+"&version2="+v1.1,
//		headers:{
//			  'Accept':'application/json',
//			  'Content-Type':'application/json'
//				  
//		  },
//	}).success(function(json){
//		console.log("reponse :::::::::::::::::::::::::::::::::::::   " +JSON.stringify(json));
//		$scope.serviceData = json;
//		$scope.serviceScore = Math.round(json.comparison_output.comparisionScore*100)/100;
//		$scope.serviceComparisonData = json.comparison_output;
//		console.log("comparison data is:::"+JSON.stringify($scope.serviceComparisonData));
//		$scope.groupData = json.comparison_output.results;
//		console.log("color of the group is::::"+JSON.stringify($scope.groupData));
//});
	$scope.getServiceMetric = function(selectedMetricData){
		$scope.selectedMetricName = selectedMetricData.metricName;
		console.log("MEtric data is :::::::::::::::::::::::: " +JSON.stringify(selectedMetricData));
		for(var keyName in $scope.serviceData.comparison_output.results.Metrics){
			$scope.serviceMetricNames.push(keyName);
			console.log("data of the metria name is:::::"+JSON.stringify($scope.serviceData.comparison_output.results.Metrics));
		}
		$scope.showVersions = true;
		
		$scope.metricVersionsData = selectedMetricData.metricstats.statinfo;
		$scope.name = selectedMetricData;
		console.log("selected name of the metric is:::"+JSON.stringify($scope.selectedMetricName));
//		$scope.selectedMetricName = selectedMetricData.Metrics;
		
		
		
		FusionCharts.ready(function () {
			
			
			var version1_bucket1_name = selectedMetricData["version1.model.char"].bucket1.bucketname;
			var version1_bucket1_loadRange = selectedMetricData["version1.model.char"].bucket1["bucket.load.range"];
			var version1_bucket1_name = selectedMetricData["version1.model.char"].bucket2.bucketname;
			var version1_bucket1_data = selectedMetricData["version1.model.char"].bucket1["bucket.raw.data"];
			var version1_bucket2_data = selectedMetricData["version1.model.char"].bucket2["bucket.raw.data"];
			
			console.log("load range of the bucket is:::"+JSON.stringify(version1_bucket1_loadRange));
			
			
			var version2_bucket1_name = selectedMetricData["version2.model.char"].bucket1.bucketname;
			var version2_bucket2_name = selectedMetricData["version2.model.char"].bucket2.bucketname;
			var version2_bucket1_data =selectedMetricData["version2.model.char"].bucket1["bucket.raw.data"];
			var version2_bucket2_data = selectedMetricData["version2.model.char"].bucket2["bucket.raw.data"];
			
			console.log("version 1 bucket 1 data : " +JSON.stringify(version1_bucket1_data));
			console.log("version 1 bucket 2 data : " +JSON.stringify(version1_bucket2_data));
			console.log("version 2 bucket 1 data : " +JSON.stringify(version2_bucket1_data));
			console.log("version 2 bucket 2 data : " +JSON.stringify(version2_bucket2_data));
			
				
				var salaryDistribution = new FusionCharts({
		        type: 'boxandwhisker2d',
		        renderAt: 'chart-container',
		        width: '370',
		        height: '400',
		        dataFormat: 'json',
		        dataSource:
		        {
		            "chart": {
		                "caption": $scope.selectedMetricName,
		                "subcaption": "Version Comparison",                
		                "xAxisName": "Buckets",
		                "YAxisName": $scope.selectedMetricName,                
		                "numberPrefix": "",
		                "theme": "carbon",
		                "showValues": "1",
		                /*//To show or hide the lower quartile value
		                "showQ1Values": "1",	
		                //To show or hide the upper quartile value
		                "showQ3Values": "1",*/
		                "usePlotGradientColor": "",
		                "showMean": "1",
		                "showMedian": "1"
		            },
		            "categories": [
		                {
		                    "category": [
		                        {
		                            "labe": "version1"
		                        }, 
		                        {
		                            "labe": "version2",
		                        }
		                    ]
		                }
		            ],
		            "dataset": [
		                {
		                    "seriesname": "Version1",
		                    "lowerboxcolor": "#89E894",
		                    "upperboxcolor": "#BED661",
		                    "data": [
		                        {
		                            "value":version1_bucket1_data.toString()
		                        }, 
		                        {
		                            "value":version1_bucket2_data.toString()
		                        },
		                    ]
		                }, {
		                    "seriesname": "Version2",
		                    "lowerboxcolor": "#e44a00",
		                    "upperboxcolor": "#f8bd19",
		                    "data": [
		                        {
		                            
		                             "value":version2_bucket1_data.toString()
		                        }, 
		                        {
		                            "value":version2_bucket2_data.toString()
		                        }
		                    ]
		                },
		                
		            ]
		        }                
		    }).render();
			});
		
	}
	
		
	
			$scope.colourIncludes = [];
		    $scope.includeColour = function(groupColour) {
		    	$scope.version = false;
		        var i = $.inArray(groupColour, $scope.colourIncludes);
		        if (i > -1) {
		            $scope.colourIncludes.splice(i, 1);
		        } else {
		            $scope.colourIncludes.push(groupColour);
		        }
		    }
		    
		    $scope.colourFilter = function(serviceData) {
		        if ($scope.colourIncludes.length > 0) {
		            if ($.inArray(serviceData.groupColour, $scope.colourIncludes) < 0)
		                return;
		        }
		        
		        return serviceData;
		    }
		    
				$scope.serviceArray = [];
				
				$http({
					method:"GET",
					url:"http://52.8.104.253:8161/n42-cas-services/resources/svc/getSVCServices",
					headers:{
						  'Accept':'application/json',
						  'Content-Type':'application/json'
							  
					  },
				}).success(function(response){
					console.log("response from lalit code:::"+JSON.stringify(response));
					$scope.tomcatServiceData = response.services[0].serviceVersions;
					$scope.services =response.services;
				});
				
				
				
//				$scope.serviceName =
			/*})*/
			$scope.showServiceVersions = false;
			
			$scope.versionCompharision = function(compharisionsData){
				console.log(compharisionsData);
				$scope.selectedServiceName =compharisionsData.versionss.serviceName;
				$rootScope.serviceName = compharisionsData.versionss.serviceName;
				$scope.selectedVersion1 = compharisionsData.version1.versionName;
				$scope.selectedVersion2 = compharisionsData.version2.versionName;
				console.log("compare");
				$http({
					method:"GET",
					url:"http://52.8.104.253:8161/n42-cas-services/resources/svc/compareSVC?serviceName="+$scope.selectedServiceName+"&version1="+$scope.selectedVersion1+"&version2="+$scope.selectedVersion2,
					headers:{
						  'Accept':'application/json',
						  'Content-Type':'application/json'
							  
					  },
				}).success(function(json){
					console.log("reponse :::::::::::::::::::::::::::::::::::::   " +JSON.stringify(json));
					$scope.serviceData = json;
					$scope.serviceScore = Math.round(json.comparison_output.comparisionScore*100)/100;
					$scope.serviceComparisonData = json.comparison_output;
					console.log("comparison data is:::"+JSON.stringify($scope.serviceComparisonData));
					$scope.groupData = json.comparison_output.results;
					console.log("color of the group is::::"+JSON.stringify($scope.groupData));
	});
}
			
		/*	$scope.getData = function(){
			  	var req = {
				        method: 'GET',
				        url: 'http://52.8.104.253:8161/n42-cas-services/resources/svcadvanced/scoretrend/Tomcat/'+ $scope.version,
				        headers: {'Content-Type': 'application/json'},
			        };

			    $http(req).then(function(res){
			    	console.log("data is::::"+JSON.strigify(res));
			        console.log(res.data);
			        $scope.data = res.data;
			        $scope.callTrendlinegraph($scope.data);
			    }, function(err){
			        console.log(err);
			        $scope.data = err;
			    });
			} */
			
			

		    $scope.tableRowExpanded = false;
		    $scope.tableRowIndexCurrExpanded = "";
		    $scope.tableRowIndexPrevExpanded = "";
		    $scope.storeIdExpanded = "";
		    $scope.dayDataCollapse = [true, true, true, true, true, true];

		    $scope.dayDataCollapseFn = function () {
		        for (var i = 0; storeDataModel.storedata.length - 1; i += 1) {
		            $scope.dayDataCollapse.append('true');
		        }
		    };



		    $scope.selectTableRow = function (index) {
		        if ($scope.dayDataCollapse === 'undefined') {
		            $scope.dayDataCollapse = $scope.dayDataCollapseFn();
		        } else {

		            if ($scope.tableRowExpanded === false && $scope.tableRowIndexCurrExpanded === "" && $scope.storeIdExpanded === "") {
		                $scope.tableRowIndexPrevExpanded = "";
		                $scope.tableRowExpanded = true;
		                $scope.tableRowIndexCurrExpanded = index;
		                //$scope.storeIdExpanded = storeId;
		                $scope.dayDataCollapse[index] = false;
		            } else if ($scope.tableRowExpanded === true) {
		                if ($scope.tableRowIndexCurrExpanded === index) {
		                    $scope.tableRowExpanded = false;
		                    $scope.tableRowIndexCurrExpanded = "";
		                    $scope.storeIdExpanded = "";
		                    $scope.dayDataCollapse[index] = true;
		                } else {
		                    $scope.tableRowIndexPrevExpanded = $scope.tableRowIndexCurrExpanded;
		                    $scope.tableRowIndexCurrExpanded = index;
		                   // $scope.storeIdExpanded = storeId;
		                    $scope.dayDataCollapse[$scope.tableRowIndexPrevExpanded] = true;
		                    $scope.dayDataCollapse[$scope.tableRowIndexCurrExpanded] = false;
		                }
		            }
		        }
		    };
			
			
			
			
			
})
app.controller('TrendAnalysisCtrl', function($scope, $http, $location, $stateParams,$rootScope) {
	console.log('the page is here '+$rootScope.serviceName);
	
})
app.controller('svcCtrl', function($scope, $http, $location, $stateParams ) {
	console.log('the page is here ');
})
app.controller('trendCtrl', function($scope, $http, $location, $stateParams,$rootScope,$stateParams) {
	$scope.serviceName = $stateParams.serviceName;
  	
  	$scope.getData = function(){
	  	var req = {
		        method: 'GET',
		        url: 'http://52.8.104.253:8161/n42-cas-services/resources/svcadvanced/scoretrend/'+$scope.serviceName+'/'+ $scope.version,
		        headers: {'Content-Type': 'application/json'},
	        };

	    $http(req).then(function(res){
	    	console.log("data for score trend is:::"+JSON.stringify(res));
	        console.log(res.data);
	        $scope.data = res.data;
	       callTrendlinegraph($scope.data);
	    }, function(err){
	        console.log(err);
	        $scope.data = err;
	    });
	}             
  function callTrendlinegraph(data) {
	  console.log("data");
	  console.log(data);
	  
      $('#visualisation').show();
       $('#visualisation').empty();
      
      var data= data;
      
       var vis = d3.select("#visualisation"),
           WIDTH = 400,
           HEIGHT = 250,
           MARGINS = {
               top: 20,
               right: 20,
               bottom: 20,
               left: 50
           },
           xScale =  d3.scale.ordinal()
           		.domain(data.map(function(d) { return d.version; }))
           		.rangeRoundBands([0, WIDTH], 1);

           yScale = d3.scale.linear().range([HEIGHT - MARGINS.top, MARGINS.bottom]).domain([0, 100]),

           xAxis = d3.svg.axis()
           .orient('bottom')
		    .tickPadding(8)
		     .scale(xScale),

           yAxis = d3.svg.axis()
           .scale(yScale)
           .orient("left");

        vis.selectAll("dot")
           .data(data)
           .enter().append("circle").attr("fill", "#C80303")
           .attr("r", 5)
           .attr("cx", function(d) {
               return xScale(d.version);
           })
           .attr("cy", function(d) {
               return yScale(d.score);
           })
           .on("mouseover", function(d) {
               div.transition()
                   .duration(200)
                   .style("opacity", 0.9);
               div.html('Version:' + d.version + "<br/>" + 'Score:' + d.score)
                   .style("left", (d3.event.pageX) + "px")
                   .style("top", (d3.event.pageY - 28) + "px");
           })
           .on("mouseout", function(d) {
               div.transition()
                   .duration(1000)
                   .style("opacity", 0);
           });

       var div = d3.select("body").append("div")
           .attr("class", "tooltip")
           .style("opacity", 0);

       vis.append("svg:text")
           .attr("class", "x label")
           .attr("text-anchor", "end")
           .attr("x", WIDTH - 20)
           .attr("y", HEIGHT + 10)
           .text("Version");

       vis.append("svd:text")
           .attr("class", "y label")
           .attr("text-anchor", "middle")
           .attr("y", 10)
           .attr("x", HEIGHT - 340)
           .attr("dy", "1em")
           .attr("transform", "rotate(-90)")
           .text("Score");

       vis.append("svg:g")
           .attr("class", "x axis")
           .attr("transform", "translate(0," + (HEIGHT - MARGINS.bottom) + ")")
           .call(xAxis);
       vis.append("svg:g")
           .attr("class", "y axis")
           .attr("transform", "translate(" + (MARGINS.left) + ",0)")
           .call(yAxis);
       var lineGen = d3.svg.line()
           .x(function(d) {
               return xScale(d.version);
           })
           .y(function(d) {
               return yScale(d.score);
           });
       //.interpolate("basis");
       vis.append('svg:path')
           .attr('d', lineGen(data))
           .attr('stroke', '#C80303')
           .attr('stroke-width', 1)
           .style("opacity", 1)
           .attr('fill', 'none');

   }
  
  /*Metric type advanced trends*/
  
	$http({
		method:'GET',
		url:'http://52.8.104.253:8161/n42-cas-services/resources/svcadvanced/metrictypes/'+$scope.serviceName,
		headers:{
			  'Accept':'application/json',
			  'Content-Type':'application/json'
				  
		  },
	}).success(function(response){
		$scope.serviceAdvancedMetricData = response;
		console.log("response from versioon is::::::"+JSON.stringify($scope.serviceAdvancedMetricData));
	});
	
	 /*Metric names in advanced trends*/
	$scope.selectedMetricType=function(metricType){
		console.log(metricType);
		$http({
			method:'GET',
			url:'http://52.8.104.253:8161/n42-cas-services/resources/svcadvanced/metricnames/'+$scope.serviceName+'/'+metricType,
			headers:{
				'Accept':'application/json',
				'Content-Type':'application/json'
					
			},
		}).success(function(response){
			$scope.serviceAdvancedMetricNames = response;
			console.log("metric type of the names is+++:::::"+JSON.stringify($scope.serviceAdvancedMetricNames));
		});
	}
	

	$scope.getbargraphData = function(){
	  	var req = {
		        method: 'GET',
		        url: 'http://52.8.104.253:8161//n42-cas-services/resources/svcadvanced/metrictrend/'+$scope.serviceName+'/'+$scope.metricName+'/'+ $scope.metricTrendVersion,
		        headers: {'Content-Type': 'application/json'},
	        };

	    $http(req).then(function(res){
	    	$scope.data = res.data;
	        $scope.callTrendbargraph($scope.data);
	    });
	}        
	
	$scope.selectedMetricName = function(metricName){
		console.log("metric name Isss:::::"+ metricName);
	
	$scope.callTrendbargraph= function(data){
//		 d3.select("svg").remove();
		console.log("data is ::::::::::::::::::::::::: " +JSON.stringify(data));
		console.error("Call trend graph");
		/*var data = [
		            {
		                "State": "2.1",
		                "5th Percentile": 23,
		                "Mean": 45,
		                "Median": 43,
		                "95th Percentile": 10,
		                "Max": 65
		              },
		              {
		                "State": "2.2",
		                "5th Percentile": 25,
		                "Mean": 44,
		                "Median": 47,
		                "95th Percentile": 15,
		                "Max": 72
		              },
		              {
		                "State": "2.3",
		                "5th Percentile": 15,
		                "Mean": 35,
		                "Median": 38,
		                "95th Percentile": 18,
		                "Max": 50
		              },
		              {
		                "State": "2.4",
		                "5th Percentile": 16,
		                "Mean": 39,
		                "Median": 40,
		                "95th Percentile": 8,
		                "Max": 78
		              },
		              {
		                "State": "2.5",
		                "5th Percentile": 18,
		                "Mean": 48,
		                "Median": 51,
		                "95th Percentile": 27,
		                "Max": 60
		              }
		            ];*/

		           var margin = {top: 20, right: 90, bottom: 30, left: 40},
		               width = 510 - margin.left - margin.right,
		               height = 300 - margin.top - margin.bottom;

		           var x0 = d3.scale.ordinal()
		               .rangeRoundBands([0, width], .1);

		           var x1 = d3.scale.ordinal();

		           var y = d3.scale.linear()
		               .range([height, 0]);

		           var color = d3.scale.ordinal()
		               .range(["#d0743c", "#ff8c00", "#7b6888", "#6b486b", "#a05d56", "#d0743c"]);

		           var xAxis = d3.svg.axis()
		               .scale(x0)
		               .orient("bottom");

		           var yAxis = d3.svg.axis()
		               .scale(y)
		               .orient("left")
		               .tickFormat(d3.format(".2s"));
		          
		           var svg = d3.select("#trendgraph").append("svg")
		               .attr("width", width + margin.left + margin.right)
		               .attr("height", height + margin.top + margin.bottom)
		             .append("g")
		               .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
		         var ageNames = d3.keys(data[0]).filter(function(key) { return key !== "State"; });

		         data.forEach(function(d) {
		           d.ages = ageNames.map(function(name) { return {name: name, value: +d[name]}; });
		         });

		         x0.domain(data.map(function(d) { return d.State; }));
		         x1.domain(ageNames).rangeRoundBands([0, x0.rangeBand()]);
		         y.domain([0, d3.max(data, function(d) { return d3.max(d.ages, function(d) { return d.value; }); })]);

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
		             .text(metricName);

		         var state = svg.selectAll(".state")
		             .data(data)
		           .enter().append("g")
		             .attr("class", "state")
		             .attr("transform", function(d) { return "translate(" + x0(d.State) + ",0)"; });

		         state.selectAll("rect")
		             .data(function(d) { return d.ages; })
		           .enter().append("rect")
		             .attr("width", x1.rangeBand())
		             .attr("x", function(d) { return x1(d.name); })
		             .attr("y", function(d) { return y(d.value); })
		             .attr("height", function(d) { return height - y(d.value); })
		             .style("fill", function(d) { return color(d.name); });

		         var legend = svg.selectAll(".legend")
		             .data(ageNames.slice().reverse())
		           .enter().append("g")
		             .attr("class", "legend")
		             .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });

		         legend.append("rect")
		             .attr("x", width - 8)
		             .attr("width",8)
		             .attr("height", 8)
		             .style("fill", color);

		         legend.append("text")
		             .attr("x", width + 10)
		             .attr("y", 5)
		             .attr("dy", ".40em")
		             .style("text-anchor", "start")
		             .text(function(d) { return d; });

	}
	}
	
})