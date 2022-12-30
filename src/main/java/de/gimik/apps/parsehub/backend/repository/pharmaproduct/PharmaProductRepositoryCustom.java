package de.gimik.apps.parsehub.backend.repository.pharmaproduct;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import de.gimik.apps.parsehub.backend.model.PharmaProduct;
import de.gimik.apps.parsehub.backend.web.viewmodel.pharmadetail.PharmaProductBasicInfo;

public interface PharmaProductRepositoryCustom{
	Page<PharmaProductBasicInfo> findAll(Pageable pageable, Map<String, String> filter);
}