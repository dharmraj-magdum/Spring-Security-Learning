package com.madgum.spring_security_learning.config.filter;

import com.madgum.spring_security_learning.constant.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MyJWTTokenValidatorFilter extends OncePerRequestFilter {
    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(ApplicationConstants.JWT_HEADER);
        if (null != jwt) {
            jwt = jwt.startsWith("Bearer") ? jwt.substring(7) : jwt;
            try {
                Environment env = getEnvironment();
                if (null != env) {
                    //get secret from environment and hash it using same algo used while generating
                    String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                    if (null != secretKey) {
                        //we mention our secret to Jwts lib like Jwts.parser().verifyWith(secretKey)
                        //and call for build().parseSignedClaims(jwt).getPayload() get the claims if token is verified.
                        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();
                        String username = String.valueOf(claims.get("username"));
                        String authorities = String.valueOf(claims.get("authorities"));
                        //as we got valid token, we direclty create new authentication object with authenticated true with details in token
                        List<GrantedAuthority> userAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, userAuthorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }

            } catch (Exception exception) {
                throw new BadCredentialsException("Invalid Token received!");
            }
        }
        filterChain.doFilter(request, response);
    }

    //do not use this filter while /user as its our login attempt we cannot validate toke before login
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/user");
    }

}