package com.example.vehicleservice.repository;

import com.example.vehicleservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository already gives us save, findAll, findById, delete...
// We only write the extra searches we need. Spring writes the SQL from the method name.
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByRole(String role);
}
