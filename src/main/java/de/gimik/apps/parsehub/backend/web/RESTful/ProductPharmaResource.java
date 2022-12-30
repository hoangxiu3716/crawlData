package de.gimik.apps.parsehub.backend.web.RESTful;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.model.GroupPharmaDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;
import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.model.PharmaSetting;
import de.gimik.apps.parsehub.backend.service.GroupPharmaService;
import de.gimik.apps.parsehub.backend.service.ParsehubService;
import de.gimik.apps.parsehub.backend.service.PharmaDetailService;
import de.gimik.apps.parsehub.backend.service.PharmaProductService;
import de.gimik.apps.parsehub.backend.service.PharmaSettingService;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.util.DateTimeUtility;
import de.gimik.apps.parsehub.backend.util.ServerConfig;
import de.gimik.apps.parsehub.backend.web.viewmodel.PageInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.ResultInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.TransferHelper;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaBasicInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmaSettingBasicInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmacyCrawlBasicInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pushhub.PharmacyBasicData;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmaProductBasicInfo;

@Component
@Path("/productpharma")
public class ProductPharmaResource {
	@Autowired
	private ServerConfig serverConfig;
	@Autowired
	private PharmaProductService mainService;
	@Autowired
	private GroupPharmaService groupPharmaService;
	@Autowired
	ParsehubService parsehubService;
	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public PageInfo list(@DefaultValue("0") @QueryParam("page") int pageIndex,
			@DefaultValue("10") @QueryParam("size") int pageSize,
			@QueryParam("field") String field,
			@QueryParam("direction") String direction,
			@QueryParam("filter") String filters
			)throws JsonGenerationException, JsonMappingException {
		Map<String, String> filter = null;
		if (!StringUtils.isEmpty(filters)) {
			filter = new Gson().fromJson(filters,
					new TypeToken<HashMap<String, String>>() {
					}.getType());
		}
		Page<PharmaProductBasicInfo> page = mainService.findAll(pageIndex, pageSize, field,
				direction, filter);
		return TransferHelper.convertToProductPharmaPageInfo(page);
	}
	
	@GET
	@Path("getAllProduct")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultInfo getAllProduct(@Context HttpServletRequest request) {
		List<PharmaProduct> groupPharma = mainService.findByActiveTrue();
		List<PharmaProductBasicInfo> datas = Lists.newArrayList(
				Iterables.transform(groupPharma, new Function<PharmaProduct, PharmaProductBasicInfo>() {
					@Override
					public PharmaProductBasicInfo apply(PharmaProduct item) {
						return new PharmaProductBasicInfo(item);
					}
				}));
		return new ResultInfo(Constants.OK, Constants.SUCCESS, datas);
	}
	
	@GET
	@Path("getProductByGroup")
	@Produces(MediaType.APPLICATION_JSON)
	public ResultInfo getProductByGroup(@Context HttpServletRequest request, @QueryParam("groupId") Integer groupId) {
		GroupPharmaDetail groupPharmaDetail = groupPharmaService.findById(groupId);
		if(groupPharmaDetail == null)
			new ResultInfo(Constants.OK, Constants.ERROR);
		if(!groupPharmaDetail.isActive()) {
			throw new BackendException(Constants.ERROR_MESSAGE.GROUP_HAVE_BEEN_DELETED);
		}
		List<PharmaProduct> pharmaProducts = groupPharmaService.findProductByGroup(groupPharmaDetail);
		List<PharmaProductBasicInfo> datas = Lists.newArrayList(
		Iterables.transform(pharmaProducts, new Function<PharmaProduct, PharmaProductBasicInfo>() {
			@Override
			public PharmaProductBasicInfo apply(PharmaProduct item) {
				return new PharmaProductBasicInfo(item);
			}
		}));
		return new ResultInfo(Constants.OK, Constants.SUCCESS, datas);
	}
	
	@POST
	@Path("importMissingData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ResultInfo importMissingData(@Context HttpServletRequest request, @FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,  @FormDataParam("parsehubId") Integer parsehubId,  @FormDataParam("dateTime") Long dateTime) {
		if(fileDetail == null || parsehubId ==null || dateTime == null)
			return new ResultInfo(Constants.OK, Constants.SUCCESS);
		ParsehubSetting item = parsehubService.findSettingById(parsehubId);
		if(item!= null)
			buidDataPharmacy(inputStream, item, item.getProjectToken(),dateTime);
		return new ResultInfo(Constants.OK, Constants.SUCCESS);
	}
	public   void buidDataPharmacy(InputStream inputStream, ParsehubSetting parsehubSetting, String projectToken ,Long dateTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateTime);
//		calendar.add(Calendar.DAY_OF_MONTH, -1);
//		calendar.set(Calendar.HOUR, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date importDate = calendar.getTime();
		try {
			JsonElement element = new com.google.gson.JsonParser().parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			JSONObject convertedObject = new JSONObject(element.getAsJsonObject().toString());
//			JSONObject convertedObject = new JSONObject(result);
			@SuppressWarnings("unchecked")
			Iterator<String> keys = convertedObject.keys();
			while(keys.hasNext()) {
			    String key = keys.next();
			    if (convertedObject.get(key) instanceof JSONArray) {
			    	JSONArray values = convertedObject.getJSONArray(key);
			    	if(values != null && values.length() > 0) {
			    		List<ParsehubDetail> parsehubDetails = new ArrayList<ParsehubDetail>();
				    	for (int i=0;i< values.length();i++){   
				    		String value = values.get(i).toString();
				    		PharmacyBasicData item = new Gson().fromJson(value, PharmacyBasicData.class);  
				    		ParsehubDetail parsehubDetail = new ParsehubDetail();
							parsehubDetail = ParsehubDetail.buildData(parsehubDetail, item,key,projectToken);
							parsehubDetail.setParsehubSetting(parsehubSetting);
							parsehubDetail.setKeyword(key);
							parsehubDetail.setImportDate(importDate);
							parsehubDetail.setSequenceByKeyword(i+1);
							System.out.println(parsehubDetail.getFullPzn());
							System.out.println(parsehubDetail.getName());
							// check product 
							if(!StringUtils.isEmpty(parsehubDetail.getPzn())) {
								PharmaProduct pharmaProductUnique = mainService.findByPznAndActiveTrue(parsehubDetail.getPzn().trim());
								if(pharmaProductUnique != null) parsehubDetail.setProductId(pharmaProductUnique.getId());
								else {
									pharmaProductUnique = new PharmaProduct(parsehubDetail.getName(), parsehubDetail.getPzn().trim());
									mainService.create(pharmaProductUnique);
									parsehubDetail.setProductId(pharmaProductUnique.getId());
								}
							}
							parsehubDetails.add(parsehubDetail);
				        }  
				    	if(!CollectionUtils.isEmpty(parsehubDetails)) System.out.println("size "+parsehubDetails.size());
				    		parsehubService.createListDetail(parsehubDetails);
			    	}
			    }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		

	}
}
