package de.gimik.apps.parsehub.backend.web.RESTful;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
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
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;
import de.gimik.apps.parsehub.backend.model.PharmaSetting;
import de.gimik.apps.parsehub.backend.model.User;
import de.gimik.apps.parsehub.backend.service.ParsehubService;
import de.gimik.apps.parsehub.backend.service.PharmaDetailService;
import de.gimik.apps.parsehub.backend.service.PharmaSettingService;
import de.gimik.apps.parsehub.backend.service.ProfileService;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.util.DateTimeUtility;
import de.gimik.apps.parsehub.backend.util.ServerConfig;
import de.gimik.apps.parsehub.backend.web.viewmodel.PageInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.ResultInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.ResultObjecttInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.TransferHelper;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.FilterConditionInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmaSettingBasicInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmacyCrawlBasicInfo;

@Component
@Path("/pharmacrawl")
public class PharmaCrawlResource {
	@Autowired
	private ServerConfig serverConfig;
	@Autowired
	private PharmaDetailService mainService;
	@Autowired
	private PharmaSettingService settingService;
	@Autowired
	private ParsehubService parsehubService;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private ParsehubService parseHubService;
	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public PageInfo list(@DefaultValue("0") @QueryParam("page") int pageIndex,
			@DefaultValue("10") @QueryParam("size") int pageSize,
			@QueryParam("field") String field,
			@QueryParam("direction") String direction,
			@QueryParam("filter") String filters,@QueryParam("pharmaId") Integer pharmaId,
			@QueryParam("from") String fromTime, @QueryParam("to") String toTime,
			@QueryParam("fromPrice") Double fromPrice, @QueryParam("toPrice") Double toPrice,
			@QueryParam("fromDiscount") Integer fromDiscount, @QueryParam("toDiscount") Integer toDiscount
			)throws JsonGenerationException, JsonMappingException {
		Map<String, String> filter = null;
		if (!StringUtils.isEmpty(filters)) {
			filter = new Gson().fromJson(filters,
					new TypeToken<HashMap<String, String>>() {
					}.getType());
		}
		User token =  profileService.getProfile();
		if(token == null)
			throw new BackendException(Constants.ErrorCode.BAD_TOKEN,Constants.ERROR_MESSAGE.BAD_TOKEN);
		Date from = null;
		if(!StringUtils.isEmpty(fromTime))
			from = DateTimeUtility.parseInput_DD_MM_YYYY(fromTime);
		Date toHour = null;
		Date toMinutes = null;
		Date to = null;
		if(!StringUtils.isEmpty(toTime)) {
			toHour = DateTimeUtility.parseInput_DD_MM_YYYY(toTime);
			toMinutes = DateUtils.addHours(toHour, 23);
			to = DateUtils.addMinutes(toMinutes, 59);
		}
		Page<PharmacyCrawlBasicInfo> page = mainService.findAll(pageIndex, pageSize, field,
				direction, filter,pharmaId,from,to, fromPrice, toPrice, fromDiscount, toDiscount);
		return TransferHelper.convertToPharmaCrawlPageInfo(page);
	}
	@GET
	@Path("getAllPharmaSetting")
	public ResultInfo getAllPharmaSetting(@Context HttpServletRequest request) {
		User token =  profileService.getProfile();
		if(token == null)
			throw new BackendException(Constants.ErrorCode.BAD_TOKEN,Constants.ERROR_MESSAGE.BAD_TOKEN);
		List<PharmaSetting> pharmaSetting = new ArrayList<PharmaSetting>();
		pharmaSetting = settingService.findByActiveTrue();
		List<PharmaSettingBasicInfo> datas = Lists.newArrayList(
				Iterables.transform(pharmaSetting, new Function<PharmaSetting, PharmaSettingBasicInfo>() {
					@Override
					public PharmaSettingBasicInfo apply(PharmaSetting item) {
						return new PharmaSettingBasicInfo(item);
					}
				}));
		return new ResultInfo(Constants.OK, Constants.SUCCESS, datas);
	}
	
