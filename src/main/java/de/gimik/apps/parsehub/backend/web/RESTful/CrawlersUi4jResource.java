package de.gimik.apps.parsehub.backend.web.RESTful;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;
import de.gimik.apps.parsehub.backend.model.PharmaDetail;
import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.model.PharmaSetting;
import de.gimik.apps.parsehub.backend.service.ParsehubService;
import de.gimik.apps.parsehub.backend.service.PharmaDetailService;
import de.gimik.apps.parsehub.backend.service.PharmaProductService;
import de.gimik.apps.parsehub.backend.service.PharmaSettingService;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.util.DateTimeUtility;
import de.gimik.apps.parsehub.backend.util.ServerConfig;
import de.gimik.apps.parsehub.backend.web.viewmodel.ResultInfo;
import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;

@Component
@Path("/crawlersui4j")
public class CrawlersUi4jResource {
	@Autowired
	private ServerConfig serverConfig;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private PharmaProductService pharmaProductService;
	@Autowired
	private ParsehubService parsehubService;
	@POST
	@Path("buildData")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public ResultInfo importData(@Context HttpServletRequest request,
			@FormDataParam("file") InputStream file,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		if(fileDetail == null)
			return new ResultInfo(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED);
		String fileName = fileDetail.getFileName();
		String extension = FilenameUtils.getExtension(fileName);
		if(StringUtils.isEmpty(extension) || !extension.equals("xlsx"))
			return new ResultInfo(Constants.ERROR_MESSAGE.NOT_EXCEL_FILE);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(1);
			int rowIndex = 0;
			for (Row row : sheet) {
				rowIndex++;
				if (rowIndex < 3)
					continue;
				Double valueNeeded = 0.0;
				if(row.getCell(27) != null){
					Hyperlink link = row.getCell(27).getHyperlink();
					if(link!=null && !StringUtils.isEmpty(link.getAddress()) ){
						String crawlerLink = link.getAddress();
						String data = getDataFromUrl(crawlerLink);
						if(!StringUtils.isEmpty(data) && data.indexOf("%") > -1){
							data = data.substring(0, data.indexOf("%"));
							valueNeeded = convertStringToDouble(data);
						}
						row.getCell(13).setCellValue(valueNeeded);
						Thread.sleep(getRandomNumberInRange(2000, 5000));
					}
				}
//				break;
			}
			String reportId = new Date().getTime()+".xlsx";
			File newFile = getExportPdfFile(reportId);
			 FileOutputStream outputStream = new FileOutputStream(newFile);
	            workbook.write(outputStream);
	            outputStream.close();
	            return new ResultInfo(Constants.OK,reportId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultInfo(Constants.ERROR);
			// TODO: handle exception
		}
		
	}
	@GET
	@Path("downloadfile")
	@Produces("application/xlsx")
	public Response createPDFRoomInfo(@Context HttpServletRequest request ,@QueryParam("reportId") String reportId){		
		if(StringUtils.isEmpty(reportId))
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED);
		File pdfFile = getExportPdfFile(reportId);
		
		try {
			final byte[] fileBytes = FileUtils.readFileToByteArray(pdfFile);
			StreamingOutput stream = new StreamingOutput() {
		        public void write(OutputStream output) throws IOException,
		                WebApplicationException {
		            try {
		                output.write(fileBytes);
		            } catch (Exception e) {
		                throw new WebApplicationException(e);
		            }
		        }
		    };
		    Response.ResponseBuilder response = Response.ok(stream);
		    response.header("Content-Disposition",
		            "filename=" + reportId);
		    return response.build();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
//		    FileUtils.deleteQuietly(pdfFile);
		}

		    return null;
		   
	}
	public File getExportPdfFile(String reportId) {
		File pdfFolder = new File(serverConfig.getDirectoryFileUpload());
		if (!pdfFolder.exists())
			pdfFolder.mkdirs();
		return new File(pdfFolder,reportId);
	}
