package com.example.vehicleservice.service;

import com.example.vehicleservice.entity.User;
import com.example.vehicleservice.exception.MyException;
import com.example.vehicleservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// The service layer has the "rules" of the project.
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // A new person who registers on the website is always a CUSTOMER
    public User register(User user) {
        user.setEmail(user.getEmail().trim().toLowerCase());

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new MyException("This email is already registered");
        }

        user.setRole("CUSTOMER");
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        if (email == null || password == null) {
            throw new MyException("Enter your email and password");
        }

        User user = userRepository.findByEmail(email.trim().toLowerCase());

        if (user == null || !user.getPassword().equals(password)) {
            throw new MyException("Wrong email or password");
        }
        return user;
    }

    // The admin needs the list of mechanics to choose from
    public List<User> getMechanics() {
        return userRepository.findByRole("MECHANIC");
    }
}
