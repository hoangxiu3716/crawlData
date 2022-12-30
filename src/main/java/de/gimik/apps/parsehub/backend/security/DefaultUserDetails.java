package de.gimik.apps.parsehub.backend.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.gimik.apps.parsehub.backend.model.Role;
import de.gimik.apps.parsehub.backend.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by dang on 30.08.2014.
 */
public class DefaultUserDetails implements UserDetails {
    private User user;

    
    public DefaultUserDetails(User user) {
		super();
		this.user = user;
	}

	public List<Role> getRoles() {
        return user == null ? null : user.getRoles();
    }

    public String getFullname() {
        return user == null ? null : user.getFullname();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user == null ? null : user.getRoles();
    }

    @Override
    public String getPassword() {
        return user == null ? null : user.getPassword();
    }

    @Override
    public String getUsername() {
        return user == null ? null : user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
