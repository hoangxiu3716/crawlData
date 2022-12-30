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
public class CrawlDataApotal {
	@Autowired
	private ParsehubService parsehubService;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private PharmaProductService pharmaProductService;
	public void crawlDataPharmaApotal() {
		ParsehubSetting parsehubSetting = parsehubService.findByCodeActiveTrue(Constants.Code.APOTAL);
    	List<PharmaSetting> pharmaSetting = new ArrayList<PharmaSetting>();
    	pharmaSetting = settingService.findByActiveTrueAndApotalUrlIsNotNull();
       Launcher launcher = new Launcher();
	   SessionFactory factory = launcher.launch();
	   Session dummySession = factory.create();
	   for(int k = 0;k<pharmaSetting.size();k++) {
			PharmaSetting pharmaSettingMain = pharmaSetting.get(k);
			Integer placement =1;
				String url = pharmaSettingMain.getApotalUrl();
				if(pharmaSettingMain.getType().equals(Constants.CrawlType.CATEGORY) || pharmaSettingMain.getType().equals(Constants.CrawlType.CROSS_SELLING)) {
					dummySession.navigate(url);
				}else {
					String keyword = pharmaSettingMain.getName();
					String urlFront = "https://shop.apotal.de/keywordsearch?sortBy=default&VIEW_SIZE=30&clearSearch=N&SEARCH_STRING=";
					String urlEnd = "&SEARCH_CATEGORY_ID=&anb=";
					String mainUrl = urlFront + keyword + urlEnd;
					dummySession.navigate(mainUrl);
				}
		    	dummySession.waitDocumentReady(200000);
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	dummySession.evaluate("var button = document.querySelector(\"#cookie-message > div:nth-child(2) > div.mb-wrap > button.mb3.accept-all-cookies\");"
			 			+ "var click = function(){if(button != null)button.click();}");
			 	dummySession.callFunction("click");
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	String responseNextEvent = dummySession.getContent();
		    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
		    	Elements title = documentNextEvents.select("div[class=boxProduct] h3");
		    	Elements price = documentNextEvents.select("div[class=boxProduct] div[class=productInformations]");
		    	Elements pzn = documentNextEvents.select("div[class=boxProduct] dd[itemprop=productID]");
		    	Elements quantity = documentNextEvents.select("div[class=boxProduct] div[class=productInformations] dd[itemprop=model]");
		    	Elements priceCrossSelling = documentNextEvents.select("div[class=boxProduct] dd[class=yourPrice]");
		    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
		    	if(title != null && !title.isEmpty()) {
		    		for(int h=0;h<title.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(title.get(h).text());
		    			pharmaProduct.setPzn(pzn.get(h).text());
		    			pharmaProduct.setQuantity(quantity.get(h).text());
	    				String avpText = price.get(h).text();
	    				if(avpText.indexOf("%") > -1) {
	    					String discountText = avpText.substring(avpText.indexOf("%")-3, avpText.indexOf("%")).replace("(", "").trim();
	    					Integer discountInNumber = convertStringToInteger(discountText);
	    					pharmaProduct.setDiscount(discountInNumber);
	    					Double avpInDouble = convertStringToDouble(avpText.substring(avpText.indexOf("VP"), avpText.indexOf("€")).replace("VP", "").replace("*", "").replace(",", ".").trim());
	    					pharmaProduct.setAvp(avpInDouble);
	    					Double priceInDouble = convertStringToDouble(avpText.substring(avpText.indexOf("Unser Preis"), avpText.indexOf("Sie sparen")).replace("Unser Preis", "").replace("*", "").replace(",", ".").replace("€", "").trim());
	    					pharmaProduct.setPriceInDouble(priceInDouble);
	    					pharmaProduct.setPrice(avpText.substring(avpText.indexOf("Unser Preis"), avpText.indexOf("Sie sparen")).replace("Unser Preis", "").replace("*", "").replace(",", ".").trim());
	    				}else if(avpText.indexOf("Unser Preis") > -1) {
	    					pharmaProduct.setDiscount(0);
	    					Double priceInDouble = convertStringToDouble(avpText.substring(avpText.indexOf("Unser Preis"), avpText.indexOf("€")).replace("Unser Preis", "").replace("*", "").replace(",", ".").trim());
	    					pharmaProduct.setPriceInDouble(priceInDouble);
	    					pharmaProduct.setAvp(priceInDouble);
	    					pharmaProduct.setPrice(avpText.substring(avpText.indexOf("Unser Preis"), avpText.indexOf("€")).replace("Unser Preis", "").replace("*", "").replace(",", ".").trim());
	    				}
	    				if(pharmaSettingMain.getType().equals(Constants.CrawlType.CROSS_SELLING)) {
	    					Double priceInDouble = convertStringToDouble(priceCrossSelling.get(h).ownText().replace(",", ".").replace("€", "").trim());
	    					pharmaProduct.setPriceInDouble(priceInDouble);
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
		    		System.out.println("LOI "+pharmaSettingMain.getApotalUrl());
			
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
