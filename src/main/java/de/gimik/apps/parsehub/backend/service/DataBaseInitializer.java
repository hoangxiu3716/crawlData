package de.gimik.apps.parsehub.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import de.gimik.apps.parsehub.backend.model.Role;
import de.gimik.apps.parsehub.backend.model.User;
import de.gimik.apps.parsehub.backend.repository.role.RoleRepository;
import de.gimik.apps.parsehub.backend.util.Constants;

public class DataBaseInitializer {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    public void initDataBase() {
        initRoleUserData();
    }

    private void initRoleUserData() {
        String[] roles = new String[]{Constants.ROLE_ADMIN,Constants.ROLE_CUSTOMER,Constants.ROLE_EMPLOYEES,Constants.ROLE_AGENCY,Constants.ROLE_AGENT};
        String[] roleDescriptions = new String[]{"Administrators","Customer","Employees","Agency","agent"};
        for (int i = 0; i < roles.length; i++) {
            Role role = this.roleRepository.findByName(roles[i]);
            if (role == null) {
                role = new Role();
                role.setName(roles[i]);
                role.setDescription(roleDescriptions[i]);
                this.roleRepository.save(role);
            }
        }

        User adminUser = this.userService.findByUsername("admin");
        if (adminUser == null) {
            adminUser = new User("admin", "admin");

            adminUser.addRole(this.roleRepository.findByName(Constants.ROLE_ADMIN));
            adminUser.setFullname("Administrator");

            this.userService.addNewUser(RemoteClientInfo.backgroundOne("DataBaseInitializer"), adminUser);
        }
    }

}
