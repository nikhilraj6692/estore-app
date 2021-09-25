package com.estore.demo.common.domain;

/*
POJO to parse login parameters sent in request payload in order to get Bearer token
 */
public class UserCredentials {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String _password) {
        this.password = _password;
    }
}
