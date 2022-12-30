function ReportpageController($scope,$stateParams,$state,$cookieStore) {
		$scope.inProcess = 10;
		$scope.hidenReportPage = true;

		if(window.reportData){
			$scope.witnessesAvailables =  window.reportData.witnessesAvailables;
			$scope.people = window.reportData.people;
			$scope.info = window.reportData.info;
		}
}