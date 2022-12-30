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

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.model.PharmaDetail;
import de.gimik.apps.parsehub.backend.repository.pharma.PharmaDetailRepository;
import de.gimik.apps.parsehub.backend.service.CommonService;
import de.gimik.apps.parsehub.backend.service.PharmaDetailService;
import de.gimik.apps.parsehub.backend.service.RemoteClientInfo;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.FilterConditionInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmacyCrawlBasicInfo;

/**
 *
 * @author trung
 */
@Service
@Transactional
public class PharmaDetailServiceImpl extends CommonService implements PharmaDetailService {

	@Autowired
	private PharmaDetailRepository mainRepository;


	@Override
	public void createListDetail( List<PharmaDetail> items) {
		if (CollectionUtils.isEmpty(items))
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED);
		mainRepository.save(items);
	}
	@Override
	public void create(RemoteClientInfo clientInfo , PharmaDetail item){
		if(item ==null)
			throw new WebApplicationException(404);
		mainRepository.save(item);
//		actionLogService.log(Constants.Object.FORM_BUILDER, Constants.Action.ADD,item.toString(), clientInfo.getIp());
	}
	@Override
    public Page<PharmacyCrawlBasicInfo> findAll(int pageIndex, int pageSize, String sortField, String sortDirection, Map<String, String> filter, Integer pharmaId,Date from,Date to,Double fromPrice,Double toPrice, Integer fromDiscount, Integer toDiscount) {
        return mainRepository.findAll(constructPageSpecification(pageIndex, pageSize, sortField, sortDirection), filter, pharmaId,from, to,fromPrice,toPrice,fromDiscount,toDiscount);
    }
	@Override
	public PharmaDetail findById(Integer id) {
		return mainRepository.findOne(id);
	}
	@Override
	public List<PharmacyCrawlBasicInfo> detailPharma(FilterConditionInfo item) {
		return mainRepository.detailPharma(item);
	}
}
