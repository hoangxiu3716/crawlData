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
public class CrawlDataMedikamente {
	@Autowired
	private ParsehubService parsehubService;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private PharmaProductService pharmaProductService;
	public void crawlDataPharmaMedikamente() {
		ParsehubSetting parsehubSetting = parsehubService.findByCodeActiveTrue(Constants.Code.MEDIKAMENTE);
    	List<PharmaSetting> pharmaSetting = new ArrayList<PharmaSetting>();
    	pharmaSetting = settingService.findByActiveTrueAndMedikamenteUrlIsNotNull();
       Launcher launcher = new Launcher();
	   SessionFactory factory = launcher.launch();
	   Session dummySession = factory.create();
	   for(int k = 0;k<pharmaSetting.size();k++) {
			PharmaSetting pharmaSettingMain = pharmaSetting.get(k);
// lấy data nếu type = KEYWORD or CATEGORY
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.KEYWORD) || pharmaSettingMain.getType().equals(Constants.CrawlType.CATEGORY)) {
				Integer placement =1;
				String url = pharmaSettingMain.getMedikamenteUrl();
				dummySession.navigate(url);
		    	dummySession.waitDocumentReady(200000);
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	dummySession.evaluate("var button = document.querySelector(\"#usercentrics-root\").shadowRoot.querySelector(\"#uc-center-container > div.sc-jcFjpl.eUKJSw > div > div > div > button.sc-gsDKAQ.jRmpcI\");"
		    			+ "var click = function(){if(button != null)button.click();}");
		    	dummySession.callFunction("click");
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	String responseNextEvent = dummySession.getContent();
		    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
		    	Elements title = documentNextEvents.select(".productsList .boxProduct span[itemprop=name]");
		    	Elements quantity = documentNextEvents.select(".productsList .boxProduct dd[itemprop=model]");
		    	Elements pzn = documentNextEvents.select(".productsList .boxProduct dd[itemprop=productID]");
		    	Elements avp = documentNextEvents.select(".productsList .boxProduct div[class=col wide-3 align-right price] dl");
		    	Elements price = documentNextEvents.select(".productsList .boxProduct div[class=col wide-3 align-right price] dd[class=price lowPrice]");
		    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
		    	if(price != null && !price.isEmpty()) {
		    		for(int h=0;h<price.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(title.get(h).text());
		    			pharmaProduct.setQuantity(quantity.get(h).text());
		    			pharmaProduct.setPzn(pzn.get(h).text());
		    			pharmaProduct.setPrice(price.get(h).text());
		    			Double priceInDouble = convertStringToDouble(price.get(h).text().replace(",", ".").trim());
		    			pharmaProduct.setPriceInDouble(priceInDouble);
		    			String avpText = avp.get(h).text();
		    			if(avpText.indexOf("Sie sparen") > -1 || avpText.indexOf("%") > -1) {
		    				Integer number1 = avpText.indexOf(":");
		    				Integer number2 = avpText.indexOf("€");
		    				Double avpInDouble = convertStringToDouble(avpText.substring(number1, number2).replace(":", "").replace(",", ".").trim());
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
		    		System.out.println("LOI "+pharmaSettingMain.getMedikamenteUrl());
			}
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.CROSS_SELLING)) {
				Integer placement =1;
				String url = pharmaSettingMain.getMedikamenteUrl();
				dummySession.navigate(url);
		    	dummySession.waitDocumentReady(200000);
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	dummySession.evaluate("var button = document.querySelector(\"#usercentrics-root\").shadowRoot.querySelector(\"#uc-center-container > div.sc-jcFjpl.eUKJSw > div > div > div > button.sc-gsDKAQ.jRmpcI\");"
		    			+ "var click = function(){if(button != null)button.click();}");
		    	dummySession.callFunction("click");
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	String responseNextEvent = dummySession.getContent();
		    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
		    	Elements title = documentNextEvents.select("div[class=slick-track] .boxProduct span[itemprop=name]");
		    	Elements quantity = documentNextEvents.select("div[class=slick-track] .boxProduct dd[itemprop=model]");
		    	Elements pzn = documentNextEvents.select("div[class=slick-track] .boxProduct dd[itemprop=productID]");
		    	Elements avp = documentNextEvents.select("div[class=slick-track] .boxProduct div[class=col wide-3 align-right price] dl");
		    	Elements price = documentNextEvents.select("div[class=slick-track] .boxProduct div[class=col wide-3 align-right price] dd[class=price lowPrice]");
		    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
		    	if(price != null && !price.isEmpty()) {
		    		for(int h=0;h<price.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(title.get(h).text());
		    			pharmaProduct.setQuantity(quantity.get(h).text());
		    			pharmaProduct.setPzn(pzn.get(h).text());
		    			pharmaProduct.setPrice(price.get(h).text());
		    			Double priceInDouble = convertStringToDouble(price.get(h).text().replace(",", ".").trim());
		    			pharmaProduct.setPriceInDouble(priceInDouble);
		    			String avpText = avp.get(h).text();
		    			if(avpText.indexOf("Sie sparen") > -1 || avpText.indexOf("%") > -1) {
		    				Integer number1 = avpText.indexOf(":");
		    				Integer number2 = avpText.indexOf("€");
		    				Double avpInDouble = convertStringToDouble(avpText.substring(number1, number2).replace(":", "").replace(",", ".").trim());
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
		    		System.out.println("LOI "+pharmaSettingMain.getMedikamenteUrl());
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
