package com.example.vehicleservice.controller;

import com.example.vehicleservice.entity.User;
import com.example.vehicleservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// A controller has the web addresses (URLs) that the React website calls.
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST http://localhost:8080/api/users/register
    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        return userService.register(user);
    }

    // POST http://localhost:8080/api/users/login
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.login(user.getEmail(), user.getPassword());
    }

    // GET http://localhost:8080/api/users/mechanics
    @GetMapping("/mechanics")
    public List<User> getMechanics() {
        return userService.getMechanics();
    }
}
