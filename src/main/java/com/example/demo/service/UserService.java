package com.example.demo.service;


import com.example.demo.model.User;
import com.example.demo.model.dto.SignInDto;
import com.example.demo.model.dto.UserDto;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface UserService {

    UserDto saveCustomer(SignInDto user) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    UserDto getUserById(Long userId);

    UserDto saveUser(User user);

    User getSavedUserById(Long userId);

    User getUserByUsername(String username);

}