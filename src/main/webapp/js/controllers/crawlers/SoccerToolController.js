function SoccerToolController($scope, $location,$route,CrawlersService,UtilityService, $timeout,$state, $log,ngTableParams ,$rootScope,ngDialog,$window,Upload) {
 
	$scope.info ={
			delay : 0,
			reportId :''
	}
	$scope.selectedFiles = null;
	$scope.onFileSelect = function ($files) {
			if($files==null)
				return;
			$scope.selectedFiles = $files;
	};
  $scope.linkresult = "";		
  $scope.buildData = function () {   
	  $scope.info ={
				delay : 0,
				reportId :''
		}
	 if(!$scope.selectedFiles || $scope.selectedFiles.length < 1)
		  return
	var fileToUpload = $scope.selectedFiles;	
	 $rootScope.showCustomLoading = true;
	 $("#importdataarea").css({"display":"none"});
    	$scope.upload = Upload.upload({
            url: 'rest/crawlerscdp4j/soccerTool', 
            method: 'POST',
            fields: {
            	delay : $scope.info.delay
            },
            file : fileToUpload
        });
    	
        $scope.upload.then(function (result) {
        	 $rootScope.showCustomLoading = false;
        	$("#importdataarea").css({"display":"block"});
            if (result != undefined && result != null) 
            	var data = result.data;
                if (data.errorMessage != undefined && data.errorMessage != null)
                    $rootScope.error = data.errorMessage;
                else {
                    $rootScope.error = '';
                    $scope.info.reportId = data.resultMessage;
                    UtilityService.notificationDilog("success!")
//                    $scope.resetForm();
            }
        }, function (error) {
        	$rootScope.showCustomLoading = false;
        	$("#importdataarea").css({"display":"block"});
            if (error.errorCode != undefined && error.errorCode != null)
                $rootScope.error = error.errorMessage;
        }, function (evt) {
        });
        $scope.upload.xhr(function (xhr) {
//					xhr.upload.addEventListener('abort', function() {console.log('abort complete')}, false);
        });

    };
    $scope.buildDataSui4j = function () {   
    	$scope.info ={
    			delay : 0,
    			reportId :''
    	}
   	 if(!$scope.selectedFiles || $scope.selectedFiles.length < 1)
   		  return
   	var fileToUpload = $scope.selectedFiles;	
   	 $rootScope.showCustomLoading = true;
   	 $("#importdataarea").css({"display":"none"});
       	$scope.upload = Upload.upload({
               url: 'rest/crawlersui4j/buildData', 
               method: 'POST',
               fields: {
               	delay : $scope.info.delay
               },
               file : fileToUpload
           });
       	
           $scope.upload.then(function (result) {
           	 $rootScope.showCustomLoading = false;
           	$("#importdataarea").css({"display":"block"});
               if (result != undefined && result != null) 
               	var data = result.data;
                   if (data.errorMessage != undefined && data.errorMessage != null)
                       $rootScope.error = data.errorMessage;
                   else {
                       $rootScope.error = '';
                       $scope.info.reportId = data.resultMessage;
                       UtilityService.notificationDilog("success!")
//                       $scope.resetForm();
               }
           }, function (error) {
           	$rootScope.showCustomLoading = false;
           	$("#importdataarea").css({"display":"block"});
               if (error.errorCode != undefined && error.errorCode != null)
                   $rootScope.error = error.errorMessage;
           }, function (evt) {
           });
           $scope.upload.xhr(function (xhr) {
//   					xhr.upload.addEventListener('abort', function() {console.log('abort complete')}, false);
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

