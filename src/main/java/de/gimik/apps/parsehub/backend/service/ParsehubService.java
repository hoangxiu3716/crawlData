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

import de.gimik.apps.parsehub.backend.model.ParsehubDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.FilterConditionInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmacyCrawlBasicInfo;

/**
 *
 * @author trung
 */
public interface ParsehubService {

	ParsehubSetting findSettingById(Integer id);

	void createDetail(ParsehubDetail item);

	void createListDetail(List<ParsehubDetail> items);

	List<ParsehubSetting> findAllSetting();

	List<PharmacyCrawlBasicInfo> detailParsehub(FilterConditionInfo item);

	ParsehubSetting findByCodeActiveTrue(String code);
	List<ParsehubSetting> findByIdIn(List<Integer> ids);
   
}
