package com.example.demo.controller;

import com.example.demo.model.dto.SignInDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.example.demo.controller.AuthenticationController.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/api/v1/user")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public UserDto registerUser(@RequestBody SignInDto newUser) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return userService.saveCustomer(newUser);
    }

    @GetMapping("/find-user/{userId}")
    public UserDto getUserById(@PathVariable Long userId, @RequestHeader(AUTHORIZATION_HEADER) Optional<String> bearerToken) {
        authenticationService.authenticateUser(bearerToken);
        return userService.getUserById(userId);
    }

}