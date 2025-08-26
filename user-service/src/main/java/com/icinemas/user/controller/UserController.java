package com.icinemas.user.controller;

import com.icinemas.model.AuthResponse;
import com.icinemas.model.User;
import com.icinemas.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody User user) {
        AuthResponse jwtToken =  userService.authenticateUser(user);
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping()
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        return userService.registerUser(user);


    }
}
