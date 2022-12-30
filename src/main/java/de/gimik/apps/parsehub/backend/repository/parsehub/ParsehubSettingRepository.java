package de.gimik.apps.parsehub.backend.repository.parsehub;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.gimik.apps.parsehub.backend.model.GroupPharmaDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;

public interface ParsehubSettingRepository extends JpaRepository<ParsehubSetting, Integer> {
	List<ParsehubSetting> findByActiveTrue();
	ParsehubSetting findByCodeAndActiveTrue(String code);
	List<ParsehubSetting> findByIdIn(List<Integer> ids);
	
}