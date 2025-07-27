package com.madgum.spring_security_learning.config;

import com.madgum.spring_security_learning.dao.CustomerRepository;
import com.madgum.spring_security_learning.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    /*
    This is our custom implementation of UserDetailsService so the spring used it to load userInfo as per we say
    here we load the user from our Db
    in filter chain whichever filter uses the UserDetailsService to get user details
    and compare can decide the authentication status of use
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User details not found for the user: " + username));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
        return new User(customer.getEmail(), customer.getPwd(), authorities);
    }

}
