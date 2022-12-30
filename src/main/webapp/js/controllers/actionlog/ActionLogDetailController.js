function ActionLogDetailController($scope, $rootScope, $state, $location, ActionLogService, $timeout, $log) {
    ActionLogService.get({id: $state.params.id}).$promise.then(function(actionLog) {
        $scope.actionLogItem = {
            id: actionLog.id,
            performer: actionLog.performer,
            ip: actionLog.ip,
            actionTime: actionLog.actionTime,
            content: actionLog.content,
            action: $rootScope.getActionText(actionLog.action),
            object: $rootScope.getObjectText(actionLog.object)
        }

    }, function(errResponse) {

    });

}

