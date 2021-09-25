package com.estore.demo.common.context;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface LoggedInUser {

    public String getUserId();

    public String getUserName();

    public Collection<? extends GrantedAuthority> getAssignedRoles();

    String getCorrelationId();
}
