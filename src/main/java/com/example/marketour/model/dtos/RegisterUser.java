package com.example.marketour.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterUser {
    private final String username;
    private final String password;
    private final String city;
    private final String country;
    private final String userType;
}
