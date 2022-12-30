package de.gimik.apps.parsehub.backend.web.RESTful;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.model.Role;
import de.gimik.apps.parsehub.backend.model.User;
import de.gimik.apps.parsehub.backend.security.DefaultUserDetails;
import de.gimik.apps.parsehub.backend.service.ProfileService;
import de.gimik.apps.parsehub.backend.service.RemoteClientInfo;
import de.gimik.apps.parsehub.backend.service.RoleService;
import de.gimik.apps.parsehub.backend.service.UserService;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.web.viewmodel.PageInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.TransferHelper;
import de.gimik.apps.parsehub.backend.web.viewmodel.user.ChangePasswordInput;
import de.gimik.apps.parsehub.backend.web.viewmodel.user.UserInputInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.user.UserViewInfo;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Path("/profile")
public class ProfileResource {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RoleService roleService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserViewInfo updateProfile(@Context HttpServletRequest request, UserInputInfo userInfo) {
        User user = userService.findByUsername(userInfo.getUsername());

        if (user == null)
            throw new BackendException(Constants.ErrorCode.USERNAME_NOT_EXIST);

        user.setFullname(userInfo.getFullname());

        if (profileService.updateProfile(new RemoteClientInfo(request), user) != null)
            return new UserViewInfo(user.getUsername(), user.getFullname(), TransferHelper.createRoleMap(user.getRoles()));
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserViewInfo getProfile() {
        User user = profileService.getProfile();

        if (user == null) {
            throw new WebApplicationException(401);
        }

        return new UserViewInfo(user.getUsername(), user.getFullname(), TransferHelper.createRoleMap(user.getRoles()));
    }

    @Path("changePassword")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserViewInfo changePassword(@Context HttpServletRequest request, ChangePasswordInput passwordInput) {
        profileService.changePassword(new RemoteClientInfo(request),
                                             passwordInput.getPassword(), passwordInput.getNewPassword());

        return getProfile();
    }
}
