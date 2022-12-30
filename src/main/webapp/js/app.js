/**
 * INSPINIA - Responsive Admin Theme
 *
 */
(function () {
    angular.module('claimstarApp', [
        'ui.router',                    // Routing
        'oc.lazyLoad',    
        'ngCookies',
        'claimstarApp.services',
        'ngRoute',
        'ngDialog',
        'ngTable',
        'trNgGrid',
        'ngFileUpload',
        'app.localize',
        'oitozero.ngSweetAlert','angucomplete-alt','rwdImageMaps','ui.sortable'
    ]);
    
   

    var services = angular.module('claimstarApp.services', ['ngResource']);
    services.factory('settings', ['$rootScope', function($rootScope){
		// supported languages
		
    	var settings = {
    			languages: [
    				{
    					language: 'English',
    					translation: 'English',
    					langCode: 'en',
    					flagCode: 'us'
    				}
    			],
    			
    		};

		return settings;
		
	}]);
    services.factory('LangFactory', [ 'settings', 'localize','$cookieStore', function(settings, localize,$cookieStore) {
    	var langFactory = {};
    	langFactory.setLang = function(lang) {
    		settings.currentLang = lang;
    		localize.setLang(lang);
    		$cookieStore.put('localize_thienbinh', lang);
    	}
    	langFactory.setDefauLanguge = function(){
    		  var localizeThienbinh = $cookieStore.get('localize_thienbinh');	
    		  var languages = settings.languages;
    		  var currentLang = settings.currentLang;
    	    	// set the default language
    	    	if(localizeThienbinh)
    	    		langFactory.setLang(localizeThienbinh);
    	    	else{
    	    		var language = window.navigator.userLanguage || window.navigator.language;
//    	    		if(language && language =='de')
    	    			langFactory.setLang(languages[0]);
//    	    		else
//    	    			langFactory.setLang(languages[1]);
    	    	}
    	}
    	return langFactory;
    }]);
    // user service
    services.factory('SecurityService', function ($resource) {

        return $resource(
            'rest/security/:action/:id', // ws url
            {id: '@id'}, //parameters default
            {
                // authenticate function
                authenticate: {
                    method: 'POST',
                    params: {'action': 'authenticate'},
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }
            }
        );
    });

    //user service
    services.factory('UserManagementService', function ($resource) { 

        return $resource(
            'rest/manage/user/:action/:id', // ws url
            {id: '@id'}, //parameters default
            {

                list: {
                    method: 'GET',
                    params: {'action': 'list'},
                    isArray: false
                },
                delete: {
                    method: 'DELETE',
                    params: {}
                },
                create: {
                    method: 'POST'
                },
                read: {
                    method: 'GET'
                },
                update: {
                    method: 'POST',
                    params: {id: '@id'}
                }
                /*
                 list:function(pageIndex, pageSize){

                 }
                 */
            }
        );
    });

    //RoleService
    services.factory('RoleManagementService', function ($resource) {

        return $resource(
            'rest/manage/role/:action/:id', // ws url
            {}, //parameters default
            {

                list: {
                    method: 'GET',
                    params: {'action': 'list'},
                    isArray: false
                },

                listManagementRoles: {
                    method: 'GET',
                    params: {'action': 'listManagementRoles'},
                    isArray: true
                }
            }
        );
    });

    // ActionLogService
    services.factory('ActionLogService', function ($resource) {

        return $resource(
            'rest/actionlog/:action/:id', // ws url
            {}, //parameters default
            {
                // list action log

                list: {
                    method: 'POST',
                    params: {},
                    isArray: false
                },
                update: {
                    method: 'POST'
                },
                listActions: {
                    method: 'GET',
                    params: {'action': 'listActions'},
                    isArray: true
                },
                listObjects: {
                    method: 'GET',
                    params: {'action': 'listObjects'},
                    isArray: true
                }
            }
        );
    });

    //ProfileService
    services.factory('ProfileService', function ($resource) {

        return $resource(
            'rest/profile/:action', // ws url
            {}, //parameters default
            {

                get: {
                    method: 'GET'
                },
                update: {
                    method: 'POST'
                },
                changePassword: {
                    method: 'POST',
                    params: {'action': 'changePassword'}
                }
            }
        );
    });



    services.factory('CrawlersService', function ($resource) {

        return $resource(
            'rest/crawlers/:action', // ws url
            {}, //parameters default
            {
            	footballList: {
                    method: 'GET',
                    params: {'action': 'footballList'},
                    isArray: true
                },
                tenisList: {
                    method: 'GET',
                    params: {'action': 'tenisList'},
                    isArray: true
                }

            }
        );
    });
    services.factory('TeamsService', function ($resource) { 

        return $resource(
            'rest/teams/:action', // ws url
            {}, //parameters default
            {

                list: {
                    method: 'GET',
                    params: {'action': 'list'},
                    isArray: false
                },
                delete: {
                    method: 'DELETE',
                    params: {}
                },
                create: {
                    method: 'POST',
                    params: {'action': 'create'}
                },
                update: {
                    method: 'POST',
                    params: {'action': 'update'}
                }
            }
        );
    });
    services.factory('ForebetService', function ($resource) {

        return $resource(
            'rest/forebet/:action', // ws url
            {}, //parameters default
            {
            	getDataToday: {
                    method: 'GET',
                    params: {'action': 'getDataToday'},
                    isArray: false
                }

            }
        );
    });
    //Pharma Crawl Service
    services.factory('PharmaCrawlService', function ($resource) {

        return $resource(
            'rest/pharmacrawl/:action', // ws url
            {}, //parameters default
            {
            	list: {
                    method: 'GET',
                    params: {'action': 'list'},
                    isArray: false
                },getAllPharmaSetting: {
                    method: 'GET',
                    params: {'action': 'getAllPharmaSetting'},
                    isArray: false
                },pharmaDetail: {
                    method: 'POST',
                    params: {'action': 'pharmaDetail'},
                    isArray: false
                },exportPharmaDetail: {
                    method: 'POST',
                    params: {'action': 'exportPharmaDetail'},
                    isArray: false
                },getPharmaSettingById: {
                    method: 'GET',
                    params: {'action': 'getPharmaSettingById'},
                    isArray: false
                },getAllParsehubSetting: {
                    method: 'GET',
                    params: {'action': 'getAllParsehubSetting'},
                    isArray: false
                }
                
            }
        );
    });
    //Product Crawl Service
    services.factory('ProductPharmaService', function ($resource) {

        return $resource(
            'rest/productpharma/:action', // ws url
            {}, //parameters default
            {
            	list: {
                    method: 'GET',
                    params: {'action': 'list'},
                    isArray: false
                },
                getAllProduct: {
                    method: 'GET',
                    params: {'action': 'getAllProduct'},
                    isArray: false
                },
                getProductByGroup: {
                    method: 'GET',
                    params: {'action': 'getProductByGroup'},
                    isArray: false
                }

            }
        );
    });
    //Session service
    services.factory('SessionService', function () {

        var sessionService = {};

        sessionService.storage = {};

        sessionService.saveItem = function (key, value) {
            sessionService.storage[key] = value;
        };

        sessionService.deleteItem = function (key) {
            sessionService.storage[key] = null;
        };

        sessionService.getItem = function (key) {
            return sessionService.storage[key];
        };

        return sessionService;
    });
    services.factory('GroupPharmaService', function ($resource) { 

        return $resource(
            'rest/grouppharma/:action', // ws url
            {}, //parameters default
            {

                list: {
                    method: 'GET',
                    params: {'action': 'list'},
                    isArray: false
                },
                create: {
                    method: 'POST',
                    params: {'action': 'create'}
                },detailGroupPharma: {
                    method: 'GET',
                    params: {'action': 'detailGroupPharma'},
                    isArray: false
                },update: {
                    method: 'POST',
                    params: {'action': 'update'}
                },updateDeleted: {
                    method: 'GET',
                    params: {'action': 'updateDeleted'},
                    isArray: false
                },getAllGroupPharma: {
                    method: 'GET',
                    params: {'action': 'getAllGroupPharma'},
                    isArray: false
                }
                
            }
        );
    });

    //Utility service
    services.factory('UtilityService', function (ngDialog,SweetAlert) {

        var utilityService = {};

        // assume only 1 file
        utilityService.isAllowedUploadImageType = function (files) {
            if (files != null && files != undefined && files.length > 0) {
                var fileType = files[0].type.toLowerCase();
                return ("image/png" == fileType || "image/jpeg" == fileType || "image/jpg" == fileType);
            }

            return true;
        };

        //assume only 1 file
        utilityService.isAllowedUploadImageSize = function (files) {
            if (files != null && files != undefined && files.length > 0) {
                var fileSize = files[0].size;
                return (5024000 >= fileSize);
            }

            return true;
        };

        utilityService.safeParseInt = function (value) {
            try {
                return parseInt(value);
            } catch (e) {
                return value;
            }

        };

        utilityService.notificationDilog = function (message) {
            if (!message || message.length == 0)
                message = "Erfolg!";
            toastr.options = {
          		  "closeButton": true,
          		  "debug": false,
          		  "newestOnTop": false,
          		  "progressBar": true,
          		  "positionClass": "toast-top-right",
          		  "preventDuplicates": false,
          		  "onclick": null,
          		  "showDuration": "300",
          		  "hideDuration": "1000",
          		  "timeOut": "5000",
          		  "extendedTimeOut": "1000",
          		  "showEasing": "swing",
          		  "hideEasing": "linear",
          		  "showMethod": "fadeIn",
          		  "hideMethod": "fadeOut"
          		};
            toastr.success(message);
           
            
            
//            var dialog = ngDialog.open({
//                template: '<p class="notificationColor">' + message + '</p>',
//                className: 'ngdialog-theme-default customDialogNotifitation',
//                plain: true,
//                closeByDocument: true,
//                closeByEscape: true
//            });
//            setTimeout(function () {
//                dialog.close();
//            }, 3000);
        }

        utilityService.messageErrorDilog = function (message) {
            if (!message || message.length == 0)
                message = "Es ist ein Fehler aufgetreten. Bitte versuchen Sie es spÃ¤ter noch einmal!";
            toastr.options = {
            		  "closeButton": true,
            		  "debug": false,
            		  "newestOnTop": false,
            		  "progressBar": true,
            		  "positionClass": "toast-top-right",
            		  "preventDuplicates": false,
            		  "onclick": null,
            		  "showDuration": "300",
            		  "hideDuration": "1000",
            		  "timeOut": "5000",
            		  "extendedTimeOut": "1000",
            		  "showEasing": "swing",
            		  "hideEasing": "linear",
            		  "showMethod": "fadeIn",
            		  "hideMethod": "fadeOut"
            		};
              toastr.error(message); 
        }
        utilityService.alertError = function (title,text,button) {
      	  SweetAlert.swal({
		        title: title,
		        text: text,
		        type: 'error',
		        showCancelButton: false,
		        confirmButtonColor: "#00427f",
		        confirmButtonText: button,
		        closeOnConfirm: true,
		        closeOnCancel: true },
		    function (isConfirm) {
		        if (isConfirm) {
		        	
		        } else {
		        }
		    });  
        }
        utilityService.alertWarning = function (title,text,button) {
        	  SweetAlert.swal({
  		        title: title,
  		        text: text,
  		        type: 'warning',
  		        showCancelButton: false,
  		        confirmButtonColor: "#00427f",
  		        confirmButtonText: button
  		        }
        	 );  
          }
        utilityService.scrollToDiv = function(divId){
    		if (divId===undefined)
    			return;
    		var div = $(divId);
    		if (div){
    			var pos = div.offset().top - 120;
    			if (pos != undefined){
    				// animated top scrolling
    				$('body, html').animate({scrollTop: pos},500);
    			}
			}
		};
        return utilityService;
    });


})();

