package de.gimik.apps.parsehub.backend.repository.pharma;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.FilterConditionInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmacyCrawlBasicInfo;


public interface PharmaDetailCustomRepository{
	Page<PharmacyCrawlBasicInfo> findAll(Pageable pageable, Map<String, String> filter, Integer pharmaId, Date from, Date to, Double fromPrice, Double toPrice, Integer fromDiscount, Integer toDiscount);

	List<PharmacyCrawlBasicInfo> detailPharma(FilterConditionInfo item);
}