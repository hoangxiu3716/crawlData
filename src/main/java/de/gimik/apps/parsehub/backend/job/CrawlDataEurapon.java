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
public class CrawlDataEurapon {
	@Autowired
	private ParsehubService parsehubService;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private PharmaProductService pharmaProductService;
	public void crawlDataPharmaEurapon() {
		ParsehubSetting parsehubSetting = parsehubService.findByCodeActiveTrue(Constants.Code.EURAPON);
    	List<PharmaSetting> pharmaSetting = new ArrayList<PharmaSetting>();
    	pharmaSetting = settingService.findByActiveTrueAndEuraponUrlIsNotNull();
        Launcher launcher = new Launcher();
    	SessionFactory factory = launcher.launch();
    	Session dummySession = factory.create();
    	for(int k = 0;k<pharmaSetting.size();k++) {
		PharmaSetting pharmaSettingMain = pharmaSetting.get(k);
		if(pharmaSettingMain.getType().equals(Constants.CrawlType.KEYWORD) || pharmaSettingMain.getType().equals(Constants.CrawlType.CATEGORY)) {
			String url = pharmaSettingMain.getEuraponUrl();
			dummySession.navigate(url);
			dummySession.waitDocumentReady(200000);
			dummySession.wait(getRandomNumberInRange(5000, 10000));
			dummySession.evaluate("var button = document.querySelector(\"#cmpwrapper\").shadowRoot.querySelector(\"#cmpwelcomebtnyes > a\");"
					+ "var click = function(){if(button != null)button.click();}");
			dummySession.callFunction("click");
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.KEYWORD)) {
				dummySession.evaluate("var forMore = document.querySelector(\"body > div.page-wrap > main > section > div > div > div.produktliste.container > div.ff-row-container > div.ff-row-container__item.ff-row-container__item--products > div.produktliste-headerbar.jsFfShowOnLoad.ff-hasloaded > form > div > div.produktliste-headerbar__control.product-limit > div > ff-products-per-page-list > ff-products-per-page-item:nth-child(2)\");"
						+ "var clickForMore = function(){if(forMore != null)forMore.click();}");
				dummySession.callFunction("clickForMore");
			}else {
				dummySession.evaluate("var forMore = document.querySelector(\"body > div.page-wrap > main > section > div > div.produktliste.container > div > div.ff-row-container__item.ff-row-container__item--products > div.produktliste-headerbar.jsFfShowOnLoad.ff-hasloaded > form > div > div.produktliste-headerbar__control.product-limit > div > ff-products-per-page-list > ff-products-per-page-item:nth-child(2)\");"
						+ "var clickForMore = function(){if(forMore != null)forMore.click();}");
				dummySession.callFunction("clickForMore");
			}
			dummySession.wait(getRandomNumberInRange(10000, 15000));
			String responseNextEvent = dummySession.getContent();
			org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
			Integer placement =1;
			Elements title = documentNextEvents.select("#mainProductList article[class=produktliste-listitem__itembox] p[class=produktliste-listitem__header headline6]");
			Elements price = documentNextEvents.select("#mainProductList article[class=produktliste-listitem__itembox] p[class=produktliste-listitem__currentprice]");
			Elements avp = documentNextEvents.select("#mainProductList article[class=produktliste-listitem__itembox] p[class=produktliste-listitem__oldprice hide-ingallery]");
			Elements pzn = documentNextEvents.select("#mainProductList article[class=produktliste-listitem__itembox] li[data-cbl-type=\"pzn\"]");
			Elements quantity = documentNextEvents.select("#mainProductList article[class=produktliste-listitem__itembox] li[data-cbl-type=\"inhalt\"]");
			Elements discount = documentNextEvents.select("#mainProductList article[class=produktliste-listitem__itembox] div[class=produktliste-listitem__imagebox]");
	    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
//best seller on category Erkältung
	    	if(pharmaSettingMain.getId().equals(19)) {
	    		Elements promoTitle = documentNextEvents.select("ul[class=produktliste-list productswiper__items swiper-wrapper produktliste-list-gallery] article[class=produktliste-listitem__itembox] p[class=produktliste-listitem__header headline6]");
		    	Elements promoPrice = documentNextEvents.select("ul[class=produktliste-list productswiper__items swiper-wrapper produktliste-list-gallery] article[class=produktliste-listitem__itembox] p[class=produktliste-listitem__currentprice]");
		    	Elements promoAvp = documentNextEvents.select("ul[class=produktliste-list productswiper__items swiper-wrapper produktliste-list-gallery] article[class=produktliste-listitem__itembox] p[class=produktliste-listitem__oldprice hide-ingallery]");
		    	Elements promoPzn = documentNextEvents.select("ul[class=produktliste-list productswiper__items swiper-wrapper produktliste-list-gallery] article[class=produktliste-listitem__itembox] li[data-cbl-type=\"pzn\"]");
		    	Elements promoQuantity = documentNextEvents.select("ul[class=produktliste-list productswiper__items swiper-wrapper produktliste-list-gallery] article[class=produktliste-listitem__itembox] li[data-cbl-type=\"inhalt\"]");
		    	Elements promoDiscount = documentNextEvents.select("ul[class=produktliste-list productswiper__items swiper-wrapper produktliste-list-gallery] article[class=produktliste-listitem__itembox] div[class=produktliste-listitem__imagebox]");
		    	if(promoTitle != null && !promoTitle.isEmpty()) {
		    		for(int h=0;h<promoTitle.size();h++) {
		    			PharmaDetail pharmaProduct = new PharmaDetail();
		    			pharmaProduct.setName(promoTitle.get(h).text());
		    			pharmaProduct.setQuantity(promoQuantity.get(h).text());
		    			pharmaProduct.setPzn(promoPzn.get(h).text());
		    			String avpText = promoAvp.get(h).text();
		    			String priceText = promoPrice.get(h).text();
		    			pharmaProduct.setPrice(priceText);
		    			String priceInString = priceText.replace("nur ", "").replace("€", "").replace(",", ".").trim();
		    			Double priceInDouble = convertStringToDouble(priceInString.substring(0, priceInString.length()-1));
		    			pharmaProduct.setPriceInDouble(priceInDouble);
		    			if(avpText.indexOf("statt") > -1) {
		    				String avpInString = avpText.replace("statt ", "").replace("€", "").replace(",", ".").replace("*", "").trim();
		    				Double avpInDouble = convertStringToDouble(avpInString.substring(0, avpInString.length()-1));
		    				pharmaProduct.setAvp(avpInDouble);
		    			}else
		    				pharmaProduct.setAvp(priceInDouble);
		    			String discountText = promoDiscount.get(h).text();
		    			if(discountText.indexOf("%") > -1) {
		    				Integer number1 = discountText.indexOf("-");
		    				Integer number2 = discountText.indexOf("%");
		    				String discountInString = discountText.substring(number1+1, number2).trim();
		    				Integer discountInteger = convertStringToInteger(discountInString);
			    			pharmaProduct.setDiscount(discountInteger);
		    			}else
		    				pharmaProduct.setDiscount(0);
		    			pharmaProduct.setSequenceByKeyword(placement);
		    			pharmaProduct.setPharmaSetting(pharmaSettingMain);
		    			pharmaProduct.setKeyword(pharmaSettingMain.getName());
		    			pharmaProduct.setCrawlType(pharmaSettingMain.getType());
		    			pharmaProduct.setUrl(pharmaSettingMain.getEuraponUrl());
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
		    		System.out.println("loi promo"+pharmaSettingMain.getEuraponUrl());
	    	}
	    	if(title != null && !title.isEmpty()) {
	    		for(int h=0;h<title.size();h++) {
	    			PharmaDetail pharmaProduct = new PharmaDetail();
	    			pharmaProduct.setName(title.get(h).text());
	    			pharmaProduct.setQuantity(quantity.get(h).text());
	    			pharmaProduct.setPzn(pzn.get(h).text().replace("PZN: ", "").trim());
	    			String avpText = avp.get(h).text();
	    			String priceText = price.get(h).text();
	    			pharmaProduct.setPrice(priceText);
	    			String priceInString = priceText.replace("nur ", "").replace("€", "").replace(",", ".").trim();
	    			Double priceInDouble = convertStringToDouble(priceInString);
	    			pharmaProduct.setPriceInDouble(priceInDouble);
	    			if(avpText.indexOf("statt") > -1) {
	    				String avpInString = avpText.replace("statt ", "").replace("€", "").replace(",", ".").replace("*", "").trim();
	    				Double avpInDouble = convertStringToDouble(avpInString);
	    				pharmaProduct.setAvp(avpInDouble);
	    			}else
	    				pharmaProduct.setAvp(priceInDouble);
	    			String discountText = discount.get(h).text();
	    			if(discountText.indexOf("%") > -1) {
	    				Integer number1 = discountText.indexOf("-");
	    				Integer number2 = discountText.indexOf("%");
	    				String discountInString = discountText.substring(number1+1, number2).trim();
	    				Integer discountInteger = convertStringToInteger(discountInString);
		    			pharmaProduct.setDiscount(discountInteger);
	    			}else
	    				pharmaProduct.setDiscount(0);
	    			pharmaProduct.setSequenceByKeyword(placement);
	    			pharmaProduct.setPharmaSetting(pharmaSettingMain);
	    			pharmaProduct.setKeyword(pharmaSettingMain.getName());
	    			pharmaProduct.setCrawlType(pharmaSettingMain.getType());
	    			pharmaProduct.setUrl(pharmaSettingMain.getEuraponUrl());
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
	    		System.out.println("loi"+pharmaSettingMain.getEuraponUrl());
			}
		
		if(pharmaSettingMain.getType().equals(Constants.CrawlType.CROSS_SELLING)) {
			String url = pharmaSettingMain.getEuraponUrl();
			dummySession.navigate(url);
			dummySession.waitDocumentReady(200000);
			dummySession.wait(getRandomNumberInRange(5000, 10000));
			dummySession.evaluate("var button = document.querySelector(\"#cmpwrapper\").shadowRoot.querySelector(\"#cmpwelcomebtnyes > a\");"
					+ "var click = function(){if(button != null)button.click();}");
			dummySession.callFunction("click");
			dummySession.wait(getRandomNumberInRange(10000, 15000));
			String responseNextEvent = dummySession.getContent();
			org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
			Elements title = documentNextEvents.select("div[class=productswiper__swiper-container swiper-container produktliste jsFFWKSwiper swiper-container-initialized swiper-container-horizontal] p[class=produktliste-listitem__header headline6]");
			Elements price = documentNextEvents.select("div[class=productswiper__swiper-container swiper-container produktliste jsFFWKSwiper swiper-container-initialized swiper-container-horizontal] p[class=produktliste-listitem__currentprice]");
//			Elements avp = documentNextEvents.select("div[class=productswiper__swiper-container swiper-container produktliste jsFFWKSwiper swiper-container-initialized swiper-container-horizontal] p[class=produktliste-listitem__oldprice hide-ingallery]");
			Elements pzn = documentNextEvents.select("div[class=productswiper__swiper-container swiper-container produktliste jsFFWKSwiper swiper-container-initialized swiper-container-horizontal] [^data-tracking-article-ordernumber]");
			Elements quantity = documentNextEvents.select("div[class=productswiper__swiper-container swiper-container produktliste jsFFWKSwiper swiper-container-initialized swiper-container-horizontal] li[data-cbl-type=\"inhalt\"]");
			Elements discount = documentNextEvents.select("div[class=productswiper__swiper-container swiper-container produktliste jsFFWKSwiper swiper-container-initialized swiper-container-horizontal] div[class=produktliste-listitem__imagebox]");
	    	Integer placement =1;
	    	List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
	    	if(title != null && !title.isEmpty()) {
	    		for(int h=0;h<title.size();h++) {
	    			PharmaDetail pharmaProduct = new PharmaDetail();
	    			pharmaProduct.setName(title.get(h).text());
	    			pharmaProduct.setQuantity(quantity.get(h).text());
	    			pharmaProduct.setPzn(pzn.get(h).dataset().get("tracking-article-ordernumber"));
	    			String priceText = price.get(h).text();
	    			pharmaProduct.setPrice(priceText);
	    			String priceInString = priceText.replace("nur ", "").replace("€", "").replace(",", ".").trim();
	    			Double priceInDouble = convertStringToDouble(priceInString.substring(0,priceInString.length()-1));
	    			pharmaProduct.setPriceInDouble(priceInDouble);
	    			String discountText = discount.get(h).text();
	    			String discountInString = discountText.replace("-", "").replace("%", "").replace("TOPSELLER", "").trim();
	    			Integer discountInteger = convertStringToInteger(discountInString);
	    			pharmaProduct.setDiscount(discountInteger);
	    			if(discountText.indexOf("%") > -1) {
	    				Double avpInDouble = priceInDouble/(100-discountInteger)*100;
	    				pharmaProduct.setAvp((double) Math.round(avpInDouble * 100) / 100);
	    			}else
	    				pharmaProduct.setAvp(priceInDouble);
	    			pharmaProduct.setSequenceByKeyword(placement);
	    			pharmaProduct.setPharmaSetting(pharmaSettingMain);
	    			pharmaProduct.setKeyword(pharmaSettingMain.getName());
	    			pharmaProduct.setCrawlType(pharmaSettingMain.getType());
	    			pharmaProduct.setUrl(pharmaSettingMain.getEuraponUrl());
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
	    		System.out.println("loi"+pharmaSettingMain.getEuraponUrl());
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
