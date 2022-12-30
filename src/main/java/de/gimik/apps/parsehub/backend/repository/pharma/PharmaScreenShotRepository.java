package de.gimik.apps.parsehub.backend.repository.pharma;
import org.springframework.data.jpa.repository.JpaRepository;
import de.gimik.apps.parsehub.backend.model.PharmaScreenShot;

public interface PharmaScreenShotRepository extends JpaRepository<PharmaScreenShot, Integer> {
	
}