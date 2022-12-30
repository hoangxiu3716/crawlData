package de.gimik.apps.parsehub.backend.web.RESTful;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.gimik.apps.parsehub.backend.model.Role;
import de.gimik.apps.parsehub.backend.model.User;
import de.gimik.apps.parsehub.backend.service.RemoteClientInfo;
import de.gimik.apps.parsehub.backend.service.RoleService;
import de.gimik.apps.parsehub.backend.service.UserService;
import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.web.viewmodel.PageInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.TransferHelper;
import de.gimik.apps.parsehub.backend.web.viewmodel.user.UserInputInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.user.UserViewInfo;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@Path("/manage/user")
public class UserManagementResource {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @DELETE
    @Path("{id}")
    public void delete(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        userService.delete(new RemoteClientInfo(request), id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserViewInfo create(@Context HttpServletRequest request, UserInputInfo userInfo) {
        User user = new User();

        user.setUsername(userInfo.getUsername());
        user.setPassword(userInfo.getPassword());
        user.setFullname(userInfo.getFullname());
        user.setRoles(parseRoles(userInfo));

        if (userService.addNewUser(new RemoteClientInfo(request), user) != null)
            return new UserViewInfo(user.getUsername(), user.getFullname(), TransferHelper.createRoleMap(user.getRoles()));
        return null;
    }

    private List<Role> parseRoles(UserInputInfo userInfo) {
       return parseRoles(userInfo, false);
    }
    
    private List<Role> parseRoles(UserInputInfo userInfo,boolean hasRoleEmployee) {
        if (userInfo.getRoles() != null) {
            List<Role> roles = new ArrayList<>();

            for (Map.Entry<String, Boolean> roleMapEntry : userInfo.getRoles().entrySet()) {
                if (roleMapEntry.getValue()) {
                    String roleName = roleMapEntry.getKey();
                    if(!hasRoleEmployee &&  (StringUtils.isEmpty(roleName) || roleName.equals(Constants.ROLE_EMPLOYEES)))
                		continue;
                    Role role = roleService.getByName(roleName);

                    if (role != null) {
                        roles.add(role);
                    }
                }
            }

            return roles;
        }

        return null;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public UserViewInfo update(@Context HttpServletRequest request, @PathParam("id") Long id, UserInputInfo userInfo) {
        User user = userService.findByUsername(userInfo.getUsername());

        if (user == null)
            throw new WebApplicationException(404);
        boolean hasRoleEmployee = false;
        for(Role role : user.getRoles()){
        	if(role.getName() != null &&  role.getName().equals(Constants.ROLE_CUSTOMER)){
        		hasRoleEmployee = true;
        		break;
        	}
        }
        user.setUsername(userInfo.getUsername());
        user.setFullname(userInfo.getFullname());
        user.setRoles(parseRoles(userInfo,hasRoleEmployee));

        if (userService.save(new RemoteClientInfo(request), user) != null)
            return new UserViewInfo(user.getUsername(), user.getFullname(), TransferHelper.createRoleMap(user.getRoles()));
        return null;
    }

    @Path("list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PageInfo list(
            @DefaultValue("0") @QueryParam("page") int pageIndex,
            @DefaultValue("10") @QueryParam("size") int pageSize,
            @QueryParam("field") String field,
            @QueryParam("direction") String direction,
            @QueryParam("filter") String filters
    ) throws JsonGenerationException, JsonMappingException {
        //List<User> list = userService.getAllByCurrentUser(pageIndex, pageSize);
        //System.out.println("filter: " + filters);
        Map<String, String> filter = null;
        if (!StringUtils.isEmpty(filters)) {
            filter = new Gson().fromJson(filters, new TypeToken<HashMap<String, String>>() {
            }.getType());
        }
        Page<User> page = userService.getAll(pageIndex, pageSize, field, direction, filter);

        return TransferHelper.convertToUserInfoPage(page);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public UserViewInfo read(@PathParam("id") int id) {
        User user = userService.getByID(id);

        if (user == null) {
            throw new WebApplicationException(404);
        }

        return new UserViewInfo(user.getUsername(), user.getFullname(), TransferHelper.createRoleMap(user.getRoles()));
    }

}
