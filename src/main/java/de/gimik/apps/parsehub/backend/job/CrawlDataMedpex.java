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
public class CrawlDataMedpex {
	@Autowired
	private ParsehubService parsehubService;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private PharmaProductService pharmaProductService;
	public void crawlDataPharmaMedpex() {
		ParsehubSetting parsehubSetting = parsehubService.findByCodeActiveTrue(Constants.Code.MEDPEX);
    	List<PharmaSetting> pharmaSetting = new ArrayList<PharmaSetting>();
    	pharmaSetting = settingService.findByActiveTrueAndMedpexUrlIsNotNull();
       Launcher launcher = new Launcher();
	   SessionFactory factory = launcher.launch();
	   Session dummySession = factory.create();
	   for(int k = 0;k<pharmaSetting.size();k++) {
			PharmaSetting pharmaSettingMain = pharmaSetting.get(k);
// lấy data nếu type = KEYWORD or CATEGORY
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.KEYWORD) || pharmaSettingMain.getType().equals(Constants.CrawlType.CATEGORY)) {
				Integer placement =1;
				String url = pharmaSettingMain.getMedpexUrl();
				for(int i=1; i <= 10;i++) {
				if(i>1 && pharmaSettingMain.getType().equals(Constants.CrawlType.KEYWORD)) {
					url = url+"&pn="+i;
				}else if(i > 1) {
					url = url+i;
				}
				dummySession.navigate(url);
				url = pharmaSettingMain.getMedpexUrl();
		    	dummySession.waitDocumentReady(200000);
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	dummySession.evaluate("var button = document.querySelector(\"#cmpwelcomebtnyes > a\");"
		    			+ "var click = function(){if(button != null)button.click();}");
		    	dummySession.callFunction("click");
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	String responseNextEvent = dummySession.getContent();
		    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
		    	Elements title = documentNextEvents.select("#product-list .data-tracking-product");
		    	Elements avp = documentNextEvents.select("#product-list .data-tracking-product div[class=transaction]");
		    	Elements quantity = documentNextEvents.select("#product-list .data-tracking-product div[class=description]");
		    	Elements pageLength = documentNextEvents.select("div[class=pagenav] td[class=index]");
		    	if(pageLength.size() > 0) {
		    		if(pageLength.get(0).text().indexOf("von") > -1) {
			    		Integer pageLengthNumber1 = pageLength.get(0).text().indexOf("von");
			    		String pageLengthText = pageLength.get(0).text().substring(pageLengthNumber1, pageLength.get(0).text().length());
				    	Integer pageNumber = convertStringToInteger(pageLengthText.replace("von", "").trim());
				    	if(i > pageNumber)
				    		break;
			    	}
		    	}
		    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
		    	if(title != null && !title.isEmpty()) {
		    		for(int h=0;h<title.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(title.get(h).dataset().get("tracking-product-name"));
		    			pharmaProduct.setPzn(title.get(h).dataset().get("tracking-product-code"));
		    			pharmaProduct.setPrice(title.get(h).dataset().get("tracking-product-price"));
		    			String priceText = title.get(h).dataset().get("tracking-product-price");
		    			Double priceInDouble = convertStringToDouble(priceText.replace("&euro;", "").replace(",", ".").trim());
		    			pharmaProduct.setPriceInDouble(priceInDouble);
		    			String avpText = avp.get(h).text();
		    			if(avpText.indexOf("*") > -1 && avpText.indexOf("€") > -1) {
		    				Integer number1 = avpText.indexOf("*");
		    				Integer number2 = avpText.indexOf("€");
		    				Double avpInDouble = convertStringToDouble(avpText.substring(number2+1, number1).replace(",", ".").replace("€", "").trim());
		    				pharmaProduct.setAvp(avpInDouble);
		    				Double discountFinally = 100-(priceInDouble / avpInDouble*100);
			    			Integer discount =  (int) Math.round(discountFinally);
			    			pharmaProduct.setDiscount(discount);
		    			}else {
		    				pharmaProduct.setAvp(priceInDouble);
			    			pharmaProduct.setDiscount(0);
		    			}
		    			String quantityText = quantity.get(h).ownText();
		    			if(quantityText.indexOf(",") > -1) {
		    				Integer number1 = quantityText.indexOf(",");
		    				if(quantityText.indexOf("Stück") > -1) {
		    					pharmaProduct.setQuantity(quantityText.substring(number1+1, quantityText.indexOf("Stück")).replace(",", "").trim()+" Stück");
		    				}else if(quantityText.indexOf("Milliliter") > -1) {
		    					pharmaProduct.setQuantity(quantityText.substring(number1+1, quantityText.indexOf("Milliliter")).replace(",", "").trim()+" Milliliter");
		    				}else if(quantityText.indexOf("Gramm") > -1) {
		    					pharmaProduct.setQuantity(quantityText.substring(number1+1, quantityText.indexOf("Gramm")).replace(",", "").trim()+" Gramm");
		    				}else if(quantityText.indexOf("Packung") > -1) {
		    					pharmaProduct.setQuantity(quantityText.substring(number1+1, quantityText.indexOf("Packung")).replace(",", "").trim()+" Packung");
		    				}
		    			}else {
		    				if(quantityText.indexOf("Stück") > -1) {
		    					pharmaProduct.setQuantity(quantityText.substring(0, quantityText.indexOf("Stück")).replace(",", "").trim()+" Stück");
		    				}else if(quantityText.indexOf("Milliliter") > -1) {
		    					pharmaProduct.setQuantity(quantityText.substring(0, quantityText.indexOf("Milliliter")).replace(",", "").trim()+" Milliliter");
		    				}else if(quantityText.indexOf("Gramm") > -1) {
		    					pharmaProduct.setQuantity(quantityText.substring(0, quantityText.indexOf("Gramm")).replace(",", "").trim()+" Gramm");
		    				}else if(quantityText.indexOf("Packung") > -1) {
		    					pharmaProduct.setQuantity(quantityText.substring(0, quantityText.indexOf("Packung")).replace(",", "").trim()+" Packung");
		    				}
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
		    	}else {
		    		System.out.println("LOI "+pharmaSettingMain.getMedpexUrl());
		    		if(i > 1) {
		    			break;
		    		}	
		    	}
		    	if(placement > 30)
    				break;
			}
			}
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.CROSS_SELLING)) {
				Integer placement =1;
				String url = pharmaSettingMain.getMedpexUrl();
				dummySession.navigate(url);
		    	dummySession.waitDocumentReady(200000);
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	dummySession.evaluate("var button = document.querySelector(\"#cmpwelcomebtnyes > a\");"
		    			+ "var click = function(){if(button != null)button.click();}");
		    	dummySession.callFunction("click");
		    	dummySession.wait(getRandomNumberInRange(5000, 10000));
		    	String responseNextEvent = dummySession.getContent();
		    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
		    	Elements title = documentNextEvents.select("#personalization-100010787-swiper-container div[class=productListEntry] > a");
		    	Elements quantity = documentNextEvents.select("#personalization-100010787-swiper-container div[class=productListEntry] span[class=small]");
		    	Elements avp = documentNextEvents.select("#personalization-100010787-swiper-container div[class=productListEntry] span[class=sp2p normal-price-crossedout]");
		    	Elements price = documentNextEvents.select("#personalization-100010787-swiper-container div[class=productListEntry] span[class=normal-price]");
		    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
		    	if(title != null && !title.isEmpty()) {
		    		for(int h=0;h<title.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(title.get(h).dataset().get("tracking-promotion-creative"));
		    			pharmaProduct.setPzn(title.get(h).dataset().get("tracking-promotion-id"));
		    			String avpText = avp.get(h).text();
		    			String priceText = price.get(h).text();
		    			pharmaProduct.setPrice(priceText);
		    			pharmaProduct.setQuantity(quantity.get(h).text());
		    			Double priceInDouble = convertStringToDouble(priceText.replace("*", "").replace(",", ".").replace("€", "").trim());
		    			pharmaProduct.setPriceInDouble(priceInDouble);
		    			Double avpInDouble = convertStringToDouble(avpText.replace("*", "").replace(",", ".").replace("€", "").trim());
		    			if(avpText.replace("*", "").replace(",", ".").replace("€", "").trim().indexOf("0.0") > -1) {
		    				pharmaProduct.setAvp(priceInDouble);
		    			}else
		    				pharmaProduct.setAvp(avpInDouble);
		    			Double discountFinally = 100-(priceInDouble / avpInDouble*100);
		    			Integer discount =  (int) Math.round(discountFinally);
		    			pharmaProduct.setDiscount(discount);
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
