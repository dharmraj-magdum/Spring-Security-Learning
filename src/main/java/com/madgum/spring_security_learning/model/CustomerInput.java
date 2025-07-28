package com.madgum.spring_security_learning.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerInput {

    private long id;
    private String email;
}
