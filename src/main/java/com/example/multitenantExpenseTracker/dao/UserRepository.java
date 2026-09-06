//package com.example.multitenantExpenseTracker.dao;

//public interface UserRepository {
    package com.example.multitenantExpenseTracker.dao; // same package as ExpenseRepo

import com.example.multitenantExpenseTracker.model.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

    public interface UserRepository extends JpaRepository<user, Long> {
        Optional<user> findByUsername(String username);
    }

