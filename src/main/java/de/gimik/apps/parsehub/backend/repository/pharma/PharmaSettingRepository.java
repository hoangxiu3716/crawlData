package de.gimik.apps.parsehub.backend.repository.pharma;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gimik.apps.parsehub.backend.model.PharmaSetting;

public interface PharmaSettingRepository extends JpaRepository<PharmaSetting, Integer> {
	PharmaSetting findById(Integer id);
	List<PharmaSetting> findByActiveTrue();
	List<PharmaSetting> findByActiveTrueAndUrlIsNotNull();
	List<PharmaSetting> findByCodeAndActiveTrue(String code);
	List<PharmaSetting> findByActiveTrueAndApothekeUrlIsNotNull();
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