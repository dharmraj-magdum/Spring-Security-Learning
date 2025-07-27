package com.madgum.spring_security_learning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfigBasic {

    /*
        this is main Bean for Spring Security, the core security use this bean to configure the security feature
        this most important part where all security and user authenticate/authorization logic come together to validate each and every http request.
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        //spring gives us the bean of HttpSecurity we have to configure it and build it at end to provide SecurityFilterChain

        //by default spring security provide a lot of basis and advance security feature like CSRF,CORS, login form and all
        //we disable those for our ease in development in rest API
        http.csrf(csrfConfig -> csrfConfig.disable());
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }
}
