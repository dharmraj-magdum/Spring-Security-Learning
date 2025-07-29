package com.madgum.spring_security_learning.config.filter;

import com.madgum.spring_security_learning.constant.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.*;
import org.springframework.core.env.*;
import org.springframework.security.core.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.*;
import org.springframework.web.*;
import org.springframework.web.filter.*;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.Collectors;

public class MyJWTTokenGeneratorFilter extends OncePerRequestFilter {

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //we first get the details of user using securityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //only if present we can generate token for it
        if (null != authentication) {
            Environment env = getEnvironment();
            if (null != env) {
                //get the secretes from environment variables
                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                        ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                //hash teh secrete by using library
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                //start building JWT with adding contextual details like issue and sub etc
                //also put claims, here we want to put the username and authorities (',' separated string) of logged-in user.
                String jwt = Jwts.builder().issuer("Eazy Bank").subject("JWT Token")
                        .claim("username", authentication.getName())
                        .claim("authorities", authentication.getAuthorities().stream().map(
                                GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                        .issuedAt(new Date())
                        .expiration(new Date((new Date()).getTime() + 30000000))
                        .signWith(secretKey).compact();
                //make current timestamp as issuredAt and expiry of 30000000 milliseconds
                //signwith our secrete, the JWT builder library take care of salts, signature and all
                //we directly get string JWT , we add it to response.
                response.setHeader(ApplicationConstants.JWT_HEADER, jwt);
            }
        }
        filterChain.doFilter(request, response);
    }


    /*
    we can override shouldNotFilter which return boolean when filter chain call it before calling our filter
     here we instruc the filter chain that do not use this filter except, if request not come for /user as its our login page
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/user");
    }

}
