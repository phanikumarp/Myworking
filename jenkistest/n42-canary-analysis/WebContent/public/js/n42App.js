var app = angular.module('myApp', ['ui.router']);
app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

	$urlRouterProvider.otherwise('/');
	$stateProvider
	.state('home', {
				url : '/',
				templateUrl : '/public/canaryAnalysis.html',
				controller : 'myCtrl'
			})
}]);
app.controller('myCtrl', function($scope, $http, $location, $stateParams ) {
	
	 $scope.getQuerystringNameValue =function(name){

         var winURL = window.location.href;
         var queryStringArray = winURL.split("?");  
         if(typeof queryStringArray[1]=='undefined') return
     
         var nameValue = null;
         var queryStringNameValueArray;
         var queryStringParamArray = queryStringArray[1].split("=");
             if ( name == queryStringParamArray[0] ){
                 nameValue = queryStringParamArray[1];
             }                       
         return nameValue;
       }
	 /*$('.collaptable').aCollapTable({ 

			// the table is collapased at start
			startCollapsed: true,

			// the plus/minus button will be added like a column
			addColumn: true, 

			// The expand button ("plus" +)
			plusButton: '<span class="i">+</span>', 

			// The collapse button ("minus" -)
			minusButton: '<span class="i">-</span>' 
			  
			});*/
  $scope.canaryId = $scope.getQuerystringNameValue('canaryId');
  console.log("url data is :::::::::::::::::::: http://172.9.239.142:9090/n42-cas-services/resources/cas/getCanaryOutput?canaryId="+$scope.canaryId);
  $http({
	  method:"GET",
	  url:"http://172.9.239.142:8090/cas/getCanaryOutput?canaryId="+$scope.canaryId,
	  headers:{
		  'Accept':'application/json',
		  'Content-Type':'application/json'
			  
	  },
  }).success(function(response){
	  $scope.versionNames=[];
			$scope.metricData = '';
			console.log(window.location.href);
				$scope.version = false;
				$scope.arr = [];
				$scope.canaryData = response;
				$scope.count = 0;
				
			})
				$scope.getMetric = function(data){
	  console.log("metric data is::"+JSON.stringify(data));
	  			$scope.versionNames=[];
	  			$scope.versions=data.stats;
				for(versionName in $scope.versions){
					$scope.versionNames.push(versionName);
				}
				$scope.version = true;
				$scope.metricData = data;
			}
			
			$scope.getValues = function(data,value){
				angular.forEach(data, function(key,value){
				})
			}
			$scope.colourIncludes = [];
		    $scope.includeColour = function(groupColor) {
		    	$scope.version = false;
		        var i = $.inArray(groupColor, $scope.colourIncludes);
		        if (i > -1) {
		            $scope.colourIncludes.splice(i, 1);
		        } else {
		            $scope.colourIncludes.push(groupColor);
		        }
		    }
		    
		    $scope.colourFilter = function(canaryData) {
		        if ($scope.colourIncludes.length > 0) {
		            if ($.inArray(canaryData.groupColor, $scope.colourIncludes) < 0)
		                return;
		        }
		        
		        return canaryData;
		    }
		    
			$http.get("json/serviceVersion.json").then(function(data) {
				$scope.arr = [];
				$scope.serviceArray = [];
				$scope.versionsData = data.data.serviceVersions;
			})
			$http.get("json/bar.json").then(function(data) {
				$scope.serviceData = data.data;
				$scope.serviceScore = Math.round($scope.serviceData.comparison_output.comparisionScore*100)/100;
				$scope.serviceMetricNames =[];
				for(var keyName in $scope.serviceData.comparison_output.results){
					$scope.serviceMetricNames.push(keyName);
					console.log("key name is :::::::::::::::::::::::; "+keyName);
				}
				
			})
			$scope.getServiceMetric = function(metricName){
				console.log("MEtric data is :::::::::::::::::::::::: " +JSON.stringify(metricName.metricstats.statinfo));
				
				$scope.metricVersionsData = metricName.metricstats.statinfo;
//				console.log("metric name is ::::::::::::::::::: " +$scope.serviceData.comparison_output.results.metricName.metricstats.statinfo);
//				$scope.metricVersionsData = $scope.serviceData.comparison_output.results.metricName.metricstats.statinfo;
			}
			
			
})