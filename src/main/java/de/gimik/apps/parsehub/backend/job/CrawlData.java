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
import de.gimik.apps.parsehub.backend.util.ServerConfig;
import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
public class CrawlData {
	@Autowired
	private ParsehubService parsehubService;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private PharmaProductService pharmaProductService;
	@Autowired
	private ServerConfig serverConfig;
	public void crawlDataPharma() {
	   ParsehubSetting parsehubSetting = parsehubService.findByCodeActiveTrue(Constants.Code.SANICARE);
	   List<PharmaSetting> pharmaSetting = new ArrayList<PharmaSetting>();
	   pharmaSetting = settingService.findByActiveTrueAndUrlIsNotNull();
       Launcher launcher = new Launcher();
	   SessionFactory factory = launcher.launch();
	   Session dummySession = factory.create();
//	   ChromeDriver driver;
//   	   WebDriverManager.chromedriver().setup();
//	    driver=new ChromeDriver();
//	    List<PharmaScreenShot> pharmaScreenshots = new ArrayList<PharmaScreenShot>();
		for(int k=0;k<pharmaSetting.size();k++) {
			Integer placement = 1 ;
			//screenshot entire page
//			String screenShotUrl = pharmaSettingMain.getUrl();
//		           driver.get(screenShotUrl);
//		           driver.manage().window().maximize();
//		    String str = "return document.querySelector(\"#usercentrics-root\").shadowRoot.querySelector(\"div > div > div.sc-fubCzh.byndWw > div > div.sc-jJEKmz.erdLpy > div > div > div.sc-bBXrwG.csEvJZ > div > button.sc-gsTEea.kgwCkg\")";
//			WebElement fname = (WebElement) driver.executeScript(str);
//			if(fname != null && fname.isDisplayed())
//			       fname.click();
//		    Screenshot screenshot=new AShot().shootingStrategy(ShootingStrategies.viewportRetina(200, 10, 10, (float) 1.2)).takeScreenshot(driver);
//		    FileUploadInfo videoOrAudioUpload = FileUtil.uploadFileScreenShot(screenshot,serverConfig.getDirectoryFileUpload() + "/", Constants.folder.SCREENSHOT);
//		    PharmaScreenShot phamaSceenShot = new PharmaScreenShot();
//		            phamaSceenShot.setUrl(videoOrAudioUpload.getFileUrl());
//		            PharmaSetting SettingId = settingService.findById(pharmaSettingMain.getId());
//		            phamaSceenShot.setPharmaSetting(SettingId);
//		            phamaSceenShot.setKeyword(pharmaSettingMain.getKeyword());
//		            pharmaScreenshots.add(phamaSceenShot);
			
			PharmaSetting pharmaSettingMain = pharmaSetting.get(k);
			String url = pharmaSettingMain.getUrl();
			if(pharmaSettingMain.getParentId() == null && !StringUtils.isEmpty(url)){ 
			for(int i=1;i <= 10;i++) {
			    if(i>1 && pharmaSettingMain.getType().equals(Constants.CrawlType.KEYWORD) && url.contains("search")) {
			    	url = url+"&p="+i;
			    }else if(i>1){
			    	url = url+"?p="+i;
			    }
				dummySession.navigate(url);
				dummySession.waitDocumentReady(200000);
				dummySession.wait(getRandomNumberInRange(3000, 5000));
				dummySession.evaluate("var button = document.querySelector(\"#usercentrics-root\").shadowRoot.querySelector(\"div > div > div.sc-furwcr.hXwEJN > div > div.sc-jJoQJp.dTzACB > div > div > div.sc-bBHxTw.hgPqkm > div > button.sc-gsDKAQ.jAAwDU\");"
						+ "var click = function(){if(button != null)button.click()}");
				dummySession.callFunction("click");
				String responseNextEvent = dummySession.getContent();
				org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
				if(i==1) {
					Elements promoTitle = documentNextEvents.select("div[class=emotion--row row--2] div[class=product--info] a[class=product--title]");
					Elements promoPriceUnit = documentNextEvents.select("div[class=emotion--row row--2] div[class=product--info] div[class=price--unit]");
					Elements price = documentNextEvents.select("div[class=emotion--row row--2] div[class=product--info] div[class=product--price-info] span[class=price--default is--nowrap is--discount]");
					Elements fullPrice = documentNextEvents.select("div[class=emotion--row row--2] div[class=product--info] div[class=product--price-info] div[class=product--price]");
					Elements promoPzn = documentNextEvents.select("div[class=emotion--row row--2] [^data-ordernumber]");
					if(!promoTitle.isEmpty() && (!(promoPriceUnit.size() == price.size()) || !(promoTitle.size() == fullPrice.size()) || !(fullPrice.size() == price.size()))) {
						System.out.println("loi");
						price = documentNextEvents.select("div[class=emotion--row row--2] div[class=product--info] div[class=product--price-info]");
					}
					if(promoTitle.size() != 0 && promoTitle.size() <2) {
						List<PharmaDetail> promoPharmaProducts = new ArrayList<PharmaDetail>();
					
					for(int h=0;h<promoTitle.size();h++) {
							PharmaDetail pharmaProduct = new PharmaDetail();
							pharmaProduct.setName(promoTitle.get(h).text());
							pharmaProduct.setQuantity(promoPriceUnit.get(h).text());
							if(price.get(h).text().contains("Unser Preis")) {
								String priceShortcut = price.get(h).text().replaceAll("Unser Preis:", "").replace(",", ".");
								Double priceInDouble = convertStringToDouble(priceShortcut.trim().substring(0, priceShortcut.trim().length()-2));
								pharmaProduct.setPriceInDouble(priceInDouble);
								pharmaProduct.setPrice(priceShortcut.replace(",", "."));
							}else {
								Double priceInDouble = convertStringToDouble(price.get(h).text().replace(",", ".").trim().substring(0, price.get(h).text().replace(",", ".").trim().length()-2));
								pharmaProduct.setPriceInDouble(priceInDouble);
								pharmaProduct.setPrice(price.get(h).text().replace(",", "."));
							}
							pharmaProduct.setFullPrice(fullPrice.get(h).text());
							if(fullPrice.get(h).text().indexOf("Sie sparen:") > -1) {
								String discoutText = fullPrice.get(h).text().substring(fullPrice.get(h).text().indexOf("Sie sparen:"), fullPrice.get(h).text().length()).replace("Sie sparen: ","").trim();
								String discountText2 = discoutText.substring(0, discoutText.length()-2);
								Integer numberDiscount = convertStringToInteger(discountText2);
								pharmaProduct.setDiscount(numberDiscount);
							}else if(fullPrice.get(h).text().indexOf("VP:") > -1){
								String discountText = fullPrice.get(h).text().substring(fullPrice.get(h).text().indexOf("VP:"), fullPrice.get(h).text().length()).replace("VP:", "").replace(",", ".");
								Double discountInDouble1 = convertStringToDouble(discountText.substring(0, discountText.length()-2).trim());
								String discountText2 = fullPrice.get(h).text().substring(0, fullPrice.get(h).text().indexOf("€")).replace(",", ".").trim();
								Double discountInDouble2 = convertStringToDouble(discountText2.substring(0, discountText2.length()-1));
								Double discountFinally = 100- (discountInDouble2 / discountInDouble1*100);
								Integer numberDiscount = (int) Math.round(discountFinally);
								pharmaProduct.setDiscount(numberDiscount);
							}else {
								pharmaProduct.setDiscount(0);
							}
							if(fullPrice.get(h).text().indexOf("VP") > -1) {
								String fullPriceClone = fullPrice.get(h).text().substring(fullPrice.get(h).text().indexOf("VP"),fullPrice.get(h).text().length());
				    			Integer number = fullPriceClone.indexOf("€");
				    			String testfullPrice = fullPriceClone.substring(fullPriceClone.indexOf("VP"),number-1);
					    		String data = testfullPrice.replace("VP:","").replace(",", ".").trim();
					    		Double avpInDouble = convertStringToDouble(data);
					    		pharmaProduct.setAvp(avpInDouble);
							}else if(fullPrice.get(h).text().indexOf("Unser Preis:") > -1) {
								String data = fullPrice.get(h).text().replace("Unser Preis:","").replace("€","").replace(",",".").trim();
								String dataForAvp = data.substring(0, data.length()-1);
								Double avpInDouble = convertStringToDouble(dataForAvp);
								pharmaProduct.setAvp(avpInDouble);
							}else {
								String data = fullPrice.get(h).text().replace("€","").trim().replace(",", ".");
								String dataForAvp = data.substring(0, data.length()-1);
								Double avpInDouble = convertStringToDouble(dataForAvp);
								pharmaProduct.setAvp(avpInDouble);
							}
							pharmaProduct.setPharmaSetting(pharmaSettingMain);
							pharmaProduct.setUrl(url);
							pharmaProduct.setKeyword(pharmaSettingMain.getKeyword());
							pharmaProduct.setPzn(promoPzn.get(h).dataset().get("ordernumber"));
							pharmaProduct.setSequenceByKeyword(placement);
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
							promoPharmaProducts.add(pharmaProduct);
					}
					if(!CollectionUtils.isEmpty(promoPharmaProducts))
						mainService.createListDetail(promoPharmaProducts);
					}
//					Elements bestSellerTitle = documentNextEvents.select("div[class=topseller panel has--border is--rounded prudsys prudsys-SC_categorypage-] div[class=product--info] a[class=product--title]");
//					Elements bestSellerPriceUnit = documentNextEvents.select("div[class=topseller panel has--border is--rounded prudsys prudsys-SC_categorypage-] div[class=product--info] div[class=price--unit]");
//					Elements bestSellerPrice = documentNextEvents.select("div[class=topseller panel has--border is--rounded prudsys prudsys-SC_categorypage-] div[class=product--info] div[class=product--price-info] span[class=price--default is--nowrap is--discount]");
//					Elements bestSellerFullPrice = documentNextEvents.select("div[class=topseller panel has--border is--rounded prudsys prudsys-SC_categorypage-] div[class=product--info] div[class=product--price-info] div[class=product--price]");
//					Elements bestSellerPzn = documentNextEvents.select("div[class=topseller panel has--border is--rounded prudsys prudsys-SC_categorypage-] [^data-ordernumber]");		
//					if(!bestSellerTitle.isEmpty() && (!(bestSellerPriceUnit.size() == bestSellerPrice.size()) || !(bestSellerTitle.size() == bestSellerFullPrice.size()) || !(bestSellerFullPrice.size() == bestSellerPrice.size()))) {
//						System.out.println("loi");
//						bestSellerPrice = documentNextEvents.select("div[class=topseller panel has--border is--rounded prudsys prudsys-SC_categorypage-] div[class=product--info] div[class=product--price-info]");
//					}
//					if(bestSellerTitle.size() != 0) {
//						List<PharmaDetail> bestSellerPharmaProducts = new ArrayList<PharmaDetail>();
//					
//					for(int h=0;h<bestSellerTitle.size();h++) {
//							PharmaDetail pharmaProduct = new PharmaDetail();
//							pharmaProduct.setName(bestSellerTitle.get(h).text());
//							pharmaProduct.setQuantity(bestSellerPriceUnit.get(h).text());
//							if(bestSellerPrice.get(h).text().contains("Unser Preis") && bestSellerPrice.get(h).text().contains("Sie sparen")) {
//								Integer number = bestSellerPrice.get(h).text().indexOf("Unser");
//								Integer number1 =bestSellerPrice.get(h).text().indexOf("Sie");
//								String priceShortcut = bestSellerPrice.get(h).text().substring(number, number1).replaceAll("Unser Preis:", "").replace(",", ".");
//								Double priceInDouble = convertStringToDouble(priceShortcut.trim().substring(0, priceShortcut.trim().length()-2));
//								pharmaProduct.setPriceInDouble(priceInDouble);
//								pharmaProduct.setPrice(priceShortcut.replace(",", "."));
//							}else if(bestSellerPrice.get(h).text().contains("Unser Preis")) {
//								String priceShortcut = bestSellerPrice.get(h).text().replaceAll("Unser Preis:", "").replace(",", ".");
//								Double priceInDouble = convertStringToDouble(priceShortcut.trim().substring(0, priceShortcut.trim().length()-2));
//								pharmaProduct.setPriceInDouble(priceInDouble);
//								pharmaProduct.setPrice(priceShortcut.replace(",", "."));
//							}
//							else {
//								Double priceInDouble = convertStringToDouble(bestSellerPrice.get(h).text().replace(",", ".").trim().substring(0, bestSellerPrice.get(h).text().replace(",", ".").trim().length()-2));
//								pharmaProduct.setPriceInDouble(priceInDouble);
//								pharmaProduct.setPrice(bestSellerPrice.get(h).text().replace(",", "."));
//							}
//							pharmaProduct.setFullPrice(bestSellerFullPrice.get(h).text());
//							if(bestSellerFullPrice.get(h).text().indexOf("Sie sparen:") > -1) {
//								String discoutText = bestSellerFullPrice.get(h).text().substring(bestSellerFullPrice.get(h).text().indexOf("Sie sparen:"), bestSellerFullPrice.get(h).text().length()).replace("Sie sparen: ","").trim();
//								String discountText2 = discoutText.substring(0, discoutText.length()-2);
//								Integer numberDiscount = convertStringToInteger(discountText2);
//								pharmaProduct.setDiscount(numberDiscount);
//							}else if(bestSellerFullPrice.get(h).text().indexOf("VP:") > -1){
//								String discountText = bestSellerFullPrice.get(h).text().substring(bestSellerFullPrice.get(h).text().indexOf("VP:"), bestSellerFullPrice.get(h).text().length()).replace("VP:", "").replace(",", ".");
//								Double discountInDouble1 = convertStringToDouble(discountText.substring(0, discountText.length()-2).trim());
//								String discountText2 = bestSellerFullPrice.get(h).text().substring(0, bestSellerFullPrice.get(h).text().indexOf("€")).replace(",", ".").trim();
//								Double discountInDouble2 = convertStringToDouble(discountText2.substring(0, discountText2.length()-1));
//								Double discountFinally = 100- (discountInDouble2 / discountInDouble1*100);
//								Integer numberDiscount = (int) Math.round(discountFinally);
//								pharmaProduct.setDiscount(numberDiscount);
//							}else {
//								pharmaProduct.setDiscount(0);
//							}
//							
//							pharmaProduct.setPharmaSetting(pharmaSettingId);
//							pharmaProduct.setUrl(url);
//							pharmaProduct.setKeyword(pharmaSettingMain.getKeyword());
//							pharmaProduct.setPzn(bestSellerPzn.get(h).dataset().get("ordernumber"));
//							pharmaProduct.setSequenceByKeyword(placement);
//							pharmaProduct.setCrawlType(pharmaSettingMain.getType());
//							placement++;
//							if(!StringUtils.isEmpty(pharmaProduct.getPzn())) {
//								PharmaProduct pharmaProductUnique = pharmaProductService.findByPznAndActiveTrue(pharmaProduct.getPzn().trim());
//								if(pharmaProductUnique != null) pharmaProduct.setProductId(pharmaProductUnique.getId());
//								else {
//									pharmaProductUnique = new PharmaProduct(pharmaProduct.getName(), pharmaProduct.getPzn().trim());
//									pharmaProductService.create(pharmaProductUnique);
//									pharmaProduct.setProductId(pharmaProductUnique.getId());
//								}
//							}
//							bestSellerPharmaProducts.add(pharmaProduct);
//					}
//					if(!CollectionUtils.isEmpty(bestSellerPharmaProducts))
//						mainService.createListDetail(bestSellerPharmaProducts);
//					}
				}
				Elements title = documentNextEvents.select("div[class=listing--container] div[class=product--info] a[class=product--title]");
				Elements priceUnit = documentNextEvents.select("div[class=listing--container] div[class=product--info] span[class=is--nowrap list--article-unit]");
				Elements avp = documentNextEvents.select("div[class=listing--container] div[class=product--info] div[class=product--price]");
				Elements pzn = documentNextEvents.select("div[class=listing--container] [^data-ordernumber]");
					if(!(title.size() == priceUnit.size())||!(avp.size() == priceUnit.size()) || !(pzn.size() == priceUnit.size())) {
						priceUnit = documentNextEvents.select("div[class=listing--container] div[class=product--info] div[class=listing--article-hints]");
					}
					if(title.size() != 0) {
					List<PharmaDetail> pharmaProducts = new ArrayList<PharmaDetail>();
					for(int j=0;j<title.size();j++) {
							PharmaDetail pharmaProduct = new PharmaDetail();
							pharmaProduct.setName(title.get(j).ownText());
							pharmaProduct.setQuantity(priceUnit.get(j).text());
							pharmaProduct.setUrl(url);
							pharmaProduct.setKeyword(pharmaSettingMain.getKeyword());
							pharmaProduct.setFullPrice(avp.get(j).text());
							if(avp.get(j).text().indexOf("VP") > -1) {
								String fullPriceClone = avp.get(j).text().substring(avp.get(j).text().indexOf("VP"),avp.get(j).text().length());
				    			Integer number = fullPriceClone.indexOf("€");
				    			String testfullPrice = fullPriceClone.substring(fullPriceClone.indexOf("VP"),number-1);
					    		String data = testfullPrice.replace("VP:","").replace(",", ".").trim();
					    		Double avpInDouble = convertStringToDouble(data);
					    		pharmaProduct.setAvp(avpInDouble);
							}else if(avp.get(j).text().indexOf("Unser Preis:") > -1) {
								String data = avp.get(j).text().replace("Unser Preis:","").replace("€","").replace(",",".").trim();
								String dataForAvp = data.substring(0, data.length()-1);
								Double avpInDouble = convertStringToDouble(dataForAvp);
								pharmaProduct.setAvp(avpInDouble);
							}else {
								String data = avp.get(j).text().replace("€","").trim().replace(",", ".");
								String dataForAvp = data.substring(0, data.length()-1);
								Double avpInDouble = convertStringToDouble(dataForAvp);
								pharmaProduct.setAvp(avpInDouble);
							}
							if(avp.get(j).text().indexOf("Sie sparen:") > -1) {
								String discoutText = avp.get(j).text().substring(avp.get(j).text().indexOf("Sie sparen:"), avp.get(j).text().length()).replace("Sie sparen: ","").trim();
								String discountText2 = discoutText.substring(0, discoutText.length()-2);
								Integer numberDiscount = convertStringToInteger(discountText2);
								pharmaProduct.setDiscount(numberDiscount);
							}else if(avp.get(j).text().indexOf("VP:") > -1){
								String discountText = avp.get(j).text().substring(avp.get(j).text().indexOf("VP:"), avp.get(j).text().length()).replace("VP:", "").replace(",", ".");
								Double discountInDouble1 = convertStringToDouble(discountText.substring(0, discountText.length()-2).trim());
								String discountText2 = avp.get(j).text().substring(0, avp.get(j).text().indexOf("€")).replace(",", ".").trim();
								Double discountInDouble2 = convertStringToDouble(discountText2.substring(0, discountText2.length()-1));
								Double discountFinally = 100-(discountInDouble2 / discountInDouble1*100);
								Integer numberDiscount = (int) Math.round(discountFinally);
								pharmaProduct.setDiscount(numberDiscount);
							}else {
								pharmaProduct.setDiscount(0);
							}
//						    PharmaSetting pharmaSettingId = settingService.findById(pharmaSettingMain.getId());
							pharmaProduct.setPharmaSetting(pharmaSettingMain);
							pharmaProduct.setPzn(pzn.get(j).dataset().get("ordernumber"));
							pharmaProduct.setSequenceByKeyword(placement);
							pharmaProduct.setCrawlType(pharmaSettingMain.getType());
							placement++;
							if(avp.get(j).text().contains("Sie sparen")) {
								Integer number = avp.get(j).text().indexOf("Unser");
								Integer number1 = avp.get(j).text().indexOf("Sie");
								String avpSon1 = avp.get(j).text().substring(number, number1).replaceAll("Unser Preis: ", "");
								  avpSon1 = avpSon1.replaceAll(" ", "");
					              if (!StringUtils.isEmpty(avpSon1) && (avpSon1.indexOf("UVP") > -1 || avpSon1.indexOf("AVP") > -1)) {
					                if ((pharmaSettingMain).getKeyword().equals("Teststreifen") || (
						                  pharmaSettingMain).getKeyword().equals("Diabetes Teststreifen") || (
						                  pharmaSettingMain).getKeyword().equals("KOLIKEN") || (
						                  pharmaSettingMain).getKeyword().equals("Blutzucker")) {
						                  if (avpSon1.indexOf("AVP") > -1)
						                    avpSon1 = avpSon1.substring(0, avpSon1.indexOf("AVP")); 
						                  if (avpSon1.indexOf("UVP") > -1)
						                    avpSon1 = avpSon1.substring(0, avpSon1.indexOf("UVP")); 
					                } else {
						                  if (avpSon1.indexOf("UVP") == 0 && avpSon1.indexOf("€") > -1) {
						                    avpSon1 = String.valueOf(avpSon1.split("€")[1]) + " €";
						                  }
						                  if (avpSon1.indexOf("AVP") == 0 && avpSon1.indexOf("€") > -1) {
						                    avpSon1 = String.valueOf(avpSon1.split("€")[1]) + " €";
						                  }
					                } 
					              }
					              Double priceInDouble = convertStringToDouble(avpSon1.replace(",", ".").trim().substring(0, avpSon1.replace(",", ".").trim().length()-2));
					              pharmaProduct.setPriceInDouble(priceInDouble);
					              pharmaProduct.setPrice(avpSon1.replace(",", "."));
							}else if(avp.get(j).text().contains("VP")){
								Integer number2 = avp.get(j).text().indexOf("VP");
								if(number2>5) {
								String avp2 = avp.get(j).text().substring(0,number2-1);
								Double priceInDouble = convertStringToDouble(avp2.replace(",", ".").trim().substring(0, avp2.replace(",", ".").trim().length()-2));
								pharmaProduct.setPriceInDouble(priceInDouble);
								pharmaProduct.setPrice(avp2.replace(",", "."));
								}
								else {
									Integer number3 = avp.get(j).text().indexOf("€");
									String avp3 = avp.get(j).text().substring(number2+3,number3);
									Double priceInDouble = convertStringToDouble(avp3.replace(",", ".").trim().substring(0, avp3.replace(",", ".").trim().length()-2));
									pharmaProduct.setPriceInDouble(priceInDouble);
									pharmaProduct.setPrice(avp3.replace(",", "."));
								}
							}else {
								 String avpSon1 = (avp.get(j)).text().replaceAll("Unser Preis: ", "");
					              if (!StringUtils.isEmpty(avpSon1) && (avpSon1.indexOf("UVP") > -1 || avpSon1.indexOf("AVP") > -1)) {
					                if ((pharmaSettingMain).getKeyword().equals("Teststreifen") || (
						                  pharmaSettingMain).getKeyword().equals("Diabetes Teststreifen") || (
						                  pharmaSettingMain).getKeyword().equals("KOLIKEN") || (
						                  pharmaSettingMain).getKeyword().equals("Blutzucker")) {
					                  if (avpSon1.indexOf("AVP") > -1)
					                    avpSon1 = avpSon1.substring(0, avpSon1.indexOf("AVP")); 
					                  if (avpSon1.indexOf("UVP") > -1)
					                    avpSon1 = avpSon1.substring(0, avpSon1.indexOf("UVP")); 
					                } else {
					                  if (avpSon1.indexOf("UVP") == 0 && avpSon1.indexOf("€") > -1) {
					                    avpSon1 = String.valueOf(avpSon1.split("€")[1]) + " €";
					                  }
					                  if (avpSon1.indexOf("AVP") == 0 && avpSon1.indexOf("€") > -1) {
					                    avpSon1 = String.valueOf(avpSon1.split("€")[1]) + " €";
					                  }
					                } 
					              }
					              Double priceInDouble = convertStringToDouble(avpSon1.replace(",", ".").trim().substring(0, avpSon1.replace(",", ".").trim().length()-2));
					              pharmaProduct.setPriceInDouble(priceInDouble);
					              pharmaProduct.setPrice(avpSon1.replace(",", "."));
							}
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
							if(placement == 31 || title.isEmpty()) {
								break;
							}
					}
					if(!CollectionUtils.isEmpty(pharmaProducts))
						mainService.createListDetail(pharmaProducts);
					}
				if(placement == 31 || CollectionUtils.isEmpty(title)) {
					break;
				}
			}
			if(pharmaSettingMain.getType().equals(Constants.CrawlType.CROSS_SELLING)) {
				dummySession.navigate(pharmaSettingMain.getUrl());
				dummySession.waitDocumentReady(200000);
				dummySession.wait(getRandomNumberInRange(3000, 5000));
				dummySession.evaluate("var button = document.querySelector(\"#usercentrics-root\").shadowRoot.querySelector(\"div > div > div.sc-furwcr.hXwEJN > div > div.sc-jJoQJp.dTzACB > div > div > div.sc-bBHxTw.hgPqkm > div > button.sc-gsDKAQ.jAAwDU\");"
						+ "var click = function(){if(button != null)button.click()}");
				dummySession.callFunction("click");
				String responseNextEvent = dummySession.getContent();
				org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
				Elements detailTitle = documentNextEvents.select("div[class=tab--container has--content recommendation--interesting] .product--title");
				Elements priceUnit = documentNextEvents.select("div[class=tab--container has--content recommendation--interesting] .price--unit");
				Elements price = documentNextEvents.select("div[class=tab--container has--content recommendation--interesting] .product--price");
				Elements pzn = documentNextEvents.select("div[class=tab--container has--content recommendation--interesting] [^data-ordernumber]");
				List<PharmaDetail> detailPharmaProducts = new ArrayList<PharmaDetail>();
				for(int z=0;z<detailTitle.size();z++) {
					PharmaDetail pharmaProduct = new PharmaDetail();
					pharmaProduct.setName(detailTitle.get(z).ownText());
					pharmaProduct.setQuantity(priceUnit.get(z).text());
					pharmaProduct.setUrl(pharmaSettingMain.getUrl());
					pharmaProduct.setKeyword(pharmaSettingMain.getKeyword());
					pharmaProduct.setFullPrice(price.get(z).text());
					if(price.get(z).text().indexOf("Sie sparen:") > -1) {
						String discoutText = price.get(z).text().substring(price.get(z).text().indexOf("Sie sparen:"), price.get(z).text().length()).replace("Sie sparen: ","").trim();
						String discountText2 = discoutText.substring(0, discoutText.length()-2);
						Integer numberDiscount = convertStringToInteger(discountText2);
						pharmaProduct.setDiscount(numberDiscount);
					}else if(price.get(z).text().indexOf("VP:") > -1){
						String discountText = price.get(z).text().substring(price.get(z).text().indexOf("VP:"), price.get(z).text().length()).replace("VP:", "").replace(",", ".");
						Double discountInDouble1 = convertStringToDouble(discountText.substring(0, discountText.length()-2).trim());
						String discountText2 = price.get(z).text().substring(0, price.get(z).text().indexOf("€")).replace(",", ".").trim();
						Double discountInDouble2 = convertStringToDouble(discountText2.substring(0, discountText2.length()-1));
						Double discountFinally = 100-(discountInDouble2 / discountInDouble1*100);
						Integer numberDiscount = (int) Math.round(discountFinally);
						pharmaProduct.setDiscount(numberDiscount);
					}else {
						pharmaProduct.setDiscount(0);
					}
//				    PharmaSetting pharmaSettingId = settingService.findById(pharmaSettingMain.getId());
					if(price.get(z).text().indexOf("VP") > -1) {
						String fullPriceClone = price.get(z).text().substring(price.get(z).text().indexOf("VP"),price.get(z).text().length());
		    			Integer number = fullPriceClone.indexOf("€");
		    			String testfullPrice = fullPriceClone.substring(fullPriceClone.indexOf("VP"),number-1);
			    		String data = testfullPrice.replace("VP:","").replace(",", ".").trim();
			    		Double avpInDouble = convertStringToDouble(data);
			    		pharmaProduct.setAvp(avpInDouble);
					}else if(price.get(z).text().indexOf("Unser Preis:") > -1) {
						String data = price.get(z).text().replace("Unser Preis:","").replace("€","").replace(",",".").trim();
						String dataForAvp = data.substring(0, data.length()-1);
						Double avpInDouble = convertStringToDouble(dataForAvp);
						pharmaProduct.setAvp(avpInDouble);
					}else {
						String data = price.get(z).text().replace("€","").trim().replace(",", ".");
						String dataForAvp = data.substring(0, data.length()-1);
						Double avpInDouble = convertStringToDouble(dataForAvp);
						pharmaProduct.setAvp(avpInDouble);
					}
					pharmaProduct.setPharmaSetting(pharmaSettingMain);
					pharmaProduct.setPzn(pzn.get(z).dataset().get("ordernumber"));
					pharmaProduct.setSequenceByKeyword(placement);
					pharmaProduct.setCrawlType(pharmaSettingMain.getType());
					placement++;
					if(price.get(z).text().contains("Sie sparen")) {
						Integer number = price.get(z).text().indexOf("Unser");
						Integer number1 = price.get(z).text().indexOf("Sie");
						String avpSon1 = price.get(z).text().substring(number, number1).replaceAll("Unser Preis: ", "");
						Double priceInDouble = convertStringToDouble(avpSon1.replace(",", ".").trim().substring(0, avpSon1.replace(",", ".").trim().length()-2));
						pharmaProduct.setPriceInDouble(priceInDouble);
						pharmaProduct.setPrice(avpSon1.replace(",", "."));
					}else {
						Double priceInDouble = convertStringToDouble(price.get(z).text().replaceAll("Unser Preis: ", "").replace(",", ".").trim().substring(0, price.get(z).text().replaceAll("Unser Preis: ", "").replace(",", ".").trim().length()-2));
                        pharmaProduct.setPriceInDouble(priceInDouble);
                        pharmaProduct.setPrice(price.get(z).text().replace(",", "."));
					}
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
					detailPharmaProducts.add(pharmaProduct);
				}
				if(!CollectionUtils.isEmpty(detailPharmaProducts))
					mainService.createListDetail(detailPharmaProducts);
			}
		}
		}System.out.println("DONE");
		 dummySession.close();
//		 if(!CollectionUtils.isEmpty(pharmaScreenshots))
//			 settingService.createListDetail(pharmaScreenshots);
//         driver.quit();
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
