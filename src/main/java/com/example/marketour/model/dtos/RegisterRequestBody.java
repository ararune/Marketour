package com.example.marketour.model.dtos;

import com.example.marketour.model.entities.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequestBody {
    private final String username;
    private final String password;
    private final UserType userType;
}
