function UserEditController($scope, $rootScope, $routeParams,$state, $location, UserManagementService, RoleManagementService, $timeout, $log) {
	$scope.disabledRoles = ["ROLE_EMPLOYEE"];
    $scope.loadRoleList = function() {
        RoleManagementService.listManagementRoles().$promise.then(function(roles) {
            $scope.roleList = roles;

        }, function(errResponse) {

        });
    };

    $scope.loadRoleList();

    UserManagementService.read({id: $state.params.id}).$promise.then(function(userInfo) {
        $scope.user = {
            username: userInfo.username,
            fullname: userInfo.fullname,
            roles: userInfo.roles
        };

    }, function(errResponse) {

    });

    $scope.saveUser = function() {
        var roles= $scope.user.roles;
        var check=false;
        for(role in roles){
            check=$scope.user.roles[role];
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

        UserManagementService.update({id: $state.params.id}, $scope.user).$promise.then(function(data) {
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

