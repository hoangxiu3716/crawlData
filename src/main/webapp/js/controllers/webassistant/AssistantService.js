var assistant = angular.module('assistant.services', ['ngResource']);
assistant.factory('AssistantService', function () {
	 var assistantService = {};
	    
	 var calculatorScoreDateTime = function(info){
			var scoreDayOfAccident = 0;
	    	var scoreTimeOfAccident = 0;
	    	var todoTextAccidents = [];
	    	if(info.dayOfAccidentUnknow){
	    		scoreDayOfAccident = 50;
	    		todoTextAccidents.push("Achtung Schadentag: Vorgang durch Revision prüfen lassen.");
	    		todoTextAccidents.push("Achtung Uhrzeit unbekannt: Vorgang durch Revision prüfen lassen.");
	    	}	
	    	else {
	    		if(info.dayOfAccident){
		    		var dayOfWeek = new Date(info.dayOfAccident).getDay();
			    	if(dayOfWeek > 5)
			    		scoreDayOfAccident = 92;
			    	else
				    	scoreDayOfAccident = 85;
		    	}if(info.timeOfAccident){
		    		var hours = info.timeOfAccident.split(":")[0];
		    		if(hours >= 0 && hours < 7){
		    			scoreTimeOfAccident = 20;
		    			todoTextAccidents.push("Achtung Uhrzeit von 0 - 7 Uhr: Vorgang durch Revision prüfen lassen.");
		    		}
		    		if(hours >= 7 && hours < 18 )
		    			scoreTimeOfAccident = 90;
		    		if(hours >= 18 && hours < 21 )
		    			scoreTimeOfAccident = 80;
		    		if(hours >= 21 && hours < 24 ){
		    			scoreTimeOfAccident = 60;
		    			todoTextAccidents.push("Achtung Uhrzeit von 21 - 24 Uhr: Vorgang eventuell durch Revision prüfen ");
		    		}
		    	}
	    	}
	    	return {scoreDayOfAccident : scoreDayOfAccident , scoreTimeOfAccident : scoreTimeOfAccident ,todoTextAccidents : todoTextAccidents}
	 }
	 var calculatorScoreDateTimeNew = function(info){
			var scoreDayOfAccident = 0;
	    	var scoreTimeOfAccident = 0;
	    	var todoTextAccidents = [];
	    	if(info.dayOfAccidentUnknow){
	    		scoreDayOfAccident = 50;
	    		todoTextAccidents.push("Achtung Uhrzeit: Vorgang durch Revision prüfen lassen.");
//	    		todoTextAccidents.push("Achtung Uhrzeit unbekannt: Vorgang durch Revision prüfen lassen.");
	    	}	
	    	else {
	    		if(info.dayOfAccident){
		    		var dayOfWeek = new Date(info.dayOfAccident).getDay();
			    	if(dayOfWeek > 5)
			    		scoreDayOfAccident = 92;
			    	else
				    	scoreDayOfAccident = 85;
		    	}if(info.timeOfAccident){
		    		var hours = info.timeOfAccident.split(":")[0];
		    		var minutes = info.timeOfAccident.split(":")[1];
		    		if(hours*60 >= 1260 && (hours*60 + minutes) <= 1430){
		    			todoTextAccidents.push("Achtung Uhrzeit: Vorgang eventuell durch Revision prüfen lassen.");
		    		}
		    		if(hours >= 0 && hours < 7){
		    			scoreTimeOfAccident = 20;
		    			todoTextAccidents.push("Achtung Uhrzeit: Vorgang durch Revision prüfen lassen.");
		    		}
		    		if(hours >= 7 && hours < 18 )
		    			scoreTimeOfAccident = 90;
		    		if(hours >= 18 && hours < 21 )
		    			scoreTimeOfAccident = 80;
		    		if(hours >= 21 && hours < 24 ){
		    			scoreTimeOfAccident = 60;
//		    			todoTextAccidents.push("Achtung Uhrzeit von 21 - 24 Uhr: Vorgang eventuell durch Revision prüfen ");
		    		}
		    	}
	    	}
	    	return {scoreDayOfAccident : scoreDayOfAccident , scoreTimeOfAccident : scoreTimeOfAccident ,todoTextAccidents : todoTextAccidents}
	 }
	 var calculatorScoreWitnessesAvailable = function(info){
		  var scoreWitnessesAvailable = 0 ;
		  var todoTextAccidents = [];
    	  if(!info.witnessesAvailable)
    		  scoreWitnessesAvailable = 75 ;
    	  else{
    		  scoreWitnessesAvailable = 95 ;
    		  var textWitnessesAvailable = "Bitte Zeugen kontaktieren: ";
    		 
    		  info.witnessesAvailables.forEach(function(entry) {
//    			  textWitnessesAvailable += entry.name +", " + entry.street + ", " +entry.postCode + " "+ entry.city + ", Tel :" + entry.phone+ ";";
    			  textWitnessesAvailable +="<br/>  - ";
    			  if(entry.name)
    				  textWitnessesAvailable += entry.name +", ";
    			  if(entry.street)
    				  textWitnessesAvailable += entry.street +", ";
    			  if(entry.postCode)
    				  textWitnessesAvailable += entry.postCode+ " "+ entry.city +", ";
    			  if(entry.phone)
    				  textWitnessesAvailable += "Tel :" + entry.phone;
    			  else
    				  textWitnessesAvailable =  textWitnessesAvailable.substring(0, textWitnessesAvailable.length -2);
//    			  textWitnessesAvailable += "<br/>";
    		  });
    		  todoTextAccidents.push(textWitnessesAvailable);
    	  }
    	  return {scoreWitnessesAvailable : scoreWitnessesAvailable,todoTextAccidents : todoTextAccidents}
	 }
	 
	 var calculatorScorePoliceInformed = function(info){
		  var scorePoliceInformed = 0;
		  var todoTextAccidents = [];
    	  if(!info.policeInformed)
    		  scorePoliceInformed = 75;
    	  else{
    		  scorePoliceInformed = 95;
    		  var department = (info.department ? info.department  + ", " : "");
    		  todoTextAccidents.push("Bitte Polizei kontaktieren: " + department + info.fileNumber);
    	  }
    	  return {scorePoliceInformed : scorePoliceInformed , todoTextAccidents : todoTextAccidents}
	 }
	 
	 var calculatorScoreVehicleReady = function(info){
		 var scoreVehicleReady = 0;
		 var todoTextAccidents = [];
	   	  if(info.vehicleReady)
	   		  scoreVehicleReady = 93;
	   	  else{
	   		  scoreVehicleReady = 50;
	   		  todoTextAccidents.push("Bitte Unfallersatzwagen anbieten.");
	   	  }
	   	  return {scoreVehicleReady : scoreVehicleReady ,todoTextAccidents : todoTextAccidents};
	 };
	 var calculatorScoreTowingService = function(info){
		 var scoreTowingService = 0;
		 var todoTextAccidents = [];
		   	  if(!info.vehicleReady && info.towingService){
		   		  scoreTowingService = 50;
		   		  todoTextAccidents.push("Bitte Abschleppdienst beauftragen.");
		   	  }else
		   		  scoreTowingService = 93;
		
	   	  return {scoreTowingService : scoreTowingService ,todoTextAccidents : todoTextAccidents};
	 };
	 
	 var calculatorScoreValidDrivingLicense = function(info){
		 var scoreValidDrivingLicense = 0;
		 var todoTextAccidents = [];
	   	  if(info.validDrivingLicense)
	   		  scoreValidDrivingLicense = 93;
	   	  else
	   		  todoTextAccidents.push("Achtung: Fahrer hat keinen gültigen Führerschein.");
	   	  return {scoreValidDrivingLicense : scoreValidDrivingLicense , todoTextAccidents : todoTextAccidents}
	 }
	 var calculatorScoreDamageLevel = function(info){
	 	  var scoreDamageLevel = 0;
	 	  var todoTextAccidents = [];
    	  if(info.damageLevel === 'ohne Beleg' ){
    		  scoreDamageLevel = 20;
    		  todoTextAccidents.push("Achtung Schaden ohne Beleg: Werkstattsteuerung bitte prüfen.");
    	  }
    	  if(info.damageLevel === 'Kostenvoranschlag' ){
    		  scoreDamageLevel = 85;
    		  todoTextAccidents.push("KV liegt vor: eventuell Prüfdienstleister beauftragen.");
    	  }
    	  if(info.damageLevel === 'Rechnung' ){
    		  scoreDamageLevel = 92;
    		  todoTextAccidents.push("Rechnung liegt vor: eventuell Prüfdienstleister beauftragen.");
    	  }
    	  if(info.damageLevel === 'Gutachten' ){
    		  scoreDamageLevel = 75;
    		  todoTextAccidents.push("Achtung Gutachten liegt vor: Werkstattsteuerung bitte prüfen.");
    	  }
    	  return {scoreDamageLevel : scoreDamageLevel, todoTextAccidents : todoTextAccidents};
	 }
	 var calculatorVehicleRepaired = function(info){
		  var scoreVehicleRepaired = 0;
		  var todoTextAccidents = [];
    	  if(info.damageLevel !='Rechnung' && !info.vehicleRepaired && info.moneyOfTheRepair){
    		  scoreVehicleRepaired = 70;
    		  todoTextAccidents.push("VN wünscht fiktive Abrechnung, bitte kontaktieren.");
    	  }
    	  if(info.damageLevel !='Rechnung' && info.vehicleRepaired && !info.werkstattnetz){
    		  scoreVehicleRepaired = 75;
    		  todoTextAccidents.push("Kunde wünscht Reparatur.");
    	  }
    	  if(info.damageLevel !='Rechnung' && info.vehicleRepaired && info.werkstattnetz){
    		  scoreVehicleRepaired = 92;
    		  var address = info.werkstattnetz.strasse + ", " + info.werkstattnetz.plz + " " + info.werkstattnetz.ort ;	
    		  if (info.werkstattnetz.telefon){
    		  	address += ", Tel.: " + info.werkstattnetz.telefon;
    		  }
    		  todoTextAccidents.push("Kunde wünscht Reparatur in Partnerwerkstatt "+info.werkstattnetz.name +", " +address);
    	  }
    	  return {scoreVehicleRepaired : scoreVehicleRepaired , todoTextAccidents : todoTextAccidents};
	 }
	 var calculatorScoreStolenItem = function(info){
		 var todoTextAccidents = [];
		 var hasTodo = false;
		 var scoreStolen = 0;
		 var i=0;
		 for(key  in info.stolenItem){
	    		if(info.stolenItem[key]){
	    			hasTodo = true;
	    			i++;
	    			if(key != 'Sonstiges')
	    				scoreStolen +=85;
	    		}	
	     }
		 scoreStolen = scoreStolen / i;
		 if(hasTodo)
			 todoTextAccidents.push("Sachverständigen für Besichtigung beauftragen");
		 return {scoreStolen : scoreStolen , todoTextAccidents : todoTextAccidents};
	 }
	 
	 var calculatorScoreUploadedFile = function(info){
		 var scoreUploadedFile = 0;
		 var todoTextAccidents = [];
	   	  if(info.urlZipFile){
	   		  scoreUploadedFile = 90;
	   		  todoTextAccidents.push("Es liegen Dokumente und Lichtbilder zur möglichen Prüfung vor.");
	   	  }else{
	   		  scoreUploadedFile = 50;
	   		  todoTextAccidents.push("Achtung: VN hat keine Bilder und Dokumente hochgeladen. Bitte einfordern!");
	   	  }
	   	  return {scoreUploadedFile : scoreUploadedFile,todoTextAccidents : todoTextAccidents}
	 }
	 var calculatorWhoDriving = function(info){
		 var scoreWhoDriving = 0 ;
		 var todoTextAccidents = [];
    	 if(!info.whoDriving){}
//    		 todoTextAccidents.push("Bitte sonstigen Fahrer kontaktieren und Stellungnahme anfordern.");
    	 else {
    		 if(info.whoDriving === 'Versicherungsnehmer')
    			 scoreWhoDriving = 93;
    	 	 if(info.whoDriving === 'Sonstiges (Name)'){
    	 		 scoreWhoDriving = 50;
    	 		todoTextAccidents.push("Bitte sonstigen Fahrer kontaktieren und Stellungnahme anfordern.");
    	 	 }
    	 }
    	 return {scoreWhoDriving : scoreWhoDriving , todoTextAccidents : todoTextAccidents}
	 }
	 
	 var calculatoScoreInfluence = function(info){
		 var scoreInfluence = 0 ;
		 var todoTextAccidents = [];
	   	  if(!info.influence)
	   		  scoreInfluence = 85;
	   	  else  todoTextAccidents.push("Fahrer stand unter Alkohol bzw. Drogen. Bitte Revision kontaktieren.");
	   	  return {scoreInfluence : scoreInfluence , todoTextAccidents : todoTextAccidents}
	 }
	 
	 var calculatorScoreInjuredPeople = function(info){
		 var scoreInjuredPeople = 0 ;
		 var todoTextAccidents = [];
	   	  if(info.injuredPeople){
	   		  scoreInjuredPeople = 25;
	   		  var people = info.people ? info.people.join(",") : "";
	   		  todoTextAccidents.push("Achtung: Es liegt ein zusätzlicher Personenschaden vor: "+people);
	   	  }else
	   		  scoreInjuredPeople = 90;
	   	  return {scoreInjuredPeople : scoreInjuredPeople , todoTextAccidents : todoTextAccidents}
	 }
	 var calculatorScoreEnvironment = function(info){
		 var scoreEnvironment = 0 ;
		 var todoTextAccidents = [];
	   	  if(info.environment == 'Autobahn'){
	   		  scoreEnvironment = 75;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang eventuell durch Revision prüfen lassen");
	   	  }
	   	  if(info.environment == 'Land-/Bundesstraße'){
	   		  scoreEnvironment = 40;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang durch Revision prüfen lassen");
	   	  }
	   	  if(info.environment == 'Innerorts'){
	   		  scoreEnvironment = 90;
	   	  }
	   	  if(info.environment == 'Gewerbegebiet'){
	   		  scoreEnvironment = 40;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang durch Revision prüfen lassen");
	   	  }
	   	  if(info.environment == 'Parkplatz'){
	   		  scoreEnvironment = 50;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang durch Revision prüfen lassen");
	   	  }
	   	  if(info.environment == 'Privatgelände'){
	   		  scoreEnvironment = 70;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang eventuell durch Revision prüfen lassen");
	   	  }
	   	  if(info.environment == 'Sonstiges'){
	   		  scoreEnvironment = 0;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang durch Revision prüfen lassen");
	   	  }
	   	  return {scoreEnvironment : scoreEnvironment , todoTextAccidents : todoTextAccidents}
	 }
	 var calculatorScoreEnvironmentSturm = function(info){
		 var scoreEnvironment = 0 ;
		 var todoTextAccidents = [];
	   	  if(info.environment == 'Autobahn'){
	   		  scoreEnvironment = 75;
	   	  }
	   	  if(info.environment == 'Land-/Bundesstraße'){
	   		  scoreEnvironment = 40;
	   	  }
	   	  if(info.environment == 'Innerorts'){
	   		  scoreEnvironment = 90;
	   	  }
	   	  if(info.environment == 'Gewerbegebiet'){
	   		  scoreEnvironment = 40;
	   	  }
	   	  if(info.environment == 'Parkplatz'){
	   		  scoreEnvironment = 50;
	   	  }
	   	  if(info.environment == 'Privatgelände'){
	   		  scoreEnvironment = 70;
	   	  }
	   	  if(info.environment == 'Sonstiges'){
	   		  scoreEnvironment = 0;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang durch Revision prüfen lassen");
	   	  }
	   	  return {scoreEnvironment : scoreEnvironment , todoTextAccidents : todoTextAccidents}
	 }
	 var calculatorScoreEnvironmentVandalismus = function(info){
		 var scoreEnvironment = 0 ;
		 var todoTextAccidents = [];
	   	  if(info.environment == 'Autobahn'){
	   		  scoreEnvironment = 40;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang eventuell durch Revision prüfen lassen");
	   	  }
	   	  if(info.environment == 'Land-/Bundesstraße'){
	   		  scoreEnvironment = 60;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang eventuell durch Revision prüfen lassen");
	   	  }
	   	  if(info.environment == 'Innerorts'){
	   		  scoreEnvironment = 90;
	   	  }
	   	  if(info.environment == 'Gewerbegebiet'){
	   		  scoreEnvironment = 85;
	   	  }
	   	  if(info.environment == 'Parkplatz'){
	   		  scoreEnvironment = 90;
	   	  }
	   	  if(info.environment == 'Privatgelände'){
	   		  scoreEnvironment = 85;
	   	  }
	   	  if(info.environment == 'Sonstiges'){
	   		  scoreEnvironment = 0;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang durch Revision prüfen lassen");
	   	  }
	   	  return {scoreEnvironment : scoreEnvironment , todoTextAccidents : todoTextAccidents}
	 }
	 var calculatorScoreEnvironmentHagel = function(info){
		 var scoreEnvironment = 0 ;
		 var todoTextAccidents = [];
	   	 
	   	  if(info.environment && info.environment == 'Sonstiges'){
	   		  scoreEnvironment = 60;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang durch Revision prüfen lassen");
	   	  }
	   	 if(info.environment && info.environment != 'Sonstiges'){
	   		  scoreEnvironment = 85;
	   	  }
	   	  return {scoreEnvironment : scoreEnvironment , todoTextAccidents : todoTextAccidents}
	 }
	 var calculatorScoreEnvironmentSonstiges = function(info){
		 var scoreEnvironment = 0 ;
		 var todoTextAccidents = [];
	   	 
	   	  if(info.environment && info.environment == 'Sonstiges'){
	   		  scoreEnvironment = 0;
	   		  todoTextAccidents.push("Achtung Örtlichkeit: Vorgang durch Dubios-SB prüfen lassen");
	   	  }
	   	 if(info.environment && info.environment != 'Sonstiges'){
	   		  scoreEnvironment = 75;
	   	  }
	   	  return {scoreEnvironment : scoreEnvironment , todoTextAccidents : todoTextAccidents}
	 }
	 var calculatorScoreDamageEarlierTimes = function(info){
		 var score = 0 ;
		 var todoTextAccidents = [];
	   	  if(info.damageEarlierTimes){
	   		  score = 20;
	   		  todoTextAccidents.push("Achtung: Es liegt ein nicht reparierter Hagelschaden vor. Bitte Vorgutachten anfordern");
	   	  }else
	   		score = 90;
	   	  return {score : score , todoTextAccidents : todoTextAccidents}
	 }

	 var calculatorScoreVehicleDamagedItem = function(info){
		 var scoreVehicleDamagedItem = 0 ;
		 var todoTextAccidents = [];
	   	  if(info.vehicleDamagedItem == 'Äste'){
	   		scoreVehicleDamagedItem = 93;
	   		todoTextAccidents.push("Kunde kontaktieren und ggf. Abschleppunternehmen beauftragen");
	   	  }
	   	  if(info.environment == 'Baum'){
	   		scoreVehicleDamagedItem = 85;
	   	  }
	   	  if(info.environment == 'Dachziegel'){
	   		scoreVehicleDamagedItem = 75;
	   	  }
	   	  if(info.environment == 'Herumfliegende Gegenstände'){
	   		scoreVehicleDamagedItem = 20;
	   	  }
	   	  if(info.environment == 'Sonstiges'){
	   		scoreVehicleDamagedItem = 0;
	   	  }
	   	  return {scoreVehicleDamagedItem : scoreVehicleDamagedItem , todoTextAccidents : todoTextAccidents}
	 }
	 
	 var calculatorVehicleParked = function(info){
		 var scoreVehicleParked  = 0 ;
	   	  if(info.vehicleParked)
	   		scoreVehicleParked = 93;
	   	  else
	   		scoreVehicleParked = 60;
	   
	   	  return {scoreVehicleParked : scoreVehicleParked }
	 }

	 var calculatorDescribeYourDamageItem = function(info){
		 var todoTextAccidents = [];
		 var scoreDescribeYourDamageItem = 0;
		 var i=0;
		 for(key  in info.describeYourDamageItem){
	    		if(info.describeYourDamageItem[key]){
	    			
	    			i++;
	    			if(key == 'Sonstiges'){
	    				scoreDescribeYourDamageItem +=20;
	    				 todoTextAccidents.push("Achtung: Schadenhergang ist nicht bekannt");
	    			}
	    			if(key == 'Lackbeschädigung durch Kratzer'){
	    				scoreDescribeYourDamageItem +=80;
	    			}
	    			if(key == 'Eindellung an der Karosserie'){
	    				scoreDescribeYourDamageItem +=75;
	    			}	
	    			if(key == 'Scheiben beschädigt'){
	    				scoreDescribeYourDamageItem +=75;
	    			}
	    			if(key == 'Antenne beschädigt'){
	    				scoreDescribeYourDamageItem +=75;
	    			}
	    			if(key == 'Außenspiegel beschädigt'){
	    				scoreDescribeYourDamageItem +=80;
	    			}
	    			if(key == 'Scheibenwischer beschädigt'){
	    				scoreDescribeYourDamageItem +=75;
	    			}

	    		}	
	     }
		 if(i > 0)
			 scoreDescribeYourDamageItem = scoreDescribeYourDamageItem / i;
		 return {scoreDescribeYourDamageItem : scoreDescribeYourDamageItem , todoTextAccidents : todoTextAccidents};
	 }
	 
	 var calculatorScoreForesterInformed = function(info){
		 var score = 0;
		 if(info.foresterInformed)
			 score = 93;
		 else
			 score = 25;
		 return {score : score};
	 }
	 
	 var calculatoWildCasualtyCertificate = function(info){
		 var score = 0 ;
		 var todoTextAccidents = [];
	   	  if(!info.wildCasualtyCertificate){
	   		score = 25;
	   		  todoTextAccidents.push("Achtung: Es liegt keine Wildbescheinigung vor. Ggf. VN befragen")
	   	  	}
	   	  else  score = 90;
	   	  return {score : score , todoTextAccidents : todoTextAccidents}
	 }
	 
	 var calculatorScoreAnimal = function(info){
		 var score = 0 ;
		 var todoTextAccidents = [];
	   	  if(info.animal == 'Fuchs'){
	   		score = 85;
	   	  }
	   	  if(info.animal == 'Hirsch'){
	   		score = 75;
	   	  }
	   	  if(info.animal == 'Hund'){
	   		score = 0;
	   		todoTextAccidents.push("Achtung Kollision mit einem Hund - kein Wildschaden");
	   	  }
	   	  if(info.animal == 'Kaninchen'){
	   		score = 85;
	   	  }
	   	  if(info.animal == 'Marder'){
		   		score = 75;
		   	  }
	   	  if(info.animal == 'Reh'){
		   		score = 90;
		   	  }
	   	  if(info.animal == 'Waschbär'){
		   		score = 70;
		   	  }
	   	  if(info.animal == 'Wildschwein'){
		   		score = 90;
		   	  }
	   	  if(info.animal == 'Sonstiges'){
	   		score = 20;
	   		todoTextAccidents.push("Achtung:	Vorgang	auf	Wildschaden	prüfen");
	   	  }
	   	  return {score : score , todoTextAccidents : todoTextAccidents}
	 }
	 var calculatoCollisionAnimal = function(info){
		 var score = 0 ;
		 var todoTextAccidents = [];
	   	  if(!info.collisionAnimal){
	   		  score = 85;
	   	  	}
	   	  else  score = 20;
	   	  return {score : score , todoTextAccidents : todoTextAccidents}
	 }
	 var calculatorBaseZahlschaden = function(customer){
		 var scoreZahlschadenYes =0;
		 var scoreZahlschadenNo = 0;
		  if(customer.vn_kfz_his == 0)
			  scoreZahlschadenYes += 10;
		  if(customer.vertrag_vorSchd_anz == null || customer.vertrag_vorSchd_anz == undefined )
			  scoreZahlschadenNo += 5;
	   	  if(customer.vertrag_vorSchd_anz == 1 && customer.vertrag_letztVorschd_monat >=0 && customer.vertrag_letztVorschd_monat <= 24  && info.rateDamage > 100 )
	   		  scoreZahlschadenNo +=15;
	   	  if(customer.vertrag_vorSchd_anz == 2 && customer.vertrag_letztVorschd_monat >=0 && customer.vertrag_letztVorschd_monat <= 24 )
	   		  scoreZahlschadenNo +=30;
	   	  if(customer.vertrag_vorSchd_anz >= 2 && customer.vertrag_vorSchd_anz <= 99 && customer.vertrag_letztVorschd_monat >=0 && customer.vertrag_letztVorschd_monat <= 24 )
	   		  scoreZahlschadenNo +=50;
	   	  if(customer.vertrag_vorSchd_anz >= 1 && customer.vertrag_vorSchd_anz <= 99 && customer.vertrag_letztVorschd_monat >=0 && customer.vertrag_letztVorschd_monat <= 6 )
	   		  scoreZahlschadenNo +=20;
	   	  if(customer.vertrag_vorSchd_anz >= 1 && customer.vertrag_vorSchd_anz <= 99 && customer.vertrag_letztVorschd_monat >6 && customer.vertrag_letztVorschd_monat <=12 )
	   		  scoreZahlschadenNo +=10;
	   	  if(customer.vertrag_vorSchd_anz >= 1 && customer.vertrag_vorSchd_anz <= 99 && customer.vertrag_letztVorschd_monat >12 && customer.vertrag_letztVorschd_monat <=18 )
	   		  scoreZahlschadenNo +=5;
	   	  if(customer.fahrleistung > 40000)
	   		  scoreZahlschadenYes += 10;
   	   return {scoreZahlschadenYes : scoreZahlschadenYes,scoreZahlschadenNo : scoreZahlschadenNo}
	 }
	 assistantService.calculatorAccidentUnfall = function(info,customer,unfall){
		 	
	    	var todoTextAccidents = [];
	    	var resultData ={
	    			scoreSchadeninformation : 0,
	    			scoreSchadenzusatzkosten : 0,
	    			scoreSchadenhergang : 0,
	    			scoreSchadensteuerung : 0,
	    			scoreUploadedFile : 0 ,
	    			scoreEnvironment : 0,
	    			zahlschaden : '' ,
	    			todoTextAccidents : [],
	    			needCalculatorZahlschadenInserver : true
	    	}
	    	
	    	// start calculator score Schadeninformation
	    	todoTextAccidents.push("Schadeninformation");
	    	var scoreDayOfAccident = 0;
	    	var scoreTimeOfAccident = 0;
	    	var scoreDateTime= calculatorScoreDateTimeNew(info);
	    	scoreDayOfAccident = scoreDateTime.scoreDayOfAccident;
	    	scoreTimeOfAccident = scoreDateTime.scoreTimeOfAccident;
	    	todoTextAccidents.push.apply(todoTextAccidents,scoreDateTime.todoTextAccidents);
	    	
	    	 var scoreWhoDriving = 0 ;
	    	 var whoDriving = calculatorWhoDriving(info);
	    	 scoreWhoDriving = whoDriving.scoreWhoDriving;
	    	 todoTextAccidents.push.apply(todoTextAccidents,whoDriving.todoTextAccidents);
	    	 
	    	  var scoreValidDrivingLicense = 0;
	    	  var validDrivingLicense = calculatorScoreValidDrivingLicense(info);
	    	  scoreValidDrivingLicense = validDrivingLicense.scoreValidDrivingLicense;
	    	  todoTextAccidents.push.apply(todoTextAccidents,validDrivingLicense.todoTextAccidents);
	    	
	    	  
	    	  var scoreInfluence = 0 ;
	    	  var influence = calculatoScoreInfluence(info); 
	    	  scoreInfluence = influence.scoreInfluence;
	    	  todoTextAccidents.push.apply(todoTextAccidents,influence.todoTextAccidents);
	    	  
	    	  var scoreWitnessesAvailable = 0 ;
	    	  var witnessesAvailable = calculatorScoreWitnessesAvailable(info);
	    	  scoreWitnessesAvailable = witnessesAvailable.scoreWitnessesAvailable;
	    	  todoTextAccidents.push.apply(todoTextAccidents,witnessesAvailable.todoTextAccidents);
	    	 
	    	  var scorePoliceInformed = 0;
	    	  var policeInformed =  calculatorScorePoliceInformed(info);
	    	  scorePoliceInformed = policeInformed.scorePoliceInformed;
	    	  todoTextAccidents.push.apply(todoTextAccidents,policeInformed.todoTextAccidents);
	    	  
	    	  var scoreEnvironment = 0;
	    	  var environment =  calculatorScoreEnvironment(info);
	    	  scoreEnvironment = environment.scoreEnvironment;
	    	  todoTextAccidents.push.apply(todoTextAccidents,environment.todoTextAccidents);
	    	  
	    	  //need
	    	  resultData.scoreSchadeninformation = ((scoreDayOfAccident + scoreTimeOfAccident + scoreWhoDriving + scoreValidDrivingLicense + scoreInfluence + scoreWitnessesAvailable + scorePoliceInformed+scoreEnvironment ) / 8).toFixed(2);
	    	  // end calulator score Schadeninformation
	    	  
	    	  // start calculator Schadenzusatzkosten
	    	  todoTextAccidents.push("Schadenzusatzkosten");
	    	  var scoreVehicleReady = 0;
	    	  var vehicleReady = calculatorScoreVehicleReady(info);
	    	  scoreVehicleReady = vehicleReady.scoreVehicleReady;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleReady.todoTextAccidents);
	    	  
	    	  var scoreTowingService = 0;
	    	  var towingService = calculatorScoreTowingService(info);
	    	  scoreTowingService = towingService.scoreTowingService;
	    	  todoTextAccidents.push.apply(todoTextAccidents,towingService.todoTextAccidents);
	    	  
	    	  var scoreInjuredAnotherPassenger = 0;
	    	  if(info.injuredAnotherPassenger){
	    		  scoreInjuredAnotherPassenger = 25;
	    		  var textInjuredAnotherPassenger = "Achtung es könnte ein KH Schaden vorliegen: Bitte kontaktieren Sie den AST ";
	    		  if(info.name)
	    			  textInjuredAnotherPassenger += info.name + ", ";
	    		  if(info.mark)
	    			  textInjuredAnotherPassenger += info.mark + ", ";
	    		  if(info.phone)
	    			  textInjuredAnotherPassenger += "Tel.: " + info.phone;
	    		  else
	    			  textInjuredAnotherPassenger = textInjuredAnotherPassenger.substring(0, textInjuredAnotherPassenger.length - 2);
	    		  todoTextAccidents.push(textInjuredAnotherPassenger);
	    	  }else
	    		  scoreInjuredAnotherPassenger = 92;
	    	  
	    	  var scoreInjuredPeople = 0 ;
	    	  var  injuredPeople = calculatorScoreInjuredPeople(info);
	    	  scoreInjuredPeople = injuredPeople.scoreInjuredPeople ;
	    	  todoTextAccidents.push.apply(todoTextAccidents,injuredPeople.todoTextAccidents);

	    	  
	    	  //need
	    	  resultData.scoreSchadenzusatzkosten = ((scoreVehicleReady + scoreTowingService + scoreInjuredAnotherPassenger + scoreInjuredPeople)/4).toFixed(2) ;
	    	  // end calculator score Schadenzusatzkosten
	    	  
	    	  // start calculator score Schadenhergang
	    	  todoTextAccidents.push("Schadenhergang");
	    	  var scoreWhatHappened = 0 ;
	    	  if(info.whatHappened == 'Auffahrunfall' ){
	    		  scoreWhatHappened = 92 ;
	    		  todoTextAccidents.push("Achtung Auffahrunfall: Es könnte ein KH Schadenvorliegen.");
	    	  }
	    	  if(info.whatHappened == 'Ausweichkollision' ){
	    		  scoreWhatHappened = 50 ;
	    		  todoTextAccidents.push("Achtung Ausweichkollision: Rechtslage ist zu klären.");
	    	  }
	    	  if(info.whatHappened == 'Frontalzusammenstoß' ){
	    		  scoreWhatHappened = 50 ;
	    		  todoTextAccidents.push("Achtung Frontalzusammenstoß: Bitte Unfallgegner kontaktieren.");
	    	  }
	    	  if(info.whatHappened == 'Frontschaden' ){
	    		  scoreWhatHappened = 90 ;
	    		  todoTextAccidents.push("Achtung Frontschaden: Es könnte eventuell auch ein KH Schaden vorliegen");
	    	  }
	    	  if(info.whatHappened == 'Heckschaden' ){
	    		  scoreWhatHappened = 90 ;
	    	  }
	    	  if(info.whatHappened == 'Kollision' ){
	    		  scoreWhatHappened = 70 ;
	    	  }
	    	  if(info.whatHappened == 'Parkplatzschaden/Delle' ){
	    		  scoreWhatHappened = 92 ;
	    		  todoTextAccidents.push("Bitte Smart-Repair-Dienstleister beauftragen.");
	    	  }
	    	  if(info.whatHappened == 'Seitenkollision' ){
	    		  scoreWhatHappened = 70 ;
	    		  todoTextAccidents.push("Achtung Seitenkollision: Es könnte ein KH Schadenvorliegen.");
	    	  }
	    	  if(info.whatHappened == 'Streifschaden' ){
	    		  scoreWhatHappened = 60 ;
	    		  todoTextAccidents.push("Achtung Streifschaden: Es könnte ein KH Schadenvorliegen.");
	    	  }
	    	  if(info.whatHappened == 'Vorfahrtsverletzung' ){
	    		  scoreWhatHappened = 85 ;
	    		  todoTextAccidents.push("Achtung Vorfahrtsverletzung: Bitte Unfallgegner kontaktieren.");
	    	  }
	    	  if(info.whatHappened == 'Sonstiges (Freitext)' ){
	    		  scoreWhatHappened = 20 ;
	    		  todoTextAccidents.push("Achtung: Es liegt kein Standardschaden vor.");
	    	  }
	    	  //need
	    	  resultData.scoreSchadenhergang = scoreWhatHappened.toFixed(2);
	    	  //end calculator Schadenhergang
	    	  
	    	  // start calculator Schadensteuerung
	    	  todoTextAccidents.push("Schadensteuerung");
	    	  var scoreDamageLevel = 0;
	    	  var damageLevel = calculatorScoreDamageLevel(info);
	    	  scoreDamageLevel = damageLevel.scoreDamageLevel;
	    	  todoTextAccidents.push.apply(todoTextAccidents,damageLevel.todoTextAccidents);
	    	  if(!unfall.onlyForeignCar){
		    	  var scoreVehicleRepaired = 0;
		    	  var vehicleRepaired = calculatorVehicleRepaired(info);
		    	  scoreVehicleRepaired = vehicleRepaired.scoreVehicleRepaired;
		    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleRepaired.todoTextAccidents);
		    	  resultData.scoreSchadensteuerung = ((scoreDamageLevel + scoreVehicleRepaired)/2).toFixed(2) ;
	    	  }else{
	    	  //need
	    	  resultData.scoreSchadensteuerung = scoreDamageLevel.toFixed(2) ;
	    	  // end calculator Schadensteuerung
	    	  }
	    	  //start calculator Datenumfang
	    	  todoTextAccidents.push("Datenumfang");
	    	  var scoreUploadedFile = 0;
	    	  var uploadedFile = calculatorScoreUploadedFile(info);
	    	  scoreUploadedFile = uploadedFile.scoreUploadedFile;
	    	  todoTextAccidents.push.apply(todoTextAccidents,uploadedFile.todoTextAccidents);
	    	
	    	  resultData.scoreUploadedFile = scoreUploadedFile.toFixed(2) ;  
	    	  // end calculator Datenumfang
	    	  
	    	  // start calculator Zahlschaden
	    	  var baseZahlschaden =  calculatorZahlschaden(info,customer);
    		  resultData.zahlschaden = baseZahlschaden.zahlschaden;
	    	  resultData.todoTextAccidents = todoTextAccidents;
	    	  
	    	  return resultData;
	    }
	 var getTksb = function(customer){
		 var tksb = null;
		 if(customer.tkSb){
			  tksb = customer.tkSb.split(" ")[1];
		 }
		 return tksb;
	 };
	 var calculatorZahlschaden = function(info,customer){
		  // start calculator Zahlschaden
	   	  var scoreZahlschadenYes = 0 ;
	   	  var scoreZahlschadenNo = 0 ;
	   	  var rateDamage = 0;
	   	  if(!info.rateDamage && !info.valueDamageComponent){
	   		rateDamage = -1;
	   	  }else{
		   	if(info.rateDamage>=0 && info.damageLevel && info.damageLevel !='ohne Beleg'){
		   		rateDamage = info.rateDamage;
		   	}else{
		   		if(info.valueDamageComponent)
		   			rateDamage=info.valueDamageComponent.average;
		   	}	
	   	  } 	

	   	  if(info.rateDamageUnknow || !rateDamage || rateDamage < 0){
	   		 //scoring 0
	   	  }else{
	   		  if(0 < rateDamage && rateDamage <= 250)
	   			  scoreZahlschadenYes += 40;
	   		  if(250 < rateDamage  && rateDamage  <= 500)
	   			  scoreZahlschadenYes += 30;
	   		  if(500 < rateDamage  && rateDamage  <= 1000)
	   			  scoreZahlschadenYes += 20;
	   		  if(2000 < rateDamage  && rateDamage  <= 2500)
	   			  scoreZahlschadenNo += 10;
	   		  if(2500 < rateDamage  && rateDamage  <= 3000)
	   			  scoreZahlschadenNo += 15;
	   		  if(3000 < rateDamage  && rateDamage  <= 5000)
	   			  scoreZahlschadenNo += 20;
	   		  if(rateDamage > 5000)
	   			  scoreZahlschadenNo += 75;
	   	  }
//	   	if(1000 < info.rateDamage && info.rateDamage <= 1500)
	   	// scoring 0
//	   	if(1500 < info.rateDamage && info.rateDamage <= 2000)
	   	// scoring 0
	   	  if(info.damageLevel == 'Kostenvoranschlag')
	   		  scoreZahlschadenNo +=10;
	   	  if(info.damageLevel == 'Rechnung')
	   		  scoreZahlschadenYes +=20;
	   	  if(info.damageLevel == 'Gutachten')
	   		  scoreZahlschadenNo +=10;
	   	  if(info.influence)
	   		  scoreZahlschadenNo +=100;
	   	  if(info.policeInformed)
	   		  scoreZahlschadenYes +=20;
	   	  
	   	  if(customer.dateOfFirstRegistration){
	               var timeDistance  = new Date().getTime() - customer.dateOfFirstRegistration;
	               var dayDistance = Math.ceil(timeDistance / (1000 * 3600 *24*30));
	               if(0<= dayDistance && dayDistance <= 6)
	               	scoreZahlschadenYes += 10;
	               if(6< dayDistance && dayDistance <= 12)
	               	scoreZahlschadenYes += 15;
	               if(120 < dayDistance && dayDistance <= 360)
	               	scoreZahlschadenNo += 10;
	               if(dayDistance > 360)
	               	scoreZahlschadenNo += 30;
	   	  }else{
	   		  scoreZahlschadenNo += 5;
	   	  }
	   	  var dayDistanceStartDate = null;
	   	  if(customer.firstContractBegin){
	   		  var timeDistanceStartDate  = new Date().getTime() - customer.firstContractBegin;
	                dayDistanceStartDate = Math.ceil(timeDistanceStartDate / (1000 * 3600 * 24*30));
	               if(0 < dayDistanceStartDate && dayDistanceStartDate <= 2){
	               	scoreZahlschadenNo +=20;
	               	if(info.rateDamage > 500)
	               		scoreZahlschadenNo +=40;
	               }
	               if(2 < dayDistanceStartDate && dayDistanceStartDate <= 6)
	               	scoreZahlschadenNo +=10;
	               if(6 < dayDistanceStartDate && dayDistanceStartDate <= 12)
	               	scoreZahlschadenNo +=5;
	               if(24 < dayDistanceStartDate && dayDistanceStartDate <= 36)
	               	scoreZahlschadenYes += 10;
	               if(dayDistanceStartDate > 36)
	               	scoreZahlschadenYes += 15;
	   	  }else{
	   		  scoreZahlschadenNo += 5;
	   	  }
	   	  if(customer.garageCommitment)
	   		  scoreZahlschadenYes += 20;
	   	  if(info.moneyOfTheRepair){
	   		  scoreZahlschadenNo +=15;
	   		  if(dayDistanceStartDate && (0 <=  dayDistanceStartDate && dayDistanceStartDate <=3))
	   			  scoreZahlschadenNo +=20;
	   	  }

   		  
   		  var tksb = getTksb(customer);
	   	  if(info.typeOfDamage == 'Wildunfall' && info.collisionAnimal){
   			  if(tksb == 150)
   				 scoreZahlschadenNo +=30;
   			  if(tksb == 300)
   				 scoreZahlschadenNo +=25;
	   	  }
	   	  var baseZahlschaden =  calculatorBaseZahlschaden(customer);
	   	  scoreZahlschadenYes += baseZahlschaden.scoreZahlschadenYes;
	   	  scoreZahlschadenNo += baseZahlschaden.scoreZahlschadenNo;
	   	  
	   	  var puffer = 5;
	   	  var evaluation = 1;
	   	  var totalScoreZahlschadenYes = Math.min(100,evaluation*Math.max(puffer, scoreZahlschadenYes));
	   	  var totalScoreZahlschadenNo = Math.min(100,Math.max(puffer, scoreZahlschadenNo));
	   	  
	   	  var zahlschaden = totalScoreZahlschadenYes/(totalScoreZahlschadenYes+totalScoreZahlschadenNo)*100;
	   	  var result ={
	   			zahlschaden : ''
	   	  }
	   	  if(!info.scoreNumberZahlschaden){
		    	  if(zahlschaden > 75 )
		    		  result.zahlschaden ='Ja';
		    	  else
		    		  result.zahlschaden ='Nein';
	   	  }else
	   		 result.zahlschaden = Math.round(zahlschaden);
	//   	   end calculator Zahlschaden
	   	  if(customer.vn_kfz_his)
	   		 result.zahlschaden ='Nein';
	   	  if(info.typeOfDamage == 'Wildunfall' && !info.collisionAnimal){
	   		  result.zahlschaden ='Nein';
	   	  }
//	   	  result.todoTextAccidents = todoTextAccidents;
	   	  return result;
	 }
	 assistantService.calculatorAccidentEinbruchdiebstahl = function(info,customer){
		 	
	    	var todoTextAccidents = [];
	    	var resultData ={
	    			scoreSchadeninformation : 0,
	    			scoreSchadenzusatzkosten : 0,
	    			scoreSchadenhergang : 0,
	    			scoreSchadensteuerung : 0,
	    			scoreUploadedFile : 0 ,
	    			scoreEnvironment : 0,
	    			zahlschaden : '' ,
	    			todoTextAccidents : [],
	    			needCalculatorZahlschadenInserver : true
	    	}
	    	
	    	// start calculator score Schadeninformation
	    	todoTextAccidents.push("Schadeninformation");
	    	var scoreDayOfAccident = 0;
	    	var scoreTimeOfAccident = 0;
	    	var scoreDateTime= calculatorScoreDateTimeNew(info);
	    	scoreDayOfAccident = scoreDateTime.scoreDayOfAccident;
	    	scoreTimeOfAccident = scoreDateTime.scoreTimeOfAccident;
	    	todoTextAccidents.push.apply(todoTextAccidents,scoreDateTime.todoTextAccidents);
	    	
	    	  
	    	  var scoreWitnessesAvailable = 0 ;
	    	  var witnessesAvailable = calculatorScoreWitnessesAvailable(info);
	    	  scoreWitnessesAvailable = witnessesAvailable.scoreWitnessesAvailable;
	    	  todoTextAccidents.push.apply(todoTextAccidents,witnessesAvailable.todoTextAccidents);
	    	 
	    	  var scorePoliceInformed = 0;
	    	  var policeInformed =  calculatorScorePoliceInformed(info);
	    	  scorePoliceInformed = policeInformed.scorePoliceInformed;
	    	  todoTextAccidents.push.apply(todoTextAccidents,policeInformed.todoTextAccidents);
	    	  
	    	  var scoreEnvironment = 0;
	    	  var environment =  calculatorScoreEnvironment(info);
	    	  scoreEnvironment = environment.scoreEnvironment;
	    	  todoTextAccidents.push.apply(todoTextAccidents,environment.todoTextAccidents);
	    	  
	    	  
	    	  //need
	    	  resultData.scoreSchadeninformation = ((scoreDayOfAccident + scoreTimeOfAccident + scoreWitnessesAvailable + scorePoliceInformed + scoreEnvironment ) / 5).toFixed(2);
	    	  // end calulator score Schadeninformation
	    	  
	    	  // start calculator Schadenzusatzkosten
	    	  todoTextAccidents.push("Schadenzusatzkosten");
	    	  var scoreVehicleReady = 0;
	    	  var vehicleReady = calculatorScoreVehicleReady(info);
	    	  scoreVehicleReady = vehicleReady.scoreVehicleReady;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleReady.todoTextAccidents);
	    	  
	    	  var scoreTowingService = 0;
	    	  var towingService = calculatorScoreTowingService(info);
	    	  scoreTowingService = towingService.scoreTowingService;
	    	  todoTextAccidents.push.apply(todoTextAccidents,towingService.todoTextAccidents);

	    	 
	    	  var scoreStolen = 0;
	    	  var stolen =  calculatorScoreStolenItem(info);
	    	  scoreStolen = stolen.scoreStolen;
	    	  
	    	  
	    	  
	    	  //need
	    	  if(!info.stolen)
	    		  resultData.scoreSchadenzusatzkosten = ((scoreVehicleReady + scoreTowingService   )/ 2).toFixed(2) ;
	    	  else
	    		  resultData.scoreSchadenzusatzkosten = ((scoreVehicleReady + scoreTowingService + scoreStolen  )/ 3).toFixed(2) ;
	    	  // end calculator score Schadenzusatzkosten
	    	  
	    	  // start calculator score Schadenhergang
	    
	    	  //end calculator Schadenhergang
	    	  
	    	  // start calculator Schadensteuerung
	    	  todoTextAccidents.push("Schadensteuerung");
	    	  var scoreDamageLevel = 0;
	    	  var damageLevel = calculatorScoreDamageLevel(info);
	    	  scoreDamageLevel = damageLevel.scoreDamageLevel;
	    	  todoTextAccidents.push.apply(todoTextAccidents,damageLevel.todoTextAccidents);

	    	  var scoreVehicleRepaired = 0;
	    	  var vehicleRepaired = calculatorVehicleRepaired(info);
	    	  scoreVehicleRepaired = vehicleRepaired.scoreVehicleRepaired;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleRepaired.todoTextAccidents);

	    	  //need
	    	  resultData.scoreSchadensteuerung = ((scoreDamageLevel + scoreVehicleRepaired)/2).toFixed(2) ;
	    	  // end calculator Schadensteuerung
	    	  
	    	  //start calculator Datenumfang
	    	  todoTextAccidents.push("Datenumfang");
	    	  var scoreUploadedFile = 0;
	    	  var uploadedFile = calculatorScoreUploadedFile(info);
	    	  scoreUploadedFile = uploadedFile.scoreUploadedFile;
	    	  todoTextAccidents.push.apply(todoTextAccidents,uploadedFile.todoTextAccidents);
	    	
	    	  resultData.scoreUploadedFile = scoreUploadedFile.toFixed(2) ;  
	    	  // end calculator Datenumfang
	    	  
	    	  
	    	  var stolenItem = calculatorScoreStolenItem(info);
	    	  todoTextAccidents.push.apply(todoTextAccidents,stolenItem.todoTextAccidents);
	    	  
	    	// start calculator Zahlschaden
    		  var baseZahlschaden =  calculatorZahlschaden(info,customer);
    		  resultData.zahlschaden = baseZahlschaden.zahlschaden;
    		  
	    	  resultData.todoTextAccidents = todoTextAccidents;
	    	  
	    	  return resultData;
	    }
	 
	 assistantService.calculatorAccidentVandalismus = function(info,customer){
		 	
	    	var todoTextAccidents = [];
	    	var resultData ={
	    			scoreSchadeninformation : 0,
	    			scoreSchadenzusatzkosten : 0,
	    			scoreSchadenhergang : 0,
	    			scoreSchadensteuerung : 0,
	    			scoreUploadedFile : 0 ,
	    			zahlschaden : '' ,
	    			todoTextAccidents : [],
	    			needCalculatorZahlschadenInserver : true
	    	}
	    	
	    	// start calculator score Schadeninformation
	    	todoTextAccidents.push("Schadeninformation");
	    	var scoreDayOfAccident = 0;
	    	var scoreTimeOfAccident = 0;
	    	var scoreDateTime= calculatorScoreDateTime(info);
	    	scoreDayOfAccident = scoreDateTime.scoreDayOfAccident;
	    	scoreTimeOfAccident = scoreDateTime.scoreTimeOfAccident;
	    	todoTextAccidents.push.apply(todoTextAccidents,scoreDateTime.todoTextAccidents);
	    	
	    	  
	    	  var scoreWitnessesAvailable = 0 ;
	    	  var witnessesAvailable = calculatorScoreWitnessesAvailable(info);
	    	  scoreWitnessesAvailable = witnessesAvailable.scoreWitnessesAvailable;
	    	  todoTextAccidents.push.apply(todoTextAccidents,witnessesAvailable.todoTextAccidents);
	    	 
	    	  var scorePoliceInformed = 0;
	    	  var policeInformed =  calculatorScorePoliceInformed(info);
	    	  scorePoliceInformed = policeInformed.scorePoliceInformed;
	    	  todoTextAccidents.push.apply(todoTextAccidents,policeInformed.todoTextAccidents);
	    	  
	    	  var scoreEnvironment = 0;
	    	  var environment =  calculatorScoreEnvironmentVandalismus(info);
	    	  scoreEnvironment = environment.scoreEnvironment;
	    	  todoTextAccidents.push.apply(todoTextAccidents,environment.todoTextAccidents);
 	  
	    	  //need
	    	  resultData.scoreSchadeninformation = ((scoreDayOfAccident + scoreTimeOfAccident + scoreWitnessesAvailable + scorePoliceInformed + scoreEnvironment + scorePoliceInformed) / 6).toFixed(2);
	    	  // end calulator score Schadeninformation
	    	  
	    	  // start calculator Schadenzusatzkosten
	    	  todoTextAccidents.push("Schadenzusatzkosten");
	    	  var scoreVehicleReady = 0;
	    	  var vehicleReady = calculatorScoreVehicleReady(info);
	    	  scoreVehicleReady = vehicleReady.scoreVehicleReady;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleReady.todoTextAccidents);
	    	  
	    	  var scoreTowingService = 0;
	    	  var towingService = calculatorScoreTowingService(info);
	    	  scoreTowingService = towingService.scoreTowingService;
	    	  todoTextAccidents.push.apply(todoTextAccidents,towingService.todoTextAccidents);

	    	  
	    	  //need
	    	  resultData.scoreSchadenzusatzkosten = ((scoreVehicleReady + scoreTowingService  )/ 2).toFixed(2) ;
	    	  // end calculator score Schadenzusatzkosten
	    	  
	    	  // start calculator score Schadenhergang
	    	  
	    	  
	    	  var scoreDescribeYourDamageItem = 0;
	    	  var describeYourDamageItem = calculatorDescribeYourDamageItem(info);
	    	  scoreDescribeYourDamageItem = describeYourDamageItem.scoreDescribeYourDamageItem;
	    	  todoTextAccidents.push.apply(todoTextAccidents,describeYourDamageItem.todoTextAccidents);

	    	  resultData.scoreSchadenhergang = scoreDescribeYourDamageItem.toFixed(2);
	    	  //end calculator Schadenhergang
	    	  
	    	  // start calculator Schadensteuerung
	    	  todoTextAccidents.push("Schadensteuerung");
	    	  var scoreDamageLevel = 0;
	    	  var damageLevel = calculatorScoreDamageLevel(info);
	    	  scoreDamageLevel = damageLevel.scoreDamageLevel;
	    	  todoTextAccidents.push.apply(todoTextAccidents,damageLevel.todoTextAccidents);

	    	  var scoreVehicleRepaired = 0;
	    	  var vehicleRepaired = calculatorVehicleRepaired(info);
	    	  scoreVehicleRepaired = vehicleRepaired.scoreVehicleRepaired;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleRepaired.todoTextAccidents);

	    	  //need
	    	  resultData.scoreSchadensteuerung = ((scoreDamageLevel + scoreVehicleRepaired)/2).toFixed(2) ;
	    	  // end calculator Schadensteuerung
	    	  
	    	  //start calculator Datenumfang
	    	  todoTextAccidents.push("Datenumfang");
	    	  var scoreUploadedFile = 0;
	    	  var uploadedFile = calculatorScoreUploadedFile(info);
	    	  scoreUploadedFile = uploadedFile.scoreUploadedFile;
	    	  todoTextAccidents.push.apply(todoTextAccidents,uploadedFile.todoTextAccidents);
	    	
	    	  resultData.scoreUploadedFile = scoreUploadedFile.toFixed(2) ;  
	    	  // end calculator Datenumfang
	    	  
	    	  // start calculator Zahlschaden
	    	  //to do calculator in server
	    	  var baseZahlschaden =  calculatorZahlschaden(info,customer);
    		  resultData.zahlschaden = baseZahlschaden.zahlschaden;
	    	  // end calculator Zahlschaden

	    	  resultData.todoTextAccidents = todoTextAccidents;
	    	  
	    	  return resultData;
	    }
	 
	 assistantService.calculatorAccidentSturm = function(info,customer){
		 	
	    	var todoTextAccidents = [];
	    	var resultData ={
	    			scoreSchadeninformation : 0,
	    			scoreSchadenzusatzkosten : 0,
	    			scoreSchadenhergang : 0,
	    			scoreSchadensteuerung : 0,
	    			scoreUploadedFile : 0 ,
	    			scoreEnvironment : 0,
	    			zahlschaden : '' ,
	    			todoTextAccidents : [],
	    			needCalculatorZahlschadenInserver : true
	    	}
	    	
	    	// start calculator score Schadeninformation
	    	todoTextAccidents.push("Schadeninformation");
	    	var scoreDayOfAccident = 0;
	    	var scoreTimeOfAccident = 0;
	    	var scoreDateTime= calculatorScoreDateTime(info);
	    	scoreDayOfAccident = scoreDateTime.scoreDayOfAccident;
	    	scoreTimeOfAccident = scoreDateTime.scoreTimeOfAccident;
	    	todoTextAccidents.push.apply(todoTextAccidents,scoreDateTime.todoTextAccidents);
	    	
	    	 var scoreWhoDriving = 0 ;
	    	 var whoDriving = calculatorWhoDriving(info);
	    	 scoreWhoDriving = whoDriving.scoreWhoDriving;
	    	 todoTextAccidents.push.apply(todoTextAccidents,whoDriving.todoTextAccidents);
	    	 
	    	  var scoreValidDrivingLicense = 0;
	    	  var validDrivingLicense = calculatorScoreValidDrivingLicense(info);
	    	  scoreValidDrivingLicense = validDrivingLicense.scoreValidDrivingLicense;
	    	  todoTextAccidents.push.apply(todoTextAccidents,validDrivingLicense.todoTextAccidents);
	    	
	    	  
	    	  var scoreInfluence = 0 ;
	    	  var influence = calculatoScoreInfluence(info); 
	    	  scoreInfluence = influence.scoreInfluence;
	    	  todoTextAccidents.push.apply(todoTextAccidents,influence.todoTextAccidents);
	    	  
	    	  var scoreWitnessesAvailable = 0 ;
	    	  var witnessesAvailable = calculatorScoreWitnessesAvailable(info);
	    	  scoreWitnessesAvailable = witnessesAvailable.scoreWitnessesAvailable;
	    	  todoTextAccidents.push.apply(todoTextAccidents,witnessesAvailable.todoTextAccidents);
	    	 
	    	  var scorePoliceInformed = 0;
	    	  var policeInformed =  calculatorScorePoliceInformed(info);
	    	  scorePoliceInformed = policeInformed.scorePoliceInformed;
	    	  todoTextAccidents.push.apply(todoTextAccidents,policeInformed.todoTextAccidents);
	    	  
	    	  var scoreEnvironment = 0;
	    	  var environment =  calculatorScoreEnvironmentSturm(info);
	    	  scoreEnvironment = environment.scoreEnvironment;
	    	  todoTextAccidents.push.apply(todoTextAccidents,environment.todoTextAccidents);
	    	  
	     	  var scoreVehicleDamagedItem = 0;
	    	  var vehicleDamagedItem =  calculatorScoreVehicleDamagedItem(info);
	    	  scoreVehicleDamagedItem = vehicleDamagedItem.scoreVehicleDamagedItem;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleDamagedItem.todoTextAccidents);
	    	  
	    	  
	    	  //need
	    	  resultData.scoreSchadeninformation = ((scoreDayOfAccident + scoreTimeOfAccident + scoreWhoDriving + scoreValidDrivingLicense + scoreInfluence + scoreWitnessesAvailable + scorePoliceInformed + scoreEnvironment + scoreVehicleDamagedItem) / 9).toFixed(2);
	    	  // end calulator score Schadeninformation
	    	  
	    	  // start calculator Schadenzusatzkosten
	    	  todoTextAccidents.push("Schadenzusatzkosten");
	    	  var scoreVehicleReady = 0;
	    	  var vehicleReady = calculatorScoreVehicleReady(info);
	    	  scoreVehicleReady = vehicleReady.scoreVehicleReady;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleReady.todoTextAccidents);
	    	  
	    	  var scoreTowingService = 0;
	    	  var towingService = calculatorScoreTowingService(info);
	    	  scoreTowingService = towingService.scoreTowingService;
	    	  todoTextAccidents.push.apply(todoTextAccidents,towingService.todoTextAccidents);
	    	  
	    	  var scoreInjuredPeople = 0 ;
	    	  var  injuredPeople = calculatorScoreInjuredPeople(info);
	    	  scoreInjuredPeople = injuredPeople.scoreInjuredPeople ;
	    	  todoTextAccidents.push.apply(todoTextAccidents,injuredPeople.todoTextAccidents);
	    	  
	    	  var scoreVehicleParked = 0 ;
	    	  var  vehicleParked = calculatorVehicleParked(info);
	    	  scoreVehicleParked = vehicleParked.scoreVehicleParked ;
	    	  
	    	  
	    	  //need
	    	  resultData.scoreSchadenzusatzkosten = ((scoreVehicleReady + scoreTowingService + scoreInjuredPeople + scoreVehicleParked)/ 4).toFixed(2) ;
	    	  // end calculator score Schadenzusatzkosten
	    	  
	    	  // start calculator score Schadenhergang
	    
	    	  //end calculator Schadenhergang
	    	  
	    	  // start calculator Schadensteuerung
	    	  todoTextAccidents.push("Schadensteuerung");
	    	  var scoreDamageLevel = 0;
	    	  var damageLevel = calculatorScoreDamageLevel(info);
	    	  scoreDamageLevel = damageLevel.scoreDamageLevel;
	    	  todoTextAccidents.push.apply(todoTextAccidents,damageLevel.todoTextAccidents);

	    	  var scoreVehicleRepaired = 0;
	    	  var vehicleRepaired = calculatorVehicleRepaired(info);
	    	  scoreVehicleRepaired = vehicleRepaired.scoreVehicleRepaired;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleRepaired.todoTextAccidents);

	    	  //need
	    	  resultData.scoreSchadensteuerung = ((scoreDamageLevel + scoreVehicleRepaired)/2).toFixed(2) ;
	    	  // end calculator Schadensteuerung
	    	  
	    	  //start calculator Datenumfang
	    	  todoTextAccidents.push("Datenumfang");
	    	  var scoreUploadedFile = 0;
	    	  var uploadedFile = calculatorScoreUploadedFile(info);
	    	  scoreUploadedFile = uploadedFile.scoreUploadedFile;
	    	  todoTextAccidents.push.apply(todoTextAccidents,uploadedFile.todoTextAccidents);
	    	
	    	  resultData.scoreUploadedFile = scoreUploadedFile.toFixed(2) ;  
	    	  // end calculator Datenumfang
	    	  
	    	  // start calculator Zahlschaden
	    	  // to do create in server
