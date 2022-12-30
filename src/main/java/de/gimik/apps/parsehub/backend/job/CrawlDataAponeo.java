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
public class CrawlDataAponeo {
	@Autowired
	private ParsehubService parsehubService;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private PharmaProductService pharmaProductService;
	public void crawlDataPharmaAponeo() {
		ParsehubSetting parsehubSetting = parsehubService.findByCodeActiveTrue(Constants.Code.APONEO);
	 	   List<PharmaSetting> pharmaSetting = new ArrayList<PharmaSetting>();
	 	   pharmaSetting = settingService.findByActiveTrueAndAponeoUrlIsNotNull();
	        Launcher launcher = new Launcher();
	 	   SessionFactory factory = launcher.launch();
	 	   Session dummySession = factory.create();
	 	   for(int k = 0;k<pharmaSetting.size();k++) {
	 			PharmaSetting pharmaSettingMain = pharmaSetting.get(k);
	 // lấy data nếu type = KEYWORD or CATEGORY
	 			if(pharmaSettingMain.getType().equals(Constants.CrawlType.KEYWORD) || pharmaSettingMain.getType().equals(Constants.CrawlType.CATEGORY)) {
	 			String url = pharmaSettingMain.getAponeoUrl();
	 			dummySession.navigate(url);
	 	    	dummySession.waitDocumentReady(200000);
	 	    	dummySession.wait(getRandomNumberInRange(3000, 5000));
	 	    	dummySession.evaluate("var button = document.querySelector(\"#cmpwelcomebtnyes > a\");"
	 		 			+ "var click = function(){if(button != null)button.click();}");
	 		 	dummySession.callFunction("click");
	 	    	dummySession.wait(getRandomNumberInRange(10000, 15000));
	 	    	String responseNextEvent = dummySession.getContent();
	 	    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
	 	    	Elements title = documentNextEvents.select("div[class=row apn-content-block apn-product-list] div[class=col-12] div[class*=apn-product-list-product-name]");
			   	Elements pzn = documentNextEvents.select("div[class=row apn-content-block apn-product-list] div[class=col-12]");
			   	Elements quantity = documentNextEvents.select("div[class=row apn-content-block apn-product-list] div[class=col-12] div[class*=apn-product-list-product-info] div[class*=apn-mb-5] span:not([class!=\"\"])");
			   	Elements price = documentNextEvents.select("div[class=row apn-content-block apn-product-list] div[class=col-12] div[class=apn-product-list-price]");
			   	Elements avp = documentNextEvents.select("div[class=row apn-content-block apn-product-list] div[class=col-12] div[class*=apn-product-list-product-cta]");
	 // nếu không có data thì đợi thêm thời gian để page loaded  		    	
	 	    	if(price.size() == 0) {
	     		dummySession.wait(getRandomNumberInRange(10000, 15000));
	 	    	responseNextEvent = dummySession.getContent();
	 	    	documentNextEvents = Jsoup.parse(responseNextEvent);
	 	    	 title = documentNextEvents.select("div[class=row apn-content-block apn-product-list] div[class=col-12] div[class*=apn-product-list-product-name]");
		 	   	 pzn = documentNextEvents.select("div[class=row apn-content-block apn-product-list] div[class=col-12]");
		 	   	 quantity = documentNextEvents.select("div[class=row apn-content-block apn-product-list] div[class=col-12] div[class*=apn-product-list-product-info] div[class*=apn-mb-5] span:not([class!=\"\"])");
		 	   	 price = documentNextEvents.select("div[class=row apn-content-block apn-product-list] div[class=col-12] div[class=apn-product-list-price]");
		 	   	 avp = documentNextEvents.select("div[class=row apn-content-block apn-product-list] div[class=col-12] div[class*=apn-product-list-product-cta]");
	 	    	}
	 	    	Integer placement =1;
	 	    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
	 	    	if(price != null && !price.isEmpty()) {
	 	    		for(int h=0;h<price.size();h++) {
	 	    			PharmaDetail pharmaProduct = new PharmaDetail();
	 	    			pharmaProduct.setName(title.get(h).text());
	 	    			pharmaProduct.setPzn(pzn.get(h).id().substring(0, pzn.get(h).id().indexOf("_")));
	 	    			String quantityText = quantity.get(h).text();
		 	   			if(quantityText.indexOf(",")> -1) {
		 	   				pharmaProduct.setQuantity(quantityText.substring(quantityText.indexOf(",")).replace(",", "").trim());
		 	   			}else
		 	   				pharmaProduct.setQuantity(quantityText.substring(quantityText.length()-5, quantityText.length()).trim());
		 	   			pharmaProduct.setPrice(price.get(h).text());
		 	   			Double priceInDouble = convertStringToDouble(price.get(h).text().replace(",", ".").replace("€", "").trim());
		 	   			pharmaProduct.setPriceInDouble(priceInDouble);
			 	   		String avpText = avp.get(h).text();
						if(avpText.indexOf("VP") > -1 || avpText.indexOf("RP") > -1) {
							String avpInString = avpText.substring(avpText.indexOf("P")+2, avpText.indexOf("€")-1).replace(",", ".").trim();
							Double avpInDouble = convertStringToDouble(avpInString);
							pharmaProduct.setAvp(avpInDouble);
							Double discountFinally = 100-(priceInDouble / avpInDouble*100);
			    			Integer discount =  (int) Math.round(discountFinally);
			    			pharmaProduct.setDiscount(discount);
						}else if(avpText.indexOf("Rezept einlösen") > -1) {
							String avpInString = avpText.substring(avpText.indexOf("€"), avpText.lastIndexOf("€")).replace("€","").replace(",", ".").trim();
							Double avpInDouble = convertStringToDouble(avpInString);
							pharmaProduct.setAvp(avpInDouble);
							Double discountFinally = 100-(priceInDouble / avpInDouble*100);
			    			Integer discount =  (int) Math.round(discountFinally);
			    			pharmaProduct.setDiscount(discount);
						}else {
							pharmaProduct.setAvp(priceInDouble);
							pharmaProduct.setDiscount(0);
						}
	 	    			pharmaProduct.setSequenceByKeyword(placement);
	 	    			pharmaProduct.setPharmaSetting(pharmaSettingMain);
	 	    			pharmaProduct.setKeyword(pharmaSettingMain.getName());
	 	    			pharmaProduct.setCrawlType(pharmaSettingMain.getType());
	 	    			pharmaProduct.setUrl(pharmaSettingMain.getAponeoUrl());
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
	 	    			
	 	    		}
	 	    		if(!CollectionUtils.isEmpty(pharmaProducts))
	 					mainService.createListDetail(pharmaProducts);
	 	    	}else
	 	    		System.out.println("Error at url: "+pharmaSettingMain.getAponeoUrl());
	 			}
	 // lấy data nếu type = CROSS-SELLING
	 			if(pharmaSettingMain.getType().equals(Constants.CrawlType.CROSS_SELLING) || pharmaSettingMain.getName().equals(Constants.Keyword.Diabetes)) {
	 			String url = pharmaSettingMain.getAponeoUrl();
	 			dummySession.navigate(url);
	 	    	dummySession.waitDocumentReady(200000);
	 	    	dummySession.wait(getRandomNumberInRange(3000, 5000));
	 	    	dummySession.evaluate("var button = document.querySelector(\"#cmpwelcomebtnyes > a\");"
	 		 			+ "var click = function(){if(button != null)button.click();}");
	 		 	dummySession.callFunction("click");
	 	    	dummySession.wait(getRandomNumberInRange(10000, 15000));
	 	    	String responseNextEvent = dummySession.getContent();
	 	    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
	 	    	Elements title = documentNextEvents.select("#reco-product div[class=apn-product-list-item] input[name=abskt]");
	 	   	    Elements quantity = documentNextEvents.select("#reco-product div[class=apn-product-list-item] div[class=apn-product-list-product-info]");
	 // nếu không có data thì đợi thêm thời gian để page loaded  		    	
	 	    	if(title.size() == 0) {
	     		dummySession.wait(getRandomNumberInRange(10000, 15000));
	 	    	responseNextEvent = dummySession.getContent();
	 	    	documentNextEvents = Jsoup.parse(responseNextEvent);
	 	    	 title = documentNextEvents.select("#reco-product div[class=apn-product-list-item] input[name=abskt]");
	 	   	     quantity = documentNextEvents.select("#reco-product div[class=apn-product-list-item] div[class=apn-product-list-product-info]");
	 	    	}
	 	    	if(pharmaSettingMain.getName().equals(Constants.Keyword.Diabetes)) {
 	    		 title = documentNextEvents.select("#reco-category div[class=apn-product-list-item] input[name=abskt]");
	 	   	     quantity = documentNextEvents.select("#reco-category div[class=apn-product-list-item] div[class=apn-product-list-product-info]");
	 	    	}
	 	    	Integer placement =1;
	 	    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
	 	    	if(title != null && !title.isEmpty()) {
	 	    		for(int h=0;h<title.size();h++) {
	 	    			PharmaDetail pharmaProduct = new PharmaDetail();
	 	    			pharmaProduct.setName(title.get(h).dataset().get("name"));
	 	    			pharmaProduct.setPzn(title.get(h).dataset().get("pzn"));
	 	    			if(quantity.get(h).text().indexOf(",") > -1) {
	 	   				pharmaProduct.setQuantity(quantity.get(h).text().substring(quantity.get(h).text().indexOf(",")).replace(",", ""));;
	 	   			}else
	 	   				pharmaProduct.setQuantity(quantity.get(h).text());
	 	    			Double price = convertStringToDouble(title.get(h).dataset().get("gross-price"));
	 	    			pharmaProduct.setPriceInDouble(price);
	 	    			Double avp = convertStringToDouble(title.get(h).dataset().get("base-price"));
	 	    			pharmaProduct.setAvp(avp);
	 	    			pharmaProduct.setPrice(title.get(h).dataset().get("gross-price"));
	 	    			Double discountFinally = 100-(price / avp*100);
		    			Integer discount =  (int) Math.round(discountFinally);
		    			pharmaProduct.setDiscount(discount);
	 	    			pharmaProduct.setSequenceByKeyword(placement);
	 	    			pharmaProduct.setPharmaSetting(pharmaSettingMain);
	 	    			pharmaProduct.setKeyword(pharmaSettingMain.getName());
	 	    			pharmaProduct.setCrawlType(pharmaSettingMain.getType());
	 	    			pharmaProduct.setUrl(pharmaSettingMain.getAponeoUrl());
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
	 	    			
	 	    		}
	 	    		if(!CollectionUtils.isEmpty(pharmaProducts))
	 					mainService.createListDetail(pharmaProducts);
	 	    	}else
	 	    		System.out.println("Error at url: "+pharmaSettingMain.getAponeoUrl());
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
