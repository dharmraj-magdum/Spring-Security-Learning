package com.madgum.spring_security_learning.controller;

import com.madgum.spring_security_learning.dao.CustomerRepository;
import com.madgum.spring_security_learning.model.Customer;
import com.madgum.spring_security_learning.model.CustomerInfo;
import com.madgum.spring_security_learning.model.CustomerInput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            Customer savedCustomer = customerRepository.save(customer);

            if(savedCustomer.getId()>0) {
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("Given user details are successfully registered");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("User registration failed");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("An exception occurred: " + ex.getMessage());
        }

    }

    @GetMapping("/get-user-info")
    public ResponseEntity<Object> registerUser() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Customer customer = customerRepository.findByEmail(email).get();

            CustomerInfo customerInfo = new CustomerInfo();
            if(customer.getId()>0) {
                customerInfo.setId(customer.getId());
                customerInfo.setRole(customer.getRole());
                customerInfo.setEmail(customer.getEmail());
                customerInfo.setContact("something");
                return ResponseEntity.status(HttpStatus.OK).
                        body(customerInfo);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("User search failed");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("An exception occurred: " + ex.getMessage());
        }

    }

}