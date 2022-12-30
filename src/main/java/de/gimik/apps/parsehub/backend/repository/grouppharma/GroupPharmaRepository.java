package de.gimik.apps.parsehub.backend.repository.grouppharma;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gimik.apps.parsehub.backend.model.GroupPharmaDetail;

public interface GroupPharmaRepository extends JpaRepository<GroupPharmaDetail, Integer>,GroupPharmaCustomRepository {
	List<GroupPharmaDetail> findByActiveTrue();
}