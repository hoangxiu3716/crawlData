function ActionLogListController($scope, $rootScope,$state, $location, ActionLogService, SessionService, $timeout, $log) {

    $scope.actionList = [];
    $scope.objectList = [];

    $scope.loadActions = function() {
        ActionLogService.listActions().$promise.then(function(actions) {
            actions.forEach(function(action) {
                $scope.actionList.push({
                    name: action,
                    text: $rootScope.getActionText(action)
                });
            });

        }, function(errResponse) {

        });
    };

    $scope.loadObjects = function() {
        ActionLogService.listObjects().$promise.then(function(objects) {
            objects.forEach(function(object) {
                $scope.objectList.push({
                    name: object,
                    text: $rootScope.getObjectText(object)
                });
            });

        }, function(errResponse) {

        });
    };    
    
    var storedSearchInfo = SessionService.getItem('ACTION_LOG_SEARCH_INFO');
    if (storedSearchInfo == null || storedSearchInfo == undefined)
    	$scope.searchInfo = {
            fromDate: '',
            toDate: '',
            content: '',
            performer: '',
            ip: '',
            actions: {},
            objects: {}
        };
    else
    	$scope.searchInfo = storedSearchInfo;

    //$scope.myData = {};
    $scope.sortOptions = {
        fields: ["actionTime"],
        directions: ["desc"]
    };

    $scope.filterOptions = {
        filterText: "",
        useExternalFilter: true,
        filterByFields: {}
    };

    $scope.pagingOptions = {
        pageSizes: [10, 20, 50],
        pageSize: 10,
        currentPage: 0
    };

    /*
    $scope.gridOptions = {
        data: 'actionlogList',
        columnDefs: [
            {field: 'id', displayName: '#'},
            {field: 'actionTime', displayName: 'Zeit'},
            {field: 'action', displayName: 'Aktion'},
            {field: 'actionText', displayName: 'Aktion'},
            {field: "performer", displayName: "Performer"},
            {field: 'ip', displayName: 'IP'},
            {field: 'object', displayName: 'Objekt'},
            {field: 'objectText', displayName: 'Objekt'}

        ],
        multiSelect: false,
        enablePaging: true,
        showFooter: true,
        showFilter: true,
        useExternalSorting: true,
        totalServerItems: 'totalElements',
        pagingOptions: $scope.pagingOptions,
        sortInfo: $scope.sortOptions
    };

    $scope.setPagingData = function (data, page, pageSize) {
        var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
        $scope.actionLogList = pagedData;
        $scope.actionLogList.forEach(function(actionLog) {
            actionLog.actionText = $rootScope.getActionText(actionLog.action);
            actionLog.objectText = $rootScope.getObjectText(actionLog.object);
        });
        $scope.totalServerItems = data.length;
        if (!$scope.$$phase) {
            $scope.$apply();
        }
    };

    $scope.getPagedDataAsync = function (pageSize, page, searchText) {
        setTimeout(function () {
            var data;
            if (searchText) {
                var ft = searchText.toLowerCase();
                $scope.refresh();
            } else {
                $scope.refresh();
            }
        }, 100);
    };
    */

    $scope.refresh = function () {
    	SessionService.saveItem('ACTION_LOG_SEARCH_INFO', $scope.searchInfo);
    	    	
        var getList = function () {
            //$log.debug($scope.sortOptions);
            var field;
            if ($scope.sortOptions.fields.length > 0) {
                field = $scope.sortOptions.fields[0];
                //$log.debug(field);
            }
            var direction;
            if ($scope.sortOptions.directions.length > 0) {
                direction = $scope.sortOptions.directions[0];
                //$log.debug(direction);
            }

//            var filters = $scope.filterOptions.filterByFields;
//            filters['actionTimeFromDate'] = $scope.actionTimeFilter.fromDate;
//            filters['actionTimeToDate'] = $scope.actionTimeFilter.toDate;

            ActionLogService.list({page: $scope.pagingOptions.currentPage, size: $scope.pagingOptions.pageSize, field: field, direction: direction}, $scope.searchInfo).$promise.then(function (pageData) {
                // success
                var data = pageData.data;
                $scope.actionLogList = data;
                $scope.actionLogList.forEach(function(actionLog) {
                    actionLog.actionText = $rootScope.getActionText(actionLog.action);
                    actionLog.objectText = $rootScope.getObjectText(actionLog.object);
                });
                $scope.totalPages = pageData.totalPages;
                $scope.totalElements = pageData.totalElements;

            }, function (errResponse) {
                // fail
            });
        };
        $timeout(getList, 100);
    };

    $scope.onServerSideItemsRequested = function (currentPage, filterBy, filterByFields, orderBy, orderByReverse) {
        //$scope.refresh();
        $scope.pagingOptions.currentPage = currentPage;
        $scope.sortOptions.fields[0] = orderBy;
        if (orderByReverse === false)
            $scope.sortOptions.directions[0] = 'asc';
        else
            $scope.sortOptions.directions[0] = 'desc';
        $scope.filterOptions.filterByFields = filterByFields;
        $scope.filterOptions.filterText = filterBy;

        $scope.refresh();
    };

    $scope.loadActions();
    $scope.loadObjects();

//    $scope.refresh();

    $scope.showDetail = function (log) {
//        $path = "actionlog/detail/" + log.id;
//        $state.go('actionlog.detail');
        $state.go('index.actionlogdetail', {id: log.id});
    };

    $('#fromDate').datepicker({
        dateFormat: 'dd.mm.yy',
        changeYear: true,
        changeMonth: true,
        yearRange: "2009:2020",
        prevText: '<i class="fa fa-chevron-left"></i>',
        nextText: '<i class="fa fa-chevron-right"></i>',
        onSelect: function (selectedDate) {
            //$('#toDate').datepicker('option', 'minDate', selectedDate);
            $scope.searchInfo.fromDate = selectedDate;
        }
    });

    $('#toDate').datepicker({
        dateFormat: 'dd.mm.yy',
        changeYear: true,
        changeMonth: true,
        yearRange: "2009:2020",
        prevText: '<i class="fa fa-chevron-left"></i>',
        nextText: '<i class="fa fa-chevron-right"></i>',
        onSelect: function (selectedDate) {
            //$('#fromDate').datepicker('option', 'maxDate', selectedDate);
            $scope.searchInfo.toDate = selectedDate;
        }
    });
}