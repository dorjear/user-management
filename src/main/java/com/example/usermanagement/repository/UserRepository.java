package com.example.usermanagement.repository;

import com.example.usermanagement.service.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
