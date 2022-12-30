var assistant = angular.module('trickery.services', ['ngResource']);
assistant.factory('TrickeryScoreService', function () {
	 var trickeryScoreService = {};
	 var checkBetweenRange = function(min,max,value){
		var result = false;
		if(value > min && value <= max)
			result = true;
		return result;
	 }   
	 var scoreStolen = function(info){
		  var scoreBlue = 0;
		  var scoreRed = 0;
		  var todoTexts = [];
		  var todoTextsBlues = [];
		  if(info.stolen){
			  for(item in info.stolenItem){
				  if(info.stolenItem[item]){
					  if(item == 'Radio/CD'){
						  scoreBlue += 20;
						  todoTextsBlues.push("Radio");
					  }
					  if(item == 'Navigationssystem'){
						  scoreBlue += 20;
						  todoTextsBlues.push("Navigation");
					  }
					  if(item == 'Airbag'){
						  scoreBlue += 20;
						  todoTextsBlues.push("Airbag");
					  }
				  }
			  }
   	  	  }

   	  return {scoreBlue : scoreBlue ,scoreRed : scoreRed , todoTexts : todoTexts, todoTextsBlues : todoTextsBlues};
	 };
	 var scoreVehicleReady = function(info){
		  var scoreBlue = 0;
		  var scoreRed = 0;
		  var todoTexts = [];
		  var todoTextsBlues = [];
		  if(!info.vehicleReady && !info.stolen && info.beenDamaged){
			  scoreRed = 60;
			  todoTexts.push("Vandalismusschaden");
  	  	  }
		  if(info.vehicleReady && !info.stolen && info.beenDamaged){
			  scoreRed = 50;
			  todoTexts.push("Vandalismusschaden");
  	  	  }

  	  return {scoreBlue : scoreBlue ,scoreRed : scoreRed , todoTexts : todoTexts , todoTextsBlues : todoTextsBlues};
	 };
	 var getTksb = function(customer){
		 var tksb = null;
		 if(customer.tkSb){
			  tksb = customer.tkSb.split(" ")[1];
		 }
		 return tksb;
	 }
	 var getVksb = function(customer){
		 var vkSb = null;
		 if(customer.vkSb){
			 vkSb = customer.vkSb.split(" ")[1];
		 }
		 return vkSb;
	 }
	 var scoreTksb = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 var tksb = getTksb(customer);
		 if(tksb && tksb == 0){
			 scoreBlue =+ 10;
			 todoTextsBlues.push("Ohne Selbstbeteiligung");
		 }
		 if(tksb && tksb > 150){
			 scoreRed =+ 20;
			 todoTexts.push("Hohe Selbstbeteiligung");
		 }
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues: todoTextsBlues};
	 }
	 
	 
	 var checkScoreStolenText = function(info,text){
		 if(!text)
			 text ="Sonstiges";
		 var result = false;
		 for(key  in info.stolenItem){
	    		if(info.stolenItem[key]){
	    			if(key == text)
	    				result =true;
	    		}	
	     }

		 return result;
	 }
	 var scoreSonstiges = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 var tksb = getTksb(customer);
		 var vksb = getVksb(customer);
		 var checkScoreStolenSonstiges = checkScoreStolenText(info);
		 var damageLength = 0;
		 for(key  in info.damageItems){
	    		if(info.damageItems[key])
	    			damageLength ++;
	     } 
		 if(damageLength >= 4 ){
			 if(tksb && info.beenDamaged && checkScoreStolenSonstiges && tksb <= 150  && customer.covertypLiability && customer.covertypPartiallyComprehensive && customer.covertypLiability  <= 150 && customer.covertypPartiallyComprehensive <= 150){
				 scoreRed += 30;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenSonstiges &&  checkBetweenRange(150,300,tksb) && customer.covertypLiability  && customer.covertypPartiallyComprehensive){
				 scoreRed += 20;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenSonstiges &&  checkBetweenRange(300,500,tksb)  && checkBetweenRange(300,500,customer.covertypLiability) && checkBetweenRange(300,500,customer.covertypPartiallyComprehensive)){
				 scoreRed += 15;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenSonstiges &&  checkBetweenRange(500,1000,tksb) && checkBetweenRange(500,1000,customer.covertypLiability) && checkBetweenRange(500,1000,customer.covertypPartiallyComprehensive) && info.rateDamage >=0 &&  info.rateDamage < 2000){
				 scoreRed += 10;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenSonstiges &&  checkBetweenRange(500,1000,tksb) && checkBetweenRange(500,1000,customer.covertypLiability) && checkBetweenRange(500,1000,customer.covertypPartiallyComprehensive) && info.rateDamage >= 2000){
				 scoreRed += 20;
				 todoTexts.push("Vandalismusschaden");
			 }
			 
			 if(tksb && info.beenDamaged && checkScoreStolenSonstiges &&  tksb == 0 && customer.covertypLiability  == 0 && customer.covertypFullyCoprehensiveInsurance == 0 ){
				 scoreRed += 5;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenSonstiges && checkBetweenRange(0,300,tksb) && checkBetweenRange(0,300,customer.covertypLiability)  && checkBetweenRange(0,300,customer.covertypFullyCoprehensiveInsurance) ){
				 scoreRed += 7;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenSonstiges &&  checkBetweenRange(300,500,tksb) && checkBetweenRange(300,500,customer.covertypLiability) && customer.covertypFullyCoprehensiveInsurance && customer.covertypFullyCoprehensiveInsurance <= 500 ){
				 scoreRed += 10;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenSonstiges &&  vksb && vksb <=500 &&  tksb > 500 && customer.covertypLiability && customer.covertypPartiallyComprehensive &&  customer.covertypLiability < 500  && customer.covertypFullyCoprehensiveInsurance < 500  ){
				 scoreRed += 10;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenSonstiges &&  vksb && vksb > 500  && tksb <=500 && customer.covertypLiability  >500   &&  customer.covertypFullyCoprehensiveInsurance  >500  ){
				 scoreRed += 10;
				 todoTexts.push("Vandalismusschaden");
			 }
				 
			if(tksb && info.beenDamaged && checkScoreStolenSonstiges &&  vksb && vksb > 500  && tksb > 500 && customer.covertypLiability  >500   &&  customer.covertypFullyCoprehensiveInsurance  >500  ){
				 scoreRed += 10;
				 todoTexts.push("Vandalismusschaden");
			 }
		 }
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts , todoTextsBlues : todoTextsBlues};
	 }
	 
	 var scoreRadio = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 var tksb = getTksb(customer);
		 var vksb = getVksb(customer);
		 var checkScoreStolenRadio = checkScoreStolenText(info,"Radio/CD");
		 var damageLength = 0;
		 for(key  in info.damageItems){
	    		if(info.damageItems[key])
	    			damageLength ++;
	     } 
		 if(damageLength >= 4 ){
			 if(tksb && info.beenDamaged && checkScoreStolenRadio && tksb <= 150 && customer.covertypLiability && customer.covertypPartiallyComprehensive && customer.covertypLiability  <= 150 && customer.covertypPartiallyComprehensive <= 150){
				 scoreRed += 20;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenRadio && checkBetweenRange(150,300,tksb) && checkBetweenRange(150,300,customer.covertypLiability) && checkBetweenRange(150,300,customer.covertypPartiallyComprehensive)){
				 scoreRed += 15;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenRadio && checkBetweenRange(300,500,tksb) && checkBetweenRange(300,500,customer.covertypLiability) &&  checkBetweenRange(300,500,customer.covertypPartiallyComprehensive)){
				 scoreRed += 10;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenRadio &&  checkBetweenRange(500,1000,tksb) && checkBetweenRange(500,1000,customer.covertypLiability) && checkBetweenRange(500,1000,customer.covertypPartiallyComprehensive) && info.rateDamage >=0 &&  info.rateDamage < 2000){
				 scoreRed += 8;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenRadio &&  checkBetweenRange(500,1000,tksb) && checkBetweenRange(500,1000,customer.covertypLiability) && checkBetweenRange(500,1000,customer.covertypPartiallyComprehensive) && info.rateDamage >= 2000){
				 scoreRed += 15;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenRadio &&  tksb == 0 && customer.covertypLiability  == 0 && customer.covertypFullyCoprehensiveInsurance == 0 ){
				 scoreRed += 5;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenRadio && checkBetweenRange(0,300,tksb) && checkBetweenRange(0,300,customer.covertypLiability) && checkBetweenRange(150,300,customer.covertypFullyCoprehensiveInsurance)  ){
				 scoreRed += 7;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenRadio &&  checkBetweenRange(300,500,tksb) && checkBetweenRange(300,500,customer.covertypLiability) && checkBetweenRange(300,500,customer.covertypFullyCoprehensiveInsurance)  ){
				 scoreRed += 10;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenRadio &&  vksb && vksb > 500 && tksb <= 500 && customer.covertypLiability && customer.covertypPartiallyComprehensive && customer.covertypLiability < 500   && customer.covertypFullyCoprehensiveInsurance < 500  ){
				 scoreRed += 10;
				 todoTexts.push("Vandalismusschaden");
			 }
			 if(tksb && info.beenDamaged && checkScoreStolenRadio &&  vksb && vksb > 500  && tksb > 500 && customer.covertypLiability && customer.covertypPartiallyComprehensive && customer.covertypLiability  <=500   &&  customer.covertypFullyCoprehensiveInsurance  <=500  ){
				 scoreRed += 10;
				 todoTexts.push("Vandalismusschaden");
			 };
			if(tksb && info.beenDamaged && checkScoreStolenRadio &&  vksb && vksb <= 500  && tksb > 500 && customer.covertypLiability && customer.covertypPartiallyComprehensive && customer.covertypLiability  <=500   &&  customer.covertypFullyCoprehensiveInsurance  <=500  ){
				 scoreRed += 10;
				 todoTexts.push("Vandalismusschaden");
			 }
		 }
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues : todoTextsBlues};
	 }
	 var scorePoliceInformed = function(info){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 if(info.policeInformed){
			 scoreBlue =10;
			 todoTextsBlues.push("Polizei");
	 	}else{
			 scoreRed = 20;
			 todoTexts.push("Keine Polizei");
	 	}
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues : todoTextsBlues};
	 }
	 
	 var scoreWitnessesAvailables = function(info){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 if(info.witnessesAvailables && info.witnessesAvailables.length > 0){
			 scoreBlue =10;
			 todoTextsBlues.push("Zeugen");
		 }
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues : todoTextsBlues};
	 }
	 var scoreWitnessesAvailablesSturm = function(info){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 if(info.witnessesAvailables && info.witnessesAvailables.length > 0){
			 scoreBlue =20;
			 todoTextsBlues.push("Zeugen");
		 }
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues : todoTextsBlues};
	 }
	 function calcAge(birthday,info) {
		  var sysDate = new Date().getTime();
		  if(!info.dayOfAccidentUnknow && info.dayOfAccident && info.timeOfAccident){
			  sysDate = new Date(info.dayOfAccident)
			  var time =  info.timeOfAccident.split(":");
			  sysDate.setHours(time[0], time[1], 0);
			  sysDate = sysDate.getTime();
		  }
		  birthday = new Date(birthday).getTime();
		  return ((sysDate - birthday) / (31557600000));
	}
	 var scoreOtherEinbruchDiebstahl = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 if(info.rateDamage >=0 && info.rateDamage < 1000 && !info.vehicleRepaired &&  info.moneyOfTheRepair && info.damageLevel !='Rechnung' && info.damageLevel !='Rechnung'){
			 scoreBlue +=5;
			 todoTextsBlues.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 3000 && info.damageLevel =='ohne Beleg' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=10;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Kostenvoranschlag' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Gutachten' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 
		 if(customer.birthday ){
			 var age = calcAge(customer.birthday,info) ;
			 if(age < 30){
				 scoreRed +=10;
				 todoTexts.push("Junger VN");
			 }
			 if(60< age < 65){
				 scoreRed +=5;
			 }
			 if(age > 75){
				 scoreBlue +=10;
				 todoTextsBlues.push("Alter VN");
			 }
		 }
		 if(info.dayOfAccident){
			 var dayOfWeed = new Date(info.dayOfAccident).getDay();
			 if( dayOfWeed == 0 || dayOfWeed == 5 || dayOfWeed ==6 ){
				 scoreBlue +=5;
				 todoTextsBlues.push("Wochenende");
			 }else{
				 scoreRed +=5;
				 todoTexts.push("Wochentag");
			 }
		 }
		 if(customer.dateOfFirstRegistration){
			 var age = calcAge(customer.dateOfFirstRegistration,info);
			 if(age > 10 && info.kmVehicle/age < 10000){
				 scoreBlue +=5;
				 todoTextsBlues.push("Fahrzeugalter");
			 }
			 if(age > 5 && info.kmVehicle/age > 30000){
				 scoreRed +=10;
				 todoTexts.push("Fahrzeugalter");
			 }
		 }
		 if(customer.garageCommitment && info.vehicleRepaired){
			 scoreBlue +=5;
			 todoTextsBlues.push("Werkstattbindung");
		 }
		 if(customer.garageCommitment && !info.vehicleRepaired){
			 scoreRed +=10;
			 todoTexts.push("Werkstattbindung");
		 }
		 if(info.saturday){
			 scoreBlue +=5;
			 todoTextsBlues.push("Erreichbarkeit");
		 }
		 if(info.manufacturer){
			 if(info.manufacturer.name && info.manufacturer.name.toUpperCase().indexOf('BMW') > -1 ){
				 scoreRed +=25;
				 todoTexts.push("Fahrzeugtyp");
			 }
			 if(info.manufacturer.name == 'Audi' || info.manufacturer.name == 'Fiat' || info.manufacturer.name == 'Mercedes-Benz'){
				 scoreRed +=20;
				 todoTexts.push("Fahrzeugtyp");
			 }
			 if(info.manufacturer.name == 'Ford' || info.manufacturer.name == 'Renault' || info.manufacturer.name == 'Volkswagen'){
				 scoreRed +=10;
				 todoTexts.push("Fahrzeugtyp");
			 }
			 if(info.manufacturer.name == 'Hyundai' || info.manufacturer.name == 'Peugeot' || info.manufacturer.name == 'Toyota'){
				 scoreBlue +=20;
				 todoTextsBlues.push("Fahrzeugtyp");
			 }
		 }
		 if(info.environment == 'Privatgelände'){
			 scoreBlue +=5;
			 todoTextsBlues.push("Privatgelände");
		 }
		 if(info.environment == 'Gewerbegebiet'){
			 scoreRed +=10;
			 todoTexts.push("Gewerbegebiet");
		 }
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues: todoTextsBlues};
	 };
	 // glass
	 var scoreEnvironment = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 if(info.environment == 'Autobahn'){
			 scoreBlue +=20;
			 todoTextsBlues.push("Autobahn");
		 }
		 if(info.environment == 'Land-/Bundesstraße'){
			 scoreBlue +=10;
			 todoTextsBlues.push("Land/Bundesstraße");
		 }
		 if(info.environment == 'Privatgelände'){
			 scoreBlue +=5;
			 todoTextsBlues.push("Privatgelände");
		 }
		 if(info.environment == 'Gewerbegebiet'){
			 scoreRed +=10;
			 todoTexts.push("Gewerbegebiet");
		 }
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues : todoTextsBlues};
	 }
	 
	 
	 var scoreWhatBeenDamagedItem = function(info,customer){
		  var scoreBlue = 0;
		  var scoreRed = 0;
		  var todoTexts = [];
		  var todoTextsBlues = [];
		  var hasCheck = false;
		  var countCheck = 0 ;
			  for(item in info.whatBeenDamagedItem){
				  if(info.whatBeenDamagedItem[item]){
					  if(item == 'Windschutzscheibe' && info.whatBeenDamagedItem.length	==	1){
						  scoreBlue += 20;
						  todoTextsBlues.push(" Nur Windschutzscheibe");
					  }
					  if(item != 'Sonstiges'){
						  countCheck ++;
						  hasCheck = true;
					  }
						  
				  }
			  }
			  if(countCheck > 0 && countCheck < 3 && !info.vehicleReady && customer.covertypLiability && customer.covertypFullyCoprehensiveInsurance){
				  scoreBlue += 20;
				  todoTextsBlues.push("Nichtfahrbereit");
			  }
			  if(countCheck >= 3 && !info.vehicleReady && customer.covertypLiability && customer.covertypPartiallyComprehensive ){
				  scoreRed += 30;
				  todoTexts.push("Mehrfachbeschädigung ohne VK");
			  }
			  if(countCheck >= 3 && info.vehicleReady && customer.covertypLiability && customer.covertypPartiallyComprehensive ){
				  scoreRed += 20;
				  todoTexts.push("Mehrfachbeschädigung ohne VK");
			  }
			  if(!customer.covertypFullyCoprehensiveInsurance && !customer.covertypPartiallyComprehensive){
				  scoreBlue += 50;
				  todoTextsBlues.push("Keine Deckung");
			  }
			  return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues : todoTextsBlues};
  	  	  }
	 var scoreOtherGlas = function(info,customer){
		  var scoreBlue = 0;
		  var scoreRed = 0;
		  var todoTexts = [];
		  var todoTextsBlues = [];
		  if(info.rateDamage >=0 &&  info.rateDamage < 750){
			  scoreBlue += 10;
			  todoTextsBlues.push("Schadenhöhe");
		  }	
		  if(info.rateDamage >=0 && info.rateDamage < 1500 && info.damageLevel == 'ohne Beleg'){
			  scoreRed += 20;
			  todoTexts.push("Schadenhöhe");
		  }	
		  if(info.rateDamage >=0 && info.rateDamage < 1500 && info.damageLevel == 'Kostenvoranschlag'){
			  scoreRed += 25;
			  todoTexts.push("Schadenhöhe");
		  }	
		  if(info.rateDamage >=0 && info.rateDamage < 1500 && info.damageLevel == 'Gutachten'){
			  scoreRed += 30;
			  todoTexts.push("Schadenhöhe");
		  }	
		  if(info.dayOfAccident){
			  var dayOfAccident = new Date(info.dayOfAccident).getDay();
			  if(dayOfAccident == 6 ){
				  scoreBlue += 5;
				  todoTextsBlues.push("Wochenende");
			  }
			  if(dayOfAccident > 0 &&  dayOfAccident <6){
				  scoreRed += 10;
				  todoTexts.push("Wochentag");
			  }
		  }
			 if(customer.dateOfFirstRegistration ){
				 var age = calcAge(customer.dateOfFirstRegistration,info) ;
				 if(age > 10 && info.kmVehicle/age < 10000 ){
					 scoreBlue +=5;
					 todoTextsBlues.push("Fahrzeugalter");
				 }
				 if(age > 5 && info.kmVehicle/age > 30000 ){
					 scoreRed += 10;
					 todoTexts.push("Fahrzeugalter");
				 }
			 }
		  if(customer.garageCommitment){
			  	scoreBlue +=5;
			  	todoTextsBlues.push("Werkstattbindung");
		  }
		  if(info.saturday){
			  scoreBlue +=5;
			  todoTextsBlues.push("Erreichbarkeit");
		  }
		  if(info.brand == 'Fiat' || info.brand == 'Ford'){
			  scoreBlue +=20;
			  todoTextsBlues.push("Fahrzeugtyp");
		  }
		  if (info.brand && (info.brand.toUpperCase().indexOf('BMW') > -1 || info.brand == 'Kia' || info.brand == 'Seat' || info.brand == 'Hyundai')){
			  scoreRed +=25;
			  todoTexts.push("Fahrzeugtyp");
		  }
		  var tksb = getTksb(customer);
		  if(tksb && tksb == 0 ){
			  scoreRed +=10;
			  todoTexts.push("Ohne Selbstbeteiligung in TK");
		  }
		  if(customer.roadsideAssistance == 'Schutzbrief'){
			  scoreBlue +=5;
			  todoTextsBlues.push("Schutzbrief");
		  }
		  if(customer.firstContractBegin){
			  var contact = calcAge(customer.firstContractBegin,info);
			  if(contact * 12 < 3 ){
				  scoreRed +=5;
				  todoTexts.push("Neuer Vertrag");
			  }
		  }
		  return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues: todoTextsBlues};
 	 }
	 
	 var scoreHagel = function(info,customer){
		  var scoreBlue = 0;
		  var scoreRed = 0;
		  var todoTexts = [];
		  var todoTextsBlues = [];
		  if(!info.vehicleReady){
			  scoreBlue +=20;
			  todoTextsBlues.push("Nicht Fahrbereit");
		  }
		  if(!customer.covertypPartiallyComprehensive && !customer.covertypFullyCoprehensiveInsurance){
			  scoreBlue +=50;
			  todoTextsBlues.push("Keine Deckung");
		  }
		  if(info.rateDamage >=0 && info.rateDamage < 1000 && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			  scoreBlue +=5;
			  todoTextsBlues.push("Fiktive Abrechnung");
		  }
		  if(info.rateDamage > 3000 && info.damageLevel == 'ohne Beleg' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			  scoreRed += 10;
			  todoTexts.push("Fiktive Abrechnung");
		  }
		  if(info.rateDamage > 2000 && info.damageLevel == 'Kostenvoranschlag' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			  scoreRed += 15;
			  todoTexts.push("Fiktive Abrechnung");
		  }
		  if(info.rateDamage > 2000 && info.damageLevel == 'Gutachten' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			  scoreRed += 15;
			  todoTexts.push("Fiktive Abrechnung");
		  }
		  if(customer.dateOfFirstRegistration){
			 var age = calcAge(customer.dateOfFirstRegistration,info);
			 if(age > 10 && info.kmVehicle/age < 10000){
				 scoreBlue +=5;
				 todoTextsBlues.push("Fahrzeugalter");
			 }
			 if(age > 5 && info.kmVehicle/age > 30000){
				 scoreRed +=10;
				 todoTexts.push("Fahrzeugalter");
			 }
		  }	
		  if(customer.garageCommitment && info.vehicleRepaired){
				 scoreBlue +=5;
				 todoTextsBlues.push("Werkstattbindung");
			 }
			 if(customer.garageCommitment && !info.vehicleRepaired){
				 scoreRed +=10;
				 todoTexts.push("Werkstattbindung");
		  }
		 if(info.saturday || info.sunday){
			 scoreBlue +=5;
			 todoTextsBlues.push("Erreichbarkeit");
		 }  

		 if (customer.brand && (customer.brand == 'Audi' || customer.brand.toUpperCase().indexOf('BMW') || customer.brand == 'Mercedes-Benz')){
			 scoreRed +=15;
			 todoTexts.push("Fahrzeugtyp");
		 }
		 if(customer.brand == 'Hyundai' || customer.brand == 'Kia'   || customer.brand == 'Skoda' || customer.brand == 'Seat'){
			 scoreBlue +=10;
			 todoTextsBlues.push("Fahrzeugtyp");
		 } 
		 if(info.environment == 'Privatgelände'){
			 scoreBlue +=5;
			 todoTextsBlues.push("Privatgelände");
		 }
		 if(info.environment == 'Gewerbegebiet'){
			 scoreRed +=10;
			 todoTexts.push("Gewerbegebiet");
		 }
		 var tksb = getTksb(customer);
		  if(tksb && tksb == 0 ){
			  scoreBlue +=5;
			  todoTextsBlues.push("Ohne Selbstbeteiligung in TK");
		  }
		  if(tksb && tksb > 150 ){
			  scoreRed += 20;
			  todoTexts.push("Hohe Selbstbeteiligung in TK");
		  }
		  if(info.damageEarlierTimes){
			  scoreBlue +=20;
			  todoTextsBlues.push("Bekannter Vorschaden");
		  }
		 if(info.environment == 'Autobahn' || info.environment == 'Land-/Bundesstraße'){
			 scoreBlue +=10;
			 todoTextsBlues.push("Schadenhergang");
		 }
		 if(info.rateDamage >=0 && info.rateDamage <= 1000){
			 scoreBlue +=20;
			 todoTextsBlues.push("Geringe Schadenhöhe");
		 }
		 if(customer.roadsideAssistance == 'Schutzbrief'){
			 scoreBlue +=5;
			 todoTextsBlues.push("Schutzbrief");
		 }
		 var vksb = getVksb(customer);
		 if(vksb && vksb >= 300){
			 scoreBlue +=5;
			 todoTextsBlues.push("VK-Deckung");
		 }
		 if(customer.firstContractBegin){
			  var contact = calcAge(customer.firstContractBegin,info);
			  if(contact * 12 < 3 ){
				  scoreRed +=10;
				  todoTexts.push("Neuer Vertrag");
			  }
		  }
		 if(info.dayOfAccidentUnknow){
			 scoreRed += 20;
			 todoTexts.push("Umgebung unbekannt");
		 }
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues : todoTextsBlues};
		 
	 }
	 var scoreSturm = function(info,customer){
		  var scoreBlue = 0;
		  var scoreRed = 0;
		  var todoTexts = [];
		  var todoTextsBlues = [];
		  if(!info.vehicleReady){
			  scoreBlue +=20;
			  todoTextsBlues.push("Nicht Fahrbereit");
		  }
		  if(!customer.covertypPartiallyComprehensive && !customer.covertypFullyCoprehensiveInsurance){
			  scoreBlue +=50;
			  todoTextsBlues.push("Keine Deckung");
		  }
		  if(info.policeInformed){
			  scoreBlue +=10;
			  todoTextsBlues.push("Polizei");
		  }
		  if(!info.policeInformed){
			  scoreRed +=10;
			  todoTexts.push("Keine Polizei");
		  }
		 var scoreWitnessesAvailablesData = scoreWitnessesAvailablesSturm(info);
		 scoreBlue += scoreWitnessesAvailablesData.scoreBlue;
		 scoreRed += scoreWitnessesAvailablesData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreWitnessesAvailablesData.todoTexts);		
		 todoTextsBlues.push.apply(todoTextsBlues,scoreWitnessesAvailablesData.todoTextsBlues);
		   
		 if(info.rateDamage >=0 && info.rateDamage < 1000 && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreBlue +=5;
			 todoTextsBlues.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 3000 && info.damageLevel =='ohne Beleg' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=10;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Kostenvoranschlag' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Gutachten' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(customer.dateOfFirstRegistration){
			 var age = calcAge(customer.dateOfFirstRegistration,info);
			 if(age > 10 && info.kmVehicle/age < 10000){
				 scoreBlue +=5;
				 todoTextsBlues.push("Fahrzeugalter");
			 }
			 if(age > 5 && info.kmVehicle/age > 30000){
				 scoreRed +=10;
				 todoTexts.push("Fahrzeugalter");
			 }
		  }	
		 if(customer.garageCommitment && info.vehicleRepaired){
			 scoreBlue +=5;
			 todoTextsBlues.push("Werkstattbindung");
		 }
		 if(customer.garageCommitment && !info.vehicleRepaired){
			 scoreRed +=10;
			 todoTexts.push("Werkstattbindung");
		 }
		 if(info.saturday || info.sunday){
			 scoreBlue +=5;
			 todoTextsBlues.push("Erreichbarkeit");
		 }
		 if(info.manufacturer && info.manufacturer.name && (info.manufacturer.name.toUpperCase().indexOf('BMW') > -1  || info.manufacturer.name == 'Audi' || info.manufacturer.name == 'Mercedes-Benz')){
			  scoreRed +=15;
			  todoTexts.push("Fahrzeugtyp");
		  }
		  if(info.manufacturer && ( info.manufacturer.name == 'Skoda' || info.manufacturer.name == 'Kia' || info.manufacturer.name == 'Seat' || info.manufacturer.name == 'Hyundai' )){
			  scoreBlue +=10;
			  todoTextsBlues.push("Fahrzeugtyp");
		  }
		  if(info.environment == 'Privatgelände'){
			 scoreBlue +=5;
			 todoTextsBlues.push("Privatgelände");
		 }
		 if(info.environment == 'Gewerbegebiet'){
			 scoreRed +=10;
			 todoTexts.push("Gewerbegebiet");
		 }
		 var tksb = getTksb(customer);
		  if(tksb && tksb == 0 ){
			  scoreBlue +=10;
			  todoTextsBlues.push("Ohne Selbstbeteiligung in TK");
		  }
		  if(tksb && tksb > 150 ){
			  scoreRed += 20;
			  todoTexts.push("Hohe Selbstbeteiligung in TK");
		  }
		  if((info.vehicleDamagedItem == 'Dachziegel' || info.vehicleDamagedItem == 'Baum') && info.vehicleParked){
			  scoreBlue += 20;
			  todoTextsBlues.push("Schadenhergang");
		  }
		  if((info.vehicleDamagedItem == 'Äste' || info.vehicleDamagedItem == 'Herumfliegende Gegenstände' || info.vehicleDamagedItem == 'Sonstiges') && info.vehicleParked){
			  scoreRed += 10;
			  todoTexts.push("Schadenhergang");
		  }
		  if((info.environment == 'Autobahn' || info.environment == 'Land-/Bundesstraße') && !info.vehicleParked){
				 scoreBlue +=10;
				 todoTextsBlues.push("Schadenhergang");
		  }
		  if(info.rateDamage >=0 && info.rateDamage <= 1000){
			  scoreBlue +=20;
			  todoTextsBlues.push("Geringe Schadenhöhe");
		  }
		  if(customer.roadsideAssistance == 'Schutzbrief'){
			  scoreBlue +=5;
			  todoTextsBlues.push("Schutzbrief");
		  }
		  if(customer.firstContractBegin){
			  var contact = calcAge(customer.firstContractBegin,info);
			  if(contact * 12 < 3 ){
				  scoreRed +=5;
				  todoTexts.push("Junger Vertrag");
			  }
		  }
		  if(info.injuredPeople && info.people && info.people.length > 0){
			  scoreBlue +=20;
			  todoTextsBlues.push("Personenschaden");
		  }
		  if(info.dayOfAccidentUnknow){
				 scoreRed += 20;
				 todoTexts.push("Umgebung unbekannt");
		  }
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues:todoTextsBlues};
		 
	 }
	 
	 var scoreUnfall = function(info,customer){
		  var scoreBlue = 0;
		  var scoreRed = 0;
		  var todoTexts = [];
		  var todoTextsBlues = [];
		  if(!info.vehicleReady){
			  scoreBlue +=20;
			  todoTextsBlues.push("Nicht Fahrbereit");
		  }
		  if(!customer.covertypFullyCoprehensiveInsurance){
			  scoreBlue +=50;
			  todoTextsBlues.push("Keine Deckung");
		  }
		  if(info.policeInformed){
			  scoreBlue =+10;
			  todoTextsBlues.push("Polizei");
		  }
		  if(!info.policeInformed){
			  scoreRed += 20;
			  todoTexts.push("Keine Polizei");
		  }
		  var scoreWitnessesAvailablesData = scoreWitnessesAvailables(info);
		 scoreBlue += scoreWitnessesAvailablesData.scoreBlue;
		 scoreRed += scoreWitnessesAvailablesData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreWitnessesAvailablesData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreWitnessesAvailablesData.todoTextsBlues);
		 if(info.rateDamage >=0 && info.rateDamage < 1000 && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreBlue +=5;
			 todoTextsBlues.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 3000 && info.damageLevel =='ohne Beleg' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=10;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Kostenvoranschlag' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Gutachten' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.vehicleRepaired){
			 scoreBlue +=10;
			 todoTextsBlues.push("Reparaturabsicht");
		 }
		 if(info.dayOfAccident){
			  var dayOfAccident = new Date(info.dayOfAccident).getDay();
			  if(dayOfAccident == 6 ){
				  scoreBlue += 5;
				  todoTextsBlues.push("Wochenende");
			  }
			  if(dayOfAccident == 1 ){
				  scoreBlue += 10;
				  todoTextsBlues.push("Wochentag");
			  }
			  if(dayOfAccident == 0 ){
				  scoreRed += 10;
				  todoTexts.push("Wochenende");
			  }
			  if(dayOfAccident == 2 ){
				  scoreRed += 5;
				  todoTexts.push("Wochentag");
			  }
		  }
		 if(customer.dateOfFirstRegistration){
			 var age = calcAge(customer.dateOfFirstRegistration,info);
			 if(age > 10 && info.kmVehicle/age < 10000){
				 scoreBlue +=5;
				 todoTextsBlues.push("Fahrzeugalter");
			 }
			 if(age > 5 && info.kmVehicle/age > 30000){
				 scoreRed +=10;
				 todoTexts.push("Fahrzeugalter");
			 }
		 }
		 if(info.saturday){
			 scoreBlue +=5;
			 todoTextsBlues.push("Erreichbarkeit");
		 }
		 if(info.manufacturer && info.manufacturer.name && (info.manufacturer.name.toUpperCase().indexOf('BMW') > -1  || info.manufacturer.name == 'Audi' || info.manufacturer.name == 'Mercedes-Benz')){
			  scoreRed +=25;
			  todoTexts.push("Fahrzeugtyp");
		  }
		  if(info.manufacturer && ( info.manufacturer.name == 'Skoda' || info.manufacturer.name == 'Kia' || info.manufacturer.name == 'Seat' || info.manufacturer.name == 'Hyundai' || info.manufacturer.name == 'Nissan')){
			  scoreBlue +=20;
			  todoTextsBlues.push("Fahrzeugtyp");
		  }
		  
		  if(info.environment == 'Autobahn'){
			  scoreBlue +=20;
			  todoTextsBlues.push("Autobahn");
		  }
		  var tksb = getTksb(customer);
		 if(tksb && tksb == 0){
			 scoreBlue += 5;
			 todoTextsBlues.push("Ohne Selbstbeteiligung in TK");
		 }
		 if(tksb && tksb > 150){
			 scoreRed = +20;
			 todoTexts.push("Hohe Selbstbeteiligung in TK");
		 }
		 var vksb = getVksb(customer);
		 if(vksb && vksb > 300){
			 scoreRed +=10;
			 todoTexts.push("Hohe Selbstbeteiligung in VK");
		 }
		 if(info.rateDamage > 5000 && info.damageLevel == 'Kostenvoranschlag'){
			 scoreRed = +20;
			 todoTexts.push("Hoher Schaden mit KVA");
		 }
		 if(info.rateDamage >=0 && info.rateDamage <= 1000){
			 scoreBlue = +20;
			 todoTextsBlues.push("Geringe Schadenhöhe");
		 }
		 if(info.rateDamage > 10000 && !info.injuredPeople){
			 scoreRed = +30;
			 todoTexts.push("Hoher Schaden ohne Verletzte");
		 }
		 if(info.injuredPeople && info.people && info.people.length > 0){
			  scoreBlue +=20;
			  todoTextsBlues.push("Personenschaden");
		  }
		 if(info.timeOfAccident && info.dayOfAccident ){
			 var dayOfWeed = new Date(info.dayOfAccident).getDay();
			 var hours = info.timeOfAccident.split(":")[0];
			 if(hours >= 6 && hours <=9 && (info.environment == 'Autobahn' || info.environment == 'Land-/Bundesstraße' || info.environment == 'Innerorts' || info.environment == 'Sonstiges')
					 && (info.whatHappened =='Auffahrunfall' || info.whatHappened =='Ausweichkollision'  || info.whatHappened =='Kollision' || info.whatHappened =='Seitenkollision' || info.whatHappened =='Vorfahrtsverletzung')
					 && dayOfWeed > 0 && dayOfWeed < 6){
				 scoreBlue +=10;
				 todoTextsBlues.push("Berufsverkehr");
			 }
			 if(hours >= 16 && hours <=18 && (info.environment == 'Autobahn' || info.environment == 'Land-/Bundesstraße' || info.environment == 'Innerorts' || info.environment == 'Sonstiges')
					 && (info.whatHappened =='Auffahrunfall' || info.whatHappened =='Ausweichkollision'  || info.whatHappened =='Kollision' || info.whatHappened =='Seitenkollision' || info.whatHappened =='Vorfahrtsverletzung')
					 && dayOfWeed > 0 && dayOfWeed < 5){
				 scoreBlue +=10;
				 todoTextsBlues.push("Berufsverkehr");
			 }
			 if(hours >= 15 && hours <=17 && (info.environment == 'Autobahn' || info.environment == 'Land-/Bundesstraße' || info.environment == 'Innerorts' || info.environment == 'Sonstiges')
					 && (info.whatHappened =='Auffahrunfall' || info.whatHappened =='Ausweichkollision'  || info.whatHappened =='Kollision' || info.whatHappened =='Seitenkollision' || info.whatHappened =='Vorfahrtsverletzung')
					 && dayOfWeed == 5){
				 scoreBlue +=10;
				 todoTextsBlues.push("Berufsverkehr");
			 }
			 if(hours >= 15 && hours <=19 && (info.environment == 'Parkplatz' || info.environment == 'Privatgelände' )
					 && (info.whatHappened =='Parkplatzschaden/Delle')
					 && (dayOfWeed == 4 || dayOfWeed == 5)){
				 scoreBlue +=20;
				 todoTextsBlues.push("Haupteinkaufszeit");
			 }
			 if(hours >= 10 && hours <=13 && (info.environment == 'Parkplatz' || info.environment == 'Privatgelände' )
					 && (info.whatHappened == 'Parkplatzschaden/Delle')
					 && (dayOfWeed == 6)){
				 scoreBlue +=20;
				 todoTextsBlues.push("Haupteinkaufszeit");
			 }
			 if((hours <= 6 || hours >=21) && (info.environment == 'Privatgelände' )){
				 scoreRed +=15;
				 todoTexts.push("Nachts, Privatgelände");
			 }
			 if((hours <= 6 || hours >=21) && (info.environment == 'Gewerbegebiet' || info.environment == 'Parkplatz' || info.environment == 'Sonstiges' )){
				 scoreRed +=30;
				 todoTexts.push("Nachts, verlassene Umgebung");
			 }
		 }
		 if(customer.roadsideAssistance == 'Schutzbrief'){
			  scoreBlue +=5;
			  todoTextsBlues.push("Schutzbrief");
		  }
		 if(customer.firstContractBegin){
			  var contact = calcAge(customer.firstContractBegin,info);
			  if(contact * 12 < 3 ){
				  scoreRed +=5;
				  todoTexts.push("Neuer Vertrag");
			  }
		  }
		 if(info.whatHappened =='Streifschaden'){
			 scoreRed +=20;
			 todoTexts.push("Streifschaden");
		 }
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts , todoTextsBlues : todoTextsBlues};
	 }
	 
	 
	 var scoreVandalismus = function(info,customer){
		  var scoreBlue = 0;
		  var scoreRed = 0;
		  var todoTexts = [];
		  var todoTextsBlues = [];
		  if(!info.vehicleReady){
			  scoreBlue +=20;
			  todoTextsBlues.push("Nicht Fahrbereit");
		  }
		  if(!customer.covertypPartiallyComprehensive && !customer.covertypFullyCoprehensiveInsurance){
			  scoreBlue +=50;
			  todoTextsBlues.push("Keine Deckung");
		  }
		  if(info.policeInformed){
			  scoreBlue +=10;
			  todoTextsBlues.push("Polizei");
		  }
		  if(!info.policeInformed){
			  scoreRed += 20;
			  todoTexts.push("Keine Polizei");
		  }
		 var scoreWitnessesAvailablesData = scoreWitnessesAvailables(info);
		 scoreBlue += scoreWitnessesAvailablesData.scoreBlue;
		 scoreRed += scoreWitnessesAvailablesData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreWitnessesAvailablesData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreWitnessesAvailablesData.todoTextsBlues);
		 if(info.rateDamage >=0 && info.rateDamage < 1000 && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreBlue +=5;
			 todoTextsBlues.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 3000 && info.damageLevel =='ohne Beleg' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=10;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Kostenvoranschlag' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Gutachten' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.dayOfAccident){
			  var dayOfAccident = new Date(info.dayOfAccident).getDay();
			  if(dayOfAccident == 6 ){
				  scoreBlue += 5;
				  todoTextsBlues.push("Wochenende");
			  }
			  if(dayOfAccident > 0 &&  dayOfAccident < 6){
				  scoreRed += 10;
				  todoTexts.push("Wochentag");
			  }
		  }
		 if(customer.dateOfFirstRegistration){
			 var age = calcAge(customer.dateOfFirstRegistration,info);
			 if(age > 10 && info.kmVehicle/age < 10000){
				 scoreBlue +=5;
				 todoTextsBlues.push("Fahrzeugalter");
			 }
			 if(age > 5 && info.kmVehicle/age > 30000){
				 scoreRed +=10;
				 todoTexts.push("Fahrzeugalter");
			 }
		 }
		 if(customer.garageCommitment && info.vehicleRepaired){
			 scoreBlue +=5;
			 todoTextsBlues.push("Werkstattbindung");
		 }
		 if(customer.garageCommitment && !info.vehicleRepaired){
			 scoreRed +=10;
			 todoTexts.push("Werkstattbindung");
		 }
		 if(info.saturday || info.sunday){
			 scoreBlue +=5;
			 todoTextsBlues.push("Erreichbarkeit");
		 }
		 if(info.manufacturer && info.manufacturer.name && (info.manufacturer.name.toUpperCase().indexOf('BMW') > -1 || info.manufacturer.name == 'Audi' || info.manufacturer.name == 'Mercedes-Benz')){
			  scoreRed +=25;
			  todoTexts.push("Fahrzeugtyp");
		  }
		  if(info.manufacturer && ( info.manufacturer.name == 'Kia' || info.manufacturer.name == 'Seat' || info.manufacturer.name == 'Hyundai' )){
			  scoreBlue +=20;
			  todoTextsBlues.push("Fahrzeugtyp");
		  }
		 if(info.environment == 'Privatgelände'){
			 scoreBlue +=5;
			 todoTextsBlues.push("Privatgelände");
		 }
		 if(info.environment == 'Gewerbegebiet'){
			 scoreRed +=10;
			 todoTexts.push("Gewerbegebiet");
		 }
		 if(info.environment == 'Autobahn' || info.environment == 'Land-/Bundesstraße'){
			 scoreRed +=20;
			 todoTexts.push("BAB/Land-/Bundesstraße");
		 }
		 if(info.environment == 'Sonstiges'){
			 scoreRed +=15;
			 todoTexts.push("“Umgebung Sonstiges");
		 }
		 var scoreTksbData = scoreTksb(info,customer);
		 scoreBlue += scoreTksbData.scoreBlue;
		 scoreRed += scoreTksbData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreTksbData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreTksbData.todoTextsBlues);
		 
		 if(customer.roadsideAssistance == 'Schutzbrief'){
			  scoreBlue +=5;
			  todoTextsBlues.push("Schutzbrief");
		  }
		 if(customer.firstContractBegin){
			  var contact = calcAge(customer.firstContractBegin,info);
			  if(contact * 12 < 3 ){
				  scoreRed +=5;
				  todoTexts.push("Junger Vertrag");
			  }
		  }
		 if(info.culprit && info.culpritName){
			 scoreBlue +=5;
			 todoTextsBlues.push("Mutmaßlicher Täter bekannt");
		 }
		 if(info.timeOfAccident ){
			 var hours = info.timeOfAccident.split(":")[0];
			 if((hours >= 21 && hours <=24) || (hours >= 0 && hours <=6)){
				 scoreRed += 10;
				 todoTexts.push("Nachts");
			 }
		  }
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues:todoTextsBlues};
		 
	 }
	 
	 var scoreWild = function(info,customer){
		  var scoreBlue = 0;
		  var scoreRed = 0;
		  var todoTexts = [];
		  var todoTextsBlues = [];
		  if(info.timeOfAccident && info.dayOfAccident ){
			 var dayOfWeed = new Date(info.dayOfAccident).getDay();
			 var hours = info.timeOfAccident.split(":")[0];
			 if(hours <= 9 && hours >=6 && dayOfWeed > 0 && dayOfWeed < 6){
				 scoreBlue += 10;
				 todoTextsBlues.push("Berufsverkehr");
			 }
			 if(hours <= 18 && hours >= 16 && (dayOfWeed == 1 || dayOfWeed == 2 || dayOfWeed == 3 || dayOfWeed == 4)){
				 scoreBlue += 10;
				 todoTextsBlues.push("Berufsverkehr");
			 }
			 if(hours <= 17 && hours >= 15 &&  dayOfWeed == 5){
				 scoreBlue += 10;
				 todoTextsBlues.push("Berufsverkehr");
			 }
		  }
		  if(info.environment == 'Autobahn' ){
			  scoreBlue += 5;
			  todoTextsBlues.push("Autobahn");
		  }
		  if(info.environment == 'Land-/Bundesstraße' ){
			  scoreRed += 10;
			  todoTexts.push("Land/Bundesstraße");
		  }
		  if(info.environment == 'Innerorts' ){
			  scoreBlue += 10;
			  todoTextsBlues.push("Innerorts");
		  }
		  if((info.environment != 'Autobahn' && info.environment != 'Land-/Bundesstraße') && info.animal == 'Hund' ){
			  scoreBlue += 20;
			  todoTextsBlues.push("Hund");
		  }
		  if(!info.vehicleReady && info.collisionAnimal){
			  scoreBlue += 20;
			  todoTextsBlues.push("Nicht Fahrbereit");
		  }
		  var tksb = getTksb(customer);
		  if(info.collisionAnimal && (info.animal == 'Fuchs' || info.animal == 'Kaninchen' || info.animal == 'Waschbär' || info.animal == 'Marder') && tksb && tksb == 0){
			  scoreBlue += 10;
			  todoTextsBlues.push("Kleintier");
		  }
		  if(info.animal == 'Reh' || info.animal == 'Hirsch' || info.animal == 'Wildschwein' ){
			  scoreBlue += 20;
			  todoTextsBlues.push("Großwild");
		  }
		  if(!info.collisionAnimal && !customer.covertypFullyCoprehensiveInsurance){
			  scoreRed  +=10;
			  todoTexts.push("Keine VK-Deckung");
		  }
		  if(!info.collisionAnimal ){
			  scoreRed  +=30;
			  todoTexts.push("Wildausweichschaden");
		  }
		  if(info.collisionAnimal && info.injuredPeople){
			  scoreBlue  +=20;
			  todoTextsBlues.push("Personenschaden");
		  }
		  var scoreWitnessesAvailablesData = scoreWitnessesAvailables(info);
		 scoreBlue += scoreWitnessesAvailablesData.scoreBlue;
		 scoreRed += scoreWitnessesAvailablesData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreWitnessesAvailablesData.todoTexts);	
		 todoTextsBlues.push.apply(todoTextsBlues,scoreWitnessesAvailablesData.todoTextsBlues);
		 if(info.policeInformed){
			  scoreBlue +=10;
			  todoTextsBlues.push("Polizei");
		  }
		  if(!info.policeInformed){
			  scoreRed +=20;
			  todoTexts.push("Keine Polizei");
		  }
		  if(info.foresterInformed && info.department && info.fileNumber){
			  scoreBlue +=10;
			  todoTextsBlues.push("Förster");
		  }
		  if(!info.injuredPeople && info.rateDamage >= 10000){
			  scoreRed +=30;
			  todoTexts.push("Hoher Schaden ohne Verletzte");
		  }
		  if(info.manufacturer && info.manufacturer.name && (info.manufacturer.name.toUpperCase().indexOf('BMW') > -1  || info.manufacturer.name == 'Audi' || info.manufacturer.name == 'Mercedes-Benz')){
			  scoreRed +=25;
			  todoTexts.push("Fahrzeugtyp");
		  }
		  if(info.manufacturer && ( info.manufacturer.name == 'Skoda' || info.manufacturer.name == 'Kia' || info.manufacturer.name == 'Seat' || info.manufacturer.name == 'Hyundai' || info.manufacturer.name == 'Toyota')){
			  scoreBlue +=20;
			  todoTextsBlues.push("Fahrzeugtyp");
		  }
		 if(info.rateDamage >=0 && info.rateDamage < 1000 && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreBlue +=5;
			 todoTextsBlues.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 3000 && info.damageLevel =='ohne Beleg' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=10;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Kostenvoranschlag' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Gutachten' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(customer.garageCommitment && info.vehicleRepaired){
			 scoreBlue +=5;
			 todoTextsBlues.push("Werkstattbindung");
		 }
		 if(customer.garageCommitment && !info.vehicleRepaired){
			 scoreRed +=10;
			 todoTexts.push("Werkstattbindung");
		 }
		 if(info.rateDamage > 5000 && info.damageLevel == 'Kostenvoranschlag'){
			 scoreRed = +20;
			 todoTexts.push("Hoher Schaden mit KVA");
		 }
		 if(info.rateDamage >=0 && info.rateDamage <= 1000){
			 scoreBlue = +20;
			 todoTextsBlues.push("Geringe Schadenhöhe");
		 }
		 if(info.saturday || info.sunday){
			 scoreBlue +=5;
			 todoTextsBlues.push("Erreichbarkeit");
		 }
		 if(customer.firstContractBegin){
			  var contact = calcAge(customer.firstContractBegin,info);
			  if(contact * 12 < 3 ){
				  scoreRed +=5;
				  todoTexts.push("Neuer Vertrag");
			  }
		  }
		 if(customer.dateOfFirstRegistration){
			 var age = calcAge(customer.dateOfFirstRegistration,info);
			 if(age > 10 && info.kmVehicle/age < 10000){
				 scoreBlue +=5;
				 todoTextsBlues.push("Fahrzeugalter");
			 }
			 if(age > 5 && info.kmVehicle/age > 30000){
				 scoreRed +=10;
				 todoTexts.push("Fahrzeugalter");
			 }
		 }
		 var tksb = getTksb(customer);
		  if(tksb && tksb == 0 ){
			  scoreBlue +=5;
			  todoTextsBlues.push("Ohne Selbstbeteiligung in TK");
		  }
		  if(tksb && tksb > 150 ){
			  scoreRed += 20;
			  todoTexts.push("Hohe Selbstbeteiligung in TK");
		  }	 
		  if(!customer.covertypPartiallyComprehensive && !customer.covertypFullyCoprehensiveInsurance){
			  scoreBlue +=50;
			  todoTextsBlues.push("Keine Deckung");
		  }
		  if(customer.roadsideAssistance == 'Schutzbrief'){
			  scoreBlue +=5;
			  todoTextsBlues.push("Schutzbrief");
		  }
		  if(info.dayOfAccident){
			 var dayOfWeed = new Date(info.dayOfAccident).getDay();
			 if( dayOfWeed == 0 ){
				 scoreBlue +=5;
				 todoTextsBlues.push("Wochenende");
			 }
			 if( dayOfWeed == 2 || dayOfWeed ==3 ){
				 scoreBlue +=10;
				 todoTextsBlues.push("Wochentag");
			 }
			 if( dayOfWeed == 4 || dayOfWeed ==6 ){
				 scoreRed +=10;
				 todoTexts.push("Wochentag");
			 }
			 var dayOfAccident = new Date(info.dayOfAccident);
			 dayOfAccident.setHours(0, 0, 0, 0);
			 var sysDate = new Date();
			 sysDate.setHours(0, 0, 0, 0);
			 if(dayOfAccident.getTime() == sysDate.getTime()){
				 scoreBlue +=15;
				 todoTextsBlues.push("kurze Meldedauer");
			 }
			 dayOfAccident.setDate(dayOfAccident.getDate()+1);
			 if(dayOfAccident.getTime() == sysDate.getTime()){
				 scoreBlue +=10;
				 todoTextsBlues.push("kurze Meldedauer");
			 }
			 dayOfAccident.setDate(dayOfAccident.getDate()+4);
			 if(dayOfAccident.getTime() < sysDate.getTime()){
				 scoreRed +=10;
				 todoTexts.push("Meldedauer");
			 }
		 }
		  
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues : todoTextsBlues};
		 
	 }
	 
	 var scoreSonstigesChoosed = function(info,customer){
		  var scoreBlue = 0;
		  var scoreRed = 0;
		  var todoTexts = [];
		  var todoTextsBlues = [];
		  
		  if(!info.vehicleReady){
			  scoreRed += 20;
			  todoTexts.push("Nicht Fahrbereit");
		  }
		  if(!customer.covertypPartiallyComprehensive && !customer.covertypFullyCoprehensiveInsurance){
			  scoreBlue +=50;
			  todoTextsBlues.push("Keine Deckung");
		  }
		  if(info.policeInformed){
			  scoreBlue +=10;
			  todoTextsBlues.push("Polizei");
		  }
		  if(!info.policeInformed){
			  scoreRed +=20;
			  todoTexts.push("Keine Polizei");
		  }
		  if(info.rateDamage >=0 && info.rateDamage < 1000 && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
				 scoreBlue +=5;
				 todoTextsBlues.push("Fiktive Abrechnung");
		  }

		 if(info.rateDamage > 3000 && info.damageLevel =='ohne Beleg' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=10;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Kostenvoranschlag' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(info.rateDamage > 2000 && info.damageLevel =='Gutachten' && !info.vehicleRepaired && info.moneyOfTheRepair && info.damageLevel !='Rechnung'){
			 scoreRed +=15;
			 todoTexts.push("Fiktive Abrechnung");
		 }
		 if(customer.dateOfFirstRegistration){
			 var age = calcAge(customer.dateOfFirstRegistration,info);
			 if(age > 10 && info.kmVehicle/age < 10000){
				 scoreBlue +=5;
				 todoTextsBlues.push("Fahrzeugalter");
			 }
			 if(age > 5 && info.kmVehicle/age > 30000){
				 scoreRed +=10;
				 todoTexts.push("Fahrzeugalter");
			 }
		 }
		 if(customer.garageCommitment && info.vehicleRepaired){
			 scoreBlue +=5;
			 todoTextsBlues.push("Werkstattbindung");
		 }
		 if(customer.garageCommitment && !info.vehicleRepaired){
			 scoreRed +=10;
			 todoTexts.push("Werkstattbindung");
		 }
		 if(info.saturday || info.sunday){
			 scoreBlue +=5;
			 todoTextsBlues.push("Erreichbarkeit");
		 }
		 if(info.manufacturer && info.manufacturer.name && (info.manufacturer.name.toUpperCase().indexOf('BMW') > -1  || info.manufacturer.name == 'Audi' || info.manufacturer.name == 'Mercedes-Benz')){
			  scoreRed +=25;
			  todoTexts.push("Fahrzeugtyp");
		  }
		  if(info.manufacturer && ( info.manufacturer.name == 'Skoda' || info.manufacturer.name == 'Kia' || info.manufacturer.name == 'Seat' || info.manufacturer.name == 'Hyundai' )){
			  scoreBlue +=20;
			  todoTextsBlues.push("Fahrzeugtyp");
		  }
		  if(info.dayOfAccidentUnknow){
				 scoreRed += 20;
				 todoTexts.push("Umgebung unbekannt");
			 }
		  var tksb = getTksb(customer);
		  if(tksb && tksb == 0 ){
			  scoreBlue +=5;
			  todoTextsBlues.push("Ohne Selbstbeteiligung in TK");
		  }
		  if(tksb && tksb > 150 ){
			  scoreRed += 5;
			  todoTexts.push("Ohne Selbstbeteiligung in TK");
		  }	 
		  if(info.environment == 'Land-/Bundesstraße' ){
			  scoreBlue +=10;
			  todoTextsBlues.push("Schadenhergang");
		  }
		  if(info.rateDamage >=0 && info.rateDamage < 1000){
				 scoreBlue +=20;
				 todoTextsBlues.push("Geringe Schadenhöhe");
		  }
		  if(customer.roadsideAssistance == 'Schutzbrief'){
			  scoreBlue +=5;
			  todoTextsBlues.push("Schutzbrief");
		  }
		  if(customer.firstContractBegin){
			  var contact = calcAge(customer.firstContractBegin,info);
			  if(contact * 12 < 3 ){
				  scoreRed +=5;
				  todoTexts.push("Neuer Vertrag");
			  }
		  }
		  if(info.whatHappenedOthers =='Brand'){
			  scoreRed +=10;
			  todoTexts.push("Brand");
		  }
		  if(info.whatHappenedOthers =='Brand' && info.environment =='Gewerbegebiet'){
			  scoreRed +=15;
			  todoTexts.push("Brand im Gewerbegebietrand");
		  }
		  
		  if(info.timeOfAccident){
			 var hours = info.timeOfAccident.split(":")[0];

			 if((hours <= 6 && hours >=0) || (hours <= 24 && hours >=21)){
				 scoreRed += 10;
				 todoTexts.push("Nachts");
			 }
		  }
		 
		 return {scoreBlue : scoreBlue,scoreRed : scoreRed ,todoTexts : todoTexts,todoTextsBlues : todoTextsBlues};
		 
	 }
	 
	 var buildTodo = function(todoBlue,todoRed){
		 var todo = "+ ";
		 for(var i=0;i < todoBlue.length;i++){
			 todo +=  todoBlue[i] + " | ";
		 }
		 if(todo)
			 todo = todo.substring(0, todo.length -3);
		 todoBlue = todo;
		 todo ="- ";
		 for(var i=0;i < todoRed.length;i++){
			 todo +=  todoRed[i] + " | ";
		 }
		 if(todo)
			 todo = todo.substring(0, todo.length -3);
		 todoRed = todo;
		 return {todoBlue : todoBlue ,todoRed : todoRed };
	 }
	 var buildTotaScoreAndTodo = function(todoTextsBlues,todoTexts,scoreRed,scoreBlue){
		 var todoResult = [];
		 todoResult.push("Betrug");
		 var todo =  buildTodo(todoTextsBlues,todoTexts);
		 if(todo.todoBlue)
			 todoResult.push(todo.todoBlue);
		 if(todo.todoRed)
			 todoResult.push(todo.todoRed);
		 var score = scoreRed - scoreBlue;
		 if(score > 100)
			 score = 100;
		 if(score <0)
			 score = 0;
		 return {score : score,todoResult : todoResult};
	 }
	 var calculatorTrickreyEinbruchdiebstahl = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 var scoreStolenData = scoreStolen(info);
		 scoreBlue += scoreStolenData.scoreBlue;
		 scoreRed += scoreStolenData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreStolenData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreStolenData.todoTextsBlues);
		 
		 var scoreVehicleReadyData = scoreVehicleReady(info);
		 scoreBlue += scoreVehicleReadyData.scoreBlue;
		 scoreRed += scoreVehicleReadyData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreVehicleReadyData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreVehicleReadyData.todoTextsBlues);
		 
		 var scoreTksbData = scoreTksb(info,customer);
		 scoreBlue += scoreTksbData.scoreBlue;
		 scoreRed += scoreTksbData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreTksbData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreTksbData.todoTextsBlues);
		 
		 var scoreSonstigesData = scoreSonstiges(info,customer);
		 scoreBlue += scoreSonstigesData.scoreBlue;
		 scoreRed += scoreSonstigesData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreSonstigesData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreSonstigesData.todoTextsBlues);
		 
		 var scoreRadioData = scoreRadio(info,customer);
		 scoreBlue += scoreRadioData.scoreBlue;
		 scoreRed += scoreRadioData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreRadioData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreRadioData.todoTextsBlues);
		 
		 var scorePoliceInformedData = scorePoliceInformed(info);
		 scoreBlue += scorePoliceInformedData.scoreBlue;
		 scoreRed += scorePoliceInformedData.scoreRed;
		 todoTexts.push.apply(todoTexts,scorePoliceInformedData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scorePoliceInformedData.todoTextsBlues);
		 
		 var scoreWitnessesAvailablesData = scoreWitnessesAvailables(info);
		 scoreBlue += scoreWitnessesAvailablesData.scoreBlue;
		 scoreRed += scoreWitnessesAvailablesData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreWitnessesAvailablesData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreWitnessesAvailablesData.todoTextsBlues);
		 
		 var scoreOther = scoreOtherEinbruchDiebstahl(info,customer);
		 scoreBlue += scoreOther.scoreBlue;
		 scoreRed += scoreOther.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreOther.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreOther.todoTextsBlues);
		 
		 if(info.dayOfAccidentUnknow){
			 scoreRed += 15;
			 todoTexts.push("Umgebung unbekannt");
		 }
		 
		 var result = buildTotaScoreAndTodo(todoTextsBlues,todoTexts,scoreRed,scoreBlue);
		 return {score : result.score,todoTexts : result.todoResult};
	 }

	 var calculatorTrickreyGlas = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 var scoreEnvironmentData = scoreEnvironment(info);
		 scoreBlue += scoreEnvironmentData.scoreBlue;
		 scoreRed += scoreEnvironmentData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreEnvironmentData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreEnvironmentData.todoTextsBlues);
		
		 var scoreWhatBeenDamagedItemData = scoreWhatBeenDamagedItem(info,customer);
		 scoreBlue += scoreWhatBeenDamagedItemData.scoreBlue;
		 scoreRed += scoreWhatBeenDamagedItemData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreWhatBeenDamagedItemData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreWhatBeenDamagedItemData.todoTextsBlues);
		 
		 var scoreOtherGlassData = scoreOtherGlas(info,customer);
		 scoreBlue += scoreOtherGlassData.scoreBlue;
		 scoreRed += scoreOtherGlassData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreOtherGlassData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreOtherGlassData.todoTextsBlues);
		 
		 var result = buildTotaScoreAndTodo(todoTextsBlues,todoTexts,scoreRed,scoreBlue);
		 return {score : result.score,todoTexts : result.todoResult};
	 }
	 
	 var calculatorTrickeryHagel = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 var scoreHagelData = scoreHagel(info,customer);
		 scoreBlue += scoreHagelData.scoreBlue;
		 scoreRed += scoreHagelData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreHagelData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreHagelData.todoTextsBlues);
		 
		 
		 var result = buildTotaScoreAndTodo(todoTextsBlues,todoTexts,scoreRed,scoreBlue);
		 return {score : result.score,todoTexts : result.todoResult};
	 }
	 
	 var calculatorTrickerySturm = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 var scoreSturmData = scoreSturm(info,customer);
		 scoreBlue += scoreSturmData.scoreBlue;
		 scoreRed += scoreSturmData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreSturmData.todoTexts);
		 todoTextsBlues.push.apply(todoTexts,scoreSturmData.todoTextsBlues);
		 
		 
		 var result = buildTotaScoreAndTodo(todoTextsBlues,todoTexts,scoreRed,scoreBlue);
		 return {score : result.score,todoTexts : result.todoResult};
	 }
	 var calculatorTrickeryUnfall = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 var scoreUnfallData = scoreUnfall(info,customer);
		 scoreBlue += scoreUnfallData.scoreBlue;
		 scoreRed += scoreUnfallData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreUnfallData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreUnfallData.todoTextsBlues);
		 
		 
		 var result = buildTotaScoreAndTodo(todoTextsBlues,todoTexts,scoreRed,scoreBlue);
		 return {score : result.score,todoTexts : result.todoResult};
	 }
	 var calculatorTrickeryVandalismus = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 var scoreVandalismusData = scoreVandalismus(info,customer);
		 scoreBlue += scoreVandalismusData.scoreBlue;
		 scoreRed += scoreVandalismusData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreVandalismusData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreVandalismusData.todoTextsBlues);
		 
		 var result = buildTotaScoreAndTodo(todoTextsBlues,todoTexts,scoreRed,scoreBlue);
		 return {score : result.score,todoTexts : result.todoResult};
	 }
	 var calculatorTrickeryWildunfall = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 var scoreWildData = scoreWild(info,customer);
		 scoreBlue += scoreWildData.scoreBlue;
		 scoreRed += scoreWildData.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreWildData.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreWildData.todoTextsBlues);
		 
		 var result = buildTotaScoreAndTodo(todoTextsBlues,todoTexts,scoreRed,scoreBlue);
		 return {score : result.score,todoTexts : result.todoResult};
	 }
	 var calculatorTrickerySonstiges = function(info,customer){
		 var scoreBlue = 0;
		 var scoreRed = 0;
		 var todoTexts = [];
		 var todoTextsBlues = [];
		 var scoreSonstiges = scoreSonstigesChoosed(info,customer);
		 scoreBlue += scoreSonstiges.scoreBlue;
		 scoreRed += scoreSonstiges.scoreRed;
		 todoTexts.push.apply(todoTexts,scoreSonstiges.todoTexts);
		 todoTextsBlues.push.apply(todoTextsBlues,scoreSonstiges.todoTextsBlues);
		 
		 var todoResult = [];
		 todoResult.push("Betrug");
		 var todo =  buildTodo(todoTextsBlues,todoTexts);
		 if(todo.todoBlue)
			 todoResult.push(todo.todoBlue);
		 if(todo.todoRed)
			 todoResult.push(todo.todoRed);
		 var score = scoreRed - scoreBlue;
		 if(score > 100)
			 score = 100;
		 if(score <0)
			 score = 0;
		 return {score : score,todoTexts : todoResult};
		 var result = buildTotaScoreAndTodo(todoTextsBlues,todoTexts,scoreRed,scoreBlue);
		 return {score : result.score,todoTexts : result.todoResult};
	 }
	 
	 trickeryScoreService.calculatorTrickery = function(info,customer){
		 if(info.typeOfDamage && info.typeOfDamage === 'Unfall') 
			 return calculatorTrickeryUnfall(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Einbruch / Diebstahl') 
			 return calculatorTrickreyEinbruchdiebstahl(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Vandalismus') 
			 return calculatorTrickeryVandalismus(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Glas') 
			 return calculatorTrickreyGlas(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Sturm') 
			 return calculatorTrickerySturm(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Hagel') 
			 return calculatorTrickeryHagel(info,customer); 
		 if(info.typeOfDamage && info.typeOfDamage === 'Wildunfall') 
			 return calculatorTrickeryWildunfall(info,customer);
		 if(info.typeOfDamage && info.typeOfDamage === 'Sonstiges') 
			 return calculatorTrickerySonstiges(info,customer);
	 };
	 
	 return trickeryScoreService;
	     
});