//	    		  resultData.zahlschaden ='Nein';
	    	  var baseZahlschaden =  calculatorZahlschaden(info,customer);
    		  resultData.zahlschaden = baseZahlschaden.zahlschaden;
	    	  // end calculator Zahlschaden

	    	  resultData.todoTextAccidents = todoTextAccidents;
	    	  
	    	  return resultData;
	    }
	 assistantService.calculatorAccidentGlas = function(info,customer){
		 	
	    	var todoTextAccidents = [];
	    	var resultData ={
	    			scoreSchadeninformation : 0,
	    			scoreSchadenzusatzkosten : 0,
	    			scoreSchadenhergang : 0,
	    			scoreSchadensteuerung : 0,
	    			scoreUploadedFile : 0 ,
	    			scoreEnvironment : 0,
	    			zahlschaden : '' ,
	    			todoTextAccidents : [],
	    			needCalculatorZahlschadenInserver : true
	    	}
	    	
	    	// start calculator score Schadeninformation
	    	todoTextAccidents.push("Schadeninformation");
	    	var scoreDayOfAccident = 0;
	    	var scoreTimeOfAccident = 0;
	    	var scoreDateTime= calculatorScoreDateTimeNew(info);
	    	scoreDayOfAccident = scoreDateTime.scoreDayOfAccident;
	    	scoreTimeOfAccident = scoreDateTime.scoreTimeOfAccident;
	    	todoTextAccidents.push.apply(todoTextAccidents,scoreDateTime.todoTextAccidents);
	    	
	    	  
	    	  var scoreWitnessesAvailable = 0 ;
	    	  var witnessesAvailable = calculatorScoreWitnessesAvailable(info);
	    	  scoreWitnessesAvailable = witnessesAvailable.scoreWitnessesAvailable;
	    	  todoTextAccidents.push.apply(todoTextAccidents,witnessesAvailable.todoTextAccidents);
	    	 
	    	  var scorePoliceInformed = 0;
	    	  var policeInformed =  calculatorScorePoliceInformed(info);
	    	  scorePoliceInformed = policeInformed.scorePoliceInformed;
	    	  todoTextAccidents.push.apply(todoTextAccidents,policeInformed.todoTextAccidents);
	    	  
	    	  var scoreEnvironment = 0;
	    	  var environment =  calculatorScoreEnvironment(info);
	    	  scoreEnvironment = environment.scoreEnvironment;
	    	  todoTextAccidents.push.apply(todoTextAccidents,environment.todoTextAccidents);
	    	  
	    	  //need
	    	  resultData.scoreSchadeninformation = ((scoreDayOfAccident + scoreTimeOfAccident + scoreWitnessesAvailable + scorePoliceInformed + scoreEnvironment) / 5).toFixed(2);
	    	  // end calulator score Schadeninformation
	    	  
	    	  // start calculator Schadenzusatzkosten
	    	  todoTextAccidents.push("Schadenzusatzkosten");
	    	  var scoreVehicleReady = 0;
	    	  var vehicleReady = calculatorScoreVehicleReady(info);
	    	  scoreVehicleReady = vehicleReady.scoreVehicleReady;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleReady.todoTextAccidents);
	    	  
	    	  var scoreTowingService = 0;
	    	  var towingService = calculatorScoreTowingService(info);
	    	  scoreTowingService = towingService.scoreTowingService;
	    	  todoTextAccidents.push.apply(todoTextAccidents,towingService.todoTextAccidents);
	    	  
	    	  //need
	    	  resultData.scoreSchadenzusatzkosten = ((scoreVehicleReady + scoreTowingService   )/ 2).toFixed(2) ;
	    	  // end calculator score Schadenzusatzkosten
	    	  
	    	  // start calculator score Schadenhergang
	    
	    	  //end calculator Schadenhergang
	    	  
	    	  // start calculator Schadensteuerung
	    	  todoTextAccidents.push("Schadensteuerung");
	    	  var scoreDamageLevel = 0;
	    	  var damageLevel = calculatorScoreDamageLevel(info);
	    	  scoreDamageLevel = damageLevel.scoreDamageLevel;
	    	  todoTextAccidents.push.apply(todoTextAccidents,damageLevel.todoTextAccidents);

	    	  var scoreVehicleRepaired = 0;
	    	  var vehicleRepaired = calculatorVehicleRepaired(info);
	    	  scoreVehicleRepaired = vehicleRepaired.scoreVehicleRepaired;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleRepaired.todoTextAccidents);

	    	  //need
	    	  resultData.scoreSchadensteuerung = ((scoreDamageLevel + scoreVehicleRepaired)/2).toFixed(2) ;
	    	  // end calculator Schadensteuerung
	    	  
	    	  //start calculator Datenumfang
	    	  todoTextAccidents.push("Datenumfang");
	    	  var scoreUploadedFile = 0;
	    	  var uploadedFile = calculatorScoreUploadedFile(info);
	    	  scoreUploadedFile = uploadedFile.scoreUploadedFile;
	    	  todoTextAccidents.push.apply(todoTextAccidents,uploadedFile.todoTextAccidents);
	    	
	    	  resultData.scoreUploadedFile = scoreUploadedFile.toFixed(2) ;  
	    	  // end calculator Datenumfang
	    	  
	    	  // start calculator Zahlschaden
	    	  
	    	  var baseZahlschaden =  calculatorZahlschaden(info,customer);
    		  resultData.zahlschaden = baseZahlschaden.zahlschaden;
	    	  // end calculator Zahlschaden
	    	  
	    	  resultData.todoTextAccidents = todoTextAccidents;
	    	  
	    	  return resultData;
	    }

	 assistantService.calculatorAccidentHagel = function(info,customer){
		 	
	    	var todoTextAccidents = [];
	    	var resultData ={
	    			scoreSchadeninformation : 0,
	    			scoreSchadenzusatzkosten : 0,
	    			scoreSchadenhergang : 0,
	    			scoreSchadensteuerung : 0,
	    			scoreUploadedFile : 0 ,
	    			scoreEnvironment : 0,
	    			zahlschaden : '' ,
	    			todoTextAccidents : [],
	    			needCalculatorZahlschadenInserver : true
	    	}
	    	
	    	// start calculator score Schadeninformation
	    	todoTextAccidents.push("Schadeninformation");
	    	var scoreDayOfAccident = 0;
	    	var scoreTimeOfAccident = 0;
	    	var scoreDateTime= calculatorScoreDateTimeNew(info);
	    	scoreDayOfAccident = scoreDateTime.scoreDayOfAccident;
	    	scoreTimeOfAccident = scoreDateTime.scoreTimeOfAccident;
	    	todoTextAccidents.push.apply(todoTextAccidents,scoreDateTime.todoTextAccidents);
  
	    	  var scoreEnvironment = 0;
	    	  var environment =  calculatorScoreEnvironmentHagel(info);
	    	  scoreEnvironment = environment.scoreEnvironment;
	    	  todoTextAccidents.push.apply(todoTextAccidents,environment.todoTextAccidents);
	    	  
	    	  var scoreDamageEarlier = 0;
	    	  var damageEarlier =  calculatorScoreDamageEarlierTimes(info);
	    	  scoreDamageEarlier = damageEarlier.score;
	    	  todoTextAccidents.push.apply(todoTextAccidents,damageEarlier.todoTextAccidents);
	    	  
	    	  //need
	    	  resultData.scoreSchadeninformation = ((scoreDayOfAccident + scoreTimeOfAccident + scoreDamageEarlier + scoreEnvironment) / 4).toFixed(2);
	    	  // end calulator score Schadeninformation
	    	  
	    	  // start calculator Schadensteuerung
	    	  todoTextAccidents.push("Schadensteuerung");
	    	  var scoreDamageLevel = 0;
	    	  var damageLevel = calculatorScoreDamageLevel(info);
	    	  scoreDamageLevel = damageLevel.scoreDamageLevel;
	    	  todoTextAccidents.push.apply(todoTextAccidents,damageLevel.todoTextAccidents);

	    	  var scoreVehicleRepaired = 0;
	    	  var vehicleRepaired = calculatorVehicleRepaired(info);
	    	  scoreVehicleRepaired = vehicleRepaired.scoreVehicleRepaired;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleRepaired.todoTextAccidents);

	    	  //need
	    	  resultData.scoreSchadensteuerung = ((scoreDamageLevel + scoreVehicleRepaired)/2).toFixed(2) ;
	    	  // end calculator Schadensteuerung
	    	  
	    	  //start calculator Datenumfang
	    	  todoTextAccidents.push("Datenumfang");
	    	  var scoreUploadedFile = 0;
	    	  var uploadedFile = calculatorScoreUploadedFile(info);
	    	  scoreUploadedFile = uploadedFile.scoreUploadedFile;
	    	  todoTextAccidents.push.apply(todoTextAccidents,uploadedFile.todoTextAccidents);
	    	
	    	  resultData.scoreUploadedFile = scoreUploadedFile.toFixed(2) ;  
	    	  // end calculator Datenumfang
	    	  
	    	  // start calculator Zahlschaden
	    	  
	    	  
