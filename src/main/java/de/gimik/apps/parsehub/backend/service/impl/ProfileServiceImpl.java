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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.model.Role;
import de.gimik.apps.parsehub.backend.model.User;
import de.gimik.apps.parsehub.backend.repository.user.UserRepository;
import de.gimik.apps.parsehub.backend.security.DefaultUserDetails;
import de.gimik.apps.parsehub.backend.security.MD5Encoder;
import de.gimik.apps.parsehub.backend.security.SecurityUtility;
import de.gimik.apps.parsehub.backend.service.*;
import de.gimik.apps.parsehub.backend.util.Constants;

import javax.ws.rs.WebApplicationException;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author dang
 */
@Service
@Transactional
public class ProfileServiceImpl extends CommonService implements ProfileService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActionLogService actionLogService;

    @Autowired
    private MD5Encoder passwordEncoder;

    @Override
    public User getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && principal.equals("anonymousUser")) {
            return null;
        }
        DefaultUserDetails userDetails = (DefaultUserDetails) principal;

        return userRepository.findByUsername(userDetails.getUsername());
    }

    @Override
    public User updateProfile(RemoteClientInfo clientInfo, User user) {

        User found = getProfile();

        if (found == null)
            throw new BackendException(Constants.ErrorCode.USERNAME_NOT_EXIST);

        if (!found.getUsername().equals(user.getUsername()))
            throw new BackendException(Constants.ErrorCode.USERNAME_NOT_EXIST);

        if (!SecurityUtility.hasRole(found, Constants.ROLE_ADMIN) )
            throw new BackendException(Constants.ErrorCode.NOT_AVAILABLE_FUNCTION);

        String oldInfo = getUserInfo(found);

        found.setFullname(user.getFullname());

        userRepository.save(found);

        String newInfo = getUserInfo(found);

        actionLogService.log(Constants.Object.PROFILE, Constants.Action.UPDATE,
                "Old Info: [" + oldInfo + "]" + "\n"
                        + "New Info: [" + newInfo + "]", clientInfo.getIp());

        return user;

    }

    @Override
    public void changePassword(RemoteClientInfo clientInfo, String oldPassword, String newPassword) {
        User found = getProfile();

        if (found == null)
            throw new BackendException(Constants.ErrorCode.USERNAME_NOT_EXIST);

        String cryptedOldPassword = passwordEncoder.encode(oldPassword);
        if (!found.getPassword().equals(cryptedOldPassword))
            throw new BackendException(Constants.ErrorCode.PASSWORD_INVALID);

        String cryptedNewPassword = passwordEncoder.encode(newPassword);

        found.setPassword(cryptedNewPassword);
        userRepository.save(found);

        actionLogService.log(Constants.Object.PROFILE, Constants.Action.CHANGE_PASSWORD, "", clientInfo.getIp());
    }

//    @Override
//    public void resetPassword(RemoteClientInfo clientInfo, int id) {
//        User user = userRepository.findOne(id);
//
//        if (user == null)
//            throw new BackendException(Constants.ErrorCode.USERNAME_NOT_EXIST);
//
//        String info = getUserInfo(user);
//
//        user.setPassword(passwordEncoder.encode(retrieveResetPassword(user)));
//        userRepository.save(user);
//
//        actionLogService.log(Constants.Object.USER, Constants.Action.RESET_PASSWORD,
//                             "User Info: [" + info + "]", clientInfo.getIp());
//    }
//
//    @Override
//    public String retrieveResetPassword(User user) {
//        return user.getUsername() + "_1234";
//    }

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
}
