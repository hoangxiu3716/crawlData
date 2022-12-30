package de.gimik.apps.parsehub.backend.repository.pharma;
import org.springframework.data.jpa.repository.JpaRepository;

import de.gimik.apps.parsehub.backend.model.PharmaDetail;

public interface PharmaDetailRepository extends JpaRepository<PharmaDetail, Integer>,PharmaDetailCustomRepository {
	
}