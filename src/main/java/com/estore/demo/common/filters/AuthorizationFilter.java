package com.estore.demo.common.filters;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
Authorization filter to validate Bearer token and set context in Spring security context
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /*
    validates JWT token and sets up spring security context
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        Claims claims = new TokenGenerationUtil().getClaims(request);

        if (null != claims.get("username")) {
            setUpSpringAuthentication(claims);
        } else {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

    private void setUpSpringAuthentication(Claims claims) {
        String username = (String) claims.get("username");
        List<Map<String, String>> authorities = (List<Map<String, String>>) claims.get("roles");

        List<String> roles = new ArrayList<>();
        for (Map<String, String> authority : authorities) {
            roles.add(authority.get("authority"));
        }
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
                roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        auth.setDetails(claims);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
