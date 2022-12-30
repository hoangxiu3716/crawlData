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

package de.gimik.apps.parsehub.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmaProductBasicInfo;

/**
 *
 * @author trung
 */
public interface PharmaProductService {

	void create(PharmaProduct item);


	PharmaProduct findByPznAndActiveTrue(String pzn);
    PharmaProduct findOneById(Integer id);
    Page<PharmaProductBasicInfo> findAll(int pageIndex, int pageSize, String sortField, String sortDirection,
			Map<String, String> filter);

	List<PharmaProduct> findByActiveTrue();
}
