package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates Getters and Setters
@AllArgsConstructor
@NoArgsConstructor
public class SignInDto { // Both for Register and Login

    private String username; // Required for Login
    private String firstName;
    private String lastName;
    private String password; // Required for Login

}
