package de.gimik.apps.parsehub.backend.repository.pharma;
import org.springframework.data.jpa.repository.JpaRepository;

import de.gimik.apps.parsehub.backend.model.Categories;

public interface CategoryRepository extends JpaRepository<Categories, Integer> {
	Categories findById(Integer id);
}