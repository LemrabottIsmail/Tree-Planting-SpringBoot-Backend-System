package com.example.demo.service.impl;


import com.example.demo.exception.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.model.dto.SignInDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;


    @Override
    public UserDto saveCustomer(SignInDto user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        // Request Body
        /*
        {
            "firstName": "Dana",
            "lastName": "Dana",
            "username": "danaxx1",
            "password": "danaPass"
        }
         */

        User newUser = new User(); // Making a new User Entity
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setHashedPassword(authenticationService.simpleHashFunction(user.getPassword()));
        newUser.setUsername(user.getUsername());



        User savedUser = userRepository.save(newUser);

        // Make a new UserDto class to prevent sharing sensitive information like in our case; hashedPassword

        return saveUser(newUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User entityUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User", "Id", userId));

        UserDto user = new UserDto();
        user.setFirstName(entityUser.getFirstName());
        user.setLastName(entityUser.getLastName());
        user.setUsername(entityUser.getUsername());
        user.setUserId(entityUser.getUserId());
        user.setCreatedAt(entityUser.getCreatedAt());
        user.setUpdatedAt(entityUser.getUpdatedAt());
        user.setPoints(entityUser.getPoints());

        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username).orElseThrow(() -> new NotFoundException("User", "Name", username));
    }

    @Override
    public User getSavedUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User", "Id", userId));
    }

    @Override
    public UserDto saveUser(User user) {

        User savedUser = userRepository.save(user);

        UserDto userDto = new UserDto();

        userDto.setFirstName(savedUser.getFirstName());
        userDto.setLastName(savedUser.getLastName());
        userDto.setUsername(savedUser.getUsername());
        userDto.setUserId(savedUser.getUserId());
        userDto.setCreatedAt(savedUser.getCreatedAt());
        userDto.setUpdatedAt(savedUser.getUpdatedAt());
        userDto.setPoints(savedUser.getPoints());

        return userDto;
    }

}