package de.gimik.apps.parsehub.backend.model;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@JsonIgnoreProperties(value = {"roles"}, ignoreUnknown = true)
public class User implements Serializable {
    private static final long serialVersionUID = -1325531598075759936L;

    public static final String USER_NAME = "username";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullname;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> roles = new ArrayList<Role>();

//    @Column(unique = true, length = 255, nullable = true)
//    private String email;
//
//    @Column(columnDefinition = "BIT", length = 1)
//    private boolean enabled;

    public User() {
    }

    public User(String username, String passwordHash) {
        this.username = username;
        this.password = passwordHash;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
	public Collection<String> getAuthorities() {
        if (roles == null) {
            return Collections.emptyList();
        }

        return Collections2.transform(roles, new Function<Role, String>() {
            @Override
            public String apply(Role role) {
                return role.getAuthority();
            }
        });
    }

//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("ID=").append(this.getId()).append("|");
        sb.append("Username=").append(this.getUsername()).append("|");
        if (this.getFullname() != null) {
            sb.append("Fullname=").append(this.getFullname()).append("|");
        }

        String roles = StringUtils.join(this.getAuthorities(), ", ").replace("[", "")
                .replace("]", "");
        sb.append("Roles=").append(roles);


        return sb.toString();
    }
}
