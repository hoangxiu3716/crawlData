/**
 * INSPINIA - Responsive Admin Theme
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */


function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider,$httpProvider) {
    $urlRouterProvider.otherwise("/login");

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });
    $stateProvider.state('crawlers', {
	        url: "/crawlers",
	        templateUrl: "partials/crawlers/index.html",
	        controller: CrawlersController
    	}).state('soccertool', {
	        url: "/soccertool",
	        templateUrl: "partials/soccertool/index.html",
	        controller: SoccerToolController
    	}).state('forebettoday', {
            url: "/forebettoday",
            templateUrl: "partials/forebettoday/index.html",
	        controller: ForebetTodayController
        }).state('login', {
	        url: "/login",
	        templateUrl: "views/login.html",
	        controller: LoginController
	    }).state('index', {
            abstract: true,
            url: "/index",
            templateUrl: "views/common/content.html",
        }).state('index.welcome', {
            url: "/welcome",
            templateUrl: "partials/home.html"
        }).state('index.teams', {
            url: "/teams",
            templateUrl: "partials/teams/index.html",
	        controller: TeamsController
        })
        .state('index.actionlog', {
            url: "/actionlog",
            templateUrl: "partials/actionlog/index.html",
            controller: ActionLogListController
        }).state('index.actionlogdetail', {
            url: "/actionlogdetail/:id",
            templateUrl: "partials/actionlog/detail.html",
            controller: ActionLogDetailController
        }).state('admin', {
            abstract: true,
            url: "/admin",
            templateUrl: "views/common/content.html",
        }).state('admin.user', {
	        url: "/user",
	        templateUrl: "partials/user/index.html",
	        controller: UserListController
        }).state('admin.createuser', {
	        url: "/createuser",
	        templateUrl: "partials/user/create.html",
	        controller: UserAddController
        }).state('admin.edituser', {
	        url: "/edituser/:id",
	        templateUrl: "partials/user/edit.html",
	        controller: UserEditController
        }).state('admin.role', {
	        url: "/role",
	        templateUrl: "partials/role/index.html",
	        controller: RoleController
        }).state('profile', {
            abstract: true,
            url: "/profile",
            templateUrl: "views/common/content.html",
        }).state('profile.change', {
	        url: "/change",
	        templateUrl: "partials/profile/changeProfile.html",
	        controller: ProfileController
        }).state('profile.changePassword', {
	        url: "/changePassword",
	        templateUrl: "partials/profile/changePassword.html",
	        controller: ProfileController
        }).state('index.pharmacrawl', {
	        url: "/pharmacrawl",
	        templateUrl: "partials/pharmacrawl/index.html",
	        controller: PharmaCrawlController
        }).state('index.grouppharma', {
	        url: "/grouppharma",
	        templateUrl: "partials/grouppharma/index.html",
	        controller: GroupPharmaController
        }).state('index.detailpharmaingroup', {
	        url: "/detailpharmaingroup",
	        templateUrl: "partials/detailpharmaingroup/index.html",
	        controller: PharmaInGroupController
        });
	  	var adhsAppConfig = {
	  			useAuthTokenHeader : true 
	  	};
	    XMLHttpRequest.prototype = Object.getPrototypeOf(new XMLHttpRequest);
        $httpProvider.interceptors.push(function ($q, $rootScope, $cookieStore, $location) {
            return {
                'responseError': function (rejection) {
                    var status = rejection.status;

                    if (status === 401) {
                        $rootScope.go("index/login");
                    } else if (status === 400) {
                        $rootScope.error = 'error.';
                    } else if (status === 403) {
                        $rootScope.error = 'You have no rights to view this page.';
                    } else {
                        $rootScope.error = 'System error occurred. Please try again!'; //method + " on " + url + " failed with status " + status;
                    }

                    return $q.reject(rejection);
                }
            };
        }
    );

    /* Registers auth token interceptor, auth token is either passed by header or by query parameter
     * as soon as there is an authenticated user */
    $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
            return {
                'request': function (config) {
                    var isRestCall = config.url.indexOf('rest') === 0;
                    var authToken = $rootScope.getAuthToken();
                    if (isRestCall && authToken != null && authToken != undefined) {

                        if (adhsAppConfig.useAuthTokenHeader) {
                            config.headers['X-Auth-Token'] = authToken;
                        } else {
                            config.url = config.url + "?token=" + authToken;
                        }

                        //disable IE ajax request caching
                        if (config.method == 'GET') {
                            var separator = config.url.indexOf('?') === -1 ? '?' : '&';
                            config.url = config.url + separator + 'noCache=' + new Date().getTime();
                        }
                    }
                    return config || $q.when(config);
                }
            };
        }
    );   
}
var app=angular.module('claimstarApp');
app.config(config);
app.config(['$provide', function ($provide) {
    $provide.decorator('$locale', ['$delegate', function ($delegate) {
        $delegate.NUMBER_FORMATS.DECIMAL_SEP = ',';
        return $delegate;
    }]);
}]);
app.run(function ($rootScope, $location,$state, $cookieStore, $state,SecurityService,localize) {
        // lo
		$rootScope.translate = function(name){
			return localize.currentLocaleData[name];
		}
    	$rootScope.$state = $state;
        $rootScope.redirectToHttps = function () {
            if (window.location.protocol == "http:") {
                var restOfUrl = window.location.href.substr(5);
                window.location = "https:" + restOfUrl;
            }
        };
        $rootScope.countryList = ["Afghanistan","Albania","Algeria","Andorra","Angola","Anguilla","Antigua &amp; Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia","Bosnia &amp; Herzegovina","Botswana","Brazil","British Virgin Islands","Brunei","Bulgaria","Burkina Faso","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands","Chad","Chile","China","Colombia","Congo","Cook Islands","Costa Rica","Cote D Ivoire","Croatia","Cruise Ship","Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic","Ecuador","Egypt","El Salvador","Equatorial Guinea","Estonia","Ethiopia","Falkland Islands","Faroe Islands","Fiji","Finland","France","French Polynesia","French West Indies","Gabon","Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada","Guam","Guatemala","Guernsey","Guinea","Guinea Bissau","Guyana","Haiti","Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland","Isle of Man","Israel","Italy","Jamaica","Japan","Jersey","Jordan","Kazakhstan","Kenya","Kuwait","Kyrgyz Republic","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Macedonia","Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Mauritania","Mauritius","Mexico","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Mozambique","Namibia","Nepal","Netherlands","Netherlands Antilles","New Caledonia","New Zealand","Nicaragua","Niger","Nigeria","Norway","Oman","Pakistan","Palestine","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland","Portugal","Puerto Rico","Qatar","Reunion","Romania","Russia","Rwanda","Saint Pierre &amp; Miquelon","Samoa","San Marino","Satellite","Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Singapore","Slovakia","Slovenia","South Africa","South Korea","Spain","Sri Lanka","St Kitts &amp; Nevis","St Lucia","St Vincent","St. Lucia","Sudan","Suriname","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","Timor L'Este","Togo","Tonga","Trinidad &amp; Tobago","Tunisia","Turkey","Turkmenistan","Turks &amp; Caicos","Uganda","Ukraine","United Arab Emirates","United Kingdom","United States","United States Minor Outlying Islands","Uruguay","Uzbekistan","Venezuela","Vietnam","Virgin Islands (US)","Yemen","Zambia","Zimbabwe"];
        /*
         $rootScope.redirectToHttps();
         */
        /* Reset error when a new view is loaded */

        $rootScope.initActionMap = function () {
//          if ($rootScope.actionMap == undefined || $rootScope.actionMap == null) {
//              if ($rootScope.hasAnyRole(['ROLE_ADMIN'])) {
            $rootScope.actionMap = {
                'Add': 'Add',
                'Update': 'Update',
                'Delete': 'Delete',
                'Change Password': 'Change Password'
            };
//              } else {
//                  $rootScope.actionMap = { };
//              }
//          }

        };

        $rootScope.initObjectMap = function () {
//          if ($rootScope.objectMap == undefined || $rootScope.objectMap == null) {
//              if ($rootScope.hasAnyRole(['ROLE_ADMIN'])) {
            $rootScope.objectMap = {
                'Profile': 'Profile',
                'User': 'User',
                'Role': 'Role'
            };
//              } else {
//                  $rootScope.objectMap = { };
//              }
//          }

        };

        $rootScope.initSexMap = function () {
//          if ($rootScope.sexMap == undefined || $rootScope.sexMap == null) {
//              if ($rootScope.hasAnyRole(['ROLE_PHYSICIAN', 'ROLE_PATIENT', 'ROLE_OFFICER'])) {
            $rootScope.sexMap = {
                '1': 'M\xE4nnlich',
                '2': 'Weiblich'
            };
//              } else {
//                  $rootScope.sexMap = { };
//              }
//          }
        };

        $rootScope.getActionText = function (action) {
            $rootScope.initActionMap();
            return $rootScope.actionMap[action];
        };

        $rootScope.getObjectText = function (object) {
            $rootScope.initObjectMap();
            return $rootScope.objectMap[object];
        };

        $rootScope.getSexText = function (sex) {
            $rootScope.initSexMap();
            return $rootScope.sexMap[sex];
        };

        
        
        $rootScope.scrollToDiv = function (divId) {
            if (divId === undefined)
                return;
            var div = $(divId);
            if (div) {
                var pos = div.offset().top;
                if (pos != undefined) {
                    // animated top scrolling
                    $('body, html').animate({scrollTop: pos});
                }
            }
        };

        $rootScope.$on('$viewContentLoaded', function () {
            delete $rootScope.error;
            var url = $location.path();
            var authToken = $rootScope.getAuthToken();
          
            if (url.indexOf("/login") > -1) {
            }
            else if (authToken == null || authToken === undefined) {
            	$state.go("login", {}, {reload: true});
            }
        });


        $rootScope.hasRole = function (role) {

            if ($rootScope.user === undefined) {
                return false;
            }

            if ($rootScope.user.roles[role] === undefined) {
                return false;
            }

            return $rootScope.user.roles[role];
        };

        $rootScope.hasAnyRole = function (roles) {
            var hasRole = false;
            roles.forEach(function (role) {
                if ($rootScope.hasRole(role))
                    hasRole = true;
            });

            return hasRole;
        };

        $rootScope.isLogged = function () {

            if ($rootScope.user === undefined) {
                return false;
            }

            if ($rootScope.user.roles[role] === undefined) {
                return false;
            }

            return true;
        };

        


        $rootScope.logout = function () {
            delete $rootScope.user;
            delete $rootScope.center;
            $cookieStore.remove('Claimstar_Admiral_authToken');
            $('.breadcrumb').empty();
            $cookieStore.remove('isEmployee');
            $state.go("login", {}, {reload: true});
            localStorage.clear();
//                location.reload();

        };

        $rootScope.getAuthToken = function () {
            try {
                return $cookieStore.get('Claimstar_Admiral_authToken');
            } catch (e) {
                return null;
            }
        };

        $rootScope.go = function (path) {
            $location.path(path);
        };

        var originalPath = $location.path();

        var authToken = null;

        try {
            authToken = $rootScope.getAuthToken();
        } catch (e) {
        }
        if (authToken !== undefined && authToken != null) {

            SecurityService.get(function (user) {
                $rootScope.user = user;
                $rootScope.go(originalPath);

            });
        } else {
        	$state.go("login", {}, {reload: true});
        }

        $rootScope.initialized = true;
    });

