function UserAddController($scope, $rootScope,$state, $location, UserManagementService, RoleManagementService, $timeout, $log) {
	$scope.disabledRoles = ["ROLE_EMPLOYEE"];
    $scope.loadRoleList = function() {
        RoleManagementService.listManagementRoles().$promise.then(function(roles) {
            $scope.roleList = roles;
        }, function(errResponse) {

        });
    };

    $scope.newUser = {
        username: '',
        password: '',
        fullname: '',
        roles: { }
    };

    $scope.loadRoleList();

    $scope.saveUser = function() {
        var roles= $scope.newUser.roles;
        var check=false;
        for(role in roles){
            check=$scope.newUser.roles[role];
            if(check){
                break;
            }
        }
        if(!check){
            $scope.checkRole=true;
            return;
        }else{
            $scope.checkRole=false;
        }

        UserManagementService.create($scope.newUser).$promise.then(function(data) {
            if (data != undefined && data != null)
                if (data.errorCode != undefined && data.errorCode != null)
                    $rootScope.error = data.errorMessage;
                else
                	$state.go("admin.user");

        }, function(error) {
            if (error.errorCode != undefined && error.errorCode != null)
                $rootScope.error = error.errorMessage;
        });
    }
}

