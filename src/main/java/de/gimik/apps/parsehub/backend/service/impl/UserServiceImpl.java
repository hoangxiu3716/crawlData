/*
 * Copyright 2014 dang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.gimik.apps.parsehub.backend.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.model.User;
import de.gimik.apps.parsehub.backend.repository.user.UserRepository;
import de.gimik.apps.parsehub.backend.security.DefaultUserDetails;
import de.gimik.apps.parsehub.backend.security.MD5Encoder;
import de.gimik.apps.parsehub.backend.service.ActionLogService;
import de.gimik.apps.parsehub.backend.service.CommonService;
import de.gimik.apps.parsehub.backend.service.RemoteClientInfo;
import de.gimik.apps.parsehub.backend.service.UserService;
import de.gimik.apps.parsehub.backend.util.Constants;

/**
 *
 * @author dang
 */
@Service
@Transactional
public class UserServiceImpl extends CommonService implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActionLogService actionLogService;

    @Autowired
    private MD5Encoder passwordEncoder;

    @Override
    public DefaultUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("The user with name " + username + " was not found");
        }

        /*
        ArrayList<Role> list = new ArrayList<>();
        for (Role r: user.getRoles()) {
            Role role = new Role();
            role.setName(r.getAuthority());
            list.add(role);
        }
        */

        DefaultUserDetails defaultUserDetails = new DefaultUserDetails(user);
        return defaultUserDetails;
    }

    @Override
    public User save(RemoteClientInfo clientInfo, User user) {
        if (user.getRoles() == null || user.getRoles().size() <= 0)
            throw new BackendException(Constants.ErrorCode.USER_ROLE_NOT_SET);

        User found = userRepository.findByUsername(user.getUsername());

        if (found == null)
            throw new BackendException(Constants.ErrorCode.USERNAME_NOT_EXIST);

        String oldInfo = getUserInfo(found);

        userRepository.save(user);

        String newInfo = getUserInfo(user);

        actionLogService.log(Constants.Object.USER, Constants.Action.UPDATE,
                "Old Info: [" + oldInfo + "]" + "\n"
                        + "New Info: [" + newInfo + "]", clientInfo.getIp());

        return user;

    }

    @Override
    public User addNewUser(RemoteClientInfo clientInfo, User user) {
        if (user.getRoles() == null || user.getRoles().size() <= 0)
            throw new BackendException(Constants.ErrorCode.USER_ROLE_NOT_SET);

        User found = userRepository.findByUsername(user.getUsername());

        if (found != null)
            throw new BackendException(Constants.ErrorCode.USERNAME_DUPLICATED);

        String password = user.getPassword();
        password = passwordEncoder.encode(password);
        user.setPassword(password);

        actionLogService.log(Constants.Object.USER, Constants.Action.ADD, getUserInfo(user), clientInfo.getIp());

        return userRepository.save(user);
    }

    @Override
    public void delete(RemoteClientInfo clientInfo, int userID) {
        actionLogService.log(Constants.Object.USER, Constants.Action.DELETE, getUserInfo(userID), clientInfo.getIp());

        userRepository.delete(userID);
    }

    @Override
    public User getByID(int userID) {
        return userRepository.findOne(userID);
    }

    private String getUserInfo(int userID){
        User user = userRepository.findOne(userID);

        return getUserInfo(user);
    }

    private String getUserInfo(User user) {
        if (user != null){
            return user.toString();
        }

        return "";
    }

    @Override
    public Page<User> getAll(int pageIndex, int pageSize, String sortField, String sortDirection, Map<String, String> filter) {
        /*
        if (CollectionUtils.isEmpty(filter)) {
            return getAll(pageIndex, pageSize, sortField, sortDirection);
        }
        */
        return userRepository.findAll(constructPageSpecification(pageIndex, pageSize, sortField, sortDirection), filter);
    }

    @Override
    public Page<User> getAll(int pageIndex, int pageSize, String sortField, String sortDirection) {
        return userRepository.findAll(constructPageSpecification(pageIndex, pageSize, sortField, sortDirection));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public String defaultSortField() {
        return User.USER_NAME;
    }

    @Override
    public Sort.Direction defaultSortDirection() {
        return Sort.Direction.ASC;
    }
    
    @Override
    public User changePass(RemoteClientInfo clientInfo, User user) {
        if (user.getRoles() == null || user.getRoles().size() <= 0)
            throw new BackendException(Constants.ErrorCode.USER_ROLE_NOT_SET);

        User found = userRepository.findByUsername(user.getUsername());

        if (found == null)
        	 throw new BackendException(Constants.ErrorCode.USERNAME_NOT_EXIST);

        String password = user.getPassword();
        password = passwordEncoder.encode(password);
        user.setPassword(password);

        actionLogService.log(Constants.Object.USER, Constants.Action.UPDATE, getUserInfo(user), clientInfo.getIp());
        
        return userRepository.save(user);
    }

	@Override
	public DefaultUserDetails findByUsernameAndPassword(String username, String password) {
		password = passwordEncoder.encode(password);
		User user = userRepository.findByUsernameAndPassword(username,password);
		if(user == null){
			return null;
		}
	    DefaultUserDetails defaultUserDetails = new DefaultUserDetails(user);
		return defaultUserDetails;
	}

    
}
