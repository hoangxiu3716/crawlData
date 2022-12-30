function TeamsController($scope, $location,$route,TeamsService,UtilityService, $timeout,$state, $log,ngTableParams ,$rootScope,ngDialog) {
 
	$scope.info ={
			id :'',
			nameOnFlashscore : '',
			nameOnWettportal : '',
			code :''
	}

	$scope.rePassword = '';
	$scope.edit = false ;
	$scope.addArea = false;
	$scope.heading ="Teams hinzufügen";
	$scope.showAddArea = function(){
		$scope.addArea = true;
		$scope.heading ="Teams hinzufügen";
	};
  
	   $scope.save = function(){
		   $rootScope.error = '';
		   TeamsService.create($scope.info).$promise.then(function (data) {
	             if (data != undefined && data != null)
	                 if (data.errorCode != undefined && data.errorCode != null)
	                     $rootScope.error = data.errorMessage;
	                 else {
	                 	UtilityService.notificationDilog("Teams erfolgreich hinzugefügt!");
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
    			nameOnFlashscore : item.nameOnFlashscore,
    			nameOnWettportal : item.nameOnWettportal,
    			code : item.code
	    	}
	    	$scope.edit =  true;
	    	$scope.addArea = true;
	    	$scope.heading ="Teams bearbeiten";
	    };
	    
	    $scope.update = function(){
	    	$rootScope.error = '';
	    	TeamsService.update($scope.info).$promise.then(function (data) {
	             if (data != undefined && data != null)
	                 if (data.errorCode != undefined && data.errorCode != null)
	                     $rootScope.error = data.errorMessage;
	                 else {
	                 	UtilityService.notificationDilog("Teams erfolgreich geändert!");
	                 	$scope.resetForm(); 
	                 	
	                 }

	         }, function (error) {
	             if (error.errorCode != undefined && error.errorCode != null)
	                 $rootScope.error = error.errorMessage;
	         });
	    };
	    
	    $scope.deleteItem = function (id) {
	    	$rootScope.error = '';
	    	TeamsService.delete({id: id}).$promise.then(function (data) {
	            if (data != undefined && data != null)
	                if (data.errorCode != undefined && data.errorCode != null)
	                    $rootScope.error = data.errorMessage;
	                else {
	                	UtilityService.notificationDilog("Der ausgewählte Teams wurde gelöscht!");
	                	$scope.tableParams.reload();
	                }
	        }, function (error) {
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
	                TeamsService.list({
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

