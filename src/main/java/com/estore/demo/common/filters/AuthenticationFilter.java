package com.estore.demo.common.filters;

import com.estore.demo.common.domain.UserCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
Authentication filter to authenticate user name, password and generate a Bearer token
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenGenerationUtil tokenGenerationUtil;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.tokenGenerationUtil = new TokenGenerationUtil();
        setFilterProcessesUrl("/login");
    }

    /*
    Authenticates username, password against users present in database
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserCredentials user = new ObjectMapper()
                    .readValue(request.getInputStream(), UserCredentials.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /*
    creates JWT token on succesful authentication
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) {
        tokenGenerationUtil.addAuthentication(response, authentication);
    }
}
