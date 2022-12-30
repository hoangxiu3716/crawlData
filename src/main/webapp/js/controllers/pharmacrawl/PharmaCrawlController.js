function PharmaCrawlController($scope, $location,$route,PharmaCrawlService,UtilityService, $timeout,$state, $log,ngTableParams ,$rootScope,ngDialog,$filter) {
 

	$scope.showOptions = false;
	$scope.loadMore = function (value) {
		if(value)
			$scope.showOptions = true;
		else
			$scope.showOptions = false;
	}
	var sysDate = new Date()
	 var firstDayOfMonth = new Date(sysDate.getFullYear(),sysDate.getMonth(),1);
		$scope.resetData = function(){
			$scope.search ={
					pharmaSettingId : null,
					fromTime : moment().set({hour:0,minute:0,second:0}),
					toTime :  moment().set({hour:23,minute:59,second:59}),
					type : '',
					pharmaSettingChoose : {},
					fromPrice : null,
					toPrice : null,
					shop : {}
			}
		}
		$scope.resetData();
		$scope.getPharmaSetting = function() {
			$scope.search.pharmaSettingChoose = $scope.pharmaSetting.find(obj => {
			  return (obj.id == $scope.search.pharmaSettingId)
			})
    	}
		
		$scope.buildFilterCondition = function(){
			$scope.info = {
				fromTime : 	$scope.search.fromTime,
				toTime : 	$scope.search.toTime ? moment($scope.search.toTime).set({hour:23,minute:59,second:59}) : null,
				pzns : [],
				fromPrice : $scope.search.fromPrice,
				toPrice : $scope.search.toPrice,
				pharmaKeyword : $scope.search.pharmaSettingChoose ? $scope.search.pharmaSettingChoose.pharmaKeyword : null,
				parsehubKeyword : $scope.search.pharmaSettingChoose ? $scope.search.pharmaSettingChoose.parsehubKeyword : null,
				keywordName : 	 $scope.search.pharmaSettingChoose ? $scope.search.pharmaSettingChoose.name : '',	
				shopId : $scope.search.shop ? $scope.search.shop.id : null,
				toolId :  $scope.search.shop ? $scope.search.shop.toolId : null,
				shopUrl :  $scope.search.shop ? $scope.search.shop.url : null,
				typeOfPharma : $scope.search.pharmaSettingChoose ? $scope.search.pharmaSettingChoose.type : null,
			    pharmaId : $scope.search.pharmaSettingChoose ? $scope.search.pharmaSettingChoose.id : null,
	    		childrenId : $scope.search.shop ? $scope.search.shop.childrenId : null,
	    		parentId : $scope.search.pharmaSettingChoose ? $scope.search.pharmaSettingChoose.parentId : null
			}

		}
		$scope.pharmaDetail = function() {
			$scope.buildFilterCondition();
			$rootScope.showCustomLoading = true;
			PharmaCrawlService.pharmaDetail($scope.info).$promise .then( function(result) {
				$rootScope.showCustomLoading = false;
				if (result.resultCode != undefined && result.resultCode != null && result.resultCode != 200){ $rootScope.error = result.resultMessage;
				} else {
					$scope.data = result.data;
					$scope.pharmaDetailTable();
				}
			},
			function(error) {
				$rootScope.showCustomLoading = false;
				$rootScope.error = "Das vortragende konnte nicht gelöscht werden.";
				if (error.errorCode != undefined
						&& error.errorCode != null)
					$rootScope.error = error.errorMessage;
			});
		}
		$scope.pharmaDetail();
		$scope.pharmaDetailTable = function () {
			$rootScope.showCustomLoading = true;
	        $scope.tableParams = new ngTableParams({
	            page: 1, // show first page
	            count: 50,
	            // count per page
	            sorting : {  
	            	importDate : 'desc',
	            	sequenceByKeyword : 'asc'
	            }
	        }, {
	        	
	            total: $scope.data ? $scope.data.length : 0,
	            getData: function ($defer, params) {
//	                $scope.dataTable = params.sorting() ? $filter('orderBy')($scope.data, params.orderBy()) : $scope.data;
//	                $scope.dataTable = params.filter() ? $filter('filter')($scope.dataTable, params.filter()) : $scope.dataTable;
	            	$scope.dataTable = params.filter() ? $filter('filter')($scope.data, params.filter()) : $scope.data;
	            	$scope.dataTable = params.sorting() ? $filter('orderBy')($scope.dataTable, params.orderBy()) : $scope.dataTable;
		               
	                params.total($scope.dataTable.length);
	                $scope.dataTable = $scope.dataTable.slice((params.page() - 1) * params.count(), params.page() * params.count());
	                $defer.resolve($scope.dataTable);
	                $rootScope.showCustomLoading = false;
	                $scope.loadKeyword = true;
	                $scope.loadShopName = true;
	            }
	        });
	    };
		
		
		$scope.loadKeyword = false;
		$scope.convertPriceToDouble =  function(fullPrice){
			if(fullPrice){
				fullPrice = fullPrice.replace(/[^a-zA-Z0-9\,\.\€]/g,'');
				if(fullPrice.indexOf("VP") != -1){
					var fullPriceClone = fullPrice.substr(fullPrice.indexOf("VP"),fullPrice.length);
					var number = fullPriceClone.indexOf("€");
					var testfullPrice = fullPriceClone.substr(fullPriceClone.indexOf("VP"),number);
		    		var data = testfullPrice.replace("VP","").trim();
				}else if(fullPrice.indexOf("UnserPreis") != -1)
					{
					var data = fullPrice.replace("UnserPreis","").replace("€","").trim();
		
					}else{
						var data = fullPrice.replace("€","").trim();
					}
				return data
			}
		};
    	$scope.getAllPharmaSetting = function() {
    		$rootScope.showCustomLoading = true;
    		PharmaCrawlService.getAllPharmaSetting({}).$promise.then(function(data) {
    			$rootScope.showCustomLoading = false;
    			if (data != undefined && data != null) {
    				if (data.resultCode != undefined && data.resultCode != null
    						&& data.resultCode != 200)
    					$rootScope.error = data.resultMessage;
    				else {
    					$scope.pharmaSetting = data.data;
    				}
    			}
    		}, function(error) {
    			$rootScope.showCustomLoading = false;
    			$rootScope.error = "Das vortragende konnte nicht gelöscht werden.";
    			if (error.errorCode != undefined && error.errorCode != null)
    				$rootScope.error = error.errorMessage;
    		});
    	}
    	$scope.getAllPharmaSetting();
    	$scope.getAllParsehubSetting = function() {
    		$rootScope.showCustomLoading = true;
    		PharmaCrawlService.getAllParsehubSetting({}).$promise.then(function(data) {
    			$rootScope.showCustomLoading = false;
    			if (data != undefined && data != null) {
    				if (data.resultCode != undefined && data.resultCode != null
    						&& data.resultCode != 200)
    					$rootScope.error = data.resultMessage;
    				else {
    					$scope.parsehubSetting = data.data;
    				}
    			}
    		}, function(error) {
    			$rootScope.showCustomLoading = false;
    			$rootScope.error = "Das vortragende konnte nicht gelöscht werden.";
    			if (error.errorCode != undefined && error.errorCode != null)
    				$rootScope.error = error.errorMessage;
    		});
    	}
    	$scope.getAllParsehubSetting();
    	$scope.loadKeywordFalse = function(){
    		$scope.loadKeyword = false;
    		$scope.getPharmaSetting();
    	}
    	$scope.loadShopNameFalse = function(){
    		$scope.loadShopName = false;
    	}
    	$scope.convertKeyword =  function(keyword){
    		if($scope.info && $scope.info.keywordName) return $scope.info.keywordName;
    		var data;
    		if(keyword){
        		data = $scope.pharmaSetting.find(obj => {
        			if(obj.type != 'CROSS-SELLING'){
        				if(keyword == 'dia_teststreifen')
    		  				return (obj.parsehubKeyword == 'diabetes_teststreifen')
    		  			if(keyword == 'kat_1')
    		  				return ( obj.parsehubKeyword == 'kat1')
    		  			if(keyword == 'kat_2')
    		  				return (obj.parsehubKeyword == 'kat2')		
       			  		return (obj.parsehubKeyword == keyword || obj.pharmaKeyword == keyword);
        			}
    		  		else
    		  			return (obj.parsehubKeyword == keyword || obj.pharmaKeyword == keyword || obj.parsehubKeyword == keyword.split("_")[1] || obj.pharmaKeyword == keyword.split("_")[1])
       			})
    		}
    		if(data) return data.name;
    		return keyword;
    	};
    	$scope.convertPrice = function(priceInDouble){
    		var resultInTwo = parseFloat(priceInDouble).toFixed(2);
    		var result = resultInTwo.replace(".",",");
    		return result
    	};
    	$scope.convertShopName = function(parsehubName){
    		if($scope.loadShopName && $scope.search.shop && $scope.search.shop.id){
    			 parsehubName = $scope.search.shop.name;
    		}
    		if(parsehubName === "" && !$scope.search.shop.id && $scope.loadShopName)
    			parsehubName = "Sanicare";
    		return parsehubName
    	}
    	$scope.convertAvpToDouble = function(avp){
    		var resultInTwo = parseFloat(avp).toFixed(2);
    		var result = resultInTwo.replace(".",",");
    		return result
    	};
}