//	    	  todoTextAccidents.push("Zahlschaden");
	    	  var baseZahlschaden =  calculatorZahlschaden(info,customer);
    		  resultData.zahlschaden = baseZahlschaden.zahlschaden;
	    	  // end calculator Zahlschaden
	    	  
	    	  resultData.todoTextAccidents = todoTextAccidents;
	    	  
	    	  return resultData;
	    }
	 
	 assistantService.calculatorAccidentWildunfall = function(info,customer){
		 	
	    	var todoTextAccidents = [];
	    	var resultData ={
	    			scoreSchadeninformation : 0,
	    			scoreSchadenzusatzkosten : 0,
	    			scoreSchadenhergang : 0,
	    			scoreSchadensteuerung : 0,
	    			scoreUploadedFile : 0 ,
	    			scoreEnvironment : 0,
	    			zahlschaden : '' ,
	    			todoTextAccidents : [],
	    			needCalculatorZahlschadenInserver : true
	    	}
	    	
	    	// start calculator score Schadeninformation
	    	todoTextAccidents.push("Schadeninformation");
	    	var scoreDayOfAccident = 0;
	    	var scoreTimeOfAccident = 0;
	    	var scoreDateTime= calculatorScoreDateTimeNew(info);
	    	scoreDayOfAccident = scoreDateTime.scoreDayOfAccident;
	    	scoreTimeOfAccident = scoreDateTime.scoreTimeOfAccident;
	    	todoTextAccidents.push.apply(todoTextAccidents,scoreDateTime.todoTextAccidents);
	    	
	    	 var scoreWhoDriving = 0 ;
	    	 var whoDriving = calculatorWhoDriving(info);
	    	 scoreWhoDriving = whoDriving.scoreWhoDriving;
	    	 todoTextAccidents.push.apply(todoTextAccidents,whoDriving.todoTextAccidents);
	    	
	    	  var scoreValidDrivingLicense = 0;
	    	  var validDrivingLicense = calculatorScoreValidDrivingLicense(info);
	    	  scoreValidDrivingLicense = validDrivingLicense.scoreValidDrivingLicense;
	    	  todoTextAccidents.push.apply(todoTextAccidents,validDrivingLicense.todoTextAccidents);
	    	
	    	  var scoreInfluence = 0 ;
	    	  var influence = calculatoScoreInfluence(info); 
	    	  scoreInfluence = influence.scoreInfluence;
	    	  todoTextAccidents.push.apply(todoTextAccidents,influence.todoTextAccidents);
	    	  
	    	  
	    	  var scoreWitnessesAvailable = 0 ;
	    	  var witnessesAvailable = calculatorScoreWitnessesAvailable(info);
	    	  scoreWitnessesAvailable = witnessesAvailable.scoreWitnessesAvailable;
	    	  todoTextAccidents.push.apply(todoTextAccidents,witnessesAvailable.todoTextAccidents);
	    	 
	    	  var scorePoliceInformed = 0;
	    	  var policeInformed =  calculatorScorePoliceInformed(info);
	    	  scorePoliceInformed = policeInformed.scorePoliceInformed;
	    	  todoTextAccidents.push.apply(todoTextAccidents,policeInformed.todoTextAccidents);
	    	  
	    	  var scoreEnvironment = 0;
	    	  var environment =  calculatorScoreEnvironment(info);
	    	  scoreEnvironment = environment.scoreEnvironment;
	    	  todoTextAccidents.push.apply(todoTextAccidents,environment.todoTextAccidents);
	    	  
	    	  var scoreForesterInformed = 0;
	    	  var foresterInformed =  calculatorScoreForesterInformed(info);
	    	  scoreForesterInformed = foresterInformed.score;
	    	  
	    	  var scoreWildCasualtyCertificate = 0;
	    	  var wildCasualtyCertificate =  calculatoWildCasualtyCertificate(info);
	    	  scoreWildCasualtyCertificate = wildCasualtyCertificate.score;
	    	  todoTextAccidents.push.apply(todoTextAccidents,wildCasualtyCertificate.todoTextAccidents);
	    	  
	    	  
	    	  
	    	  //need
	    	  resultData.scoreSchadeninformation = ((scoreDayOfAccident + scoreTimeOfAccident + scoreWhoDriving + scoreValidDrivingLicense + scoreInfluence + scoreWitnessesAvailable + scorePoliceInformed +scoreEnvironment +scoreForesterInformed + scoreWildCasualtyCertificate) / 10).toFixed(2);
	    	  // end calulator score Schadeninformation
	    	  
	    	  // start calculator Schadenzusatzkosten
	    	  todoTextAccidents.push("Schadenzusatzkosten");
	    	  var scoreInjuredPeople = 0 ;
	    	  var  injuredPeople = calculatorScoreInjuredPeople(info);
	    	  scoreInjuredPeople = injuredPeople.scoreInjuredPeople ;
	    	  todoTextAccidents.push.apply(todoTextAccidents,injuredPeople.todoTextAccidents);
	    	  
	    	  //need
	    	  resultData.scoreSchadenzusatzkosten = scoreInjuredPeople.toFixed(2) ;
	    	  // end calculator score Schadenzusatzkosten
	    	  
	    	  // start calculator score Schadenhergang
	     	  todoTextAccidents.push("Schadenhergang");
	     	  var scoreAnimal = 0;
	    	  var animal = calculatorScoreAnimal(info);
	    	  scoreAnimal = animal.score;
	    	  todoTextAccidents.push.apply(todoTextAccidents,animal.todoTextAccidents);
	    	  
	    	  var scoreCollisionAnimal = 0;
	    	  var collisionAnimal = calculatoCollisionAnimal(info);
	    	  scoreCollisionAnimal = collisionAnimal.score;
	    	  
	    	  //need
	    	  resultData.scoreSchadenhergang = ((scoreAnimal + scoreCollisionAnimal)/2).toFixed(2);
	    	  //end calculator Schadenhergang
	    	  
	    	  // start calculator Schadensteuerung
	    	  todoTextAccidents.push("Schadensteuerung");
	    	  var scoreDamageLevel = 0;
	    	  var damageLevel = calculatorScoreDamageLevel(info);
	    	  scoreDamageLevel = damageLevel.scoreDamageLevel;
	    	  todoTextAccidents.push.apply(todoTextAccidents,damageLevel.todoTextAccidents);

	    	  var scoreVehicleRepaired = 0;
	    	  var vehicleRepaired = calculatorVehicleRepaired(info);
	    	  scoreVehicleRepaired = vehicleRepaired.scoreVehicleRepaired;
	    	  todoTextAccidents.push.apply(todoTextAccidents,vehicleRepaired.todoTextAccidents);

	    	  //need
	    	  resultData.scoreSchadensteuerung = ((scoreDamageLevel + scoreVehicleRepaired)/2).toFixed(2) ;
	    	  // end calculator Schadensteuerung
	    	  
	    	  //start calculator Datenumfang
	    	  todoTextAccidents.push("Datenumfang");
	    	  var scoreUploadedFile = 0;
	    	  var uploadedFile = calculatorScoreUploadedFile(info);
	    	  scoreUploadedFile = uploadedFile.scoreUploadedFile;
	    	  todoTextAccidents.push.apply(todoTextAccidents,uploadedFile.todoTextAccidents);
	    	
	    	  resultData.scoreUploadedFile = scoreUploadedFile.toFixed(2) ;  
	    	  // end calculator Datenumfang
	    	  
	    	  // start calculator Zahlschaden
	    	
	    	  var baseZahlschaden =  calculatorZahlschaden(info,customer);
    		  resultData.zahlschaden = baseZahlschaden.zahlschaden;
	    	  resultData.todoTextAccidents = todoTextAccidents;
	    	  
	    	  return resultData;
	    }
	 
	 assistantService.calculatorAccidentSonstiges = function(info,customer){
		 	
	    	var todoTextAccidents = [];
	    	var resultData ={
	    			scoreSchadeninformation : 0,
	    			scoreSchadenzusatzkosten : 0,
	    			scoreSchadenhergang : 0,
	    			scoreSchadensteuerung : 0,
	    			scoreUploadedFile : 0 ,
	    			scoreEnvironment : 0,
	    			zahlschaden : '' ,
	    			todoTextAccidents : [],
	    			needCalculatorZahlschadenInserver : true
	    	}
	    	
	    	// start calculator score Schadeninformation
	    	todoTextAccidents.push("Schadeninformation");
	    	var scoreDayOfAccident = 0;
	    	var scoreTimeOfAccident = 0;
	    	var scoreDateTime= calculatorScoreDateTime(info);
	    	scoreDayOfAccident = scoreDateTime.scoreDayOfAccident;
	    	scoreTimeOfAccident = scoreDateTime.scoreTimeOfAccident;
	    	todoTextAccidents.push.apply(todoTextAccidents,scoreDateTime.todoTextAccidents);
	    
    	  var scoreEnvironment = 0;
    	  var environment =  calculatorScoreEnvironmentSonstiges(info);
    	  scoreEnvironment = environment.scoreEnvironment;
    	  todoTextAccidents.push.apply(todoTextAccidents,environment.todoTextAccidents);
	    	  
	    	
	    	  
	    	  //need
	    	  resultData.scoreSchadeninformation = ((scoreDayOfAccident + scoreTimeOfAccident +scoreEnvironment ) / 3).toFixed(2);
	    	  // end calulator score Schadeninformation
	    	  //trung
	    	  // start calculator Schadenzusatzkosten
	    	  todoTextAccidents.push("Schadenzusatzkosten");
	    	  var scoreVehicleReady = 0 ;
	    	  var towingService = 0;
	    	  if(info.vehicleReady)
	    		  scoreVehicleReady = 93;
    		  else{
    			  scoreVehicleReady = 50;
    			  todoTextAccidents.push("Deckung prügen ggf. bitte Unfallersatz-/werkstattwagen anbieten");
    			  if(!info.towingService)
    				  towingService = 93;
    			  else{
    				  towingService = 50;
    				  todoTextAccidents.push("Bitte Abschleppdienst beauftragen");
    			  }
    		  }
	    	
	    	  
	    	  //need
	    	  resultData.scoreSchadenzusatzkosten = ((scoreVehicleReady + towingService)/2).toFixed(2) ;
	    	  // end calculator score Schadenzusatzkosten
	    	  
	    	  // start calculator score Schadenhergang
	     	  todoTextAccidents.push("Schadenhergang");
	     	  var whatHappenedOthers = 0;
	    	  if(info.whatHappenedOthers){
	    		  if(info.whatHappenedOthers == 'Blitz')
	    			  whatHappenedOthers = 60;
	    		  if(info.whatHappenedOthers == 'Brand'){
	    			  whatHappenedOthers = 60;
	    			  todoTextAccidents.push("Brandschaden ggf. Brandsachverständigen beauftragen.");
	    		  }
	    		  if(info.whatHappenedOthers == 'Kabelschmorschaden'){
	    			  whatHappenedOthers = 60;
	    			  todoTextAccidents.push("Kabelschmorschaden ggf. Brandsachverständigen beauftragen.");
	    		  }
	    		  if(info.whatHappenedOthers == 'Lawine'){
	    			  whatHappenedOthers = 85;
	    		  }
	    		  if(info.whatHappenedOthers == 'Marderbiss'){
	    			  whatHappenedOthers = 85;
	    		  }
	    		  if(info.whatHappenedOthers == 'Überschwemmung'){
	    			  whatHappenedOthers = 85;
	    			  todoTextAccidents.push("Achtung Überschwemmungsshaden. Es könnte ein Totalschaden vorliegen. Ggf. Sachverständigen beauftragen.");
	    		  }
	    		  if(info.whatHappenedOthers == 'Überspannung'){
	    			  whatHappenedOthers = 60;
	    			  todoTextAccidents.push("Achtung Überspannungsschaden.");
	    		  }
	    		  if(info.whatHappenedOthers == 'Sonstiges (Freitext)'){
	    			  whatHappenedOthers = 20;
	    			  todoTextAccidents.push("Achtung Schadenart prüfen.");
	    		  }
	    	  }
	    	  //need
	    	  resultData.scoreSchadenhergang = whatHappenedOthers.toFixed(2);
	    	  //end calculator Schadenhergang
	    	  
	    	  // start calculator Schadensteuerung
	    	  todoTextAccidents.push("Schadensteuerung");
	    	  var scoreDamageLevel = 0;
	    	  var damageLevel = calculatorScoreDamageLevel(info);
	    	  scoreDamageLevel = damageLevel.scoreDamageLevel;
	    	  todoTextAccidents.push.apply(todoTextAccidents,damageLevel.todoTextAccidents);
	    	  
	    	  var scoreVehicleRepaired = 0;
	    	  if(info.damageLevel !='Rechnung' && info.vehicleRepaired ){
	    		  scoreVehicleRepaired = 75;
	    		  todoTextAccidents.push("Kunde wünscht Reparatur. Bitte Kontakt für Werkstattsteuerung aufnehmen.");
	    	  }
	    	  
	    	  if(info.damageLevel !='Rechnung' && info.vehicleRepaired && info.werkstattnetz){
	    		  scoreVehicleRepaired = 92;
	    		  var address = info.werkstattnetz.strasse + ", " + info.werkstattnetz.plz + " " + info.werkstattnetz.ort ;	
	    		  if (info.werkstattnetz.telefon){
	    		  	address += ", Tel.: " + info.werkstattnetz.telefon;
	    		  }
	    		  todoTextAccidents.push("Kunde wünscht Reparatur in Partnerwerkstatt "+info.werkstattnetz.name +", " +address);
	    	  }
	    	  if(info.damageLevel !='Rechnung' && !info.vehicleRepaired && info.moneyOfTheRepair){
	    		  scoreVehicleRepaired = 70;
	    		  todoTextAccidents.push("VN wünscht fiktive Abrechnung.");
	    	  }
	    	  //need
	    	  resultData.scoreSchadensteuerung = ((scoreDamageLevel + scoreVehicleRepaired)/2).toFixed(2) ;
	    	  // end calculator Schadensteuerung
	    	  
	    	  //start calculator Datenumfang
	    	  todoTextAccidents.push("Datenumfang");
	    	  var scoreUploadedFile = 0;
	    	  var uploadedFile = calculatorScoreUploadedFile(info);
	    	  scoreUploadedFile = uploadedFile.scoreUploadedFile;
	    	  todoTextAccidents.push.apply(todoTextAccidents,uploadedFile.todoTextAccidents);
	    	
	    	  resultData.scoreUploadedFile = scoreUploadedFile.toFixed(2) ;  
	    	  // end calculator Datenumfang
	    	  
	    	  // start calculator Zahlschaden
	    	  var baseZahlschaden =  calculatorZahlschaden(info,customer);
    		  resultData.zahlschaden = baseZahlschaden.zahlschaden;
	    	  resultData.todoTextAccidents = todoTextAccidents;
	    	  return resultData;
	    }
	 assistantService.calculatorAccident = function(info,customer,unfall){
		 if(info.typeOfDamage && info.typeOfDamage === 'Unfall') 
			 return assistantService.calculatorAccidentUnfall(info,customer,unfall);
		 if(info.typeOfDamage && info.typeOfDamage === 'Einbruch / Diebstahl') 
			 return assistantService.calculatorAccidentEinbruchdiebstahl(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Vandalismus') 
			 return assistantService.calculatorAccidentVandalismus(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Glas') 
			 return assistantService.calculatorAccidentGlas(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Sturm') 
			 return assistantService.calculatorAccidentSturm(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Hagel') 
			 return assistantService.calculatorAccidentHagel(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Wildunfall') 
			 return assistantService.calculatorAccidentWildunfall(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Sonstiges') 
			 return assistantService.calculatorAccidentSonstiges(info,customer);
	 }
	 
	 return assistantService;
	    
});