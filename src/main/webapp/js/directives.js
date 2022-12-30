

/**
 * INSPINIA - Responsive Admin Theme
 *
 */


/**
 * pageTitle - Directive for set Page title - mata title
 */
function pageTitle($rootScope, $timeout) {
    return {
        link: function(scope, element) {
            var listener = function(event, toState, toParams, fromState, fromParams) {
                // Default title - load on Dashboard 1
                var title = 'Pharma Dashboard';
                // Create your own title pattern
                if (toState.data && toState.data.pageTitle) title = 'Pharma Dashboard | ' + toState.data.pageTitle;
                $timeout(function() {
                    element.text(title);
                });
            };
            $rootScope.$on('$stateChangeStart', listener);
        }
    }
}

/**
 * sideNavigation - Directive for run metsiMenu on sidebar navigation
 */
function sideNavigation($timeout) {
    return {
        restrict: 'A',
        link: function(scope, element) {
            // Call the metsiMenu plugin and plug it to sidebar navigation
            $timeout(function(){
                element.metisMenu();
            });
        }
    };
}

/**
 * iboxTools - Directive for iBox tools elements in right corner of ibox
 */
function iboxTools($timeout) {
    return {
        restrict: 'A',
        scope: true,
        templateUrl: 'views/common/ibox_tools.html',
        controller: function ($scope, $element) {
            // Function for collapse ibox
            $scope.showhide = function () {
                var ibox = $element.closest('div.ibox');
                var icon = $element.find('i:first');
                var content = ibox.find('div.ibox-content');
                content.slideToggle(200);
                // Toggle icon from up to down
                icon.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
                ibox.toggleClass('').toggleClass('border-bottom');
                $timeout(function () {
                    ibox.resize();
                    ibox.find('[id^=map-]').resize();
                }, 50);
            },
                // Function for close ibox
                $scope.closebox = function () {
                    var ibox = $element.closest('div.ibox');
                    ibox.remove();
                }
        }
    };
}

/**
 * iboxTools with full screen - Directive for iBox tools elements in right corner of ibox with full screen option
 */
function iboxToolsFullScreen($timeout) {
    return {
        restrict: 'A',
        scope: true,
        templateUrl: 'views/common/ibox_tools_full_screen.html',
        controller: function ($scope, $element) {
            // Function for collapse ibox
            $scope.showhide = function () {
                var ibox = $element.closest('div.ibox');
                var icon = $element.find('i:first');
                var content = ibox.find('div.ibox-content');
                content.slideToggle(200);
                // Toggle icon from up to down
                icon.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
                ibox.toggleClass('').toggleClass('border-bottom');
                $timeout(function () {
                    ibox.resize();
                    ibox.find('[id^=map-]').resize();
                }, 50);
            };
            // Function for close ibox
            $scope.closebox = function () {
                var ibox = $element.closest('div.ibox');
                ibox.remove();
            };
            // Function for full screen
            $scope.fullscreen = function () {
                var ibox = $element.closest('div.ibox');
                var button = $element.find('i.fa-expand');
                $('body').toggleClass('fullscreen-ibox-mode');
                button.toggleClass('fa-expand').toggleClass('fa-compress');
                ibox.toggleClass('fullscreen');
                setTimeout(function() {
                    $(window).trigger('resize');
                }, 100);
            }
        }
    };
}

/**
 * minimalizaSidebar - Directive for minimalize sidebar
*/
function minimalizaSidebar($timeout) {
    return {
        restrict: 'A',
        template: '<a class="navbar-minimalize minimalize-styl-2 btn btn-primary btn-custom" href="" ng-click="minimalize()"><i class="fa fa-bars"></i></a>',
        controller: function ($scope, $element) {
            $scope.minimalize = function () {
                $("body").toggleClass("mini-navbar");
                if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
                    // Hide menu in order to smoothly turn on when maximize menu
                    $('#side-menu').hide();
                    // For smoothly turn on menu
                    setTimeout(
                        function () {
                            $('#side-menu').fadeIn(400);
                        }, 200);
                } else if ($('body').hasClass('fixed-sidebar')){
                    $('#side-menu').hide();
                    setTimeout(
                        function () {
                            $('#side-menu').fadeIn(400);
                        }, 100);
                } else {
                    // Remove all inline style from jquery fadeIn function to reset menu state
                    $('#side-menu').removeAttr('style');
                }
            }
        }
    };
}


