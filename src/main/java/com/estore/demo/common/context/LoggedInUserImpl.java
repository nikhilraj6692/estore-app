package com.estore.demo.common.context;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/*
Class holds logged in user header values per request scope. Facilitates the ease of getting headers by autowiring in any spring
managed bean
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
public class LoggedInUserImpl implements LoggedInUser {

    private UsernamePasswordAuthenticationToken authentication;

    public LoggedInUserImpl() {
        this.authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getAttributes() {
        return (Map<String, Object>) this.authentication.getDetails();
    }

    @Override
    public String getUserName() {
        return this.authentication.getName().toString();
    }

    @Override
    public String getUserId() {
        Map<String, Object> details = this.getAttributes();
        String userId = (String) details.get("userId");
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAssignedRoles() {
        return this.authentication.getAuthorities();
    }

    @Override
    public String getCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
