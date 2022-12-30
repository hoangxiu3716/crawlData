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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.model.ParsehubDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;
import de.gimik.apps.parsehub.backend.repository.parsehub.ParsehubDetailRepository;
import de.gimik.apps.parsehub.backend.repository.parsehub.ParsehubSettingRepository;
import de.gimik.apps.parsehub.backend.service.CommonService;
import de.gimik.apps.parsehub.backend.service.ParsehubService;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.FilterConditionInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmacyCrawlBasicInfo;

/**
 *
 * @author trung
 */
@Service
@Transactional
public class ParsehubServiceImpl extends CommonService implements ParsehubService {

	@Autowired
	private ParsehubSettingRepository parsehubSettingRepository;
	@Autowired
	private ParsehubDetailRepository parsehubDetailRepository;


	@Override
	public ParsehubSetting findSettingById(Integer id) {
		return parsehubSettingRepository.findOne(id);
	}
	@Override
	public ParsehubSetting findByCodeActiveTrue(String code) {
		return parsehubSettingRepository.findByCodeAndActiveTrue(code);
	}
	@Override
	public List<ParsehubSetting> findAllSetting() {
		return parsehubSettingRepository.findByActiveTrue();
	}
	@Override
	public void createDetail(ParsehubDetail item) {
		if (item == null)
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED);
		parsehubDetailRepository.save(item);
	}

	@Override
	public void createListDetail( List<ParsehubDetail> items) {
		if (CollectionUtils.isEmpty(items))
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED);
		parsehubDetailRepository.save(items);
	}

//	@Override
//	public void update(RemoteClientInfo clientInfo, ArManager item) {
//		if (item == null)
//			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED);
//		repository.save(item);
//		actionLogService.log(Constants.Object.AR, Constants.Action.UPDATE, item.toString(), clientInfo.getIp());
//	}

//	@Override
//	public void delete(RemoteClientInfo clientInfo, ArManager item) {
//		if (item != null) {
//			repository.delete(item);
//			actionLogService.log(Constants.Object.AR, Constants.Action.DELETE, item.toString(),
//					clientInfo.getIp());
//		}
//	}
//	@Override
//	public void deleteByAppManager(RemoteClientInfo clientInfo, AppManager app) {
//		if (app != null) {
//			repository.deleteByAppManager(app);
//			actionLogService.log(Constants.Object.AR, Constants.Action.DELETE, app.toString(),
//					clientInfo.getIp());
//		}
//	}
	@Override
	public List<PharmacyCrawlBasicInfo> detailParsehub(FilterConditionInfo item) {
		return parsehubDetailRepository.detailParsehub(item);
	}
	@Override
	public List<ParsehubSetting> findByIdIn(List<Integer> ids) {
		return parsehubSettingRepository.findByIdIn(ids);
	}
}
