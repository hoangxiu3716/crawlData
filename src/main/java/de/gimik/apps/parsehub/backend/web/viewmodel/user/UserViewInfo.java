package de.gimik.apps.parsehub.backend.web.viewmodel.user;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;


public class UserViewInfo
{
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String username;

    private String fullname;
	private Map<String, Boolean> roles;
    public UserViewInfo() {}

	public UserViewInfo(String username, String fullname, Map<String, Boolean> roles)
	{
		this.username = username;
        this.fullname = fullname;
		this.roles = roles;
	}

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

	public Map<String, Boolean> getRoles()
	{
		return this.roles;
	}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setRoles(Map<String, Boolean> roles) {
        this.roles = roles;
    }

	public String getDisplayRoles() {
        return StringUtils.join(roles.keySet(), ", ")
                          .replace("[", "")
                          .replace("]", "");
    }
}