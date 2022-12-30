package de.gimik.apps.parsehub.backend.repository.pharmaproduct;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gimik.apps.parsehub.backend.model.PharmaProduct;

public interface PharmaProductRepository extends JpaRepository<PharmaProduct, Integer> {
	PharmaProduct findByPznAndActiveTrue(String pzn);
	List<PharmaProduct> findByActiveTrue();
}