package com.madgum.spring_security_learning.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confidential")
public class ConfidentialController {

    @GetMapping("/info")
    public ResponseEntity<Object> getConfidential() {
        return ResponseEntity.status(HttpStatus.OK).body("confidential access");
    }


    @GetMapping("/secret")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Object> getAdminSecret() {
        return ResponseEntity.status(HttpStatus.OK).body("Secret access (Only Admin)");
    }


}
