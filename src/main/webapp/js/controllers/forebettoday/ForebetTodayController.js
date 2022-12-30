function ForebetTodayController($scope, $location,$route,ForebetService,UtilityService, $timeout,$state, $log,ngTableParams ,$rootScope,ngDialog,$window,Upload) {
 
	$scope.info ={
			delay : 0,
			reportId :''
	}
  $scope.linkresult = "";		
  $scope.buildData = function () {   
	  	$scope.info ={
				delay : 0,
				reportId :''
		}

		  $rootScope.showCustomLoading = true;
		  $("#importdataarea").css({"display":"none"});
		  ForebetService.getDataToday().$promise.then(function(data) {
	      	$rootScope.showCustomLoading = false;
	      	$("#importdataarea").css({"display":"block"});
				if (data != undefined && data != null){
		            if (data.resultCode != undefined && data.resultCode != null && data.resultCode != 'ok'){
	             	 	UtilityService.messageErrorDilog("have error or no data found");
	              }else {
	            	  $scope.info.reportId = data.resultMessage;
	                  UtilityService.notificationDilog("success!")
	              }
		          
	          }
	      }, function(error) {
	    	  $("#importdataarea").css({"display":"block"});
	     		$rootScope.showCustomLoading = false;
	          if (error.errorCode != undefined && error.errorCode != null)
	              $rootScope.error = error.errorMessage;
	      });
	 
    	

    };
    
    $scope.download = function(){
 	   $rootScope.error = '';
 	   var url = window.location.pathname + 'rest/crawlersui4j/downloadfile?reportId=' + $scope.info.reportId;
 	   $window.open(url,'_blank');
     };   
    $scope.resetForm = function(){
    	$state.go($state.current.name, {}, {reload: true});
    }
    
	
}

