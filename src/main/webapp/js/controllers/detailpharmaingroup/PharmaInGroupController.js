function PharmaInGroupController($scope, $location, $route, PharmaCrawlService,
		UtilityService, $timeout, $state, $log, ngTableParams, $rootScope,$cookieStore,
		ngDialog, GroupPharmaService, ProductPharmaService,$filter, $window) {
	$scope.refreshSeach = function(){
		var sysDate = new Date()
		 var firstDayOfMonth = new Date(sysDate.getFullYear(),sysDate.getMonth(),1);
		$scope.search = {
				fromTime : firstDayOfMonth,
				toTime : new Date(),
				type : {},
				productPzn : {},
				groupId : null,
				group : null,
				fromPrice : null,
				toPrice : null,
				viewType : 'PRODUCT',
				lastProductPzn : {},
				lastGroup : null,
				pharmaSettingChoose : {},
				pharmaSettingId : '',
				groupType : 'NONE',
				shop : {}
			}
	}
	$scope.resetData = function() {
		if($cookieStore.get('filter_pharma_products'))
			$scope.search = $cookieStore.get('filter_pharma_products');
		else $scope.refreshSeach();
			
	}
	$scope.resetData();
	$scope.resetAllData = function(){
		$scope.refreshSeach();
		$scope.getAllProduct();
	}
	$scope.viewTypes = [ {code:"PRODUCT", name : "Produkt"} ,{ code:"KEYWORDORCATEGORY", name: "Keyword / Kategorie"},{code:"GROUP", name:"Gruppe"}];
	$scope.groupTypes = [ {code:"NONE", name : "None"} ,{ code:"SHOP", name: "Shop"}];
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
	$scope.dialogAddGroup = function() {
		$scope.title = "Filter Option";
		$scope.getProductByGroup();
		ngDialog.open({
			template : 'partials/detailpharmaingroup/selectfilter.html',
			className : 'ngdialog-theme-default customDialog',
			scope : $scope,
			preCloseCallback : function(value) {
				$scope.edit = false;
			}
		});
	}
	$scope.closeDialog = function() {
		if (ngDialog)
			ngDialog.close();
	}
	$scope.resetForm = function() {
		$state.go($state.current.name, {}, {
			reload : true
		});
	}
	$scope.getAllGroupPharma = function() {
		$rootScope.showCustomLoading = true;
		GroupPharmaService.getAllGroupPharma().$promise.then(function(data) {
			$rootScope.showCustomLoading = false;
			if (data != undefined && data != null) {
				if (data.resultCode != undefined && data.resultCode != null
						&& data.resultCode != 200)
					$rootScope.error = data.resultMessage;
				else {
					$scope.groupPharmas = data.data;
				}
			}
		}, function(error) {
			$rootScope.showCustomLoading = false;
			$rootScope.error = "Das vortragende konnte nicht gelöscht werden.";
			if (error.errorCode != undefined && error.errorCode != null)
				$rootScope.error = error.errorMessage;
		});
	}
	$scope.getAllGroupPharma();
	
	$scope.getAllProduct = function() {
		$rootScope.showCustomLoading = true;
		ProductPharmaService.getAllProduct().$promise.then(function(data) {
			$rootScope.showCustomLoading = false;
			if (data != undefined && data != null) {
				if (data.resultCode != undefined && data.resultCode != null
						&& data.resultCode != 200)
					$rootScope.error = data.resultMessage;
				else {
					$scope.products = data.data;
					
				}
			}
		}, function(error) {
			$rootScope.showCustomLoading = false;
			$rootScope.error = "Das vortragende konnte nicht gelöscht werden.";
			if (error.errorCode != undefined && error.errorCode != null)
				$rootScope.error = error.errorMessage;
		});
	}
	$scope.getProductByGroup = function(id){
		 $rootScope.showCustomLoading = true;
		 if($scope.search.group && $scope.search.group.id){
			 $scope.search.productPzn = {};
			 $scope.search.shop = ($scope.search && $scope.search.group ) ? $scope.search.group.shopUrls : null;
			 ProductPharmaService.getProductByGroup({groupId : $scope.search.group.id}).$promise.then(function (data) {
				 $rootScope.showCustomLoading = false;
		 			if (data != undefined && data != null){
		                 if (data.errorCode)
		                	 $scope.getAllProduct();
		                 else {
		                	 $scope.products = data.data;
		                	 if(($scope.products && $scope.products.length > 0) && ($scope.search.lastGroup != $scope.search.group)){
		                		 for(var i=0; i <$scope.products.length ; i++){
		                			 $scope.search.productPzn[$scope.products[i].pzn] = true;
		                		 }
		                	 }else{
		                		 for(var i=0; i <$scope.products.length;i++){
		                			 if($scope.search.lastProductPzn){
		                					for(item in $scope.search.lastProductPzn){
		                						if(item == $scope.products[i].pzn){
		                							 $scope.search.productPzn[$scope.products[i].pzn] = $scope.search.lastProductPzn[item];
		                						}
		                					}
		                				}
		                		 }
		                	 }
		                 }
		     		}
		         }, function (error) {
		        	 $rootScope.showCustomLoading = false;
		         	$rootScope.error = "Das vortragende konnte nicht gelöscht werden.";
		             if (error.errorCode != undefined && error.errorCode != null)
		                 $rootScope.error = error.errorMessage;
	         });
		 }else  $scope.getAllProduct();
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
			listShopId : [],
			listToolId :  [],
			typeOfPharma : $scope.search.pharmaSettingChoose ? $scope.search.pharmaSettingChoose.type : null,
	    	pharmaId : $scope.search.pharmaSettingChoose ? $scope.search.pharmaSettingChoose.id : null,
			shopUrls :[]
		}
		$scope.search.lastProductPzn = $scope.search.productPzn;
		$scope.search.lastGroup = $scope.search.group;
		if($scope.search.productPzn){
			for(item in $scope.search.productPzn){
				if($scope.search.productPzn[item])
					$scope.info.pzns.push(item);
			}
		}
		if($scope.search.shop){
			for(item in $scope.search.shop){
				if($scope.search.shop[item])
					$scope.info.shopUrls.push(item);
			}
		}

	}
	$scope.getPharmaSetting = function() {
		$scope.search.pharmaSettingChoose = $scope.pharmaSetting.find(obj => {
		  return (obj.id == $scope.search.pharmaSettingId)
		})
	}
	$scope.pharmaDetail = function() {
		$scope.buildFilterCondition();
		if(!$scope.info.pzns || $scope.info.pzns.length <1 || $scope.info.pzns.length > 30){
			UtilityService.messageErrorDilog("Products are required and must be less then or equal 30 products");
			return;
		}
		$rootScope.showCustomLoading = true;
		$scope.closeDialog();
		$cookieStore.put('filter_pharma_products', $scope.search);
		PharmaCrawlService.pharmaDetail($scope.info).$promise .then( function(result) {
			$rootScope.showCustomLoading = false;
			if (result.resultCode != undefined && result.resultCode != null && result.resultCode != 200){ $rootScope.error = result.resultMessage;
			} else {
				$scope.data = result.data;
				$scope.pharmaDetailTable();
				if($scope.data && $scope.data.length > 0)
					$scope.buildSeries();
				else{
					pharmaChart = $('#pharmaChart').highcharts();
					 if(typeof pharmaChart !== 'undefined' && pharmaChart)
						 pharmaChart.destroy();
				}
				
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
	
	$scope.pharmaDetailTable = function () {
		$rootScope.showCustomLoading = true;
        $scope.tableParams = new ngTableParams({
            page: 1, // show first page
            count: 50,
            // count per page
            sorting : {  
            	importDate : 'desc'  ,
            	sequenceByKeyword : 'asc'
            }
        }, {
        	
            total:  $scope.data ? $scope.data.length : 0,
            getData: function ($defer, params) {
//                $scope.dataTable = params.sorting() ? $filter('orderBy')($scope.data, params.orderBy()) : $scope.data;
//                $scope.dataTable = params.filter() ? $filter('filter')($scope.dataTable, params.filter()) : $scope.dataTable;
                $scope.dataTable = params.filter() ? $filter('filter')($scope.data, params.filter()) : $scope.data;
            	$scope.dataTable = params.sorting() ? $filter('orderBy')($scope.dataTable, params.orderBy()) : $scope.dataTable;
                params.total($scope.dataTable.length);
                $scope.dataTable = $scope.dataTable.slice((params.page() - 1) * params.count(), params.page() * params.count());
                $defer.resolve($scope.dataTable);
                $rootScope.showCustomLoading = false;
            }
        });
    };
	
	if($cookieStore.get('filter_pharma_products'))
		$scope.pharmaDetail();
	
	$scope.buildSeries = function(){
		$scope.seriesData =  $scope.data.reduce((groups, item) => {
			 var key = item.pzn;
			 var name = item.name + "-" + item.pzn;
			 var id = item.pzn;
			 if($scope.search.viewType =='GROUP'){
				 key = 'GROUP'; name = $scope.search.group ?  $scope.search.group.name : "" ;;
			 }
			 if($scope.search.viewType =='KEYWORDORCATEGORY'){
				 key = item.keyword;	 
				 name = item.name + "-" + $scope.convertKeyword(key);
			 }	 
			 if($scope.search.groupType && $scope.search.groupType=='SHOP'){
				 key +=("-" + (item.parsehubId ? item.parsehubId : ''));
				 name += (" / " + (item.parsehubName ? item.parsehubName : ''));
			 }
			  const seriesData = (groups[key] || 
				  {
			    	id : key,
			        name: name,
			        data: []
				   }
			  );
			  seriesData.data.push({x:item.importDate, y : item.sequenceByKeyword});
			  groups[key] = seriesData;
			  return groups;
			}, {});
		$scope.series = [];
		Object.entries($scope.seriesData).forEach(
		    ([key, value]) => $scope.series.push(value)
		);
		$scope.createChart();
	}
	
	
	
	
	var pharmaChart;
	$scope.createChart = function(){
		pharmaChart = $('#pharmaChart').highcharts();
		 if(typeof pharmaChart !== 'undefined' && pharmaChart)
			 pharmaChart.destroy();
		 Highcharts.setOptions({
			    lang: {
			    	shortMonths: ['Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun', 'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez']
			    }
			});
		 pharmaChart = Highcharts.chart('pharmaChart', {
			    chart: {
			        type: 'line',
			        animation: Highcharts.svg, // don't animate in old IE
			        marginRight: 10
			    },
			    time: {
			        useUTC: false
			    },
			    title: {
			        text: 'Sichtbarkeitsauswertung'
			    },
			    plotOptions: {
			        series: {
			            lineWidth: 1,
			            turboThreshold: 0 ,
			            dataGrouping: {
		                    approximation: 'average',
		                    units: [
		                    	[ 'day', [1] ],[ 'week', [1] ], [ 'month', [1, 3, 6] ]
		                    ],
		                    forced: true,
		                    enabled: true,
		                    groupAll: true
		                },
		                tooltip: {
		                    valueDecimals: 2
		                }
			        },
	                line: {
	                    dataLabels: {
	                        enabled: true,
	                        formatter: function () {
	                            return ''+ parseFloat(this.point.y.toFixed(2));
                        	}
	                	}
                    },
                    enableMouseTracking: false,
			    },
			    xAxis: {
			        type: 'datetime',
			        alternateGridColor: '#f5f5f5'
			    },

			    yAxis: {
			        title: {
			            text: 'Platzierung'
			        },
			        plotLines: [{
			            value: 0,
			            width: 1,
			            color: '#808080'
			        }],
			        reversed:true
			    },

			    tooltip: {
			    	valueDecimals: 3,
			        headerFormat: '<b>{series.name}</b><br/>',
			        pointFormat: '{point.x:%d.%m.%Y}<br/>Platzierung: {point.y:.2f}'
			    },

			    legend: {
			        enabled: true
			    },

			    exporting: {
			    	enabled: true
			    },

			    boost: {
			        useGPUTranslations: true
			    },
			    lang: {
			    	months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
		            viewFullscreen: "Vollbild",
		            printChart: "Diagramm ausdrucken",
		            downloadPNG: 'Dowload image PNG',
		            downloadJPEG: 'Download image JPEG',
		            downloadPDF: 'Download document PDF',
		            downloadSVG: 'Download imagemSVG'
		            },

			    series: $scope.series
			});
	}
	
	$scope.updateDataGroupingUnits = function(unit){
		pharmaChart.update({
	        plotOptions: {
	            series: {
	                dataGrouping: {
	                    units: [
	                        unit
	                    ]
	                }
	            }
	        }});
//		for(var i =0 ; i < $scope.series.length; i ++){
//			pharmaChart.get($scope.series[i].pzn)
//			.update({ dataGrouping: { forced: true, units: [ ['week', [1]] ] } });
//		}
	}		
	

	$scope.convertDateToString = function(creationTime) {
		var data = moment(creationTime).format("DD.MM.YYYY");
		return data
	};
	$scope.exportPharmaDetail = function(){
		$rootScope.showCustomLoading = true;
		$scope.buildFilterCondition();
		PharmaCrawlService.exportPharmaDetail($scope.info).$promise .then( function(result) {
			$rootScope.showCustomLoading = false;
			if (result.resultCode != undefined && result.resultCode != null && result.resultCode != 200){ $rootScope.error = result.resultMessage;
			} else {
				var reportId = result.resultMessage;
				var url = "rest/pharmacrawl/downloadExcelPharmaDetail?reportId="+reportId;
				$window.open(url,'_blank');
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
