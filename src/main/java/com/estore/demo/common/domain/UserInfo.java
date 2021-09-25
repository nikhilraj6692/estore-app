package com.estore.demo.common.domain;


import org.springframework.data.mongodb.core.mapping.Document;

/*
POJO class to hold jwt principal and authority. Since, LDAP is not present, so it is used to save in USerInfo collection
 */
@Document(collection = "UserInfo")
public class UserInfo {

    private String id;
    private String username;
    private String password;
    private String roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}