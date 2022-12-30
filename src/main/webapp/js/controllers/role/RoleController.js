function RoleController($scope, $location, RoleManagementService, $timeout, $log) {
    //$scope.myData = {};
    $scope.sortOptions = {
        fields: ["name"],
        directions: ["asc"]
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

    $scope.gridOptions = {
        data: 'roleList',
        columnDefs: [
            {field: 'name', displayName: 'Name'},
            {field: 'description', displayName: 'Description'},
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

    $scope.setPagingData = function(data, page, pageSize) {
        var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
        $scope.roleList = pagedData;
        $scope.totalServerItems = data.length;
        if (!$scope.$$phase) {
            $scope.$apply();
        }
    };

    $scope.getPagedDataAsync = function(pageSize, page, searchText) {
        setTimeout(function() {
            var data;
            if (searchText) {
                var ft = searchText.toLowerCase();
                $scope.refresh();
            } else {
                $scope.refresh();
            }
        }, 100);
    };

    $scope.refresh = function() {
        var getList = function() {
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
            RoleManagementService.list().$promise.then(function(pageData) {
                // success
                var data = pageData.data;
                $scope.roleList = data;
                $scope.totalPages = pageData.totalPages;
                $scope.totalElements = pageData.totalElements;

            }, function(errResponse) {
                // fail
            });
        };
        $timeout(getList, 100);
    };

    $scope.onServerSideItemsRequested = function(currentPage, filterBy, filterByFields, orderBy, orderByReverse){
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
    $scope.refresh();
}

