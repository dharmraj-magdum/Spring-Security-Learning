package com.madgum.spring_security_learning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfo {

    private long id;
    private String email;
    private String role;
    private String contact;
}
