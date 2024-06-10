package com.example.demo.service.impl;

import com.example.demo.exception.AuthorizationException;
import com.example.demo.model.User;
import com.example.demo.model.UserSession;
import com.example.demo.repository.UserSessionRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private JwtUtils jwtUtils;

    @Lazy // To prevent Circular dependency
    @Autowired
    private UserService userService;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Override
    public String simpleHashFunction(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();

        StringBuilder hexStr = new StringBuilder();
        for (byte b : digest) {
            hexStr.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return hexStr.toString();
    }

    @Override
    public String signIn(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Basic")) {
            // Extract the Base64 encoded username and password
            String base64Credentials = authHeader.substring("Basic".length()).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes);

            // credentials format is "username:password"
            String[] values = credentials.split(":", 2);

            String username = values[0];
            String password = values[1];

            User user = userService.getUserByUsername(username);

            try {
                String hashedPassword = simpleHashFunction(password);
                if (hashedPassword.equals(user.getHashedPassword())) {

                    String token = jwtUtils.generateToken(username);

                    UserSession newSession = new UserSession();
                    newSession.setToken(token);
                    newSession.setUserId(user.getUserId());
                    newSession.setCreatedAt(LocalDateTime.now());
                    userSessionRepository.save(newSession);

                    return token;
                }
                return "Wrong Username or password";
            } catch (Exception e) {
                throw new RuntimeException("Error with hashing password " + e.toString());
            }

        } else {
            return "Authorization header is missing or not Basic.";
        }
    }

    public void authenticateUser(Optional<String> bearerToken) {
        try {
            if (bearerToken.isPresent()) { // If present validate Token
                String token = bearerToken.get().substring(7); // Excludes "Bearer "
                String username = jwtUtils.extractUsername(token);
                User user = userService.getUserByUsername(username);
                boolean tokenIsValid = jwtUtils.validateToken(token, user);
                if (!tokenIsValid)
                    throw new AuthorizationException();
            } else { // If not present throw 401
                throw new AuthorizationException();
            }
        } catch (Exception e) {
            throw new AuthorizationException();
        }

    }

}
