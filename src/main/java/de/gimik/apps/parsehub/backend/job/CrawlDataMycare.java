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
public class CrawlDataMycare {
	@Autowired
	private ParsehubService parsehubService;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private PharmaProductService pharmaProductService;
	public void crawlDataPharmaMycare() {
		ParsehubSetting parsehubSetting = parsehubService.findByCodeActiveTrue(Constants.Code.MYCARE);
    	List<PharmaSetting> pharmaSetting = new ArrayList<PharmaSetting>();
    	pharmaSetting = settingService.findByActiveTrueAndMycareUrlIsNotNull();
       Launcher launcher = new Launcher();
	   SessionFactory factory = launcher.launch();
	   Session dummySession = factory.create();
	   for(int k = 0;k<pharmaSetting.size();k++) {
			PharmaSetting pharmaSettingMain = pharmaSetting.get(k);
// lấy data nếu type = KEYWORD or CATEGORY
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.KEYWORD) || pharmaSettingMain.getType().equals(Constants.CrawlType.CATEGORY)) {
				Integer placement =1;
				String url = pharmaSettingMain.getMycareUrl();
				dummySession.navigate(url);
		    	dummySession.waitDocumentReady(200000);
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	dummySession.evaluate("var button = document.querySelector(\"#btn-cookie-accept\");"
			 			+ "var click = function(){if(button != null)button.click();}");
			 	dummySession.callFunction("click");
			 	dummySession.wait(getRandomNumberInRange(3000, 5000));
			 	dummySession.evaluate("var buttonForMore = document.querySelector(\"#page-size-form > div.selectric-wrapper.selectric-js-select-page-size.selectric-select-transparent > div.selectric-items > div > ul > li.page-size.size-50\");"
			 			+ "var clickForMore = function(){if(buttonForMore != null)buttonForMore.click();}");
			 	dummySession.callFunction("clickForMore");
		    	dummySession.wait(getRandomNumberInRange(10000, 15000));
		    	String responseNextEvent = dummySession.getContent();
		    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
		    	Elements title = documentNextEvents.select("div[class=product-view product-view--grid row] div[class=product-item product-item--grid js-product-item--grid ]");
		        Elements quantity = documentNextEvents.select("div[class=product-view product-view--grid row] div[class=product-item product-item--grid js-product-item--grid] span[class=product-item__name]");
		        Elements price = documentNextEvents.select("div[class=product-view product-view--grid row] div[class=product-item product-item--grid js-product-item--grid] div[class=product-item__price-line]");
		        Elements discount = documentNextEvents.select("div[class=product-view product-view--grid row] div[class=product-item product-item--grid js-product-item--grid] div[class=product-item__link product-item__link--desktop]");
		    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
		    	if(title != null && !title.isEmpty()) {
		    		for(int h=0;h<title.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(title.get(h).dataset().get("product-name"));
		    			pharmaProduct.setPzn(title.get(h).dataset().get("product-code"));
		    			if(quantity.get(h).text().indexOf(",") >-1) {
		    				pharmaProduct.setQuantity(quantity.get(h).text().substring(quantity.get(h).text().indexOf(",")).replace(",", ""));
		    			}
		    			String priceText = price.get(h).text();
		    			Integer number1 = priceText.indexOf("€", priceText.indexOf("€")+1);
		    			if(number1 >-1 && priceText.indexOf("€") >-1) {
		    				Double priceInDouble = convertStringToDouble(priceText.substring(0, priceText.indexOf("€")-1).replace(",", ".").trim());
		    				pharmaProduct.setPriceInDouble(priceInDouble);
		    				Double avpInDouble = convertStringToDouble(priceText.substring(priceText.indexOf("€")+3, priceText.lastIndexOf("€")-1).replace(",", ".").trim());
		    				pharmaProduct.setAvp(avpInDouble);
		    				
		    			}else{
		    				Double priceInDouble = convertStringToDouble(priceText.substring(0, priceText.indexOf("€")-1).replace(",", ".").trim());
		    				pharmaProduct.setPriceInDouble(priceInDouble);
		    				pharmaProduct.setAvp(priceInDouble);
		    			}
		    			if(discount.get(h).text().indexOf("%") > -1) {
		    				Integer discountInNumber = convertStringToInteger(discount.get(h).text().substring(0, discount.get(h).text().indexOf("%")).replace("-", "").trim());
		    				pharmaProduct.setDiscount(discountInNumber);
		    			}else
		    				pharmaProduct.setDiscount(0);
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
		    		System.out.println("LOI "+pharmaSettingMain.getMedpexUrl());
			}
			//cross-selling
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.CROSS_SELLING)) {
				Integer placement =1;
				String url = pharmaSettingMain.getMycareUrl();
				dummySession.navigate(url);
		    	dummySession.waitDocumentReady(200000);
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	dummySession.evaluate("var button = document.querySelector(\"#btn-cookie-accept\");"
			 			+ "var click = function(){if(button != null)button.click();}");
			 	dummySession.callFunction("click");
		    	dummySession.wait(getRandomNumberInRange(10000, 15000));
		    	String responseNextEvent = dummySession.getContent();
		    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
		    	Elements title = documentNextEvents.select("div[class=productGrid product-slider-wrap] div[class*=product-item tns-item] span[class=product-item__name]");
		    	Elements pzn = documentNextEvents.select("div[class=productGrid product-slider-wrap] div[class*=product-item tns-item] input[class=productCodePost]");
		        Elements price = documentNextEvents.select("div[class=productGrid product-slider-wrap] div[class*=product-item tns-item] div[class=product-item__price-container]");
		        Elements discount = documentNextEvents.select("div[class=productGrid product-slider-wrap] div[class*=product-item tns-item] span[class=redir-link product-item__link product-item__link--desktop]");
		    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
		    	if(title != null && !title.isEmpty()) {
		    		for(int h=0;h<title.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(title.get(h).text());
		    			pharmaProduct.setPzn(pzn.get(h).val());
		    			if(title.get(h).text().indexOf(",") >-1) {
		    				pharmaProduct.setQuantity(title.get(h).text().substring(title.get(h).text().indexOf(",")).replace(",", ""));
		    			}
		    			String priceText = price.get(h).text();
		    			Integer number1 = priceText.indexOf("€", priceText.indexOf("€")+1);
		    			if(number1 >-1 && priceText.indexOf("€") >-1) {
		    				Double priceInDouble = convertStringToDouble(priceText.substring(0, priceText.indexOf("€")-1).replace(",", ".").trim());
		    				pharmaProduct.setPriceInDouble(priceInDouble);
		    				Double avpInDouble = convertStringToDouble(priceText.substring(priceText.indexOf("€")+3, priceText.lastIndexOf("€")-1).replace(",", ".").trim());
		    				pharmaProduct.setAvp(avpInDouble);
		    				
		    			}else{
		    				Double priceInDouble = convertStringToDouble(priceText.substring(0, priceText.indexOf("€")-1).replace(",", ".").trim());
		    				pharmaProduct.setPriceInDouble(priceInDouble);
		    				pharmaProduct.setAvp(priceInDouble);
		    			}
		    			if(discount.get(h).text().indexOf("%") > -1) {
		    				Integer discountInNumber = convertStringToInteger(discount.get(h).text().substring(0, discount.get(h).text().indexOf("%")).replace("-", "").trim());
		    				pharmaProduct.setDiscount(discountInNumber);
		    			}else
		    				pharmaProduct.setDiscount(0);
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
		    		System.out.println("LOI "+pharmaSettingMain.getMedpexUrl());
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
