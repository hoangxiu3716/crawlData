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
public class CrawlDataDocmorris {
	@Autowired
	private ParsehubService parsehubService;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private PharmaProductService pharmaProductService;
	public void crawlDataPharmaDocmorris() {
		ParsehubSetting parsehubSetting = parsehubService.findByCodeActiveTrue(Constants.Code.DOCMORRIS);
    	List<PharmaSetting> pharmaSetting = new ArrayList<PharmaSetting>();
    	pharmaSetting = settingService.findByActiveTrueAndDocmorrisUrlIsNotNull();
       Launcher launcher = new Launcher();
	   SessionFactory factory = launcher.launch();
	   Session dummySession = factory.create();
	   for(int k = 0;k<pharmaSetting.size();k++) {
			PharmaSetting pharmaSettingMain = pharmaSetting.get(k);
			Integer placement =1;
// GET DATA IF TYPE = KEYWORD or CATEGORY
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.KEYWORD) || pharmaSettingMain.getType().equals(Constants.CrawlType.CATEGORY)) {
				String url = pharmaSettingMain.getDocmorrisUrl();
				dummySession.navigate(url);
		    	dummySession.waitDocumentReady(200000);
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	dummySession.evaluate("var button = document.querySelector(\"#cmpwelcomebtnyes > a\");"
			 			+ "var click = function(){if(button != null)button.click();}");
			 	dummySession.callFunction("click");
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	String responseNextEvent = dummySession.getContent();
		    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
		    	Elements title = documentNextEvents.select("div[class=row productlist__row] h2");
		    	Elements pzn = documentNextEvents.select("div[class=row productlist__row] a[class=row productlist__tile]");
		    	Elements quantity = documentNextEvents.select("div[class=row productlist__row] a[class=row productlist__tile] span[class=size packagingSize]");
		    	Elements avp = documentNextEvents.select("div[class=row productlist__row] a[class=row productlist__tile] div[class=productlist__description pt1 col-xs-8]");
		    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
		    	if(title != null && !title.isEmpty()) {
		    		for(int h=0;h<title.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(title.get(h).text());
		    			pharmaProduct.setPzn(pzn.get(h).dataset().get("pzn"));
		    			pharmaProduct.setQuantity(quantity.get(h).text());
		    			String avpText = avp.get(h).text();
		    			if(avpText.lastIndexOf("VP") > -1) {
		    				Double avpInDouble = convertStringToDouble(avpText.substring(avpText.lastIndexOf("VP"), avpText.indexOf("€")).replace("VP*", "").replace(",", ".").trim());
		    				pharmaProduct.setAvp(avpInDouble);
		    				String priceText = avpText.substring(avpText.lastIndexOf("VP"), avpText.lastIndexOf("€"));
		    				Double priceInDouble = convertStringToDouble(priceText.substring(priceText.indexOf("€"), priceText.lastIndexOf("€")).replace("€", "").replace(",", ".").trim());
		    				pharmaProduct.setPriceInDouble(priceInDouble);
		    				pharmaProduct.setPrice(priceText.substring(priceText.indexOf("€"), priceText.lastIndexOf("€")).trim());
		    				Double discountFinally = 100-(priceInDouble / avpInDouble*100);
			    			Integer discount =  (int) Math.round(discountFinally);
			    			pharmaProduct.setDiscount(discount);
		    				}else if(avpText.indexOf("€") > -1) {
		    				Double priceInDouble = convertStringToDouble(avpText.substring(avpText.indexOf("€")-10, avpText.indexOf("€")).replaceAll("[^\\d,]", "").replace(",", ".").trim());
		    				pharmaProduct.setPriceInDouble(priceInDouble);
		    				pharmaProduct.setAvp(priceInDouble);
		    				pharmaProduct.setPrice(avpText.substring(avpText.indexOf("€")-10, avpText.indexOf("€")).replaceAll("[^\\d,]", "").trim());
		    				pharmaProduct.setDiscount(0);
		    				}
		    			pharmaProduct.setSequenceByKeyword(placement);
		    			pharmaProduct.setPharmaSetting(pharmaSettingMain);
		    			pharmaProduct.setKeyword(pharmaSettingMain.getName());
		    			pharmaProduct.setCrawlType(pharmaSettingMain.getType());
		    			pharmaProduct.setUrl(url);
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
		    			if(placement > 30)
		    				break;
		    		}
		    		if(!CollectionUtils.isEmpty(pharmaProducts))
						mainService.createListDetail(pharmaProducts);
		    	}else
		    		System.out.println("LOI "+pharmaSettingMain.getDocmorrisUrl());
			}
// GET DATA IF TYPE = CROSS-SELLING
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.CROSS_SELLING)) {
				String url = pharmaSettingMain.getDocmorrisUrl();
				dummySession.navigate(url);
		    	dummySession.waitDocumentReady(200000);
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	dummySession.evaluate("var button = document.querySelector(\"#cmpwelcomebtnyes > a\");"
			 			+ "var click = function(){if(button != null)button.click();}");
			 	dummySession.callFunction("click");
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	String responseNextEvent = dummySession.getContent();
		    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
		    	Elements title = documentNextEvents.select("#slider-nr-1 h2");
		    	Elements pzn = documentNextEvents.select("#slider-nr-1 a[class=row productlist__tile productlist__tile--no-mobile productlist__tile--flat]");
		    	Elements quantity = documentNextEvents.select("#slider-nr-1 span[class=productlist__size]");
		    	Elements avp = documentNextEvents.select("#slider-nr-1 div[class=productlist__price-wrapper]");
		    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
		    	if(title != null && !title.isEmpty()) {
		    		for(int h=0;h<title.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(title.get(h).text());
		    			pharmaProduct.setPzn(pzn.get(h).dataset().get("pzn"));
		    			pharmaProduct.setQuantity(quantity.get(h).text());
		    			String avpText = avp.get(h).text();
		    			if(avpText.lastIndexOf("VP") > -1) {
		    				Double avpInDouble = convertStringToDouble(avpText.substring(avpText.lastIndexOf("VP"), avpText.indexOf("€")).replace("VP*", "").replace(",", ".").trim());
		    				pharmaProduct.setAvp(avpInDouble);
		    				String priceText = avpText.substring(avpText.lastIndexOf("VP"), avpText.lastIndexOf("€"));
		    				Double priceInDouble = convertStringToDouble(priceText.substring(priceText.indexOf("€"), priceText.lastIndexOf("€")).replace("€", "").replace(",", ".").trim());
		    				pharmaProduct.setPriceInDouble(priceInDouble);
		    				pharmaProduct.setPrice(priceText.substring(priceText.indexOf("€"), priceText.lastIndexOf("€")).trim());
		    				Double discountFinally = 100-(priceInDouble / avpInDouble*100);
			    			Integer discount =  (int) Math.round(discountFinally);
			    			pharmaProduct.setDiscount(discount);
		    				}else if(avpText.indexOf("€") > -1) {
		    				Double priceInDouble = convertStringToDouble(avpText.substring(avpText.indexOf("€")-10, avpText.indexOf("€")).replaceAll("[^\\d,]", "").replace(",", ".").trim());
		    				pharmaProduct.setPriceInDouble(priceInDouble);
		    				pharmaProduct.setAvp(priceInDouble);
		    				pharmaProduct.setPrice(avpText.substring(avpText.indexOf("€")-10, avpText.indexOf("€")).replaceAll("[^\\d,]", "").trim());
		    				pharmaProduct.setDiscount(0);
		    				}
		    			pharmaProduct.setSequenceByKeyword(placement);
		    			pharmaProduct.setPharmaSetting(pharmaSettingMain);
		    			pharmaProduct.setKeyword(pharmaSettingMain.getName());
		    			pharmaProduct.setCrawlType(pharmaSettingMain.getType());
		    			pharmaProduct.setUrl(url);
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
		    			if(placement > 30)
		    				break;
		    		}
		    		if(!CollectionUtils.isEmpty(pharmaProducts))
						mainService.createListDetail(pharmaProducts);
		    	}else
		    		System.out.println("LOI "+pharmaSettingMain.getDocmorrisUrl());
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
