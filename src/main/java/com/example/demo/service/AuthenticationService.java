package com.example.demo.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface AuthenticationService {

    String simpleHashFunction(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException;

    String signIn(String authHeader);

    void authenticateUser(Optional<String> bearerToken);

}
