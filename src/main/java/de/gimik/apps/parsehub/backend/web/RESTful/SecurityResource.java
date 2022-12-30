package de.gimik.apps.parsehub.backend.web.RESTful;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import de.gimik.apps.parsehub.backend.security.DefaultUserDetails;
import de.gimik.apps.parsehub.backend.service.RoleService;
import de.gimik.apps.parsehub.backend.service.UserService;
import de.gimik.apps.parsehub.backend.web.TokenUtils;
import de.gimik.apps.parsehub.backend.web.viewmodel.TokenInfo;
import de.gimik.apps.parsehub.backend.web.viewmodel.TransferHelper;
import de.gimik.apps.parsehub.backend.web.viewmodel.user.UserViewInfo;

@Component
@Path("/security")
public class SecurityResource {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authManager;

    
    /**
     * Retrieves the currently logged in user.
     *
     * @return A transfer containing the username and the roles.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserViewInfo getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && principal.equals("anonymousUser")) {
            throw new WebApplicationException(401);
        }
        DefaultUserDetails userDetails = (DefaultUserDetails) principal;

        return new UserViewInfo(userDetails.getUsername(), userDetails.getFullname(), TransferHelper.createRoleMap(userDetails.getAuthorities()));
    }

    /**
     * Authenticates a user and creates an authentication token.
     *
     * @param username The name of the user.
     * @param password The password of the user.
     * @return A transfer containing the authentication token.
     */
    @Path("authenticate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Pair<TokenInfo, TokenInfo> authenticate(@FormParam("username") String username, @FormParam("password") String password) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication;
        try {
            authentication = this.authManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            /*
            * Reload user as password of authentication principal will be null after authorization and
            * password is needed for token generation
            */
            UserDetails userDetails = this.userService.loadUserByUsername(username);
            TokenInfo token = new TokenInfo(TokenUtils.createToken(userDetails));

            DefaultUserDetails defaultUserDetails = (DefaultUserDetails)userDetails;
            Pair<TokenInfo, TokenInfo> result = null;
        	result = new ImmutablePair<TokenInfo, TokenInfo>(token, null);            
            return result;
        } catch (org.springframework.security.core.AuthenticationException ex) {
            //System.out.println(ex);
        }
        return null;
    }

}
