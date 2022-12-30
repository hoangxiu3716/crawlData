package de.gimik.apps.parsehub.backend.repository.role;


import org.springframework.data.jpa.repository.JpaRepository;

import de.gimik.apps.parsehub.backend.model.Role;

/**
 * Created by dang on 30.08.2014.
 */
public interface RoleRepository extends JpaRepository<Role, Long>, RoleRepositoryCustom {
    Role findByName(String name);
}
