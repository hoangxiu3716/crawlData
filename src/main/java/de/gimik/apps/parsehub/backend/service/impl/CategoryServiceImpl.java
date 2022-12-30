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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.gimik.apps.parsehub.backend.model.Categories;
import de.gimik.apps.parsehub.backend.repository.pharma.CategoryRepository;
import de.gimik.apps.parsehub.backend.service.CategoryService;
import de.gimik.apps.parsehub.backend.service.CommonService;

/**
 *
 * @author trung
 */
@Service
@Transactional
public class CategoryServiceImpl extends CommonService implements CategoryService {
	@Autowired
	private CategoryRepository mainRepository;
	@Override
	public Categories findById(Integer id) {
		return mainRepository.findById(id);
	}



}
