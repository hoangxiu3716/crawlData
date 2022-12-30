function WebAssistantController($scope, $location,$route,CustomerService,ClaimDirectService,ZipCoordinatesService,WerkstattnetzService,AssistantService,TrickeryScoreService,UtilityService,$timeout,$state, $log,ngTableParams ,Upload,$rootScope,ngDialog,$q,$window,SweetAlert,$cookieStore) {
		$scope.buildInfo = function(item){
			$scope.info = {
				 insured :  item ? item.insured : null,
				 policyNumber : item ? item.policyNumber : '',
				 licencePlateNumber :  item ? item.licencePlateNumber : '',
				 typeOfDamage :'',
				 
				 strangeVehicle : false,
				 strangeVehicleName :'',
				 strangeVehicleLicenseNumber : '',
				 strangeVehiclePhone : '',
				 
				 dayOfAccident : '',
				 dayOfAccidentUnknow : false,
				 timeOfAccident :'',
				 street :'',
				 postCode : '',
				 city : '',
				 
				 vehicleReady : 1,
				 towingService : 0,
				 whatHappened : '',
				 accidentSigns : '',
				 injuredAnotherPassenger : 0,
				 name:'',
				 mark : '',
				 phone : '',
				 whoDriving : '',
				 nameDriving : '',
				 validDrivingLicense : 1,
				 influence : 0,
				 witnessesAvailable : 0,
				 policeInformed : 0,
				 department : '',
				 fileNumber : '',
				 injuredPeople : 0,
				 people : [],
				 namePersion : '',
				 
				 manufacturerId : null,
				 manufacturer : null,
				 modelId : null,
				 model : null,
				 damageArea : '',
				 damageItems : {},
				 
		 		 kmVehicle : '',
		 		 rateDamage : '',
		 		 rateDamageUnknow : false,
		 		 damageLevel : '',
		 		 vehicleRepaired : 0,
		 		 moneyOfTheRepair : 0,
		 		 accountOwner : '',
		 		 iban : '',
		 		 bic : '',
		 		 nearPostCode : '',
		 		 werkstattnetz : null,
		 		 onlyBranded :false,
		 		 workshop : false,
		 		 urlZipFile : '',
		 		 
		 		witnessesAvailables : [],
		 		
		 		sms : false,
		 		smsValue: '',
		 		email : false,
		 		emailValue : '',
		 		whatsApp : false ,
		 		whatsAppValue : '',
		 		skype :false,
		 		skypeValue :'',
		 		faceTime : false,
		 		faceTimeValue :'',
		 		googleDuo : false,
		 		googleDuoValue : '',
		 		googleHangouts : false,
		 		googleHangoutsValue : '',
		 		phoneContacts : false,
		 		phoneContactsValue :'',
		 		monday : false,
		 		tuesday : false,
		 		wednesday : false,
		 		thursday : false,
		 		friday : false,
		 		saturday : false,
		 		fromTime :'',
		 		toTime :'',
		 		
		 		
		 		stolen : false,
				stolenItem :{} ,
				otherStolen : "",
				beenDamaged : true,
				
				culprit : false,
				culpritName : '',
				describeYourDamage : '',
				describeYourDamageItem :{},
				
				vehicleDamaged : '',
				vehicleDamagedItem : '',
				vehicleParked :false,
				
				whatBeenDamaged : '',
				whatBeenDamagedItem: {},
				repairAlready : false,
				
				damageEarlierTimes : false,
				
				foresterInformed : false,
				foresterInformedName : '',
				forestryService : '',
				wildCasualtyCertificate : false,
				
				
				// haftpflicht
				forename : '',
				licencePlateNumberInjuredPerson: '',
				mileage : '',
				
				// nein 
				characteristicsYourCar : '',
				land : 'Germany',
				insuranceNumber : '',
				nameOffender : '',
				forenameOffender : '',
				streetOffender : '',
				cityOffender : '',
				postCodeOffender : '',
				
				animal : '',
				collisionAnimal : false,
				
				noInfoInjured : false,
				
				claimType : '',
				whatHappenedOthers : '',
				accidentSignsOthers : '',
				
				environment :'',
				trickery : true,
				valueDamageComponent : null,
				scoreNumberZahlschaden : true
		 }
		}
		$scope.showAreaWitnessesAvailable = true;
		$scope.showAreaPerson = true;
		$scope.buildInfo();
	 $scope.environmentDatas = ["Autobahn","Land-/Bundesstraße","Innerorts","Gewerbegebiet","Parkplatz","Privatgelände","Sonstiges"];
	 $scope.animalDatas = ["Reh","Wildschwein","Fuchs",	"Hirsch","Kaninchen","Hund","Marder","Waschbär","Sonstiges"];
	 $scope.whatBeenDamagedDatas = ["Windschutzscheibe", "Heckscheibe", "Seitenscheibe", "Scheinwerfer","Nebelscheinwerfer", "Spiegelglas", "Rückleuchte", "Sonstiges"];
	 $scope.vehicleDamagedDatas = ["Äste", "Baum","Dachziegel", "Herumfliegende Gegenstände", "Sonstiges"];
	 $scope.describeYourDamageDatas=["Lackbeschädigung durch Kratzer","Eindellung an der Karosserie","Scheiben beschädigt","Antenne beschädigt","Außenspiegel beschädigt","Scheibenwischer beschädigt","Sonstiges"];
	 $scope.stolenDatas = ["Navigationssystem", "Airbag", "Lenkrad", "Klimabedienteil", "Räder", "Radio/CD","Sonstiges"];
	 $scope.resetModel = function(){
		 if($scope.info.noInfoInjured){
			 $scope.info.model = null;
			 $scope.info.manufacturer = null;
			 $scope.info.manufacturerId = null;
			 $scope.models = [];
		 }
	 }
	 $scope.$watch('info.dayOfAccidentUnknow', function(newValue, oldValue) {
		  if(newValue){
			  $scope.info.dayOfAccident = '';
			  $scope.info.timeOfAccident = '';
		  }
	 }, true);
	 $scope.$watch('info.dayOfAccident', function(newValue, oldValue) {
		  if(newValue){
			  $scope.info.dayOfAccidentUnknow = false;
		  }
	 }, true);
	 $scope.$watch('info.timeOfAccident', function(newValue, oldValue) {
		  if(newValue){
			  $scope.info.dayOfAccidentUnknow = false;
		  }
	 }, true);
	 $scope.$watch('info.rateDamageUnknow', function(newValue, oldValue) {
		  if(newValue){
			  $scope.info.rateDamage = '';
		  }
	 }, true);
	 $scope.$watch('info.rateDamage', function(newValue, oldValue) {
		  if(newValue){
			  $scope.info.rateDamageUnknow = false;
		  }
	 }, true);
	 $scope.$watch('info.fromTime', function(newValue, oldValue) {
		 $scope.timeInvalid = $scope.validateTime(true);
		  if(!$scope.timeInvalid){
			  document.getElementById("fromTime").value=$scope.info.toTime; 
		  	  $scope.info.fromTime = $scope.info.toTime;
		  }
	 }, true);
	 $scope.$watch('info.toTime', function(newValue, oldValue) {
		 $scope.timeInvalid = $scope.validateTime();
		  if(!$scope.timeInvalid){
			  document.getElementById("toTime").value=$scope.info.fromTime; 
			  $scope.info.toTime = $scope.info.fromTime;
		  }
	 }, true);

	 $scope.validateTime = function(fromTime){
			if($scope.info.toTime && $scope.info.fromTime){
				var sts = $scope.info.fromTime.split(":");
	            var ets = $scope.info.toTime.split(":");
	            var stMin = (parseInt(sts[0]) * 60 + parseInt(sts[1]));
	            var etMin = (parseInt(ets[0]) * 60 + parseInt(ets[1]));
	            if(etMin < stMin) {
	            	return  false;
	            }
			}

			return true;
	 };
	 $scope.changeToTime = function(){
			if($scope.info.fromTime){
			var sts = $scope.info.fromTime.split(":");
			$scope.info.toTime = (parseInt(sts[0]) +1 ) + ":" + parseInt(sts[1]);
			document.getElementById("toTime").value=$scope.info.toTime; 
		}
	 }
	 $scope.showAreaOfBeenDamaged = false;
	 $scope.$watch('info.beenDamaged', function(newValue, oldValue) {
		 if($scope.info.typeOfDamage =='Einbruch / Diebstahl' && !$scope.info.beenDamaged)
			 $scope.showAreaOfBeenDamaged = true;
		 else{
			 $scope.showAreaOfBeenDamaged = false;
			 if($scope.fzgFabrikats)
				 $scope.getAllFzgFabrikat();
		 }
			 
	 }, true);
	 $scope.damageLevels = ["ohne Beleg","Kostenvoranschlag","Rechnung","Gutachten"];
	 $scope.newdate = new Date().getTime();
	 $scope.dataWhatHappens = ["Auffahrunfall","Ausweichkollision","Frontalzusammenstoß","Frontschaden","Heckschaden","Kollision","Parkplatzschaden/Delle","Seitenkollision","Streifschaden","Vorfahrtsverletzung","Sonstiges (Freitext)"];
	 $scope.dataWhatHappensOthers = ["Blitz","Brand","Kabelschmorschaden","Lawine","Marderbiss","Überschwemmung","Überspannung","Sonstiges (Freitext)"];
	 $scope.inProcess = 1;
	 $scope.changeProcess = function(value){
		 if(value)
			 $scope.inProcess = value;
		 else
			 $scope.inProcess ++ ;
	 }
	 $scope.showHideElement = function(divId,next){
		 if(divId == '#uploadFileAreaContent'  && !next && $scope.hideHagel)
			 divId ='#detailsVehicleAreaContent';
		 if(divId == '#uploadFileAreaContent'  && next && $scope.hideHagel)
			divId ='#contactsAreaContent';
		 if($scope.unfall.onlyForeignCar && divId == '#detailsVehicleAreaContent' && next )
			 divId ='#uploadFileAreaContent';
		 if($scope.unfall.onlyForeignCar && divId == '#detailsVehicleAreaContent' && !next )
			 divId ='#infoVehicleAreaContent';	
		 if($scope.unfall.onlyForeignCar && divId == '#contactsAreaContent'  )
			 divId ='#reportAreaContent';
		 var state = 'none';
		 if($(divId).css('display') == 'none')
			 state ='block';
		 $scope.closeAllDiv();
		 $(divId).css('display',state) ;
		 if(state == 'block'){
			 UtilityService.scrollToDiv(divId);
			 $(divId+"State").css('display','none') ;
		 }else
			 $(divId+"State").css('display','block') ;
	 }
	 $scope.checkStateElement = function(divId){
		 return $(divId).css('display');
	 }
	 $scope.closeAllDiv = function(){
		 $('#accidentNeinAreaContent').css('display','none') ;
		 
		 $('#foreigncarAreaContent').css('display','none') ;
		 $('#accidentAreaContent').css('display','none') ;
		 $('#infoVehicleAreaContent').css('display','none') ;
		 $('#detailsVehicleAreaContent').css('display','none') ;
		 $('#uploadFileAreaContent').css('display','none') ;
		 $('#contactsAreaContent').css('display','none') ;
		 $('#foreigncarAreaContentState').css('display','block') ;
		 $('#accidentAreaContentState').css('display','block') ;
		 $('#infoVehicleAreaContentState').css('display','block') ;
		 $('#detailsVehicleAreaContentState').css('display','block') ;
		 $('#uploadFileAreaContentState').css('display','block') ;
		 $('#contactsAreaContentState').css('display','block') ;
		 
	 }
	 $scope.dataWhoDrivings = ["Versicherungsnehmer","Sonstiges (Name)"];
	 $scope.customer = null ;
	 $scope.login = function(){
		   $rootScope.error = '';
		   if(!$scope.info.policyNumber || !$scope.info.licencePlateNumber)
			   return;
		   CustomerService.login({
			   policyNumber:  $scope.info.policyNumber,
			   plateNumber:  $scope.info.licencePlateNumber.replace(/ |-/g, "")
           }).$promise.then(function (data) {
        	   if (!data || !data.id) {
        		   UtilityService.messageErrorDilog("Login fehlgeschlagen. Bitte überprüfen Sie Ihre eingegebenen Daten.");
            	   return;
               }
        	   UtilityService.notificationDilog("Herzlich Willkommen in Ihrem Schadenportal der AdmiralDirekt.de");
        	   $rootScope.needReload = true;
               $scope.customer = data;
               $scope.changeProcess(3);
               $scope.buildInfoLogin($scope.customer);
               UtilityService.scrollToDiv("#inProcess3");

	         }, function (error) {
	             if (error.errorCode != undefined && error.errorCode != null)
	                 $rootScope.error = error.errorMessage;
	         });
		   
	    };
	    
	    $scope.buildInfoLogin = function(customer){
	    	$scope.buttonTypeOfDamages = [];
	    	if(!customer.vkSb && !customer.tkSb){
	    		$scope.messageLogin = "Ihr Versicherungsumfang beinhaltet folgenden Schutz: Haftpflicht";
	    		$scope.buttonTypeOfDamages = ["Haftpflicht"];
	    	}
	    	if(!customer.vkSb && customer.tkSb){
	    		$scope.messageLogin = "Ihr Versicherungsumfang beinhaltet folgenden Schutz: Haftpflicht und Teilkasko.";
	    		$scope.buttonTypeOfDamages = ["Unfall","Einbruch / Diebstahl","Glas","Sturm","Hagel","Wildunfall","Sonstiges"];
	    	}
	    	if(customer.vkSb && !customer.tkSb){
	    		$scope.messageLogin = "Ihr Versicherungsumfang beinhaltet folgenden Schutz: Haftpflicht und Vollkasko.";
	    		$scope.buttonTypeOfDamages = ["Haftpflicht"];
	    	}
	    	if(customer.vkSb && customer.tkSb){
	    		$scope.messageLogin = "Ihr Versicherungsumfang beinhaltet folgenden Schutz: Haftpflicht, Teilkasko und Vollkasko.";
	    		$scope.buttonTypeOfDamages = ["Unfall","Einbruch / Diebstahl","Vandalismus","Glas","Sturm","Hagel","Wildunfall","Sonstiges"];
	    		
	    	}
	    	$scope.info.nearPostCode = customer.zip;
	    }
	   
	    $scope.buildStatusDefault = function(){ 
	    	$scope.hideGlas = false;
	 	    $scope.hideHagel = false;
		    $scope.unfall = {
		    		onlyOwnCar : false,
		    		onlyForeignCar : false,
		    		both : false
		    }
		    $scope.info.typeOfDamage ='';
	    }
	    $scope.chooseNein = function(){
	    	$scope.buildStatusDefault();
	    	$scope.nextToNeinArea();
	    }
	    $scope.buildStatusDefault();
	    $scope.buildClaimType = function(item){
	    	$scope.info.claimType ="";
	    	if(!$scope.customer.vkSb && !$scope.customer.tkSb){
	    		$scope.info.claimType+="KFZ-Haftpflichtschaden";
	    	}
	    	if(!$scope.customer.vkSb && $scope.customer.tkSb){
	    		$scope.info.claimType+="KFZ-Teilkaskoschaden";
	    	}
	    	if($scope.customer.vkSb && !$scope.customer.tkSb){
	    		$scope.info.claimType+="KFZ-Vollkaskoschaden";
	    	}
	    	if($scope.customer.vkSb && $scope.customer.tkSb){
	    		$scope.info.claimType+="KFZ-Vollkaskoschaden";
	    	}
	    }
	    $scope.chooseTypeOfDamage = function(item){
	    	$scope.buildStatusDefault();
	    	$scope.info.typeOfDamage = item;
	    	$scope.info.nearPostCode = $scope.customer.zip;
	    	$scope.buildClaimType();
	    	if(item == 'Unfall'){
	    		var htmlOwnCar = "";
	    		if($scope.customer && !$scope.customer.vkSb && $scope.customer.tkSb)
	    			htmlOwnCar = '';
	    		else
	    			htmlOwnCar = '<div class="control-group col-sm-6 no-padding-left-right"><label class="display-block control control--checkbox"><input type="checkbox"  ng-model="unfall.ownCar" id="ownCarId" /><i></i> eigenes	Fahrzeug<div class="control__indicator"></div></label></div>';
	    			
            	SweetAlert.swal({
    		        title: 'Was	wurde	beschädigt?',
    		        text: '  <div class="col-sm-12 no-padding-left-right">'+htmlOwnCar+'<div class="control-group col-sm-6 no-padding-left-right"><label class="display-block control control--checkbox"><input type="checkbox"  ng-model="unfall.foreignCar" id="foreignCarId"/><i></i> fremdes	Fahrzeug<div class="control__indicator"></div> </label></div></div>',
    		        html: true,
    		        imageUrl: "images/question.png",
    		        showCancelButton: true,
    		        confirmButtonColor: "#00427f",
    		        confirmButtonText: "Weiter",
    		        cancelButtonText: "Abbrechen",
    		        closeOnConfirm: true,
    		        closeOnCancel: true },
    		    function (isConfirm) {
    		        if (isConfirm) {
    		        	var foreignCar = document.getElementById("foreignCarId") ?  document.getElementById("foreignCarId").checked : false;
    		        	var ownCar = document.getElementById("ownCarId") ? document.getElementById("ownCarId").checked : false;
    		        	if(ownCar && foreignCar){
    		        		$scope.unfall.both = true; 
    		        		$scope.info.injuredAnotherPassenger = true;
    		        		$scope.info.claimType+= " - Unfall - Eigenschaden + Fremdschaden";
    		        	}
    		        	if(ownCar && !foreignCar){
    		        		$scope.unfall.onlyOwnCar = true;
    		        		$scope.info.claimType+= " - Unfall - Eigenschaden";
    		        	}
    		        	if(!ownCar && foreignCar){
    		        		$scope.unfall.onlyForeignCar = true;
    		        		$scope.info.claimType+= " - Unfall - Fremdschaden";
    		        		$scope.nextToForeignCarArea();
    		        		return;
    		        	}
    		        	if(!ownCar && !foreignCar)
    		        		return;
    		        	
    		        	$scope.nextToAccidentArea();
//    		        		$scope.info.strangeVehicle= true;
    		        } else {
//    		        	$scope.nextToAccidentArea();
    		        }
    		    });
	    	}else{
	    		if(item == 'Haftpflicht'){
	    			$scope.info.claimType+= " - Unfall - Fremdschaden";
	    			$scope.unfall.onlyForeignCar = true;
	    			$scope.nextToForeignCarArea();
	    			return;
	    		}
	    		$scope.nextToAccidentArea();
	    		if(item == 'Glas')
	    			$scope.hideGlas = true;
	    		if(item == 'Hagel')
	    			$scope.hideHagel = true;
	    		
	    		$scope.info.claimType+= " - " + item;
	    	}
	    	
	    }
	    $scope.nextToNeinArea = function(){
	    	
	    	$scope.changeProcess(3.5);
//	    	if($scope.inProcess > 3.1){
//	    		$scope.closeAllDiv();
//	    	}
	    	$scope.closeAllDiv();
	    	$scope.showHideElement('#accidentNeinAreaContent');
	    	UtilityService.scrollToDiv("#accidentNeinArea");
	    }
	    $scope.nextToForeignCarArea = function(){
	    	
	    	$scope.changeProcess(3.5);
//	    	if($scope.inProcess > 3.1){
//	    		$scope.closeAllDiv();
//	    	}
	    	$scope.closeAllDiv();
//	    	$scope.showHideElement('#foreigncarAreaContent');
//	    	UtilityService.scrollToDiv("#foreigncarArea");
	    }
	    $scope.nextToAccidentArea = function(caseNein){
    		if(caseNein && (!$scope.info.dayOfAccident) && !$scope.info.dayOfAccidentUnknow){
				UtilityService.alertError("Fehlende	Daten","Bitte wählen Sie einen Schadentag aus.","OK");
				return;
			}
	    	$scope.changeProcess(4);
	    	$scope.closeAllDiv();
	    	$scope.showHideElement('#accidentAreaContent');
	    	UtilityService.scrollToDiv("#accidentArea");
	    }
	    
	    $scope.person ={namePerson : ""};
	    $scope.people = [];
		$scope.addPerson = function (){
			if($scope.person.namePerson && $scope.person.namePerson != ''  )
				$scope.people.push($scope.person.namePerson);
			$scope.person.namePerson = '';
		};
		$scope.removePerson = function(index){
			$scope.people.splice(index,1);
		};
	   
	 
	    $scope.witnessesAvailable = {
	    		name : "",
	    		street : '',
	    		postCode : '',
	    		city : '',
	    		phone : ''};
	    $scope.witnessesAvailables = [];
		$scope.addWitnessesAvailable = function (){
			if($scope.witnessesAvailable && $scope.witnessesAvailable.name   )
				$scope.witnessesAvailables.push($scope.witnessesAvailable);
			$scope.witnessesAvailable = {
		    		name : "",
		    		street : '',
		    		postCode : '',
		    		city : '',
		    		phone : ''};
			$scope.$broadcast('angucomplete-alt:clearInput', 'witnessesAvailablePLZ');
		};
		 $scope.selectedWitnessesAvailablesPlz = function(selected) {
		        if (selected){ 
		        	$scope.witnessesAvailable.postCode=selected.originalObject.zcZip;
		        	$scope.witnessesAvailable.city=selected.originalObject.zcLocationName;
		        }
		 }
		$scope.removeWitnessesAvailable = function(index){
			$scope.witnessesAvailables.splice(index,1);
		};		
		
		$scope.toInfoVehicle = function(){
			if($scope.showAreaOfBeenDamaged || $scope.hideGlas || $scope.hideHagel){
				$scope.nextToDetailsVehicle();
				return;
			}
			 $scope.changeProcess(5);
	    	
	    	 $scope.showHideElement('#infoVehicleAreaContent');
	    	 setTimeout(function(){ 
	    		 UtilityService.scrollToDiv("#infoVehicleArea"); 
	    		
	    	}, 500);
	    	 $scope.getAllFzgFabrikat();
		}

		$scope.nextToInfoVehicle = function(){
			if($scope.unfall.both && $scope.bothArea){
				
			}
			if(!$scope.info.typeOfDamage && (!$scope.info.name || !$scope.info.forename)){
					UtilityService.alertError("Fehlende	Daten","Bitte geben Sie Ihren Namen sowie Vornamen ein.","OK");
					return;
			}
			if((!$scope.info.dayOfAccident) && !$scope.info.dayOfAccidentUnknow ){
				UtilityService.alertError("Fehlende	Daten","Bitte wählen Sie einen Schadentag aus.","OK");
				return;
			}
			if($scope.unfall.both && $scope.info.injuredAnotherPassenger){
				if(!$scope.info.name || !$scope.info.mark ){
//					UtilityService.alertWarning("Fehlende	Daten","Bitte geben Sie den Namen sowie das Kennzeichen des geschädigten Verkehrsteilnehmers an.","OK");
					SweetAlert.swal({
				        title: 'Fehlende Daten',
				        text: "Bitte geben Sie den Namen sowie das Kennzeichen des geschädigten Verkehrsteilnehmers an..",
				        type: "warning",
				        showCancelButton: true,
				        confirmButtonColor: "#00427f",
				        confirmButtonText: "Überspringen",
				        cancelButtonText: "Ergänzen",
				        closeOnConfirm: true,
				        closeOnCancel: true },
				    function (isConfirm) {
				        if (!isConfirm)
				        	return;
				        else
				        	$scope.toInfoVehicle();
				    });
				}else{
					$scope.toInfoVehicle();
				}
			}else{
				$scope.toInfoVehicle();
			}	
			
		};
		$scope.nextToDetailsVehicle = function(){
			if($scope.unfall.onlyForeignCar || $scope.info.typeOfDamage =='Haftpflicht'){
				$scope.nextToUploadFile(); 
				return;
			}
			 $scope.changeProcess(6);
			 $scope.showHideElement('#detailsVehicleAreaContent');
	    	 setTimeout(function(){ 
	    		 UtilityService.scrollToDiv("#detailsVehicleArea"); 
	    		
	    	}, 500);
	    	 
		};
		
		$scope.nextToUploadFile = function(){
			if(!$scope.info.typeOfDamage && (!$scope.info.licencePlateNumberInjuredPerson || $scope.showMileage == 2 )){
				UtilityService.alertError("Fehlende	Daten","Bitte geben Sie	das	Kennzeichen	des	Schädigers ein.","OK");
				return;
			}
			var checkIbanAndBic = false;
			 if($("#checkIbanArea").css('display') && ($("#checkIbanArea").css('display') != 'none'))
				 checkIbanAndBic = true;
			 if($scope.unfall.onlyForeignCar || $scope.info.vehicleRepaired || ($scope.info.typeOfDamage =='Einbruch / Diebstahl' && !$scope.info.beenDamaged) || $scope.info.typeOfDamage =='Haftpflicht')
				 checkIbanAndBic = false;
			if(checkIbanAndBic  && ($scope.ibanValidate || !$scope.info.iban) ){
				UtilityService.alertError("Bitte geben Sie zum Fortfahren eine gültige IBAN ein.","Die aus 22 alpha-numerischen Zeichen bestehende IBAN-Nummer finden Sie auf der Rückseite Ihrer Bankkarte. Sie beginnt mit DE…","OK");
				return;
			}
			if(checkIbanAndBic   && ($scope.bicValidate || !$scope.info.bic)){
				UtilityService.alertError("Bitte geben Sie zum Fortfahren eine gültige BIC ein.","Die aus 11 alpha-numerischen Zeichen bestehende BIC-Nummer finden Sie auf der Rückseite Ihrer Bankkarte.","OK");
				return;	
			}
			if($scope.hideHagel){
				$scope.nextToContacts();
				return;
			}
			
			 $scope.changeProcess(7);
			 $scope.showHideElement('#uploadFileAreaContent');
	    	 setTimeout(function(){ 
	    		 UtilityService.scrollToDiv("#uploadFileArea"); 
	    		 
	    	}, 500);
	    	 
		};
		$scope.nextToContacts = function(){
			if($scope.unfall.onlyForeignCar){
				$scope.nextToReport(); 
				return;
			}
			 $scope.changeProcess(8);
			 $scope.showHideElement('#contactsAreaContent');
	    	 setTimeout(function(){ 
	    		 UtilityService.scrollToDiv("#contactsArea"); 
	    		
	    	}, 500);
	    	 
		};
		$scope.nextToReport = function(){
			 $scope.changeProcess(9);
			 $scope.showHideElement('#reportAreaContent');
	    	 setTimeout(function(){ 
	    		 UtilityService.scrollToDiv("#reportArea"); 
	    		
	    	}, 500);
	    	 
		};
		$scope.models = [];
		$scope.fzgFabrikats = [];
		$scope.bauteils = ['Dach','Front','Glas','Heck','Innenraum','Seite','Achshälfte vorne','Achshälfte hinten'];
		$scope.getAllFzgFabrikat = function(){
			if(!$scope.customer)
				return;
			ClaimDirectService.getAllFzgFabrikat().$promise.then(function (data) {
					$scope.fzgFabrikats = data;
					if($scope.customer.brand){
					$scope.fzgFabrikats.some(function(item) {
						if(item.name){
							if($scope.customer.brand.toLowerCase().indexOf(item.name.toLowerCase()) > -1){
								$scope.info.manufacturer = item;
								$scope.changeManufacturer();
							}
						    return $scope.customer.brand.toLowerCase().indexOf(item.name.toLowerCase()) > -1;
						}
					  });
					};
		         }, function (error) {
		             if (error.errorCode != undefined && error.errorCode != null)
		                 $rootScope.error = error.errorMessage;
		         });
			
		};
		//test need remove when deploy
//		$scope.getAllFzgFabrikat();	
		$scope.changeManufacturer = function(){
			$scope.models = [];
			ClaimDirectService.findByClaimDirectFzgFabrikat({directFzgFabrikatId :  $scope.info.manufacturer.id}).$promise.then(function (data) {
				$scope.models = data;
				if($scope.info.manufacturer.name =='Sonstiges' && $scope.models)
					$scope.info.model = $scope.models[0];
	         }, function (error) {
	             if (error.errorCode != undefined && error.errorCode != null)
	                 $rootScope.error = error.errorMessage;
	         });
		};
		
		$scope.components = [];
		$scope.changeDamageArea = function(value){
			if(value)
				$scope.info.damageArea = value;
			ClaimDirectService.findBauteilByAnsicht({ansicht :  $scope.info.damageArea}).$promise.then(function (data) {
				$scope.components = data;
	         }, function (error) {
	             if (error.errorCode != undefined && error.errorCode != null)
	                 $rootScope.error = error.errorMessage;
	         });
			
		};
		$scope.removeDamageArea = function(key){
			$scope.info.damageItems[key] = false;
		};
		
		$scope.selectedPlz = function(selected) {
	        if (selected){ 
	        		$scope.info.postCode=selected.originalObject.zcZip;
	        		$scope.info.city=selected.originalObject.zcLocationName;
	        }
		}
		 $scope.selectedPlzOffender = function(selected) {
		        if (selected){ 
		        		$scope.info.postCodeOffender=selected.originalObject.zcZip;
		        		$scope.info.cityOffender=selected.originalObject.zcLocationName;
		        }
		 }
		 $scope.autoFillOrt = function (value) {
				var filter = {zcZip : value}
				var defer = $q.defer();
				ZipCoordinatesService.findByZcZipStartingWith({
		            size: 50,
		            filter: filter
		        }).$promise.then(function (pageData) {
		        		defer.resolve(pageData);             
		            }, function (errResponse) {
		            });
		        return defer.promise;
		    };

		
		    $scope.selectedFiles = [];
		    $scope.newFileInfo = function(){
			    $scope.fileInfo ={
			    		file : null,
			    		image : "",
			    		type : "",
			    		size : '',
			    		name : '',
			    		fileType : '',
			    		typeOfNein : ''
			    }
		    }
		    $scope.fileReaderSupported = window.FileReader != null && (window.FileAPI == null || FileAPI.html5 != false);
		    $scope.indexOfFile = {
		    		image : 0,
		    		document : 0
		    };
		    $scope.onFileSelect = function ($files) {
			        if (!$files) {
			            return;
			        }
	
			        for (var i = 0; i < $files.length; i++) {
			        	
			        	   var $file = $files[i];
			        	   if ($scope.fileReaderSupported && $file.type.indexOf('image') > -1) {
				                var fileReader = new FileReader();
				                fileReader.fileName = $file.name;
				                fileReader.oldFile = $file
				                fileReader.readAsDataURL($files[i]);
				                var loadFile = function (fileReader, index) {
				                    fileReader.onloadend = function (e) {
				                        $timeout(function () {
				                        	$scope.newFileInfo();
				                        	$scope.fileInfo.fileType = e.target.oldFile.type;
				                        	$scope.fileInfo.type ="image"; 
				                            $scope.fileInfo.name = e.target.fileName;
				     			            $scope.fileInfo.size = bytesToSize(e.total);
				     			            $scope.fileInfo.image = e.target.result; 
				     			            $scope.fileInfo.file = e.target.oldFile; 
				     			            $scope.selectedFiles.push($scope.fileInfo);
				     			            $scope.indexOfFile.image ++;
				                        });
				                    };
				                }(fileReader, i);
			        	   }else{
			        		   $scope.newFileInfo();
	                            $scope.fileInfo.name = $file.name;
	     			            $scope.fileInfo.size = bytesToSize($file.size);
	     			            $scope.fileInfo.file = $file;
	     			            $scope.selectedFiles.push($scope.fileInfo);
	     			            $scope.indexOfFile.document ++;
			        	   }
			        }
			
				};    
				$scope.fileIdChosed = {};
				$scope.calculatorFile = function(){
					$scope.indexOfFile = {
				    		image : 0,
				    		document : 0
				    };
					for(item in $scope.fileIdChosed){
						if($scope.fileIdChosed[item] == 'image')
							$scope.indexOfFile.image ++;
						else
							$scope.indexOfFile.document ++;
					}
				}

				$scope.onFileSelectOnly = function ($files,id,type) {
			        if (!$files) {
			            return;
			        }
			        	   var $file = $files[0];
			        	   $scope.newFileInfo();
			        	   var hasfile = false;
			        	   for(var i=0;i < $scope.selectedFiles.length ;i++){
			        		   if($scope.selectedFiles[i].typeOfNein == type){
			        			   $scope.selectedFiles[i].file = $file;
			        			   $scope.selectedFiles[i].name = $file.name;
						           $scope.selectedFiles[i].size = bytesToSize($file.size);
						           $scope.selectedFiles[i].fileType = $file.type;
			        			   hasfile = true;
			        		   }
			        	   }
			        	   if(!hasfile){
				        	   $scope.fileInfo.typeOfNein = type;
	                           $scope.fileInfo.name = $file.name;
					           $scope.fileInfo.size = bytesToSize($file.size);
					           $scope.fileInfo.file = $file;
					           $scope.fileInfo.fileType = $file.type;
					           $scope.selectedFiles.push($scope.fileInfo);
			        	   }
			        	   if ($scope.fileReaderSupported && $file.type.indexOf('image') > -1) {
				                var fileReader = new FileReader();
				                fileReader.readAsDataURL($file);
				                var loadFile = function (fileReader, index) {
				                    fileReader.onloadend = function (e) {
				                        $timeout(function () {
				                            $(id).attr("src", e.target.result);
				                            if(id == '#image-360'){
				                            	$('#image-360').css("display","inline-block");
				                            	$('#video-360').css("display","none");
				                            }
				                            $scope.fileIdChosed[id] = 'image';
				                            $scope.calculatorFile();
				                        });
				                    };
				                }(fileReader);
			        	   }else{
			        		   $scope.fileIdChosed[id] = 'document';
			        		   $scope.calculatorFile();
			        		   if(id == '#image-360' && $scope.fileReaderSupported && $file.type.indexOf('video') > -1){
			        			   $scope.buildPlayVideo($files);
			        			   return;
			        		   }
			        			   
			        		   if(id == '#image-file-7')
			        			   $(id).attr("src", "images/image-file-7-selected.png");
			        		   else
			        			   $(id).attr("src", "images/image-file-selected.png");
			        	   }
			
				};    
				
				 $scope.buildPlayVideo = function($files) {

		     		  var URL = $window.URL || $window.webkitURL;

		               var file = $files[0];
		               if(!file)
		            	   return;
		               var type = file.type;
		  
		               var videoNode = document.querySelector('#video-360');
		               var canPlay = videoNode.canPlayType(type);
		  
		               canPlay = (canPlay === '' ? 'no' : canPlay);
		  
		               var message = 'Trinh duyet khong ho tro "' + type + '": ' + canPlay;
		  
		               var isError = canPlay === 'no';
		               if(isError)
		            	   displayMessage(message, isError);
		  
		               if (isError) {
		                   return;
		               }
		               	
		               var fileURL = URL.createObjectURL(file);
		  
		               videoNode.src = fileURL; 

		               var source = document.createElement('source');
		               source.src = fileURL;
		               source.type = type;
		               videoNode.appendChild(source); 
		               $('#video-360').css("display","inline-block");
				       $('#image-360').css("display","none");
			         if (!URL) {
			         displayMessage('Your browser is not ' + 
			            '<a href="http://caniuse.com/bloburls">supported</a>!', true);
				         
			         
			         return;
			         }                

		 	    };		
		function bytesToSize(bytes) {
			var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
		    if (bytes == 0) return 'n/a';
		    var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
		    if (i == 0) return bytes + ' ' + sizes[i];
		    return (bytes / Math.pow(1024, i)).toFixed(1) + ' ' + sizes[i];
		};
		$scope.removeFile = function(index){
			if ($scope.fileReaderSupported && $scope.selectedFiles[index].type.indexOf('image') > -1) 
				$scope.indexOfFile.image --;
			else
				$scope.indexOfFile.document --;
			$scope.selectedFiles.splice(index,1);
			
		}
		
		$scope.uploadReportData = function () {
		 	if(!$scope.htmlText)
			 return;

	    	$scope.upload = Upload.upload({
	            url: 'rest/webassistant/uploadHtml',
	            method: 'POST',
	            fields: {
	            	folder : $scope.folder,
	            	htmlText : $scope.htmlText
	            }
	        });
	        $scope.showCustomLoading = true;
	        $scope.upload.then(function (result) {
	            $scope.showCustomLoading = false;
	            if (result) 
	                if (result.errorMessage )
	                    $rootScope.error = result.errorMessage;
	                else {
	                    $rootScope.error = '';
	                    UtilityService.notificationDilog("Datei(en) erfolgreich hochgeladen.");
	                    $state.go("mainassistant.endpage");
	                }
	        }, function (error) {
	            $scope.showCustomLoading = false;
	            if (error.errorCode != undefined && error.errorCode != null)
	                $rootScope.error = error.errorMessage;
	        }, function (evt) {
	        });
	        $scope.upload.xhr(function (xhr) {
	        });

	    };
	    	
	    $scope.folder = "";
		 $scope.uploadAndZipFile = function (callback) {   	
			 	if(!$scope.selectedFiles || $scope.selectedFiles.length < 1)
				 return;
			 	var uploadData = [];
			 	var typeOfNein = [];
		    	for(var i = 0 ;i < $scope.selectedFiles.length ;i++){
		    		if($scope.selectedFiles[i].typeOfNein)
		    			typeOfNein.push($scope.selectedFiles[i].typeOfNein);
		    		uploadData.push($scope.selectedFiles[i].file);
		    	}
		    	
		    	$scope.upload = Upload.upload({
		            url: 'rest/webassistant/create',
		            method: 'POST',
		            fields: {
		            	policyNumber : $scope.info.policyNumber,
		            	licencePlateNumber : $scope.info.licencePlateNumber.replace(/ |-/g, ""),
		            	typeOfNein : JSON.stringify(typeOfNein),
		            	folder : $scope.folder
		            },
		            file : uploadData
		        });
		        $scope.showCustomLoading = true;
		        $scope.upload.then(function (result) {
		            $scope.showCustomLoading = false;
		            if (result) 
		                if (result.errorMessage || !result.data)
		                    $rootScope.error = result.errorMessage;
		                else {
		                    $rootScope.error = '';
		                    $scope.info.urlZipFile = result.data.zipFileUrl;
		                    if(callback)
		                    	callback();
	                }
		        }, function (error) {
		            $scope.showCustomLoading = false;
		            if (error.errorCode != undefined && error.errorCode != null)
		                $rootScope.error = error.errorMessage;
		        }, function (evt) {
		        });
		        $scope.upload.xhr(function (xhr) {
		        });

		    };
	
		$scope.backtotop = function(){
			
			SweetAlert.swal({
		        title: 'Sind Sie sicher?',
		        text: "Möchten Sie wirklich zur Startseite zurückkehren? Alle eingegebenen Daten werden dadurch gelöscht.",
		        type: "info",
		        showCancelButton: true,
		        confirmButtonColor: "#d00000",
		        confirmButtonText: "Ja",
		        cancelButtonText: "Nein",
		        closeOnConfirm: true,
		        closeOnCancel: true },
		    function (isConfirm) {
		        if (isConfirm) {
		        	if(!$scope.info.insured)	
		        		$state.go($state.current.name, {}, {reload: true});
		        	$scope.changeProcess(3);
		        	$scope.closeAllDiv();
		        	$scope.buildInfo($scope.info);
		        	$scope.witnessesAvailables = [];
		        	$scope.clearDataWerkstattnetz();
		        	$scope.people = [];
		        	$scope.selectedFiles = [];
		        	$scope.indexOfFile = {
				    		image : 0,
				    		document : 0
				    };
		        } else {
		        }
		    });
			
		};
	    $scope.resetForm = function(){
	    	$state.go($state.current.name, {}, {reload: true});
	    };
	    $scope.changeDamageLevel = function(){
	    	if($scope.info.damageLevel == 'Rechnung')
	    		$scope.info.moneyOfTheRepair = false;
	    	else
	    		$scope.info.moneyOfTheRepair = true;
	    }
	    $scope.searchByPostCode = function(){
	    	if(!$scope.info.nearPostCode)
	    		return ;
	    	$scope.clearDataWerkstattnetz();
	    	$scope.showCustomLoading = true;
	    	var werkstatt = "";
	    	if($scope.info.onlyBranded)
	    		werkstatt = $scope.info.manufacturer.name;
	    	WerkstattnetzService.findLikePlz({plz :  $scope.info.nearPostCode,werkstatt : werkstatt}).$promise.then(function (data) {
				$scope.werkstattnetzs = data;
				$scope.showCustomLoading = false;
	         }, function (error) {
	        	 $scope.showCustomLoading = true;
	             if (error.errorCode != undefined && error.errorCode != null)
	                 $rootScope.error = error.errorMessage;
	         });
	    };
	    
	    $scope.clearDataWerkstattnetz = function(){
	    	$scope.werkstattnetzs = [];
	    	$scope.info.werkstattnetz = null;
	    	$scope.info.workshop = false;
	    	$scope.info.moneyOfTheRepair = false;
	    }
	    
	    $scope.htmlReport ;
	    $scope.htmlText = "";
	    $scope.reportScoreCard = function(){
	    	if(!$scope.info.rateDamage && $scope.info.valueDamageComponent && $scope.info.valueDamageComponent.average)
				 $scope.info.rateDamage = Math.round($scope.info.valueDamageComponent.average);
	    	$scope.folder = ("claimstar/"+new Date().getTime())
	    	var elId = "htmlSaveFile";
//	    	var style = "<style> label { display: inline-block; max-width: 100%; margin-bottom: 5px; font-weight: 700; } .ibox-content-with-line { padding: 0px 20px 40px 20px!important; } fieldset { min-width: 0; padding: 0; margin: 0; border: 0; } .title-color { color: #1e64b2; cursor: pointer; font-size: 18px; margin: 0px 0px 10px 0px!important; } .no-padding { padding: 0 !important; } .cursor-default { cursor: default!important; } .sub-title-color { color: #1e64b2; cursor: pointer; margin: 0!important; } .custom-customer-area { } .padding-10 { padding: 10px; } .border-area { border: 1px solid #ffffff; margin: 5px; } .font-weight-500 { font-weight: 500 !important; } .ng-hide:not(.ng-hide-animate) { display: none !important; } @media (min-width: 768px){ .col-sm-12 { width: 100%; margin: 5px; } .col-sm-4 { width: 33.33333333%; } .col-sm-8 { width: 66.66666667%; } .col-sm-1, .col-sm-10, .col-sm-11, .col-sm-12, .col-sm-2, .col-sm-3, .col-sm-4, .col-sm-5, .col-sm-6, .col-sm-7, .col-sm-8, .col-sm-9 { float: left; } } .table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td { padding-top: 8px; padding-right: 8px; padding-bottom: 8px; vertical-align: top; } </style>";
	    	var name ="";
	    	if($rootScope.user && ($rootScope.hasRole('ROLE_EMPLOYEES')|| $rootScope.hasRole('ROLE_AGENCY') || $rootScope.hasRole('ROLE_AGENT')) ){
	    		name = $rootScope.user.fullname;
	    	}else{
		    	if(!$scope.info.insured)
		    		name = "AST";
		    	else
		    		name = "VN";
	    	}	
	    	
	    	var melder = "";
	    	if(!$scope.info.insured)
	    		melder = "AST";
	    	else
	    		melder = "VN";
	    	var header = '<label class="control-label col-sm-12" for="name">Ausfüller:'+name+'</label> \\r\\n  ';	
	    	header += '<label class="control-label col-sm-12" for="name">Melder:'+melder+'</label> ';
	    	if($scope.info.insured)
	    		header += ' \\r\\n  ';
    	    var elHtml = document.getElementById(elId).innerHTML; 
//	    	var elHtml = $('#'+elId).html();
    	    elHtml =  header + elHtml;
    	    elHtml = elHtml.replace(/\r/g, "");
    		elHtml = elHtml.replace(/<section/g, "\\r\\n<section");
    		elHtml = elHtml.replace(/fieldset>/g, "fieldset>\\r\\n");
    		elHtml = elHtml.replace(/<\/tr>/g, "<\/tr>;");
    		elHtml = elHtml.replace(/<\/td>/g,'<\/td>,');
    		elHtml = elHtml.replace(/<[^>]*>/g, '');
    		elHtml = elHtml.replace(/{[^>]*}/g, '');
    		elHtml = elHtml.replace(/-->/g, '');
    		elHtml = elHtml.replace(/\s{2,}/g,' ');
    		elHtml = elHtml.replace(/\\r\\n0 &amp;&amp; info.witnessesAvailable == 1">/g,'');
    		elHtml = elHtml.replace(/\\r\\n0 &amp;&amp; info.injuredPeople == 1">/g,'');
    		elHtml = elHtml.replace(/\\r\\n /g,'\r\n');
    		elHtml = elHtml.replace(/\\t/g,'\t');
    		elHtml = elHtml.replace(/0 && info.witnessesAvailable == 1 >/g,'');
    		elHtml = elHtml.replace(/0 && info.witnessesAvailable == 1/g,'');
    		
    		elHtml = elHtml.replace(/0 && info.injuredPeople == 1/g,'');

    		elHtml = elHtml.replace('Name, Straße + Nr., PLZ, Ort, Telefon, , ;','');
			elHtml = elHtml.replace(' ,  ','');
			elHtml = elHtml.replace(/, , ;/g,';');
			elHtml = elHtml.replace(/;,/g,'');
			elHtml = elHtml.replace(/,;/g,'');
			elHtml = elHtml.replace(/, ;/g,'');
     		elHtml = elHtml.replace(/amp;/g, '');
     		$scope.htmlText = elHtml;
//    		var blob = new Blob([elHtml], {encoding:"UTF-8",type: "text/plain;charset=UTF-8"});
//    		$scope.htmlReport = new File([blob], "Schadenmeldung.txt", {type:"text/plain"});
//    		var link = document.createElement("a");
//    	    link.download = "Schadenmeldung.txt";
//    	    link.href = window.URL.createObjectURL($scope.htmlReport);
//    	    link.click();
	    	
    		if($scope.info.insured && $scope.info.typeOfDamage !='Haftpflicht'){
		    	if(!$scope.info.urlZipFile && $scope.selectedFiles && $scope.selectedFiles.length > 0)
		    		$scope.uploadAndZipFile($scope.calculatorAndEExport);
		    	else
		    		$scope.calculatorAndEExport();
    		}else{
    			if(!$scope.info.urlZipFile && $scope.selectedFiles && $scope.selectedFiles.length > 0)
		    		$scope.uploadAndZipFile($scope.uploadReportData);
		    	else
		    		$scope.uploadReportData();
    		}	
	    };
	    
	     $scope.calculatorDamageComponent = function(){
	    		 $scope.calculatorInfo={
	    				 damageComponents : [],
	    				 customerZipcode : '',
	    				 modelId : null
	    		 }
	    		 for(key  in $scope.info.damageItems){
		 	    		if($scope.info.damageItems[key])
		 	    			$scope.calculatorInfo.damageComponents.push(key);
		 	     }
	    		 
	    		 $scope.calculatorInfo.customerZipcode = $scope.customer.zip;
	    		 $scope.calculatorInfo.modelId =  $scope.info.model ? $scope.info.model.id: null;
	    		 if(!$scope.calculatorInfo.damageComponents || !$scope.calculatorInfo.damageComponents.length > 0 || !$scope.calculatorInfo.customerZipcode || !$scope.calculatorInfo.modelId){
	    			 $scope.info.valueDamageComponent = null;
	    			 return;
	    		 }
	    		 ClaimDirectService.calculatorDamageComponent($scope.calculatorInfo).$promise.then(function (data) {
//	    			 if($rootScope.hasRole('ROLE_EMPLOYEES')){
//	    		    		$scope.valueDamageComponent = data;
	    		    		$scope.info.valueDamageComponent = data;
//	    			 }
	    		    		$scope.showCustomLoading = false;
	    		  }, function (error) {
	    		        	 $scope.showCustomLoading = false;
	    		             if (error.errorCode != undefined && error.errorCode != null)
	    		                 $rootScope.error = error.errorMessage;
	    		 });
	     }
//	     $scope.TRICKERY = true;
	    $scope.calculatorAndEExport = function(){
	    	$scope.info.witnessesAvailables = $scope.witnessesAvailables;
	    	$scope.info.people = $scope.people;
	    	$scope.calculatorAccident = AssistantService.calculatorAccident($scope.info,$scope.customer,$scope.unfall);
	    	$scope.scoreTrickery = 0;
	    	if($scope.info.trickery){
	    		$scope.calculatorTrickery =  TrickeryScoreService.calculatorTrickery($scope.info,$scope.customer);
	    		$scope.scoreTrickery = $scope.calculatorTrickery.score;
	    		$scope.calculatorAccident.todoTextAccidents.push.apply($scope.calculatorAccident.todoTextAccidents,$scope.calculatorTrickery.todoTexts);
	    	}
	    	$scope.inputInfo = {
	    			scoreSchadeninformation : $scope.calculatorAccident.scoreSchadeninformation,
	    			scoreSchadenzusatzkosten : $scope.calculatorAccident.scoreSchadenzusatzkosten,
	    			scoreSchadenhergang : $scope.calculatorAccident.scoreSchadenhergang,
	    			scoreSchadensteuerung : $scope.calculatorAccident.scoreSchadensteuerung,
	    			scoreUploadedFile : $scope.calculatorAccident.scoreUploadedFile,
	    			scoreTrickery : $scope.scoreTrickery,
	    			todoTextAccidents : $scope.calculatorAccident.todoTextAccidents,
	    			zahlschaden : $scope.calculatorAccident.zahlschaden,
	    			needCalculatorZahlschadenInserver :  $scope.calculatorAccident.needCalculatorZahlschadenInserver,
	    			policyNumber : $scope.info.policyNumber,
	    			licencePlateNumber : $scope.info.licencePlateNumber,
	    			name : $scope.customer.firstName + " " + $scope.customer.lastName,
	    			mark : $scope.info.mark,
	    			rateDamage : $scope.info.rateDamage,
	    			damageLevel : $scope.info.damageLevel,
	    			damageComponents : [],
	    			customerZipcode : $scope.customer.zip,
	    			kmVehicle : $scope.info.kmVehicle,
	    			vehicleRepaired : $scope.info.vehicleRepaired,
	    			rateDamageUnknow : $scope.info.rateDamageUnknow,
	    			typeOfDamage : $scope.info.typeOfDamage,
	    			trickery : $scope.info.trickery,
	    			folder : $scope.folder 
	    	}
	    	if($scope.info.typeOfDamage == 'Unfall'){
	    		if($scope.unfall.both)
	        		$scope.inputInfo.typeOfDamage = "Unfall - Eigenschaden + Fremdschaden";
	        	if($scope.unfall.onlyOwnCar)
	        		$scope.inputInfo.typeOfDamage = "Unfall - Eigenschaden";
	        	if($scope.unfall.onlyForeignCar)
	        		$scope.inputInfo.typeOfDamage = "Unfall - Fremdschaden";
	    	}
//	    	$scope.folder = $scope.inputInfo.folder;
	    	$scope.inputInfo.whatHappened = $scope.info.whatHappened == 'Sonstiges (Freitext)' ? $scope.info.accidentSigns : $scope.info.whatHappened;
	    	$scope.inputInfo.modelId =  $scope.info.model ? $scope.info.model.id: null;
	    	for(key  in $scope.info.damageItems){
	    		if($scope.info.damageItems[key])
	    			$scope.inputInfo.damageComponents.push(key);
	    	}
	    	
	    	$scope.showCustomLoading = true;
	    	if($scope.showAreaOfBeenDamaged || $scope.hideHagel || $scope.hideGlas){
	    		ClaimDirectService.exportPdfNotUseServerData($scope.inputInfo).$promise.then(function (data) {
		    		$scope.showCustomLoading = false;
		    	    if (data != undefined && data != null){
		            	 if(data.message == 'ok'){
		            		 var url = window.location.pathname + 'rest/claimdirect/downloadPdf?reportId=' + data.reportId+'&folder='+$scope.folder;
		            		 $scope.uploadReportData();
		            		 if($rootScope.user && $rootScope.hasRole('ROLE_EMPLOYEES') )
		            			 $window.open(url,'_blank');
		     		    	$state.go("mainassistant.endpage");
		            	 }
		            	 else
		            		 UtilityService.messageErrorDilog(data.errorMessage);
		             }
		         }, function (error) {
		        	 $scope.showCustomLoading = false;
		             if (error.errorCode != undefined && error.errorCode != null)
		                 $rootScope.error = error.errorMessage;
		         });
	    	}else{
		    	ClaimDirectService.exportPdf($scope.inputInfo).$promise.then(function (data) {
		    		$scope.showCustomLoading = false;
		    	    if (data != undefined && data != null){
		            	 if(data.message == 'ok'){
		            		var url = window.location.pathname + 'rest/claimdirect/downloadPdf?reportId=' + data.reportId+'&folder='+$scope.folder;
		            		$scope.uploadReportData();
		            		if($rootScope.user && $rootScope.hasRole('ROLE_EMPLOYEES') )
		            			$window.open(url,'_blank');
		     		    	$state.go("mainassistant.endpage");
		            	 }
		            	 else
		            		 UtilityService.messageErrorDilog(data.errorMessage);
		             }
		         }, function (error) {
		        	 $scope.showCustomLoading = false;
		             if (error.errorCode != undefined && error.errorCode != null)
		                 $rootScope.error = error.errorMessage;
		         });
	    	
	    	}
	    	
	    }
	 
	    $scope.inputSms = function(){
	    	$scope.info.whatsAppValue = $scope.info.smsValue;
	    	$scope.info.faceTimeValue = $scope.info.smsValue;
	    	$scope.info.googleDuoValue = $scope.info.smsValue;
	    	$scope.info.googleHangoutsValue = $scope.info.smsValue;
	    }
	    $scope.inputEmail = function(){
	    	$scope.info.skypeValue = $scope.info.emailValue;
	    };
	    $scope.showMileage = 0;
	    $scope.otherCustomer = null;
	    $scope.findByLicencePlateNumber = function (){
	    	 $scope.showMileage = 0;
	    	if(!$scope.info.licencePlateNumberInjuredPerson)
	    		return;
	    	CustomerService.findByLicencePlateNumber({licencePlateNumber :  $scope.info.licencePlateNumberInjuredPerson.replace(/ |-/g, "")}).$promise.then(function (data) {
				if(data && data.length > 0){
					$scope.otherCustomer = data[0];
					$scope.showMileage = 1;
				}else{
					$scope.otherCustomer = null;
					$scope.showMileage = 2;
				}
					
	         }, function (error) {
	             if (error.errorCode != undefined && error.errorCode != null)
	                 $rootScope.error = error.errorMessage;
	         });
	    };
	    $scope.ibanValidate = "";
	    $scope.checkIban = function(){
	    	if(!IBAN.isValid($scope.info.iban))
	    		 $scope.ibanValidate = "ibanerror";
	    	else
	    		$scope.ibanValidate = "";
	    }
	    $scope.bicValidate ="";
	    $scope.checkBic = function(){
	    	if(!isBic($scope.info.bic))
	    		 $scope.bicValidate = "ibanerror";
	    	else
	    		 $scope.bicValidate = "";
	    }
	    function isBic(value) {
	        return /^([A-Z]{6}[A-Z2-9][A-NP-Z1-2])(X{3}|[A-WY-Z0-9][A-Z0-9]{2})?$/.test( value.toUpperCase() );
	    }
	    $scope.changeWhatApp = function(){
	    	if($scope.info.phoneContacts)
	    		$scope.info.whatsAppValue = $scope.info.phoneContactsValue
	    }
	    $scope.changeSms = function(){
	    	if($scope.info.phoneContacts)
	    		$scope.info.smsValue = $scope.info.phoneContactsValue
	    }
	    $rootScope.logoutAssistant = function(){
	    	SweetAlert.swal({
		        title: 'Sind Sie sicher?',
		        text: "Möchten Sie sich wirklich ausloggen? Alle eingegebenen Daten werden dadurch gelöscht.",
		        type: "info",
		        showCancelButton: true,
		        confirmButtonColor: "#d00000",
		        confirmButtonText: "Ja",
		        cancelButtonText: "Nein",
		        closeOnConfirm: true,
		        closeOnCancel: true }, 
		    function (isConfirm) {
		        if (isConfirm) {
		        	$rootScope.logout();
		        	
		        } else {
		        }
		    });
	    }
	    $rootScope.reloadPage = function(){
	    	SweetAlert.swal({
		        title: 'Sind Sie sicher?',
		        text: "Möchten Sie sich wirklich ausloggen? Alle eingegebenen Daten werden dadurch gelöscht.",
		        type: "info",
		        showCancelButton: true,
		        confirmButtonColor: "#d00000",
		        confirmButtonText: "Ja",
		        cancelButtonText: "Nein",
		        closeOnConfirm: true,
		        closeOnCancel: true }, 
		    function (isConfirm) {
		        if (isConfirm) {
		        	$rootScope.needReload = false;
			    	$state.go($state.current.name, {}, {reload: true});
		        	
		        } else {
		        }
		    });
	    	
	    };
	    $scope.goReportPage = function(){
	    	$scope.baseData = {
	    			witnessesAvailables : $scope.witnessesAvailables,
	    			info : $scope.info,
	    			people : $scope.people
	    	}
	    	if(window.reportData){
	    		window.reportData = null;
	    	}
	    	var url = "#/reportpage" ;
	    	var reportWindow = window.open(url,'_blank');
	    	reportWindow.reportData = $scope.baseData;
	    }
	    
	    $scope.showInzahlungnahme = function(fatherId){
	    	$scope.inzahlungnahmeDialog = ngDialog.open({
	               template: 'partials/webassistant/inzahlungahme.html',
	               className: 'ngdialog-theme-default customDialog',
	               scope: $scope
	           });
   		}
	    $scope.inzahlungnahmeDialog = '';
	    $scope.inzahlungnahmeAlert='';
	    $scope.closeInzahlungnahme = function(){
	    	 $scope.inzahlungnahmeDialog.close();
	    	 $scope.inzahlungnahmeAlert = ngDialog.open({
	               template: 'partials/webassistant/inzahlungahmeAlert.html',
	               className: 'ngdialog-theme-default customDialog',
	               scope: $scope
	           });
	    };
	    $scope.closeInzahlungnahmeAlert = function(){
	    	 $scope.inzahlungnahmeAlert.close();
	    };
	    $scope.$watch('location.search()', function() {
	    	var user = ($location.search()).user;
	    	var password = ($location.search()).password;
	    	login = ($location.search()).login;
	    	if(user){
	    		$scope.info.policyNumber = user;	    		
	    	}
	    	if(password){
	    		$scope.info.licencePlateNumber = password;	    		
	    	}
	    	if(user || password){
	    		$scope.info.insured = 1;	
	    		
	    	}
	    	if(login && login == 1){
	    		$scope.fullLink = true;
	    		$scope.login();	    		
	    	}
	    }, true);
	    $scope.autoLogin = function(){
	    	$location.search('user', user);
	    	$location.search('password', password);
	    	$location.search('login',login);
	    };
}

