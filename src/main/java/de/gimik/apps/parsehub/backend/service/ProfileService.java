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

package de.gimik.apps.parsehub.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import de.gimik.apps.parsehub.backend.model.User;

import java.util.Map;

/**
 *
 * @author dang
 */
public interface ProfileService {
    User getProfile();
    User updateProfile(RemoteClientInfo clientInfo, User user);
    void changePassword(RemoteClientInfo clientInfo, String oldPassword, String newPassword);

//    void resetPassword(RemoteClientInfo clientInfo, int id);
//    String retrieveResetPassword(User user);
}
