package com.cava.AuthService.repository;

import com.cava.AuthService.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Users findByName(String name);
    Users findByPhoneNumber(String phoneNumber);
}
