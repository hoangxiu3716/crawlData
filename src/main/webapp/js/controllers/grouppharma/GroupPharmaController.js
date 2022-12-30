function GroupPharmaController($scope, $location,$route,PharmaCrawlService,UtilityService, $timeout,$state, $log,ngTableParams ,$rootScope,ngDialog,GroupPharmaService,ProductPharmaService) {

	$scope.resetData = function(item){
		$scope.info ={
			type: item ? item.type : '',
			name:  item ? item.name : '',
			id: item ? item.id: null,
			active : item ? item.active: true,
			basePharma : item ? item.basePharma : [],
			shops : item ? item.shopDetail : {}
			
		}	
	}
	$scope.resetBase = function(){
		$scope.resetData();
		$scope.info.basePharma = [];
	}
	$scope.resetBase();
	$scope.addArea = false;
	$scope.showAddArea = function(){
		$scope.resetData();
		$scope.addArea = true;
		$scope.edit =  false;
		$scope.title = "Produktgruppen hinzufügen"
	}
		$scope.resetForm = function() {
			$state.go($state.current.name, {}, {
				reload : true
			});
		}
		$scope.resetBaseInfo = function(){
			$scope.basePharma = {
				ids : {}
			}
		}
		$scope.buildCheckedBasePharma = function(){
			$scope.resetBaseInfo();
			if($scope.info.basePharma && $scope.info.basePharma.length > 0){
				for (var i = 0; i < $scope.info.basePharma.length; i++) {
					$scope.basePharma.ids[$scope.info.basePharma[i].baseId] = true;
				}
			}
		}
		$scope.dialogPharmaDatas = function() {
			$scope.buildCheckedBasePharma();
			ngDialog.open({
				template : 'partials/grouppharma/products.html',
				className : 'ngdialog-theme-default customDialog',
				scope : $scope,
				closeByDocument  : true,
				showClose : true,
				preCloseCallback : function(value) {
				}
			});
			if ($scope.tableParamProductPharma)
				$scope.tableParamProductPharma.reload();
			else
				$scope.loadTableProductPharma();
		}
		var sysDate = new Date()
		 var firstDayOfMonth = new Date(sysDate.getFullYear(),sysDate.getMonth(),1);
			$scope.search ={
					from :firstDayOfMonth,
					to : new Date(),
					fromPrice: null,
					toPrice: null,
					fromDiscount: null,
					toDiscount: null,
					type:"",
					products: ""
			}
		$scope.loadTableProductPharma = function () {
		        $scope.tableParamProductPharma = new ngTableParams({
		            page: 1, // show first page
		            count: 50,
		            filter : {
						active : 1
					}
		            // count per page
		        }, {
		        	counts : [ 50, 100, 200, 300, 500 ],
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
		                ProductPharmaService.list({
		                    page: (params.page() - 1),
		                    size: params.count(),
		                    field: field,
		                    direction: direction,
		                    filter: params.filter()
		                }).$promise.then(function (pageData) {
		                        $scope.dataPharmaDetail = pageData.data;
		                        params.total(pageData.totalElements);
		                        $defer.resolve($scope.dataPharmaDetail);
		                        $rootScope.showCustomLoading = false;
		                    }, function (errResponse) {
		                        $rootScope.showCustomLoading = false;
		                    });
		            }
		        });
		    };
		    $scope.selectPharma = function(item) {
				if ($scope.basePharma.ids[item.id]) {
					var data = {
						id : null,
						baseId : item.id,
						baseName : item.name,
						active : item.active,
						baseActive : item.active
					}
					$scope.info.basePharma.push(data);
				} else {
					$scope.info.basePharma = $scope.info.basePharma.filter(function(
							value, index, arr) {
						return value.baseId != item.id;
					});
				}

			}
		    $scope.removePharma = function(index){
				$scope.info.basePharma.splice(index,1);
			}
