package com.estore.demo.common.filters;

import com.estore.demo.common.domain.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
Utility class to generate and validate JWT token
 */
public class TokenGenerationUtil {

    private final long EXPIRATIONTIME = 1000 * 60 * 60 * 24;
    private final String secret = "secret";
    private final String tokenPrefix = "Bearer";
    private final String headerString = "Authorization";

    /*
    Method to generate token
     */
    public void addAuthentication(HttpServletResponse response, Authentication authentication)
    {
        // We generate a token now.
        String JWT = Jwts.builder()
                .setSubject(authentication.getName())
                .setClaims(createClaims(authentication))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        response.addHeader(headerString,tokenPrefix + " "+ JWT);
    }

    /*
    Method to create user claims
     */
    private Map<String, Object> createClaims(Authentication authentication) {
        Map<String, Object> map = new HashMap<>();
        map.put("roles", authentication.getAuthorities());
        map.put("username", authentication.getName());
        map.put("userId", ((CustomUser)authentication.getPrincipal()).getId());
        return map;
    }

    /*
    Method to get claims from existing JWT token
     */
    public Claims getClaims(HttpServletRequest request)
    {
        String token = request.getHeader(headerString);
        if(token != null)
        {
            // remove Bearer
            token=token.substring(7);

            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token).getBody();
            return claims;
        }
        return null;
    }
}
