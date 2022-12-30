function AgencyController($scope, $location,$route,EmployeesService,UtilityService, $timeout,$state, $log,ngTableParams ,$rootScope,ngDialog) {
 
	$scope.info ={
			id :'',
			name : '',
			phone : '',
			email :'',
			street : '',
			postCode : '',
			city : '',
			username :'',
			password : '',
			type : 'ROLE_AGENCY'
	}

	$scope.rePassword = '';
	$scope.edit = false ;
	$scope.addArea = false;
	$scope.heading ="Agentur hinzufügen";
	$scope.showAddArea = function(){
		$scope.addArea = true;
		$scope.heading ="Agentur hinzufügen";
	};
  
	   $scope.save = function(){
		   $rootScope.error = '';
		   EmployeesService.create($scope.info).$promise.then(function (data) {
	             if (data != undefined && data != null)
	                 if (data.errorCode != undefined && data.errorCode != null)
	                     $rootScope.error = data.errorMessage;
	                 else {
	                 	UtilityService.notificationDilog("Agentur erfolgreich hinzugefügt!");
	                 	$scope.resetForm(); 
	                 }

	         }, function (error) {
	             if (error.errorCode != undefined && error.errorCode != null)
	                 $rootScope.error = error.errorMessage;
	         });
	    };
	    
	    $scope.detail= function(item){
	    	$rootScope.error = '';
	    	
	    	$scope.info ={
	    			id :item.id,
	    			name : item.name,
	    			phone : item.phone,
	    			email : item.email,
	    			street : item.street,
	    			postCode : item.postCode,
	    			city : item.city
	    	}
	    	$scope.info.username = item.userInfo ?  item.userInfo.username : "" ;
	    	$scope.edit =  true;
	    	$scope.addArea = true;
	    	$scope.heading ="Agentur bearbeiten";
	    };
	    
	    $scope.update = function(){
	    	$rootScope.error = '';
	    	EmployeesService.update($scope.info).$promise.then(function (data) {
	             if (data != undefined && data != null)
	                 if (data.errorCode != undefined && data.errorCode != null)
	                     $rootScope.error = data.errorMessage;
	                 else {
	                 	UtilityService.notificationDilog("Agentur erfolgreich geändert!");
	                 	$scope.resetForm(); 
	                 	
	                 }

	         }, function (error) {
	             if (error.errorCode != undefined && error.errorCode != null)
	                 $rootScope.error = error.errorMessage;
	         });
	    };
	    
	    $scope.deleteItem = function (id) {
	    	$rootScope.error = '';
	    	EmployeesService.delete({id: id}).$promise.then(function (data) {
	            if (data != undefined && data != null)
	                if (data.errorCode != undefined && data.errorCode != null)
	                    $rootScope.error = data.errorMessage;
	                else {
	                	UtilityService.notificationDilog("Der ausgewählte Agentur wurde gelöscht!");
	                	$scope.tableParams.reload();
	                }
	        }, function (error) {
	            if (error.errorCode != undefined && error.errorCode != null)
	                $rootScope.error = error.errorMessage;
	        });
	    }
	   
	$scope.passwordInput = {
			employeesId :'',
			username: '',
            newPassword: '',
            repeatPassword: ''
    }    
	$scope.dialogResetPass = function(item){
		$scope.passwordInput.employeesId = item.id;
		$scope.passwordInput.username = item.userInfo  ? item.userInfo.username : ""  ;
		ngDialog.open({
	        template: 'partials/agency/resetPass.html',
	        className: 'ngdialog-theme-default customDialog',
	        scope: $scope
	    });
	}
	    
    $scope.changePassword = function() {
    	EmployeesService.changePassEmployees($scope.passwordInput).$promise.then(function(data) {
            if (data != undefined && data != null)
                if (data.errorCode != undefined && data.errorCode != null)
                    $rootScope.error = data.errorMessage;
                else {
                	ngDialog.close();
                	UtilityService.notificationDilog("Paswort ändern erfolgreich");
                    //alert(message);
                }

        }, function(error) {
        	$rootScope.error = "Das Agentur konnte nicht gelöscht werden.";
            if (error.errorCode != undefined && error.errorCode != null)
                $rootScope.error = error.errorMessage;
        });
    }
	
    $scope.resetForm = function(){
    	$state.go($state.current.name, {}, {reload: true});
//    	$route.reload();
    }
    
	
	   $scope.loadTable = function () {
	        $scope.tableParams = new ngTableParams({
	            page: 1, // show first page
	            count: 10
	            // count per page
	        }, {

	            total: 0,
	            getData: function ($defer, params) {
	                $rootScope.showCustomLoading = true;
	                // use build-in angular filter
	                var sorting = params.sorting();
	                var field;
	                var direction;
	                for (var key in sorting) {
	                    field = key;
	                    direction = sorting[field];
	                }
	                EmployeesService.list({
	                    page: (params.page() - 1),
	                    size: params.count(),
	                    field: field,
	                    direction: direction,
	                    filter: params.filter(),
	                    type : $scope.info.type
	                }).$promise.then(function (pageData) {
	                        $scope.datas = pageData.data;
	                        params.total(pageData.totalElements);
	                        $defer.resolve($scope.datas);
	                        $rootScope.showCustomLoading = false;
	                    }, function (errResponse) {
	                        $rootScope.showCustomLoading = false;
	                    });
	            }
	        });
	    };
	    $scope.loadTable();
}