	@POST
	@Path("pharmaDetail")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultObjecttInfo pharmaDetail(@Context HttpServletRequest request,FilterConditionInfo item) {
		User token =  profileService.getProfile();
		if(token == null)
			throw new BackendException(Constants.ErrorCode.BAD_TOKEN,Constants.ERROR_MESSAGE.BAD_TOKEN);
		if(item.getFromTime() == null || item.getToTime() == null )
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED + " fromTime*, toTime*, product*");
		ArrayList<String> listToolId = new ArrayList<String>();
		if(!CollectionUtils.isEmpty(item.getListShopId())){
			for(Integer data : item.getListShopId()) {
				ParsehubSetting parsehubSetting = parsehubService.findSettingById(data);
				listToolId.add(parsehubSetting.getToolId());
			}
		}
		item.setListToolId(listToolId);
		List<PharmacyCrawlBasicInfo> dataPharma = new ArrayList<PharmacyCrawlBasicInfo>();
		List<PharmacyCrawlBasicInfo> dataParhub = new ArrayList<PharmacyCrawlBasicInfo>();
		if(CollectionUtils.isEmpty(item.getListShopId())) {
			if(item.getShopId() != null && !StringUtils.isEmpty(item.getToolId())) {
				if(item.getToolId().equals(Constants.TOOLID.PHARMA_TOOL) || item.getChildrenId() != null)
					dataPharma = mainService.detailPharma(item);
				if(item.getToolId().equals(Constants.TOOLID.PARSEHUB_TOOL))
					dataParhub = parsehubService.detailParsehub(item);
			}
			else {	
				dataPharma = mainService.detailPharma(item);
			    dataParhub = parsehubService.detailParsehub(item);
			}
		}else {
			if(item.getListToolId().contains(Constants.TOOLID.PHARMA_TOOL))
				dataPharma = mainService.detailPharma(item);
			if(item.getListToolId().contains(Constants.TOOLID.PARSEHUB_TOOL))
				dataParhub = parsehubService.detailParsehub(item);
		}
		List<PharmacyCrawlBasicInfo> data = new ArrayList<PharmacyCrawlBasicInfo>(); 
		if(!CollectionUtils.isEmpty(dataPharma)) data.addAll(dataPharma);
		if(!CollectionUtils.isEmpty(dataParhub)) data.addAll(dataParhub);
		return new ResultObjecttInfo(Constants.OK, Constants.SUCCESS,data);
	}
	
	@POST
	@Path("exportPharmaDetail")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultInfo exportPharmaDetail(@Context HttpServletRequest request,FilterConditionInfo item) {
		if(item == null ||item.getFromTime() == null || item.getToTime() == null || CollectionUtils.isEmpty(item.getPzns()))
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED + " fromTime*, toTime*, product*");
		ResultObjecttInfo dataInfo = pharmaDetail(request, item);
		@SuppressWarnings("unchecked")
		List<PharmacyCrawlBasicInfo> data = (List<PharmacyCrawlBasicInfo>) dataInfo.getData();
		
		String baseName =  "Blupassion_Pharma_"+ new Date().getTime();
		String fileName = baseName+ ".xlsx";
		File uploadFile = null;
		List<PharmaSetting> pharmaSettings = new ArrayList<PharmaSetting>();
		pharmaSettings = settingService.findByActiveTrue();
		List<PharmaSettingBasicInfo> pharmaSettingInfo = Lists.newArrayList(
				Iterables.transform(pharmaSettings, new Function<PharmaSetting, PharmaSettingBasicInfo>() {
					@Override
					public PharmaSettingBasicInfo apply(PharmaSetting item) {
						return new PharmaSettingBasicInfo(item);
					}
				}));
		try {
			String directoryFileUpload = serverConfig.getDirectoryFileUpload();
			File uploadFolder = new File(directoryFileUpload, Constants.folder.GARBAGE_EXCEL);
			uploadFile = new File(uploadFolder, fileName);
			FileUtils.forceMkdir(uploadFolder);
			
			XSSFWorkbook workbook = new XSSFWorkbook();
			Integer sheetIndex = 0;
		    XSSFSheet sheet = workbook.createSheet("Pharma_ "+ sheetIndex);
		    int rowNum = 0;
			   
		    CellStyle style = workbook.createCellStyle();
		    style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		    style.setWrapText(true);
		    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    Font font = workbook.createFont();
		    font.setColor(IndexedColors.WHITE.getIndex());
		    font.setFontHeightInPoints((short) 20);
		    font.setBold(true);
	        style.setFont(font);
	        style.setAlignment(HorizontalAlignment.CENTER);
	        style.setVerticalAlignment(VerticalAlignment.CENTER);
	        Row rowTitle = sheet.createRow(rowNum++);
	        rowTitle.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
	    	Cell titleCell = rowTitle.createCell(0);
	    	titleCell.setCellStyle(style);
	    	String sFromTime = DateTimeUtility.formatInputDate(item.getFromTime());
	    	String sToTime = DateTimeUtility.formatInputDate(item.getToTime() == null ? new Date() : item.getToTime());
	    	titleCell.setCellValue("Zeitraum: "+  sFromTime + " - " + sToTime );
	    	 sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
	    	rowNum++;rowNum++;rowNum++;
	    	CellStyle styleHearder = workbook.createCellStyle();
	    	styleHearder.setFillForegroundColor(IndexedColors.WHITE.getIndex());
	    	styleHearder.setWrapText(true);
	    	styleHearder.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    Font fontHearder = workbook.createFont();
		    fontHearder.setColor(IndexedColors.BLACK.getIndex());
		    fontHearder.setFontHeightInPoints((short) 14);
		    fontHearder.setBold(true);
		    styleHearder.setFont(fontHearder);
		    Row rowHearder = sheet.createRow(rowNum++);
//		    rowHearder.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
	    	
		    Cell hearderDateTime = rowHearder.createCell(0);
		    hearderDateTime.setCellStyle(styleHearder);
		    hearderDateTime.setCellValue("Datum");
	    	
		    Cell hearderShop = rowHearder.createCell(1);
		    hearderShop.setCellStyle(styleHearder);
		    hearderShop.setCellValue("Apotheke");
		    
		    Cell hearderProduct = rowHearder.createCell(2);
		    hearderProduct.setCellStyle(styleHearder);
		    hearderProduct.setCellValue("Produkt");
		    
		    Cell hearderPzn = rowHearder.createCell(3);
		    hearderPzn.setCellStyle(styleHearder);
		    hearderPzn.setCellValue("PNZ");
		    
		    Cell hearderAvp = rowHearder.createCell(4);
		    hearderAvp.setCellStyle(styleHearder);
		    hearderAvp.setCellValue("AVP");
		    
		    Cell hearderPrice = rowHearder.createCell(5);
		    hearderPrice.setCellStyle(styleHearder);
		    hearderPrice.setCellValue("Preis");
		    
		    Cell hearderQuantity = rowHearder.createCell(6);
		    hearderQuantity.setCellStyle(styleHearder);
		    hearderQuantity.setCellValue("Menge");
		    
		    Cell hearderKeyword = rowHearder.createCell(7);
		    hearderKeyword.setCellStyle(styleHearder);
		    hearderKeyword.setCellValue("Keyword / Kategorie / Crosselling");
		    
		    Cell hearderPlace = rowHearder.createCell(8);
		    hearderPlace.setCellStyle(styleHearder);
		    hearderPlace.setCellValue("Platzierung");

		    
		    CellStyle styleDetail = workbook.createCellStyle();
//		    styleDetail.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		    styleDetail.setWrapText(true);
//		    styleDetail.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    CellStyle styleDetailRight = workbook.createCellStyle();
//		    styleDetail.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		    styleDetailRight.setWrapText(true);
		    styleDetailRight.setAlignment(HorizontalAlignment.RIGHT);
		    
		    
		    CreationHelper createHelper = workbook.getCreationHelper();
		    CellStyle cellStyleDate = workbook.createCellStyle();
		    cellStyleDate.setDataFormat(
		        createHelper.createDataFormat().getFormat("dd.mm.yyyy"));
		    
		    if(!CollectionUtils.isEmpty(data)) {
		    	for(PharmacyCrawlBasicInfo info  : data) {
		    		Row rowData = sheet.createRow(rowNum++);
					Integer cellIndex = 0;
					
					Cell cell0 = rowData.createCell(cellIndex++);
//					XSSFCreationHelper createHelper = workbook.getCreationHelper();
					cell0.setCellStyle(cellStyleDate);
//					cell0.setCellValue(createHelper.createDataFormat().getFormat("dd MMMM, yyyy"));
					cell0.setCellValue(info.getImportDate());
					
					Cell cell1 = rowData.createCell(cellIndex++);
					cell1.setCellStyle(styleDetail);
					cell1.setCellValue(info.getParsehubName());	

					Cell cell2 = rowData.createCell(cellIndex++);
					cell2.setCellStyle(styleDetail);
					cell2.setCellValue(info.getName());	

					Cell cell3 = rowData.createCell(cellIndex++);
					cell3.setCellStyle(styleDetail);
					cell3.setCellValue(info.getPzn());	
					
					Cell cell4 = rowData.createCell(cellIndex++);
					cell4.setCellStyle(styleDetail);
					String fullPrice = info.getFullPrice();
					Double avp= null;
					if(!StringUtils.isEmpty(fullPrice) && fullPrice.indexOf("VP:") > 0) {
						fullPrice = fullPrice.substring(fullPrice.indexOf("VP:")+3,fullPrice.length());
						if(fullPrice.indexOf("€") > 0) 
							fullPrice = fullPrice.substring(0,fullPrice.indexOf("€")-1);
						fullPrice = fullPrice.replace(",", ".").trim();
						try {
							avp = Double.parseDouble(fullPrice.trim());
						} catch (Exception e) {
							avp = null;
						}
					}else {
						avp = info.getPriceInDouble();
					}
					if(avp != null)
						cell4.setCellValue(avp);	
					
					Cell cell5 = rowData.createCell(cellIndex++);
					cell5.setCellStyle(styleDetail);
					if(info.getPriceInDouble()!= null)
						cell5.setCellValue(info.getPriceInDouble());	
					
					Cell cell6 = rowData.createCell(cellIndex++);
					cell6.setCellStyle(styleDetail);
					if( info.getQuantity() != null)
					cell6.setCellValue(info.getQuantity());	
					
					Cell cell7 = rowData.createCell(cellIndex++);
					cell7.setCellStyle(styleDetail);
					String keywordName = StringUtils.isEmpty(item.getKeywordName()) ? PharmaSettingBasicInfo.convertKeyword(info.getKeyword(), pharmaSettingInfo) : item.getKeywordName();
					cell7.setCellValue(keywordName);	
					
					Cell cell8 = rowData.createCell(cellIndex++);
					cell8.setCellStyle(styleDetail);
					cell8.setCellValue(info.getSequenceByKeyword());	
		    	}
		    }
		    sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.setAutoFilter(new CellRangeAddress(4, rowNum, 0, 8));
			FileOutputStream outputStream = new FileOutputStream(uploadFile);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
           
//            final byte[] fileBytes = FileUtils.readFileToByteArray(uploadFile);
//			StreamingOutput stream = new StreamingOutput() {
//		        public void write(OutputStream output) throws IOException,
//		                WebApplicationException {
//		            try {
//		                output.write(fileBytes);
//		            } catch (Exception e) {
//		                throw new WebApplicationException(e);
//		            }
//		        }
//		    };
//		    Response.ResponseBuilder response = Response.ok(stream);
//		    response.header("Content-Disposition",
//		            "filename=" + fileName);
		    return new ResultInfo(Constants.OK,fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//			if(uploadFile !=null)
//				FileUtil.deleteFile(uploadFile.getAbsolutePath());
		}
		 return new ResultInfo(Constants.ERROR);
	}	
	
	@GET
	@Path("downloadExcelPharmaDetail")
	@Produces("application/xlsx")
	public Response createPDFRoomInfo(@Context HttpServletRequest request ,@QueryParam("reportId") String reportId){		
		if(StringUtils.isEmpty(reportId))
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED);
		String directoryFileUpload = serverConfig.getDirectoryFileUpload();
		File uploadFolder = new File(directoryFileUpload, Constants.folder.GARBAGE_EXCEL);
		File uploadFile = new File(uploadFolder, reportId);
		try {
			final byte[] fileBytes = FileUtils.readFileToByteArray(uploadFile);
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
		    FileUtils.deleteQuietly(uploadFile);
		}
		return null;
		   
	}
	@GET
	@Path("getPharmaSettingById")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultObjecttInfo getPharmaSettingById(@Context HttpServletRequest request, @QueryParam("id") Integer id) {
		User token =  profileService.getProfile();
		if(token == null)
			throw new BackendException(Constants.ErrorCode.BAD_TOKEN,Constants.ERROR_MESSAGE.BAD_TOKEN);
		if(id == null)
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_DONT_EXIST);
		PharmaSetting pharmaSetting = settingService.findById(id);
		if(pharmaSetting != null) {
			PharmaSettingBasicInfo result = new PharmaSettingBasicInfo(pharmaSetting);	
			return new ResultObjecttInfo(Constants.OK, Constants.SUCCESS,result);
		}
		return new ResultObjecttInfo(Constants.OK, Constants.SUCCESS);
	}
	@GET
	@Path("getAllParsehubSetting")
	public ResultInfo getAllParsehubSetting(@Context HttpServletRequest request) {
		User token =  profileService.getProfile();
		if(token == null)
			throw new BackendException(Constants.ErrorCode.BAD_TOKEN,Constants.ERROR_MESSAGE.BAD_TOKEN);
		List<ParsehubSetting> parsehubSetting = new ArrayList<ParsehubSetting>();
		parsehubSetting = parseHubService.findAllSetting();
		
		return new ResultInfo(Constants.OK, Constants.SUCCESS, parsehubSetting);
	}
	
}
