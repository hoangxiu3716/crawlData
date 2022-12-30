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

import de.gimik.apps.parsehub.backend.model.PharmaScreenShot;
import de.gimik.apps.parsehub.backend.model.PharmaSetting;

/**
 *
 * @author trung
 */
public interface PharmaSettingService {
	List<PharmaSetting> findByActiveTrue();
	PharmaSetting findById(Integer id);
	void createListDetail(List<PharmaScreenShot> items);
	List<PharmaSetting> findByActiveTrueAndUrlIsNotNull();
	List<PharmaSetting> findByActiveTrueAndApothekeUrlIsNotNull();
	List<PharmaSetting> findByCodeAndActiveTrue(String code);
	List<PharmaSetting> findByActiveTrueAndJuvalisUrlIsNotNull();
	List<PharmaSetting> findByActiveTrueAndEuraponUrlIsNotNull();
	List<PharmaSetting> findByActiveTrueAndApodiscounterUrlIsNotNull();
	List<PharmaSetting> findByActiveTrueAndMedpexUrlIsNotNull();
	List<PharmaSetting> findByActiveTrueAndMedikamenteUrlIsNotNull();
	List<PharmaSetting> findByActiveTrueAndDocmorrisUrlIsNotNull();
	List<PharmaSetting> findByActiveTrueAndAponeoUrlIsNotNull();
	List<PharmaSetting> findByActiveTrueAndApotalUrlIsNotNull();
	List<PharmaSetting> findByActiveTrueAndMycareUrlIsNotNull();
}