app.controller('LangController', ['$scope', 'settings', 'localize','$cookieStore', function($scope, settings, localize,$cookieStore) {
	$scope.languages = settings.languages;
	$scope.currentLang = settings.currentLang;
//	
	$scope.setLang = function(lang) {
		settings.currentLang = lang;
		$scope.currentLang = lang;
		localize.setLang(lang);
		$cookieStore.put('localize_thienbinh', lang);
	}
	
//
	$scope.localizeThienbinh = $cookieStore.get('localize_thienbinh');	
	
	// set the default language
	if($scope.localizeThienbinh){
		$scope.currentLang = $scope.localizeThienbinh;
//		$scope.setLang($scope.currentLang);
	}
	else{
		var language = window.navigator.userLanguage || window.navigator.language;
		if(language && language =='de')
			$scope.currentLang = $scope.languages[0];
		else
			$scope.currentLang =$scope.languages[1];
	}
	$scope.setLang($scope.currentLang);
}]);
app.filter('unique', function () {

    return function (items, filterOn) {

        
        if (filterOn === false) {
            return items;
        }

        if ((filterOn || angular.isUndefined(filterOn)) && angular.isArray(items)) {
            var hashCheck = {}, newItems = [];

            var extractValueToCompare = function (item) {
                if (angular.isObject(item) && angular.isString(filterOn)) {
                    return item[filterOn];
                } else {
                    return item;
                }
            };

            angular.forEach(items, function (item) {
                var valueToCheck, isDuplicate = false;

                for (var i = 0; i < newItems.length; i++) {
                    if (angular.equals(extractValueToCompare(newItems[i]), extractValueToCompare(item))) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    newItems.push(item);
                }

            });
            items = newItems;
        }
        return items;
    };
});

