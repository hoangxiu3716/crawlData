package de.gimik.apps.parsehub.backend.repository.parsehub;
import org.springframework.data.jpa.repository.JpaRepository;

import de.gimik.apps.parsehub.backend.model.ParsehubDetail;

public interface ParsehubDetailRepository extends JpaRepository<ParsehubDetail, Integer>, ParsehubDetailCustomRepository {
	
}