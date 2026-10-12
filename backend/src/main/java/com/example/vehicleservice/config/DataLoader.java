package com.example.vehicleservice.config;

import com.example.vehicleservice.entity.User;
import com.example.vehicleservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// This runs one time when the server starts.
// It creates the admin, two mechanics and one customer so you can log in and test.
@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            addUser("Admin", "admin@gmail.com", "admin123", "ADMIN");
            addUser("Ravi", "ravi@gmail.com", "mech123", "MECHANIC");
            addUser("Anil", "anil@gmail.com", "mech123", "MECHANIC");
            addUser("Sita", "sita@gmail.com", "cust123", "CUSTOMER");
        }
    }

    private void addUser(String name, String email, String password, String role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        userRepository.save(user);
    }
}
