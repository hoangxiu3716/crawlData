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
import de.gimik.apps.parsehub.backend.model.Categories;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;
import de.gimik.apps.parsehub.backend.model.PharmaDetail;
import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.model.PharmaSetting;
import de.gimik.apps.parsehub.backend.model.Product;
import de.gimik.apps.parsehub.backend.service.CategoryService;
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
	@Autowired
	private CategoryService categoryService;
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
	private static Double convertStringToDouble(String value) {
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
//	String url = "https://donghochinhhang.com/collections/dong-ho-nam";
    String url = "https://donghochinhhang.com/products/dong-ho-tissot-t006-407-11-043-00";
	Launcher launcher = new Launcher();
    SessionFactory factory = launcher.launch();
    Session dummySession = factory.create();
   	dummySession.navigate(url);
 	dummySession.waitDocumentReady(200000);
 	dummySession.wait(getRandomNumberInRange(3000, 5000));
//	dummySession.evaluate("var button = document.querySelector(\"#usercentrics-root\").shadowRoot.querySelector(\"#focus-lock-id > div > div > div.sc-bYoBSM.hrDdPG > div > div > div.sc-dlVxhl.bEDIID > div > button.sc-gsDKAQ.dZIwB\");"
//	    			+ "var click = function(){if(button != null)button.click();}");
//	dummySession.callFunction("click");
//	dummySession.wait(getRandomNumberInRange(10000, 15000));
 	String responseNextEvent = dummySession.getContent();
	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
//	Elements title = documentNextEvents.select("div[class=product-information] div[class=product-info] div[class=hidden-xs] a");
//	Elements size = documentNextEvents.select("div[class=product-information] div[class=product-info] p[class=text-center product-info__size]");
//	Elements price = documentNextEvents.select("div[class=product-information] div[class=product-info] div[class=box-price__shop product-titlec]");
	
	Elements title = documentNextEvents.select("div[class=col-lg-5 col-md-6 col-sm-12 col-xs-12 information-product] div[class=product-title font-sans-pro] h1");
	Elements size = documentNextEvents.select("div[class=col-lg-5 col-md-6 col-sm-12 col-xs-12 information-product] div[class=product-title font-sans-pro] p");
	Elements price = documentNextEvents.select("div[class=product-information] div[class=product-info] div[class=box-price__shop product-titlec]");
	Elements sale = documentNextEvents.select("div[class=col-lg-5 col-md-6 col-sm-12 col-xs-12 information-product] div[class=font-sans-pro] b");

	for(int i = 0; i< title.size();i++) {
//		System.out.println(title.get(i).text());
//		System.out.println(size.get(i).text().substring(size.get(i).text().indexOf(":")+1).trim());
//		String priceIntext = price.get(i).text().replace("₫", "").replace(",", "");
//		Double priceInDouble = convertStringToDouble(priceIntext);
//		System.out.println(priceInDouble);
		
		
		System.out.println(title.get(i).text());
		System.out.println(size.get(i).text());
		System.out.println(sale.get(i).text().replace("-", "").replace("%", ""));
		
//		String urlProduct = "https://donghochinhhang.com"+title.get(i).attr("href");
//		Launcher launcherProduct = new Launcher();
//	    SessionFactory factoryProduct = launcherProduct.launch();
//	    Session dummySessionProduct = factoryProduct.create();
//	    dummySessionProduct.navigate(urlProduct);
//	    dummySessionProduct.waitDocumentReady(200000);
//	    dummySessionProduct.wait(getRandomNumberInRange(3000, 5000));
//	    String responseNextEventProduct = dummySessionProduct.getContent();
//	    org.jsoup.nodes.Document documentNextEventsProduct = Jsoup.parse(responseNextEventProduct);
//	    dummySessionProduct.wait(getRandomNumberInRange(10000, 15000));
//	    Elements titleProduct = documentNextEventsProduct.select("div[class=product-information] div[class=hidden-xs] a");
	}
    }
    @Path("crawl")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ResultInfo crawl(@Context HttpServletRequest request) throws IOException, InterruptedException {
    	Categories categories = categoryService.findById(1);
    	String url = "https://donghochinhhang.com/collections/dong-ho-nam";
    	Launcher launcher = new Launcher();
        SessionFactory factory = launcher.launch();
        Session dummySession = factory.create();
       	dummySession.navigate(url);
     	dummySession.waitDocumentReady(200000);
     	dummySession.wait(getRandomNumberInRange(3000, 5000));
     	String responseNextEvent = dummySession.getContent();
    	org.jsoup.nodes.Document documentNextEvents = Jsoup.parse(responseNextEvent);
    	Elements title = documentNextEvents.select("div[class=product-information] div[class=product-info] div[class=hidden-xs] a");
    	Elements size = documentNextEvents.select("div[class=product-information] div[class=product-info] p[class=text-center product-info__size]");
    	Elements price = documentNextEvents.select("div[class=product-information] div[class=product-info] div[class=box-price__shop product-titlec]");
    	for(int i = 0; i< title.size();i++) {
    		Product product = new Product();
    		product.setName(title.get(i).text());
    		product.setSize(size.get(i).text().substring(size.get(i).text().indexOf(":")+1).trim());
    		String priceIntext = price.get(i).text().replace("₫", "").replace(",", "");
    		Double priceInDouble = convertStringToDouble(priceIntext);
    		product.setPrice(priceInDouble);
    		product.setNew_product(true);
    		product.setHighlight(true);
    		product.setCategory_id(categories);
    		String urlProduct = "https://donghochinhhang.com"+title.get(i).attr("href");
    		Launcher launcherProduct = new Launcher();
    	    SessionFactory factoryProduct = launcherProduct.launch();
    	    Session dummySessionProduct = factoryProduct.create();
    	    dummySessionProduct.navigate(urlProduct);
    	    dummySessionProduct.waitDocumentReady(200000);
    	    dummySessionProduct.wait(getRandomNumberInRange(3000, 5000));
    	    String responseNextEventProduct = dummySessionProduct.getContent();
    	    org.jsoup.nodes.Document documentNextEventsProduct = Jsoup.parse(responseNextEventProduct);
    	    dummySessionProduct.wait(getRandomNumberInRange(10000, 15000));
    	    Elements titleProduct = documentNextEventsProduct.select("div[class=product-information] div[class=hidden-xs] a");
    	}
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