/**
 *
 * Pass all functions into module
 */
app.directive('pageTitle', pageTitle)
    .directive('sideNavigation', sideNavigation)
    .directive('iboxTools', iboxTools)
    .directive('minimalizaSidebar', minimalizaSidebar)
    .directive('iboxToolsFullScreen', iboxToolsFullScreen);
app.directive('pwCheck', [function () {
    return {
        require: 'ngModel',
        link: function (scope, elem, attrs, ctrl) {
            var firstPassword = '#' + attrs.pwCheck;
            elem.add(firstPassword).on('keyup', function () {
                scope.$apply(function () {
                    var v = elem.val() === $(firstPassword).val();
                    ctrl.$setValidity('pwmatch', v);
                });
            });
        }
    }
}]).directive('pwChangeCheck', [function () {
    return {
        require: 'ngModel',
        link: function (scope, elem, attrs, ctrl) {
            var currentPassword = '#' + attrs.pwChangeCheck;
            elem.add(currentPassword).on('keyup', function () {
                scope.$apply(function () {
                    var v = elem.val() != $(currentPassword).val();
                    ctrl.$setValidity('pwChangeMatch', v);
                });
            });
        }
    }
}])
app.directive('ngConfirmClick', ['SweetAlert',
    function (SweetAlert) {
        return {
            link: function (scope, element, attr) {
            	 var clickAction = attr.confirmedClick;
            	 var msg = attr.ngConfirmClick || "Are	you sure want to delete this data?";
            	 var deleteBt = attr.ngTextButtonDelete || "Ja";
            	 element.bind('click', function (event) {
	            	SweetAlert.swal({
	    		        title: '',
	    		        text: msg,
	    		        type: "warning",
	    		        showCancelButton: true,
	    		        confirmButtonColor: "#DD6B55",
	    		        confirmButtonText: deleteBt,
	    		        cancelButtonText: "Nein",
	    		        closeOnConfirm: true,
	    		        closeOnCancel: true },
	    		    function (isConfirm) {
	    		        if (isConfirm) {
	    		        	scope.$eval(clickAction);
//	    		            SweetAlert.swal("Deleted!", "Your imaginary file has been deleted.", "success");
	    		        } else {
//	    		            SweetAlert.swal("Cancelled", "Your imaginary file is safe :)", "error");
	    		        }
	    		    });
            	 })
//                var msg = attr.ngConfirmClick || "Are you sure?";
//                var clickAction = attr.confirmedClick;
//                element.bind('click', function (event) {
//                    if (window.confirm(msg)) {
//                        scope.$eval(clickAction);
//                    }
//                });
            }
        };
    }]);

app.directive('smartFloat', function ($filter) {
    var FLOAT_REGEXP_1 = /^\$?\d+(.\d{3})*(\,\d*)?$/; //Numbers like: 1.123,56
    var FLOAT_REGEXP_2 = /^\$?\d+(,\d{3})*(\.\d*)?$/; //Numbers like: 1,123.56
    return {
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue) {
                if (FLOAT_REGEXP_1.test(viewValue)) {
                    ctrl.$setValidity('float', true);
                    return parseFloat(viewValue.replace('.', '').replace(',', '.'));
                } else if (FLOAT_REGEXP_2.test(viewValue)) {
                    ctrl.$setValidity('float', true);
                    return parseFloat(viewValue.replace(',', ''));
                } else {
                    ctrl.$setValidity('float', false);
                    return undefined;
                }
            });
            ctrl.$formatters.unshift(
                function (modelValue) {
                    if (typeof modelValue === "undefined" || modelValue.length === 0)
                        return '';

                    //console.log('parsing',modelValue);
                    //console.log('parsing',parseFloat(modelValue));
                    //console.log('parsing',$filter('number')(parseFloat(modelValue) , 2));
                    //return $filter('number')(parseFloat(modelValue), 2);

                    var currencySymbol = '';
                    var decimalSeparator = ',';
                    var thousandsSeparator = '.';
                    var decimalDigits = 2;

                    if (decimalDigits < 0) decimalDigits = 0;

                    // Format the input number through the number filter
                    // The resulting number will have "," as a thousands separator
                    // and "." as a decimal separator.
                    var formattedNumber = $filter('number')(parseFloat(modelValue), 2);

                    // Extract the integral and the decimal parts
                    var numberParts = formattedNumber.split(".");

                    // Replace the "," symbol in the integral part
                    // with the specified thousands separator.
                    numberParts[0] = numberParts[0].split(",").join(thousandsSeparator);

                    // Compose the final result
                    var result = currencySymbol + numberParts[0];

                    if (numberParts.length == 2) {
                        result += decimalSeparator + numberParts[1];
                    }

                    return result;
                }
            );
        }
    };
});

