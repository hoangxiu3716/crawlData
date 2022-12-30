package de.gimik.apps.parsehub.backend.service;

import java.util.List;

import de.gimik.apps.parsehub.backend.model.Role;

public interface RoleService {
    List<Role> getAll();

    List<Role> getAllUserRoles();

    Role getByID(long roleID);

    Role getByName(String name);
}
