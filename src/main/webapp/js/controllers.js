/**
 * INSPINIA - Responsive Admin Theme
 *
 */

/**
 * MainCtrl - controller
 */
function MainCtrl() {

    this.userName = 'Example user';
    this.helloText = 'Welcome in SeedProject';
    this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

};

function IndexController($scope, NewsService) {

       $scope.newsEntries = NewsService.query();

       $scope.deleteEntry = function (newsEntry) {
           newsEntry.$remove(function () {
               $scope.newsEntries = NewsService.query();
           });
       };
   }
   ;


   function EditController($scope, $rootScope, $routeParams, $location, NewsService) {

       $scope.newsEntry = NewsService.get({id: $routeParams.id});

       $scope.save = function () {
           $scope.newsEntry.$save(function () {
               $rootScope.go('/');
           });
       };
   }
   ;


   function CreateController($scope, $rootScope, $location, NewsService) {

       $scope.newsEntry = new NewsService();

       $scope.save = function () {
           $scope.newsEntry.$save(function () {
               $rootScope.go('/');
           });
       };
   }
   ;


   function LoginController($scope, $rootScope,$location,$state, $cookieStore, SecurityService,LangFactory,SweetAlert) {
	   
       $scope.rememberMe = true;
//       $rootScope.centerId = null;
       $rootScope.center = null;

       $scope.login = function () {
           SecurityService.authenticate($.param({
               username: $scope.username,
               password: $scope.password
           }), function (authenticationResult) {
        	   var authToken = (authenticationResult && authenticationResult.left) ? authenticationResult.left.token : null;
//               if(authenticationResult.right){
//            	   $cookieStore.put('isEmployee', true);
//               } else {
//            	   $cookieStore.remove('isEmployee');
//               }
//               $rootScope.authToken = authToken;
              

               if (authToken == null) {
            	   SweetAlert.swal({
	       		        title: 'Login fehlgeschlagen',
	       		        text: 'Bitte überprüfen	Sie	Ihren Benutzernamen und Passwort.',
	       		        type: "warning",
	       		        showCancelButton: false,
	       		        confirmButtonColor: "#516A81",
	       		        confirmButtonText: "Ok",
	       		        closeOnConfirm: true,
	       		        closeOnCancel: false },
	       		    function (isConfirm) {
	       		        if (isConfirm) {
	       		         return;
	       		        } else {
	       		        }
	       		    });
            	   return;
               }
               if ($scope.rememberMe) {
                   $cookieStore.put('Claimstar_Admiral_authToken', authToken);
               }
               
               SecurityService.get(function (user) {
                   $rootScope.user = user;
                   if($rootScope.hasRole('ROLE_EMPLOYEES') || $rootScope.hasRole('ROLE_AGENCY') || $rootScope.hasRole('ROLE_AGENT'))
                	   $state.go("main");
                   else
                	   $state.go("index.pharmacrawl");
                   LangFactory.setDefauLanguge();
               });
           });
       };
       
       $scope.forgotPassword = function(){
    	   SweetAlert.swal({
		        title: 'Passwort vergessen?',
		        text: 'Falls Sie ihr Passwort vergessen haben kontaktieren Sie uns bitte telefonisch unter der Telefonnummer +49 261 2016868 oder senden uns eine E-Mail an kontakt@gimik.eu, damit wir Ihr Passwort zurücksetzen können.',
		        type: "info",
		        showCancelButton: false,
		        confirmButtonColor: "#516A81",
		        confirmButtonText: "Schließen",
		        closeOnConfirm: true,
		        closeOnCancel: true },
		    function (isConfirm) {
		        if (isConfirm) {
//		            SweetAlert.swal("Deleted!", "Your imaginary file has been deleted.", "success");
		        } else {
//		            SweetAlert.swal("Cancelled", "Your imaginary file is safe :)", "error");
		        }
		    });
       }
   }
   ;
   
	
