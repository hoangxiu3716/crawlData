/*
 * Copyright 2014 trung.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.gimik.apps.parsehub.backend.service.impl;

import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.model.BasePharma;
import de.gimik.apps.parsehub.backend.model.GroupPharmaDetail;
import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.repository.grouppharma.BasePharmaRepository;
import de.gimik.apps.parsehub.backend.repository.grouppharma.GroupPharmaRepository;
import de.gimik.apps.parsehub.backend.repository.parsehub.ParsehubSettingRepository;
import de.gimik.apps.parsehub.backend.service.ActionLogService;
import de.gimik.apps.parsehub.backend.service.CommonService;
import de.gimik.apps.parsehub.backend.service.GroupPharmaService;
import de.gimik.apps.parsehub.backend.service.RemoteClientInfo;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaViewInfo;

/**
 *
 * @author trung
 */
@Service
@Transactional
public class GroupPharmaServiceImpl extends CommonService implements GroupPharmaService {

	@Autowired
	private GroupPharmaRepository mainRepository;
	@Autowired
	private ActionLogService actionLogService;
	@Autowired
	private BasePharmaRepository basePharmaRepository;
	@Autowired
	private ParsehubSettingRepository pareseParsehubSetting;

	@Override
    public Page<GroupPharmaViewInfo> findAll(int pageIndex, int pageSize, String sortField, String sortDirection, Map<String, String> filter) {
        return mainRepository.findAll(constructPageSpecification(pageIndex, pageSize, sortField, sortDirection), filter);
    }
	@Override
	public GroupPharmaDetail findById(Integer id) {
		return mainRepository.findOne(id);
	}
	@Override
	public List<BasePharma> findPharmaDetailByGroupPharmaDetailAndActiveTrue(GroupPharmaDetail groupPharmaDetail) {
		return basePharmaRepository.findByGroupPharmaDetailAndActiveTrue(groupPharmaDetail);
	}
	@Override
	public List<PharmaProduct> findProductByGroup(GroupPharmaDetail groupPharmaDetail) {
		return basePharmaRepository.findProductByGroup(groupPharmaDetail);
	}
	@Override
	public void create(RemoteClientInfo clientInfo , GroupPharmaDetail item){
		if(item ==null)
			throw new WebApplicationException(404);
		mainRepository.save(item);
		actionLogService.log(Constants.Object.GROUP_PHARMA, Constants.Action.ADD,item.toString(), clientInfo.getIp());
	}
	@Override
	public void update(RemoteClientInfo clientInfo, GroupPharmaDetail item) {
		if (item == null)
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED);
		mainRepository.save(item);
		actionLogService.log(Constants.Object.GROUP_PHARMA, Constants.Action.UPDATE, item.toString(), clientInfo.getIp());
	}
	@Override
	public void createListPharma(RemoteClientInfo clientInfo, List<BasePharma> items) {
		if (CollectionUtils.isEmpty(items))
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED);
		basePharmaRepository.save(items); 
		actionLogService.log(Constants.Object.BASE_PHARMA, Constants.Action.ADD, "create list PharmaGroup", clientInfo.getIp());
	}
	@Override
	public BasePharma findBasePharmaByGroupPharmaDetailAndPharmaProduct(GroupPharmaDetail groupPharmaDetail, PharmaProduct pharmaProduct) {
		return basePharmaRepository.findByGroupPharmaDetailAndPharmaProduct(groupPharmaDetail,  pharmaProduct);
	}
	@Override
	public void deActiveBasePharmaByGroupPharmaDetailAndInNotIn(Integer groupId, List<Integer> basePharmaIds) {
		basePharmaRepository.deActiveByGroupPharmaDetailAndInNotIn(groupId, basePharmaIds);
	}
	@Override
	public List<GroupPharmaDetail> findAllAndActiveTrue() {
		return mainRepository.findByActiveTrue();
	}
}
