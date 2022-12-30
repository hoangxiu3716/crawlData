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

import de.gimik.apps.parsehub.backend.model.BasePharma;
import de.gimik.apps.parsehub.backend.model.GroupPharmaDetail;
import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaViewInfo;

/**
 *
 * @author trung
 */
public interface GroupPharmaService {
	Page<GroupPharmaViewInfo> findAll(int pageIndex, int pageSize, String sortField, String sortDirection,
			Map<String, String> filter);
	void create(RemoteClientInfo clientInfo, GroupPharmaDetail item);
	void createListPharma(RemoteClientInfo clientInfo, List<BasePharma> items);
	GroupPharmaDetail findById(Integer id);
	List<BasePharma> findPharmaDetailByGroupPharmaDetailAndActiveTrue(GroupPharmaDetail groupPharmaDetail);
	void update(RemoteClientInfo clientInfo, GroupPharmaDetail item);
	BasePharma findBasePharmaByGroupPharmaDetailAndPharmaProduct(GroupPharmaDetail groupPharmaDetail,
			PharmaProduct pharmaProduct);
	void deActiveBasePharmaByGroupPharmaDetailAndInNotIn(Integer groupId,
			List<Integer> basePharmaIds);
	List<GroupPharmaDetail> findAllAndActiveTrue();
	List<PharmaProduct> findProductByGroup(GroupPharmaDetail groupPharmaDetail);
}
