package de.gimik.apps.parsehub.backend.web.viewmodel.user;

import java.util.Map;

/**
 * Created by GIMIK10 on 9/1/2014.
 */
public class UserInputInfo extends UserViewInfo {

    public UserInputInfo() { }

    public UserInputInfo(String username, String password, String fullname, Map<String, Boolean> roles) {
        super(username, fullname, roles);

        this.password = password;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
