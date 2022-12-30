package de.gimik.apps.parsehub.backend.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import de.gimik.apps.parsehub.backend.model.ParsehubSetting;
import de.gimik.apps.parsehub.backend.model.PharmaDetail;
import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.model.PharmaSetting;
import de.gimik.apps.parsehub.backend.service.ParsehubService;
import de.gimik.apps.parsehub.backend.service.PharmaDetailService;
import de.gimik.apps.parsehub.backend.service.PharmaProductService;
import de.gimik.apps.parsehub.backend.service.PharmaSettingService;
import de.gimik.apps.parsehub.backend.util.Constants;
import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
public class CrawlDataApotheke {
	@Autowired
	private ParsehubService parsehubService;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private PharmaProductService pharmaProductService;
	public void crawlDataPharmaApotheke() {
		ParsehubSetting parsehubSetting = parsehubService.findByCodeActiveTrue(Constants.Code.APOTHEKE);
	 	   List<PharmaSetting> pharmaSetting = new ArrayList<PharmaSetting>();
	 	   pharmaSetting = settingService.findByActiveTrueAndApothekeUrlIsNotNull();
	        Launcher launcher = new Launcher();
	 	   SessionFactory factory = launcher.launch();
	 	   Session dummySession = factory.create();
	 	   for(int k = 0;k<pharmaSetting.size();k++) {
	 			PharmaSetting pharmaSettingMain = pharmaSetting.get(k);
	 // lấy data nếu type = KEYWORD or CATEGORY
	 			if(pharmaSettingMain.getType().equals(Constants.CrawlType.KEYWORD) || pharmaSettingMain.getType().equals(Constants.CrawlType.CATEGORY)) {
	 			String url = pharmaSettingMain.getApothekeUrl();
	 			dummySession.navigate(url);
	 	    	dummySession.waitDocumentReady(200000);
	 	    	dummySession.wait(getRandomNumberInRange(3000, 5000));
	 	    	dummySession.evaluate("var button = document.querySelector(\"#usercentrics-root\").shadowRoot.querySelector(\"#focus-lock-id > div > div > div.sc-bYoBSM.hrDdPG > div > div > div.sc-dlVxhl.bEDIID > div > button.sc-gsDKAQ.dZIwB\");"
	 	    			+ "var click = function(){if(button != null)button.click();}");
	 	    	dummySession.callFunction("click");
	 	    	dummySession.wait(getRandomNumberInRange(10000, 15000));
	 	    	String responseNextEvent = dummySession.getContent();
	 	    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
	 	    	Elements title = documentNextEvents.select("div[data-clientside-hook=productDetailButton] a[class*=o-SearchProductListItem__title u-font-weight--bold u-no-margin a-h3@medium]");
//	 	   	Elements quantity = documentNextEvents.select("div[data-clientside-hook=productDetailButton] span[class*=a-h4-tiny u-font-weight--bold]");
	 	    	Elements pzn = documentNextEvents.select("div[data-clientside-hook=productDetailButton] div[class=o-SearchProductListItem__selectableSection]");
	 	   	Elements price = documentNextEvents.select("div[data-clientside-hook=productDetailButton] [data-qa-id=entry-price]");
	 	   	Elements avp = documentNextEvents.select("div[data-clientside-hook=productDetailButton] div[class*=l-flex l-flex--distribute-space l-flex--end l-flex--wrap]");
	 // nếu không có data thì đợi thêm thời gian để page loaded  		    	
	 	    	if((price != null && !price.isEmpty()) || price.size() == 0) {
	     		dummySession.wait(getRandomNumberInRange(10000, 15000));
	 	    	responseNextEvent = dummySession.getContent();
	 	    	documentNextEvents = Jsoup.parse(responseNextEvent);
	 	    	 title = documentNextEvents.select("div[data-clientside-hook=productDetailButton] a[class*=o-SearchProductListItem__title u-font-weight--bold u-no-margin a-h3@medium]");
//		 	   	 quantity = documentNextEvents.select("div[data-clientside-hook=productDetailButton] span[class*=a-h4-tiny u-font-weight--bold]");
	 	    	 pzn = documentNextEvents.select("div[data-clientside-hook=productDetailButton] div[class=o-SearchProductListItem__selectableSection]");
		 	   	 price = documentNextEvents.select("div[data-clientside-hook=productDetailButton] [data-qa-id=entry-price]");
		 	   	 avp = documentNextEvents.select("div[data-clientside-hook=productDetailButton] div[class*=l-flex l-flex--distribute-space l-flex--end l-flex--wrap]");
	 	    	}
	 	    	Integer placement =1;
	 	    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
	 	    	if(title != null && !title.isEmpty()) {
	 	    		for(int h=0;h<title.size();h++) {
	 	    			PharmaDetail pharmaProduct = new PharmaDetail();
	 	    			pharmaProduct.setName(title.get(h).text());
	 	    			String avpText = avp.get(h).text();
	 	    			String pznText = pzn.get(h).text();
	 	    			String priceInText = price.get(h).text();
	 	    			if(pznText.indexOf("PZN/EAN") > -1) {
	 	    				Integer numberOfSlash = pznText.lastIndexOf("/");
	 	    			String pznOnly = pznText.substring(pznText.indexOf("PZN/EAN"), numberOfSlash).replace("PZN/EAN", "").replace(":", "").trim().replaceAll("[^0-9,-\\.]", "");
	 	    				pharmaProduct.setPzn(pznOnly);
	 	    				pharmaProduct.setQuantity(pznText.substring(0, pznText.indexOf("PZN/EAN")).replace("PZN/EAN", "").trim());
	 	    			}else if(pznText.indexOf(":") > -1) {
	 	    				Integer numberOfTwoDot = pznText.indexOf(":");
	 	    				pharmaProduct.setPzn(pznText.substring(numberOfTwoDot+1).trim().replaceAll("[^0-9,-\\.]", "").replace(".", ""));
	 	    				pharmaProduct.setQuantity(pznText.substring(0, pznText.indexOf(":")).replace("PZN", "").replace("ISBN", "").trim());
	 	    			}
	 	    				pharmaProduct.setPrice(price.get(h).text());
	 	    				String priceInString = priceInText.replace("ab", "").replace("€", "").replace(".", "").trim().replace("\u00a0", "").replace(",",".");
	 	    	    		Double priceInDouble = convertStringToDouble(priceInString);
	 	    	    		if(priceInText.indexOf("ab") > -1) {
	 	    					String priceInString2 = priceInText.replace("ab", "").replace("€", "").replace(".", "").trim().replace("\u00a0", "").replace(",",".");
	 	    	    			Double priceInDouble2 = convertStringToDouble(priceInString2.substring(0, priceInString2.length()));
	 	    	    			pharmaProduct.setPriceInDouble(priceInDouble2);
	 	    				}else {
	 	    					pharmaProduct.setPriceInDouble(priceInDouble);
	 	    				}
	 	    			if(avpText.indexOf("VP") > -1 && avpText.indexOf("In den Warenkorb") > -1) {
	 	    				Integer number1 = avpText.indexOf("In den Warenkorb");
	 	    				Integer number2 = avpText.lastIndexOf("€");
	 	    				String data  = avpText.substring(number2,number1).replace("€", "").replace(".", "").trim().replace("\u00a0", "").replace(",",".");
	 	    				Double avpInDouble = convertStringToDouble(data);
	 	    				pharmaProduct.setAvp(avpInDouble);
	 	    			}else if(avpText.indexOf("Originalpreis") > -1 && avpText.indexOf("In den Warenkorb") > -1) {
	 	    				Integer number1 = avpText.indexOf("In den Warenkorb");
	 	    				Integer number2 = avpText.lastIndexOf("€");
	 	    				String data  = avpText.substring(number2,number1).replace("€", "").replace(".", "").trim().replace("\u00a0", "").replace(",",".");
	 	    				Double avpInDouble = convertStringToDouble(data);
	 	    				pharmaProduct.setAvp(avpInDouble);
	 	    			}else
	 	    				pharmaProduct.setAvp(priceInDouble);
	 	    			if(avpText.indexOf("AVP/UVP") > -1 && avpText.indexOf("In den Warenkorb") < 0) {
	 	    				Integer number1 = avpText.lastIndexOf("€");
	 	    				String avpText1 = avpText.substring(number1+1,avpText.length()).replace(".", "").trim().replace("\u00a0", "").replace(",",".");
	 	    				Double avpInDouble =  convertStringToDouble(avpText1);
	 	    				pharmaProduct.setAvp(avpInDouble);
	 	    			}
	 	    			if(avpText.indexOf("%") > -1) {
	 	    				Integer number1 = avpText.indexOf("−");
	 	    				String avtText2 = avpText.substring(number1+1,avpText.indexOf("%")).trim();
	 	    				Integer numberDiscount = convertStringToInteger(avtText2);
	 	    				pharmaProduct.setDiscount(numberDiscount);
	 	    			}else
	 	    				pharmaProduct.setDiscount(0);
	 	    			pharmaProduct.setSequenceByKeyword(placement);
	 	    			pharmaProduct.setPharmaSetting(pharmaSettingMain);
	 	    			pharmaProduct.setKeyword(pharmaSettingMain.getName());
	 	    			pharmaProduct.setCrawlType(pharmaSettingMain.getType());
	 	    			pharmaProduct.setUrl(pharmaSettingMain.getApothekeUrl());
	 	    			placement++;
	 	    			if(!StringUtils.isEmpty(pharmaProduct.getPzn())) {
	 						PharmaProduct pharmaProductUnique = pharmaProductService.findByPznAndActiveTrue(pharmaProduct.getPzn().trim());
	 						if(pharmaProductUnique != null) pharmaProduct.setProductId(pharmaProductUnique.getId());
	 						else {
	 							pharmaProductUnique = new PharmaProduct(pharmaProduct.getName(), pharmaProduct.getPzn().trim());
	 							pharmaProductService.create(pharmaProductUnique);
	 							pharmaProduct.setProductId(pharmaProductUnique.getId());
	 						}
	 					}
	 					pharmaProduct.setParsehubSetting(parsehubSetting);
	 	    			pharmaProducts.add(pharmaProduct);
	 	    			if(placement == 31)
	 	    				break;
	 	    		}
	 	    		if(!CollectionUtils.isEmpty(pharmaProducts))
	 					mainService.createListDetail(pharmaProducts);
	 	    	}else
	 	    		System.out.println("Error at url: "+pharmaSettingMain.getApothekeUrl());
	 			}
	 			if(pharmaSettingMain.getType().equals(Constants.CrawlType.CROSS_SELLING)) {
	 				String url = pharmaSettingMain.getApothekeUrl();
	 				dummySession.navigate(url);
	 				dummySession.waitDocumentReady(200000);
	 				dummySession.wait(getRandomNumberInRange(15000, 20000));
	 				dummySession.evaluate("var button = document.querySelector(\"#usercentrics-root\").shadowRoot.querySelector(\"#focus-lock-id > div > div > div.sc-bYoBSM.hrDdPG > div > div > div.sc-dlVxhl.bEDIID > div > button.sc-gsDKAQ.dZIwB\");"
		 	    			+ "var click = function(){if(button != null)button.click();}");
		 	    	dummySession.callFunction("click");
	 				dummySession.evaluate("var scrollDown = function() {document.getElementsByClassName(\"o-ProductDescriptions__crosssell\")[0].scrollIntoView()};");
	 				dummySession.callFunction("scrollDown");
	 				dummySession.wait(getRandomNumberInRange(15000, 20000));
	 				String responseNextEvent = dummySession.getContent();
	 				org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
	 				Elements title = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center] [data-analytics=item-title]");
	 				Elements pzn = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center]");
	 				Elements quantity = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center] [data-stretch-group=amount]");
	 				Elements price = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center] [data-stretch-group=product-price] div[data-qa-id=entry-price]");
	 				Elements avp = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center] [data-stretch-group=product-price]");
	 				Elements discount = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center] div[class*=o-ProductSliderItem__image u-margin-large--bottom]");
	 // nếu không có data thì đợi thêm thời gian để page loaded  		    	
	 		    	if((price != null && !price.isEmpty()) || price.size() == 0) {
	 				dummySession.wait(getRandomNumberInRange(10000, 15000));
	 				responseNextEvent = dummySession.getContent();
	 				documentNextEvents = Jsoup.parse(responseNextEvent);
	 				title = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center] [data-analytics=item-title]");
	 				pzn = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center]");
	 				quantity = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center] [data-stretch-group=amount]");
	 				price = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center] [data-stretch-group=product-price] div[data-qa-id=entry-price]");
	 				avp = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center] [data-stretch-group=product-price]");
	 				discount = documentNextEvents.select("article[class=o-ProductSliderItem u-text-align--center] div[class*=o-ProductSliderItem__image u-margin-large--bottom]");
	 		    	}
	 				Integer placement =1;
	 				List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
	 				if(title != null && !title.isEmpty()) {
	 				for(int h =0;h<title.size();h++) {
	 					PharmaDetail pharmaProduct = new PharmaDetail();
	 					String discountText = discount.get(h).text();
	 					pharmaProduct.setName(title.get(h).text());
	 					pharmaProduct.setPzn(pzn.get(h).dataset().get("product-id"));
	 					pharmaProduct.setQuantity(quantity.get(h).text());
	 					pharmaProduct.setPrice(price.get(h).text());
	 					Double priceInDouble = convertStringToDouble(price.get(h).text().replace("ab", "").replace("€", "").replace(".", "").trim().replace("\u00a0", "").replace(",", "."));
	     				pharmaProduct.setPriceInDouble(priceInDouble);
	 					String avpInText = avp.get(h).text().replace("€", "");
	 					Integer number1 = avpInText.indexOf(",");
	 					String data  = avpInText.substring(0, number1+3).replace("€", "").replace(".", "").trim().replace(",", ".");
	 					Double avpInDouble = convertStringToDouble(data);
	 					if(avpInDouble != 0) {
	 						pharmaProduct.setAvp(avpInDouble);
	 					}else
	 						pharmaProduct.setAvp(priceInDouble);
	 					if(discountText.indexOf("%") > -1) {
	 						String discountText2 = discountText.substring(1, discountText.indexOf("%")).trim();
	 						Integer numberDiscount = convertStringToInteger(discountText2);
	 	    				pharmaProduct.setDiscount(numberDiscount);
	 					}else
	 						pharmaProduct.setDiscount(0);
	 					pharmaProduct.setSequenceByKeyword(placement);
	 					pharmaProduct.setPharmaSetting(pharmaSettingMain);
	 					pharmaProduct.setKeyword(pharmaSettingMain.getName());
	 					pharmaProduct.setUrl(pharmaSettingMain.getApothekeUrl());
	 					pharmaProduct.setCrawlType(pharmaSettingMain.getType());
	 					placement++;
	 					if(!StringUtils.isEmpty(pharmaProduct.getPzn())) {
	 						PharmaProduct pharmaProductUnique = pharmaProductService.findByPznAndActiveTrue(pharmaProduct.getPzn().trim());
	 						if(pharmaProductUnique != null) pharmaProduct.setProductId(pharmaProductUnique.getId());
	 						else {
	 							pharmaProductUnique = new PharmaProduct(pharmaProduct.getName(), pharmaProduct.getPzn().trim());
	 							pharmaProductService.create(pharmaProductUnique);
	 							pharmaProduct.setProductId(pharmaProductUnique.getId());
	 						}
	 					}
	 					pharmaProduct.setParsehubSetting(parsehubSetting);
	 					pharmaProducts.add(pharmaProduct);
	 					if(placement == 31)
	 	    				break;
	 				}
	 				if(!CollectionUtils.isEmpty(pharmaProducts))
	 					mainService.createListDetail(pharmaProducts);
	 				}else
	 		    		System.out.println("Error at url: "+pharmaSettingMain.getApothekeUrl());
	 			}
	 	    	}System.out.println("DONE");
	 		 dummySession.close();
	}
	
	private static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	private Double convertStringToDouble(String value) {
		Double result = 0.0;
		try {
			result = Double.parseDouble(value);
		} catch (Exception e) {
			// TODO: handle exception
			return 0.0;
		}
		return result;
	}
	private Integer convertStringToInteger(String value) {
		Integer result = 0;
		try {
			result = Integer.parseInt(value);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
		return result;
	}
	public static void main(String[] args) {

	}
}
