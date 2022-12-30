package de.gimik.apps.parsehub.backend.repository.grouppharma;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaBasicInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.grouppharma.GroupPharmaViewInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.FilterConditionInfo;


public interface GroupPharmaCustomRepository{
	Page<GroupPharmaViewInfo> findAll(Pageable pageable, Map<String, String> filter);

	
}