angular.module('filters', []).filter("customCurrency", function (numberFilter) {
    function isNumeric(value) {
        return (!isNaN(parseFloat(value)) && isFinite(value));
    }

    return function (inputNumber, currencySymbol, decimalSeparator, thousandsSeparator, decimalDigits) {
        if (isNumeric(inputNumber)) {
            // Default values for the optional arguments
            currencySymbol = (typeof currencySymbol === "undefined") ? "&euro;" : currencySymbol;
            decimalSeparator = (typeof decimalSeparator === "undefined") ? "." : decimalSeparator;
            thousandsSeparator = (typeof thousandsSeparator === "undefined") ? "," : thousandsSeparator;
            decimalDigits = (typeof decimalDigits === "undefined" || !isNumeric(decimalDigits)) ? 2 : decimalDigits;

            if (decimalDigits < 0) decimalDigits = 0;

            // Format the input number through the number filter
            // The resulting number will have "," as a thousands separator
            // and "." as a decimal separator.
            var formattedNumber = numberFilter(inputNumber, decimalDigits);

            // Extract the integral and the decimal parts
            var numberParts = formattedNumber.split(".");

            // Replace the "," symbol in the integral part
            // with the specified thousands separator.
            numberParts[0] = numberParts[0].split(",").join(thousandsSeparator);

            // Compose the final result
            var result = currencySymbol + numberParts[0];

            if (numberParts.length == 2) {
                result += decimalSeparator + numberParts[1];
            }

            return result;
        }
        else {
            return inputNumber;
        }
    };
});
app.directive('datetimepicker', function ($timeout) {
    var format = 'DD/MM/YYYY HH:mm:ss';
    return {
        // Restrict it to be an attribute in this case
        restrict: 'AE',
        // optionally hook-in to ngModel's API
        require: '?ngModel',
        // responsible for registering DOM listeners as well as updating the DOM
        link: function ($scope, element, $attrs, ngModel) {
            var $element;
            $timeout(function () {
                var locale = ($attrs.locale && $attrs.locale.length > 0) ? $attrs.locale : 'de';
                var dateformat = ($attrs.format && $attrs.format.length > 0) ? $attrs.format : format;
                var minDate = ($attrs.mindate && $attrs.mindate.length > 0) ? moment(Number($attrs.mindate)).format() : 0;
                var maxDate = ($attrs.maxdate && $attrs.maxdate.length > 0) ? moment(Number($attrs.maxdate)).format() : moment().add(50, 'years');
         
	            $element = $(element).find("input").datetimepicker({
	                    format: dateformat,
	                    locale: locale,
	                    minDate : minDate,
	                    maxDate : maxDate
	            });
              
        
                var DateTimePicker = $element.data("DateTimePicker");
                
                DateTimePicker.setValueAngular = function (newValue) {
                    this.angularSetValue = true; // a lock object to prevent calling change trigger of input to fix the re-cursive call of changing values
                    this.date(newValue);
                    this.angularSetValue = false;
                }

                if (!ngModel) {
                    return;
                }//below this we interact with ngModel's controller

                $scope.$watch($attrs['ngModel'], function (newValue) {
                    if (newValue)
                        if (newValue != "Invalid date") {
                         	if(moment(newValue).format(dateformat) == "Invalid date")
                         		DateTimePicker.setValueAngular(newValue);
                        	else
                        		DateTimePicker.setValueAngular(moment(newValue).format(dateformat));
                         	
                        }
                    if(newValue == '')
                    	$element[0].value = '';
                });

                ngModel.$formatters.push(function (value) {
                    // formatting the value to be shown to the user
                    //var format = DateTimePicker.format;
                    var date = moment(value);
                    if (date.isValid()) {
                        return date.format(format);
                    }
                    return '';
                });

                ngModel.$parsers.push(function toModel(input) {
                    // format user input to be used in code (converting to unix epoch or ...)
                    var modifiedInput = moment(input,dateformat).format();
                    if (modifiedInput == "Invalid date")
                        modifiedInput = input;
                    return modifiedInput;
                });

                //update ngModel when UI changes
                $element.on('dp.change', function (e) {
                    if (DateTimePicker.angularSetValue === true)
                        return;

                    var newValue = $element[0].value;
                    if (newValue !== ngModel.$viewValue)
                        $scope.$apply(function () {
                            ngModel.$setViewValue(newValue);
                        });
                    //bootstrapvalidator support
                    if ($element.attr('data-bv-field') !== undefined) // if the field had validation
                        $element.closest("form").bootstrapValidator('revalidateField', $element);

                });
            });
        }
    };
}).directive('timepicker', function ($timeout) {
    var format = 'HH:mm:ss';
    return {
        // Restrict it to be an attribute in this case
        restrict: 'AE',
        // optionally hook-in to ngModel's API
        require: '?ngModel',
        // responsible for registering DOM listeners as well as updating the DOM
        link: function ($scope, element, $attrs, ngModel) {
            var $element;
            $timeout(function () {
                var locale = ($attrs.locale && $attrs.locale.length > 0) ? $attrs.locale : 'de';
                var dateformat = ($attrs.format && $attrs.format.length > 0) ? $attrs.format : format;
                var minDate = ($attrs.mindate && $attrs.mindate.length > 0) ? moment(Number($attrs.mindate)).format() : 0;
                var maxDate = ($attrs.maxdate && $attrs.maxdate.length > 0) ? moment(Number($attrs.maxdate)).format() : moment().add(50, 'years');
         
	            $element = $(element).find("input").datetimepicker({
	                    format: dateformat,
	                    locale: locale,
	                    minDate : minDate,
	                    maxDate : maxDate
	            });
              
        
                var DateTimePicker = $element.data("DateTimePicker");
                
                DateTimePicker.setValueAngular = function (newValue) {
                    this.angularSetValue = true; // a lock object to prevent calling change trigger of input to fix the re-cursive call of changing values
                    this.date(newValue);
                    this.angularSetValue = false;
                }

                if (!ngModel) {
                    return;
                }//below this we interact with ngModel's controller

                $scope.$watch($attrs['ngModel'], function (newValue) {
                    if (newValue)
                        if (newValue != "Invalid date") {
                         	if(moment(newValue).format(dateformat) == "Invalid date")
                         		DateTimePicker.setValueAngular(newValue);
                        	else
                        		DateTimePicker.setValueAngular(moment(newValue).format(dateformat));
                         	
                        }
                    if(newValue == '')
                    	$element[0].value = '';
                });

                ngModel.$formatters.push(function (value) {
                    // formatting the value to be shown to the user
                    //var format = DateTimePicker.format;
                    var date = moment(value);
                    if (date.isValid()) {
                        return date.format(format);
                    }
                    return '';
                });

                ngModel.$parsers.push(function toModel(input) {
                    // format user input to be used in code (converting to unix epoch or ...)
//                    var modifiedInput = moment(input,dateformat).format();
//                    if (modifiedInput == "Invalid date")
                        modifiedInput = input;
                    return modifiedInput;
                });

                //update ngModel when UI changes
                $element.on('dp.change', function (e) {
                    if (DateTimePicker.angularSetValue === true)
                        return;

                    var newValue = $element[0].value;
                    if (newValue !== ngModel.$viewValue)
                        $scope.$apply(function () {
                            ngModel.$setViewValue(newValue);
                        });
                    //bootstrapvalidator support
                    if ($element.attr('data-bv-field') !== undefined) // if the field had validation
                        $element.closest("form").bootstrapValidator('revalidateField', $element);

                });
            });
        }
    };
});

