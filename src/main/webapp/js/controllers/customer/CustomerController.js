function CustomerController($scope, $location,$route,CustomerService,UtilityService, $timeout,$state, $log,ngTableParams ,Upload,$rootScope,ngDialog) {
 
	$scope.info ={
			 id : "",
			 customerId : "",
			 policyNumber : "",
			 startDate : "",
			 lastName : "",
			 firstName : "",
			 street : "",
			 number : "",
			 zip : "",
			 city : "",
			 email : "",
			 hsn : "",
			 tsn : "",
			 model : "",
			 brand : "",
			 licencePlateNumber: "",
			 dateOfFirstRegistration : "",
			 tarif : '',
			 chassisNumber : "",
			 deductible : "",
			 roadsideAssistance : "",
			 covertypLiability : "",
			 covertypPartiallyComprehensive : "",
			 covertypFullyCoprehensiveInsurance: "",
			 fu2040Mandatory: "",
			 fu1020Optional: "",
			 fu2040Optional : "",
			 fu3060Optional  : "",
			 rentalCar  : "",
			 garageCommitment  : "",
			 ristTaker  : "",
			 akbVersion : "",
			 firstContractBegin: "",
			 birthday : "",
			 phone : "",
			 mobile : "",
			 vkSb : "",
			 tkSb  : "",
			 timestamp : "",
			 vn_kfz_his : null,
			 fahrleistung : null,
			 vertrag_vorSchd_anz : null,
			 vertrag_letztVorschd_monat : null
	}


	$scope.addArea = false;
	$scope.heading ="Details";
	$scope.showAddArea = function(){
		$scope.addArea = true;
	};
  
	    $scope.detail= function(item){
	    	$rootScope.error = '';
	    	
	    	$scope.info ={
	    			 id : item.id,
	    			 customerId : item.customerId,
	    			 policyNumber : item.policyNumber,
	    			 startDate : item.startDate,
	    			 lastName : item.lastName,
	    			 firstName : item.firstName,
	    			 street : item.street,
	    			 number : item.number,
	    			 zip : item.zip,
	    			 city : item.city,
	    			 email : item.email,
	    			 hsn : item.hsn,
	    			 tsn : item.tsn,
	    			 model : item.model,
	    			 brand : item.brand,
	    			 licencePlateNumber: item.licencePlateNumber,
	    			 dateOfFirstRegistration : item.dateOfFirstRegistration,
//	    			 rateFullyComprehensive :item.rateFullyComprehensive,
//	    			 ratePartiallyComprehensive : item.ratePartiallyComprehensive,
	    			 tarif : item.tarif,
	    			 chassisNumber : item.chassisNumber,
	    			 deductible : item.deductible,
	    			 roadsideAssistance : item.roadsideAssistance,
	    			 covertypLiability : item.covertypLiability,
	    			 covertypPartiallyComprehensive : item.covertypPartiallyComprehensive,
	    			 covertypFullyCoprehensiveInsurance: item.covertypFullyCoprehensiveInsurance,
	    			 fu2040Mandatory: item.fu2040Mandatory,
	    			 fu1020Optional: item.fu1020Optional,
	    			 fu2040Optional : item.fu2040Optional,
	    			 fu3060Optional  : item.fu3060Optional,
	    			 rentalCar  : item.rentalCar,
	    			 garageCommitment  : item.garageCommitment,
	    			 ristTaker  : item.ristTaker,
	    			 akbVersion : item.akbVersion,
	    			 firstContractBegin: item.firstContractBegin,
	    			 birthday : item.birthday,
	    			 phone : item.phone,
	    			 mobile : item.mobile,
	    			 vkSb : item.vkSb,
	    			 tkSb  : item.tkSb,
	    			 timestamp : item.timestamp,
	    			 vn_kfz_his : item.vn_kfz_his,
	    			 fahrleistung : item.fahrleistung,
	    			 vertrag_vorSchd_anz : item.vertrag_vorSchd_anz,
	    			 vertrag_letztVorschd_monat : item.vertrag_letztVorschd_monat
	    	}
	    	$scope.addArea = true;
	    };
	    
	    
	 
	
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
	                CustomerService.list({
	                    page: (params.page() - 1),
	                    size: params.count(),
	                    field: field,
	                    direction: direction,
	                    filter: params.filter()
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
	    $scope.selectedFiles = null;
	    $scope.onFileSelect = function ($files) {
				if($files==null)
					return;
				$scope.selectedFiles = $files;
			};
			
			  $scope.importData = function () {   	
				 if(!$scope.selectedFiles || $scope.selectedFiles.length < 1)
					  return
		    	var fileToUpload = $scope.selectedFiles;	  
				 $("#importdataarea").css({"display":"none"});
			    	$scope.upload = Upload.upload({
			            url: 'rest/customer/importData',
			            method: 'POST',
			            fields: {

			            },
			            file : fileToUpload
			        });
			    	
			        $scope.upload.then(function (result) {
			        	$("#importdataarea").css({"display":"block"});
			            if (result != undefined && result != null) 
			            	var data = result.data;
			                if (data.errorMessage != undefined && data.errorMessage != null)
			                    $rootScope.error = data.errorMessage;
			                else {
			                    $rootScope.error = '';
			                    UtilityService.notificationDilog("success!")
			                    $scope.resetForm();
		                }
			        }, function (error) {
			        	$("#importdataarea").css({"display":"block"});
			            if (error.errorCode != undefined && error.errorCode != null)
			                $rootScope.error = error.errorMessage;
			        }, function (evt) {
			        });
			        $scope.upload.xhr(function (xhr) {
//						xhr.upload.addEventListener('abort', function() {console.log('abort complete')}, false);
			        });

			    };
	    
}

