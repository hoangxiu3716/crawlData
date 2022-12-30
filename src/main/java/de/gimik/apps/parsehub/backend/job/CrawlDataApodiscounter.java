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
public class CrawlDataApodiscounter {
	@Autowired
	private ParsehubService parsehubService;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private PharmaProductService pharmaProductService;
	public void crawlDataPharmaApodiscounter() {
		ParsehubSetting parsehubSetting = parsehubService.findByCodeActiveTrue(Constants.Code.APODISCOUNTER);
    	List<PharmaSetting> pharmaSetting = new ArrayList<PharmaSetting>();
    	pharmaSetting = settingService.findByActiveTrueAndApodiscounterUrlIsNotNull();
       Launcher launcher = new Launcher();
	   SessionFactory factory = launcher.launch();
	   Session dummySession = factory.create();
	   for(int k = 0;k<pharmaSetting.size();k++) {
			PharmaSetting pharmaSettingMain = pharmaSetting.get(k);
// lấy data nếu type = KEYWORD or CATEGORY
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.KEYWORD) || pharmaSettingMain.getType().equals(Constants.CrawlType.CATEGORY)) {
				String url = pharmaSettingMain.getApodiscounterUrl();
				dummySession.navigate(url);
		    	dummySession.waitDocumentReady(200000);
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	dummySession.evaluate("var button = document.querySelector(\"#uc-btn-accept-banner\");"
		    			+ "var click = function(){if(button != null)button.click();}");
		    	dummySession.callFunction("click");
		    	String responseNextEvent = dummySession.getContent();
		    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
		    	Elements title = documentNextEvents.select("div[class=product_listing_single_row_wrapper] a[class=product_listing_title]");
		    	Elements price = documentNextEvents.select("div[class=product_listing_single_row_wrapper] div[class=product_listing_price_container]");
		    	Elements avp = documentNextEvents.select("div[class=product_listing_single_row_wrapper] div[class=product_listing_avp]");
		    	Elements pzn = documentNextEvents.select("div[class=product_listing_single_row_wrapper] div[class=product_listing_base_price]");   	
		    	Integer placement =1;
		    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
		    	if(title != null && !title.isEmpty()) {
		    		for(int h=0;h<title.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(title.get(h).text());
		    			pharmaProduct.setQuantity(title.get(h).text().substring(title.get(h).text().indexOf("("), title.get(h).text().length()).replace("(", "").replace(")", ""));
		    			String avpText = avp.get(h).text();
		    			String priceText = price.get(h).text();
		    			pharmaProduct.setPrice(priceText.replace("*", "").trim());
		    			String priceInText = priceText.replace("€", "").replace(",", ".").replace("*", "").trim();
		    			Double priceInDouble = convertStringToDouble(priceInText);
		    			pharmaProduct.setPriceInDouble(priceInDouble);
		    			Double avpInDouble = convertStringToDouble(avpText.replace("€", "").replace(",", ".").replace("*", "").trim());
		    			if(avpInDouble != 0.0) {
		    				pharmaProduct.setAvp(avpInDouble);
		    			}else 
		    				pharmaProduct.setAvp(priceInDouble);
		    			Double discountFinally = 100-(priceInDouble / avpInDouble*100);
		    			Integer discount =  (int) Math.round(discountFinally);
		    			pharmaProduct.setDiscount(discount);
		    			String pznText = pzn.get(h).text();
		    			Integer number1 = pznText.indexOf("(");
		    			if(number1 > -1) {
		    				String pznString = pznText.substring(0,number1).trim().replace("PZN", "");
		    				pharmaProduct.setPzn(pznString.substring(1, pznString.length()));
		    			}else {
		    				String pznString = pznText.replace("PZN", "").trim();
		    				pharmaProduct.setPzn(pznString.substring(1, pznString.length()));
		    			}
		    			pharmaProduct.setSequenceByKeyword(placement);
		    			pharmaProduct.setPharmaSetting(pharmaSettingMain);
		    			pharmaProduct.setKeyword(pharmaSettingMain.getName());
		    			pharmaProduct.setCrawlType(pharmaSettingMain.getType());
		    			pharmaProduct.setUrl(pharmaSettingMain.getApodiscounterUrl());
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
		    			if(h==29)
		    				break;
		    		}
		    		if(!CollectionUtils.isEmpty(pharmaProducts))
						mainService.createListDetail(pharmaProducts);
		    	}
// check nếu title.size() == 0 thì lấy data từ class khác	    	
		    	else if(title.size() == 0) {
		    		 title = documentNextEvents.select("div[class=product_listing_block_boxes] div[class=product_listing_block_productname]");
			    	 price = documentNextEvents.select("div[class=product_listing_block_boxes] div[class=hotw_price]");
			    	 avp = documentNextEvents.select("div[class=product_listing_block_boxes] div[class=hotw_avp]");
			    	 pzn = documentNextEvents.select("div[class=product_listing_block_boxes] div[class=hotw_base_price]");
			    	 for(int h=0;h<title.size();h++) {
			    			PharmaDetail pharmaProduct = new PharmaDetail();
			    			pharmaProduct.setName(title.get(h).text());
			    			pharmaProduct.setQuantity(title.get(h).text().substring(title.get(h).text().indexOf("("), title.get(h).text().length()).replace("(", "").replace(")", ""));
			    			String avpText = avp.get(h).text();
			    			String priceText = price.get(h).text();
			    			pharmaProduct.setPrice(priceText.replace("*", "").trim());
			    			String priceInText = priceText.replace("€", "").replace(",", ".").replace("*", "").trim();
			    			Double priceInDouble = convertStringToDouble(priceInText);
			    			pharmaProduct.setPriceInDouble(priceInDouble);
			    			Double avpInDouble = convertStringToDouble(avpText.replace("€", "").replace(",", ".").replace("*", "").trim());
			    			if(avpInDouble != 0.0) {
			    				pharmaProduct.setAvp(avpInDouble);
			    			}else 
			    				pharmaProduct.setAvp(priceInDouble);
			    			Double discountFinally = 100-(priceInDouble / avpInDouble*100);
			    			Integer discount =  (int) Math.round(discountFinally);
			    			pharmaProduct.setDiscount(discount);
			    			String pznText = pzn.get(h).text();
			    			Integer number1 = pznText.indexOf("(");
			    			if(number1 > -1) {
			    				String pznString = pznText.substring(0,number1).trim().replace("PZN", "");
			    				pharmaProduct.setPzn(pznString.substring(1, pznString.length()));
			    			}else {
			    				String pznString = pznText.replace("PZN", "").trim();
			    				pharmaProduct.setPzn(pznString.substring(1, pznString.length()));
			    			}
			    			pharmaProduct.setSequenceByKeyword(placement);
			    			pharmaProduct.setPharmaSetting(pharmaSettingMain);
			    			pharmaProduct.setKeyword(pharmaSettingMain.getName());
			    			pharmaProduct.setCrawlType(pharmaSettingMain.getType());
			    			pharmaProduct.setUrl(pharmaSettingMain.getApodiscounterUrl());
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
			    			if(h==29)
			    				break;
			    		}
			    		if(!CollectionUtils.isEmpty(pharmaProducts))
							mainService.createListDetail(pharmaProducts);
		    	}else
		    		System.out.println("Error at url: "+pharmaSettingMain.getApodiscounterUrl());
				}
// lấy data nếu type = CROSS_SELLING
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.CROSS_SELLING)) {
				String url = pharmaSettingMain.getApodiscounterUrl();
				dummySession.navigate(url);
		    	dummySession.waitDocumentReady(200000);
		    	dummySession.wait(getRandomNumberInRange(3000, 5000));
		    	dummySession.evaluate("var button = document.getElementById(\"uc-btn-accept-banner\");"
		    			+ "var click = function(){if(button != null)button.click();}");
		    	dummySession.callFunction("click");
		    	dummySession.wait(getRandomNumberInRange(10000, 15000));
		    	String responseNextEvent = dummySession.getContent();
		    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
		    	Elements title = documentNextEvents.select("#product_recommendations_scroll div[class=hotw_productname]");
		    	Elements price = documentNextEvents.select("#product_recommendations_scroll div[class=hotw_price]");
		    	Elements avp = documentNextEvents.select("#product_recommendations_scroll div[class=hotw_avp]");
		    	Elements pzn = documentNextEvents.select("#product_recommendations_scroll input[name=hotw_products_model]");
		    	Elements discount = documentNextEvents.select("#product_recommendations_scroll div[class=product_image product_image_200_200]");
// nếu không có data thì đợi thêm thời gian để page loaded  	
		    	if(title.size() == 0) {
	    		 dummySession.wait(getRandomNumberInRange(10000, 15000));
	    		 responseNextEvent = dummySession.getContent();
		    	 documentNextEvents = Jsoup.parse(responseNextEvent);
		    	 	 title = documentNextEvents.select("#product_recommendations_scroll div[class=hotw_productname]");
		    		 price = documentNextEvents.select("#product_recommendations_scroll div[class=hotw_price]");
		    		 avp = documentNextEvents.select("#product_recommendations_scroll div[class=hotw_avp]");
		    		 pzn = documentNextEvents.select("#product_recommendations_scroll input[name=hotw_products_model]");
		    		 discount = documentNextEvents.select("#product_recommendations_scroll div[class=product_image product_image_200_200]");
		    	}
		    	Integer placement =1;
		    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
		    	if(title != null && !title.isEmpty()) {
		    		for(int h=0;h<title.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(title.get(h).text());
		    			pharmaProduct.setQuantity(title.get(h).text().substring(title.get(h).text().indexOf("("), title.get(h).text().length()).replace("(", "").replace(")", ""));
		    			String avpText = avp.get(h).text();
		    			String priceText = price.get(h).text();
		    			pharmaProduct.setPrice(priceText.replace("*", "").trim());
		    			Double priceInDouble = convertStringToDouble(priceText.replace("€", "").replace(",", ".").replace("*", "").trim());
		    			pharmaProduct.setPriceInDouble(priceInDouble);
		    			if(avpText.indexOf("€") > -1) {
		    				Double avpInDouble = convertStringToDouble(avpText.replace("€", "").replace(",", ".").replace("*", "").trim());
			    			pharmaProduct.setAvp(avpInDouble);
		    			}else
		    				pharmaProduct.setAvp(priceInDouble);
		    			String discountText = discount.get(h).text();
		    			if(discountText.indexOf("%") > -1) {
		    				Integer number1 = discountText.indexOf("%");
		    				Integer discountNumber = convertStringToInteger(discountText.substring(0, number1));
		    				pharmaProduct.setDiscount(discountNumber);
		    			}else
		    				pharmaProduct.setDiscount(0);
		    			pharmaProduct.setPzn(pzn.get(h).val());
		    			pharmaProduct.setSequenceByKeyword(placement);
		    			pharmaProduct.setPharmaSetting(pharmaSettingMain);
		    			pharmaProduct.setKeyword(pharmaSettingMain.getName());
		    			pharmaProduct.setCrawlType(pharmaSettingMain.getType());
		    			pharmaProduct.setUrl(pharmaSettingMain.getApodiscounterUrl());
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
		    			if(h==29)
		    				break;
		    		}
		    		if(!CollectionUtils.isEmpty(pharmaProducts))
						mainService.createListDetail(pharmaProducts);
		    	}else
		    		System.out.println("Error at url: "+pharmaSettingMain.getApodiscounterUrl());
				}
	    	}
	    	System.out.println("DONE");
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
