package com.example.pdponline.repository;

import com.example.pdponline.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.phoneNumber = ?1 and u.enabled = true")
    User getUserAndEnabledTrue(String phone);


    Optional<User> findByPhoneNumber(String phoneNumber);


    Optional<User> findByPhoneNumberAndEnabledTrue(String phoneNumber);
}
