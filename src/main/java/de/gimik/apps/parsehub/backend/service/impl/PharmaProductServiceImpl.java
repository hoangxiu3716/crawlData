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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.repository.pharmaproduct.PharmaProductRepository;
import de.gimik.apps.parsehub.backend.repository.pharmaproduct.PharmaProductRepositoryCustom;
import de.gimik.apps.parsehub.backend.service.CommonService;
import de.gimik.apps.parsehub.backend.service.PharmaProductService;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmaProductBasicInfo;

/**
 *
 * @author trung
 */
@Service
@Transactional
public class PharmaProductServiceImpl extends CommonService implements PharmaProductService {

	@Autowired
	private PharmaProductRepository mainRepository;
	@Autowired
	private PharmaProductRepositoryCustom customRepository;


	@Override
    public PharmaProduct findByPznAndActiveTrue(String pzn) {
        return mainRepository.findByPznAndActiveTrue(pzn);
    }
	@Override
	public List<PharmaProduct> findByActiveTrue(){
		return mainRepository.findByActiveTrue();
	}
	@Override
	public void create( PharmaProduct item) {
		if (item == null)
			throw new BackendException(Constants.ERROR_MESSAGE.KEY_INPUT_REQUIRED + " data must not empty ");
		mainRepository.save(item);
	}
	@Override
	public PharmaProduct findOneById(Integer id) {
		return mainRepository.findOne(id);
	}
	@Override
    public Page<PharmaProductBasicInfo> findAll(int pageIndex, int pageSize, String sortField, String sortDirection, Map<String, String> filter) {
        return customRepository.findAll(constructPageSpecification(pageIndex, pageSize, sortField, sortDirection), filter);
    }

}