//		    $scope.sortablePharma = {
//		            start: function(e, ui) {
//		            	 ui.item.data('startLeft', ui.item.index());
//		            },
//		            stop: function(e, ui) {
//		                  var start = ui.item.data('startLeft'),
//		                  end = ui.item.index();
//		                  var geo = $scope.info.basePharma[start];
//		                  $scope.info.basePharma.splice(start, 1);
//		                  $scope.info.basePharma.splice(end, 0, geo);
//		            }
//		        };
		    $scope.save = function() {
				$rootScope.error = '';
				$rootScope.showCustomLoading = true;
				$scope.info.shops = JSON.stringify($scope.info.shops);
				GroupPharmaService.create($scope.info).$promise.then(function(data) {
					$rootScope.showCustomLoading = false;
					if (data != undefined && data != null)
						if (data.resultCode != undefined && data.resultCode != null
								&& data.resultCode != 200)
							UtilityService.messageErrorDilog(data.resultMessage);
						else {
							UtilityService.notificationDilog("Success");
							$scope.addArea = false;
							$scope.tableParams.reload();
						}

				}, function(error) {
					$rootScope.showCustomLoading = false;
					if (error.errorCode != undefined && error.errorCode != null)
						$rootScope.error = error.errorMessage;
				});
			};
			$scope.update = function() {
				$rootScope.error = '';
				$rootScope.showCustomLoading = true;
				$scope.info.shops = JSON.stringify($scope.info.shops);
				GroupPharmaService.update($scope.info).$promise.then(function(data) {
					$rootScope.showCustomLoading = false;
					if (data != undefined && data != null)
						if (data.resultCode != undefined && data.resultCode != null
								&& data.resultCode != 200)
							UtilityService.messageErrorDilog(data.resultMessage);
						else {
							UtilityService.notificationDilog("Success");
							$scope.addArea = false;
							$scope.tableParams.reload();
						}

				}, function(error) {
					$rootScope.showCustomLoading = false;
					if (error.errorCode != undefined && error.errorCode != null)
						$rootScope.error = error.errorMessage;
				});
			};
			$scope.detailGroupPharma = function(id){
				 $rootScope.showCustomLoading = true;
				 $scope.title = "Edit Group Pharma"
				 GroupPharmaService.detailGroupPharma({id : id}).$promise.then(function (data) {
					 $rootScope.showCustomLoading = false;
			 			if (data != undefined && data != null){
			                 if (data.resultCode != undefined && data.resultCode != null && data.resultCode != 200)
			                     $rootScope.error = data.resultMessage;
			                 else {
			                	 $scope.info.basePharma = data.data.basePharma;
			                 }
			     		}
			         }, function (error) {
			        	 $rootScope.showCustomLoading = false;
			         	$rootScope.error = "Das vortragende konnte nicht gelöscht werden.";
			             if (error.errorCode != undefined && error.errorCode != null)
			                 $rootScope.error = error.errorMessage;
			         });
			     }
			$scope.detail= function(item){
		    	$rootScope.error = '';
		    	$scope.resetData(item);
		    	$scope.detailGroupPharma(item.id);
		    	$scope.edit =  true;
		    	$scope.addArea = true;
		    	
		    };
		    $scope.loadTable = function () {
		        $scope.tableParams = new ngTableParams({
		            page: 1, // show first page
		            count: 100,
		            filter : {
						type : $scope.info.type
					}
		            // count per page
		        }, {
		        	counts : [ 50, 100, 150, 200, 500 ],
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
		                GroupPharmaService.list({
		                    page: (params.page() - 1),
		                    size: params.count(),
		                    field: field,
		                    direction: direction,
		                    filter: params.filter(),
		                    appId : $scope.info.appId,
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
		    $scope.updateDeleded = function (id) {
		    	$rootScope.error = '';
		    	GroupPharmaService.updateDeleted({id: id}).$promise.then(function (data) {
		            if (data != undefined && data != null){
		                if (data.resultCode != undefined && data.resultCode != null && data.resultCode != 200)
		                    $rootScope.error = data.resultMessage;
		                else {
		                	UtilityService.notificationDilog("Erfolgreich gelöscht");
		                	$scope.resetForm();
		                }
		    		}	
		        }, function (error) {
		        	$rootScope.error = "Das Menu konnte nicht gelöscht werden.";
		            if (error.errorCode != undefined && error.errorCode != null)
		                $rootScope.error = error.errorMessage;
		        });
		    }
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
}

