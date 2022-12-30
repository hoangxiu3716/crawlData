function UserListController($scope, $location,$state, UserManagementService, $timeout, $log) {
    //$scope.myData = {};
    $scope.sortOptions = {
        fields: ["username"],
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

    $scope.cbVal = function(row) {
        //console.log('r item resolved here ',row);
        return ((row.enabled) === true) ? 1 : 0;
    };

    $scope.enableUser = function(row) {
        //console.log(row);
        $id = row.id;
        UserManagementService.save({id: $id}, row).$promise.then(function(response) {
            console.log(response);
        }, function(errResponse) {
            console.log(errResponse);
        });
    };

    var checkboxCellTemplate = '<div class="ngSelectionCell"><input tabindex="-1" class="ngSelectionCheckbox" type="checkbox" ng-model="row.entity.enabled" ng-change="enableUser(row.entity)"/></div>';  // wrks best 
    $scope.gridOptions = {
        data: 'userList',
        columnDefs: [
            {field: 'id', displayName: 'ID'},
            {field: 'username', displayName: 'Username'},
            {field: 'fullname', displayName: 'Fullname'},
            {field: 'roles', displayName: 'Roles'},
            {field: 'displayRoles', displayName: 'Roles'}
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
        $scope.userList = pagedData;
        $scope.totalServerItems = data.length;
        if (!$scope.$$phase) {
            $scope.$apply();
        }
    };

    /*
     // watches
     $scope.$watch('pagingOptions', function(newVal, oldVal) {
     if (newVal !== oldVal) {
     $scope.refresh();
     }
     }, true);
     
     $scope.$watch('filterOptions', function(newVal, oldVal) {
     if (newVal !== oldVal) {
     $scope.refresh();
     }
     }, true);
     */
    
    /*
     * use these watch only for ng-grid
    $scope.$watch('sortOptions', function(newVal, oldVal) {
        if (newVal !== oldVal) {
            $scope.refresh();
        }
    }, true);

    $scope.$watch('pagingOptions', function(newVal, oldVal) {
        if (newVal !== oldVal) {
            //$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
            $scope.refresh();
        }
    }, true);

    $scope.$watch('filterOptions', function(newVal, oldVal) {
        if (newVal !== oldVal) {
            //$scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
            $scope.refresh();
        }
    }, true);
    */
   
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
            UserManagementService.list({page: $scope.pagingOptions.currentPage, size: $scope.pagingOptions.pageSize, field: field, direction: direction, filter: $scope.filterOptions.filterByFields}).$promise.then(function(pageData) {
                // success
                var data = pageData.data;
                $scope.userList = data;
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

    $scope.deleteUser = function (user) {
        UserManagementService.delete({id: user.id})
            .$promise.then(function() {
                // success
                $scope.refresh();

            }, function(errResponse) {
                // fail
            });
    };

    $scope.editUser = function (user) {
        $state.go('admin.edituser' , {id: user.id});
    };
}

