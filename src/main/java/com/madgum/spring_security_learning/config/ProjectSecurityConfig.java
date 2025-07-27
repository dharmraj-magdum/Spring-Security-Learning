package com.madgum.spring_security_learning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrfConfig -> csrfConfig.disable()).
                authorizeHttpRequests((requests) ->
                        requests.requestMatchers("/myBalance").authenticated()
                                .requestMatchers( "/error", "/register", "/hello").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    /*@Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * From Spring Security 6.3 version
     *
     * @return
     */
//    @Bean
//    public CompromisedPasswordChecker compromisedPasswordChecker() {
//        return new HaveIBeenPwnedRestApiPasswordChecker();
//    }

    /*
    For our custom authentication logic, we need to provide bean of auth-manager/auth-provider
    We don't need to add any extra filter for custom authentication as there are already filter present
    to do same they just use default authentication manager if we create bean of it,
    Actually the  authentication manager iterate though all the providers added to it.
    it tries to successfully authenticate user by one of the auth-providers
    as we pass our custom auth-provider it authenticate user per our logic
    wherever req comes to one of security filter like UsernamePasswordAuthenticationFilter, BasicAuthFilter etc
    they use this AuthenticationManager and ask it is the user is authenticated or not
    AuthenticationManager intern use our custom authenticationProvider making custom auth flow
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        MyAuthenticationProvider authenticationProvider = new MyAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

}