package de.gimik.apps.parsehub.backend.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import de.gimik.apps.parsehub.backend.model.User;
import de.gimik.apps.parsehub.backend.util.Constants;

import java.util.Collection;

public final class SecurityUtility {
    public static boolean hasRole(Authentication authentication, String role){
        if (authentication == null){
                return false;
        }

        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();

        if (grantedAuthorities == null || grantedAuthorities.size() <= 0){
                return false;
        }

        for(GrantedAuthority grantedAuthority : grantedAuthorities){
                if (grantedAuthority.getAuthority().equalsIgnoreCase(role)){
                        return true;
                }
        }

        return false;
    }

    public static boolean hasRole(User user, String role){
        if (user == null){
            return false;
        }

        Collection<? extends GrantedAuthority> grantedAuthorities = user.getRoles();

        if (grantedAuthorities == null || grantedAuthorities.size() <= 0){
            return false;
        }

        for(GrantedAuthority grantedAuthority : grantedAuthorities){
            if (grantedAuthority.getAuthority().equalsIgnoreCase(role)){
                return true;
            }
        }

        return false;
    }

    public static final boolean isAdminRole(Authentication authentication){
        return hasRole(authentication, Constants.ROLE_ADMIN);
    }


 

    public static final boolean isCurrentUserAdmin(){
        return isAdminRole(SecurityContextHolder.getContext().getAuthentication());
    }


    public static final User getCurrentUser(){
        try{
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception ex){
            return null;
        }
    }
}