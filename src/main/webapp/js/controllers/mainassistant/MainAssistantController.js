function MainAssistantController($scope,$cookieStore, $location,$route,UtilityService,WebAssistantService, $timeout,$state, $log,ngTableParams ,Upload,$rootScope,ngDialog) {
	$scope.autoLogin = {
			user : null,
			password : null
	};
	$('#pc-version').on('touchstart', function () {
        $("#pc-recent-work-wrap").trigger('hover');
    }).on('touchend', function () {
    	$("#pc-recent-work-wrap").trigger('hover');
    });
	$('#mobile-version').on('touchstart', function () {
        $("#mobile-recent-work-wrap").trigger('hover');
    }).on('touchend', function () {
    	$("#mobile-recent-work-wrap").trigger('hover');
    });

	$scope.info ={
		emaii :''
	}
	$scope.showCustomLoading = false;
	   $scope.sendEmail = function(){
		   $rootScope.error = '';
		   $scope.showCustomLoading = true;
		   WebAssistantService.sendEmail({email : $scope.info.email, user : $scope.autoLogin.user, password: $scope.autoLogin.password}).$promise.then(function (data) {
			   $scope.showCustomLoading = false;
	             if (data != undefined && data != null)
	                 if (data.errorCode != undefined && data.errorCode != null)
	                     $rootScope.error = data.errorMessage;
	                 else {
	                 	UtilityService.notificationDilog("E-Mail erfolgreich versendet. Bitte prüfen Sie Ihr Postfach.");
	                 }
	            $scope.info ={
	            			emaii :''
	            }
	            $scope.autoLogin = {
	            		user: '',
	            		password:''
	            }
	         }, function (error) {
	        	 $scope.showCustomLoading = false;
	             if (error.errorCode != undefined && error.errorCode != null)
	                 $rootScope.error = error.errorMessage;
	         });
	    };

	  $scope.isEmployee = $cookieStore.get("isEmployee");
}

