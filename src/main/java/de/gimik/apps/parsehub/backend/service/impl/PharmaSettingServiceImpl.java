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

import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.model.PharmaDetail;
import de.gimik.apps.parsehub.backend.model.PharmaScreenShot;
import de.gimik.apps.parsehub.backend.model.PharmaSetting;
import de.gimik.apps.parsehub.backend.repository.parsehub.ParsehubDetailRepository;
import de.gimik.apps.parsehub.backend.repository.parsehub.ParsehubSettingRepository;
import de.gimik.apps.parsehub.backend.repository.pharma.PharmaScreenShotRepository;
import de.gimik.apps.parsehub.backend.repository.pharma.PharmaSettingRepository;
import de.gimik.apps.parsehub.backend.service.CommonService;
import de.gimik.apps.parsehub.backend.service.PharmaSettingService;
import de.gimik.apps.parsehub.backend.service.RemoteClientInfo;
import de.gimik.apps.parsehub.backend.util.Constants;

/**
 *
 * @author trung
 */
@Service
@Transactional
public class PharmaSettingServiceImpl extends CommonService implements PharmaSettingService {

	@Autowired
	private ParsehubSettingRepository parsehubSettingRepository;
	@Autowired
	private ParsehubDetailRepository parsehubDetailRepository;
	@Autowired
	private PharmaSettingRepository mainRepository;
	@Autowired
	private PharmaScreenShotRepository screenShotRepository;

	@Override
    public List<PharmaSetting> findByActiveTrue() {
        return mainRepository.findByActiveTrue();
    }
	
	@Override
    public List<PharmaSetting> findByActiveTrueAndUrlIsNotNull() {
        return mainRepository.findByActiveTrueAndUrlIsNotNull();
    }

	@Override
	public PharmaSetting findById(Integer id){
		return mainRepository.findById(id);
	}
	@Override
	public void createListDetail( List<PharmaScreenShot> items) {
		if (CollectionUtils.isEmpty(items))
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED);
		screenShotRepository.save(items);
	}
	@Override
    public List<PharmaSetting> findByCodeAndActiveTrue(String code) {
        return mainRepository.findByCodeAndActiveTrue(code);
    }
	@Override
    public List<PharmaSetting> findByActiveTrueAndApothekeUrlIsNotNull() {
        return mainRepository.findByActiveTrueAndApothekeUrlIsNotNull();
    }
	@Override
    public List<PharmaSetting> findByActiveTrueAndJuvalisUrlIsNotNull() {
        return mainRepository.findByActiveTrueAndJuvalisUrlIsNotNull();
    }
	@Override
    public List<PharmaSetting> findByActiveTrueAndEuraponUrlIsNotNull() {
        return mainRepository.findByActiveTrueAndEuraponUrlIsNotNull();
    }
    @Override
    public List<PharmaSetting> findByActiveTrueAndApodiscounterUrlIsNotNull() {
        return mainRepository.findByActiveTrueAndApodiscounterUrlIsNotNull();
    }
    @Override
    public List<PharmaSetting> findByActiveTrueAndMedpexUrlIsNotNull() {
        return mainRepository.findByActiveTrueAndMedpexUrlIsNotNull();
    }@Override
    public List<PharmaSetting> findByActiveTrueAndMedikamenteUrlIsNotNull() {
        return mainRepository.findByActiveTrueAndMedikamenteUrlIsNotNull();
    }
    @Override
    public List<PharmaSetting> findByActiveTrueAndDocmorrisUrlIsNotNull() {
        return mainRepository.findByActiveTrueAndDocmorrisUrlIsNotNull();
    }
    @Override
    public List<PharmaSetting> findByActiveTrueAndAponeoUrlIsNotNull() {
        return mainRepository.findByActiveTrueAndAponeoUrlIsNotNull();
    }
    @Override
    public List<PharmaSetting> findByActiveTrueAndApotalUrlIsNotNull() {
        return mainRepository.findByActiveTrueAndApotalUrlIsNotNull();
    }
    @Override
    public List<PharmaSetting> findByActiveTrueAndMycareUrlIsNotNull() {
        return mainRepository.findByActiveTrueAndMycareUrlIsNotNull();
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

}
