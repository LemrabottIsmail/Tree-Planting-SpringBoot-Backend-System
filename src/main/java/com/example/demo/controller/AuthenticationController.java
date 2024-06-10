package com.example.demo.controller;

import com.example.demo.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private AuthenticationService authenticationService;


    @GetMapping("/sign-in")
    public String signIn(HttpServletRequest request) {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        return authenticationService.signIn(authHeader);

    }

}
