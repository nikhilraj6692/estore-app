package com.estore.demo.common.domain;

import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/*
POJO class to hold user claims present in JWT token
 */
public class CustomUser extends User {

    private static final long serialVersionUID = -3531439484732724601L;

    private final String id;

    public CustomUser(String username, String password, boolean enabled,
                      boolean accountNonExpired, boolean credentialsNonExpired,
                      boolean accountNonLocked,
                      Collection authorities,
                      String id) {

        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);

        this.id = id;
    }

    public String getId() {
        return id;
    }
}