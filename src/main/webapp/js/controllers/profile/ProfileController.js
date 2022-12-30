function ProfileController($scope, $rootScope, $routeParams, $location, ProfileService, SecurityService, $timeout, $log) {

    ProfileService.get().$promise.then(function(userInfo) {
        $scope.user = {
            username: userInfo.username,
            fullname: userInfo.fullname
        };

    }, function(errResponse) {

    });

    $scope.reset = function() {
        $scope.passwordInput = {
            password: '',
            newPassword: '',
            repeatPassword: ''
        }
    }

    $scope.saveUser = function() {
        ProfileService.update($scope.user).$promise.then(function(data) {
            if (data != undefined && data != null)
                if (data.errorCode != undefined && data.errorCode != null)
                    $rootScope.error = data.errorMessage;
                else
                    SecurityService.get(function (user) {
                        $rootScope.user = user;
                        $scope.changesuccess=true;
//                        $rootScope.go("/home");
                    });

        }, function(error) {
            if (error.errorCode != undefined && error.errorCode != null)
                $rootScope.error = error.errorMessage;
        });
    }

    $scope.changePassword = function() {
        ProfileService.changePassword($scope.passwordInput).$promise.then(function(data) {
            if (data != undefined && data != null)
                if (data.errorCode != undefined && data.errorCode != null)
                    $rootScope.error = data.errorMessage;
                else {
                    //var message = "Das Passwort wurde erfolgreich geändert";
                	alert("Das Passwort wurde erfolgreich geändert. Bitte melden Sie sich erneut an.");
                    $scope.reset();
                    $rootScope.logout();
                    //alert(message);
                }

        }, function(error) {
            if (error.errorCode != undefined && error.errorCode != null)
                $rootScope.error = error.errorMessage;
        });
    }
}

