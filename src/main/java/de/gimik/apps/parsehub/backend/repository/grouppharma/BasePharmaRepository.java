package de.gimik.apps.parsehub.backend.repository.grouppharma;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import de.gimik.apps.parsehub.backend.model.BasePharma;
import de.gimik.apps.parsehub.backend.model.GroupPharmaDetail;
import de.gimik.apps.parsehub.backend.model.ParsehubSetting;
import de.gimik.apps.parsehub.backend.model.PharmaDetail;
import de.gimik.apps.parsehub.backend.model.PharmaProduct;

public interface BasePharmaRepository extends JpaRepository<BasePharma, Integer> {
	List<BasePharma> findByGroupPharmaDetailAndActiveTrue(GroupPharmaDetail groupPharmaDetail);
	BasePharma findByGroupPharmaDetailAndPharmaProduct(GroupPharmaDetail groupPharmaDetail, PharmaProduct pharmaProduct);
	@Modifying
	@Query(value="update base_pharma set active = 0, modification_time = NOW()  where group_pharma_id =?1 and product_id not in ?2 ",nativeQuery=true)
	void deActiveByGroupPharmaDetailAndInNotIn(Integer groupId, List<Integer> basePharmaIds);
	@Query("select b.pharmaProduct from BasePharma b where b.groupPharmaDetail = ?1 and b.active = 1 ")
	List<PharmaProduct> findProductByGroup(GroupPharmaDetail groupPharmaDetail);
}