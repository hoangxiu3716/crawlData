package de.gimik.apps.parsehub.backend.repository.user;


import org.springframework.data.jpa.repository.JpaRepository;

import de.gimik.apps.parsehub.backend.model.User;



public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom
{
	User findByUsername(String username);
	User findByUsernameAndPassword(String username,String password);
}