//directives for localization
angular.module('app.localize', [])

	.factory('localize', ['$http', '$rootScope', '$window', function($http, $rootScope, $window){
		var localize = {
			currentLocaleData: {},
			currentLang: {},
			setLang: function(lang) {
				$http({method: 'GET', url: localize.getLangUrl(lang), cache: false})
				.success(function(data) {
					localize.currentLocaleData = data;
					localize.currentLang = lang;
					$rootScope.$broadcast('localizeLanguageChanged');
				}).error(function(data) {
					console.log('Error updating language!');
				})
			},
			getLangUrl: function(lang) {
				return 'js/langs/' + lang.langCode + '.js';
			},

			localizeText: function(sourceText) {
				return localize.currentLocaleData[sourceText];
			}
		};

		return localize;
	}])

	.directive('localize', ['localize', function(localize) {
		return {
			restrict: 'A',
			scope: {
				sourceText: '@localize'
			},
			link: function(scope, element, attrs) {
				scope.$on('localizeLanguageChanged', function() {
					var localizedText = localize.localizeText(scope.sourceText);
					if (element.is('input, textarea')) element.attr('placeholder', localizedText)
					else element.text(localizedText);
				});
			}
		}
	}]).directive('myEnter', function () {
	    return function (scope, element, attrs) {
	        element.bind("keydown keypress", function (event) {
	            if(event.which === 13) {
	                scope.$apply(function (){
	                    scope.$eval(attrs.myEnter);
	                });

	                event.preventDefault();
	            }
	        });
	    };
	}).directive('loadFile', function() {
		return  function(scope, element, attrs) {
				var	file = attrs.fileData;
				 if ( file.type.indexOf('image') > -1) {
		                var fileReader = new FileReader();
		                fileReader.readAsDataURL(file);
		                var loadFile = function (fileReader, index) {
		                    fileReader.onload = function (e) {
		                        	 element.attr('src', e.target.result);
		                    };
		                }(fileReader, i);
		            }
			
			}
		}).directive('renderDistance', function() {
		return  function(scope, element, attrs) {
				scope.$eval(attrs.renderDistance);
			}
		}).directive('uppercased', function() {
		    return {
		        require: 'ngModel',
		        link: function(scope, element, attrs, modelCtrl) {
		            modelCtrl.$parsers.push(function(input) {
		                return input ? input.toUpperCase() : "";
		            });
		            element.css("text-transform","uppercase");
		        }
		    };
		}).directive('onlyDigits', function () {

			    return {
			        restrict: 'A',
			        require: '?ngModel',
			        link: function (scope, element, attrs, modelCtrl) {
			            modelCtrl.$parsers.push(function (inputValue) {
			                if (inputValue == undefined) return '';
			                var transformedInput = inputValue.replace(/[^0-9\\.]+/g, '');
			                if (transformedInput !== inputValue) {
			                    modelCtrl.$setViewValue(transformedInput);
			                    modelCtrl.$render();
			                }
			                return transformedInput;
			            });
			        }
			    };
			}).directive('disallowSpaces', function() {
			    return {
			        restrict: 'A',

			        link: function($scope, $element) {
			            $element.bind('keydown', function(e) {
			                if (e.which === 32) {
			                    e.preventDefault();
			                }
			            });
			        }
			    }
			}).directive('inputRestrictor', [function () {
			    return {
			        restrict: 'A',
			        require: 'ngModel',
			        link: function (scope, element, attr, ngModelCtrl) {
			            var pattern = /[^0-9A-Za-z &-]*/g;
			            function fromUser(text) {
			                if (!text)
			                    return text;

			                var transformedInput = text.replace(pattern, '');
			                if (transformedInput !== text) {
			                    ngModelCtrl.$setViewValue(transformedInput);
			                    ngModelCtrl.$render();
			                }
			                return transformedInput;
			            }
			            ngModelCtrl.$parsers.push(fromUser);
			        }
			    };
			}]);
;
;