//	private String getDataOfCellString(Cell cell){
//		String result = "";
//		if(cell == null)
//			return "";
//		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC )
//			result = pasteInteger(cell.getNumericCellValue()) ==null ? "" : pasteInteger(cell.getNumericCellValue()).toString();
//		if(cell.getCellType() == Cell.CELL_TYPE_STRING)
//			result = cell.getStringCellValue();
//		
//		if(!StringUtils.isEmpty(result) && result.trim().toUpperCase().equals("NULL"))
//			result ="";
//		return result;
//	}
//	private Double getDataOfCellDouble(Cell cell){
//		Double result = null;
//		if(cell == null)
//			return null;
//		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC )
//			result = cell.getNumericCellValue();
//		if(cell.getCellType() == Cell.CELL_TYPE_STRING)
//			result = pasteDouble(cell.getStringCellValue());
//		return result;
//	}
//
//	private Integer getDataOfCellInteger(Cell cell){
//		Integer result = null;
//		if(cell == null)
//			return null;
//		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC )
//			result = pasteInteger(cell.getNumericCellValue());
//		if(cell.getCellType() == Cell.CELL_TYPE_STRING)
//			result = pasteInteger(cell.getStringCellValue());
//		return result;
//	}
//	
//	private Date getDataOfCellDate(Cell cell){
//		Date result = null;
//		if(cell == null)
//			return null;
//		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC )
//			result = cell.getDateCellValue();
//		if(cell.getCellType() == Cell.CELL_TYPE_STRING)
//			result = pasteToDate(cell.getStringCellValue());
//		return result;
//	}
	public static Double pasteDouble(String data) {
		try {
			Double value = Double.parseDouble(data);
			return value;
		} catch (Exception e) {
			return null;
		}
	}
	public static Integer pasteInteger(String data) {
		try {
			Integer value = Integer.parseInt(data);
			return value;
		} catch (Exception e) {
			return null;
		}
	}
	public static Integer pasteInteger(Double data) {
		try {
			Integer value = data.intValue();
			return value;
		} catch (Exception e) {
			return null;
		}
		
	}
	public static String toString(Double data) {
		try {
			String value = String.valueOf(data);
			return value;
		} catch (Exception e) {
			return null;
		}
		
	}
	public static Date pasteToDate(String data) {
		try {
			Date value = DateTimeUtility.parseInput_DD_MM_YYYY_HH_MM(data);
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getDataFromUrl(String url) throws InterruptedException{
		String dataNeeded = "N/A";
		Page page = BrowserFactory.getWebKit().navigate(url);
		Thread.sleep(getRandomNumberInRange(2000, 3500));
		io.webfolder.ui4j.api.dom.Document doc = page.getDocument();
		Thread.sleep(getRandomNumberInRange(2000, 3500));
		List<io.webfolder.ui4j.api.dom.Element> elements = doc.getBody().queryAll(".inhalt2 table");
		boolean done = false;
		for (io.webfolder.ui4j.api.dom.Element element : elements) {
			List<io.webfolder.ui4j.api.dom.Element> trs = element.queryAll("tr");
			for (int i = 0; i < trs.size(); i++) {
				String checkText = trs.get(i).getInnerHTML();
				if (checkText.indexOf("Over 2.5") > 0) {
					List<io.webfolder.ui4j.api.dom.Element> tds = trs.get(i + 2).queryAll("td");
					if (tds.size() >= 2) {
						if (tds.get(2).getText() != null) {
							dataNeeded = tds.get(2).getText().get();
							System.out.println(dataNeeded);
							done = true;
							break;
						}
					}
				}
				if (done)
					break;
			}
			if (done)
				break;
		}

		System.out.println(url);
		return dataNeeded;
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
    public static void main(String[] args) throws IOException, InterruptedException {
	String url = "https://www.shop-apotheke.com/search.htm?i=1&q=Blutzucker&searchChannel=algolia&userToken=anonymous-5a0e11b6-0790-4f56-8ae3-053a562d6f2a";
	Integer placement = 1;
	Launcher launcher = new Launcher();
    SessionFactory factory = launcher.launch();
    Session dummySession = factory.create();
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
	   	Elements quantity = documentNextEvents.select("div[data-clientside-hook=productDetailButton] span[class*=a-h4-tiny u-font-weight--bold]");
	   	Elements pzn = documentNextEvents.select("div[data-clientside-hook=productDetailButton] div[class=o-SearchProductListItem__selectableSection]");
	   	Elements price = documentNextEvents.select("div[data-clientside-hook=productDetailButton] [data-qa-id=entry-price]");
	   	Elements avp = documentNextEvents.select("div[data-clientside-hook=productDetailButton] div[class*=l-flex l-flex--distribute-space l-flex--end l-flex--wrap]");
	if(pzn != null && !pzn.isEmpty()) {
		for(int h=0;h<pzn.size();h++) {
			System.out.println(placement);
			System.out.println(title.get(h).text());
			String test = pzn.get(h).text();
			if(test.indexOf("PZN/EAN") > -1) {
 				Integer numberOfSlash = test.lastIndexOf("/");
 			String pznOnly = test.substring(test.indexOf("PZN/EAN"), numberOfSlash).replace("PZN/EAN", "").replace(":", "").trim().replaceAll("[^0-9,-\\.]", "");
 				System.out.println(pznOnly);
 				System.out.println(test.substring(0, test.indexOf("PZN/EAN")).replace("PZN/EAN", "").trim());
 			}else if(test.indexOf(":") > -1) {
 				Integer numberOfTwoDot = test.indexOf(":");
 				String test2 = test.substring(numberOfTwoDot+1).trim().replaceAll("[^0-9,-\\.]", "").replace(".", "");
 				System.out.println(test2);
 				String test3 = test.substring(0, test.indexOf(":")).replace("PZN", "").replace("ISBN", "").trim();
 				System.out.println(test3);
 			}
//			System.out.println(quantity.get(h).text());
			placement++;
			System.out.println("----------------------------------------");
		}
	}else
		System.out.println("loi");
//    	 String s = "5:30";
//    	    Pattern p = Pattern.compile("(\\d+):(\\d+)");
//    	    Matcher m = p.matcher(s);
//    	    if (m.matches()) {
//    	        int hrs = Integer.parseInt(m.group(1));
//    	        int min = Integer.parseInt(m.group(2));
//    	        long ms = (long) hrs * 60 * 60 * 1000 + min * 60 * 1000;
//    	        System.out.println("hrs="+hrs+" min="+min+" ms="+ms);
//    	    } else {
//    	        System.out.println("loi");
//    	    }
    }
    @Path("crawl")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ResultInfo crawl(@Context HttpServletRequest request) throws IOException, InterruptedException {
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
		return new ResultInfo(Constants.SUCCESS);
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
}
