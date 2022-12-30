package de.gimik.apps.parsehub.backend.web.RESTful;

import java.util.ArrayList;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

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
import de.gimik.apps.parsehub.backend.model.BasePharma;
import de.gimik.apps.parsehub.backend.model.GroupPharmaDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;
import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.service.GroupPharmaService;
import de.gimik.apps.parsehub.backend.service.ParsehubService;
import de.gimik.apps.parsehub.backend.service.PharmaProductService;
import de.gimik.apps.parsehub.backend.service.RemoteClientInfo;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.web.viewmodel.PageInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.ResultInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.ResultObjecttInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.TransferHelper;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.BasePharmaInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaBasicInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaViewInfo;

@Component
@Path("/grouppharma")
public class GroupPharmaResource {
	@Autowired
	private GroupPharmaService mainService;
	@Autowired
	private PharmaProductService pharmaProductService;
	@Autowired
	private ParsehubService parsehubService;
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
		Page<GroupPharmaViewInfo> page = mainService.findAll(pageIndex, pageSize, field,
				direction, filter);
		return TransferHelper.convertToGroupPharmaPageInfo(page);
	}
	@Path("create")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ResultObjecttInfo create(@Context HttpServletRequest request,GroupPharmaBasicInfo item){
		GroupPharmaDetail groupPharma = new GroupPharmaDetail();
		groupPharma.setName(item.getName());
		groupPharma.setType(item.getType());
		groupPharma.setActive(true);
		if(!StringUtils.isEmpty(item.getShops())) {
			Map<Integer, Boolean> shopMaps = new Gson().fromJson(item.getShops(), new TypeToken<HashMap<Integer, Boolean>>() {}.getType());
			List<Integer> ids = ParsehubSetting.getShopIds(shopMaps);
			if(!CollectionUtils.isEmpty(ids)) {
				List<ParsehubSetting> shops = parsehubService.findByIdIn(ids);
				if(!CollectionUtils.isEmpty(shops))
					groupPharma.setShops(shops);
			}
		}
		mainService.create(new RemoteClientInfo(request), groupPharma);
		if(!CollectionUtils.isEmpty(item.getBasePharma())) {
			List<BasePharma> basePharmas = new ArrayList<BasePharma>();
			for(BasePharmaInfo data : item.getBasePharma()) {
				if(data.getBaseId() != null) {
					PharmaProduct pharmaId = pharmaProductService.findOneById(data.getBaseId());
					if(pharmaId != null) {
						BasePharma pharmaBase = new BasePharma();
						pharmaBase.setPharmaProduct(pharmaId);
						pharmaBase.setGroupPharmaDetail(groupPharma);
						basePharmas.add(pharmaBase);
					}
				}
			}
			if(!CollectionUtils.isEmpty(basePharmas))
				mainService.createListPharma(new RemoteClientInfo(request), basePharmas);
		}
		return new ResultObjecttInfo(Constants.OK, Constants.SUCCESS,new GroupPharmaBasicInfo(groupPharma));
	}
	@Path("update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ResultObjecttInfo update(@Context HttpServletRequest request,GroupPharmaBasicInfo item){
		GroupPharmaDetail groupPharma = mainService.findById(item.getId());
		groupPharma.setName(item.getName());
		groupPharma.setType(item.getType());
		groupPharma.setActive(item.isActive());
		if(!StringUtils.isEmpty(item.getShops())) {
			Map<Integer, Boolean> shopMaps = new Gson().fromJson(item.getShops(), new TypeToken<HashMap<Integer, Boolean>>() {}.getType());
			List<Integer> ids = ParsehubSetting.getShopIds(shopMaps);
			if(!CollectionUtils.isEmpty(ids)) {
				List<ParsehubSetting> shops = parsehubService.findByIdIn(ids);
				if(!CollectionUtils.isEmpty(shops))
					groupPharma.setShops(shops);
			}else
				groupPharma.setShops(null);
		}else
			groupPharma.setShops(null);
		mainService.update(new RemoteClientInfo(request), groupPharma);
		if(!CollectionUtils.isEmpty(item.getBasePharma())) {
			List<BasePharma> basePharmas = new ArrayList<BasePharma>();
			List<Integer> basePharmaIds = BasePharmaInfo.getBasePharmaIds(item.getBasePharma());
			if(!CollectionUtils.isEmpty(basePharmaIds))
				mainService.deActiveBasePharmaByGroupPharmaDetailAndInNotIn(groupPharma.getId(), basePharmaIds);
			for(BasePharmaInfo data : item.getBasePharma()) {
				if(data.getId() == null) {
				if(data.getBaseId() != null) {
					PharmaProduct pharmaId = pharmaProductService.findOneById(data.getBaseId());
					if(pharmaId != null) {
						BasePharma basePharma = mainService.findBasePharmaByGroupPharmaDetailAndPharmaProduct(groupPharma, pharmaId);
						if(basePharma == null)
						 basePharma = new BasePharma();
						basePharma.setPharmaProduct(pharmaId);
						basePharma.setGroupPharmaDetail(groupPharma);
						basePharma.setActive(data.isBaseActive());
						basePharmas.add(basePharma);
					}
				}
				}
			}
			if(!CollectionUtils.isEmpty(basePharmas))
				mainService.createListPharma(new RemoteClientInfo(request), basePharmas);
		}
		return new ResultObjecttInfo(Constants.OK, Constants.SUCCESS,new GroupPharmaBasicInfo(groupPharma));
	}
	
	@GET
	@Path("detailGroupPharma")
	public ResultObjecttInfo detailGroupPharma(@Context HttpServletRequest request,@QueryParam("id") Integer id) {
		if(id == null)
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_DONT_EXIST);
		GroupPharmaDetail groupPharmaDetail = mainService.findById(id);
		GroupPharmaBasicInfo data = null;
		if(groupPharmaDetail != null) {
			List<BasePharma> basePharma = mainService.findPharmaDetailByGroupPharmaDetailAndActiveTrue(groupPharmaDetail);
			List<BasePharmaInfo> basePharmaInfos = BasePharmaInfo.convertToInfo(basePharma);
			data = new GroupPharmaBasicInfo();
			data.setBasePharma(basePharmaInfos);
			data.setActive(groupPharmaDetail.isActive());
			data.setType(groupPharmaDetail.getType());
		}
		return new ResultObjecttInfo(Constants.OK, Constants.SUCCESS,data);
	}
	@GET
	@Path("updateDeleted")
	public void updateDeleted(@Context HttpServletRequest request,@QueryParam("id") int id){
		GroupPharmaDetail groupPharma = mainService.findById(id);
		if(groupPharma != null){
			groupPharma.setActive(false);
			groupPharma.setDeleted(true);
			
			mainService.update(new RemoteClientInfo(request), groupPharma);
			
		}
	}
	@GET
	@Path("getAllGroupPharma")
	public ResultInfo getAllGroupPharma(@Context HttpServletRequest request) {
	
		List<GroupPharmaDetail> groupPharma = new ArrayList<GroupPharmaDetail>();
		groupPharma = mainService.findAllAndActiveTrue();
		List<GroupPharmaViewInfo> datas = Lists.newArrayList(
				Iterables.transform(groupPharma, new Function<GroupPharmaDetail, GroupPharmaViewInfo>() {
					@Override
					public GroupPharmaViewInfo apply(GroupPharmaDetail item) {
						return new GroupPharmaViewInfo(item);
					}
				}));
		return new ResultInfo(Constants.OK, Constants.SUCCESS, datas);
	}
}
